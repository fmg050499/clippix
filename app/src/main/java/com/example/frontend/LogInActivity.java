package com.example.frontend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.example.frontend.R.layout.activity_log_in;

public class LogInActivity extends AppCompatActivity {

    private Button logInButton, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_log_in);

        logInButton = findViewById(R.id.logInButton);

        logInButton.setOnClickListener((View view) ->{
            Intent intent = new Intent(LogInActivity.this,HomeActivity.class);
            startActivity(intent);
        });

        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener((View view) ->{
            Intent intent = new Intent(LogInActivity.this,HomeActivity.class);
            startActivity(intent);
        });
    }
}
