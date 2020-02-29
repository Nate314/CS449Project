package com.nathangawith.umkc.financeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class GenericResponse {
    public String response;
}

public class SettingsActivity extends AppCompatActivity {

    private EditText labelToClear;
    private EditText txtAddAccount;
    private Button btnAddAccount;
    private EditText txtAddIncomeCategory;
    private Button btnAddIncomeCategory;
    private EditText txtAddExpenseCategory;
    private Button btnAddExpenseCategory;
    private Button btnBack;
    private TextView lblAccounts;
    private TextView lblIncomeCategories;
    private TextView lblExpenseCategories;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // initialize fields to ui elements
        this.txtAddAccount = findViewById(R.id.txtAddAccount);
        this.btnAddAccount = findViewById(R.id.btnAddAccount);
        this.lblAccounts = findViewById(R.id.lblAccounts);
        this.txtAddIncomeCategory = findViewById(R.id.txtAddIncomeCategory);
        this.btnAddIncomeCategory = findViewById(R.id.btnAddIncomeCategory);
        this.lblIncomeCategories = findViewById(R.id.lblIncomeCategories);
        this.txtAddExpenseCategory = findViewById(R.id.txtAddExpenseCategory);
        this.btnAddExpenseCategory = findViewById(R.id.btnAddExpenseCategory);
        this.lblExpenseCategories = findViewById(R.id.lblExpenseCategories);
        this.btnBack = findViewById(R.id.btnBack);
        this.progressBar = findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);
        // populate info
        this.getAllAccounts();
        this.getAllCategories(true);
        this.getAllCategories(false);
    }

    /**
     * disable everything when loading
     * @param loading true if loading
     */
    private void loading(boolean loading) {
        boolean enabled = !loading;
        this.txtAddAccount.setEnabled(enabled);
        this.btnAddAccount.setEnabled(enabled);
        this.txtAddIncomeCategory.setEnabled(enabled);
        this.btnAddIncomeCategory.setEnabled(enabled);
        this.txtAddExpenseCategory.setEnabled(enabled);
        this.btnAddExpenseCategory.setEnabled(enabled);
        if (loading) {
            this.progressBar.setVisibility(View.VISIBLE);
            this.progressBar.setIndeterminate(true);
            this.progressBar.animate();
        } else {
            this.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * retrieves all accounts from the api and displays them on the screen
     */
    private void getAllAccounts() {
        MyApi.getAllAccounts(getApplicationContext(), respCollection -> {
            String accountLabel = "Accounts: ";
            for (DBAccount acc : respCollection) accountLabel += String.format("%s, ", acc.Description);
            this.lblAccounts.setText(accountLabel.substring(0, accountLabel.length() - 2));
        }, errFunc);
    }

    /**
     * retrieves all categories from the api and displays them on the screen
     * @param income true to retrieve income categories, false to retrieve expense categories
     */
    private void getAllCategories(boolean income) {
        MyApi.getAllCategories(getApplicationContext(), income, respCollection -> {
            String categoriesLabel = String.format("%s Categories: ", income ? "Income" : "Expense");
            for (DBCategory cat : respCollection)
                categoriesLabel += String.format("%s, ", cat.Description);
            (income ? this.lblIncomeCategories : this.lblExpenseCategories)
                    .setText(categoriesLabel.substring(0, categoriesLabel.length() - 2));
        }, errFunc);
    }

    /**
     * function used when an ok response is received from the api.
     * Stops loading and clears text input.
     */
    private Consumer<GenericResponse> okFunc = resp -> {
        this.loading(false);
        // new MyDialog("Ok Response", resp.response).show(getSupportFragmentManager(), null);
        this.labelToClear.setText("");
    };

    /**
     * function used when an error response is received from the api.
     * Displays error message in dialog.
     */
    private Consumer<GenericResponse> errFunc = data -> {
        this.loading(false);
        new MyDialog("Error Response", data.response).show(getSupportFragmentManager(), null);
    };

    /**
     * adds account via api call, and then re-retrieves accounts
     * @param view button view
     */
    public void btnAddAccountClick(View view) {
        this.labelToClear = this.txtAddAccount;
        String accountDescription = this.txtAddAccount.getText().toString();
        this.loading(true);
        MyApi.postAddAccount(getApplicationContext(), new GenericResponse(), accountDescription, resp -> {
            okFunc.accept(resp);
            this.getAllAccounts();
        }, errFunc);
    }

    /**
     * adds income category via api call, and then re-retrieves income categories
     * @param view button view
     */
    public void btnAddIncomeCategoryClick(View view) {
        this.labelToClear = this.txtAddIncomeCategory;
        String incomeCategoryDescription = this.txtAddIncomeCategory.getText().toString();
        this.loading(true);
        MyApi.postAddCategory(getApplicationContext(), new GenericResponse(), true, incomeCategoryDescription, resp -> {
            okFunc.accept(resp);
            this.getAllCategories(true);
        }, errFunc);
    }

    /**
     * adds expense category via api call, and then re-retrieves expense categories
     * @param view button view
     */
    public void btnAddExpenseCategoryClick(View view) {
        this.labelToClear = this.txtAddExpenseCategory;
        String expenseCategoryDescription = this.txtAddExpenseCategory.getText().toString();
        this.loading(true);
        MyApi.postAddCategory(getApplicationContext(), new GenericResponse(), false, expenseCategoryDescription, resp -> {
            okFunc.accept(resp);
            this.getAllCategories(false);
        }, errFunc);
    }

    /**
     * sends user back to the previous screen
     * @param view button view
     */
    public void btnBackClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
