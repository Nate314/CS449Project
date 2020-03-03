package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.constants.MyState;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    /**
     * sends user to the Income screen
     * @param view button view
     */
    public void btnIncomeClick(View view) {
        MyState.SCREEN = MyConstants.INCOME;
        startActivity(new Intent(this, IncomeExpenseActivity.class));
    }

    /**
     * sends user to the Expense screen
     * @param view button view
     */
    public void btnExpenseClick(View view) {
        MyState.SCREEN = MyConstants.EXPENSE;
        startActivity(new Intent(this, IncomeExpenseActivity.class));
    }

    /**
     * sends user to the Settings screen
     * @param view button view
     */
    public void btnSettingsClick(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    /**
     * sends user to the Login screen
     * @param view button view
     */
    public void btnLogOutClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
