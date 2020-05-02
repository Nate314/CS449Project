package com.nathangawith.umkc.financeapp.constants;

import com.google.android.material.navigation.NavigationView;
import com.nathangawith.umkc.financeapp.activites.IBackNavigable;
import com.nathangawith.umkc.financeapp.dtos.TransactionDto;

public class MyState {
    public static String ROOT_API_URL;
    public static String TOKEN;
    public static String SCREEN;
    public static String LAST_SCREEN;
    public static TransactionDto EDITING_TRANSACTION = null;
    public static String REPORT_SELECTED_START_DATE;
    public static String REPORT_SELECTED_END_DATE;
    public static String REPORT_SELECTED_TYPE;
    public static String REPORT_SELECTED_BREAKPOINT;
    public static NavigationView.OnNavigationItemSelectedListener NAVIGATION_LISTENER;
    public static String GOTO;
    public static IBackNavigable CURRENT_FRAGMENT = null;
}
