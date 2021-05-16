package com.spcba.bpass.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.spcba.bpass.ui.fragments.fragment.OnBoardingItemFragment;

public class OnBoardingAdapter extends FragmentStatePagerAdapter {
    public OnBoardingAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new OnBoardingItemFragment(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
