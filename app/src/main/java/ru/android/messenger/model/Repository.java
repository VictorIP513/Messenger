package ru.android.messenger.model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.android.messenger.model.api.RegistrationResponse;

public interface Repository {

    @POST("/api/registration")
    Call<RegistrationResponse> registerUser(@Body User user);
}
