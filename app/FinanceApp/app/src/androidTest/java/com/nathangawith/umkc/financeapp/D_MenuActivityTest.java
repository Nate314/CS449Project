package com.nathangawith.umkc.financeapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.activites.IncomeExpenseActivity;
import com.nathangawith.umkc.financeapp.activites.MenuActivity;
import com.nathangawith.umkc.financeapp.activites.RegisterActivity;
import com.nathangawith.umkc.financeapp.activites.ReportActivity;
import com.nathangawith.umkc.financeapp.activites.SettingsActivity;
import com.nathangawith.umkc.financeapp.constants.MyState;

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
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class D_MenuActivityTest extends TestBase {

    public D_MenuActivityTest() {
        super();
    }

    @Rule
    public ActivityTestRule<MenuActivity> mActivityActivityTestRule = new ActivityTestRule<MenuActivity>(MenuActivity.class);
    private MenuActivity mActivity = null;
    private Instrumentation.ActivityMonitor incomeExpenseMonitor = getInstrumentation().addMonitor(IncomeExpenseActivity.class.getName(), null, false);
    private Instrumentation.ActivityMonitor registerMonitor = getInstrumentation().addMonitor(RegisterActivity.class.getName(), null, false);
    private Instrumentation.ActivityMonitor reportMonitor = getInstrumentation().addMonitor(ReportActivity.class.getName(), null, false);
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
    public void a_testLaunch() {
        testLaunch(new View[] {
            mActivity.findViewById(R.id.btnSettings),
            mActivity.findViewById(R.id.btnLogOut),
            mActivity.findViewById(R.id.btnIncome),
            mActivity.findViewById(R.id.btnExpense),
            mActivity.findViewById(R.id.btnRegister)
        });
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
    public void b_btnIncome(){
        btnClickTest(R.id.btnIncome, incomeExpenseMonitor, "Income");
    }

    @Test
    public void c_btnExpense(){
        btnClickTest(R.id.btnExpense, incomeExpenseMonitor, "Expense");
    }

    @Test
    public void d_btnRegister(){
        btnClickTest(R.id.btnRegister, registerMonitor, "Register");
    }

    @Test
    public void e_btnReport(){
        btnClickTest(R.id.btnReport, reportMonitor, "Report");
    }

    @Test
    public void f_btnSettings(){
        btnClickTest(R.id.btnSettings, settingsMonitor, "Settings");
    }

    @Test
    public void z_finish(){ mActivity.finish(); }
}
