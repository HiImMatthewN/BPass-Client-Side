package com.spcba.bpass.ui.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spcba.bpass.data.datamodels.Destination;
import com.spcba.bpass.data.datamodels.Ticket;
import com.spcba.bpass.data.datamodels.Trip;
import com.spcba.bpass.data.datamodels.User;
import com.spcba.bpass.data.datautils.Event;
import com.spcba.bpass.repository.TicketRepository;
import com.spcba.bpass.repository.TripRepository;
import com.spcba.bpass.repository.UserRepository;

import java.util.Date;
import java.util.UUID;

public class CheckoutDialogViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private TicketRepository ticketRepository = TicketRepository.getInstance();
    private TripRepository tripRepository = TripRepository.getInstance();


    private int totalTickets = 1;
    private int fare = 0;
    private int totalPrice = 0;
    private MutableLiveData<Event<Integer>> totalPriceLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Integer>> totalAmountLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> isBalanceEnough = new MutableLiveData<>();
    private MutableLiveData<Event<Date>> reminderAlarmDateLiveData = new MutableLiveData<>();

    private User user;
    private Trip selectedTrip;

    private static final String TAG = "CheckoutDialogViewModel";
    public void setTrip(Trip trip) {

        this.selectedTrip = trip;
        Destination destination = trip.getDestination();
        this.fare = destination.getFare();
        this.totalPrice = fare;
        reminderAlarmDateLiveData.postValue(new Event<>(trip.getDestination().getExpectLeaveTime()));


    }


    public void setUserDetail(User user) {
        this.user = user;

    }
    public void checkIfBalanceIsEnough(){

        isBalanceEnough.setValue(new Event<>(user.getBalance()>=totalPrice));


    }
    public void checkout() {
        if ( totalPrice > user.getBalance()){
            isBalanceEnough.setValue(new Event<>(false));
            Log.d(TAG, "buyTickets: Balance not enough");
        }else{
            Log.d(TAG, "buyTickets: Balance is enough");
            double newBalance = user.getBalance() - totalPrice;
            userRepository.updateNewBalance(newBalance);
            for (int i=1;i<=totalTickets;i++){
                ticketRepository.addTicket(createTicket(selectedTrip));
            }
            tripRepository.deductSeat(selectedTrip.getBusNumber(),totalTickets);
        }

    }
    public Ticket createTicket(Trip trip){
        String id = UUID.randomUUID().toString().substring(0,8);
        return new Ticket(id, trip.getDestination(),false,trip.getSchedule(),trip.getBusNumber());

    }
    public void addAmount() {
        if (totalTickets == 80) return;

        totalTickets++;
        totalPrice = fare * totalTickets;
        totalPriceLiveData.setValue(new Event<>(totalPrice));
        totalAmountLiveData.setValue(new Event<>(totalTickets));
    }

    public void minusAmount() {
        if (totalTickets == 1) return;
        totalTickets--;

        totalPrice = fare * totalTickets;
        totalPriceLiveData.setValue(new Event<>(totalPrice));
        totalAmountLiveData.setValue(new Event<>(totalTickets));
    }

    public LiveData<Event<Integer>> getTotalPriceLiveData() {
        return totalPriceLiveData;
    }

    public LiveData<Event<Integer>> getTotalAmountLiveData() {
        return totalAmountLiveData;
    }

    public LiveData<Event<Date>> getReminderAlarmDate() {
        return reminderAlarmDateLiveData;
    }

    public LiveData<Event<Boolean>> getIsBalanceEnough() {
        return isBalanceEnough;
    }
}
