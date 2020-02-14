package com.nathangawith.umkc.financeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.function.Consumer;

class TokenResponseDto {
    public String token;
}

public class MainActivity extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtPassword;
    private TextView lblToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.txtUsername = findViewById(R.id.txtUsername);
        this.txtPassword = findViewById(R.id.txtPassword);
        this.lblToken = findViewById(R.id.lblToken);
    }

    public void btnLoginClick(View view) {
        String username = this.txtUsername.getText().toString();
        String password = this.txtPassword.getText().toString();
        MyApi.postLogin(getApplicationContext(), new TokenResponseDto(), username, password, resp -> {
            if (resp.token.equals("null")) {
                new MyDialog("Invalid login", "Please contact your system administrator")
                        .show(getSupportFragmentManager(), null);
            }
            this.lblToken.setText(resp.token);
        });
    }
}
