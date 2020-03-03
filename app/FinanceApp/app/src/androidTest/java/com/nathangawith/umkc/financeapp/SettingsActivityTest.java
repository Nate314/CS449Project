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

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {

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
    public void testLaunch() {
        View[] views = new View[] {
            mActivity.findViewById(R.id.txtAddAccount),
            mActivity.findViewById(R.id.btnAddAccount),
            mActivity.findViewById(R.id.lblAccounts),
            mActivity.findViewById(R.id.txtAddIncomeCategory),
            mActivity.findViewById(R.id.btnAddIncomeCategory),
            mActivity.findViewById(R.id.lblIncomeCategories),
            mActivity.findViewById(R.id.txtAddExpenseCategory),
            mActivity.findViewById(R.id.btnAddExpenseCategory),
            mActivity.findViewById(R.id.lblExpenseCategories)
        };

        System.out.println("----------------VIEW----------------");
        for (View view : views) {
            System.out.println(view.toString());
        }
        System.out.println("----------------VIEW----------------");

        for (View view : views) {
            assertNotNull(view);
        }
    }

    private void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void typeKeys(int id, String textToType) {
        onView(withId(id)).perform(typeText(textToType));
        closeSoftKeyboard();
    }

    private void lblUpdatesGeneric(int txtID, int btnID, int lblID) {
        String newDescription = "@" + System.currentTimeMillis();
        typeKeys(txtID, newDescription);
        onView(withId(btnID)).perform(click());
        safeSleep(2000);
        String lblString = ((TextView) mActivity.findViewById(lblID)).getText().toString();
        assertTrue(lblString.indexOf(", " + newDescription) != -1);
    }

    @Test
    public void lblAccountsUpdates() {
        this.lblUpdatesGeneric(R.id.txtAddAccount, R.id.btnAddAccount, R.id.lblAccounts);
    }

    @Test
    public void lblIncomeCategoriesUpdates() {
        this.lblUpdatesGeneric(R.id.txtAddIncomeCategory, R.id.btnAddIncomeCategory, R.id.lblIncomeCategories);
    }

    @Test
    public void lblExpenseCategoriesUpdates() {
        this.lblUpdatesGeneric(R.id.txtAddExpenseCategory, R.id.btnAddExpenseCategory, R.id.lblExpenseCategories);
    }

}
