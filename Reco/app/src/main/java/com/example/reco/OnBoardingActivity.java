package com.example.reco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.reco.controller.OnBoardingScreenAdapter;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class OnBoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        OnBoardingScreenAdapter adapter = new OnBoardingScreenAdapter(OnBoardingActivity.this);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        WormDotsIndicator dotsIndicator = findViewById(R.id.dotsIndicator);
        dotsIndicator.attachTo(viewPager);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                TextView nextBtn = findViewById(R.id.nextBtn);
                if (position == 2)
                    nextBtn.setVisibility(View.VISIBLE);
                else
                    nextBtn.setVisibility(View.GONE);
            }
        });
    }
}