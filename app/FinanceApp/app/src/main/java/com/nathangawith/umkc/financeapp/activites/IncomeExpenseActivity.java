package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.constants.MyState;
import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.dtos.DBAccount;
import com.nathangawith.umkc.financeapp.dtos.DBCategory;
import com.nathangawith.umkc.financeapp.dtos.DBTransaction;
import com.nathangawith.umkc.financeapp.http.MyApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;

public class IncomeExpenseActivity extends AppCompatActivity {

    // ui elements
    private TextView lblScreenName;
    private EditText txtAmount;
    private EditText txtDescription;
    private Spinner spinnerAccount;
    private Spinner spinnerCategory;
    private Button btnSelectDate;
    private TextView lblDate;
    private ProgressBar progressBar;
    private Button btnSubmit;
    // private fields
    private boolean income;
    private DBAccount selectedAccount;
    private DBCategory selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expense);
        // initialize fields to ui elements
        this.txtAmount = findViewById(R.id.txtAmount);
        this.txtDescription = findViewById(R.id.txtDescription);
        this.spinnerAccount = findViewById(R.id.spinnerAccount);
        this.spinnerCategory = findViewById(R.id.spinnerCategory);
        this.btnSelectDate = findViewById(R.id.btnSelectDate);
        this.lblDate = findViewById(R.id.lblDate);
        this.btnSubmit = findViewById(R.id.btnSubmit);
        this.progressBar = findViewById(R.id.progressBar);
        this.lblScreenName = findViewById(R.id.lblScreenName);
        this.btnSelectDate.setOnClickListener(v -> MyUtility.btnDateClick(this, this.lblDate));
        this.init();
    }

    public void init() {
        // initialize ui elements
        this.progressBar.setVisibility(View.INVISIBLE);
        this.income = MyState.SCREEN != null ? MyState.SCREEN.equals(MyConstants.INCOME) : false;
        this.lblScreenName.setText(this.income ? "Income" : "Expense");
        // get accounts categories from api
        MyApi.getAllAccounts(getApplicationContext(),
                respCollection -> this.setSpinnerItems(this.spinnerAccount, DBAccount.class, respCollection, account -> this.selectedAccount = account),
                x -> MyUtility.okDialog(this, "Error", x.response));
        MyApi.getAllCategories(getApplicationContext(), income,
                respCollection -> this.setSpinnerItems(this.spinnerCategory, DBCategory.class, respCollection, category -> this.selectedCategory = category),
                x -> MyUtility.okDialog(this, "Error", x.response));
    }

    public void clearFields() {
        this.txtAmount.setText("");
        this.txtDescription.setText("");
        this.spinnerAccount.getOnItemSelectedListener().onItemSelected(null, null, 0, 0);
        this.spinnerCategory.getOnItemSelectedListener().onItemSelected(null, null, 0, 0);
        this.lblDate.setText("");
    }

    /**
     * @param spinner spinner to set the items on
     * @param type type of the respCollection items
     * @param respCollection collection of items used for spinner
     * @param onSelection when an item is selected, this function is called
     * @param <T> DBAccount or DBCategory
     */
    private <T extends Object> void setSpinnerItems(Spinner spinner, Class<T> type, Collection<T> respCollection, Consumer<T> onSelection) {
        try {
            ArrayList<T> respArrayList = new ArrayList<T>(respCollection);
            ArrayList<String> descriptions = new ArrayList<String>();
            for (T x : respArrayList) descriptions.add(type.getField("Description").get(x).toString());
            MyUtility.initializeSpinner(this, spinner, descriptions, new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    onSelection.accept(respArrayList.get((int) id));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * disable everything when loading
     * @param loading true if loading
     */
    private void loading(boolean loading) {
        boolean enabled = !loading;
        this.txtAmount.setEnabled(enabled);
        this.txtDescription.setEnabled(enabled);
        this.spinnerAccount.setEnabled(enabled);
        this.spinnerCategory.setEnabled(enabled);
        this.btnSelectDate.setEnabled(enabled);
        this.btnSubmit.setEnabled(enabled);
        if (loading) {
            this.progressBar.setVisibility(View.VISIBLE);
            this.progressBar.setIndeterminate(true);
            this.progressBar.animate();
        } else {
            this.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void sendToLastActivity() {
        if (MyState.LAST_SCREEN == MyConstants.MENU) {
            startActivity(new Intent(this, MenuActivity.class));
        } else if (MyState.LAST_SCREEN == MyConstants.REGISTER) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    /**
     * submit transaction
     * @param view button view
     */
    public void btnSubmitClick(View view) {
        boolean allRequiredFields = true;
        allRequiredFields = allRequiredFields && !this.txtAmount.getText().toString().equals("");
        allRequiredFields = allRequiredFields && !this.txtDescription.getText().toString().equals("");
        allRequiredFields = allRequiredFields && !this.lblDate.getText().toString().equals("");
        if (allRequiredFields) {
            DBTransaction transaction = new DBTransaction();
            transaction.AccountID = this.selectedAccount.AccountID;
            transaction.CategoryID = this.selectedCategory.CategoryID;
            transaction.Amount = Double.parseDouble(this.txtAmount.getText().toString());
            transaction.Description = this.txtDescription.getText().toString();
            transaction.Date = new Date(this.lblDate.getText().toString());
            this.loading(true);
            MyApi.postTransaction(getApplicationContext(), this.income, transaction, x -> {
                    this.loading(false);
                    this.clearFields();
                    this.sendToLastActivity();
                },
                x -> MyUtility.okDialog(this, "Error", x.response));
        } else {
            MyUtility.okDialog(this, "Enter all required Fields", "");
        }
    }
}
