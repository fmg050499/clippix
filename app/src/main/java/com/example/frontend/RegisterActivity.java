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

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth authentication;

    EditText emailEditText, passwordEditText;

    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        authentication = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener((View view)->{
            signUp();
        });

    }

    private void signUp() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        int length = password.length();
        if (email.equals("")){
            Toast.makeText(this, "Email field is empty",Toast.LENGTH_LONG).show();
        }else{
            if (length < 8){
                Toast.makeText(this, "Password must be not less than 8 characters",Toast.LENGTH_LONG).show();
            } else {
                authentication.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener((Task<AuthResult> task) -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "success", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(this, HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }



    }

}
