package com.matan.digitalstore;

import com.matan.digitalstore.Utils.HttpUtil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.matan.digitalstore.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
/*
This class extends the RecyclerView.Adapter class and handles the products list in the catalog.
It's used by the catalog activity.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductItem> {
    private Context context;
    private List<Product> products;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    // data is passed into the constructor
    ProductAdapter(Context context, ArrayList<Product> products) {
        this.inflater = LayoutInflater.from(context);
        this.products = products;
        this.context = context;
    }

    // inflates the item layout from xml when needed
    @Override
    public ProductItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductItem(view);
    }

    // binds the data to the TextViews in each item
    @Override
    public void onBindViewHolder(ProductItem holder, int position) {
        Product product = products.get(position);
        holder.nameTextView.setText(product.getName());
        holder.product = product;
        holder.priceTextView.setText("₪" + product.getPrice());
        holder.quantityTextView.setText("Qty: " + product.getQuantity());
        if(product.getImage()!=null && product.getImage().length()>0){
            String imageURL = HttpUtil.getImageURL(product.getImage());
            Picasso.get().load(imageURL).into(holder.imageView);
        }
    }

    // total number of items
    @Override
    public int getItemCount() {
        return products.size();
    }

    // This class extends the ViewHolder class and represent a specific procuct in the
    //catalog list
    public class ProductItem extends RecyclerView.ViewHolder implements View.OnClickListener {

        Product product;
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        ImageView imageView;

        ProductItem(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_name);
            priceTextView = itemView.findViewById(R.id.text_price);
            quantityTextView = itemView.findViewById(R.id.text_quantity);
            imageView = itemView.findViewById(R.id.image_product);
            itemView.setOnClickListener(this);
        }
        /*
        When clicking on specific product, we want to open a new activity that enables
        to purchase the product. We sent the product object as an extra to the new activity
         */
        @Override
        public void onClick(View view) {
            System.out.println("clicked on product with id "+ product.getId());
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
            //start a new purchase activity with the properties of the current produt
            Intent intent = new Intent(context, PurchaseActivity.class);
            intent.putExtra("product",product);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    //  method for getting data at click position
    Product getItem(int id) {
        return products.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}