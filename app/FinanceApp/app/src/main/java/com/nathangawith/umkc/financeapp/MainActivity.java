package com.nathangawith.umkc.financeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

class TokenResponseDto {
    public String token;
}

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {

    private Spinner spinnerApiUrl;
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private TextView lblToken;
    private ProgressBar progressBar;
    private ArrayList<String> spinnerApiUrlOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // create layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialize fields to ui elements
        this.spinnerApiUrl = findViewById(R.id.spinnerApiUrl);
        this.txtUsername = findViewById(R.id.txtUsername);
        this.txtPassword = findViewById(R.id.txtPassword);
        this.lblToken = findViewById(R.id.lblToken);
        this.btnLogin = findViewById(R.id.btnLogin);
        this.progressBar = findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);
        // initialize spinner
        this.spinnerApiUrlOptions = new ArrayList<String>();
        spinnerApiUrlOptions.add("http://pi.nathangawith.com:900/");
        spinnerApiUrlOptions.add("http://10.0.0.26:9090/");
        spinnerApiUrlOptions.add("http://nathang2018:9090/");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, this.spinnerApiUrlOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerApiUrl.setAdapter(adapter);
        spinnerApiUrl.setOnItemSelectedListener(this);
        this.onItemSelected(null, null, 0, 0);
    }

    /**
     * when an item is selected from the api dropdown, save to ROOT_API_URL
     * @param parent
     * @param view
     * @param pos
     * @param id id of the item selected
     */
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        MyState.ROOT_API_URL = this.spinnerApiUrlOptions.get((int) id);
        System.out.println(MyState.ROOT_API_URL);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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
        this.loading(true);
        MyApi.postLogin(getApplicationContext(), new TokenResponseDto(), username, password, resp -> {
            this.loading(false);
            this.lblToken.setText(resp.token);
            if (resp.token.equals("null")) {
                new MyDialog("Invalid login", "Please contact your system administrator")
                        .show(getSupportFragmentManager(), null);
            } else {
                MyState.TOKEN = resp.token;
                this.getMoveToNextActivityThread().start();
            }
        }, data -> {
            this.loading(false);
            new MyDialog("Error, Please contact your system administrator", data.getMessage())
                    .show(getSupportFragmentManager(), null);
        });
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
                    Thread.sleep(2000);
                    startActivity(new Intent(me, SettingsActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
