package ru.android.messenger.model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import ru.android.messenger.model.api.LoginResponse;
import ru.android.messenger.model.api.RegistrationResponse;

public interface Repository {

    @POST("/api/registration")
    Call<RegistrationResponse> registerUser(@Body User user);

    @Multipart
    @POST("/api/login")
    Call<LoginResponse> login(@Part("login") String login, @Part("password") String password);
}
