package com.nathangawith.umkc.financeapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.constants.MyState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

public class TestBase {


    public void SET_NUMBER_OF_TRANSACTIONS(Activity activity, int number) {
        try
        {
            FileOutputStream fos = activity.openFileOutput("file.txt", Context.MODE_PRIVATE);
            fos.write(("" + number).getBytes());
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public int GET_NUMBER_OF_TRANSACTIONS(Activity activity) {
        String text = "0";
        try
        {
            FileInputStream fis = activity.openFileInput("file.txt");
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            text = new String(buffer);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return Integer.parseInt(text);
    }

    public TestBase() {
//        MyState.ROOT_API_URL = "http://pi.nathangawith.com:900/";
        MyState.ROOT_API_URL = "http://10.0.0.26:9090/";
    }

    public void testLaunch(View[] views) {
        System.out.println("----------------VIEW----------------");
        for (View view : views) {
            System.out.println(view.toString());
        }
        System.out.println("----------------VIEW----------------");
        safeSleep(2500);
        for (View view : views) {
            assertNotNull(view);
        }
    }

    public void typeKeys(int id, String textToType) {
        onView(withId(id)).perform(typeText(textToType));
        closeSoftKeyboard();
    }

    public String getTextFromTextView(Activity mActivity, int id) {
        return ((TextView) mActivity.findViewById(id)).getText().toString();
    }

    public void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
