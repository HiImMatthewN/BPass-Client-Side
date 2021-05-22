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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LobbyActivityViewModel extends AndroidViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private TicketRepository ticketRepository = TicketRepository.getInstance();
    private TopUpRepository topUpRepository = TopUpRepository.getInstance();
    private TripRepository tripRepository = TripRepository.getInstance();
    private LocationHelper locationHelper = LocationHelper.getInstance();

    private MutableLiveData<Event<Trip>> selectedTripLivaData = new MutableLiveData<>();
    private MutableLiveData<Event<Ticket>> selectedTicketLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Trip>> tripsLiveData = new MutableLiveData<>();
    private MutableLiveData<Date> selectedDateLiveData = new MutableLiveData<>();
    private List<String> chipsSelected = new ArrayList<>();
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
//        tripRepository.addSchedule();


        Calendar calendar = Calendar.getInstance();
        setSelectedDate(calendar.getTime());

    }


    public void setSelectedDate(Date selectedDate){
        selectedDateLiveData.setValue(selectedDate);
        getTrips(selectedDate);

    }
    public void filter(String value,boolean addFilter){
        if (addFilter)
            chipsSelected.add(value);
        else
            chipsSelected.remove(value);



        List<Trip> trips = tripRepository.getTripsLiveData().getValue();

        if (chipsSelected.size() == 0){
            tripsLiveData.setValue(trips);
            return;
        }

        if (trips == null || trips.size() == 0) return;
         Observable.fromIterable(trips)
        .filter(trip -> chipsSelected.contains(trip.getDestination().getEndDestination()))
                .toList()
                .subscribeOn(Schedulers.io())
                .subscribe(result ->{
                    tripsLiveData.postValue(result);
                });
    }


    private void getTrips(Date date){
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        startCalendar.set(Calendar.HOUR_OF_DAY,0);
        startCalendar.set(Calendar.MINUTE,0);
        startCalendar.set(Calendar.SECOND,0);
        startCalendar.set(Calendar.MILLISECOND,0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(date);
        endCalendar.set(Calendar.HOUR_OF_DAY,23);
        endCalendar.set(Calendar.MINUTE,59);
        endCalendar.set(Calendar.SECOND,59);
        endCalendar.set(Calendar.MILLISECOND,0);

        Log.d(TAG, "End Date " + endCalendar.getTime());


        tripRepository.fetchTripViaDate(startCalendar.getTime(),endCalendar.getTime());

    }
    public void setSelectedTrip(Trip trip){
        Log.d(TAG, "setSelectedDestination: Selected");
        selectedTripLivaData.postValue(new Event<>(trip));
    }

    public void setSelectedTicket(Ticket ticket){
        selectedTicketLiveData.postValue(new Event<>(ticket));
    }
    public LiveData<Event<Trip>> getSelectedTrip() {
        return selectedTripLivaData;
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

        tripRepository.getTripsLiveData().observeForever(trips -> {
            if (chipsSelected.size() >0)
            Observable.fromIterable(trips)
                    .filter(trip -> chipsSelected.contains(trip.getDestination().getEndDestination()))
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .subscribe(result ->{
                        tripsLiveData.postValue(result);
                    });
            tripsLiveData.postValue(trips);

        });

        return tripsLiveData;
    }

    public LiveData<Date> getSelectedDateLiveData() {

        return selectedDateLiveData;
    }
}
