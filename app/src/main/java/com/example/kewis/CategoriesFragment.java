package com.example.kewis;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kewis.databases.DatabaseHandler;
import com.example.kewis.databases.DatabaseHelper;
import com.example.kewis.models.Category;
import com.example.kewis.recylerViews.CategoriesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends Fragment implements CategoriesAdapter.OnCategoryClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView recyclerView;
    private CategoriesAdapter categoriesAdapter;
    private List<Category> categories;
    private FloatingActionButton fabAddCategory;
    private DatabaseHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        db = new DatabaseHandler(getActivity(), new DatabaseHelper(getActivity()));
        categories = new ArrayList<>();
        categories = db.getAllCategories();

        categoriesAdapter = new CategoriesAdapter(getContext(), categories, this);
        recyclerView.setAdapter(categoriesAdapter);

        fabAddCategory = view.findViewById(R.id.fab_add_category);
        fabAddCategory.setOnClickListener(v -> showAddCategoryDialog());

        return view;
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_category, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.text_add_category_name);
        EditText editTextDescription = dialogView.findViewById(R.id.text_add_category_description);
        EditText editTextImageUrl = dialogView.findViewById(R.id.text_add_category_imageUrl);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryName = editTextName.getText().toString();
                String categoryDescription = editTextDescription.getText().toString();
                String imageUrl = editTextImageUrl.getText().toString();
                saveCategory(categoryName, categoryDescription, imageUrl);
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onEditClick(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_category, null);
        builder.setView(dialogView);
        TextView textViewTitle = dialogView.findViewById(R.id.dialog_add_category_title);
        textViewTitle.setText("Edit Category");
        final EditText editTextName = dialogView.findViewById(R.id.text_add_category_name);
        final EditText editTextDescription = dialogView.findViewById(R.id.text_add_category_description);
        final EditText editTextImageUrl = dialogView.findViewById(R.id.text_add_category_imageUrl);

        // Prefill the dialog with the current data
        editTextName.setText(category.getName());
        editTextDescription.setText(category.getDescription());
        editTextImageUrl.setText(category.getImageUrl());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryName = editTextName.getText().toString();
                String categoryDescription = editTextDescription.getText().toString();
                String imageUrl = editTextImageUrl.getText().toString();
                int id = category.getId();
                Log.d("cate ID","Category ID: " + id);
                updateCategory(id, categoryName, categoryDescription, imageUrl);
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDeleteClick(Category category) {
        // Handle the delete category click event
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Category");
        builder.setMessage("Are you sure you want to delete this category?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] whereArgs = { String.valueOf(category.getId()) };
                // Assuming you have a method getId in your Product class to get product's id
                int result = db.delete("categories","id = ?", whereArgs); // Replace 'TABLE_PRODUCTS' with your actual product table's name

                if(result > 0){
                    Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    categories.remove(category);
                    categoriesAdapter.notifyDataSetChanged();

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

    private void saveCategory(String categoryName, String categoryDescription, String imageUrl) {
        Category category = new Category(categoryName, categoryDescription, imageUrl);
        long ids = db.addCategory(category);

        if (ids == -1) {
            Toast.makeText(getActivity(), "Failed to add category", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Category added successfully", Toast.LENGTH_SHORT).show();
            categories = db.getAllCategories();
            categoriesAdapter.notifyDataSetChanged();
        }
    }

    public int updateCategory(int id, String name, String description, String imageUrl) {
        Category category = new Category(id, name, description, imageUrl);
        int updateStatus = db.updateCategory(category);
        if (updateStatus == 1) {
            Toast.makeText(getActivity(), "Category updated successfully", Toast.LENGTH_SHORT).show();
            categories = db.getAllCategories();
            categoriesAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "Failed to update category", Toast.LENGTH_SHORT).show();
        }

        return updateStatus;
    }

}