package ru.android.messenger.view.utils;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class ViewUtils {

    private ViewUtils() {

    }

    public static void clearErrorInTextInputLayoutOnChangeText(
            final TextInputLayout textInputLayout, final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // unused method
            }

            @Override
            public void afterTextChanged(Editable s) {
                // unused method
            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                textInputLayout.setError(null);
            }
        });
    }
}
