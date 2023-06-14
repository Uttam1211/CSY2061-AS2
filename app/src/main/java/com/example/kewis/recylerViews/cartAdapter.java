package com.example.kewis.recylerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kewis.R;
import com.example.kewis.models.Cart;
import com.example.kewis.models.Category;

import java.util.List;

//same like categories adapter
public class cartAdapter extends RecyclerView.Adapter<cartAdapter.CartViewHolder> {
    private Context context;
    private List<Cart> carts;
    private OnCartClickListener onCartClickListener;
    
    
    public cartAdapter(Context context, List<Cart> carts, OnCartClickListener onCartClickListener) {
        this.context = context;
        this.carts = carts;
        this.onCartClickListener = onCartClickListener;
    }
    
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(itemView);
    }
    
    
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = carts.get(position);
        holder.textViewCartName.setText(cart.getName());
        holder.textViewCartAdded.setText("£" + String.valueOf( cart.getPrice()));
       // Toast.makeText(context, cart.getDateAdded(), Toast.LENGTH_SHORT).show();
        holder.buttonDeleteCategory.setOnClickListener(v -> {
            onCartClickListener.onDeleteClick(cart);
        });

    }
    
    @Override
    public int getItemCount() {
        return carts != null ? carts.size() : 0;
    }

    public interface OnCartClickListener {
        void onDeleteClick(Cart cart);
    }


    public class CartViewHolder extends RecyclerView.ViewHolder {
       private TextView textViewCartName, textViewCartAdded;

       private ImageView buttonDeleteCategory;
        
        public CartViewHolder(@NonNull View itemView){
            super(itemView);
            textViewCartName = itemView.findViewById(R.id.textView_cart_product);
            buttonDeleteCategory = itemView.findViewById(R.id.button_delete_cart);
            textViewCartAdded = itemView.findViewById(R.id.date_added);

        }
    }

}
