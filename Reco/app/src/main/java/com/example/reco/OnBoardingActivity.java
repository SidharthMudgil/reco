package com.example.reco;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.reco.controller.OnBoardingScreenAdapter;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.Timer;
import java.util.TimerTask;

public class OnBoardingActivity extends AppCompatActivity {

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        OnBoardingScreenAdapter adapter = new OnBoardingScreenAdapter(OnBoardingActivity.this);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        WormDotsIndicator dotsIndicator = findViewById(R.id.dotsIndicator);
        dotsIndicator.attachTo(viewPager);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wobble);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                TextView nextBtn = findViewById(R.id.nextBtn);
                if (position == 2) {
                    nextBtn.setVisibility(View.VISIBLE);
                    nextBtn.startAnimation(animation);
                }
                else {
                    nextBtn.setVisibility(View.GONE);
                }
            }
        });

        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == adapter.getItemCount()) {
                currentPage = 0;
                timer.cancel();
            } else {
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }
}