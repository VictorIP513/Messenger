package ru.android.messenger.model.callbacks;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.android.messenger.view.interfaces.ViewWithAlerts;

public class DefaultCallback<T, V extends ViewWithAlerts> implements Callback<T> {

    private V view;

    protected DefaultCallback(V view) {
        this.view = view;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        view.cancelWaitAlertDialog();
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        view.cancelWaitAlertDialog();
        view.showConnectionErrorAlertDialog();
    }
}
