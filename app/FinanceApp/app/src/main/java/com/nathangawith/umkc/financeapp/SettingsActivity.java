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

import java.util.function.BiConsumer;
import java.util.function.Consumer;

class GenericResponse {
    public String response;
}

public class SettingsActivity extends AppCompatActivity {

    private EditText txtAddAccount;
    private Button btnAddAccount;
    private EditText txtAddIncomeCategory;
    private Button btnAddIncomeCategory;
    private EditText txtAddExpenseCategory;
    private Button btnAddExpenseCategory;
    private Button btnBack;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.txtAddAccount = findViewById(R.id.txtAddAccount);
        this.btnAddAccount = findViewById(R.id.btnAddAccount);
        this.txtAddIncomeCategory = findViewById(R.id.txtAddIncomeCategory);
        this.btnAddIncomeCategory = findViewById(R.id.btnAddIncomeCategory);
        this.txtAddExpenseCategory = findViewById(R.id.txtAddExpenseCategory);
        this.btnAddExpenseCategory = findViewById(R.id.btnAddExpenseCategory);
        this.btnBack = findViewById(R.id.btnBack);
        this.progressBar = findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);
    }

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

    private EditText labelToClear;

    private Consumer<GenericResponse> okFunc = resp -> {
        this.loading(false);
        new MyDialog("Ok Response", resp.response).show(getSupportFragmentManager(), null);
        this.labelToClear.setText("");
    };

    private Consumer<GenericResponse> errFunc = data -> {
        this.loading(false);
        new MyDialog("Error Response", data.response).show(getSupportFragmentManager(), null);
    };

    public void btnAddAccountClick(View view) {
        this.labelToClear = this.txtAddAccount;
        String accountDescription = this.txtAddAccount.getText().toString();
        this.loading(true);
        MyApi.postAddAccount(getApplicationContext(), new GenericResponse(), accountDescription, okFunc, errFunc);
    }

    public void btnAddIncomeCategoryClick(View view) {
        this.labelToClear = this.txtAddIncomeCategory;
        String incomeCategoryDescription = this.txtAddIncomeCategory.getText().toString();
        this.loading(true);
        MyApi.postAddCategory(getApplicationContext(), new GenericResponse(), true, incomeCategoryDescription , okFunc, errFunc);
    }

    public void btnAddExpenseCategoryClick(View view) {
        this.labelToClear = this.txtAddExpenseCategory;
        String expenseCategoryDescription = this.txtAddExpenseCategory.getText().toString();
        this.loading(true);
        MyApi.postAddCategory(getApplicationContext(), new GenericResponse(), false, expenseCategoryDescription, okFunc, errFunc);
    }

    public void btnBackClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
