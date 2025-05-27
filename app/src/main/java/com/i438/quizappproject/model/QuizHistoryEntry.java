package com.i438.quizappproject.model;

public class QuizHistoryEntry {
    private int correct;
    private int incorrect;
    private long timestamp;

    public QuizHistoryEntry(int correct, int incorrect) {
        this.correct = correct;
        this.incorrect = incorrect;
        this.timestamp = System.currentTimeMillis();
    }
    public float getCorrectPercent() {
        return (correct * 100f) / (correct + incorrect);
    }
    public float getIncorrectPercent() {
        return (incorrect * 100f) / (correct + incorrect);
    }
    public int getCorrect() { return correct; }
    public int getIncorrect() { return incorrect; }
    public long getTimestamp() { return timestamp; }
}