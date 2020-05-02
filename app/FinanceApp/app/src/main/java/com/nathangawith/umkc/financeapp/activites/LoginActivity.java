package com.nathangawith.umkc.financeapp.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.nathangawith.umkc.financeapp.constants.MyUtility;
import com.nathangawith.umkc.financeapp.http.MyApi;
import com.nathangawith.umkc.financeapp.constants.MyState;
import com.nathangawith.umkc.financeapp.R;
import com.nathangawith.umkc.financeapp.dtos.TokenResponseDto;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private Spinner spinnerApiUrl;
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private TextView lblToken;
    private ProgressBar progressBar;
    private ArrayList<String> spinnerApiUrlOptions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // initialize fields to ui elements
        LoginActivity me = this;
        this.spinnerApiUrl = findViewById(R.id.spinnerApiUrl);
        this.txtUsername = findViewById(R.id.txtUsername);
        this.txtPassword = findViewById(R.id.txtPassword);
        this.lblToken = findViewById(R.id.lblToken);
        this.btnLogin = findViewById(R.id.btnLogin);
        this.progressBar = findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);
        this.txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // Perform action on key press
                    me.btnLoginClick(null);
                    return true;
                }
                return false;
            }
        });
        // initialize spinner
        this.spinnerApiUrlOptions = new ArrayList<String>();
        if (MyUtility.isRunningTest) {
            spinnerApiUrlOptions.add("http://10.0.0.26:9090/");
            spinnerApiUrlOptions.add("http://pi.nathangawith.com:900/");
            spinnerApiUrlOptions.add("http://nathang2018:9090/");
        } else {
            spinnerApiUrlOptions.add("http://pi.nathangawith.com:900/");
            spinnerApiUrlOptions.add("http://10.0.0.26:9090/");
            spinnerApiUrlOptions.add("http://nathang2018:9090/");
        }
        MyUtility.initializeSpinner(this, this.spinnerApiUrl, this.spinnerApiUrlOptions, new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MyState.ROOT_API_URL = me.spinnerApiUrlOptions.get((int) id);
                System.out.println(MyState.ROOT_API_URL);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    /**
     * disable everything when loading
     * @param loading true if loading
     */
    private void loading(boolean loading) {
        boolean enabled = !loading;
        this.spinnerApiUrl.setEnabled(enabled);
        this.txtUsername.setEnabled(enabled);
        this.txtPassword.setEnabled(enabled);
        this.btnLogin.setEnabled(enabled);
        if (loading) {
            this.progressBar.setVisibility(View.VISIBLE);
            this.progressBar.setIndeterminate(true);
            this.progressBar.animate();
        } else {
            this.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * logs in the user by retrieving a JWT from the api
     * @param view button view
     */
    public void btnLoginClick(View view) {
        String username = this.txtUsername.getText().toString();
        String password = this.txtPassword.getText().toString();
        if (username != null && password != null) {
            username = username.toLowerCase();
            this.loading(true);
            MyApi.postLogin(this, new TokenResponseDto(), username, password, resp -> {
                this.loading(false);
                this.lblToken.setText(resp.token);
                if (resp.token.equals("null")) {
                    MyUtility.okDialog(this, "Invalid login", "Please contact your system administrator");
                } else {
                    MyState.TOKEN = resp.token;
                    this.getMoveToNextActivityThread().start();
                }
            }, data -> {
                this.loading(false);
                MyUtility.okDialog(this, "Error, Please contact your system administrator", data.getMessage());
            });
        } else {
            MyUtility.okDialog(this, "Error", "Please enter a username and password");
        }
    }

    /**
     * waits for 1 second before moving the user to the next screen
     */
    private Thread getMoveToNextActivityThread() {
        AppCompatActivity me = this;
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(MyUtility.isRunningTest ? 5000 : 0);
                    MyUtility.goToActivity(me, MainActivity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
