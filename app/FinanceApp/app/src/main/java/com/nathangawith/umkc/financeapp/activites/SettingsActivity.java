package com.nathangawith.umkc.financeapp.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.components.SettingsEntry;
import com.nathangawith.umkc.financeapp.components.SettingsEntryArrayAdapter;
import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.constants.MyState;
import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.dtos.TransactionRequest;
import com.nathangawith.umkc.financeapp.http.MyApi;
import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.dtos.DBAccount;
import com.nathangawith.umkc.financeapp.dtos.DBCategory;
import com.nathangawith.umkc.financeapp.dtos.GenericResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SettingsActivity extends Fragment implements IBackNavigable {

    // Views
    private TextView lblScreenName;
    private TextView lblLabel1;
    private TextView lblLabel2;
    private TextView lblLabel3;
    private EditText txtAddAccount;
    private TextView lblOptions;
    private Spinner spinnerOptions;
    private EditText txtAddIncomeCategory;
    private EditText txtAddExpenseCategory;
    private Button btnAdd;
    private EditText txtEdit;
    private ProgressBar progressBar;
    private ListView listAccounts;
    private ListView listIncomeCategories;
    private ListView listExpenseCategories;
    // Private Fields
    private SettingsEntry editEntity;
    private DBAccount selectedAccount;
    private DBCategory selectedCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_settings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        // initialize fields to ui elements
        this.lblScreenName = view.findViewById(R.id.lblScreenName);
        this.lblLabel1 = view.findViewById(R.id.lblLabel1);
        this.lblLabel2 = view.findViewById(R.id.lblLabel2);
        this.lblLabel3 = view.findViewById(R.id.lblLabel3);
        this.txtAddAccount = view.findViewById(R.id.txtAddAccount);
        this.lblOptions = view.findViewById(R.id.lblOptions);
        this.spinnerOptions = view.findViewById(R.id.spinnerOptions);
        this.btnAdd = view.findViewById(R.id.btnAdd);
        this.txtAddIncomeCategory = view.findViewById(R.id.txtAddIncomeCategory);
        this.txtAddExpenseCategory = view.findViewById(R.id.txtAddExpenseCategory);
        this.txtEdit = view.findViewById(R.id.txtEdit);
        this.progressBar = view.findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);
        this.listAccounts = view.findViewById(R.id.listAccounts);
        this.listIncomeCategories = view.findViewById(R.id.listIncomeCategories);
        this.listExpenseCategories = view.findViewById(R.id.listExpenseCategories);
        this.txtEdit.setVisibility(View.GONE);
        this.lblOptions.setVisibility(View.GONE);
        this.spinnerOptions.setVisibility(View.GONE);
        SettingsActivity me = this;
        this.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.btnAddClick(null);
            }
        });
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

    private boolean isInPickView() {
        return this.lblOptions.getVisibility() == View.VISIBLE;
    }

    private void setVisibilityOfMainElements(int visibility) {
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
    }

    /**
     * toggles between Settings and Edit view
     */
    private void toggleEditView(SettingsEntry entry) {
        this.editEntity = entry;
        int visibility = this.isInEditView() ? View.VISIBLE : View.GONE;
        this.setVisibilityOfMainElements(visibility);
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

    /**
     * toggles between Settings and Pick view
     */
    private void togglePickView(SettingsEntry entry) {
        this.editEntity = entry;
        int visibility = this.isInPickView() ? View.VISIBLE : View.GONE;
        this.setVisibilityOfMainElements(visibility);
        if (visibility == View.VISIBLE) {
            this.btnAdd.setText("+");
            this.lblScreenName.setText("Settings");
            this.lblOptions.setVisibility(View.GONE);
            this.spinnerOptions.setVisibility(View.GONE);
        } else {
            this.btnAdd.setText("Submit");
            this.lblOptions.setVisibility(View.VISIBLE);
            this.spinnerOptions.setVisibility(View.VISIBLE);
            this.lblOptions.setText(entry.getIsAccount() ? "Account:" : "Category:");
            String description = entry.getDescription().split("                    ")[0];
            this.lblScreenName.setText(String.format("Pick %s To Transfer Funds From:\n%s", entry.getIsAccount() ? "Account" : "Category", description));
            if (entry.getIsAccount()) {
                MyApi.getAllAccounts(getContext(), respCollection -> {
                    List<DBAccount> options = respCollection.stream().filter(x -> !x.Description.equals(description)).collect(Collectors.toList());
                    MyUtility.setSpinnerItems(getContext(), this.spinnerOptions, DBAccount.class, options, account -> this.selectedAccount = account);
                }, x -> MyUtility.okDialog(getFragmentManager(), "Error", x.response));
            } else {
                MyApi.getAllIncomeAndExpenseCategories(getContext(), respCollection -> {
                    List<DBCategory> options = respCollection.stream().filter(x -> !x.Description.equals(description)).collect(Collectors.toList());
                    MyUtility.setSpinnerItems(getContext(), this.spinnerOptions, DBCategory.class, options, category -> this.selectedCategory = category);
                }, x -> MyUtility.okDialog(getFragmentManager(), "Error", x.response));
            }
        }
    }

    private <T> void bindListView(ListView listView, Collection<T> respCollection, boolean account, boolean income) {
        listView.invalidateViews();
        listView.refreshDrawableState();
        ArrayList<SettingsEntry> list = new ArrayList<SettingsEntry>();
        for (T item : respCollection) list.add(account ? new SettingsEntry((DBAccount) item) : new SettingsEntry((DBCategory) item, income));
        SettingsEntryArrayAdapter arrayAdapter = new SettingsEntryArrayAdapter(this, getContext(), 0, list);
        listView.setAdapter(arrayAdapter);
        listView.invalidateViews();
        listView.refreshDrawableState();
    }

    /**
     * retrieves all accounts from the api and displays them on the screen
     */
    private void getAllAccounts() {
        MyApi.getAllAccounts(getContext(),
            respCollection -> this.bindListView(this.listAccounts, respCollection, true, false),
            errFunc);
    }

    /**
     * retrieves all categories from the api and displays them on the screen
     * @param income true to retrieve income categories, false to retrieve expense categories
     */
    private void getAllCategories(boolean income) {
        MyApi.getAllCategories(getContext(), income,
            respCollection -> this.bindListView(income ? this.listIncomeCategories : this.listExpenseCategories, respCollection, false, income),
            errFunc);
    }

    /**
     * function used when an error response is received from the api.
     * Displays error message in dialog.
     */
    private Consumer<GenericResponse> errFunc = data -> {
        this.loading(false);
        MyUtility.okDialog(getFragmentManager(), "Error Response", data.response);
    };

    /**
     * adds account via api call, and then re-retrieves accounts
     */
    public void addAccountApiCall(boolean showWarning) {
        String accountDescription = this.txtAddAccount.getText().toString();
        this.loading(true);
        SettingsActivity me = this;
        MyApi.postAddAccount(getContext(), accountDescription, showWarning,
                x -> {
                    MyUtility.okDialog(getFragmentManager(), "ADD ACCOUNT SUCCESS", x.response);
                    me.loading(false);
                    me.txtAddAccount.setText("");
                    me.getAllAccounts();
                },
                x -> {
                    if (showWarning && x.response.toLowerCase().contains("this name was deleted")) {
                        MyUtility.yesnoDialog(getFragmentManager(), "Warning", x.response, yesNoResponse -> {
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
        MyApi.postAddCategory(getContext(), income, categoryDescription, showWarning,
                x -> {
                    MyUtility.okDialog(getFragmentManager(), "ADD CATEGORY SUCCESS", x.response);
                    me.loading(false);
                    if (income) me.txtAddIncomeCategory.setText("");
                    else me.txtAddExpenseCategory.setText("");
                    me.getAllCategories(income);
                },
                x -> {
                    if (showWarning && x.response.toLowerCase().contains("this name was deleted")) {
                        MyUtility.yesnoDialog(getFragmentManager(), "Warning", x.response, yesNoResponse -> {
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
            MyApi.putEditAccount(getContext(), editEntity.getID(), editText,
                    x -> {
                        MyUtility.okDialog(getFragmentManager(), "EDIT ACCOUNT SUCCESS", x.response);
                        this.txtEdit.setText("");
                        this.toggleEditView(null);
                        this.getAllAccounts();
                    },
                    x -> MyUtility.okDialog(getFragmentManager(), "EDIT ACCOUNT FAILED", x.response));
        } else {
            MyApi.putEditCategory(getContext(), editEntity.getID(), editText,
                    x -> {
                        MyUtility.okDialog(getFragmentManager(), "EDIT CATEGORY SUCCESS", x.response);
                        this.txtEdit.setText("");
                        this.toggleEditView(null);
                        this.getAllCategories(true);
                        this.getAllCategories(false);
                    },
                    x -> MyUtility.okDialog(getFragmentManager(), "EDIT CATEGORY FAILED", x.response));
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
                MyUtility.okDialog(getFragmentManager(), "Please enter:", "An alternate title");
            } else {
                this.editApiCall(editText, true);
            }
        } else if (this.isInPickView()) {
            boolean isAccount = this.editEntity.getIsAccount();
            TransactionRequest transaction = new TransactionRequest();
            transaction.AccountID = -1;
            transaction.CategoryID = -1;
            transaction.AccountFromID = isAccount ? this.editEntity.getID() : -1;
            transaction.AccountToID = isAccount ? this.selectedAccount.AccountID : -1;
            transaction.CategoryFromID = isAccount ? -1 : this.editEntity.getID();
            transaction.CategoryToID = isAccount ? -1 : this.selectedCategory.CategoryID;
            transaction.Amount = this.editEntity.getTotal();
            transaction.Description = String.format("Closed %s: %s", isAccount ? "Account" : "Category", this.editEntity.getDescription());
            transaction.Date = new Date();
            MyApi.postTransaction(getContext(), isAccount ? MyConstants.TRANSFER_ACCOUNT : MyConstants.TRANSFER_CATEGORY, transaction,
                x -> {
                    this.deleteApiCall(this.editEntity, false);
                    this.togglePickView(null);
                },
                x -> MyUtility.okDialog(getFragmentManager(), "Error when submitting transfer transaction", ""));
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
                MyUtility.okDialog(getFragmentManager(), "Please enter:", "an Account, an Income Category, or an Expense Category");
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
        Consumer<GenericResponse> failedResponse = x -> {
            boolean isUsedInTransactions = x.response.toLowerCase().contains("used for some transaction");
            boolean isNonzeroBalance = x.response.toLowerCase().contains("remove has a balance");
            if ((showWarning && isUsedInTransactions) || isNonzeroBalance) {
                MyUtility.yesnoDialog(getFragmentManager(), "Warning", x.response, yesNoResponse -> {
                    if (yesNoResponse) {
                        if (isUsedInTransactions) {
                            this.deleteApiCall(entry, false);
                        } else {
                            this.togglePickView(entry);
                        }
                    }
                });
            } else MyUtility.okDialog(getFragmentManager(), "DELETE FAILED", x.response);
        };
        if (entry.getIsAccount()) {
            MyApi.deleteRemoveAccount(getContext(), entry.getID(), showWarning,
                x -> {
                    MyUtility.okDialog(getFragmentManager(), "DELETE ACCOUNT SUCCESS", x.response);
                    this.getAllAccounts();
                },
                x -> failedResponse.accept(x));
        } else {
            MyApi.deleteRemoveCategory(getContext(), entry.getID(), showWarning,
                x -> {
                    MyUtility.okDialog(getFragmentManager(), "DELETE CATEGORY SUCCESS", x.response);
                    this.getAllCategories(entry.getIsIncomeCategory());
                },
                x -> failedResponse.accept(x));
        }
    }

    /**
     * when the delete button is clicked on an account or category entry
     */
    public void btnDeleteClick(SettingsEntry entry) {
        this.deleteApiCall(entry, true);
    }

    @Override
    public void onBackClick() {
        if (this.isInEditView()) {
            this.toggleEditView(null);
        } else if (this.isInPickView()) {
            this.togglePickView(null);
        } else {
            MyState.GOTO = MyConstants.MENU;
        }
    }
}
