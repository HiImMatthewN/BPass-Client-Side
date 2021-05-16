package com.spcba.bpass.ui.fragments.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.spcba.bpass.R;
import com.spcba.bpass.databinding.FragmentOnboardingItemBinding;
import com.spcba.bpass.ui.activities.LoginActivity;

public class OnBoardingItemFragment extends Fragment {
    private FragmentOnboardingItemBinding binder;
    private ImageView onBoardingIv;
    private TextView onBoardingTitleTv;
    private TextView onBoardingMsgTv;
    private int currentPos;



    public OnBoardingItemFragment(int position) {
        this.currentPos = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentOnboardingItemBinding.inflate(inflater, container, false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBoardingIv = binder.onBoardingIV;
        onBoardingTitleTv = binder.onBoardingTitleTv;
        onBoardingMsgTv =  binder.onBoardingMessageTv;

        String[] title =  new String[]{"Buy Ticket","Top Up","Get Notified with GeoFence"};
        String[] msg = new String[]{"Buy Tickets before boarding the bus with ease","Top Up Easily with your choice of payment option"
                ,"Powered by GeoFence, get real time updates when you Entered a Drop off"};

        switch (currentPos){
            case 0:
                Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), R.drawable.onboarding_image_one)).into(onBoardingIv);
                onBoardingTitleTv.setText(title[0]);
                onBoardingMsgTv.setText(msg[0]);
                break;

            case 1:
                Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), R.drawable.onboarding_image_two)).into(onBoardingIv);
                onBoardingTitleTv.setText(title[1]);
                onBoardingMsgTv.setText(msg[1]);
                break;

            case 2:
                Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), R.drawable.onboarding_image_three)).into(onBoardingIv);
                onBoardingTitleTv.setText(title[2]);
                onBoardingMsgTv.setText(msg[2]);
                break;


        }
    }

}
