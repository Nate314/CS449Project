package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.components.RegisterEntryArrayAdapter;
import com.nathangawith.umkc.financeapp.components.RegisterEntry;
import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.constants.MyState;
import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.dtos.TransactionDto;
import com.nathangawith.umkc.financeapp.http.MyApi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegisterActivity extends AppCompatActivity {

    private TextView lblTotal;
    private SwipeRefreshLayout swipeRefresh;
    private ListView listTransactions;
    private List<TransactionDto> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // initialize fields to ui elements
        RegisterActivity me = this;
        this.lblTotal = findViewById(R.id.lblTotal);
        this.swipeRefresh = findViewById(R.id.swipeRefresh);
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                me.init();
            }
        });
        this.listTransactions = findViewById(R.id.listTransactions);
        // populate info
        this.init();
    }

    private void init() {
        MyState.EDITING_TRANSACTION = null;
        MyApi.getTotal(getApplicationContext(), resp -> {
            this.lblTotal.setText(String.format("Total: %s", resp.response));
        }, data -> MyUtility.okDialog(this, "Error", data.response));
        MyApi.getTransactions(getApplicationContext(), respCollection -> {
            this.transactions = respCollection.stream().collect(Collectors.toList());
            ArrayList<RegisterEntry> transactionsList = new ArrayList<RegisterEntry>();
            for (TransactionDto transaction : respCollection) {
                transactionsList.add(new RegisterEntry(transaction));
            }
            RegisterEntryArrayAdapter arrayAdapter = new RegisterEntryArrayAdapter(this, this, 0, transactionsList);
            listTransactions.setAdapter(arrayAdapter);
            this.swipeRefresh.setRefreshing(false);
        }, data -> MyUtility.okDialog(this, "Error", data.response));
    }

    /**
     * income button
     * @param view button view
     */
    public void btnFloatingIncomeClick(View view) {
        MyState.SCREEN = MyConstants.INCOME;
        MyState.LAST_SCREEN = MyConstants.REGISTER;
        startActivity(new Intent(this, IncomeExpenseActivity.class));
    }

    /**
     * expense button
     * @param view button view
     */
    public void btnFloatingExpenseClick(View view) {
        MyState.SCREEN = MyConstants.EXPENSE;
        MyState.LAST_SCREEN = MyConstants.REGISTER;
        startActivity(new Intent(this, IncomeExpenseActivity.class));
    }

    /**
     * account transfer button
     * @param view button view
     */
    public void btnFloatingAccountTransferClick(View view) {
        MyState.SCREEN = MyConstants.TRANSFER_ACCOUNT;
        MyState.LAST_SCREEN = MyConstants.REGISTER;
        MyUtility.goToActivity(this, IncomeExpenseActivity.class);
    }

    /**
     * category transfer button
     * @param view button view
     */
    public void btnFloatingCategoryTransferClick(View view) {
        MyState.SCREEN = MyConstants.TRANSFER_CATEGORY;
        MyState.LAST_SCREEN = MyConstants.REGISTER;
        MyUtility.goToActivity(this, IncomeExpenseActivity.class);
    }

    public void btnBackClick(View view) {
        startActivity(new Intent(this, MenuActivity.class));
    }

    public void btnEditClick(RegisterEntry entry) {
        MyState.SCREEN = entry.getTransactionType();
        MyState.LAST_SCREEN = MyConstants.REGISTER;
        TransactionDto editing_transaction = this.transactions.stream().filter(x -> x.TransactionID == entry.getTransactionID()).findFirst().orElse(null);
        if (editing_transaction != null) {
            MyState.EDITING_TRANSACTION = editing_transaction;
            MyUtility.goToActivity(this, IncomeExpenseActivity.class);
        } else {
            MyUtility.okDialog(this, "ERROR", "Could not find transaction to edit");
        }
    }

    public void btnDeleteClick(RegisterEntry entry) {
        MyUtility.yesnoDialog(this, "Are you sure you want to delete this transaction?", "", yesNoResponse -> {
            if (yesNoResponse) {
                MyApi.deleteTransaction(this, entry.getTransactionType(), entry.getTransactionID(),
                    x -> {
                        MyUtility.okDialog(this, "SUCCESS");
                        this.init();
                    },
                    x -> MyUtility.okDialog(this, "ERROR")
                );
            }
        });
    }
}
