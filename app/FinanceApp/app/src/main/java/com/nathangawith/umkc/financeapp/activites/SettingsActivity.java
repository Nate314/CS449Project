package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.http.MyApi;
import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.dtos.DBAccount;
import com.nathangawith.umkc.financeapp.dtos.DBCategory;
import com.nathangawith.umkc.financeapp.dtos.GenericResponse;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SettingsActivity extends AppCompatActivity {

    private EditText txtAddAccount;
    private EditText txtAddIncomeCategory;
    private EditText txtAddExpenseCategory;
    private Button btnAdd;
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
        this.btnAdd = findViewById(R.id.btnAdd);
        this.lblAccounts = findViewById(R.id.lblAccounts);
        this.txtAddIncomeCategory = findViewById(R.id.txtAddIncomeCategory);
        this.lblIncomeCategories = findViewById(R.id.lblIncomeCategories);
        this.txtAddExpenseCategory = findViewById(R.id.txtAddExpenseCategory);
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
        this.txtAddIncomeCategory.setEnabled(enabled);
        this.txtAddExpenseCategory.setEnabled(enabled);
        this.btnAdd.setEnabled(enabled);
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
     * function used when an error response is received from the api.
     * Displays error message in dialog.
     */
    private Consumer<GenericResponse> errFunc = data -> {
        this.loading(false);
        MyUtility.okDialog(this, "Error Response", data.response);
    };

    /**
     * adds account via api call, and then re-retrieves accounts
     */
    public void addAccount() {
        String accountDescription = this.txtAddAccount.getText().toString();
        this.loading(true);
        MyApi.postAddAccount(getApplicationContext(), new GenericResponse(), accountDescription, resp -> {
            this.loading(false);
            this.txtAddAccount.setText("");
            this.getAllAccounts();
        }, errFunc);
    }

    /**
     * adds income category via api call, and then re-retrieves income categories
     */
    public void addIncomeCategory() {
        String incomeCategoryDescription = this.txtAddIncomeCategory.getText().toString();
        this.loading(true);
        MyApi.postAddCategory(getApplicationContext(), new GenericResponse(), true, incomeCategoryDescription, resp -> {
            this.loading(false);
            this.txtAddIncomeCategory.setText("");
            this.getAllCategories(true);
        }, errFunc);
    }

    /**
     * adds expense category via api call, and then re-retrieves expense categories
     */
    public void addExpenseCategory() {
        String expenseCategoryDescription = this.txtAddExpenseCategory.getText().toString();
        this.loading(true);
        MyApi.postAddCategory(getApplicationContext(), new GenericResponse(), false, expenseCategoryDescription, resp -> {
            this.loading(false);
            this.txtAddExpenseCategory.setText("");
            this.getAllCategories(false);
        }, errFunc);
    }

    /**
     * adds expense category via api call, and then re-retrieves expense categories
     * @param view button view
     */
    public void btnAddClick(View view) {
        boolean noneAdded = true;
        if (!this.txtAddAccount.getText().toString().equals("")) {
            this.addAccount();
            noneAdded = false;
        }
        if (!this.txtAddIncomeCategory.getText().toString().equals("")) {
            this.addIncomeCategory();
            noneAdded = false;
        }
        if (!this.txtAddExpenseCategory.getText().toString().equals("")) {
            this.addExpenseCategory();
            noneAdded = false;
        }
        if (noneAdded){
            MyUtility.okDialog(this, "Please enter:", "an Account, an Income Category, or an Expense Category");
        }
    }

    /**
     * sends user back to the previous screen
     * @param view button view
     */
    public void btnBackClick(View view) {
        startActivity(new Intent(this, MenuActivity.class));
    }
}
