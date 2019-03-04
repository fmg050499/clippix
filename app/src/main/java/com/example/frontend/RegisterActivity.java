package com.example.frontend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener((View view)->{
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

    }
}
