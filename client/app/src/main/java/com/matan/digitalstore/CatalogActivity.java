package com.matan.digitalstore;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private FloatingActionButton plusBtn;

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
        // 2 columns in grid
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        plusBtn = findViewById(R.id.new_product_btn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostProductActivity.class);
                startActivity(intent);
            }
        });
        FetchProducts fetchProducts = new FetchProducts();
        fetchProducts.execute();
    }
    /*
    We want to refresh the catalog each time the activity is resumed.
     */
    @Override
    protected  void onResume(){
        super.onResume();
        //we want to update the products list when the activity is reloaded
        FetchProducts fetchProducts = new FetchProducts();
        fetchProducts.execute();
    }
    //Thread for fetching the product list
    public class FetchProducts extends AsyncTask< Integer, Integer, ArrayList<Product> > {
        private String error;
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
                error =  "Failed to fetch products: " + e.toString();
                System.err.println(error);
            }
            return products;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> products) {
            if(products!=null){
                //we use the product adapter for displaying the products dynamically
                //based on the repsonse from the server
                adapter = new ProductAdapter(getApplicationContext(), products);
                recyclerView.setAdapter(adapter);
            }
            else{
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
            }

        }
    }
}