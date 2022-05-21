package com.sidharth.reco.view.onboarding;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.sidharth.reco.R;
import com.sidharth.reco.controller.OnBoardingScreenAdapter;
import com.sidharth.reco.utils.DepthPageTransformer;
import com.sidharth.reco.utils.WobbleInterpolator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.Timer;
import java.util.TimerTask;

public class OnBoardingActivity extends AppCompatActivity {

    int currentPage = 0;
    Timer timer;
    Handler handler;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;

    TextView nextBtn;
    boolean firstTime = true;

    ViewPager2 viewPager;
    WormDotsIndicator dotsIndicator;
    OnBoardingScreenAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        nextBtn = findViewById(R.id.nextBtn);

        adapter = new OnBoardingScreenAdapter(OnBoardingActivity.this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(new DepthPageTransformer());

        dotsIndicator = findViewById(R.id.dotsIndicator);
        dotsIndicator.attachTo(viewPager);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wobble);
        WobbleInterpolator interpolator = new WobbleInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                if (position == 2) {
                    nextBtn.setVisibility(View.VISIBLE);
                    if (firstTime)
                    {
                        nextBtn.startAnimation(animation);
                    }
                    firstTime = false;
                }
                else {
                    nextBtn.setVisibility(View.GONE);
                }
            }
        });

        handler = new Handler();
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

        nextBtn.setOnClickListener(view -> {
//            SharedPreferences sharedPreferences =
//            Intent intent = new Intent(OnBoardingActivity.this, /*TODO make login signup activity*/);
//            startActivity(/*loginActivity*/);
        });
    }
}