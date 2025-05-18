package com.i438.quizappproject.ui;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.i438.quizappproject.databinding.ActivityHistoryBinding;

import com.i438.quizappproject.R;
import com.i438.quizappproject.storage.SharedPrefsManager;

public class HistoryActivity extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    private SharedPrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefsManager = new SharedPrefsManager(this);
        setupHistogram();
    }

    private void setupHistogram() {
        binding.histogram.setData(prefsManager.getHistory());
    }
}