package com.spcba.bpass.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.spcba.bpass.data.datamodels.TopUp;
import com.spcba.bpass.repository.TopUpRepository;

import java.util.List;

public class TopUpHistoryViewModel extends ViewModel {
    private TopUpRepository topUpRepository = TopUpRepository.getInstance();

    public void requestTopUpHistory() {
        topUpRepository.requestTopUpHistory();
    }

    public LiveData<List<TopUp>> getTopUpHistory() {
        return topUpRepository.getTopUpHistory();
    }


}
