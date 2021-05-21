package com.spcba.bpass.ui.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.spcba.bpass.data.datamodels.Ticket;
import com.spcba.bpass.data.datamodels.Trip;
import com.spcba.bpass.data.datamodels.User;
import com.spcba.bpass.data.datautils.Event;
import com.spcba.bpass.helper.LocationHelper;
import com.spcba.bpass.repository.TicketRepository;
import com.spcba.bpass.repository.TopUpRepository;
import com.spcba.bpass.repository.TripRepository;
import com.spcba.bpass.repository.UserRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LobbyActivityViewModel extends AndroidViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private TicketRepository ticketRepository = TicketRepository.getInstance();
    private TopUpRepository topUpRepository = TopUpRepository.getInstance();
    private TripRepository tripRepository = TripRepository.getInstance();
    private LocationHelper locationHelper = LocationHelper.getInstance();

    private MutableLiveData<Event<Trip>> selectedDestinationLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Ticket>> selectedTicketLiveData = new MutableLiveData<>();

    private static final String TAG = "LobbyActivityViewModel";

        //Todo:Fetch Selected Date From Calendar Dialog
    public LobbyActivityViewModel(Application application) {
        super(application);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRepository.loadUserData(uid);
        ticketRepository.getUserTickets();
        ticketRepository.addTicketStatusChanged();
        topUpRepository.addReceiptStatusChanged();

        Calendar calendar = Calendar.getInstance();


        getTrips(calendar.getTime());

    }

    public void getTrips(Date startDate){
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        startCalendar.set(Calendar.HOUR_OF_DAY,0);
        startCalendar.set(Calendar.MINUTE,0);
        startCalendar.set(Calendar.SECOND,0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.DAY_OF_MONTH,22);
        endCalendar.set(Calendar.HOUR_OF_DAY,23);
        endCalendar.set(Calendar.MINUTE,59);
        endCalendar.set(Calendar.SECOND,59);
        Log.d(TAG, "End Date " + endCalendar.getTime());


        tripRepository.fetchTripViaDate(startCalendar.getTime(),endCalendar.getTime());



    }
    public void setSelectedDestination(Trip trip){
        Log.d(TAG, "setSelectedDestination: Selected");
        selectedDestinationLiveData.postValue(new Event<>(trip));
    }

    public void setSelectedTicket(Ticket ticket){
        selectedTicketLiveData.postValue(new Event<>(ticket));
    }
    public LiveData<Event<Trip>> getSelectedDestination() {
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
    public LiveData<List<Trip>> getTripsLiveData(){
        return tripRepository.getTripsLiveData();
    }
}
