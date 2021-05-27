package com.spcba.bpass.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spcba.bpass.data.datamodels.Destination;
import com.spcba.bpass.data.datamodels.Trip;
import com.spcba.bpass.data.datautils.ScheduleData;
import com.spcba.bpass.data.datautils.StringUtils;

import java.util.Calendar;
import java.util.Collections;
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,31);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        Calendar departureTime = Calendar.getInstance();
        departureTime.setTime(calendar.getTime());
        departureTime.set(Calendar.HOUR_OF_DAY,7);
        departureTime.set(Calendar.MINUTE,0);

        Calendar arriveTime = Calendar.getInstance();
        arriveTime.setTime(calendar.getTime());
        arriveTime.set(Calendar.HOUR_OF_DAY,7);
        arriveTime.set(Calendar.MINUTE,30);


        Calendar departureTime1 = Calendar.getInstance();
        departureTime1.setTime(calendar.getTime());
        departureTime1.set(Calendar.HOUR_OF_DAY,7);
        departureTime1.set(Calendar.MINUTE,15);

        Calendar arriveTime1 = Calendar.getInstance();
        arriveTime1.setTime(calendar.getTime());
        arriveTime1.set(Calendar.HOUR_OF_DAY,8);
        arriveTime1.set(Calendar.MINUTE,0);

        Calendar departureTime2 = Calendar.getInstance();
        departureTime2.setTime(calendar.getTime());
        departureTime2.set(Calendar.HOUR_OF_DAY,7);
        departureTime2.set(Calendar.MINUTE,0);

        Calendar arriveTime2 = Calendar.getInstance();
        arriveTime2.setTime(calendar.getTime());
        arriveTime2.set(Calendar.HOUR_OF_DAY,8);
        arriveTime2.set(Calendar.MINUTE,0);


        for (Destination destination : ScheduleData.getMarketMarketSched()){
            destination.setExpectLeaveTime(departureTime.getTime());
            destination.setExpectArriveTime(arriveTime.getTime());
            Trip trip = new Trip(0,calendar.getTime(),destination, StringUtils.generatePlatNumber());

            db.collection("Trips").add(trip).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d(TAG, "addSchedule: Trip Added ");
            });
            departureTime.set(Calendar.HOUR_OF_DAY,departureTime.get(Calendar.HOUR_OF_DAY) +1);
            arriveTime.set(Calendar.HOUR_OF_DAY,arriveTime.get(Calendar.HOUR_OF_DAY) +1);
        }
        for (Destination destination : ScheduleData.getCubaoSchedule()){
            destination.setExpectLeaveTime(departureTime1.getTime());
            destination.setExpectArriveTime(arriveTime1.getTime());

            Trip trip = new Trip(0,calendar.getTime(),destination,StringUtils.generatePlatNumber());

            db.collection("Trips").add(trip).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d(TAG, "addSchedule: Trip Added ");
            });
            departureTime1.set(Calendar.HOUR_OF_DAY,departureTime1.get(Calendar.HOUR_OF_DAY) +1);
            arriveTime1.set(Calendar.HOUR_OF_DAY,arriveTime1.get(Calendar.HOUR_OF_DAY) +1);
        }
        for (Destination destination : ScheduleData.getMegaMallSched()){
            destination.setExpectLeaveTime(departureTime2.getTime());
            destination.setExpectArriveTime(arriveTime2.getTime());


            Trip trip = new Trip(0,calendar.getTime(),destination,StringUtils.generatePlatNumber());
            db.collection("Trips").add(trip).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d(TAG, "addSchedule: Trip Added ");
            });
            departureTime1.set(Calendar.HOUR_OF_DAY,departureTime2.get(Calendar.HOUR_OF_DAY) +1);
            arriveTime1.set(Calendar.HOUR_OF_DAY,arriveTime2.get(Calendar.HOUR_OF_DAY) +1);

        }

    }
    public void fetchTripViaDate(Date startDate, Date endDate){


        db.collection("Trips")
                .orderBy("schedule")
                .startAt(startDate)
                .endAt(endDate)
                .addSnapshotListener((task,error) -> {
                    if (error != null || task == null) return;
                    List<Trip> destinations = task.toObjects(Trip.class);
                    Collections.sort(destinations, (o1, o2) ->
                            o1.getDestination().getExpectLeaveTime()
                                    .compareTo(o2.getDestination().getExpectLeaveTime()));

                    tripsLiveData.postValue(destinations);
                    Log.d(TAG, "fetchTripViaDate: Size " + destinations.size());

                });

    }
    public void deductSeat(String busNumber,int amount){
        db.collection("Trips")
                .whereEqualTo("busNumber",busNumber)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        List<DocumentSnapshot> snapshots = task.getResult().getDocuments();
                        Trip trip = snapshots.get(0).toObject(Trip.class);
                        if (trip == null) return;

                        String docId = snapshots.get(0).getId();
                        updateBusSlot(docId,amount);
                    }else
                        Log.d(TAG, "deductSeat: Failed " + task.getException().getMessage());


        });


    }
    private void updateBusSlot(String docId,int newValue){
        db.collection("Trips").document(docId)
                .update("slotAvailable", FieldValue.increment(newValue)).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        Log.d(TAG, "updateBusSlot: Success");
                        else
                        Log.d(TAG, "updateBusSlot: Failed " + task.getException().getMessage());


        });


    }
    public LiveData<List<Trip>> getTripsLiveData() {
        return tripsLiveData;
    }
}
