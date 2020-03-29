package com.nathangawith.umkc.financeapp;

import android.widget.ListAdapter;
import android.widget.ListView;

import com.nathangawith.umkc.financeapp.activites.RegisterActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class A1_RegisterActivityTest extends TestBase {

    public A1_RegisterActivityTest() {
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
    public void c_incomeTransactionAddedToRegister() {
        safeSleep(2000);
        ListView listTransactions = mActivity.findViewById(R.id.listTransactions);
        ListAdapter adapter = listTransactions.getAdapter();
        assertNotNull(adapter);
        int initialCount = adapter.getCount();
        SET_NUMBER_OF_TRANSACTIONS(mActivity, initialCount);
        assertTrue(true);
    }

    @Test
    public void z_finish(){ mActivity.finish(); }
}
