package com.matan.digitalstore;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matan.digitalstore.Utils.HttpUtil;
import com.matan.digitalstore.model.Product;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;

public class CatalogActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catalog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns in grid

        FetchProducts fetchProducts = new FetchProducts();
        fetchProducts.execute();
    }

    public class FetchProducts extends AsyncTask< Integer, Integer, ArrayList<Product> > {

        @Override
        protected ArrayList<Product> doInBackground(Integer...integers) {
            ArrayList<Product> products = new ArrayList<>();
            Gson gson = new Gson();
            Type productListType = new TypeToken<ArrayList<Product>>(){}.getType();
            ResponseBody responseBody= null;
            try {
                 responseBody = HttpUtil.getRequest("products/");
                System.out.println("Products List was fetched successfully");
                //convert the response body to an arrayList of product
                products = gson.fromJson(responseBody.string(), productListType);
                System.out.println("Products List was processed successfully");
                return products;
            } catch (IOException  e) {
                String message = "Fetch failed " + e.toString();
                System.err.println(message);
            }
            return products;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> products) {

            adapter = new ProductAdapter(getApplicationContext(), products);
            recyclerView.setAdapter(adapter);

        }
    }
}