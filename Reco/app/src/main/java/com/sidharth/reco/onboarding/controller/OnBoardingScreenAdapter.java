package com.sidharth.reco.onboarding.controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sidharth.reco.onboarding.view.Slide1Fragment;
import com.sidharth.reco.onboarding.view.Slide2Fragment;
import com.sidharth.reco.onboarding.view.Slide3Fragment;

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
        return new Slide3Fragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
