package com.spcba.bpass.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.spcba.bpass.data.datamodels.Trip;
import com.spcba.bpass.data.datautils.ScheduleData;

import java.util.Date;
import java.util.List;

public class TripRepository {
    private static TripRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MutableLiveData<List<Trip>> tripsLiveData = new MutableLiveData<>();

    private static final String TAG = "TripRepository";
    public static TripRepository getInstance(){
            if (instance == null)
                instance = new TripRepository();
            return instance;

    }

    public void addSchedule(){
        for (Trip trip: ScheduleData.getMarketMarketSched()){
            db.collection("Trips").add(trip).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d(TAG, "addSchedule: Trip Added ");
            });

        }
        for (Trip trip: ScheduleData.getCubaoSchedule()){
            db.collection("Trips").add(trip).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d(TAG, "addSchedule: Trip Added ");
            });

        }
        for (Trip trip: ScheduleData.getMegaMallSched()){
            db.collection("Trips").add(trip).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d(TAG, "addSchedule: Trip Added ");
            });

        }

    }
    public void fetchTripViaDate(Date startDate, Date endDate){


        db.collection("Trips")
                .orderBy("tripSchedule")
                .endAt(endDate)
                .addSnapshotListener((task,error) -> {
                    if (error != null) return;
                    List<Trip> trips = task.toObjects(Trip.class);
                    tripsLiveData.postValue(trips);
                    Log.d(TAG, "fetchTripViaDate: Size " + trips.size());

                });

    }

    public LiveData<List<Trip>> getTripsLiveData() {
        return tripsLiveData;
    }
}
