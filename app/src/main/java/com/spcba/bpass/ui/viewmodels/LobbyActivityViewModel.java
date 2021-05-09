package com.spcba.bpass.ui.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.spcba.bpass.Event;
import com.spcba.bpass.data.datamodels.Destination;
import com.spcba.bpass.data.datamodels.Ticket;
import com.spcba.bpass.data.datamodels.User;
import com.spcba.bpass.repository.TicketRepository;
import com.spcba.bpass.repository.UserRepository;

import java.util.List;

public class LobbyActivityViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private TicketRepository ticketRepository = TicketRepository.getInstance();
    private MutableLiveData<Event<Destination>> selectedDestinationLiveData = new MutableLiveData<>();

    private static final String TAG = "LobbyActivityViewModel";


    public LobbyActivityViewModel() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRepository.loadUserData(uid);
        ticketRepository.getUserTickets();
    }

    public void setSelectedDestination(Destination destination){
        Log.d(TAG, "setSelectedDestination: Selected");
        selectedDestinationLiveData.postValue(new Event<>(destination));
    }


    public LiveData<Event<Destination>> getSelectedDestination() {
        return selectedDestinationLiveData;
    }


    public LiveData<User> getUser(){
        return userRepository.getUserLiveData();
    }
    public LiveData<List<Ticket>> getTickets(){
        return ticketRepository.getTickets();


    }

}
