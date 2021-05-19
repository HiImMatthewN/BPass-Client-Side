package com.spcba.bpass.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spcba.bpass.data.datautils.Event;

public class ProfileViewModel extends ViewModel {
    //Todo: Send Update User Profile to firestore
    // Delete and Save Update Profile pic to firebase storage
    private boolean isEditEnabled = false;
    private MutableLiveData<Event<Boolean>> enableEditLiveData = new MutableLiveData<>();
    public void enableEdit(){
        isEditEnabled = !isEditEnabled;
        enableEditLiveData.setValue(new Event<>(isEditEnabled));

    }


    public LiveData<Event<Boolean>> getEnableEditLiveData() {
        return enableEditLiveData;
    }

}
