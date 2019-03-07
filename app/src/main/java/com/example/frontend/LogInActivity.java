package com.example.frontend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    FirebaseAuth authentication;

    EditText emailEditText, passwordEditText;

    Button logInButton, createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        authentication  = FirebaseAuth.getInstance();

        createAccountButton = findViewById(R.id.createAccountButton);
        logInButton=findViewById(R.id.logInButton);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        createAccountButton.setOnClickListener((View view)->{
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        logInButton.setOnClickListener((View view)->{
            logIn();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authentication.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    private void logIn() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

<<<<<<< HEAD
        if (email.equals("")) {
            Toast.makeText(this, "Email field is empty", Toast.LENGTH_LONG).show();
        }else {
            authentication.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener((Task<AuthResult> task) -> {
                        if(task.isSuccessful()){
                            Toast.makeText(this,"Successful",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(this, HomeActivity.class);
                            startActivity(intent);

                        }else {
                            Toast.makeText(this,"Failure",Toast.LENGTH_LONG).show();
                        }
                    });
        }
=======
        authentication.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener((Task<AuthResult> task) -> {
                    if(task.isSuccessful()){
                        Toast.makeText(this,"Successful",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }else {
                        Toast.makeText(this,"Failure",Toast.LENGTH_LONG).show();
                    }
                });

>>>>>>> a433c3fde37981f3e54d7010961a34c0ccccc1cf

    }
}
