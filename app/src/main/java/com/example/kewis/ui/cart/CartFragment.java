package com.example.kewis.ui.cart;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kewis.R;
import com.example.kewis.SessionManager;
import com.example.kewis.databases.DatabaseHandler;
import com.example.kewis.databases.DatabaseHelper;
import com.example.kewis.databinding.FragmentCartBinding;
import com.example.kewis.models.Cart;
import com.example.kewis.models.Order;
import com.example.kewis.recylerViews.cartAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements cartAdapter.OnCartClickListener{

    private FragmentCartBinding binding;
    SessionManager sessionManager;
    private List<Cart> carts;
    DatabaseHandler db;
    private cartAdapter cartAdapter;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.cartRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        sessionManager = new SessionManager(getContext());
        db = new DatabaseHandler(getContext(),new DatabaseHelper(getContext()));
        carts = new ArrayList<>();

        loadCartItems(sessionManager.getUserId());

        Button btnCheckout = binding.cartStartShopping;
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_cart_to_navigation_dashboard);
            }
        });

        //TextView totalPrice = binding.cartTotalPrice;
        binding.cartBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new order
                long orderId = db.insertOrder(sessionManager.getUserId());

                // For each item in the cart
                for (Cart item : carts) {
                    // Add item to order_items
                    db.insertOrderItem(orderId, item.getProductId(), item.getQuantity());

                    // Remove item from cart
                    String[] whereArgs = { String.valueOf(item.getId()) };
                    db.delete("cart","id = ?", whereArgs);
                }

                // Clear the cart in the UI
                carts.clear();
                cartAdapter.notifyDataSetChanged();

                // Reload cart items
                loadCartItems(sessionManager.getUserId());

                // Show dialog
                new AlertDialog.Builder(getContext())
                        .setTitle("Order placed")
                        .setMessage("Your order has been successfully placed!")
                        .setPositiveButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        binding.cartOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Order> orders = db.getAllOrders(sessionManager.getUserId());
                StringBuilder sb = new StringBuilder();

                for(Order order : orders) {
                    sb.append("\n\n\nOrder ID: ");
                    sb.append(order.getId());
                    sb.append("\n\nProduct Name: ");
                    sb.append(order.getProductName());
                    sb.append("\n\nOrder Date: ");
                    sb.append(order.getOrderDate());
                    sb.append("\n\nOrder Status: ");
                    sb.append(order.getOrderStatus());
                    sb.append("\n\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Orders");
                builder.setMessage(sb.toString());
                builder.setPositiveButton("Save as file", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveOrderAsTextFile();
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });


        return root;
    }

    private void loadCartItems(int userId) {
        if (db.isInCartByUser(userId)) {
            carts = db.getAllCartItems(userId);
            cartAdapter = new cartAdapter(getContext(), carts, this);
            recyclerView.setAdapter(cartAdapter);
            binding.cartRecyclerView.setVisibility(View.VISIBLE);
            binding.cartEmpty.setVisibility(View.INVISIBLE);
            binding.cartBottomBar.setVisibility(View.VISIBLE);

            refreshCartTotal();
        } else {
            binding.cartRecyclerView.setVisibility(View.INVISIBLE);
            binding.cartBottomBar.setVisibility(View.INVISIBLE);
            binding.cartEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDeleteClick(Cart cart) {
        // Handle the delete category click event
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Cart Item");
        builder.setMessage("Are you sure you want to delete this ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] whereArgs = { String.valueOf(cart.getId()) }; // Assuming you have a method getId in your Cart class to get cart item's id
                int result = db.delete("cart","id = ?", whereArgs);

                if(result > 0){
                    Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    carts.remove(cart);
                    cartAdapter.notifyDataSetChanged();

                    refreshCartTotal();

                }else{
                    Toast.makeText(getContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public int calculateTotalPrice(List<Cart> carts) {
        int totalPrice = 0;
        int quantity = 1;

        for(Cart item : carts) {
            // Assuming getPrice() method returns the price of the item and getQuantity() returns the quantity
            totalPrice += item.getPrice() * quantity;
        }

        return totalPrice;
    }

    private void refreshCartTotal() {
        int totalPrice = calculateTotalPrice(carts);
        binding.cartTotalPrice.setText("£"+String.valueOf(totalPrice));
    }

    private void saveOrderAsTextFile() {
        // Retrieve the order details from the database based on the order ID
        List<Order> orders = db.getAllOrders(sessionManager.getUserId());

        // Create the content for the text file
        StringBuilder contentBuilder = new StringBuilder();
        for(Order order : orders) {
            contentBuilder.append("Order ID: ").append(order.getId()).append("\n");
            contentBuilder.append("Product Name: ").append(order.getProductName()).append("\n");
            contentBuilder.append("Order Date: ").append(order.getOrderDate()).append("\n");
            contentBuilder.append("Order Status: ").append(order.getOrderStatus()).append("\n\n");
        }


        String content = contentBuilder.toString();

        // Save the content as a text file
        File file = new File(getContext().getFilesDir(), "order_details.txt");
        try {
            FileWriter writer = new FileWriter(file);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Display a toast message with the file path
      //  Toast.makeText(getContext(), "Order details saved as order_details.txt", Toast.LENGTH_SHORT).show();
        Snackbar.make(getView(), "Order details saved as order_details.txt", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }



}
