package com.nathangawith.umkc.financeapp.constants;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.activites.ReportActivity;
import com.nathangawith.umkc.financeapp.dialogs.MyDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.function.Consumer;

import androidx.appcompat.app.AppCompatActivity;

public class MyUtility {

    public static boolean isRunningTest = false;

    public static void initializeSpinner(Context context, Spinner spinner, ArrayList<String> options, AdapterView.OnItemSelectedListener listener) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
        listener.onItemSelected(null, null, 0, 0);
    }

    public static void initializeSpinner(Context context, Spinner spinner, ArrayList<String> options, Consumer<Long> onItemSelectedConsumer) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedConsumer.accept(id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        };
        spinner.setOnItemSelectedListener(listener);
        listener.onItemSelected(null, null, 0, 0);
    }

    /**
     * @param spinner spinner to set the items on
     * @param type type of the respCollection items
     * @param respCollection collection of items used for spinner
     * @param onSelection when an item is selected, this function is called
     * @param <T> DBAccount or DBCategory
     */
    public static <T extends Object> void setSpinnerItems(Context context, Spinner spinner, Class<T> type, Collection<T> respCollection, Consumer<T> onSelection) {
        try {
            ArrayList<T> respArrayList = new ArrayList<T>(respCollection);
            ArrayList<String> descriptions = new ArrayList<String>();
            for (T x : respArrayList) {
                System.out.println(x);
                descriptions.add(type.getField("Description").get(x).toString());
            }
            MyUtility.initializeSpinner(context, spinner, descriptions, new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (respArrayList.size() > id) {
                        onSelection.accept(respArrayList.get((int) id));
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void okDialog(AppCompatActivity activity, String title, String text) {
        new MyDialog(title, text).show(activity.getSupportFragmentManager(), null);
    }

    public static void okDialog(AppCompatActivity activity, String title) {
        MyUtility.okDialog(activity, title, "");
    }

    public static void yesnoDialog(AppCompatActivity activity, String title, String text, Consumer<Boolean> yesNoConsumer) {
        new MyDialog(title, text, new String[]{"YES", "NO"}, btnText -> yesNoConsumer.accept(btnText.equals("YES"))).show(activity.getSupportFragmentManager(), null);
    }

    public static <T> void goToActivity(Activity thisActivity, Class<T> nextActivity) {
        thisActivity.startActivity(new Intent(thisActivity, nextActivity));
    }

    /**
     * code inspired from https://youtu.be/hwe1abDO2Ag
     * @param context Context of the activity where the button was pressed
     * @param label TextView to update
     */
    public static <T> void btnDateClick(Context context, TextView label) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(context, R.style.Theme_AppCompat_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = String.format("%d/%d/%d", month + 1, dayOfMonth, year);
                        label.setText(date);
                    }
                },
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.show();
    }

    /**
     * returns the money string version of the double amount
     * @param amount amount of money
     * @return string version
     */
    public static String formatAsMoney(double amount) {
        boolean negativeBalance = amount < 0;
        amount = negativeBalance ? -1 * amount : amount;
        String stramt = "" + amount;
        String[] parts = stramt.split("\\.");
        String dollars = parts.length > 0 ? parts[0] : "0";
        String cents = parts.length > 1 ? parts[1] : "00";
        cents = cents.substring(0, Math.min(2, cents.length()));
        cents = cents.length() <= 1 ? cents + "0" : cents;
        cents = cents.length() <= 1 ? cents + "0" : cents;
        return String.format("%s$%s.%s", negativeBalance ? "-" : "", dollars, cents);
    }

    /**
     * @param date in the form YYYY-MM-DD
     * @return date in the form MM/DD/YYYY
     */
    public static String sqlDateToJavaDate(String date) throws Exception {
        String result = "";
        String[] parts = date.split("-");
        if (parts.length != 3) throw new Exception("Date passed should be in the form YYYY-MM-DD");
        return String.format("%s/%s/%s", parts[1], parts[2], parts[0]);
    }

}
