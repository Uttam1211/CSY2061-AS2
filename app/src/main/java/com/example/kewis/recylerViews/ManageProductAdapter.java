package com.example.kewis.recylerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kewis.R;
import com.example.kewis.models.Product;

import java.util.List;

public class ManageProductAdapter extends RecyclerView.Adapter<ManageProductAdapter.ProductViewHolder>{
    private Context context;
    private OnProductClickListener onProductClickListener;
    private List<Product> products;

    public ManageProductAdapter(Context context, List<Product> products, OnProductClickListener onProductClickListener){
        this.context = context;
        this.products = products;
        this.onProductClickListener = onProductClickListener;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.manage_products_item, parent, false);
        return new ProductViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.textViewProductName.setText(product.getName());

        holder.buttonEditProduct.setOnClickListener(v -> {
            onProductClickListener.onEditClick(product);
        });

        holder.buttonDeleteProduct.setOnClickListener(v -> {
            onProductClickListener.onDeleteClick(product);
        });
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView textViewProductName;
        ImageView buttonEditProduct;
        ImageView buttonDeleteProduct;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textView_product_name);
            buttonEditProduct = itemView.findViewById(R.id.button_edit_product);
            buttonDeleteProduct = itemView.findViewById(R.id.button_delete_product);
        }
    }

    public interface OnProductClickListener {
            void onEditClick(Product product);
            void onDeleteClick(Product product);
        }

}
