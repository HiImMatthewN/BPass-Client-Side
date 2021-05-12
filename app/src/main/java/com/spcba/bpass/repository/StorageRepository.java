package com.spcba.bpass.repository;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spcba.bpass.data.datautils.Event;

import java.util.UUID;

public class StorageRepository {
    private static StorageRepository instance;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference profilePicsRef = storage.getReference("Profile Pics/");
    private StorageReference userProfilePicRef;
    private MutableLiveData<Event<Uri>> downloadUri = new MutableLiveData<>();

    public static StorageRepository getInstance(){
        if (instance == null)
            instance = new StorageRepository();
        return instance;
    }

    public void saveToStorage(Uri uri){
        userProfilePicRef = profilePicsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        StorageReference fileRef = userProfilePicRef.child(UUID.randomUUID().toString());
        UploadTask uploadTask =fileRef.putFile(uri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(downloadedUri ->{
               downloadUri.postValue(new Event<>(downloadedUri));
            });
        });


    }

    public LiveData<Event<Uri>> getDownloadUri() {
        return downloadUri;
    }
}
