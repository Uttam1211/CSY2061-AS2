package com.example.kewis.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kewis.AdminActivity;
import com.example.kewis.R;
import com.example.kewis.SessionManager;
import com.example.kewis.databases.DatabaseHandler;
import com.example.kewis.databases.DatabaseHelper;
import com.example.kewis.databinding.FragmentDashboardBinding;
import com.example.kewis.login.LoginActivity;
import com.example.kewis.models.Category;
import com.example.kewis.models.InnerData;
import com.example.kewis.models.Product;
import com.example.kewis.recylerViews.DashboardOuterRecylerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {
    DrawerLayout drawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FragmentDashboardBinding binding;
    SessionManager sessionManager;
    DatabaseHandler db;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        db = new DatabaseHandler(getActivity(),new DatabaseHelper(getActivity()));
        db.createDefaultCategories();


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        generateSampleData();
        sessionManager = new SessionManager(getActivity());
          drawerLayout=binding.drawerLayout;
        FloatingActionButton leftnav = binding.leftNav;
        FloatingActionButton btnCart=binding.topRightCartIcon;
        leftnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_dashboard_to_navigation_cart);

            }
        });

        Menu menu = binding.navView.getMenu();
        MenuItem nav_logout = menu.findItem(R.id.navigation_logout);
        nav_logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sessionManager.clear();
                   Intent intent = new Intent(getActivity(), LoginActivity.class);
                     startActivity(intent);
                     getActivity().finish();
                return false;
            }
        });
        if(sessionManager.getIsLoggedIn()){
            nav_logout.setVisible(true);
        }
        else{
            nav_logout.setVisible(false);
        }
        MenuItem nav_login = menu.findItem(R.id.admin_login);
        if(sessionManager.getUserType().equals("admin")){
            nav_login.setVisible(true);
        }
        else{
            nav_login.setVisible(false);
        }




        NavigationView navigationView = binding.navView;
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem menuItem){
                int id = menuItem.getItemId();
                if(id==R.id.admin_login){
                    Intent intent = new Intent(getActivity(), AdminActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    menuItem.setChecked(false);
                    return true;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;

            }
        });


        swipeRefreshLayout = binding.swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the data here
                generateSampleData();
                // End the refreshing animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });
       db = new DatabaseHandler(getActivity(),new DatabaseHelper(getActivity()));
        return root;
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void generateSampleData() {


        List<String> titles = new ArrayList<>();
        List<List<InnerData>> innerData = new ArrayList<>();

        titles.add("Shop by Category");
        titles.add("All Products");

        //instantiate DatabaseHandler class
        //get all categories
        List<Category> categories = db.readAllCategories();
        List<Product> products = db.getAllProducts();
        //convert list of categories into a list of innerData

        List<InnerData> categoryItem = new ArrayList<>();
        for(Category category:categories){
            categoryItem.add(new InnerData(category.getName(),category.getImageUrl(),0,category.getId()));
        }

        innerData.add(categoryItem);

        List<InnerData> productItem = new ArrayList<>();
        for(Product product:products){
            productItem.add(new InnerData(product.getName(),product.getImageUrl(),product.getPrice(), product.getId()));

        }
          innerData.add(productItem);

        DashboardOuterRecylerView dashboardOuterRecylerView = new DashboardOuterRecylerView(titles, innerData,db);
        RecyclerView outerRecyclerView = binding.recyclerViewOuter;
        outerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        outerRecyclerView.setAdapter(dashboardOuterRecylerView);
    }

}