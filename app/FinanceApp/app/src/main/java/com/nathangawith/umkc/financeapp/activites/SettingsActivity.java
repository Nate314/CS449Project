package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.components.SettingsEntry;
import com.nathangawith.umkc.financeapp.components.SettingsEntryArrayAdapter;
import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.http.MyApi;
import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.dtos.DBAccount;
import com.nathangawith.umkc.financeapp.dtos.DBCategory;
import com.nathangawith.umkc.financeapp.dtos.GenericResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class SettingsActivity extends AppCompatActivity {

    private TextView lblScreenName;
    private TextView lblLabel1;
    private TextView lblLabel2;
    private TextView lblLabel3;
    private EditText txtAddAccount;
    private EditText txtAddIncomeCategory;
    private EditText txtAddExpenseCategory;
    private Button btnAdd;
    private Button btnBack;
    private EditText txtEdit;
    private ProgressBar progressBar;
    private ListView listAccounts;
    private ListView listIncomeCategories;
    private ListView listExpenseCategories;

    private SettingsEntry editEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // initialize fields to ui elements
        this.lblScreenName = findViewById(R.id.lblScreenName);
        this.lblLabel1 = findViewById(R.id.lblLabel1);
        this.lblLabel2 = findViewById(R.id.lblLabel2);
        this.lblLabel3 = findViewById(R.id.lblLabel3);
        this.txtAddAccount = findViewById(R.id.txtAddAccount);
        this.btnAdd = findViewById(R.id.btnAdd);
        this.txtAddIncomeCategory = findViewById(R.id.txtAddIncomeCategory);
        this.txtAddExpenseCategory = findViewById(R.id.txtAddExpenseCategory);
        this.btnBack = findViewById(R.id.btnBack);
        this.txtEdit = findViewById(R.id.txtEdit);
        this.progressBar = findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);
        this.listAccounts = findViewById(R.id.listAccounts);
        this.listIncomeCategories = findViewById(R.id.listIncomeCategories);
        this.listExpenseCategories = findViewById(R.id.listExpenseCategories);
        this.txtEdit.setVisibility(View.GONE);
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

    private boolean isInEditView() {
        return this.txtEdit.getVisibility() == View.VISIBLE;
    }

    /**
     * toggles between Settings and Edit view
     */
    private void toggleEditView(SettingsEntry entry) {
        this.editEntity = entry;
        int visibility = this.isInEditView() ? View.VISIBLE : View.GONE;
        this.lblLabel1.setVisibility(visibility);
        this.lblLabel2.setVisibility(visibility);
        this.lblLabel3.setVisibility(visibility);
        this.txtAddAccount.setVisibility(visibility);
        this.txtAddIncomeCategory.setVisibility(visibility);
        this.txtAddExpenseCategory.setVisibility(visibility);
        // this.btnAdd.setVisibility(visibility);
        // this.btnBack.setVisibility(visibility);
        // this.progressBar.setVisibility(visibility);
        this.listAccounts.setVisibility(visibility);
        this.listIncomeCategories.setVisibility(visibility);
        this.listExpenseCategories.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            this.btnAdd.setText("+");
            this.lblScreenName.setText("Settings");
            this.txtEdit.setVisibility(View.GONE);
        } else {
            this.btnAdd.setText("Submit");
            this.lblScreenName.setText(String.format("Edit %s Name", entry.getIsAccount() ? "Account" : "Category"));
            this.txtEdit.setVisibility(View.VISIBLE);
        }
    }

    private <T> void bindListView(ListView listView, Collection<T> respCollection, boolean account, boolean income) {
        listView.invalidateViews();
        listView.refreshDrawableState();
        ArrayList<SettingsEntry> list = new ArrayList<SettingsEntry>();
        for (T item : respCollection) list.add(account ? new SettingsEntry((DBAccount) item) : new SettingsEntry((DBCategory) item, income));
        SettingsEntryArrayAdapter arrayAdapter = new SettingsEntryArrayAdapter(this, this, 0, list);
        listView.setAdapter(arrayAdapter);
        listView.invalidateViews();
        listView.refreshDrawableState();
    }

    /**
     * retrieves all accounts from the api and displays them on the screen
     */
    private void getAllAccounts() {
        MyApi.getAllAccounts(getApplicationContext(),
            respCollection -> this.bindListView(this.listAccounts, respCollection, true, false),
            errFunc);
    }

    /**
     * retrieves all categories from the api and displays them on the screen
     * @param income true to retrieve income categories, false to retrieve expense categories
     */
    private void getAllCategories(boolean income) {
        MyApi.getAllCategories(getApplicationContext(), income,
            respCollection -> this.bindListView(income ? this.listIncomeCategories : this.listExpenseCategories, respCollection, false, income),
            errFunc);
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
    public void addAccountApiCall(boolean showWarning) {
        String accountDescription = this.txtAddAccount.getText().toString();
        this.loading(true);
        SettingsActivity me = this;
        MyApi.postAddAccount(getApplicationContext(), accountDescription, showWarning,
                x -> {
                    MyUtility.okDialog(this, "ADD ACCOUNT SUCCESS", x.response);
                    me.loading(false);
                    me.txtAddAccount.setText("");
                    me.getAllAccounts();
                },
                x -> {
                    if (showWarning && x.response.toLowerCase().contains("is disabled")) {
                        MyUtility.yesnoDialog(this, "Warning", x.response, yesNoResponse -> {
                            if (yesNoResponse) {
                                me.addAccountApiCall(false);
                            } else {
                                me.loading(false);
                                me.txtAddAccount.setText("");
                            }
                        });
                    }
                    else errFunc.accept(x);
                });
    }

    /**
     * adds income or expense category via api call, and then re-retrieves income categories
     */
    public void addCategoryApiCall(boolean income, boolean showWarning) {
        String categoryDescription = income ? this.txtAddIncomeCategory.getText().toString() : this.txtAddExpenseCategory.getText().toString();
        this.loading(true);
        SettingsActivity me = this;
        MyApi.postAddCategory(getApplicationContext(), income, categoryDescription, showWarning,
                x -> {
                    MyUtility.okDialog(this, "ADD CATEGORY SUCCESS", x.response);
                    me.loading(false);
                    if (income) me.txtAddIncomeCategory.setText("");
                    else me.txtAddExpenseCategory.setText("");
                    me.getAllCategories(income);
                },
                x -> {
                    if (showWarning && x.response.toLowerCase().contains("is disabled")) {
                        MyUtility.yesnoDialog(this, "Warning", x.response, yesNoResponse -> {
                            if (yesNoResponse) {
                                me.addCategoryApiCall(income, false);
                            } else {
                                me.loading(false);
                                if (income) me.txtAddIncomeCategory.setText("");
                                else me.txtAddExpenseCategory.setText("");
                            }
                        });
                    }
                    else errFunc.accept(x);
                });
    }

    private void editApiCall(String editText, boolean showWarning) {
        if (this.editEntity.getIsAccount()) {
            MyApi.putEditAccount(this, editEntity.getID(), editText,
                    x -> {
                        MyUtility.okDialog(this, "EDIT ACCOUNT SUCCESS", x.response);
                        this.txtEdit.setText("");
                        this.toggleEditView(null);
                        this.getAllAccounts();
                    },
                    x -> MyUtility.okDialog(this, "EDIT ACCOUNT FAILED", x.response));
        } else {
            MyApi.putEditCategory(this, editEntity.getID(), editText,
                    x -> {
                        MyUtility.okDialog(this, "EDIT CATEGORY SUCCESS", x.response);
                        this.txtEdit.setText("");
                        this.toggleEditView(null);
                        this.getAllCategories(true);
                        this.getAllCategories(false);
                    },
                    x -> MyUtility.okDialog(this, "EDIT CATEGORY FAILED", x.response));
        }
    }

    /**
     * adds expense category via api call, and then re-retrieves expense categories
     * @param view button view
     */
    public void btnAddClick(View view) {
        if (this.isInEditView()) {
            String editText = this.txtEdit.getText().toString();
            if (editText.equals("")){
                MyUtility.okDialog(this, "Please enter:", "An alternate title");
            } else {
                this.editApiCall(editText, true);
            }
        } else {
            boolean noneAdded = true;
            if (!this.txtAddAccount.getText().toString().equals("")) {
                this.addAccountApiCall(true);
                noneAdded = false;
            }
            if (!this.txtAddIncomeCategory.getText().toString().equals("")) {
                this.addCategoryApiCall(true, true);
                noneAdded = false;
            }
            if (!this.txtAddExpenseCategory.getText().toString().equals("")) {
                this.addCategoryApiCall(false, true);
                noneAdded = false;
            }
            if (noneAdded){
                MyUtility.okDialog(this, "Please enter:", "an Account, an Income Category, or an Expense Category");
            }
        }
    }

    /**
     * when the edit button is clicked on an account or category entry
     */
    public void btnEditClick(SettingsEntry entry) {
        this.toggleEditView(entry);
    }

    private void deleteApiCall(SettingsEntry entry, boolean showWarning) {
        SettingsActivity me = this;
        if (entry.getIsAccount()) {
            MyApi.deleteRemoveAccount(this, entry.getID(), showWarning,
                    x -> {
                        MyUtility.okDialog(this, "DELETE ACCOUNT SUCCESS", x.response);
                        me.getAllAccounts();
                    },
                    x -> {
                        if (showWarning && x.response.toLowerCase().contains("used for some transaction")) {
                            MyUtility.yesnoDialog(this, "Warning", x.response, yesNoResponse -> {
                                if (yesNoResponse) {
                                    me.deleteApiCall(entry, false);
                                }
                            });
                        }
                        else MyUtility.okDialog(this, "DELETE ACCOUNT FAILED", x.response);
                    });
        } else {
            MyApi.deleteRemoveCategory(this, entry.getID(), showWarning,
                    x -> {
                        MyUtility.okDialog(this, "DELETE CATEGORY SUCCESS", x.response);
                        me.getAllCategories(entry.getIsIncomeCategory());
                    },
                    x -> {
                        if (showWarning && x.response.toLowerCase().contains("used for some transaction")) {
                            MyUtility.yesnoDialog(this, "Warning", x.response, yesNoResponse -> {
                                if (yesNoResponse) {
                                    me.deleteApiCall(entry, false);
                                }
                            });
                        }
                        else MyUtility.okDialog(this, "DELETE CATEGORY FAILED", x.response);
                    });
        }
    }

    /**
     * when the delete button is clicked on an account or category entry
     */
    public void btnDeleteClick(SettingsEntry entry) {
        this.deleteApiCall(entry, true);
    }

    /**
     * sends user back to the previous screen
     * @param view button view
     */
    public void btnBackClick(View view) {
        if (this.isInEditView()) {
            this.toggleEditView(null);
        } else {
            startActivity(new Intent(this, MenuActivity.class));
        }
    }
}
