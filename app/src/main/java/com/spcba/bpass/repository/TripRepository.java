package com.spcba.bpass.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spcba.bpass.data.datamodels.Destination;
import com.spcba.bpass.data.datamodels.Trip;
import com.spcba.bpass.data.datautils.ScheduleData;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public  String generatePlatNumber(){

        int letter1 = 65 + (int)(Math.random() * (90 - 65));
        int letter2 = 65 + (int)(Math.random() * (90 - 65));
        int letter3 = 65 + (int)(Math.random() * (90 - 65));


        int number1 = (int)(Math.random() * 10);
        int number2 = (int)(Math.random() * 10);
        int number3 = (int)(Math.random() * 10);


        return "" + (char)(letter1) + ((char)(letter2)) +
                ((char)(letter3)) + "-" + number1 + number2 + number3;

    }
    public void addSchedule(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,25);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);


        for (Destination destination : ScheduleData.getMarketMarketSched()){
            Trip trip = new Trip(80,calendar.getTime(),destination,generatePlatNumber());

            db.collection("Trips").add(trip).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d(TAG, "addSchedule: Trip Added ");
            });

        }
        for (Destination destination : ScheduleData.getCubaoSchedule()){
            Trip trip = new Trip(80,calendar.getTime(),destination,generatePlatNumber());

            db.collection("Trips").add(trip).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d(TAG, "addSchedule: Trip Added ");
            });

        }
        for (Destination destination : ScheduleData.getMegaMallSched()){
            Trip trip = new Trip(80,calendar.getTime(),destination,generatePlatNumber());
            db.collection("Trips").add(trip).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d(TAG, "addSchedule: Trip Added ");
            });

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
                        int newAmount = trip.getSlotAvailable() - amount;
                        updateBusSlot(docId,newAmount);
                    }else
                        Log.d(TAG, "deductSeat: Failed " + task.getException().getMessage());


        });


    }
    private void updateBusSlot(String docId,int newValue){
        Map<String,Object> map = new HashMap<>();
        map.put("slotAvailable",newValue);
        db.collection("Trips").document(docId)
                .update(map).addOnCompleteListener(task -> {
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
