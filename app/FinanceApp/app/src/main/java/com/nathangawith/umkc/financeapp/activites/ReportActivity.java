package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.constants.MyState;
import com.nathangawith.umkc.financeapp.constants.MyUtility;

import java.util.ArrayList;
import java.util.Calendar;

public class ReportActivity extends AppCompatActivity {

    private Button btnSelectStartDate;
    private TextView lblStartDate;
    private Button btnSelectEndDate;
    private TextView lblEndDate;
    private Spinner spinnerBreakpoint;
    private Spinner spinnerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        // initialize fields to ui elements
        this.btnSelectStartDate = findViewById(R.id.btnSelectStartDate);
        this.lblStartDate = findViewById(R.id.lblStartDate);
        this.btnSelectEndDate = findViewById(R.id.btnSelectEndDate);
        this.lblEndDate = findViewById(R.id.lblEndDate);
        this.spinnerBreakpoint = findViewById(R.id.spinnerBreakpoint);
        this.spinnerType = findViewById(R.id.spinnerType);
        // initialize component
        this.init();
    }

    private void init() {
        // clear state vars
        MyState.REPORT_SELECTED_TYPE = null;
        MyState.REPORT_SELECTED_BREAKPOINT = null;
        MyState.REPORT_SELECTED_TYPE = null;
        MyState.REPORT_SELECTED_BREAKPOINT = null;
        // initialize on click listeners
        this.btnSelectStartDate.setOnClickListener(v -> MyUtility.btnDateClick(this, this.lblStartDate));
        this.btnSelectEndDate.setOnClickListener(v -> MyUtility.btnDateClick(this, this.lblEndDate));
        // initialize spinners
        ArrayList<String> breakpointOptions = new ArrayList<String>();
        breakpointOptions.add("Month");
        breakpointOptions.add("Year");
        MyUtility.initializeSpinner(this, this.spinnerBreakpoint, breakpointOptions,
                id -> MyState.REPORT_SELECTED_BREAKPOINT = breakpointOptions.get((int) (long) id));
        ArrayList<String> typeOptions = new ArrayList<String>();
        typeOptions.add("Category");
        typeOptions.add("Account");
        MyUtility.initializeSpinner(this, this.spinnerType, typeOptions,
                id -> MyState.REPORT_SELECTED_TYPE = typeOptions.get((int) (long) id));
    }

    public void btnSubmitClick(View view) {
        MyState.REPORT_SELECTED_START_DATE = this.lblStartDate.getText().toString();
        MyState.REPORT_SELECTED_END_DATE = this.lblEndDate.getText().toString();
        boolean allRequiredFields = true;
        allRequiredFields = allRequiredFields && (MyState.REPORT_SELECTED_START_DATE != null && MyState.REPORT_SELECTED_START_DATE.length() != 0);
        allRequiredFields = allRequiredFields && (MyState.REPORT_SELECTED_END_DATE   != null && MyState.REPORT_SELECTED_END_DATE.length()   != 0);
        allRequiredFields = allRequiredFields && (MyState.REPORT_SELECTED_BREAKPOINT != null && MyState.REPORT_SELECTED_BREAKPOINT.length() != 0);
        allRequiredFields = allRequiredFields && (MyState.REPORT_SELECTED_TYPE       != null && MyState.REPORT_SELECTED_TYPE.length()       != 0);
        String values = String.format("%s\n%s\n%s\n%s\n", MyState.REPORT_SELECTED_START_DATE,
                MyState.REPORT_SELECTED_END_DATE, MyState.REPORT_SELECTED_BREAKPOINT, MyState.REPORT_SELECTED_TYPE);
        System.out.println(values);
        if (allRequiredFields) {
            MyUtility.okDialog(this, "NOT YET IMPLEMENTED", values);
        } else {
            MyUtility.okDialog(this, "Not all required fields have been entered", values);
        }
    }

    public void btnBackClick(View view) {
        startActivity(new Intent(this, MenuActivity.class));
    }
}
