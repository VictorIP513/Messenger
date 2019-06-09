package ru.android.messenger.model.api;

import com.google.gson.Gson;

import okhttp3.ResponseBody;
import ru.android.messenger.model.Model;

public class ApiUtils {

    private static final Gson GSON = Model.createGson();

    private ApiUtils() {

    }

    public static <T> T getJsonFromResponseBody(ResponseBody responseBody, Class<T> type) {
        return GSON.fromJson(responseBody.charStream(), type);
    }

    public static <T> T getObjectFromJson(String json, Class<T> type) {
        return GSON.fromJson(json, type);
    }
}
