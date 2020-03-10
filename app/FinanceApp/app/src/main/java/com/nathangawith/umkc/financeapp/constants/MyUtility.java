package com.nathangawith.umkc.financeapp.constants;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nathangawith.umkc.financeapp.dialogs.MyDialog;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;

public class MyUtility {

    public static void initializeSpinner(Context context, Spinner spinner, ArrayList<String> options, AdapterView.OnItemSelectedListener listener) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
        listener.onItemSelected(null, null, 0, 0);
    }

    public static void okDialog(FragmentManager manageer, String title, String text) {
        new MyDialog(title, text).show(manageer, null);
    }

}
