package com.spcba.bpass.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spcba.bpass.Event;
import com.spcba.bpass.data.datamodels.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private static UserRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<Event<Boolean>> mobileNumberLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> saveUserLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private static final String TAG = "FireStoreRepository";

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository();
        return instance;
    }

    private boolean isNumberFound = false;

    public void checkIfMobileNumberExists(String mobileNum) {
        Log.d(TAG, "checkIfMobileNumberExists: Checking number if exists");
        db.collection("Users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                    List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                    if (documentSnapshots.size() != 0) {
                        for (DocumentSnapshot snapshot : documentSnapshots) {
                            User user = snapshot.toObject(User.class);
                            if (user.getMobileNumber().equals(mobileNum)) {
                                Log.d(TAG, "checkIfMobileNumberExists: Number Exists");
                                isNumberFound = true;
                            }

                        }
                    }


                }
            }
            mobileNumberLiveData.postValue(new Event<>(isNumberFound));
            isNumberFound = false;

        });
    }
    public void updateNewBalance(double newBalance){
        Map<String,Object> data = new HashMap<>();
        data.put("balance", newBalance);
        db.collection("Users").document(FirebaseAuth.getInstance().getUid())
        .update(data).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                Log.d(TAG, "updateNewBalance: Updated Balance");
        });


    }
    public void saveUserToDb(User user) {
        db.collection("Users").document(FirebaseAuth.getInstance().getUid())
                .set(user).addOnCompleteListener(task -> {
            Log.d(TAG, "saveUserToDb: Saving User to Db: " + task.isSuccessful());
            saveUserLiveData.postValue(new Event<>(task.isSuccessful()));
        });
    }
    public void loadUserData(String uid){
        db.collection("Users").document(uid)
        .addSnapshotListener((value, error) -> {
            if (error != null){
                Log.d(TAG, "loadUserData: Error " + error.getMessage());
                return;
            }else
                if (value != null){
                    User user = value.toObject(User.class);
                    userLiveData.postValue(user);
                }
        });

    }
    public LiveData<Event<Boolean>> getCheckIfNumberAlreadyExists() {
        return mobileNumberLiveData;
    }

    public LiveData<Event<Boolean>> getSaveUserLiveData() {
        return saveUserLiveData;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
}
