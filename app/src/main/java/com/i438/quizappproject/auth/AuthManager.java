package com.i438.quizappproject.auth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthManager {
//    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
//
//    public interface AuthCallback {
//        void onSuccess(FirebaseUser user);
//
//        void onFailure(Exception e);
//    }
//
//    public void signIn(String username, String password, AuthCallback callback) {
//        String email = username + "@i438.com";
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        callback.onSuccess(mAuth.getCurrentUser());
//                    } else {
//                        callback.onFailure(task.getException());
//                    }
//                });
//    }
//
//    public void signUp(String username, String password, AuthCallback callback) {
//        String email = username + "@i438.com";
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        callback.onSuccess(mAuth.getCurrentUser());
//                    } else {
//                        callback.onFailure(task.getException());
//                    }
//                });
//    }
//
//    public void signOut() {
//        mAuth.signOut();
//    }
//
//    public FirebaseUser getCurrentUser() {
//        return mAuth.getCurrentUser();
//    }
}