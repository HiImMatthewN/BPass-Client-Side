package com.spcba.bpass.ui.fragments.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.spcba.bpass.databinding.FragmentOnboardingParentBinding;
import com.spcba.bpass.helper.SharedPrefHelper;
import com.spcba.bpass.ui.activities.LoginActivity;
import com.spcba.bpass.ui.adapters.OnBoardingAdapter;

public class OnBoardingParentFragment extends Fragment {
    private FragmentOnboardingParentBinding binder;
    private ViewPager onBoardingVp;
    private TextView nextTv;
    private TextView backTv;

    private OnBoardingAdapter vpAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentOnboardingParentBinding.inflate(inflater,container,false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBoardingVp = binder.onBoardingVp;
        nextTv = binder.nextTv;
        backTv = binder.backTv;
        vpAdapter = new OnBoardingAdapter(getChildFragmentManager(),1);
        onBoardingVp.setAdapter(vpAdapter);

        backTv.setVisibility(View.GONE);
        nextTv.setOnClickListener(btn->{
            if (onBoardingVp.getCurrentItem() == vpAdapter.getCount()) return;
            onBoardingVp.setCurrentItem(onBoardingVp.getCurrentItem() +1,true);

        });
        backTv.setOnClickListener(btn->{
            if (onBoardingVp.getCurrentItem() == 0) return;
            onBoardingVp.setCurrentItem(onBoardingVp.getCurrentItem() -1,true);

        });

        onBoardingVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (vpAdapter.getCount()-1 == position){
                    nextTv.setText("Finish");
                    nextTv.setOnClickListener(btn->{
                        SharedPrefHelper.write(SharedPrefHelper.FIRST_TIME_OPEN,false);
                        requireActivity().finish();
                        goToLoginActivity();
                    });

                }
                else{
                    nextTv.setText("Next");
                    nextTv.setOnClickListener(btn->{
                        if (onBoardingVp.getCurrentItem() == vpAdapter.getCount()) return;
                        onBoardingVp.setCurrentItem(onBoardingVp.getCurrentItem() +1,true);

                    });
                }
                if (position ==0)
                    backTv.setVisibility(View.GONE);
                else
                    backTv.setVisibility(View.VISIBLE);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void goToLoginActivity() {
        Intent loginActivity = new Intent(requireContext(), LoginActivity.class);
        startActivity(loginActivity);
    }
}
