package com.sidharth.reco;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sidharth.reco.view.login.LoginActivity;
import com.sidharth.reco.view.onboarding.OnBoardingActivity;

public class MainActivity extends AppCompatActivity {

    private static final int STATE_ON_BOARDING = 0;
    private static final int STATE_LOGIN_SIGNUP = 1;
    private static final int STATE_SPLASH_APP = 2;

    public static final String STATE_KEY = "sidharth.reco.state";

    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        int state = sharedPreferences.getInt(STATE_KEY, 0);

        switch (state) {
            case STATE_ON_BOARDING:
                intent = new Intent(this, OnBoardingActivity.class);
                startActivity(intent);
                break;
            case STATE_LOGIN_SIGNUP:
                intent = new Intent(this, LoginActivity.class);
                break;
            case STATE_SPLASH_APP:
//                intent = new Intent(this, .class);
                break;
            default:
                finish();
        }
        startActivity(intent);
    }
}