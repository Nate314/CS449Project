package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;

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
        }, data -> MyUtility.okDialog(this, "Error", data.response));
    }

    private <T> void goToActivity(Class<T> activity, String screen) {
        MyState.SCREEN = screen;
        MyState.LAST_SCREEN = MyConstants.MENU;
        MyUtility.goToActivity(this, activity);
    }

    /**
     * sends user to the Income screen
     * @param view button view
     */
    public void btnIncomeClick(View view) {
        this.goToActivity(IncomeExpenseActivity.class, MyConstants.INCOME);
    }

    /**
     * sends user to the Expense screen
     * @param view button view
     */
    public void btnExpenseClick(View view) {
        this.goToActivity(IncomeExpenseActivity.class, MyConstants.EXPENSE);
    }

    /**
     * sends user to the Settings screen
     * @param view button view
     */
    public void btnSettingsClick(View view) {
        this.goToActivity(SettingsActivity.class, MyConstants.SETTINGS);
    }

    /**
     * sends user to the Register screen
     * @param view button view
     */
    public void btnRegisterClick(View view) {
        this.goToActivity(RegisterActivity.class, MyConstants.REGISTER);
    }

    /**
     * sends user to the Report screen
     * @param view button view
     */
    public void btnReportClick(View view) {
        this.goToActivity(ReportActivity.class, MyConstants.REPORT);
    }

    /**
     * sends user to the Login screen
     * @param view button view
     */
    public void btnLogOutClick(View view) {
        this.goToActivity(LoginActivity.class, MyConstants.LOGIN);
    }
}
