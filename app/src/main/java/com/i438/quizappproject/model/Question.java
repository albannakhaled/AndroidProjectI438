package com.i438.quizappproject.model;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class Question {
    private String id;
    private String questionText;
    private List<String> options;
    private String correctAnswer;

    public Question() {}  // Firestore requires a no-arg constructor

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
}