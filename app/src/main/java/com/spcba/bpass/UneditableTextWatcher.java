package com.spcba.bpass;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

public class UneditableTextWatcher implements TextWatcher {
    private EditText editText;
    private String constant;

    public UneditableTextWatcher(EditText editText, String constant) {
        this.editText = editText;
        this.constant = constant;
        Selection.setSelection(editText.getText(), editText.getText().length());

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!s.toString().startsWith(constant)){
            editText.setText(constant);
            Selection.setSelection(editText.getText(), editText.getText().length());

        }
    }
}
