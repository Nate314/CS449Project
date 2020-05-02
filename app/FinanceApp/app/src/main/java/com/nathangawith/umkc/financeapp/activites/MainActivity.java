package com.nathangawith.umkc.financeapp.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.constants.MyConstants;
import com.nathangawith.umkc.financeapp.constants.MyState;
import com.nathangawith.umkc.financeapp.constants.MyUtility;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

// https://www.youtube.com/watch?v=bjYstsO1PgI
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private String lastScreen = null;
    private Map<Integer, String> screenNameMap;
    private Map<String, Class> screenNameFragmentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        screenNameMap = new HashMap<>();
        screenNameMap.put(R.id.nav_login, MyConstants.LOGIN);
        screenNameMap.put(R.id.nav_menu, MyConstants.MENU);
        screenNameMap.put(R.id.nav_income, MyConstants.INCOME);
        screenNameMap.put(R.id.nav_expense, MyConstants.EXPENSE);
        screenNameMap.put(R.id.nav_account_transfer, MyConstants.TRANSFER_ACCOUNT);
        screenNameMap.put(R.id.nav_category_transfer, MyConstants.TRANSFER_CATEGORY);
        screenNameMap.put(R.id.nav_register, MyConstants.REGISTER);
        screenNameMap.put(R.id.nav_report, MyConstants.REPORT);
        screenNameMap.put(R.id.nav_settings, MyConstants.SETTINGS);

        screenNameFragmentMap = new HashMap<>();
        screenNameFragmentMap.put(MyConstants.MENU, MenuActivity.class);
        screenNameFragmentMap.put(MyConstants.INCOME, IncomeExpenseActivity.class);
        screenNameFragmentMap.put(MyConstants.EXPENSE, IncomeExpenseActivity.class);
        screenNameFragmentMap.put(MyConstants.TRANSFER_CATEGORY, IncomeExpenseActivity.class);
        screenNameFragmentMap.put(MyConstants.TRANSFER_ACCOUNT, IncomeExpenseActivity.class);
        screenNameFragmentMap.put(MyConstants.REGISTER, RegisterActivity.class);
        screenNameFragmentMap.put(MyConstants.REPORT, ReportActivity.class);
        screenNameFragmentMap.put(MyConstants.SETTINGS, SettingsActivity.class);

        MainActivity me = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    me.safeSleep(100);
                    if (MyState.GOTO != null) {
                        me.goToFragment(MyState.GOTO);
                        MyState.GOTO = null;
                    }
                }
            }
        });
        thread.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                me.safeSleep(200);
                MyState.GOTO = MyConstants.MENU;
            }
        }).start();
    }

    private void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (java.lang.InterruptedException e) {}
    }

    private void goToFragment(String screen) {
        try {
            if (MyConstants.LOGIN.equals(screen)) {
                this.logout();
            } else {
                MyState.LAST_SCREEN = this.lastScreen;
                MyState.SCREEN = screen;
                Fragment fragment = (Fragment) screenNameFragmentMap.get(screen).newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                MyState.CURRENT_FRAGMENT = (IBackNavigable) fragment;
                this.lastScreen = screen;
            }
        } catch (java.lang.InstantiationException | java.lang.IllegalAccessException e) {}
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MenuActivity()).commit();
        int id = menuItem.getItemId();
        goToFragment(screenNameMap.get(id));
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        MyUtility.yesnoDialog(this, "Are you sure you want to log out?", "", yesnoResponse -> {
            if (yesnoResponse) {
                MyUtility.goToActivity(this, LoginActivity.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            System.out.println(MyState.CURRENT_FRAGMENT);
            if (MyState.CURRENT_FRAGMENT != null) {
                MyState.CURRENT_FRAGMENT.onBackClick();
            } else {
                this.logout();
            }
        }
    }
}
