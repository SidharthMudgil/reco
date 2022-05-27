package com.sidharth.reco.login.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.sidharth.reco.R;

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
        signUpBtn.setOnClickListener(view1 -> Toast.makeText(getActivity(), "SignUp pressed", Toast.LENGTH_SHORT).show());

        return view;
    }
}