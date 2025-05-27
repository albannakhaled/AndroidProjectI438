package com.i438.quizappproject.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.i438.quizappproject.R;
import com.i438.quizappproject.model.Question;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class AddQuestionActivity extends AppCompatActivity {
    private EditText etQuestion, etOption1, etOption2, etOption3, etOption4, etCorrectAnswer;
    private FirebaseFirestore db;
    private static final int READ_REQUEST_CODE = 42;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        db = FirebaseFirestore.getInstance();
        initializeViews();
        setupButtons();
    }

    private void initializeViews() {
        etQuestion = findViewById(R.id.et_question);
        etOption1 = findViewById(R.id.et_option1);
        etOption2 = findViewById(R.id.et_option2);
        etOption3 = findViewById(R.id.et_option3);
        etOption4 = findViewById(R.id.et_option4);
        etCorrectAnswer = findViewById(R.id.et_correct_answer);
        progressBar = findViewById(R.id.progress_bar);

    }

    private void setupButtons() {
        findViewById(R.id.btn_add_question).setOnClickListener(v -> addQuestionToDB());
        findViewById(R.id.btn_clear).setOnClickListener(v -> clearFields());
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_add_from_csv).setOnClickListener(v -> pickCSVFile());

    }

    private void addQuestionToDB() {
        // Validate inputs
        List<String> options = Arrays.asList(
                etOption1.getText().toString().trim(),
                etOption2.getText().toString().trim(),
                etOption3.getText().toString().trim(),
                etOption4.getText().toString().trim()
        );

        String questionText = etQuestion.getText().toString().trim();
        String correctAnswer = etCorrectAnswer.getText().toString().trim();


        if (questionText.isEmpty() || options.contains("") || correctAnswer.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (!options.contains(correctAnswer)) {
//            Toast.makeText(this, "Correct answer must match one of the options", Toast.LENGTH_SHORT).show();
//            return;
//        }

        // Create question object
        Question question = new Question();
        question.setQuestionText(questionText);
        question.setOptions(options);
        question.setCorrectAnswer(correctAnswer);

        // Add to Firestore
        db.collection("questions")
                .add(question)
                .addOnSuccessListener(documentReference -> {
                    clearFields();
                    Toast.makeText(this, "Question added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error adding question: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void clearFields() {
        etQuestion.setText("");
        etOption1.setText("");
        etOption2.setText("");
        etOption3.setText("");
        etOption4.setText("");
        etCorrectAnswer.setText("");
    }

    private void pickCSVFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                progressBar.setVisibility(View.VISIBLE);
                new Thread(() -> {
                    loadQuestionsFromCSV(uri);
                    runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                }).start();
            }
        }
    }

    private void loadQuestionsFromCSV(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                String questionText = parts[0].trim();
                List<String> options = Arrays.asList(parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim());
                String correctAnswer = parts[5].trim();

                Question question = new Question();
                question.setQuestionText(questionText);
                question.setOptions(options);
                question.setCorrectAnswer(correctAnswer);

                db.collection("questions").add(question).addOnFailureListener(e ->
                        runOnUiThread(() -> Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()));
            }

            reader.close();
            runOnUiThread(() ->
                    Toast.makeText(this, "All questions uploaded!", Toast.LENGTH_LONG).show());

        } catch (IOException e) {
            runOnUiThread(() ->
                    Toast.makeText(this, "Error reading file: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }


}