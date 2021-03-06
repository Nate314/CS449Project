package com.nathangawith.umkc.financeapp;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.activites.IncomeExpenseActivity;
import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.constants.MyState;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.function.Consumer;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class B0_IncomeExpenseActivityTest extends TestBase {

    public B0_IncomeExpenseActivityTest() {
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
    public void a_testLaunch() {
        testLaunch(new View[] {
            mActivity.findViewById(R.id.lblScreenName),
            mActivity.findViewById(R.id.txtAmount),
            mActivity.findViewById(R.id.txtDescription),
            mActivity.findViewById(R.id.spinnerAccount),
            mActivity.findViewById(R.id.spinnerCategory),
            mActivity.findViewById(R.id.btnSelectDate),
            mActivity.findViewById(R.id.lblDate),
            mActivity.findViewById(R.id.btnSubmit)
        });
    }

    @Test
    public void b_lblIncomeScreenNameIsCorrect() {
        String lblScreenNameString = ((TextView) mActivity.findViewById(R.id.lblScreenName)).getText().toString();
        safeSleep(2500);
        assertEquals("Income", lblScreenNameString);
    }

    private boolean isRequiredErrorDisplayed() {
        String requiredMessage = "Enter all required Fields";
        boolean isDisplayed;
        try {
            onView(withText(requiredMessage)).check(matches(isDisplayed()));
            isDisplayed = true;
            safeSleep(500);
            onView(withText("OK")).perform(click());
        } catch (Exception exception) {
            isDisplayed = false;
        }
        return isDisplayed;
    }

    @Test
    public void c_fieldsRequired() {
        MyState.LAST_SCREEN = MyConstants.INCOME;
        String newDescription = "@" + System.currentTimeMillis();
        Consumer<Boolean> clickSubmitAndCheckForRequiredMessage = requiredDisplayed -> {
            onView(withId(R.id.btnSubmit)).perform(click());
            assertEquals(requiredDisplayed, isRequiredErrorDisplayed());
        };
        clickSubmitAndCheckForRequiredMessage.accept(true);
        typeKeys(R.id.txtAmount, "3.14");
        clickSubmitAndCheckForRequiredMessage.accept(true);
        typeKeys(R.id.txtDescription, newDescription);
        clickSubmitAndCheckForRequiredMessage.accept(true);
        onView(withId(R.id.btnSelectDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 12, 10));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btnSubmit)).perform(click());
        safeSleep(2500);
        assertFalse(isRequiredErrorDisplayed());
    }

    @Test
    public void z_finish(){ mActivity.finish(); }
}
