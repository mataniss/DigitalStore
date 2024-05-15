package com.matan.digitalstore;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.matan.digitalstore.Utils.HttpUtil;
import com.matan.digitalstore.model.Product;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.ResponseBody;

public class PurchaseActivity extends AppCompatActivity {

    private int selectedQuantity;
    private Product product;
    private ImageView productImage;
    private TextView productName;
    private TextView productDescription;
    private TextView productPrice;
    private TextView quantityText;
    private Button minusButton;
    private Button plusButton;
    private Button purchaseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_purchase);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        quantityText = findViewById(R.id.quantityText);
        minusButton = findViewById(R.id.minusButton);
        plusButton = findViewById(R.id.plusButton);
        purchaseButton = findViewById(R.id.purchaseButton);

        product = getIntent().getParcelableExtra("product");
        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        productPrice.setText(product.getPrice() +"₪");
        String image = product.getImage();
        if(image != null && image.length()>0){
            String imageURL = HttpUtil.getImageURL(image);
            Picasso.get().load(imageURL).into(productImage);
        }
        selectedQuantity = 1;
        minusButton.setEnabled(false);
        if(product.getQuantity() == 1)
            plusButton.setEnabled(false);

        minusButton.setOnClickListener(v -> {
            if (selectedQuantity > 1) {
                selectedQuantity--;
                quantityText.setText(String.valueOf(selectedQuantity));
                plusButton.setEnabled(true);
            }
            if(selectedQuantity == 1 )   minusButton.setEnabled(false);
                else minusButton.setEnabled(true);

        });

        plusButton.setOnClickListener(v -> {
            if (selectedQuantity < product.getQuantity() ) {
                selectedQuantity++;
                quantityText.setText(String.valueOf(selectedQuantity));
                minusButton.setEnabled(true);
            }
            if(selectedQuantity == product.getQuantity())   plusButton.setEnabled(false);
            else plusButton.setEnabled(true);
        });

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MakePurchase makePurchase = new MakePurchase();
                makePurchase.execute();
            }
        });
    }

    public class MakePurchase extends AsyncTask< Integer, Integer, Long> {

        @Override
        protected Long doInBackground(Integer...integers) {
            Long purchaseID = null;
            try {
                ResponseBody responseBody = HttpUtil.getRequest("purchase/makePurchase/"+product.getId() + "?quantity="+ selectedQuantity);
                 purchaseID = Long.valueOf(responseBody.string());

            } catch (IOException e) {
                System.err.println("An error occurred while trying to make purchase.");
            }
            return purchaseID;
        }

        @Override
        protected void onPostExecute(Long purchaseID) {
            if(purchaseID == null){
                String message = "An error occurred while trying to make purchase";
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
            finish();

        }
    }

}