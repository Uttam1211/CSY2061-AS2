package com.example.kewis;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.kewis.R;
import com.example.kewis.databases.DatabaseHandler;
import com.example.kewis.databases.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView productNameTextView;
    private TextView productDescriptionTextView, productNameTitleTextView;
    private TextView productPriceTextView;
    private TextView productQuantityTextView;
    private ImageView productImageView;
    SessionManager sessionManager;
    DatabaseHandler db;

    private Button addToCartButton, addToWishlistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        sessionManager = new SessionManager(getApplicationContext());
        productNameTitleTextView = findViewById(R.id.product_name_title_text_view);
        productNameTextView = findViewById(R.id.product_name_text_view);
        productDescriptionTextView = findViewById(R.id.product_description_text_view);
        productPriceTextView = findViewById(R.id.product_price_text_view);
        productQuantityTextView = findViewById(R.id.product_quantity);
        productImageView = findViewById(R.id.product_image_view);
        addToCartButton = findViewById(R.id.btnAddToCart);
        addToWishlistButton = findViewById(R.id.btnAddToFav);

        Intent intent = getIntent();
        int productId = intent.getIntExtra("Product_id", -1);

        if (productId != -1) {
            db = new DatabaseHandler(getApplicationContext(),new DatabaseHelper(getApplicationContext()));

            Cursor cursor = db.read("products", null, "id = ?", new String[]{String.valueOf(productId)}, null, null, null, null);

            if (cursor.moveToFirst()) {
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String productDescription = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String productPrice = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                String productQuantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));
                String productImageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));

                productNameTextView.setText(productName);
                productDescriptionTextView.setText(productDescription);
                productPriceTextView.setText(productPrice);
                productQuantityTextView.setText(productQuantity);
                productNameTitleTextView.setText(productName);

                Glide.with(this)
                        .load(productImageUrl)
                        .into(productImageView);
            }

            cursor.close();
        } else {
            Toast.makeText(this, "Product ID not found", Toast.LENGTH_LONG).show();
        }

        addToCartButton.setOnClickListener(v -> {

            //Get user_id from Session Manager
            int user_id = sessionManager.getUserId();
            //Assuming product_id is available
            int product_id = productId;

            if (db.isInCart(user_id, product_id)) {
                Snackbar.make(v, "Product is already in cart", Snackbar.LENGTH_SHORT).show();
            } else {
                if (db.insertCart(user_id, product_id)) {
                   Toast.makeText(v.getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                   // Snackbar.make(v, "Added to Cart", Snackbar.LENGTH_SHORT).show();
                    recreate();
                } else {
                    //Toast.makeText(v.getContext(), "Failed to add to Cart", Toast.LENGTH_SHORT).show();
                    Snackbar.make(v, "Failed to add to Cart", Snackbar.LENGTH_SHORT).show();
                }
        }});

        addToWishlistButton.setOnClickListener(v -> {

            //Get user_id from Session Manager
            int user_id = sessionManager.getUserId();
            //Assuming product_id is available
            int product_id = productId;

            if (db.isInWishlist(user_id, product_id)) {
                Snackbar.make(v, "Product is already in wishlist", Snackbar.LENGTH_SHORT).show();
            } else {
                if (db.insertWishlist(user_id, product_id)) {
                   Toast.makeText(v.getContext(), "Added to Wishlist", Toast.LENGTH_SHORT).show();
                 //   Snackbar.make(v, "Added to Wishlist", Snackbar.LENGTH_SHORT).show();
                    recreate();
                } else {
                    //Toast.makeText(v.getContext(), "Failed to add to Wishlist", Toast.LENGTH_SHORT).show();
                    Snackbar.make(v, "Failed to add to Wishlist", Snackbar.LENGTH_SHORT).show();
                }
            }});
    }



}
