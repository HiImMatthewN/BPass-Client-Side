package com.spcba.bpass.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.spcba.bpass.data.datamodels.TopUp;
import com.spcba.bpass.data.datautils.Event;
import com.spcba.bpass.repository.TopUpRepository;

public class TopUpDetailsViewModel extends ViewModel {
    private TopUpRepository topUpRepository = TopUpRepository.getInstance();

    private MutableLiveData<Event<TopUp>> selectedTopUpToView = new MutableLiveData<>();


    public void setTopUpToView(TopUp topUp){
        selectedTopUpToView.setValue(new Event<>(topUp));
    }
    public LiveData<Event<TopUp>> getSelectedTopUpToView() {
        return selectedTopUpToView;
    }

    public LiveData<Event<TopUp>> getAddedTopUp() {
        return topUpRepository.getTopUpLiveData();
    }
    public LiveData<Event<Boolean>> getTopUpAccepted(){
        return topUpRepository.getReceiptScannedLiveData();
    }
}
