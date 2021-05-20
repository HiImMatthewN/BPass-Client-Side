package com.spcba.bpass.ui.fragments.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private int REQ_IMAGE = 101;

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
        profilePicIv.setOnClickListener(iv->{
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i,REQ_IMAGE);
        });


        viewModel.getUser().observe(getViewLifecycleOwner(),user ->{
                nameEt.setText(user.getName());
                emailEt.setText(user.getEmail());
                mobileNumber.setText(user.getMobileNumber());
                if(user.getAge() !=0)
                ageEt.setText(String.valueOf(user.getAge()));
                else
                    ageEt.setText("");

            genderEt.setText(user.getGender());
                addressEt.setText(user.getAddress());
                secondaryMobileNumEt.setText(user.getSecondaryMobileNum());

            Glide.with(requireContext()).load(user.getProfilePicUrl()).into(profilePicIv);
        });



        profileViewModel.getUpdateProfileStatus().observe(getViewLifecycleOwner(),updateEvent->{
                    if (updateEvent.isHandled()) return;

                    if (updateEvent.getContentIfNotHandled()){
                        profileViewModel.loadUser();
                        Toast.makeText(requireContext(), "Update Success", Toast.LENGTH_SHORT).show();

                    }
                    else
                        Toast.makeText(requireContext(), "Update Failed", Toast.LENGTH_SHORT).show();


        });


        profileViewModel.getEnableEditLiveData().observe(getViewLifecycleOwner(),editEvent->{
                    if (editEvent.isHandled()) return;

                    if (editEvent.getContentIfNotHandled()){
                        enableField(true);

                        nameEt.requestFocus();
                        nameEt.setSelection(nameEt.getText().toString().length());
                        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(nameEt, InputMethodManager.SHOW_IMPLICIT);
                        editBtn.setText("Save");
                    }else{

                        String name = nameEt.getText().toString();
                        String age = ageEt.getText().toString();
                        String gender = genderEt.getText().toString();
                        String address = addressEt.getText().toString();
                        String email = emailEt.getText().toString();
                        String mobileNum = mobileNumber.getText().toString();
                        String secondaryMobileNum = secondaryMobileNumEt.getText().toString();

                        profileViewModel.updateProfile(name,Integer.parseInt(age),gender,address
                                ,email,mobileNum,secondaryMobileNum);

                        editBtn.setText("Edit");
                        enableField(false);

                    }

        });


    }
    private void enableField(boolean value){
        nameEt.setEnabled(value);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_IMAGE && data != null){
            Uri selectedImage = data.getData();
            profileViewModel.updateProfilePic(selectedImage);
            Glide.with(requireContext()).load(selectedImage).into(profilePicIv);


        }
    }
}
