package com.nathangawith.umkc.financeapp;

import android.view.View;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.activites.LoginActivity;

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
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class A0_LoginActivityTest extends TestBase {

    public A0_LoginActivityTest() {
        super();
    }

    @Rule
    public ActivityTestRule<LoginActivity> mActivityActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);
    private LoginActivity mActivity = null;

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
            mActivity.findViewById(R.id.lblAppName),
            mActivity.findViewById(R.id.spinnerApiUrl),
            mActivity.findViewById(R.id.txtUsername),
            mActivity.findViewById(R.id.txtPassword),
            mActivity.findViewById(R.id.btnLogin),
            mActivity.findViewById(R.id.progressBar),
            mActivity.findViewById(R.id.lblToken)
        });
    }

    private void enterUsernameAndPassword(String username, String password) {
        onView(withId(R.id.txtUsername)).perform(typeText(username));
        onView(withId(R.id.txtPassword)).perform(typeText(password));
        closeSoftKeyboard();
    }

    @Test
    public void b_btnLogin() {
        // https://www.youtube.com/watch?v=dyyTr-zl5v0
        enterUsernameAndPassword("nathan", "password");
        onView(withId(R.id.btnLogin)).perform(click());
        safeSleep(5000);
        String token = ((TextView) mActivity.findViewById(R.id.lblToken)).getText().toString();
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9"));
    }

    @Test
    public void z_finish(){ mActivity.finish(); }
}
