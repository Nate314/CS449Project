package com.nathangawith.umkc.financeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.function.Consumer;

class TokenResponseDto {
    public String token;
}

public class MainActivity extends AppCompatActivity {

    private EditText txtApi;
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;
    private TextView lblToken;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.txtApi = findViewById(R.id.txtApi);
        this.txtUsername = findViewById(R.id.txtUsername);
        this.txtPassword = findViewById(R.id.txtPassword);
        this.lblToken = findViewById(R.id.lblToken);
        this.btnLogin = findViewById(R.id.btnLogin);
        this.progressBar = findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.INVISIBLE);
    }

    private void loading(boolean loading) {
        boolean enabled = !loading;
        this.txtApi.setEnabled(enabled);
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

    public void btnLoginClick(View view) {
        MyState.ROOT_API_URL = this.txtApi.getText().toString();
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

    private Thread getMoveToNextActivityThread() {
        AppCompatActivity me = this;
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    startActivity(new Intent(me, SettingsActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
