package com.matan.digitalstore.Utils;


import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import android.util.Base64;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * This class includes functions that handles the http connection with the server
 */
public class HttpUtil {
    private static final OkHttpClient client = new OkHttpClient();
    //please define here the address of the server
    //mac
    private static String baseURL = "http://Matans-Air.lan:8080/";
    //my home
//    private static String baseURL = "http://192.168.1.137:8080/";
    //iphone wifi
//    private static String baseURL = "http://172.20.10.2:8080/";


    private static String jwtToken;
    private static Long userId;
    /*
        this function gets a user name and password, and tries to perform a login to the
        server. If the login was successful, the jwt will be saved in the jwtToken static
        variable and true will be returned. otherwise, an error will be thrown.
     */
    public static boolean loginRequest(String username, String password) throws IOException, JSONException {

        JSONObject json = new JSONObject();
        json.put("username",username);
        json.put("password", password);
        jwtToken = postRequest("users/login", json,false).string();
        userId = Long.valueOf(extractSubject(jwtToken));
        return true;
    }

    /*
     * The function extracts the user id (sub) from the jwt
     */
    public static String extractSubject(String jwt) {
        try {
            String[] split = jwt.split("\\.");
            if (split.length < 2) {
                return null; // Not enough parts in the JWT
            }
            String payload = split[1];
            // Decode using android.util.Base64
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
            String decodedJson = new String(decodedBytes);
            JSONObject jsonObj = new JSONObject(decodedJson);
            return jsonObj.getString("sub");
        } catch (Exception e) {
            System.err.println("Failed to decode JWT: " + e.getMessage());
            return null;
        }
    }
    /*
    This method gets a url and a json object, and it tries to preform a post request
    where the json object will be sent in the request body. If there is a jwt in the jwtToken
    variable, it will be sent by default in the headers.
    The function returns the response from the server.
     */
    public static ResponseBody postRequest(String url, JSONObject json) throws IOException {
        boolean sendJWT= false;
        if(jwtToken!=null)
            sendJWT = true;
        return postRequest(url, json, sendJWT);
    }
    /*
    This function conv ers a json object to a requestBody that cant be sent in a post/put
    request body.
     */
    public static RequestBody convertJsonToRequestBody(JSONObject json){
        //Convert JsonObject to JSON String
        String jsonString = json.toString();
        // Create RequestBody
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonString, JSON);
        return requestBody;
    }
/*
This function gets a url, json object, and send s a post request, with the json object in
the request body.  Also if sendJWT is set to true,
it will be sent in the request headers.
The function returns the response from the server.
 */
    public static ResponseBody postRequest(String url, JSONObject json, boolean sendJWT) throws IOException {
        RequestBody requestBody = convertJsonToRequestBody(json);
        return postRequest(url,requestBody,sendJWT);
    }
    /*
    This function gets a url, Requestbody object, and send s a post request with the request
    body that was sent.  if sendJWT is set to true,
     it will be sent in the request headers.
     The function returns the response from the server.
     */
    public static ResponseBody postRequest(String url, RequestBody requestBody, boolean sendJWT) throws IOException {

        String fullUrl = baseURL + url;
        Request.Builder builder = new Request.Builder()
                .url(fullUrl)
                .post(requestBody);
        if(sendJWT){
            builder.header("Authorization",jwtToken);
        }
        Request request = builder.build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        return response.body();
    }
    /*
    This method gets a url and a json object, and it tries to preform a put request
    where the json object will be sent in the response body. If there is a jwt in the jwtToken
    variable, it will be sent by default in the headers.
    The function returns the response from the server.
     */
    public static ResponseBody putRequest(String url, JSONObject json) throws IOException {
        RequestBody requestBody = convertJsonToRequestBody(json);
        return putRequest( url, requestBody);
    }
    /*
      This method gets a url and a request body object, and it tries to preform a put request
      with the request body. If there is a jwt in the jwtToken
      variable, it will be sent by default in the headers.
      The function returns the response from the server.
       */
    public static ResponseBody putRequest(String url, RequestBody requestBody) throws IOException {

        String fullUrl = baseURL + url;
        Request request = new Request.Builder()
                    .url(fullUrl)
                    .header("Authorization",jwtToken)
                    .put(requestBody)
                    .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        return response.body();
    }
    /*
      This method gets a url and it tries to preform a get request
      If there is a jwt in the jwtToken
      variable, it will be sent by default in the headers.
      The function returns the response from the server.
       */
    public static ResponseBody getRequest(String url) throws IOException {
        boolean sendJWT= false;
        if(jwtToken!=null)
            sendJWT = true;
        return getRequest(url, sendJWT);
    }
    /*
      This method gets a url and it tries to preform a get request
      If there is a sendJWT is set the true,
      the jwtToken will be sent by default in the headers.
      The function returns the response from the server.
       */
        public static ResponseBody getRequest(String url, boolean sendJWT) throws IOException {
            String fullUrl = baseURL + url;
            Request.Builder builder = new Request.Builder()
                    .url(fullUrl)
                    .get();

            if (sendJWT) {
                builder.header("Authorization", jwtToken);
            }

            Request request = builder.build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            return response.body();
    }


    /*
    The function gets an image filename and returns the image url
    on the server.
     */
    public static String getImageURL(String image) {
        return baseURL +"images/"+ image;
    }
    public static Long getUserId(){
        return userId;
    }

}