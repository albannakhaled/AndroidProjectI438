package com.i438.quizappproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.i438.quizappproject.MainActivity;
import com.i438.quizappproject.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_MS = 2000; // 2 seconds

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // create this layout next

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                // User is signed in
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // No user signed in
                startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            }
            finish(); // prevent back navigation to splash
        }, SPLASH_TIME_MS);
    }
}