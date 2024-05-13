package com.matan.digitalstore;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.matan.digitalstore.Utils.HttpUtil;
import com.squareup.picasso.Picasso;

public class PurchaseActivity extends AppCompatActivity {

    private Long id;
    private int quantity;
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

        id = getIntent().getLongExtra("id",-1);
        quantity = getIntent().getIntExtra("quantity",-1);
        productName.setText(getIntent().getStringExtra("name"));
        productDescription.setText(getIntent().getStringExtra("description"));
        productPrice.setText(getIntent().getDoubleExtra("price",-1) +"");
        String imageURL = HttpUtil.getImageURL(getIntent().getStringExtra("image"));
        Picasso.get().load(imageURL).into(productImage);

        System.out.println("!!!");
    }
}