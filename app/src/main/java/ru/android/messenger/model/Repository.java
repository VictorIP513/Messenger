package ru.android.messenger.model;

import java.util.Date;
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
import ru.android.messenger.model.dto.Dialog;
import ru.android.messenger.model.dto.Message;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.dto.response.FriendStatus;
import ru.android.messenger.model.dto.response.LoginResponse;
import ru.android.messenger.model.dto.response.RegistrationResponse;
import ru.android.messenger.model.dto.response.RestorePasswordResponse;

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

    @Multipart
    @POST("/api/createDialog")
    Call<Dialog> createDialog(@Part("login") String login,
                              @Part("lastMessageText") String lastMessageText,
                              @Part("authenticationToken") String authenticationToken);

    @Multipart
    @POST("/api/sendMessage/{dialogId}")
    Call<Message> sendMessage(@Path("dialogId") int dialogId,
                              @Part("message") String message,
                              @Part("authenticationToken") String authenticationToken);

    @PATCH("/api/addToFriend/{login}")
    Call<Void> addToFriend(@Path("login") String login,
                           @Query("authenticationToken") String authenticationToken);

    @PATCH("/api/deleteFromFriend/{login}")
    Call<Void> deleteFromFriend(@Path("login") String login,
                                @Query("authenticationToken") String authenticationToken);

    @PATCH("/api/acceptFriendRequest/{login}")
    Call<Void> acceptFriendRequest(@Path("login") String login,
                                   @Query("authenticationToken") String authenticationToken);

    @PATCH("/api/blockUser/{login}")
    Call<Void> blockUser(@Path("login") String login,
                         @Query("authenticationToken") String authenticationToken);

    @PATCH("/api/unlockUser/{login}")
    Call<Void> unlockUser(@Path("login") String login,
                          @Query("authenticationToken") String authenticationToken);

    @PATCH("/api/restorePassword")
    Call<RestorePasswordResponse> restorePassword(@Query("login") String login,
                                                  @Query("newPassword") String newPassword);

    @PATCH("/api/userIsOnline")
    Call<Void> userIsOnline(@Query("authenticationToken") String authenticationToken);

    @GET("/api/checkAuthenticationToken/{authenticationToken}")
    Call<Boolean> checkAuthenticationToken(@Path("authenticationToken") String authenticationToken);

    @GET("/api/getUser/{login}")
    Call<User> getUser(@Path("login") String login);

    @Streaming
    @GET("/api/getUserPhoto/{login}")
    Call<ResponseBody> getUserPhoto(@Path("login") String login);

    @GET("/api/getAllUsers")
    Call<List<User>> getAllUsers();

    @GET("/api/getFriends")
    Call<List<User>> getFriends(@Query("authenticationToken") String authenticationToken);

    @GET("/api/getIncomingRequests")
    Call<List<User>> getIncomingRequests(@Query("authenticationToken") String authenticationToken);

    @GET("/api/getOutgoingRequests")
    Call<List<User>> getOutgoingRequests(@Query("authenticationToken") String authenticationToken);

    @GET("/api/getFriendStatus/{login}")
    Call<FriendStatus> getFriendStatus(@Path("login") String login,
                                       @Query("authenticationToken") String authenticationToken);

    @GET("/api/getDialog/{login}")
    Call<Dialog> getDialog(@Path("login") String login,
                           @Query("authenticationToken") String authenticationToken);

    @GET("/api/getAllDialogs")
    Call<List<Dialog>> getAllDialogs(@Query("authenticationToken") String authenticationToken);

    @GET("/api/getBlockStatus/{login}")
    Call<Boolean> getBlockStatus(@Path("login") String login,
                                 @Query("authenticationToken") String authenticationToken);

    @GET("/api/getBlockYouStatus/{login}")
    Call<Boolean> getBlockYouStatus(@Path("login") String login,
                                    @Query("authenticationToken") String authenticationToken);

    @GET("/api/getAllBlockedUsers")
    Call<List<User>> getAllBlockedUsers(@Query("authenticationToken") String authenticationToken);

    @GET("/api/getLastOnlineDate/{login}")
    Call<Date> getLastOnlineDate(@Path("login") String login);
}
