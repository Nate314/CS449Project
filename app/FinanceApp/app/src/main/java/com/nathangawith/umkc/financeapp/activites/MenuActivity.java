package com.nathangawith.umkc.financeapp.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.constants.MyState;
import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.http.MyApi;

import java.util.function.BiConsumer;

public class MenuActivity extends Fragment implements IBackNavigable {

    private View view;
    private TextView lblTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_menu, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.view = getView();
        // initialize fields to ui elements
        this.lblTotal = view.findViewById(R.id.lblTotal);
        BiConsumer<Integer, String> setClickEvent = (id, screen) -> {
            view.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyState.GOTO = screen;
                }
            });
        };
        setClickEvent.accept(R.id.btnLogOut, MyConstants.LOGIN);
        setClickEvent.accept(R.id.btnIncome, MyConstants.INCOME);
        setClickEvent.accept(R.id.btnExpense, MyConstants.EXPENSE);
        setClickEvent.accept(R.id.btnAccountTransfer, MyConstants.TRANSFER_ACCOUNT);
        setClickEvent.accept(R.id.btnCategoryTransfer, MyConstants.TRANSFER_CATEGORY);
        setClickEvent.accept(R.id.btnRegister, MyConstants.REGISTER);
        setClickEvent.accept(R.id.btnReport, MyConstants.REPORT);
        setClickEvent.accept(R.id.btnSettings, MyConstants.SETTINGS);
        // populate info
        this.init();
    }

    private void init() {
        MyState.EDITING_TRANSACTION = null;
        MyApi.getTotal(view.getContext(), resp -> {
            this.lblTotal.setText(String.format("Total: %s", resp.response));
        }, data -> MyUtility.okDialog(getFragmentManager(), "Error", data.response));
    }

    @Override
    public void onBackClick() {
        MyState.GOTO = MyConstants.LOGIN;
    }
}
