package com.i438.quizappproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.i438.quizappproject.MainActivity;
import com.i438.quizappproject.R;

public class SignInActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button signInButton;
    private TextView signUpLink;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;// to handle Firebase authentication

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        usernameInput = findViewById(R.id.input_username);
        passwordInput = findViewById(R.id.input_password);
        signInButton = findViewById(R.id.btn_sign_in);
        signUpLink = findViewById(R.id.link_to_signup);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();// Initialize Firebase Auth

        signInButton.setOnClickListener(view -> signIn());

        signUpLink.setOnClickListener(view -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });
    }

    private void signIn() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            usernameInput.setError("Username is required");
            usernameInput.requestFocus();
            return;
        }
        String usernamePattern = "^[a-zA-Z0-9_]+$";// pattern to allow only letters, numbers, and underscores
        if (!username.matches(usernamePattern)) {
            usernameInput.setError("Username can only contain letters, numbers, and underscores");
            usernameInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String email = username + "@i438.com";
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> { // task holds the result of the sign-in operation
                    progressBar.setVisibility(View.GONE);// hide the progress bar after the task is complete
                    if (task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finish(); // close the SignInActivity so the user can't return to it by pressing back
                    } else {
                        Toast.makeText(SignInActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
