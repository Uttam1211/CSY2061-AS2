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
import com.example.kewis.models.WishList;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishListViewHolder> {

    private Context context;
    private List<WishList> wishLists;
    private OnWishListClickListener onWishListClickListener;

    public WishListAdapter(Context context, List<WishList> wishLists, OnWishListClickListener onWishListClickListener) {
        this.context = context;
        this.wishLists = wishLists;
        this.onWishListClickListener = onWishListClickListener;
    }

    @NonNull
    @Override
    public WishListAdapter.WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new WishListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.WishListViewHolder holder, int position) {
        WishList wishList = wishLists.get(position);
        holder.textViewCartName.setText(wishList.getName());
        holder.textViewCartAdded.setText(String.valueOf(wishList.getDateAdded()));

        // Add right margin to the textViewCartAdded
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.textViewCartAdded.getLayoutParams();
        int marginRight = 66; // Set the desired right margin in pixels
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, marginRight, layoutParams.bottomMargin);
        holder.textViewCartAdded.setLayoutParams(layoutParams);

        // Toast.makeText(context, cart.getDateAdded(), Toast.LENGTH_SHORT).show();
        holder.buttonDeleteCategory.setOnClickListener(v -> {
            onWishListClickListener.onDeleteClick(wishList);
        });

    }

    @Override
    public int getItemCount() {
        return wishLists != null ? wishLists.size() : 0;
    }


    public interface OnWishListClickListener {
        void onDeleteClick(WishList wishList);
    }



    public class WishListViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCartName, textViewCartAdded;

        private ImageView buttonDeleteCategory;
        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCartName = itemView.findViewById(R.id.textView_cart_product);
            buttonDeleteCategory = itemView.findViewById(R.id.button_delete_cart);
            textViewCartAdded = itemView.findViewById(R.id.date_added);

        }
    }
}

