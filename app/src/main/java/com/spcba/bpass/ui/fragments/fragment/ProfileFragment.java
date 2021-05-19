package com.spcba.bpass.ui.fragments.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.spcba.bpass.databinding.FragmentProfileBinding;
import com.spcba.bpass.ui.viewmodels.LobbyActivityViewModel;
import com.spcba.bpass.ui.viewmodels.ProfileViewModel;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binder;
    private EditText nameEt;
    private EditText ageEt;
    private EditText genderEt;
    private EditText emailEt;
    private EditText addressEt;
    private EditText mobileNumber;
    private EditText secondaryMobileNumEt;
    private CircularImageView profilePicIv;

    private LobbyActivityViewModel viewModel;
    private ProfileViewModel profileViewModel;
    private Button editBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentProfileBinding.inflate(inflater,container,false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LobbyActivityViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        nameEt = binder.nameEt;
        ageEt =binder.ageEt;
        genderEt = binder.genderEt;
        emailEt = binder.emailEt;
        addressEt = binder.addressEt;
        mobileNumber = binder.mobileNumberEt;
        secondaryMobileNumEt = binder.secondaryMobileNumEt;
        profilePicIv = binder.profilePicIv;
        editBtn = binder.editProfileBtn;

        enableField(false);


        editBtn.setOnClickListener(btn->{
            profileViewModel.enableEdit();
        });
        viewModel.getUser().observe(getViewLifecycleOwner(),user ->{
                nameEt.setText(user.getName());
                emailEt.setText(user.getEmail());
                mobileNumber.setText(user.getMobileNumber());

            Glide.with(requireContext()).load(user.getProfilePicUrl()).into(profilePicIv);
        });


        //Todo:Let user change profile pic



        profileViewModel.getEnableEditLiveData().observe(getViewLifecycleOwner(),editEvent->{
                    if (editEvent.isHandled()) return;

                    if (editEvent.getContentIfNotHandled()){
                        nameEt.requestFocus();
                        nameEt.setSelection(nameEt.getText().toString().length());
                        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(nameEt, InputMethodManager.SHOW_IMPLICIT);
                        editBtn.setText("Save Edit");
                        enableField(true);
                    }else{
                        //Todo: Get Values and send to Profile view model


                        enableField(false);

                    }

        });


    }
    private void enableField(boolean value){
        nameEt.setCursorVisible(value);
        nameEt.setFocusableInTouchMode(value);

        ageEt.setCursorVisible(value);
        ageEt.setFocusableInTouchMode(value);

        genderEt.setCursorVisible(value);
        genderEt.setFocusableInTouchMode(value);

        emailEt.setCursorVisible(value);
        emailEt.setFocusableInTouchMode(value);

        addressEt.setCursorVisible(value);
        addressEt.setFocusableInTouchMode(value);

        mobileNumber.setCursorVisible(value);
        mobileNumber.setFocusableInTouchMode(value);

        secondaryMobileNumEt.setCursorVisible(value);
        secondaryMobileNumEt.setFocusableInTouchMode(value);



    }
}
