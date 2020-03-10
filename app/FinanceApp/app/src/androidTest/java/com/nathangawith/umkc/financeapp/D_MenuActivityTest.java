package com.nathangawith.umkc.financeapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import android.widget.TextView;

import com.google.common.util.concurrent.Monitor;
import com.nathangawith.umkc.financeapp.activites.IncomeExpenseActivity;
import com.nathangawith.umkc.financeapp.activites.MainActivity;
import com.nathangawith.umkc.financeapp.activites.MenuActivity;
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
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class D_MenuActivityTest extends TestBase {

    public D_MenuActivityTest() {
        super();
    }

    @Rule
    public ActivityTestRule<MenuActivity> mActivityActivityTestRule = new ActivityTestRule<MenuActivity>(MenuActivity.class);
    private MenuActivity mActivity = null;
    private Instrumentation.ActivityMonitor incomeexpenseMonitor = getInstrumentation().addMonitor(IncomeExpenseActivity.class.getName(), null, false);
    private Instrumentation.ActivityMonitor registerMonitor = getInstrumentation().addMonitor(RegisterActivity.class.getName(), null, false);
    private Instrumentation.ActivityMonitor settingsMonitor = getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        MyState.ROOT_API_URL = "http://pi.nathangawith.com:900/";
        mActivity = mActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void testLaunch() {
        View[] views = new View[] {
            mActivity.findViewById(R.id.btnSettings),
            mActivity.findViewById(R.id.btnLogOut),
            mActivity.findViewById(R.id.btnIncome),
            mActivity.findViewById(R.id.btnExpense),
            mActivity.findViewById(R.id.btnRegister)
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

    private void btnClickTest(int id, Instrumentation.ActivityMonitor monitor, String title) {
        // https://www.youtube.com/watch?v=vXRoVIGttO4
        assertNotNull(mActivity.findViewById(id));
        onView(withId(id)).perform(click());
        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 1000);
        assertNotNull(secondActivity);
        safeSleep(500);
        assertEquals(title, ((TextView) secondActivity.findViewById(R.id.lblScreenName)).getText().toString());
        secondActivity.finish();
    }

    @Test
    public void btnIncome(){
        btnClickTest(R.id.btnIncome, incomeexpenseMonitor, "Income");
    }

    @Test
    public void btnExpense(){
        btnClickTest(R.id.btnExpense, incomeexpenseMonitor, "Expense");
    }

    @Test
    public void btnRegister(){
        btnClickTest(R.id.btnRegister, registerMonitor, "Register");
    }

    @Test
    public void btnSettings(){
        btnClickTest(R.id.btnSettings, settingsMonitor, "Settings");
    }
}
