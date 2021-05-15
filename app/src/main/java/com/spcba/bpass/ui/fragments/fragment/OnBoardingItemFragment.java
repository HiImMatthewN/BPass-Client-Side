package com.spcba.bpass.ui.fragments.fragment;

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

public class OnBoardingItemFragment extends Fragment {
    private FragmentOnboardingItemBinding binder;
    private ImageView onBoardingIv;
    private TextView onBoardingTitleTv;
    private TextView onBoardingMsgTv;
    private int currentPos;

    //Todo: OnBoarding Title and Message


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



        switch (currentPos){
            case 0:
                Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), R.drawable.onboarding_image_one)).into(onBoardingIv);
                onBoardingTitleTv.setText("Title " + currentPos + 1);
                onBoardingMsgTv.setText("Message " + currentPos + 1);
                break;

            case 1:
                Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), R.drawable.onboarding_image_two)).into(onBoardingIv);
                onBoardingTitleTv.setText("Title " + currentPos + 1);
                onBoardingMsgTv.setText("Message " + currentPos + 1);
                break;

            case 2:
                Glide.with(requireContext()).load(ContextCompat.getDrawable(requireContext(), R.drawable.onboarding_image_three)).into(onBoardingIv);
                onBoardingTitleTv.setText("Title " + currentPos + 1);
                onBoardingMsgTv.setText("Message " + currentPos + 1);
                break;





        }


    }
}
