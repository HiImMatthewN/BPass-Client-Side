package com.spcba.bpass.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spcba.bpass.data.datautils.Event;
import com.spcba.bpass.data.datamodels.Ticket;

import java.util.List;

public class TicketRepository {
    private static TicketRepository instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<Ticket>> userTickets = new MutableLiveData<>();
    private MutableLiveData<Event<Ticket>> ticketAcceptedLiveData = new MutableLiveData<>();
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
    public void addTicketStatusChanged(){
        Log.d(TAG, "addTicketStatusChanged: Listening");
        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("tickets").addSnapshotListener((value, error) -> {
                    if (error != null || value == null){
                        Log.d(TAG, "onEvent: Error Attaching Ticket Status Listener");
                        return;
                    }
                    for (DocumentChange dc: value.getDocumentChanges()){
                        switch (dc.getType()) {
                            case MODIFIED:
                                Ticket ticketAccepted = dc.getDocument().toObject(Ticket.class);
                                Log.d(TAG, "addTicketStatusChanged: Ticket " + ticketAccepted.getId());
                                ticketAcceptedLiveData.postValue(new Event<>(ticketAccepted));
                                break;

                        }



                    }


                });



    }
    public LiveData<List<Ticket>> getTickets() {
        return userTickets;
    }

    public LiveData<Event<Ticket>> getTicketAccepted() {
        return ticketAcceptedLiveData;
    }
}
