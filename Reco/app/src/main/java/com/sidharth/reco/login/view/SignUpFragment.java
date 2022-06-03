package com.sidharth.reco.login.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sidharth.reco.MainActivity;
import com.sidharth.reco.R;
import com.sidharth.reco.chat.ChatActivity;

import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    public SignUpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait while signing up");
        progressDialog.setTitle("Signing Up");
        progressDialog.setCanceledOnTouchOutside(false);

        MaterialButton signUpBtn = view.findViewById(R.id.mb_signUp);
        signUpBtn.setOnClickListener(view1 -> {
            EditText emailET = view.findViewById(R.id.emailET);
            EditText passwordET = view.findViewById(R.id.passwordET);
            EditText confirmPassET = view.findViewById(R.id.confirmPassET);

            boolean validEntry = false;
            String email = String.valueOf(emailET.getText());
            String password = String.valueOf(passwordET.getText());
            String confirmPass = String.valueOf(confirmPassET.getText());

            if (isValidEmail(email) && isValidPassword(password) && arePasswordsSame(password, confirmPass)) {
                validEntry = true;
            } else {
                if (!isValidEmail(email)) {
                    emailET.setError("Invalid email");
                }
                if (!isValidPassword(password)) {
                    String msg = "Password must contain at least 1 symbol, no white spaces and should be of minimum 4 characters";
                    passwordET.setError(msg);
                }
                if (!arePasswordsSame(password, confirmPass)) {
                    String msg = "Current field does not match the password";
                    confirmPassET.setError(msg);
                }
            }
            if (validEntry) {
                progressDialog.show();
                signUpAndLoginUser(email, password);
            }
        });

        return view;
    }

    private boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            return pattern.matcher(email).matches();
        }
    }

    private boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");

            return pattern.matcher(password).matches();
        }
    }

    private boolean arePasswordsSame(String password, String confirmPassword) {
        return password.matches(confirmPassword);
    }

    private void signUpAndLoginUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                Log.d("reco@recoSignUp", "Successful");
                startChatActivity();
            } else {
                progressDialog.dismiss();
                Log.d("reco@recoSignUp", "Failed");
                assert task.getException() != null;
                Log.d("reco@recoSignUp", task.getException().getMessage());
            }
        });
    }

    private void startChatActivity() {
        if (getActivity() != null) {
            Log.d(MainActivity.TAG, "starting ChatActivity");
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
            sharedPreferences.edit().putInt(MainActivity.STATE_KEY, MainActivity.STATE_CHAT_SCREEN).apply();
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}