package com.spcba.bpass.ui.fragments.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.spcba.bpass.databinding.DialogCalendarBinding;

import java.util.Calendar;

public class CalendarDialog extends DialogFragment {
    private DialogCalendarBinding binder;
    private CalendarView calendarView;

    //Todo: Create Ok Button
    //Todo: Send selected Date to @LobbyActivityViewModel
    //Todo:Add CardView as design

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            binder = DialogCalendarBinding.inflate(inflater,container,false);
            return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendarView = binder.calendarView;

        long dateToday = Calendar.getInstance().getTimeInMillis();
        calendarView.setDate(dateToday);

    }
}
