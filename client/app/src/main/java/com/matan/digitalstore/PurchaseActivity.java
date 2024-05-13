package com.matan.digitalstore;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PurchaseActivity extends AppCompatActivity {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Integer quantity;
    private String image;
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
        id = getIntent().getLongExtra("id",-1);
        name = getIntent().getStringExtra("name");
        description = getIntent().getStringExtra("description");
        image = getIntent().getStringExtra("image");
        price = getIntent().getIntExtra("price",-1);
        quantity = getIntent().getIntExtra("quantity",-1);

        System.out.println("!!!");
    }
}