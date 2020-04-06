package com.nathangawith.umkc.financeapp.constants;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
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
import java.util.function.Consumer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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

    public static void okDialog(AppCompatActivity activity, String title, String text) {
        new MyDialog(title, text).show(activity.getSupportFragmentManager(), null);
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

}
