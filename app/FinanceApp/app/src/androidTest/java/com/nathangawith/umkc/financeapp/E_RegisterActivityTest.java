package com.nathangawith.umkc.financeapp;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.activites.RegisterActivity;
import com.nathangawith.umkc.financeapp.activites.SettingsActivity;
import com.nathangawith.umkc.financeapp.constants.MyState;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class E_RegisterActivityTest extends TestBase {

    public E_RegisterActivityTest() {
        super();
    }

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityActivityTestRule = new ActivityTestRule<RegisterActivity>(RegisterActivity.class);
    private RegisterActivity mActivity = null;

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
            mActivity.findViewById(R.id.lblScreenName),
            mActivity.findViewById(R.id.lblTotal),
            mActivity.findViewById(R.id.swipeRefresh),
            mActivity.findViewById(R.id.listTransactions)
        });
    }

    @Test
    public void b_lblTotalUpdates() {
        safeSleep(2000);
        String lblString = ((TextView) mActivity.findViewById(R.id.lblTotal)).getText().toString();
        assertTrue(lblString.indexOf("$") != -1);
    }

    @Test
    public void c_incomeTransactionAddedToRegister() {
        ListView listTransactions = mActivity.findViewById(R.id.listTransactions);
        ListAdapter adapter = listTransactions.getAdapter();
        assertNotNull(adapter);
        int newCount = adapter.getCount();
        assertTrue(newCount == GET_NUMBER_OF_TRANSACTIONS(mActivity) + 3);
    }

    @Test
    public void z_finish(){ mActivity.finish(); }
}
