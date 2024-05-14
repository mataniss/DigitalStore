package com.matan.digitalstore.Utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtil {
    private static final OkHttpClient client = new OkHttpClient();
//    private static String baseURL = "http://10.0.0.147:8080/";
    private static String baseURL = "http://192.168.1.147:8080/";

    private static String jwtToken;

    public static boolean loginRequest(String username, String password) throws IOException, JSONException {

        JSONObject json = new JSONObject();
        json.put("username",username);
        json.put("password", password);
        jwtToken = postRequest("users/login", json,false).string();

        return true;
    }

    public static ResponseBody postRequest(String url, JSONObject json) throws IOException {
        boolean sendJWT= false;
        if(jwtToken!=null)
            sendJWT = true;
        return postRequest(url, json, sendJWT);
    }

    public static ResponseBody postRequest(String url, JSONObject json, boolean sendJWT) throws IOException {
        //Convert JsonObject to JSON String
        String jsonString = json.toString();
        // Create RequestBody
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonString, JSON);

        String fullUrl = baseURL + url;
        Request request ;
        if(sendJWT){
            request = new Request.Builder()
                    .url(fullUrl)
                    .header("Authorization",jwtToken)
                    .post(requestBody)
                    .build();
        }
        else {
            request = new Request.Builder()
                    .url(fullUrl)
                    .post(requestBody)
                    .build();
        }

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        return response.body();
        }

    public static ResponseBody getRequest(String url) throws IOException {
        boolean sendJWT= false;
        if(jwtToken!=null)
            sendJWT = true;
        return getRequest(url, sendJWT);
    }

        public static ResponseBody getRequest(String url, boolean sendJWT) throws IOException {
            String fullUrl = baseURL + url;
            Request request;
            if(sendJWT){
                request = new Request.Builder()
                        .url(fullUrl)
                        .header("Authorization",jwtToken)
                        .get()
                        .build();
            }
             else {
                request = new Request.Builder()
                        .url(fullUrl)
                        .get()
                        .build();
            }
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            return response.body();
    }



    public static String getImageURL(String image) {
        return baseURL +"images/"+ image;
    }
}