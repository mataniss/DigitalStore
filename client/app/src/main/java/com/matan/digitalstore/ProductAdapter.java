package com.matan.digitalstore;

import com.matan.digitalstore.Utils.HttpUtil;
import com.matan.digitalstore.model.Product;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
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

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextViews in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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

    // total number of rows
    @Override
    public int getItemCount() {
        return products.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Product product;
        TextView nameTextView;
        TextView priceTextView;
        TextView quantityTextView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_name);
            priceTextView = itemView.findViewById(R.id.text_price);
            quantityTextView = itemView.findViewById(R.id.text_quantity);
            imageView = itemView.findViewById(R.id.image_product);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            System.out.println("clicked on product with id "+ product.getId());
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
            //start a new purchase activity with the properties of the current produt
            Intent i = new Intent(context, PurchaseActivity.class);
            i.putExtra("id",product.getId());
            i.putExtra("name",product.getName());
            i.putExtra("price",product.getPrice());
            i.putExtra("image",product.getImage());
            i.putExtra("description",product.getDescription());
            i.putExtra("quantity",product.getQuantity());
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    // convenience method for getting data at click position
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