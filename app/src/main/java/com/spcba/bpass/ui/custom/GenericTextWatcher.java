package com.spcba.bpass.ui.custom;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.spcba.bpass.R;

public class GenericTextWatcher implements TextWatcher {
    private EditText etPrev;
    private EditText etNext;
    private EditText etCurrent;
    private Context context;
    public GenericTextWatcher(EditText etNext,EditText etCurrent, EditText etPrev,Context context) {
        this.etPrev = etPrev;
        this.etNext = etNext;
        this.etCurrent = etCurrent;
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        if (text.length() == 1){
            etNext.requestFocus();
            etPrev.setBackground(ContextCompat.getDrawable(context, R.drawable.border_edittext_bg));
            etCurrent.setBackground(ContextCompat.getDrawable(context,R.drawable.border_edittext_bg));
            etNext.setBackground(ContextCompat.getDrawable(context,R.drawable.border_edittext_bg_dark));
        }
        else if (text.length() == 0){
            etPrev.requestFocus();
            etPrev.setBackground(ContextCompat.getDrawable(context,R.drawable.border_edittext_bg_dark));
            etCurrent.setBackground(ContextCompat.getDrawable(context,R.drawable.border_edittext_bg));
            etNext.setBackground(ContextCompat.getDrawable(context,R.drawable.border_edittext_bg));

        }
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }
}