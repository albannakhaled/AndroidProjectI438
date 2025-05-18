package com.i438.quizappproject.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.i438.quizappproject.R;
import com.i438.quizappproject.model.Question;
import com.i438.quizappproject.storage.SharedPrefsManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionFragment extends Fragment {
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    private List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;

    private TextView tvQuestionNumber, tvQuestionText;
    private RadioGroup rgOptions;
    private Button btnNext, btnPrevious;
    private Set<Integer> answeredQuestionIndices = new HashSet<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        initializeViews(view);
        showLoading(true);
        db = FirebaseFirestore.getInstance();
        loadQuestions();
        return view;
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        setQuestionViewsVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    private void setQuestionViewsVisibility(int visibility) {
        tvQuestionNumber.setVisibility(visibility);
        tvQuestionText.setVisibility(visibility);
        rgOptions.setVisibility(visibility);
        btnNext.setVisibility(visibility);
        btnPrevious.setVisibility(visibility);
    }

    private void initializeViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        tvQuestionNumber = view.findViewById(R.id.tv_question_number);
        tvQuestionText = view.findViewById(R.id.tv_question_text);
        rgOptions = view.findViewById(R.id.rg_options);
        btnNext = view.findViewById(R.id.btn_next);
        btnPrevious = view.findViewById(R.id.btn_previous);
        setQuestionViewsVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupButtonListeners();
    }

    private void loadQuestions() {
        db.collection("questions")
                .get()
                .addOnCompleteListener(task -> {
                    showLoading(false); // Hide progress bar when done

                    if (task.isSuccessful() && isAdded()) {
                        questions.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Question question = document.toObject(Question.class);
                            question.setId(document.getId());
                            questions.add(question);
                        }

                        if (questions.isEmpty()) {
                            showError("No questions found");
                        } else {
                            displayCurrentQuestion();
                            updateNavigationButtons();
                        }
                    } else if (isAdded()) {
                        showError("Failed to load questions: " +
                                (task.getException() != null ?
                                        task.getException().getMessage() : "Unknown error"));
                    }
                });
    }

    private void displayCurrentQuestion() {
        if (!isViewValid() || currentIndex < 0 || currentIndex >= questions.size()) {
            return;
        }

        Question currentQuestion = questions.get(currentIndex);
        tvQuestionNumber.setText(getString(R.string.question_counter,
                currentIndex + 1, questions.size()));
        tvQuestionText.setText(currentQuestion.getQuestionText());

        rgOptions.removeAllViews();
        List<String> options = currentQuestion.getOptions();
        for (int i = 0; i < options.size(); i++) {
            RadioButton radioButton = createRadioButton(options.get(i), i);
            rgOptions.addView(radioButton);
        }
    }

    private RadioButton createRadioButton(String text, int id) {
        RadioButton rb = new RadioButton(requireContext());
        rb.setText(text);
        rb.setId(id);
        rb.setPadding(16, 16, 16, 16);
        rb.setTextSize(16);
        return rb;
    }

    private void setupButtonListeners() {
        btnNext.setOnClickListener(v -> {
            if (isAnswerSelected()) {
                evaluateAnswer();
                moveToNextQuestionOrFinish();
            } else {
                showError("Please select an answer");
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                displayCurrentQuestion();
                updateNavigationButtons();
            }
        });
    }

    private boolean isAnswerSelected() {
        return rgOptions != null && rgOptions.getCheckedRadioButtonId() != -1;
    }

    private void evaluateAnswer() {
        int selectedId = rgOptions.getCheckedRadioButtonId();
        Question currentQuestion = questions.get(currentIndex);

        if (answeredQuestionIndices.contains(currentIndex)) {
            showError("You've already answered this question");
            return;
        }

        if (selectedId == Integer.parseInt(currentQuestion.getCorrectAnswer())) {
            score++;
            answeredQuestionIndices.add(currentIndex);
            showError("Correct answer!");
        } else {
            showError("Wrong answer!");
            answeredQuestionIndices.add(currentIndex); // Optional: count wrong answers too
        }
    }

    private void moveToNextQuestionOrFinish() {
        if (currentIndex < questions.size() - 1) {
            currentIndex++;
            displayCurrentQuestion();
            updateNavigationButtons();
        } else {
            showFinalResults();
        }
    }

    private void showFinalResults() {
        if (isAdded()) {
            int incorrect = questions.size() - score;
            SharedPrefsManager prefsManager = new SharedPrefsManager(requireContext());
            prefsManager.saveQuizResult(score, incorrect, questions.size());

            // Show result fragment
            ResultFragment resultFragment = ResultFragment.newInstance(score, questions.size());
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, resultFragment)
                    .commit();
        }
    }

    private void updateNavigationButtons() {
        btnPrevious.setEnabled(currentIndex > 0);
        btnNext.setText(currentIndex == questions.size() - 1 ?
                getString(R.string.finish) : getString(R.string.next));
    }

    private boolean isViewValid() {
        return isAdded() && getView() != null &&
                tvQuestionNumber != null &&
                tvQuestionText != null &&
                rgOptions != null;
    }

    private void showError(String message) {
        if (isAdded()) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            Log.e("QuestionFragment", message);
        }
    }

}