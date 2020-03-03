package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.constants.MyState;

public class IncomeExpenseActivity extends AppCompatActivity {

    private TextView lblScreenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_expense);
        this.lblScreenName = findViewById(R.id.lblScreenName);
        this.lblScreenName.setText(MyState.SCREEN.equals(MyConstants.INCOME) ? "Income" : "Expense");
    }
}
