package com.spcba.bpass.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spcba.bpass.data.datamodels.Ticket;

import java.util.List;

public class TicketRepository {
    private static TicketRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<Ticket>> userTickets = new MutableLiveData<>();
    private static final String TAG = "TicketRepository";

    public static TicketRepository getInstance() {
        if (instance == null)
            instance = new TicketRepository();
        return instance;
    }

    public void addTicket(Ticket ticket) {
        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("tickets").add(ticket)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        Log.d(TAG, "addTicket: Success");
                    else
                        Log.d(TAG, "addTicket: Failed");
                });
    }

    public void getUserTickets() {
        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("tickets").addSnapshotListener((value, error) -> {
            if (error != null) return;

            List<Ticket> tickets = value.toObjects(Ticket.class);
            userTickets.postValue(tickets);
        });
    }

    public LiveData<List<Ticket>> getTickets() {
        return userTickets;
    }

}
