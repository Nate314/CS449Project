package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.constants.MyState;
import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.http.MyApi;

public class MenuActivity extends AppCompatActivity {

    private TextView lblTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // initialize fields to ui elements
        this.lblTotal = findViewById(R.id.lblTotal);
        // populate info
        this.init();
    }

    private void init() {
        MyApi.getTotal(getApplicationContext(), resp -> {
            this.lblTotal.setText(String.format("Total: %s", resp.response));
        }, data -> MyUtility.okDialog(getSupportFragmentManager(), "Error", data.response));
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
     * sends user to the Register screen
     * @param view button view
     */
    public void btnRegisterClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    /**
     * sends user to the Login screen
     * @param view button view
     */
    public void btnLogOutClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
