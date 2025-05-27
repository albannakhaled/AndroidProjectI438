package com.i438.quizappproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.i438.quizappproject.storage.SharedPrefsManager;
import com.i438.quizappproject.ui.AddQuestionActivity;
import com.i438.quizappproject.ui.HistoryActivity;
import com.i438.quizappproject.ui.QuizActivity;
import com.i438.quizappproject.ui.SignInActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button startQuizButton, viewHistoryButton, logoutButton;
    //entry point for all Firebase Authentication operations
    private FirebaseAuth mAuth;
    private TextView textQuizzes, textBestScore;
    private SharedPrefsManager prefsManager; // handles local persistence

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeText = findViewById(R.id.text_welcome);
        startQuizButton = findViewById(R.id.btn_start_quiz);
        viewHistoryButton = findViewById(R.id.btn_view_history);
        logoutButton = findViewById(R.id.btn_logout);
        textQuizzes = findViewById(R.id.text_quizzes);
        textBestScore = findViewById(R.id.text_best_score);

        prefsManager = new SharedPrefsManager(this);
        updateStats();
        mAuth = FirebaseAuth.getInstance();
        // checks if the user is authenticated
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // if the user is not authenticated, redirect to SignInActivity
        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();// closes the current activity (MainActivity)
        } else {
            String username = currentUser.getEmail(); // get the email of the current user
            welcomeText.setText(username.replace("@i438.com", "")); // removes the domain part from the email
        }

        startQuizButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, QuizActivity.class))
        );


        viewHistoryButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        });

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            prefsManager.clearCache();
            finish();
        });
        findViewById(R.id.fab_add_question).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddQuestionActivity.class)));

    }

    private void updateStats() {
        int quizCount = prefsManager.getQuizCount();
        float bestScore = prefsManager.getBestScore();

        textQuizzes.setText(String.valueOf(quizCount));
        textBestScore.setText(String.format(Locale.getDefault(), "%.1f%%", bestScore));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // update stats when the activity is resumed
        updateStats();
    }

}