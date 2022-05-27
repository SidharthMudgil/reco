package com.sidharth.reco.login.view;

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

import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {
    public SignUpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        MaterialButton signUpBtn = view.findViewById(R.id.mb_signUp);
        signUpBtn.setOnClickListener(view1 -> {
            Toast.makeText(getActivity(), "SignUp pressed", Toast.LENGTH_SHORT).show();

            EditText emailET = view.findViewById(R.id.emailET);
            EditText passwordET = view.findViewById(R.id.passwordET);

            boolean validEntry = false;
            String email = String.valueOf(emailET.getText());
            String password = String.valueOf(passwordET.getText());

            if (isValidEmail(email) && isPasswordValid(password)) {
                validEntry = true;
            } else {
                if (!isValidEmail(email)) {
                    emailET.setError("Invalid email");
                }
                if (!isPasswordValid(password)) {
                    String msg = "Password must contain at least 1 symbol, no white spaces and should be of minimum 4 characters";
                    passwordET.setError(msg);
                }
            }

            if (validEntry) {
                if (signUpAndLoginUser(email, password)) {
                    Log.d("reco@recoSignUp", "Successful");
                } else {
                    Log.d("reco@recoSignUp", "Failed");
                }
            }
        });

        return view;
    }

    private boolean signUpAndLoginUser(String email, String password) {
        return true;
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
}