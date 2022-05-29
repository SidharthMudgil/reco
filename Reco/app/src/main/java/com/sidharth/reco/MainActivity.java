package com.sidharth.reco;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sidharth.reco.chat.ChatActivity;
import com.sidharth.reco.login.LoginActivity;
import com.sidharth.reco.onboarding.OnBoardingActivity;

public class MainActivity extends AppCompatActivity {

    public static final int STATE_ON_BOARDING = 0;
    public static final int STATE_LOGIN_SIGNUP = 1;
    public static final int STATE_CHAT_SCREEN = 2;

    public static final String STATE_KEY = "sidharth.reco.state";

    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        int state = sharedPreferences.getInt(STATE_KEY, STATE_ON_BOARDING);

        switch (state) {
            case STATE_ON_BOARDING:
                intent = new Intent(this, OnBoardingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case STATE_LOGIN_SIGNUP:
                intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case STATE_CHAT_SCREEN:
                intent = new Intent(this, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            default:
                finish();
        }
        startActivity(intent);
    }
}