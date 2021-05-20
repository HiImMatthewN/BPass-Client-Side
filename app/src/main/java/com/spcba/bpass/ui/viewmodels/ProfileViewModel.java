package com.spcba.bpass.ui.viewmodels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.spcba.bpass.data.datamodels.User;
import com.spcba.bpass.data.datautils.Event;
import com.spcba.bpass.repository.StorageRepository;
import com.spcba.bpass.repository.UserRepository;

public class ProfileViewModel extends ViewModel {

    private UserRepository userRepository = UserRepository.getInstance();
    private StorageRepository storageRepository = StorageRepository.getInstance();
    private boolean isEditEnabled = false;
    private MutableLiveData<Event<Boolean>> enableEditLiveData = new MutableLiveData<>();

    public void enableEdit() {
        isEditEnabled = !isEditEnabled;
        enableEditLiveData.setValue(new Event<>(isEditEnabled));

    }
    public void loadUser(){

        userRepository.loadUserData(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    public LiveData<Event<Boolean>> getEnableEditLiveData() {
        return enableEditLiveData;
    }

    public void updateProfile(String name, int age, String gender, String address, String emailAd
            , String mobileNum, String secondaryMobilNum) {
        User updatedUser = new User();
        updatedUser.setName(name);
        updatedUser.setAge(age);
        updatedUser.setGender(gender);
        updatedUser.setAddress(address);
        updatedUser.setEmail(emailAd);
        updatedUser.setMobileNumber(mobileNum);
        updatedUser.setSecondaryMobileNum(secondaryMobilNum);
        userRepository.updateProfile(updatedUser);

    }
    public void updateProfilePic(Uri uri){
        storageRepository.saveToStorage(uri);
        storageRepository.getDownloadUri().observeForever(downloadUriEvent->{
                if (downloadUriEvent.isHandled()) return;
                userRepository.updateProfilePic(String.valueOf(downloadUriEvent
                        .getContentIfNotHandled()));

        });

    }
    public LiveData<Event<Boolean>> getUpdateProfileStatus() {
        return userRepository.getUpdateUserStatus();

    }
}
