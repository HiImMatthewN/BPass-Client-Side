package com.spcba.bpass.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.spcba.bpass.data.datamodels.TopUp;
import com.spcba.bpass.data.datautils.Event;

import java.util.List;

public class TopUpRepository {
    private static TopUpRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private MutableLiveData<Event<Boolean>> addTopUpSuccessLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<TopUp>> topUpLiveData = new MutableLiveData<>();
    private MutableLiveData<List<TopUp>> topUpHistoryLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> receiptScannedLiveData = new MutableLiveData<>();
    private static final String TAG = "TopUpRepository";

    public static TopUpRepository getInstance() {
        if (instance == null)
            instance = new TopUpRepository();
        return instance;
    }

    public void addTopUpTransaction(TopUp topUp) {
        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("loadTransactions").add(topUp).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                topUpLiveData.postValue(new Event<>(topUp));
                Log.d(TAG, "addTopUpTransaction: Top Up Success");

            }
            else
                Log.d(TAG, "addTopUpTransaction: Top Up Failed");

            addTopUpSuccessLiveData.postValue(new Event<>(task.isSuccessful()));

        });
    }

    public void requestTopUpHistory() {
        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("loadTransactions").orderBy("dateCreated", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e(TAG, "requestTopUpHistory: Error: " + error.getMessage(), error);
                return;
            }
            List<TopUp> topUpList = value.toObjects(TopUp.class);
            topUpHistoryLiveData.postValue(topUpList);
        });

    }
    public void addReceiptStatusChanged(){
        Log.d(TAG, "addReceiptStatusChanged: Listening");
        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("loadTransactions").addSnapshotListener((value, error) -> {
            if (error != null || value == null){
                Log.d(TAG, "onEvent: Error Attaching Receipt Status Listener");
                return;
            }
            for (DocumentChange dc: value.getDocumentChanges()){
                switch (dc.getType()) {
                    case MODIFIED:
                        TopUp receiptAccepted = dc.getDocument().toObject(TopUp.class);
                        Log.d(TAG, "addReceiptStatusChanged: Receipt " + receiptAccepted.getRefNumber());
                        receiptScannedLiveData.postValue(new Event<>(receiptAccepted.isPaid()));
                        break;

                }
            }


        });
    }
    public LiveData<List<TopUp>> getTopUpHistory() {
        return topUpHistoryLiveData;
    }

    public LiveData<Event<Boolean>> getAddTopUpSuccessLiveData() {
        return addTopUpSuccessLiveData;
    }

    public LiveData<Event<TopUp>> getTopUpLiveData() {
        return topUpLiveData;
    }

    public LiveData<Event<Boolean>> getReceiptScannedLiveData() {
        return receiptScannedLiveData;
    }
}
