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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth authentication;
    FirebaseFirestore db;

    EditText emailEditText, passwordEditText,usernameEditText;

    Button signUpButton,logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        authentication = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        signUpButton = findViewById(R.id.signUpButton);
        logInButton = findViewById(R.id.logInButton);

        signUpButton.setOnClickListener((View view) -> {
            signUp();
        });

        logInButton.setOnClickListener((View view)->{
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        });

    }

    private void signUp() {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(username.isEmpty()){
            usernameEditText.setError("username is required");
            usernameEditText.requestFocus();
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 8) {
            Toast.makeText(this, "Password must be not less than 8 characters", Toast.LENGTH_LONG).show();
        } else {
            authentication.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Task<AuthResult> task) -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "success", Toast.LENGTH_LONG).show();

                            String userId = authentication.getCurrentUser().getUid();

                            Map<String, Object> accounts = new HashMap<>();
                            accounts.put("username",username);
                            accounts.put("userId", userId);
                            db.collection("accounts")
                                    .add(accounts)
                                    .addOnSuccessListener((DocumentReference ref)->{
                                    })
                                    .addOnFailureListener((Exception ex)->{
                                    });
                            Intent intent = new Intent(this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

}