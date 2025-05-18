package com.i438.quizappproject.repository;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.i438.quizappproject.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuizRepository {
    private final FirebaseFirestore db;

    public QuizRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public interface QuizCallback {
        void onSuccess(List<Question> questions);
        void onFailure(String error);
    }

    public void loadQuestions(@NonNull QuizCallback callback) {
        db.collection("questions").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Question> questions = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    questions.add(doc.toObject(Question.class));
                }
                callback.onSuccess(questions);
            } else {
                callback.onFailure("Failed to load questions");
            }
        });
    }
}