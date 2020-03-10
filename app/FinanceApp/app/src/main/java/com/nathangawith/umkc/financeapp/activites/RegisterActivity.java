package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.dialogs.MyDialog;
import com.nathangawith.umkc.financeapp.dtos.TransactionDto;
import com.nathangawith.umkc.financeapp.http.MyApi;

public class RegisterActivity extends AppCompatActivity {

    private TextView lblTotal;
    private TextView lblTempTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // initialize fields to ui elements
        this.lblTotal = findViewById(R.id.lblTotal);
        this.lblTempTransactions = findViewById(R.id.lblTempTransactions);
        // populate info
        this.init();
    }

    private void init() {
        MyApi.getTotal(getApplicationContext(), resp -> {
            this.lblTotal.setText(String.format("Total: %s", resp.response));
        }, data -> MyUtility.okDialog(getSupportFragmentManager(), "Error", data.response));
        MyApi.getTransactions(getApplicationContext(), respCollection -> {
            StringBuilder text = new StringBuilder("[");
            for (TransactionDto transaction : respCollection) {
                text.append(String.format("%s: %s\n", "Description", transaction.Description));
                text.append(String.format("%s: %s\n", "Account", transaction.AccountDescription));
                text.append(String.format("%s: %s\n", "Category", transaction.CategoryDescription));
                text.append(String.format("%s: %s\n", "Amount", transaction.Amount));
                text.append(String.format("%s: %s\n", "Date", transaction.Date));
                text.append("\n\n");
            }
            text.append("]");
            this.lblTempTransactions.setText(text.toString());
        }, data -> MyUtility.okDialog(getSupportFragmentManager(), "Error", data.response));
    }
}
