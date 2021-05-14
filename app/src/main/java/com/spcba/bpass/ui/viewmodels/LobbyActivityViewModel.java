package com.spcba.bpass.ui.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.spcba.bpass.data.datautils.Event;
import com.spcba.bpass.data.datamodels.Destination;
import com.spcba.bpass.data.datamodels.Ticket;
import com.spcba.bpass.data.datamodels.User;
import com.spcba.bpass.helper.LocationHelper;
import com.spcba.bpass.repository.TicketRepository;
import com.spcba.bpass.repository.TopUpRepository;
import com.spcba.bpass.repository.UserRepository;

import java.util.List;

public class LobbyActivityViewModel extends AndroidViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private TicketRepository ticketRepository = TicketRepository.getInstance();
    private TopUpRepository topUpRepository = TopUpRepository.getInstance();
    private LocationHelper locationHelper = LocationHelper.getInstance();

    private MutableLiveData<Event<Destination>> selectedDestinationLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Ticket>> selectedTicketLiveData = new MutableLiveData<>();

    private static final String TAG = "LobbyActivityViewModel";


    public LobbyActivityViewModel(Application application) {
        super(application);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRepository.loadUserData(uid);
        ticketRepository.getUserTickets();
        ticketRepository.addTicketStatusChanged();
        topUpRepository.addReceiptStatusChanged();


    }


    public void setSelectedDestination(Destination destination){
        Log.d(TAG, "setSelectedDestination: Selected");
        selectedDestinationLiveData.postValue(new Event<>(destination));
    }

    public void setSelectedTicket(Ticket ticket){
        selectedTicketLiveData.postValue(new Event<>(ticket));
    }
    public LiveData<Event<Destination>> getSelectedDestination() {
        return selectedDestinationLiveData;
    }

    public LiveData<Event<Ticket>> getSelectedTicketLiveData() {
        return selectedTicketLiveData;
    }

    public LiveData<User> getUser(){
        return userRepository.getUserLiveData();
    }
    public LiveData<List<Ticket>> getTickets(){
        return ticketRepository.getTickets();
    }
    public LiveData<Event<Ticket>> getTicketAccepted(){
        return ticketRepository.getTicketAccepted();
    }

    public LiveData<String> getLocationLiveData() {
        return locationHelper.getLocationLiveData();
    }
}
