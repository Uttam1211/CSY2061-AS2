package com.example.kewis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdminActivity extends AppCompatActivity {

    private CardView cardViewAddCategory, cardViewAddFeed;
   private CardView cardViewAddProduct;
    private CardView cardViewAddUsers;
    private ImageView backBtnAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        cardViewAddCategory = findViewById(R.id.cardView_add_category);
        cardViewAddCategory.setOnClickListener(v -> {
            displayCategoriesFragment();
        });

        cardViewAddProduct = findViewById(R.id.cardView_add_product);
        cardViewAddProduct.setOnClickListener(v -> {
            displayProductsFragment();
        });

        cardViewAddUsers = findViewById(R.id.cardView_add_user);
        cardViewAddUsers.setOnClickListener(v -> {
            displayUsersFragment();
        });

        backBtnAdmin = findViewById(R.id.back_button_admin_page);
        backBtnAdmin.setOnClickListener(v -> {
            //back to home
            finish();
        });

        cardViewAddFeed = findViewById(R.id.cardView_add_feed);
        cardViewAddFeed.setOnClickListener(v -> {
            displayFeedsFragment();
        });

    }

    private void displayFeedsFragment() {
        ManageFeedsFragment feedsFragment = new ManageFeedsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, feedsFragment);
        fragmentTransaction.commit();
        LinearLayout linearLayout = findViewById(R.id.content_empty_view);
        linearLayout.setVisibility(LinearLayout.GONE);
    }

    private void displayUsersFragment() {
        ManageUsersFragment usersFragment = new ManageUsersFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, usersFragment);
        fragmentTransaction.commit();
        LinearLayout linearLayout = findViewById(R.id.content_empty_view);
        linearLayout.setVisibility(LinearLayout.GONE);
    }

    private void displayCategoriesFragment() {
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, categoriesFragment);
        fragmentTransaction.commit();
        LinearLayout linearLayout = findViewById(R.id.content_empty_view);
        linearLayout.setVisibility(LinearLayout.GONE);

    }

    private void displayProductsFragment() {
        ManageProductFragment productsFragment = new ManageProductFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, productsFragment);
        fragmentTransaction.commit();
        LinearLayout linearLayout = findViewById(R.id.content_empty_view);
        linearLayout.setVisibility(LinearLayout.GONE);
    }


}