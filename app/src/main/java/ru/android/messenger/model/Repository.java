package ru.android.messenger.model;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import ru.android.messenger.model.api.FriendStatus;
import ru.android.messenger.model.api.LoginResponse;
import ru.android.messenger.model.api.RegistrationResponse;
import ru.android.messenger.model.dto.User;

public interface Repository {

    @POST("/api/registration")
    Call<RegistrationResponse> registerUser(@Body User user);

    @Multipart
    @POST("/api/login")
    Call<LoginResponse> login(@Part("login") String login, @Part("password") String password);

    @Multipart
    @POST("/api/uploadPhoto")
    Call<Void> uploadPhoto(@Part MultipartBody.Part photo,
                           @Part("authenticationToken") String authenticationToken);

    @PATCH("/api/addToFriend/{login}")
    Call<Void> addToFriend(@Path("login") String login,
                                       @Query("authenticationToken") String authenticationToken);

    @GET("/api/checkAuthenticationToken/{authenticationToken}")
    Call<Boolean> checkAuthenticationToken(@Path("authenticationToken") String authenticationToken);

    @GET("/api/getUser/{login}")
    Call<User> getUser(@Path("login") String login);

    @Streaming
    @GET("/api/getUserPhoto/{login}")
    Call<ResponseBody> getUserPhoto(@Path("login") String login);

    @GET("/api/getAllUsers")
    Call<List<User>> getAllUsers();

    @GET("/api/getFriendStatus/{login}")
    Call<FriendStatus> getFriendStatus(@Path("login") String login,
                                       @Query("authenticationToken") String authenticationToken);
}
