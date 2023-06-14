package com.example.kewis.ui.wishlist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kewis.SessionManager;
import com.example.kewis.databases.DatabaseHandler;
import com.example.kewis.databases.DatabaseHelper;
import com.example.kewis.databinding.FragmentWishlistBinding;
import com.example.kewis.models.WishList;
import com.example.kewis.recylerViews.WishListAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment implements WishListAdapter.OnWishListClickListener{

    private FragmentWishlistBinding binding;
    SessionManager sessionManager;
    List<WishList> wishlists;
    DatabaseHandler db;
    RecyclerView recyclerView;
    WishListAdapter wishListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WishlistViewModel notificationsViewModel =
                new ViewModelProvider(this).get(WishlistViewModel.class);

        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


recyclerView = binding.wishListRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        sessionManager = new SessionManager(getContext());
        db = new DatabaseHandler(getContext(),new DatabaseHelper(getContext()));
        wishlists = new ArrayList<>();

        loadWishlistItems(sessionManager.getUserId());

        return root;
    }

    private void loadWishlistItems(int userId) {
        if (db.isInWishlistByUser(userId)) {
            wishlists = db.getWishlist(userId);
            WishListAdapter wishlistAdapter = new WishListAdapter(getContext(), wishlists, this);
            recyclerView.setAdapter(wishlistAdapter);
            binding.wishListRecyclerView.setVisibility(View.VISIBLE);
            binding.cartEmpty.setVisibility(View.GONE);
        } else {
            binding.wishListRecyclerView.setVisibility(View.GONE);
            binding.cartEmpty.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDeleteClick(WishList wishList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Item");
        builder.setMessage("Are you sure you want to delete this item?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] args = {String.valueOf(wishList.getId())};
                int result = db.delete("wishlist", "id=?", args);
                if(result >0){
                    Snackbar.make(getView(), "Item deleted", Snackbar.LENGTH_SHORT).show();
                    wishlists.remove(wishList);


                }else{
                    Snackbar.make(getView(), "Failed to delete", Snackbar.LENGTH_SHORT).show();

                }
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}