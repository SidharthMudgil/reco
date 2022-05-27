package com.sidharth.reco.login.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.sidharth.reco.R;

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
        signInBtn.setOnClickListener(view1 -> Toast.makeText(getActivity(), "Login pressed", Toast.LENGTH_SHORT).show());

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


}