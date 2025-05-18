package com.i438.quizappproject.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.i438.quizappproject.R;

public class ResultFragment extends Fragment {
    private static final String ARG_SCORE = "score";
    private static final String ARG_TOTAL = "total";

    private TextView tvCorrectCount, tvPercentage;
    private Button btnRetry, btnHome;

    public static ResultFragment newInstance(int score, int total) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SCORE, score);
        args.putInt(ARG_TOTAL, total);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupData();
        setupButtonListeners();
    }

    private void initializeViews(View view) {
        tvCorrectCount = view.findViewById(R.id.text_correct_count);
        tvPercentage = view.findViewById(R.id.text_percentage);
        btnRetry = view.findViewById(R.id.btn_retry);
        btnHome = view.findViewById(R.id.btn_home);
    }

    private void setupData() {
        if (getArguments() != null) {
            int score = getArguments().getInt(ARG_SCORE);
            int total = getArguments().getInt(ARG_TOTAL);

            tvCorrectCount.setText(getString(R.string.correct_count_format, score, total));

            double percentage = (double) score / total * 100;
            tvPercentage.setText(getString(R.string.percentage_format, percentage));
        }
    }

    private void setupButtonListeners() {
        btnRetry.setOnClickListener(v -> restartQuiz());
        btnHome.setOnClickListener(v -> requireActivity().finish());
    }

    private void restartQuiz() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new QuestionFragment())
                .commit();
    }
}