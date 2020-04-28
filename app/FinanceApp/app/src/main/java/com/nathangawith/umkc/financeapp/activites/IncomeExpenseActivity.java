package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.nathangawith.umkc.financeapp.dtos.TransactionRequest;
import com.nathangawith.umkc.financeapp.http.MyApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IncomeExpenseActivity extends AppCompatActivity {

    // ui elements
    private TextView lblScreenName;
    private EditText txtAmount;
    private EditText txtDescription;
    private Spinner spinnerAccount;
    private Spinner spinnerCategory;
    private Spinner spinnerToAccount;
    private Spinner spinnerFromAccount;
    private Spinner spinnerToCategory;
    private Spinner spinnerFromCategory;
    private Button btnSelectDate;
    private TextView lblDate;
    private ProgressBar progressBar;
    private Button btnSubmit;
    private TextView lblLabel1;
    private TextView lblLabel2;
    private TextView lblLabel3;
    private TextView lblLabel4;
    private TextView lblLabel5;
    private TextView lblLabel6;
    // private fields
    private boolean income;
    private DBAccount selectedAccount = null;
    private DBCategory selectedCategory = null;
    private DBAccount selectedFromAccount = null;
    private DBAccount selectedToAccount = null;
    private DBCategory selectedFromCategory = null;
    private DBCategory selectedToCategory = null;
    private Collection<DBCategory> allCategories = null;
    private Collection<DBAccount> allAccounts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expense);
        // initialize fields to ui elements
        this.txtAmount = findViewById(R.id.txtAmount);
        this.txtDescription = findViewById(R.id.txtDescription);
        this.spinnerAccount = findViewById(R.id.spinnerAccount);
        this.spinnerCategory = findViewById(R.id.spinnerCategory);
        this.spinnerFromAccount = findViewById(R.id.spinnerFromAccount);
        this.spinnerToAccount = findViewById(R.id.spinnerToAccount);
        this.spinnerFromCategory = findViewById(R.id.spinnerFromCategory);
        this.spinnerToCategory = findViewById(R.id.spinnerToCategory);
        this.btnSelectDate = findViewById(R.id.btnSelectDate);
        this.lblDate = findViewById(R.id.lblDate);
        this.btnSubmit = findViewById(R.id.btnSubmit);
        this.progressBar = findViewById(R.id.progressBar);
        this.lblScreenName = findViewById(R.id.lblScreenName);
        this.lblLabel1 = findViewById(R.id.lblLabel1);
        this.lblLabel2 = findViewById(R.id.lblLabel2);
        this.lblLabel3 = findViewById(R.id.lblLabel3);
        this.lblLabel4 = findViewById(R.id.lblLabel4);
        this.lblLabel5 = findViewById(R.id.lblLabel5);
        this.lblLabel6 = findViewById(R.id.lblLabel6);
        this.btnSelectDate.setOnClickListener(v -> MyUtility.btnDateClick(this, this.lblDate));
        this.init();
    }

    public void init() {
        // hide all spinners
        this.spinnerCategory.setVisibility(View.GONE);
        this.spinnerAccount.setVisibility(View.GONE);
        this.spinnerToAccount.setVisibility(View.GONE);
        this.spinnerFromAccount.setVisibility(View.GONE);
        this.spinnerFromCategory.setVisibility(View.GONE);
        this.spinnerToCategory.setVisibility(View.GONE);
        this.lblLabel1.setVisibility(View.GONE);
        this.lblLabel2.setVisibility(View.GONE);
        this.lblLabel3.setVisibility(View.GONE);
        this.lblLabel4.setVisibility(View.GONE);
        this.lblLabel5.setVisibility(View.GONE);
        this.lblLabel6.setVisibility(View.GONE);
        // initialize ui elements
        this.progressBar.setVisibility(View.INVISIBLE);
        this.income = MyState.SCREEN != null ? MyState.SCREEN.equals(MyConstants.INCOME) : false;
        // get accounts categories from api
        if (MyState.SCREEN.equals(MyConstants.INCOME) || MyState.SCREEN.equals(MyConstants.EXPENSE)) {
            this.lblScreenName.setText(this.income ? "Income" : "Expense");
            this.lblLabel1.setVisibility(View.VISIBLE);
            this.lblLabel2.setVisibility(View.VISIBLE);
            this.spinnerCategory.setVisibility(View.VISIBLE);
            this.spinnerAccount.setVisibility(View.VISIBLE);
            MyApi.getAllAccounts(getApplicationContext(),
                    respCollection -> MyUtility.setSpinnerItems(this, this.spinnerAccount, DBAccount.class, respCollection, account -> this.selectedAccount = account),
                    x -> MyUtility.okDialog(this, "Error", x.response));
            MyApi.getAllCategories(getApplicationContext(), income,
                    respCollection -> MyUtility.setSpinnerItems(this, this.spinnerCategory, DBCategory.class, respCollection, category -> this.selectedCategory = category),
                    x -> MyUtility.okDialog(this, "Error", x.response));
        } else if (MyState.SCREEN.equals(MyConstants.TRANSFER_ACCOUNT)) {
            this.lblScreenName.setText("Account Transfer");
            this.lblLabel3.setVisibility(View.VISIBLE);
            this.lblLabel4.setVisibility(View.VISIBLE);
            this.spinnerToAccount.setVisibility(View.VISIBLE);
            this.spinnerFromAccount.setVisibility(View.VISIBLE);
            MyApi.getAllAccounts(getApplicationContext(),
                    respCollection -> {
                        this.allAccounts = respCollection;
                        this.setFromAccounts(respCollection);
                        this.setToAccounts(respCollection);
                    },
                    x -> MyUtility.okDialog(this, "Error", x.response));
        } else if (MyState.SCREEN.equals(MyConstants.TRANSFER_CATEGORY)) {
            this.lblScreenName.setText("Category Transfer");
            this.lblLabel5.setVisibility(View.VISIBLE);
            this.lblLabel6.setVisibility(View.VISIBLE);
            this.spinnerFromCategory.setVisibility(View.VISIBLE);
            this.spinnerToCategory.setVisibility(View.VISIBLE);
            MyApi.getAllIncomeAndExpenseCategories(getApplicationContext(), respCollection -> {
                this.allCategories = respCollection;
                this.setFromCategories(respCollection);
                this.setToCategories(respCollection);
            },
            x -> MyUtility.okDialog(this, "Error", x.response));
        }
    }

    private void setFromCategories(Collection<DBCategory> respCollection) {
        Integer previousCategoryID = this.selectedFromCategory == null ? null : this.selectedFromCategory.CategoryID;
        MyUtility.setSpinnerItems(this, this.spinnerFromCategory, DBCategory.class, respCollection, category -> {
            this.selectedFromCategory = category;
            this.setToCategories(this.allCategories.stream().filter(x -> x.Description != this.spinnerFromCategory.getSelectedItem()).collect(Collectors.toList()));
        });
        int index = 0;
        if (previousCategoryID != null) {
            for (int i = 0; i < respCollection.size(); i++) {
                if (((DBCategory) respCollection.toArray()[i]).CategoryID == previousCategoryID) {
                    index = i;
                }
            }
        }
        this.spinnerFromCategory.getOnItemSelectedListener().onItemSelected(null, null, 0, index);
    }

    private void setToCategories(Collection<DBCategory> respCollection) {
        Integer previousCategoryID = this.selectedToCategory == null ? null : this.selectedToCategory.CategoryID;
        MyUtility.setSpinnerItems(this, this.spinnerToCategory, DBCategory.class, respCollection, category -> {
            this.selectedToCategory = category;
//            this.setFromCategories(this.allCategories.stream().filter(x -> x.Description != this.spinnerToCategory.getSelectedItem()).collect(Collectors.toList()));
        });
        int index = 0;
        if (previousCategoryID != null) {
            for (int i = 0; i < respCollection.size(); i++) {
                if (((DBCategory)respCollection.toArray()[i]).CategoryID == previousCategoryID) {
                    index = i;
                }
            }
        }
        this.spinnerToCategory.getOnItemSelectedListener().onItemSelected(null, null, 0, index);
    }

    private void setFromAccounts(Collection<DBAccount> respCollection) {
        Integer previousAccountID = this.selectedFromAccount == null ? null : this.selectedFromAccount.AccountID;
        MyUtility.setSpinnerItems(this, this.spinnerFromAccount, DBAccount.class, respCollection, account -> {
            this.selectedFromAccount = account;
            this.setToAccounts(this.allAccounts.stream().filter(x -> x.Description != account.Description).collect(Collectors.toList()));
        });
        int index = 0;
        if (previousAccountID != null) {
            for (int i = 0; i < respCollection.size(); i++) {
                if (((DBAccount) respCollection.toArray()[i]).AccountID == previousAccountID) {
                    index = i;
                }
            }
        }
        this.spinnerFromAccount.getOnItemSelectedListener().onItemSelected(null, null, 0, index);
    }

    private void setToAccounts(Collection<DBAccount> respCollection) {
        Integer previousAccountID = this.selectedToAccount== null ? null : this.selectedToAccount.AccountID;
        MyUtility.setSpinnerItems(this, this.spinnerToAccount, DBAccount.class, respCollection, account -> {
            this.selectedToAccount = account;
//            this.setFromCategories(this.allCategories.stream().filter(x -> x.Description != this.spinnerToCategory.getSelectedItem()).collect(Collectors.toList()));
        });
        int index = 0;
        if (previousAccountID != null) {
            for (int i = 0; i < respCollection.size(); i++) {
                if (((DBAccount)respCollection.toArray()[i]).AccountID == previousAccountID) {
                    index = i;
                }
            }
        }
        this.spinnerToAccount.getOnItemSelectedListener().onItemSelected(null, null, 0, index);
    }

    public void clearFields() {
        this.txtAmount.setText("");
        this.txtDescription.setText("");
        if (MyState.SCREEN.equals(MyConstants.INCOME) || MyState.SCREEN.equals(MyConstants.EXPENSE)) {
            this.spinnerAccount.getOnItemSelectedListener().onItemSelected(null, null, 0, 0);
            this.spinnerCategory.getOnItemSelectedListener().onItemSelected(null, null, 0, 0);
        } else if (MyState.SCREEN.equals(MyConstants.TRANSFER_ACCOUNT)) {
            this.spinnerFromAccount.getOnItemSelectedListener().onItemSelected(null, null, 0, 0);
            this.spinnerToAccount.getOnItemSelectedListener().onItemSelected(null, null, 0, 0);
        } else if (MyState.SCREEN.equals(MyConstants.TRANSFER_CATEGORY)) {
            this.spinnerFromCategory.getOnItemSelectedListener().onItemSelected(null, null, 0, 0);
            this.spinnerToCategory.getOnItemSelectedListener().onItemSelected(null, null, 0, 0);
        }
        this.lblDate.setText("");
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
        this.spinnerFromAccount.setEnabled(enabled);
        this.spinnerToAccount.setEnabled(enabled);
        this.spinnerFromCategory.setEnabled(enabled);
        this.spinnerToCategory.setEnabled(enabled);
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
        System.out.println("---------------- Selected Category ----------------");
        System.out.println(this.spinnerFromCategory.getSelectedItem());
        boolean allRequiredFields = true;
        allRequiredFields = allRequiredFields && !this.txtAmount.getText().toString().equals("");
        allRequiredFields = allRequiredFields && !this.txtDescription.getText().toString().equals("");
        allRequiredFields = allRequiredFields && !this.lblDate.getText().toString().equals("");
        if (MyState.SCREEN.equals(MyConstants.INCOME) || MyState.SCREEN.equals(MyConstants.EXPENSE)) {
            allRequiredFields = allRequiredFields && this.selectedAccount != null;
            allRequiredFields = allRequiredFields && this.selectedCategory != null;
        } else if (MyState.SCREEN.equals(MyConstants.TRANSFER_ACCOUNT)) {
            allRequiredFields = allRequiredFields && this.selectedToAccount != null;
            allRequiredFields = allRequiredFields && this.selectedFromAccount != null;
        } else if (MyState.SCREEN.equals(MyConstants.TRANSFER_CATEGORY)) {
            allRequiredFields = allRequiredFields && this.selectedToCategory != null;
            allRequiredFields = allRequiredFields && this.selectedFromCategory != null;
        }
        if (allRequiredFields) {
            TransactionRequest transaction = new TransactionRequest();
            transaction.AccountID = this.selectedAccount == null ? -1 : this.selectedAccount.AccountID;
            transaction.CategoryID = this.selectedCategory == null ? -1 : this.selectedCategory.CategoryID;
            transaction.AccountFromID = this.selectedFromAccount == null ? -1 : this.selectedFromAccount.AccountID;
            transaction.AccountToID = this.selectedToAccount == null ? -1 : this.selectedToAccount.AccountID;
            transaction.CategoryFromID = this.selectedFromCategory == null ? -1 : this.selectedFromCategory.CategoryID;
            transaction.CategoryToID = this.selectedToCategory == null ? -1 : this.selectedToCategory.CategoryID;
            transaction.Amount = Double.parseDouble(this.txtAmount.getText().toString());
            transaction.Description = this.txtDescription.getText().toString();
            transaction.Date = new Date(this.lblDate.getText().toString());
            this.loading(true);
            MyApi.postTransaction(getApplicationContext(), MyState.SCREEN, transaction, x -> {
                    this.loading(false);
                    this.clearFields();
                    this.sendToLastActivity();
                },
                x -> {
                    this.loading(false);
                    if (x != null) {
                        MyUtility.okDialog(this, "Error", x.response);
                    } else {
                        System.out.println("Error Response was null");
                    }
                });
        } else {
            MyUtility.okDialog(this, "Enter all required Fields", "");
        }
    }
}
