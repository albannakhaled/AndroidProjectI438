package com.i438.quizappproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.i438.quizappproject.model.Question;
import com.i438.quizappproject.repository.QuizRepository;

import java.util.List;

public class QuizViewModel extends ViewModel {
    private final QuizRepository repository = new QuizRepository();

    private final MutableLiveData<List<Question>> _questions = new MutableLiveData<>();
    public LiveData<List<Question>> questions = _questions;

    private final MutableLiveData<Integer> _currentIndex = new MutableLiveData<>(0);
    public LiveData<Integer> currentIndex = _currentIndex;

    private final MutableLiveData<Integer> _score = new MutableLiveData<>(0);
    public LiveData<Integer> score = _score;

    public void loadQuestions() {
        repository.loadQuestions(new QuizRepository.QuizCallback() {
            @Override
            public void onSuccess(List<Question> qList) {
                _questions.setValue(qList);
            }

            @Override
            public void onFailure(String error) {
                _questions.setValue(null); // handle in view
            }
        });
    }

    public void nextQuestion() {
        Integer index = _currentIndex.getValue();
        if (index != null && questions.getValue() != null && index < questions.getValue().size() - 1) {
            _currentIndex.setValue(index + 1);
        }
    }

    public void previousQuestion() {
        Integer index = _currentIndex.getValue();
        if (index != null && index > 0) {
            _currentIndex.setValue(index - 1);
        }
    }

    public void checkAnswer(int selectedIndex) {
        List<Question> qList = questions.getValue();
        Integer index = _currentIndex.getValue();

        if (qList != null && index != null && index < qList.size()) {
            Question q = qList.get(index);
            if (selectedIndex == Integer.parseInt(q.getCorrectAnswer())) {
                _score.setValue(_score.getValue() + 1);
            }
        }
    }
}