package com.spcba.bpass.ui.fragments.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.spcba.bpass.R;
import com.spcba.bpass.databinding.FragmentSplashScreenBinding;
import com.spcba.bpass.helper.SharedPrefHelper;
import com.spcba.bpass.ui.activities.LobbyActivity;
import com.spcba.bpass.ui.activities.LoginActivity;

public class SplashScreenFragment extends Fragment {
    private FragmentSplashScreenBinding binder;
    private NavController navController;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentSplashScreenBinding.inflate(inflater, container, false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        new Handler().postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                if (SharedPrefHelper.read(SharedPrefHelper.FIRST_TIME_OPEN, true))
                    navController.navigate(R.id.action_splashScreenFragment_to_onBoardingParentFragment);
                else
                    goToLoginActivity();
            } else {

                requireActivity().finish();
                goToLobbyActivity();

            }


        }, 1500);
    }

    private void goToLoginActivity() {
        Intent loginActivity = new Intent(requireContext(), LoginActivity.class);
        startActivity(loginActivity);
    }

    private void goToLobbyActivity() {
        Intent lobbyActivity = new Intent(requireContext(), LobbyActivity.class);
        startActivity(lobbyActivity);
    }

}
