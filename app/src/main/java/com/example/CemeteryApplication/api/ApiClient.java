package com.example.CemeteryApplication.api;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.CemeteryApplication.models.Grave;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import java.util.List;

public class ApiClient {
    private static final String BASE_URL = "http://your-server.com/api/";
    private static ApiClient instance;
    private RequestQueue requestQueue;
    private final Gson gson;

    private ApiClient(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        gson = new Gson();
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    public interface GravesCallback {
        void onSuccess(List<Grave> graves);
        void onError(String error);
    }

    public void getGravesByCemetery(String cemeteryName, GravesCallback callback) {
        String url = BASE_URL + "graves?cemetery=" + cemeteryName;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        List<Grave> graves = gson.fromJson(response.toString(),
                                new TypeToken<List<Grave>>(){}.getType());
                        callback.onSuccess(graves);
                    } catch (Exception e) {
                        callback.onError("Ошибка парсинга данных");
                    }
                },
                error -> callback.onError("Ошибка сети: " + error.getMessage())
        );

        requestQueue.add(request);
    }

    public void searchGraves(String query, String cemetery, GravesCallback callback) {
        String url = BASE_URL + "search?query=" + query + "&cemetery=" + cemetery;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        List<Grave> graves = gson.fromJson(response.toString(),
                                new TypeToken<List<Grave>>(){}.getType());
                        callback.onSuccess(graves);
                    } catch (Exception e) {
                        callback.onError("Ошибка парсинга данных");
                    }
                },
                error -> callback.onError("Ошибка сети: " + error.getMessage())
        );

        requestQueue.add(request);
    }
}