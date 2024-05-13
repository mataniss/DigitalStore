package com.matan.digitalstore;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matan.digitalstore.Utils.HttpUtil;
import com.matan.digitalstore.model.Product;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FetchProducts fetchProducts = new FetchProducts();
        fetchProducts.execute();
    }

    public class FetchProducts extends AsyncTask< Integer, Integer, ResponseBody > {

        @Override
        protected ResponseBody doInBackground(Integer...integers) {
            ResponseBody responseBody= null;
            try {
                 responseBody = HttpUtil.getRequest("products/");
                System.out.println("Products List was fetched successfully");
                return responseBody;
            } catch (IOException  e) {
                String message = "Fetch failed " + e.toString();
                System.err.println(message);
            }
            return responseBody;
        }

        @Override
        protected void onPostExecute(ResponseBody responseBody) {
            //convert the response body to an arrayList of product
            Gson gson = new Gson();
            try {
                ArrayList<Product> products = gson.fromJson(responseBody.string(), productListType);
                System.out.println("Products List was processed successfully");
                //todo: display the products list in the activity

            } catch (IOException e) {
                System.err.println("An error occurred while parsing product list "+ e.toString());
            }
        }
    }
}