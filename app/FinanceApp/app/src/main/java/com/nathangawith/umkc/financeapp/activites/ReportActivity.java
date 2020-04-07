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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.constants.MyState;
import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.dtos.ReportRequest;
import com.nathangawith.umkc.financeapp.http.MyApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReportActivity extends AppCompatActivity {

    private Button btnSelectStartDate;
    private TextView lblStartDate;
    private Button btnSelectEndDate;
    private TextView lblEndDate;
    private RadioGroup radioGrpBreakpoint;
    private RadioGroup radioGrpType;
    private RadioButton radioBtnMonth;
    private RadioButton radioBtnYear;
    private RadioButton radioBtnCategory;
    private RadioButton radioBtnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        // initialize fields to ui elements
        this.btnSelectStartDate = findViewById(R.id.btnSelectStartDate);
        this.lblStartDate = findViewById(R.id.lblStartDate);
        this.btnSelectEndDate = findViewById(R.id.btnSelectEndDate);
        this.lblEndDate = findViewById(R.id.lblEndDate);
        this.radioGrpBreakpoint = findViewById(R.id.radioGrpBreakpoint);
        this.radioGrpType = findViewById(R.id.radioGrpType);
        this.radioBtnMonth = findViewById(R.id.radioBtnMonth);
        this.radioBtnYear = findViewById(R.id.radioBtnYear);
        this.radioBtnCategory = findViewById(R.id.radioBtnCategory);
        this.radioBtnAccount = findViewById(R.id.radioBtnAccount);
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
    }

    /**
     * When a radio button is clicked
     * @param view radio button that was clicked
     */
    public void radioBtnClick(View view) {
        RadioButton radioBtnBreakpoint = findViewById(this.radioGrpBreakpoint.getCheckedRadioButtonId());
        RadioButton radioBtnType = findViewById(this.radioGrpType.getCheckedRadioButtonId());
        MyState.REPORT_SELECTED_BREAKPOINT = radioBtnBreakpoint == null ? null : radioBtnBreakpoint.getText().toString();
        MyState.REPORT_SELECTED_TYPE = radioBtnType == null ? null : radioBtnType.getText().toString();
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
            MyApi.postReport(this,
                new Date(MyState.REPORT_SELECTED_START_DATE),
                new Date(MyState.REPORT_SELECTED_END_DATE),
                MyState.REPORT_SELECTED_BREAKPOINT,
                MyState.REPORT_SELECTED_TYPE, x -> {
                    MyUtility.okDialog(this, "SUCCESS", String.format("\"%s\", \"%s\", \"%s\", \"%s\"", x.StartDate, x.EndDate, x.Breakpoint, x.Type));
                }, x -> MyUtility.okDialog(this, "ERROR", x.response));
        } else {
            MyUtility.okDialog(this, "Not all required fields have been entered", values);
        }
    }

    public void btnBackClick(View view) {
        startActivity(new Intent(this, MenuActivity.class));
    }
}
