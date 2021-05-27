package com.spcba.bpass.ui.fragments.dialog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.spcba.bpass.data.datautils.StringUtils;
import com.spcba.bpass.databinding.DialogAddAlarmBinding;
import com.spcba.bpass.ui.recievers.AlarmReceiver;
import com.spcba.bpass.ui.viewmodels.CheckoutDialogViewModel;

import java.util.Calendar;
import java.util.Date;

public class AddAlarmDialog extends DialogFragment {
    private DialogAddAlarmBinding binder;
    private Button addBtn;
    private Button noBtn;

    private static final String TAG = "AddAlarmDialog";
    private CheckoutDialogViewModel checkoutViewModel;

    //Todo:Add AddAlarmDialog ViewModel
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DialogAddAlarmBinding.inflate(inflater, container, false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkoutViewModel = new ViewModelProvider(requireActivity()).get(CheckoutDialogViewModel.class);
        addBtn = binder.addBtn;
        noBtn = binder.noBtn;

        noBtn.setOnClickListener(btn -> {
            if (getDialog() == null) return;
            getDialog().dismiss();
        });
        addBtn.setOnClickListener(btn -> {

            checkoutViewModel.getReminderAlarmDate().observe(getViewLifecycleOwner(), reminderEvent -> {
                if (reminderEvent.isHandled()) return;
                setAlarm(reminderEvent.getContentIfNotHandled());


            });
            if (getDialog() != null)
                getDialog().dismiss();

        });

    }

    private void setAlarm(Date triggerDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(triggerDate);
        calendar.add(Calendar.HOUR_OF_DAY, -1);


        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), AlarmReceiver.class);
        intent.putExtra("AlarmMessage", "You have a trip at " + StringUtils.formatTime(triggerDate));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pendingIntent), pendingIntent);

        Log.d(TAG, "setAlarm: Alarm, will trigger at " + triggerDate);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) return;
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 60);
        window.setBackgroundDrawable(inset);
    }


}
