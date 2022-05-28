package com.sidharth.reco.login.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.sidharth.reco.R;
import com.sidharth.reco.chat.ChatActivity;

import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {

    private ProgressDialog progressDialog;

    public SignUpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        progressDialog = new ProgressDialog(getActivity());

        MaterialButton signUpBtn = view.findViewById(R.id.mb_signUp);
        signUpBtn.setOnClickListener(view1 -> {
            Toast.makeText(getActivity(), "SignUp pressed", Toast.LENGTH_SHORT).show();

            EditText emailET = view.findViewById(R.id.emailET);
            EditText passwordET = view.findViewById(R.id.passwordET);
            EditText confirmPassET = view.findViewById(R.id.confirmPassET);

            boolean validEntry = false;
            String email = String.valueOf(emailET.getText());
            String password = String.valueOf(passwordET.getText());
            String confirmPass = String.valueOf(confirmPassET.getText());

            if (isValidEmail(email) && isPasswordValid(password) && arePasswordsSame(password, confirmPass)) {
                validEntry = true;
            } else {
                if (!isValidEmail(email)) {
                    emailET.setError("Invalid email");
                }
                if (!isPasswordValid(password)) {
                    String msg = "Password must contain at least 1 symbol, no white spaces and should be of minimum 4 characters";
                    passwordET.setError(msg);
                }
                if (!arePasswordsSame(password, confirmPass)) {
                    String msg = "Passwords does not match";
                    confirmPassET.setError(msg);
                }
            }

            if (validEntry) {
                if (signUpAndLoginUser(email, password)) {
                    Log.d("reco@recoSignUp", "Successful");
                    progressDialog.setMessage("Please wait while signing up");
                    progressDialog.setTitle("Signing Up");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    startChatActivity();
                } else {
                    Log.d("reco@recoSignUp", "Failed");
                }
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

    private boolean isPasswordValid(String password) {
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

    private boolean signUpAndLoginUser(String email, String password) {
        return true;
    }

    private void startChatActivity() {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}