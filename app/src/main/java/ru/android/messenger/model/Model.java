package ru.android.messenger.model;

import java.io.File;

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

    private static Repository repository;

    static {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(client)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
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

    public static MultipartBody.Part createFileToSend(File file, String partName) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
