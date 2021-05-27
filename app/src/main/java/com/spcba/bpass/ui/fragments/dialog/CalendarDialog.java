package com.spcba.bpass.ui.fragments.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.spcba.bpass.databinding.DialogCalendarBinding;
import com.spcba.bpass.ui.viewmodels.LobbyActivityViewModel;

import java.util.Calendar;

public class CalendarDialog extends DialogFragment {
    private DialogCalendarBinding binder;
    private CalendarView calendarView;
    private Button okBtn;
    private LobbyActivityViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            binder = DialogCalendarBinding.inflate(inflater,container,false);
            return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LobbyActivityViewModel.class);
        calendarView = binder.calendarView;
        okBtn = binder.okBtn;

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            viewModel.setSelectedDate(calendar.getTime());
        });
        okBtn.setOnClickListener(btn->{
            if (getDialog() == null) return;
            getDialog().dismiss();

        });
        viewModel.getSelectedDateLiveData().observe(getViewLifecycleOwner(),currentSelectedDate->{
            calendarView.setDate(currentSelectedDate.getTime());
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) return;
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        window.setBackgroundDrawable(inset);
    }
}
