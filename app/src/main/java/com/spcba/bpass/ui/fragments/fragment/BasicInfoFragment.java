package com.spcba.bpass.ui.fragments.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.spcba.bpass.R;
import com.spcba.bpass.ui.custom.UneditableTextWatcher;
import com.spcba.bpass.databinding.FragmentBasicInfoBinding;
import com.spcba.bpass.ui.viewmodels.LoginActivityViewModel;

public class BasicInfoFragment extends Fragment {
    private FragmentBasicInfoBinding binder;
    private NavController navController;
    private EditText nameEt;
    private EditText mobileNumEt;
    private EditText emailEt;
    private ImageView circularImageView;
    private Button nextBtn;
    private ProgressBar progressBar;
    private LoginActivityViewModel viewModel;
    private static final String TAG = "BasicInfoFragment";
    private int REQ_IMAGE = 101;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentBasicInfoBinding.inflate(inflater, container, false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LoginActivityViewModel.class);
        navController = Navigation.findNavController(view);
        nameEt = binder.enterNameEt;
        mobileNumEt = binder.enterMobileNumEt;
        emailEt = binder.enterEmailEt;
        nextBtn = binder.nextBtn;
        progressBar = binder.progressBar;
        circularImageView = binder.circularImageView;
        mobileNumEt.addTextChangedListener(new UneditableTextWatcher(mobileNumEt, "+639"));

        nextBtn.setOnClickListener(btn -> {
            String number = mobileNumEt.getText().toString();
            String name = nameEt.getText().toString();
            String email = emailEt.getText().toString();
            viewModel.checkIfBasicInfoValid(name, number, email);
            isVerifying(true);
        });

        circularImageView.setOnClickListener(iv->{
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i,REQ_IMAGE);
        });


        viewModel.getIfNumberAlreadyExists().observe(getViewLifecycleOwner(), numberAlreadyExistsEvent -> {
            if (numberAlreadyExistsEvent .isHandled()) return;
            if (!numberAlreadyExistsEvent.getContentIfNotHandled()){
                String number = mobileNumEt.getText().toString();
                String name = nameEt.getText().toString();
                String email = emailEt.getText().toString();
                viewModel.verifyPhoneNumber(number,requireActivity());
                viewModel.createUser(name,number,email);
                navController.navigate(R.id.action_basicInfoFragment_to_enterCodeFragment);
            }

            else
                mobileNumEt.setError("Number Already Taken");
            isVerifying(false);


        });

        viewModel.getInvalidEmail().observe(getViewLifecycleOwner(), invalidEmailEvent -> {
            if (invalidEmailEvent.isHandled()) return;
            if (!invalidEmailEvent.getContentIfNotHandled()) {
                emailEt.setError("Invalid Email Format");
                isVerifying(false);
            }

        });
        viewModel.getInvalidMobileNumber().observe(getViewLifecycleOwner(), invalidMobileNumberEvent -> {
            if (invalidMobileNumberEvent.isHandled()) return;
            if (!invalidMobileNumberEvent.getContentIfNotHandled()) {
                mobileNumEt.setError("Invalid Mobile Number");
                isVerifying(false);
            }
        });
        viewModel.getInvalidName().observe(getViewLifecycleOwner(), invalidNameEvent -> {
            if (invalidNameEvent.isHandled()) return;
            if (!invalidNameEvent.getContentIfNotHandled()) {
                nameEt.setError("Invalid Name");
                isVerifying(false);
            }
        });
    }


    private void isVerifying(boolean value) {
        if (value) {
            progressBar.setVisibility(View.VISIBLE);
            nextBtn.setText("");
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            nextBtn.setText("Next");
        }
        nextBtn.setEnabled(!value);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_IMAGE && data != null){
            Uri selectedImage = data.getData();
            viewModel.setProfilePicUri(selectedImage);
            Glide.with(requireContext()).load(selectedImage).into(circularImageView);


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
