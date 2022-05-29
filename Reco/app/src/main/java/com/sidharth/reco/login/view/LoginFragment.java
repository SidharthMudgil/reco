package com.sidharth.reco.login.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.sidharth.reco.MainActivity;
import com.sidharth.reco.R;
import com.sidharth.reco.chat.ChatActivity;

public class LoginFragment extends Fragment {

    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait while signing in");
        progressDialog.setTitle("Signing In");
        progressDialog.setCanceledOnTouchOutside(false);

        MaterialButton signInBtn = view.findViewById(R.id.mb_signIn);
        signInBtn.setOnClickListener(view1 -> {
            EditText emailET = view.findViewById(R.id.emailET);
            EditText passwordET = view.findViewById(R.id.passwordET);

            String email = String.valueOf(emailET.getText());
            String password = String.valueOf(passwordET.getText());

            progressDialog.show();
            loginUser(email, password);
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

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("reco@recoSignIn", "Successful");
                progressDialog.dismiss();
                startChatActivity();
            } else {
                progressDialog.dismiss();
                Log.d("reco@recoSignIn", "Failed");
                assert task.getException() != null;
                Log.d("reco@recoSignIn", task.getException().getMessage());
                Toast.makeText(getActivity(), "Signing failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startChatActivity() {
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
            sharedPreferences.edit().putInt(MainActivity.STATE_KEY, MainActivity.STATE_CHAT_SCREEN).apply();
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}