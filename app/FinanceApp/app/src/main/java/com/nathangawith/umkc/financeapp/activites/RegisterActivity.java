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
import com.nathangawith.umkc.financeapp.dialogs.MyDialog;
import com.nathangawith.umkc.financeapp.dtos.TransactionDto;
import com.nathangawith.umkc.financeapp.http.MyApi;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private TextView lblTotal;
    private SwipeRefreshLayout swipeRefresh;
    // private TextView lblTempTransactions;
    ListView listTransactions;

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
        MyApi.getTotal(getApplicationContext(), resp -> {
            this.lblTotal.setText(String.format("Total: %s", resp.response));
        }, data -> MyUtility.okDialog(getSupportFragmentManager(), "Error", data.response));
        MyApi.getTransactions(getApplicationContext(), respCollection -> {
            ArrayList<RegisterEntry> transactionsList = new ArrayList<RegisterEntry>();
            for (TransactionDto transaction : respCollection) {
                transactionsList.add(new RegisterEntry(transaction));
            }
            RegisterEntryArrayAdapter arrayAdapter = new RegisterEntryArrayAdapter(this, 0, transactionsList);
            listTransactions.setAdapter(arrayAdapter);
            this.swipeRefresh.setRefreshing(false);
        }, data -> MyUtility.okDialog(getSupportFragmentManager(), "Error", data.response));
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
     * transfer button
     * @param view button view
     */
    public void btnFloatingTransferClick(View view) {
        MyUtility.okDialog(getSupportFragmentManager(), "Not Yet Implemented!", "This functionality will be added in a later version");
    }

    public void btnBackClick(View view) {
        startActivity(new Intent(this, MenuActivity.class));
    }
}
