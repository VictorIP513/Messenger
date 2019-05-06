package ru.android.messenger.model;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Model {

    private static final String SERVER_IP = "http://37.192.145.83:8080";

    private static Repository repository;

    static {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_IP)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        repository = retrofit.create(Repository.class);
    }

    private Model() {

    }

    public static Repository getRepository() {
        return repository;
    }
}
