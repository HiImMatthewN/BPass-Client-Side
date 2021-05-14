package com.spcba.bpass.ui.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spcba.bpass.data.datamodels.TopUp;
import com.spcba.bpass.data.datamodels.User;
import com.spcba.bpass.data.datautils.Event;
import com.spcba.bpass.repository.TopUpRepository;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class TopUpFragmentViewModel extends ViewModel {
    private TopUpRepository topUpRepository = TopUpRepository.getInstance();

    private String selectedPaymentOption = "";
    private int topUpAmount = 0;
    private User user = null;


    private MutableLiveData<Event<TopUp>> selectedTopUpToView = new MutableLiveData<>();


    private static final String TAG = "TopUpFragmentViewModel";



    public void setPaymentOption(String selectedOption) {
        this.selectedPaymentOption = selectedOption;
    }

    public void setAmount(int amount) {
        this.topUpAmount = amount;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void confirmPayment() {
        if (topUpAmount == 0 || selectedPaymentOption.equals("") || user == null) {
            Log.d(TAG, "confirmPayment: Payment Invalid ");
            return;
        }
        int randomNum = ThreadLocalRandom.current().nextInt(1000000000, 1999999999);
        Date date = new Date();
        TopUp topUp = new TopUp(topUpAmount, selectedPaymentOption, String.valueOf(randomNum), false, date);

        topUpRepository.addTopUpTransaction(topUp);
        Log.d(TAG, "confirmPayment: Amount to TopUp: " + topUpAmount + " To be paid by: " + selectedPaymentOption + " Ref Number: " + topUp.getRefNumber());

    }



    public LiveData<Event<Boolean>> getAddedTopUpStatus() {
        return topUpRepository.getAddTopUpSuccessLiveData();
    }




}
