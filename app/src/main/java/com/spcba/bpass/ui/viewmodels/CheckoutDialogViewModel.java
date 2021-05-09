package com.spcba.bpass.ui.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spcba.bpass.Event;
import com.spcba.bpass.data.datamodels.Destination;
import com.spcba.bpass.data.datamodels.Ticket;
import com.spcba.bpass.data.datamodels.User;
import com.spcba.bpass.repository.TicketRepository;
import com.spcba.bpass.repository.UserRepository;

import java.util.UUID;

public class CheckoutDialogViewModel extends ViewModel {
    private UserRepository userRepository = UserRepository.getInstance();
    private TicketRepository ticketRepository = TicketRepository.getInstance();



    private int totalTickets = 1;
    private int fare = 0;
    private int totalPrice = 0;
    private MutableLiveData<Event<Integer>> totalPriceLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Integer>> totalAmountLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> isBalanceEnough = new MutableLiveData<>();
    private User user;
    private Destination selectedDestination;

    private static final String TAG = "CheckoutDialogViewModel";
    public void setDestination(Destination destination) {
        this.selectedDestination = destination;
        this.fare = destination.getFare();
        this.totalPrice = fare;
    }


    public void setUserDetail(User user) {
        this.user = user;

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
                ticketRepository.addTicket(createTicket(selectedDestination));
            }

        }

    }
    public Ticket createTicket(Destination destination){
        String id = UUID.randomUUID().toString().substring(0,8);
        return new Ticket(id,destination,false);

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

}
