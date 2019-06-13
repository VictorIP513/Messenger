package ru.android.messenger.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.android.messenger.model.converterfactory.StringConverterFactory;

public class Model {

    private static final String SERVER_IP = "37.192.145.83:8080";
    private static final String SERVER_URL = "http://" + SERVER_IP;
    private static final String JSON_DATE_FORMAT = "dd MMM yyyy HH:mm:ss";
    private static final int READ_TIMEOUT_SECONDS = 15;
    private static final int CONNECT_TIMEOUT_SECONDS = 15;

    private static Repository repository;

    static {
        Gson gson = createGson();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(client)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        repository = retrofit.create(Repository.class);
    }

    private Model() {

    }

    public static Repository getRepository() {
        return repository;
    }

    public static String getServerIp() {
        return SERVER_IP;
    }

    public static String getServerUrl() {
        return SERVER_URL;
    }

    public static MultipartBody.Part createFileToSend(File file, String partName) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(JSON_DATE_FORMAT);
        return gsonBuilder.create();
    }
}
