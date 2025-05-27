package com.i438.quizappproject.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.i438.quizappproject.model.QuizHistoryEntry;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


//local persistence
public class SharedPrefsManager {
    private static final String PREFS_NAME = "quiz_prefs";
    private static final String KEY_HISTORY = "quiz_history";

    private SharedPreferences prefs;
    private Gson gson = new Gson();
    private static final String KEY_QUIZ_COUNT = "quiz_count";
    private static final String KEY_BEST_SCORE = "best_score";

    public void saveQuizResult(int correct, int incorrect, int total) {
        // get existing history
        List<QuizHistoryEntry> history = getHistory();

        // add new entry
        history.add(new QuizHistoryEntry(correct, incorrect));

        // save updated history
        String json = gson.toJson(history);
        prefs.edit().putString(KEY_HISTORY, json).apply();

        // update quiz count and best score
        int quizCount = getQuizCount() + 1;
        prefs.edit().putInt(KEY_QUIZ_COUNT, quizCount).apply();
        float currentBest = prefs.getFloat(KEY_BEST_SCORE, 0);
        float percentage = (float) correct / total * 100;
        if (percentage > currentBest) {
            prefs.edit().putFloat(KEY_BEST_SCORE, percentage).apply();
        }
    }

    public int getQuizCount() {
        return prefs.getInt(KEY_QUIZ_COUNT, 0);
    }

    public float getBestScore() {
        return prefs.getFloat(KEY_BEST_SCORE, 0);
    }
    // clear cache
    public void clearCache() {
        prefs.edit().clear().apply();
    }

    public SharedPrefsManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    public List<QuizHistoryEntry> getHistory() {
        String json = prefs.getString(KEY_HISTORY, null);
        // define the expected type for Gson deserialization
        Type type = new TypeToken<ArrayList<QuizHistoryEntry>>(){}.getType();
        // convert JSON string into a List<QuizHistoryEntry>
        List<QuizHistoryEntry> history = gson.fromJson(json, type);
        return history != null ? history : new ArrayList<>();
    }

}