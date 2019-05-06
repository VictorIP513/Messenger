package ru.android.messenger.model.api;

import com.google.gson.Gson;

import okhttp3.ResponseBody;

public class ApiUtils {

    private static final Gson GSON = new Gson();

    private ApiUtils() {

    }

    public static <T> T getJsonFromResponseBody(ResponseBody responseBody, Class<T> type) {
        return GSON.fromJson(responseBody.charStream(), type);
    }
}
