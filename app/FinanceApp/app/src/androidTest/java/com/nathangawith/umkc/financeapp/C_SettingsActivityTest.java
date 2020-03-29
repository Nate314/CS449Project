package com.nathangawith.umkc.financeapp;

import android.view.View;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.activites.SettingsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class C_SettingsActivityTest extends TestBase {

    public C_SettingsActivityTest() {
        super();
    }

    @Rule
    public ActivityTestRule<SettingsActivity> mActivityActivityTestRule = new ActivityTestRule<SettingsActivity>(SettingsActivity.class);
    private SettingsActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void a_testLaunch() {
        testLaunch(new View[] {
            mActivity.findViewById(R.id.txtAddAccount),
            mActivity.findViewById(R.id.btnAddAccount),
            mActivity.findViewById(R.id.lblAccounts),
            mActivity.findViewById(R.id.txtAddIncomeCategory),
            mActivity.findViewById(R.id.btnAddIncomeCategory),
            mActivity.findViewById(R.id.lblIncomeCategories),
            mActivity.findViewById(R.id.txtAddExpenseCategory),
            mActivity.findViewById(R.id.btnAddExpenseCategory),
            mActivity.findViewById(R.id.lblExpenseCategories)
        });
    }

    private void lblUpdatesGeneric(int txtID, int btnID, int lblID) {
        String newDescription = "@" + System.currentTimeMillis();
        typeKeys(txtID, newDescription);
        onView(withId(btnID)).perform(click());
        safeSleep(2000);
        String lblString = ((TextView) mActivity.findViewById(lblID)).getText().toString();
        System.out.println("--------lblString--------");
        System.out.println(lblString);
        System.out.println("--------lblString--------");
        assertTrue(lblString.indexOf(", " + newDescription) != -1);
    }

    @Test
    public void b_lblAccountsUpdates() {
        this.lblUpdatesGeneric(R.id.txtAddAccount, R.id.btnAddAccount, R.id.lblAccounts);
    }

    @Test
    public void c_lblIncomeCategoriesUpdates() {
        this.lblUpdatesGeneric(R.id.txtAddIncomeCategory, R.id.btnAddIncomeCategory, R.id.lblIncomeCategories);
    }

    @Test
    public void d_lblExpenseCategoriesUpdates() {
        this.lblUpdatesGeneric(R.id.txtAddExpenseCategory, R.id.btnAddExpenseCategory, R.id.lblExpenseCategories);
    }

    @Test
    public void z_finish(){ mActivity.finish(); }
}
