package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.constants.MyState;
import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.dtos.ReportRequest;
import com.nathangawith.umkc.financeapp.dtos.ReportResponseDto;
import com.nathangawith.umkc.financeapp.dtos.TransactionDto;
import com.nathangawith.umkc.financeapp.http.MyApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReportActivity extends AppCompatActivity {

    private LinearLayout linearLayoutReport;
    private Button btnSelectStartDate;
    private TextView lblStartDate;
    private Button btnSelectEndDate;
    private TextView lblEndDate;
    private RadioGroup radioGrpBreakpoint;
    private RadioGroup radioGrpType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        // initialize fields to ui elements
        this.linearLayoutReport = findViewById(R.id.linearLayoutReport);
        this.btnSelectStartDate = findViewById(R.id.btnSelectStartDate);
        this.lblStartDate = findViewById(R.id.lblStartDate);
        this.btnSelectEndDate = findViewById(R.id.btnSelectEndDate);
        this.lblEndDate = findViewById(R.id.lblEndDate);
        this.radioGrpBreakpoint = findViewById(R.id.radioGrpBreakpoint);
        this.radioGrpType = findViewById(R.id.radioGrpType);
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
     * used this tutorial  for how to make a dynamic table https://www.youtube.com/watch?v=IaUjPIrKCrY
     * @param report report to build the table off of
     */
    private void buildTable(ReportResponseDto report) {
        List<TransactionDto> cells = report.Cells;
        boolean categoryType = report.Type.equals("Category");
        boolean monthBreakpoint = report.Breakpoint.equals("Month");
        TableRow header = new TableRow(this);
        int dateCharacters = 4 + (monthBreakpoint ? 1 + 2 : 0);
        List<String> rows = cells.stream().map(x -> x.Date.substring(0, dateCharacters)).distinct().collect(Collectors.toList());
        List<String> columns = cells.stream().map(x -> categoryType ? x.CategoryDescription : x.AccountDescription).distinct().collect(Collectors.toList());
        // populate header
        header.addView(new TextView(this));
        columns.forEach(col -> {
            TextView textView = new TextView(this);
            textView.setText(String.format("%s    ", col));
            header.addView(textView);
        });
        // populate rows
        List<TableRow> tableRows = new ArrayList<TableRow>();
        rows.forEach(row -> {
            TableRow tableRow = new TableRow(this);
            TextView rowLabel = new TextView(this);
            rowLabel.setText(row);
            tableRow.addView(rowLabel);
            columns.forEach(col -> {
                List<TransactionDto> candidateCells = cells.stream().filter(x -> x.Date.substring(0, dateCharacters).equals(row)
                    && (categoryType ? x.CategoryDescription : x.AccountDescription).equals(col)).collect(Collectors.toList());
                double cell = candidateCells.size() > 0 ? candidateCells.get(0).Amount : 0;
                TextView cellView = new TextView(this);
                cellView.setText(String.format("$%s", cell + ""));
                tableRow.addView(cellView);
            });
            tableRows.add(tableRow);
        });
        /// build table
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.addView(header);
        tableRows.forEach(tableRow -> tableLayout.addView(tableRow));
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tableLayout.setLayoutParams(params);
        this.linearLayoutReport.addView(tableLayout);
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
                MyState.REPORT_SELECTED_TYPE, x -> this.buildTable(x), x -> MyUtility.okDialog(this, "ERROR", x.response));
        } else {
            MyUtility.okDialog(this, "Not all required fields have been entered", values);
        }
    }

    public void btnBackClick(View view) {
        startActivity(new Intent(this, MenuActivity.class));
    }
}
