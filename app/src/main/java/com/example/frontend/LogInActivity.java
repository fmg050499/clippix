package com.example.frontend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogInActivity extends AppCompatActivity {

    Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        createAccountButton = findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener((View view)->{
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
