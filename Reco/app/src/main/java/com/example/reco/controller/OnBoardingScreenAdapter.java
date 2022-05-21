package com.example.reco.controller;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.reco.view.Slide1Fragment;
import com.example.reco.view.Slide2Fragment;
import com.example.reco.view.Slide3Fragment;

public class OnBoardingScreenAdapter extends FragmentStateAdapter {
    public OnBoardingScreenAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new Slide1Fragment();
        }
        else if (position == 1) {
            return new Slide2Fragment();
        }
        else {
            return new Slide3Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
