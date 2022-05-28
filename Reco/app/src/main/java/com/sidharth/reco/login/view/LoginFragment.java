package com.sidharth.reco.login.view;

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
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.sidharth.reco.R;
import com.sidharth.reco.chat.ChatActivity;
import com.sidharth.reco.login.LoginActivity;

import java.util.regex.Pattern;

public class LoginFragment extends Fragment {

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        MaterialButton signInBtn = view.findViewById(R.id.mb_signIn);
        signInBtn.setOnClickListener(view1 -> {
            Toast.makeText(getActivity(), "Login pressed", Toast.LENGTH_SHORT).show();

            EditText emailET = view.findViewById(R.id.emailET);
            EditText passwordET = view.findViewById(R.id.passwordET);

            String email = String.valueOf(emailET.getText());
            String password = String.valueOf(passwordET.getText());

            if (loginUser(email, password)) {
                Log.d("reco@recoLogin", "Successful");
                startChatActivity();
            } else {
                Log.d("reco@recoLogin", "Failed");
            }
        });

        MaterialTextView signUpTxt = view.findViewById(R.id.mtvBtn_signUp);
        signUpTxt.setOnClickListener(view12 -> {
            assert getActivity() != null;
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, new SignUpFragment())
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .addToBackStack(LoginFragment.class.getName())
                    .commit();
        });

        return view;
    }

    private boolean loginUser(String email, String password) {
        return true;
    }

    private void startChatActivity() {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}