package com.spcba.bpass.ui.fragments.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.spcba.bpass.data.datamodels.Destination;
import com.spcba.bpass.data.datamodels.Trip;
import com.spcba.bpass.databinding.DialogCheckoutBinding;
import com.spcba.bpass.ui.viewmodels.CheckoutDialogViewModel;
import com.spcba.bpass.ui.viewmodels.LobbyActivityViewModel;

import java.util.Date;

public class CheckoutDialog extends BottomSheetDialogFragment {
    private DialogCheckoutBinding binder;
    private ImageButton addAmountBtn;
    private ImageButton minusAmountBtn;
    private Button buyBtn;
    private TextView startDestination;
    private TextView endDestination;
    private TextView tripSchedule;
    private TextView leaveTime;
    private TextView arriveTime;
    private TextView totalPrice;
    private TextView totalAmount;


    private LobbyActivityViewModel viewModel;
    private CheckoutDialogViewModel checkoutDialogViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DialogCheckoutBinding.inflate(inflater,container,false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LobbyActivityViewModel.class);
        checkoutDialogViewModel = new ViewModelProvider(this).get(CheckoutDialogViewModel.class);
        startDestination = binder.startDestination;
        endDestination = binder.endDestination;
        leaveTime = binder.leaveTimeTv;
        arriveTime = binder.arriveTimeTv;
        tripSchedule = binder.tripSchedule;

        totalPrice = binder.currentPriceAmountTv;
        totalAmount = binder.currentTicketAmountTv;
        addAmountBtn = binder.addTicketAmountBtn;
        minusAmountBtn = binder.minusTicketAmountBtn;
        buyBtn = binder.buyTicketBtn;

        addAmountBtn.setOnClickListener(btn ->{
            checkoutDialogViewModel.addAmount();
        });
        minusAmountBtn.setOnClickListener(btn ->{
            checkoutDialogViewModel.minusAmount();
        });


        buyBtn.setOnClickListener(btn ->{
            checkoutDialogViewModel.checkout();
            getDialog().dismiss();

        });
        checkoutDialogViewModel.setUserDetail(viewModel.getUser().getValue());
        viewModel.getSelectedTrip().observe(getViewLifecycleOwner(), selectedDestinationEvent ->{
                    if (selectedDestinationEvent.isHandled()) return;
            Trip trip = selectedDestinationEvent.getContentIfNotHandled();
            Destination destination = trip.getDestination();
            startDestination.setText(destination.getStartDestination());
            endDestination.setText(destination.getEndDestination());
            leaveTime.setText(destination.getExpectLeaveTime());
            arriveTime.setText(destination.getExpectArriveTime());
            totalPrice.setText("₱" + destination.getFare());
            tripSchedule.setText(formatDate(trip.getSchedule()));
            checkoutDialogViewModel.setTrip(trip);
        });
        checkoutDialogViewModel.getTotalAmountLiveData().observe(getViewLifecycleOwner(),onTotalAmountChangeEvent ->{
                        if (onTotalAmountChangeEvent.isHandled()) return;
                        totalAmount.setText(String.valueOf(onTotalAmountChangeEvent.getContentIfNotHandled()));
        });

        checkoutDialogViewModel.getTotalPriceLiveData().observe(getViewLifecycleOwner(),totalPriceEvent ->{
                if (totalPriceEvent.isHandled()) return;
                totalPrice.setText("₱" +String.valueOf(totalPriceEvent.getContentIfNotHandled()));

        });

    }


    private String formatDate(Date date){
      String stringDate = String.valueOf(date);

      return stringDate.replace(" 00:00:00 GMT+08:00 ",",");


    }


}
