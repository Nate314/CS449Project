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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.constants.MyState;
import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.dtos.DBAccount;
import com.nathangawith.umkc.financeapp.dtos.DBCategory;
import com.nathangawith.umkc.financeapp.dtos.GenericResponse;
import com.nathangawith.umkc.financeapp.dtos.TransactionRequest;
import com.nathangawith.umkc.financeapp.http.MyApi;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class IncomeExpenseActivity extends Fragment implements IBackNavigable {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_income_expense, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        // initialize fields to ui elements
        this.txtAmount = view.findViewById(R.id.txtAmount);
        this.txtDescription = view.findViewById(R.id.txtDescription);
        this.spinnerAccount = view.findViewById(R.id.spinnerAccount);
        this.spinnerCategory = view.findViewById(R.id.spinnerCategory);
        this.spinnerFromAccount = view.findViewById(R.id.spinnerFromAccount);
        this.spinnerToAccount = view.findViewById(R.id.spinnerToAccount);
        this.spinnerFromCategory = view.findViewById(R.id.spinnerFromCategory);
        this.spinnerToCategory = view.findViewById(R.id.spinnerToCategory);
        this.btnSelectDate = view.findViewById(R.id.btnSelectDate);
        this.lblDate = view.findViewById(R.id.lblDate);
        this.btnSubmit = view.findViewById(R.id.btnSubmit);
        this.progressBar = view.findViewById(R.id.progressBar);
        this.lblScreenName = view.findViewById(R.id.lblScreenName);
        this.lblLabel1 = view.findViewById(R.id.lblLabel1);
        this.lblLabel2 = view.findViewById(R.id.lblLabel2);
        this.lblLabel3 = view.findViewById(R.id.lblLabel3);
        this.lblLabel4 = view.findViewById(R.id.lblLabel4);
        this.lblLabel5 = view.findViewById(R.id.lblLabel5);
        this.lblLabel6 = view.findViewById(R.id.lblLabel6);
        this.btnSelectDate.setOnClickListener(v -> MyUtility.btnDateClick(getContext(), this.lblDate));
        IncomeExpenseActivity me = this;
        this.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.btnSubmitClick(v);
            }
        });
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
            MyApi.getAllCategories(getContext(), income, categories -> {
                if (MyState.EDITING_TRANSACTION != null) {
                    DBCategory previouslySelectedCategory = categories.stream().filter(x -> x.CategoryID == MyState.EDITING_TRANSACTION.CategoryID).findFirst().orElse(null);
                    List<DBCategory> previouslyNotSelectedCategories = categories.stream().filter(x -> x.CategoryID != MyState.EDITING_TRANSACTION.CategoryID).collect(Collectors.toList());
                    if (previouslySelectedCategory == null) {
                        MyUtility.okDialog(getFragmentManager(), "Category not found");
                        previouslySelectedCategory = previouslyNotSelectedCategories.get(0);
                        previouslyNotSelectedCategories.remove(0);
                    }
                    previouslyNotSelectedCategories.add(0, previouslySelectedCategory);
                    categories = previouslyNotSelectedCategories;
                }
                this.allCategories = categories;
                MyUtility.setSpinnerItems(getContext(), this.spinnerCategory, DBCategory.class, categories, category -> this.selectedCategory = category);
                MyApi.getAllAccounts(getContext(), accounts -> {
                    if (MyState.EDITING_TRANSACTION != null) {
                        DBAccount previouslySelectedAccount = accounts.stream().filter(x -> x.AccountID == MyState.EDITING_TRANSACTION.AccountID).findFirst().orElse(null);
                        List<DBAccount> previouslyNotSelectedAccounts = accounts.stream().filter(x -> x.AccountID != MyState.EDITING_TRANSACTION.AccountID).collect(Collectors.toList());
                        if (previouslySelectedAccount == null) {
                            MyUtility.okDialog(getFragmentManager(), "Account not found");
                            previouslySelectedAccount = previouslyNotSelectedAccounts.get(0);
                            previouslyNotSelectedAccounts.remove(0);
                        }
                        previouslyNotSelectedAccounts.add(0, previouslySelectedAccount);
                        accounts = previouslyNotSelectedAccounts;
                    }
                    this.allAccounts = accounts;
                    MyUtility.setSpinnerItems(getContext(), this.spinnerAccount, DBAccount.class, accounts, account -> this.selectedAccount = account);
                }, x -> MyUtility.okDialog(getFragmentManager(), "Error", x.response));
            }, x -> MyUtility.okDialog(getFragmentManager(), "Error", x.response));
        } else if (MyState.SCREEN.equals(MyConstants.TRANSFER_ACCOUNT)) {
            this.lblScreenName.setText("Account Transfer");
            this.lblLabel3.setVisibility(View.VISIBLE);
            this.lblLabel4.setVisibility(View.VISIBLE);
            this.spinnerToAccount.setVisibility(View.VISIBLE);
            this.spinnerFromAccount.setVisibility(View.VISIBLE);
            MyApi.getAllAccounts(getContext(), respCollection -> {
                if (MyState.EDITING_TRANSACTION != null) {
                    DBAccount previouslySelectedFromAccount = respCollection.stream().filter(x -> x.AccountID == MyState.EDITING_TRANSACTION.AccountFromID).findFirst().orElse(null);
                    DBAccount previouslySelectedToAccount = respCollection.stream().filter(x -> x.AccountID == MyState.EDITING_TRANSACTION.AccountToID).findFirst().orElse(null);
                    List<DBAccount> previouslyNotSelectedAccounts = respCollection.stream().filter(x -> x.AccountID != MyState.EDITING_TRANSACTION.AccountFromID && x.AccountID != MyState.EDITING_TRANSACTION.AccountToID).collect(Collectors.toList());
                    if (previouslySelectedToAccount == null) {
                        MyUtility.okDialog(getFragmentManager(), "To Account not found");
                    } else {
                        previouslyNotSelectedAccounts.add(0, previouslySelectedToAccount);
                    }
                    if (previouslySelectedFromAccount == null) {
                        MyUtility.okDialog(getFragmentManager(), "From Account not found");
                    } else {
                        previouslyNotSelectedAccounts.add(0, previouslySelectedFromAccount);
                    }
                    respCollection = previouslyNotSelectedAccounts;
                }
                this.allAccounts = respCollection;
                this.setFromAccounts(respCollection);
                this.setToAccounts(respCollection);
            },
            x -> MyUtility.okDialog(getFragmentManager(), "Error", x.response));
        } else if (MyState.SCREEN.equals(MyConstants.TRANSFER_CATEGORY)) {
            this.lblScreenName.setText("Category Transfer");
            this.lblLabel5.setVisibility(View.VISIBLE);
            this.lblLabel6.setVisibility(View.VISIBLE);
            this.spinnerFromCategory.setVisibility(View.VISIBLE);
            this.spinnerToCategory.setVisibility(View.VISIBLE);
            MyApi.getAllIncomeAndExpenseCategories(getContext(), respCollection -> {
                if (MyState.EDITING_TRANSACTION != null) {
                    DBCategory previouslySelectedFromCategory = respCollection.stream().filter(x -> x.CategoryID == MyState.EDITING_TRANSACTION.CategoryFromID).findFirst().orElse(null);
                    DBCategory previouslySelectedToCategory = respCollection.stream().filter(x -> x.CategoryID == MyState.EDITING_TRANSACTION.CategoryToID).findFirst().orElse(null);
                    List<DBCategory> previouslyNotSelectedCategories = respCollection.stream().filter(x -> x.CategoryID != MyState.EDITING_TRANSACTION.CategoryFromID && x.CategoryID != MyState.EDITING_TRANSACTION.CategoryToID).collect(Collectors.toList());
                    if (previouslySelectedToCategory == null) {
                        MyUtility.okDialog(getFragmentManager(), "To Category not found");
                    } else {
                        previouslyNotSelectedCategories.add(0, previouslySelectedToCategory);
                    }
                    if (previouslySelectedFromCategory == null) {
                        MyUtility.okDialog(getFragmentManager(), "From Category not found");
                    } else {
                        previouslyNotSelectedCategories.add(0, previouslySelectedFromCategory);
                    }
                    respCollection = previouslyNotSelectedCategories;
                }
                this.allCategories = respCollection;
                this.setFromCategories(respCollection);
                this.setToCategories(respCollection);
            },
            x -> MyUtility.okDialog(getFragmentManager(), "Error", x.response));
        }
        if (MyState.EDITING_TRANSACTION != null) {
            this.btnSubmit.setText("UPDATE");
            this.txtDescription.setText(MyState.EDITING_TRANSACTION.Description);
            this.txtAmount.setText(Math.abs(MyState.EDITING_TRANSACTION.Amount) + "");
            try {
                this.lblDate.setText(MyUtility.sqlDateToJavaDate(MyState.EDITING_TRANSACTION.Date));
            } catch (Exception e) {
                MyUtility.okDialog(getFragmentManager(), "Could not parse date");
            }
        }
    }

    private int fromCategorySelectedCounter = 0;
    private int toCategorySelectedCounter = 0;

    private void setFromCategories(Collection<DBCategory> respCollection) {
        Integer previousCategoryID = this.selectedFromCategory == null ? null : this.selectedFromCategory.CategoryID;
        MyUtility.setSpinnerItems(getContext(), this.spinnerFromCategory, DBCategory.class, respCollection, category -> {
            this.selectedFromCategory = category;
            this.setToCategories(this.allCategories.stream().filter(x -> x.Description != this.spinnerFromCategory.getSelectedItem()).collect(Collectors.toList()));
            if (this.fromCategorySelectedCounter  == 0) {
                this.fromCategorySelectedCounter ++;
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
        });
    }

    private void setToCategories(Collection<DBCategory> respCollection) {
        Integer previousCategoryID = this.selectedToCategory == null ? null : this.selectedToCategory.CategoryID;
        MyUtility.setSpinnerItems(getContext(), this.spinnerToCategory, DBCategory.class, respCollection, category -> {
            this.selectedToCategory = category;
//            this.setFromCategories(this.allCategories.stream().filter(x -> x.Description != this.spinnerToCategory.getSelectedItem()).collect(Collectors.toList()));
            if (this.toCategorySelectedCounter == 0) {
                this.toCategorySelectedCounter++;
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
        });
    }

    private int fromAccountSelectedCounter = 0;
    private int toAccountSelectedCounter = 0;

    private void setFromAccounts(Collection<DBAccount> respCollection) {
        Integer previousAccountID = this.selectedFromAccount == null ? null : this.selectedFromAccount.AccountID;
        MyUtility.setSpinnerItems(getContext(), this.spinnerFromAccount, DBAccount.class, respCollection, account -> {
            this.selectedFromAccount = account;
            this.setToAccounts(this.allAccounts.stream().filter(x -> x.Description != account.Description).collect(Collectors.toList()));
            if (this.fromAccountSelectedCounter == 0) {
                this.fromAccountSelectedCounter++;
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
        });
    }

    private void setToAccounts(Collection<DBAccount> respCollection) {
        Integer previousAccountID = this.selectedToAccount== null ? null : this.selectedToAccount.AccountID;
        MyUtility.setSpinnerItems(getContext(), this.spinnerToAccount, DBAccount.class, respCollection, account -> {
            this.selectedToAccount = account;
//            this.setFromCategories(this.allCategories.stream().filter(x -> x.Description != this.spinnerToCategory.getSelectedItem()).collect(Collectors.toList()));
            if (this.toAccountSelectedCounter == 0) {
                this.toAccountSelectedCounter++;
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
        });
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
        MyState.EDITING_TRANSACTION = null;
        System.out.println(MyState.LAST_SCREEN);
        if (MyState.LAST_SCREEN == MyConstants.MENU) {
            MyState.GOTO = MyConstants.MENU;
        } else if (MyState.LAST_SCREEN == MyConstants.REGISTER) {
            MyState.GOTO = MyConstants.REGISTER;
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
            transaction.TransactionID = MyState.EDITING_TRANSACTION == null ? -1 : MyState.EDITING_TRANSACTION.TransactionID;
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
            Consumer<GenericResponse> okFunc = x -> {
                this.loading(false);
                this.clearFields();
                this.sendToLastActivity();
            };
            Consumer<GenericResponse> errFunc = x -> {
                this.loading(false);
                if (x != null) {
                    MyUtility.okDialog(getFragmentManager(), "Error", x.response);
                } else {
                    System.out.println("Error Response was null");
                }
            };
            if (MyState.EDITING_TRANSACTION == null) {
                MyApi.postTransaction(getContext(), MyState.SCREEN, transaction, okFunc, errFunc);
            } else {
                MyApi.putTransaction(getContext(), MyState.SCREEN, transaction, okFunc, errFunc);
            }
        } else {
            MyUtility.okDialog(getFragmentManager(), "Enter all required Fields", "");
        }
    }

    @Override
    public void onBackClick() {
        this.sendToLastActivity();
    }
}
