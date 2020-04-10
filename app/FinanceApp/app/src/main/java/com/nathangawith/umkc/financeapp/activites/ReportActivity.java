package com.nathangawith.umkc.financeapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

    private TableLayout tableLayout;
    private Button btnSelectStartDate;
    private TextView lblStartDate;
    private Button btnSelectEndDate;
    private TextView lblEndDate;
    private RadioGroup radioGrpBreakpoint;
    private RadioGroup radioGrpType;
    private TextView lblLabel1;
    private TextView lblLabel2;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        // initialize fields to ui elements
        this.tableLayout = findViewById(R.id.tableReport);
        this.btnSelectStartDate = findViewById(R.id.btnSelectStartDate);
        this.lblStartDate = findViewById(R.id.lblStartDate);
        this.btnSelectEndDate = findViewById(R.id.btnSelectEndDate);
        this.lblEndDate = findViewById(R.id.lblEndDate);
        this.radioGrpBreakpoint = findViewById(R.id.radioGrpBreakpoint);
        this.radioGrpType = findViewById(R.id.radioGrpType);
        this.lblLabel1 = findViewById(R.id.lblLabel1);
        this.lblLabel2 = findViewById(R.id.lblLabel2);
        this.btnSubmit = findViewById(R.id.btnSubmit);
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
     * @param content text to place in the cell
     * @param title if it's a title, the text will be bolded
     * @param color color to use in the cell
     * @return TextView for a cell
     */
    private TextView getCellTextView(String content, boolean title, int color) {
        TextView textView = new TextView(this);
        textView.setText(String.format("    %s    ", content));
        textView.setTextSize(20);
        textView.setBackground(getDrawable(R.drawable.border));
        textView.setTextColor(color);
        if (title) textView.setTypeface(null, Typeface.BOLD);
        return textView;
    }

    /**
     * wrapper function for the above getCellTextView function
     * @param content text to place in the cell
     * @param title if it's a title, the text will be bolded
     * @return TextView for a cell
     */
    private TextView getCellTextView(String content, boolean title) {
        return this.getCellTextView(content, title, getColor(R.color.black));
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
        columns.forEach(col -> header.addView(this.getCellTextView(col, true)));
        header.addView(this.getCellTextView("Total", true));
        // populate rows
        List<TableRow> tableRows = new ArrayList<TableRow>();
        List<Double> totals = new ArrayList<Double>();
        columns.stream().forEach(x -> totals.add(new Double(0)));
        rows.forEach(row -> {
            TableRow tableRow = new TableRow(this);
            tableRow.addView(this.getCellTextView(row, true));
            double total = 0;
            for (int i = 0; i < columns.size(); i++) {
                String col = columns.get(i);
                List<TransactionDto> candidateCells = cells.stream().filter(x -> x.Date.substring(0, dateCharacters).equals(row)
                        && (categoryType ? x.CategoryDescription : x.AccountDescription).equals(col)).collect(Collectors.toList());
                double cell = candidateCells.size() > 0 ? candidateCells.get(0).Amount : 0;
                tableRow.addView(this.getCellTextView(MyUtility.formatAsMoney(cell), false, getColor(cell >= 0 ? R.color.green : R.color.red)));
                totals.set(i, totals.get(i) + cell);
                total += cell;
            }
            tableRow.addView(this.getCellTextView(MyUtility.formatAsMoney(total), false, getColor(total >= 0 ? R.color.green : R.color.red)));
            tableRows.add(tableRow);
        });
        TableRow totalRow = new TableRow(this);
        totalRow.addView(this.getCellTextView("Total", true));
        totals.forEach(total -> totalRow.addView(this.getCellTextView(MyUtility.formatAsMoney(total), false, getColor(total >= 0 ? R.color.green : R.color.red))));
        totalRow.addView(this.getCellTextView("", true));
        tableRows.add(totalRow);
        /// build table
        this.toggleTableView();
        this.tableLayout.removeAllViews();
        this.tableLayout.addView(header);
        tableRows.forEach(tableRow -> this.tableLayout.addView(tableRow));
    }

    /**
     * @return true if in table view
     */
    private boolean isInTableView() {
        return this.btnSelectEndDate.getVisibility() == View.GONE;
    }

    /**
     * toggles between Report and Report View view
     */
    private void toggleTableView() {
        int visibility = this.isInTableView() ? View.VISIBLE : View.GONE;
        this.btnSelectStartDate.setVisibility(visibility);
        this.lblStartDate.setVisibility(visibility);
        this.btnSelectEndDate.setVisibility(visibility);
        this.lblEndDate.setVisibility(visibility);
        this.radioGrpBreakpoint.setVisibility(visibility);
        this.radioGrpType.setVisibility(visibility);
        this.lblLabel1.setVisibility(visibility);
        this.lblLabel2.setVisibility(visibility);
        this.btnSubmit.setVisibility(visibility);
        this.tableLayout.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    /**
     * when a radio button is clicked
     * @param view radio button that was clicked
     */
    public void radioBtnClick(View view) {
        RadioButton radioBtnBreakpoint = findViewById(this.radioGrpBreakpoint.getCheckedRadioButtonId());
        RadioButton radioBtnType = findViewById(this.radioGrpType.getCheckedRadioButtonId());
        MyState.REPORT_SELECTED_BREAKPOINT = radioBtnBreakpoint == null ? null : radioBtnBreakpoint.getText().toString();
        MyState.REPORT_SELECTED_TYPE = radioBtnType == null ? null : radioBtnType.getText().toString();
    }

    /**
     * when the Submit button is clicked
     * @param view button view
     */
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

    /**
     * when the back button is clicked
     * @param view button view
     */
    public void btnBackClick(View view) {
        if (this.isInTableView()) {
            this.toggleTableView();
        } else {
            startActivity(new Intent(this, MenuActivity.class));
        }
    }
}
