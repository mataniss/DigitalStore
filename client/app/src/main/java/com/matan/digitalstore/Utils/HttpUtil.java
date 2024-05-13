package com.matan.digitalstore.Utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtil {
    private static final OkHttpClient client = new OkHttpClient();
    private static String baseURL = "http://192.168.1.147:8080/";
    private static String jwtToken;

    public static boolean loginRequest(String username, String password) throws IOException, JSONException {

        JSONObject json = new JSONObject();
        json.put("username",username);
        json.put("password", password);
        //Convert JsonObject to JSON String
        String jsonString = json.toString();
        // Create RequestBody
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonString, JSON);
        jwtToken = postRequest("users/login", requestBody).toString();
        return true;
    }

    public static ResponseBody postRequest(String url, RequestBody requestBody) throws IOException {
        String fullUrl = baseURL + url;
        Request request = new Request.Builder()
                .url(fullUrl)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        return response.body();
        }

        public static ResponseBody getRequest(String url) throws IOException {
            String fullUrl = baseURL + url;
            Request request = new Request.Builder()
                    .url(fullUrl)
                    .header("Authorization",jwtToken)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            return response.body();
    }
    }