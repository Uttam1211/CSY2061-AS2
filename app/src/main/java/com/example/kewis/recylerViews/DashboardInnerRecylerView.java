package com.example.kewis.recylerViews;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kewis.CategoryProductsActivity;
import com.example.kewis.ProductDetailActivity;
import com.example.kewis.R;
import com.example.kewis.databases.DatabaseHandler;
import com.example.kewis.models.InnerData;

import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;
import com.example.kewis.models.Product;
import com.example.kewis.ui.dashboard.DashboardFragment;

public class DashboardInnerRecylerView extends RecyclerView.Adapter<DashboardInnerRecylerView.ViewHolder> {
    private List<InnerData> innerDataList;
    private List<String> titles;
    private boolean isShopByCategory;
    DatabaseHandler db;

    public DashboardInnerRecylerView(List<InnerData> innerDataList, boolean isShopByCategory, DatabaseHandler db){
        this.innerDataList = innerDataList;
        this.isShopByCategory = isShopByCategory;
            this.db = db;
    }

    @NonNull
    @Override
    public DashboardInnerRecylerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_body_recycler_body_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardInnerRecylerView.ViewHolder holder, int position) {
        InnerData data = innerDataList.get(position);
        holder.product_textView.setText(data.getText());

        // Show or hide the price TextView based on the flag
        if (isShopByCategory) {
            holder.product_price_textView.setVisibility(View.GONE);

        } else {
            holder.product_price_textView.setVisibility(View.VISIBLE);
            holder.product_price_textView.setText(String.valueOf(data.getPrice()));
        }


        Glide.with(holder.imageView.getContext())
                .load(data.getImageUrl())
                .error(R.drawable.no_pictures)
                .placeholder(R.drawable.no_pictures)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return innerDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView imageView;
        TextView product_textView, product_price_textView;

        ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.product_card_view);
            imageView = itemView.findViewById(R.id.product_image_view);
            product_textView = itemView.findViewById(R.id.product_name_text_view);
            product_price_textView = itemView.findViewById(R.id.product_price_text_view);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the clicked item
                    InnerData clickedItem = innerDataList.get(getBindingAdapterPosition());
                    int id = clickedItem.getId();

                    // Check if it's a category
                    if (isShopByCategory) {
                       // Toast.makeText(v.getContext(), "Category: " + clickedItem.getId(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), CategoryProductsActivity.class);
                        intent.putExtra("category", id);
                        v.getContext().startActivity(intent);

                    } else {
                        // Handle the case when it's not a category
                       // Toast.makeText(v.getContext(), "Product: " + clickedItem.getId(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
                        intent.putExtra("Product_id", id);
                        v.getContext().startActivity(intent);
                    }
                }
            });

        }
    }


}