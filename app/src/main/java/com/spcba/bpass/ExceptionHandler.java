package com.spcba.bpass;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private String TAG = "ExceptionHandler";
    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        Log.d(TAG, "uncaughtException: Exception sent to crashlytics" + e.getMessage());
        FirebaseCrashlytics.getInstance().recordException(e);
    }
}
