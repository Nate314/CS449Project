package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.components.CustomArrayAdapter;
import com.nathangawith.umkc.financeapp.components.CustomList;
import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.constants.MyState;
import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.dtos.TransactionDto;
import com.nathangawith.umkc.financeapp.http.MyApi;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private TextView lblTotal;
    // private TextView lblTempTransactions;
    ListView listTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // initialize fields to ui elements
        this.lblTotal = findViewById(R.id.lblTotal);
        // this.lblTempTransactions = findViewById(R.id.lblTempTransactions);
        this.listTransactions = findViewById(R.id.listTransactions);
        // populate info
        this.init();
    }

    private void init() {
        MyApi.getTotal(getApplicationContext(), resp -> {
            this.lblTotal.setText(String.format("Total: %s", resp.response));
        }, data -> MyUtility.okDialog(getSupportFragmentManager(), "Error", data.response));
        MyApi.getTransactions(getApplicationContext(), respCollection -> {
            ArrayList<CustomList> transactionsList = new ArrayList<CustomList>();
            for (TransactionDto transaction : respCollection) {
                int img = transaction.Amount < 0 ? R.drawable.expense_red : R.drawable.income_green;
                transactionsList.add(new CustomList(img, transaction.Description, "$" + transaction.Amount));
            }
            CustomArrayAdapter arrayAdapter = new CustomArrayAdapter(this, 0, transactionsList);
            listTransactions.setAdapter(arrayAdapter);
        }, data -> MyUtility.okDialog(getSupportFragmentManager(), "Error", data.response));
    }

    /**
     * income button
     * @param view button view
     */
    public void btnFloatingIncomeClick(View view) {
        MyState.SCREEN = MyConstants.INCOME;
        startActivity(new Intent(this, IncomeExpenseActivity.class));
    }

    /**
     * expense button
     * @param view button view
     */
    public void btnFloatingExpenseClick(View view) {
        MyState.SCREEN = MyConstants.EXPENSE;
        startActivity(new Intent(this, IncomeExpenseActivity.class));
    }

    /**
     * transfer button
     * @param view button view
     */
    public void btnFloatingTransferClick(View view) {
        System.out.println("--------------------------------");
        System.out.println("btnFloatingTransferClick");
        System.out.println("--------------------------------");
    }
}
