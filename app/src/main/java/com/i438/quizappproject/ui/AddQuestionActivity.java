package com.i438.quizappproject.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.i438.quizappproject.R;
import com.i438.quizappproject.model.Question;

import java.util.Arrays;
import java.util.List;

public class AddQuestionActivity extends AppCompatActivity {
    private EditText etQuestion, etOption1, etOption2, etOption3, etOption4, etCorrectAnswer;
    private FirebaseFirestore db;

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
    }

    private void setupButtons() {
        findViewById(R.id.btn_add_question).setOnClickListener(v -> addQuestionToDB());
        findViewById(R.id.btn_clear).setOnClickListener(v -> clearFields());
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
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
}