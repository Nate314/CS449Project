package com.nathangawith.umkc.financeapp;

import android.widget.DatePicker;

import com.nathangawith.umkc.financeapp.activites.IncomeExpenseActivity;
import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.constants.MyState;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class B1_IncomeTest extends TestBase {

    public B1_IncomeTest() {
        super();
    }

    @Rule
    public ActivityTestRule<IncomeExpenseActivity> mActivityActivityTestRule = new ActivityTestRule<IncomeExpenseActivity>(IncomeExpenseActivity.class);
    private IncomeExpenseActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        MyState.SCREEN = MyConstants.INCOME;
        mActivity = mActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void a_enterInfo() {
        MyState.LAST_SCREEN = MyConstants.INCOME;
        String newDescription = "@" + (System.currentTimeMillis() + "") + (System.currentTimeMillis() + "") + (System.currentTimeMillis() + "");
        typeKeys(R.id.txtAmount, "asdf3asdf.asdf1asdf4asdf");
        assertEquals("3.14", getTextFromTextView(mActivity, R.id.txtAmount));
        typeKeys(R.id.txtDescription, newDescription);
        assertEquals(newDescription.substring(0, 20), getTextFromTextView(mActivity, R.id.txtDescription));
        onView(withId(R.id.btnSelectDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 12, 10));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btnSubmit)).perform(click());
        safeSleep(2500);
        assertTrue(getTextFromTextView(mActivity, R.id.txtAmount).equals(""));
        assertTrue(getTextFromTextView(mActivity, R.id.txtDescription).equals(""));
        assertTrue(getTextFromTextView(mActivity, R.id.lblDate).equals(""));
    }

    @Test
    public void z_finish(){ mActivity.finish(); }
}
