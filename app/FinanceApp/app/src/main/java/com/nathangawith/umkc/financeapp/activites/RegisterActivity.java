package com.nathangawith.umkc.financeapp.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.components.RegisterEntryArrayAdapter;
import com.nathangawith.umkc.financeapp.components.RegisterEntry;
import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.constants.MyState;
import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.dtos.TransactionDto;
import com.nathangawith.umkc.financeapp.http.MyApi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class RegisterActivity extends Fragment implements IBackNavigable {

    private TextView lblTotal;
    private SwipeRefreshLayout swipeRefresh;
    private ListView listTransactions;
    private List<TransactionDto> transactions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_register, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        // initialize fields to ui elements
        RegisterActivity me = this;
        this.lblTotal = view.findViewById(R.id.lblTotal);
        this.swipeRefresh = view.findViewById(R.id.swipeRefresh);
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                me.init();
            }
        });
        BiConsumer<Integer, String> setClickEvent = (id, screen) -> {
            view.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyState.GOTO = screen;
                }
            });
        };
        setClickEvent.accept(R.id.btnFloatingIncome, MyConstants.INCOME);
        setClickEvent.accept(R.id.btnFloatingExpense, MyConstants.EXPENSE);
        setClickEvent.accept(R.id.btnFloatingAccountTransfer, MyConstants.TRANSFER_ACCOUNT);
        setClickEvent.accept(R.id.btnFloatingCategoryTransfer, MyConstants.TRANSFER_CATEGORY);
        this.listTransactions = view.findViewById(R.id.listTransactions);
        // populate info
        this.init();
    }

    private void init() {
        MyState.EDITING_TRANSACTION = null;
        MyApi.getTotal(getContext(), resp -> {
            this.lblTotal.setText(String.format("Total: %s", resp.response));
        }, data -> MyUtility.okDialog(getFragmentManager(), "Error", data.response));
        MyApi.getTransactions(getContext(), respCollection -> {
            this.transactions = respCollection.stream().collect(Collectors.toList());
            ArrayList<RegisterEntry> transactionsList = new ArrayList<RegisterEntry>();
            for (TransactionDto transaction : respCollection) {
                transactionsList.add(new RegisterEntry(transaction));
            }
            RegisterEntryArrayAdapter arrayAdapter = new RegisterEntryArrayAdapter(this, getContext(), 0, transactionsList);
            listTransactions.setAdapter(arrayAdapter);
            this.swipeRefresh.setRefreshing(false);
        }, data -> MyUtility.okDialog(getFragmentManager(), "Error", data.response));
    }

    public void btnEditClick(RegisterEntry entry) {
        MyState.SCREEN = entry.getTransactionType();
        MyState.LAST_SCREEN = MyConstants.REGISTER;
        TransactionDto editing_transaction = this.transactions.stream().filter(x -> x.TransactionID == entry.getTransactionID()).findFirst().orElse(null);
        if (editing_transaction != null) {
//            MyState.EDITING_TRANSACTION = editing_transaction;
////            MyUtility.goToActivity(this, IncomeExpenseActivity.class);
            MyState.GOTO = MyConstants.INCOME;
        } else {
            MyUtility.okDialog(getFragmentManager(), "ERROR", "Could not find transaction to edit");
        }
    }

    public void btnDeleteClick(RegisterEntry entry) {
        MyUtility.yesnoDialog(getFragmentManager(), "Are you sure you want to delete this transaction?", "", yesNoResponse -> {
            if (yesNoResponse) {
                MyApi.deleteTransaction(getContext(), entry.getTransactionType(), entry.getTransactionID(),
                    x -> {
                        MyUtility.okDialog(getFragmentManager(), "SUCCESS");
                        this.init();
                    }, x -> MyUtility.okDialog(getFragmentManager(), "ERROR")
                );
            }
        });
    }

    @Override
    public void onBackClick() {
        MyState.GOTO = MyConstants.MENU;
    }
}
