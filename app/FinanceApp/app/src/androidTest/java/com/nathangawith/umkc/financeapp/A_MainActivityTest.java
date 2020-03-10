package com.nathangawith.umkc.financeapp;

import android.view.View;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.activites.MainActivity;

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
public class A_MainActivityTest extends TestBase {

    public A_MainActivityTest() {
        super();
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

//    @Test
//    public void testLaunch() {
//        View[] views = new View[] {
//            mActivity.findViewById(R.id.lblAppName),
//            mActivity.findViewById(R.id.spinnerApiUrl),
//            mActivity.findViewById(R.id.txtUsername),
//            mActivity.findViewById(R.id.txtPassword),
//            mActivity.findViewById(R.id.btnLogin),
//            mActivity.findViewById(R.id.progressBar),
//            mActivity.findViewById(R.id.lblToken)
//        };
//
//        System.out.println("----------------VIEW----------------");
//        for (View view : views) {
//            System.out.println(view.toString());
//        }
//        System.out.println("----------------VIEW----------------");
//
//        for (View view : views) {
//            assertNotNull(view);
//        }
//    }

    private void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void enterUsernameAndPassword(String username, String password) {
        onView(withId(R.id.txtUsername)).perform(typeText(username));
        onView(withId(R.id.txtPassword)).perform(typeText(password));
        closeSoftKeyboard();
    }

//    @Test
//    public void txtFieldsEditable(){
//        enterUsernameAndPassword("nathan", "password");
//
//        assertEquals(((EditText) mActivity.findViewById(R.id.txtUsername)).getText().toString(), "nathan");
//        assertEquals(((EditText) mActivity.findViewById(R.id.txtPassword)).getText().toString(), "password");
//    }

    @Test
    public void btnLogin(){
        // https://www.youtube.com/watch?v=dyyTr-zl5v0
        enterUsernameAndPassword("nathan", "password");
        onView(withId(R.id.btnLogin)).perform(click());
        safeSleep(5000);
        String token = ((TextView) mActivity.findViewById(R.id.lblToken)).getText().toString();
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9"));
    }
}
