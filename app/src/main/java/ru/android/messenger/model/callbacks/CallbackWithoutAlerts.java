package ru.android.messenger.model.callbacks;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;

public abstract class CallbackWithoutAlerts<T> implements Callback<T> {

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        // unused method
    }
}
