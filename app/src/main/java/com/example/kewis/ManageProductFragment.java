package com.example.kewis;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kewis.databases.DatabaseHandler;
import com.example.kewis.databases.DatabaseHelper;
import com.example.kewis.models.Category;
import com.example.kewis.models.Product;
import com.example.kewis.recylerViews.ManageProductAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageProductFragment extends Fragment implements ManageProductAdapter.OnProductClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageProductFragment newInstance(String param1, String param2) {
        ManageProductFragment fragment = new ManageProductFragment();
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
    private ManageProductAdapter manageProductAdapter;
    private List<Product> productList;
    private FloatingActionButton fabAddProduct;
   private List<Category> categoryList;
    private DatabaseHandler db;
    private ImageView productImageView;
    private Bitmap selectedImage;
    private ImageUploadHandler imageUploadHandler;
    private ActivityResultLauncher<Intent> mGetContent;
    Uri selectedImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_product, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_display_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        db = new DatabaseHandler(getActivity(), new DatabaseHelper(getActivity()));
        productList = new ArrayList<>();

        productList=db.getAllProducts();

    manageProductAdapter = new ManageProductAdapter(getContext(),productList,this);
    recyclerView.setAdapter(manageProductAdapter);

    fabAddProduct = view.findViewById(R.id.fab_add_product);
    fabAddProduct.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAddProductDialog();
        }
    });


        mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                selectedImageUri = data.getData();
                                try {
                                    selectedImage = imageUploadHandler.getBitmapFromUri(getActivity(), selectedImageUri);
                                    Log.d(TAG, "selectedImage: " + selectedImage);
                                    productImageView.setImageBitmap(selectedImage);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });



        return view;
    }



        @Override
        public void onDeleteClick(Product product) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete Product");
            builder.setMessage("Are you sure you want to delete this product?");

            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String[] whereArgs = { String.valueOf(product.getId()) };
                    // Assuming you have a method getId in your Product class to get product's id
                    int result = db.delete("products","id = ?", whereArgs); // Replace 'TABLE_PRODUCTS' with your actual product table's name

                    if(result > 0){
                        Toast.makeText(getContext(), "Product deleted successfully", Toast.LENGTH_SHORT).show();
                        // Update the product list
                        productList.remove(product); // Remove the product from the list
                        productList = db.getAllProducts();
                        manageProductAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(getContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }




         private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText editProductName = dialogView.findViewById(R.id.text_add_product_name);
        EditText editProductPrice = dialogView.findViewById(R.id.text_add_product_price);
        EditText editProductQuantity = dialogView.findViewById(R.id.text_add_product_quantity);
        EditText editProductDescription = dialogView.findViewById(R.id.text_add_product_description);
        Spinner spinnerProductCategory = dialogView.findViewById(R.id.spinner_add_product_category);
        Button buttonAddProductImage = dialogView.findViewById(R.id.button_add_uploadProductPicture);
        productImageView = dialogView.findViewById(R.id.imageView_add_productPicture);


             imageUploadHandler = new ImageUploadHandler(mGetContent);
        buttonAddProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the add product image click event
                imageUploadHandler.openImagePicker();
            }
        });


        // Fetch categories from the database
        List<Category> categories = db.getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        // Create an ArrayAdapter and set it to the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductCategory.setAdapter(adapter);

        if(categories.size() == 0) {
            Toast.makeText(getContext(), "Please add a category first", Toast.LENGTH_SHORT).show();
            return;
        } else if (categoryNames.size() > 0) {
            spinnerProductCategory.setSelection(0);
        }

             builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     String productName = editProductName.getText().toString().trim();
                     String productPriceString = editProductPrice.getText().toString().trim();
                     String productQuantityString = editProductQuantity.getText().toString().trim();
                     String productDescription = editProductDescription.getText().toString().trim();
                     String productCategory = spinnerProductCategory.getSelectedItem().toString().trim();

                     if(productName.isEmpty() || productPriceString.isEmpty() || productQuantityString.isEmpty()
                             || productDescription.isEmpty() || productCategory.isEmpty() || selectedImageUri == null){
                         Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                         return;
                     }

                     double productPrice = Double.parseDouble(productPriceString);
                     int productQuantity = Integer.parseInt(productQuantityString);

                     String fileExtension = imageUploadHandler.getFileExtension(getActivity(), selectedImageUri );
                     String fileName = "profile_" + productName + "." + fileExtension;
                     Uri imagePath = imageUploadHandler.saveImageToInternalStorage(getActivity(), selectedImage, fileName);
                     String productImage = imagePath.toString();

                     //find the category id from its name
                     int categoryId = -1;
                     for (Category category : categories) {
                         if (category.getName().equals(productCategory)) {
                             categoryId = category.getId();
                             break;
                         }
                     }

                     if(categoryId == -1){
                         Toast.makeText(getContext(), "no categories found", Toast.LENGTH_SHORT).show();
                         return;
                     }

                     saveProduct(productName, productPrice, productQuantity, productDescription, productImage, categoryId);
                 }
             });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        }
        @Override
        public void onEditClick(Product product) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
            builder.setView(dialogView);

            EditText editProductName = dialogView.findViewById(R.id.text_add_product_name);
            EditText editProductPrice = dialogView.findViewById(R.id.text_add_product_price);
            EditText editProductQuantity = dialogView.findViewById(R.id.text_add_product_quantity);
            EditText editProductDescription = dialogView.findViewById(R.id.text_add_product_description);
            Spinner spinnerProductCategory = dialogView.findViewById(R.id.spinner_add_product_category);
            Button buttonAddProductImage = dialogView.findViewById(R.id.button_add_uploadProductPicture);
            productImageView = dialogView.findViewById(R.id.imageView_add_productPicture);

            imageUploadHandler = new ImageUploadHandler(mGetContent);
            buttonAddProductImage.setText("Change Image");
            buttonAddProductImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the add product image click event
                    imageUploadHandler.openImagePicker();
                }
            });

            editProductName.setText(product.getName());
            editProductPrice.setText(String.valueOf(product.getPrice()));
            editProductQuantity.setText(String.valueOf(product.getQuantity()));
            editProductDescription.setText(product.getDescription());
            String productImage = product.getImageUrl();

            Glide.with(getContext())
                    .load(productImage)
                    .placeholder(R.drawable.no_pictures)
                    .error(R.drawable.baseline_hide_image_24)
                    .into(productImageView);

            // Fetch categories from the database
            List<Category> categories = db.getAllCategories();
            List<String> categoryNames = new ArrayList<>();
            for (Category category : categories) {
                categoryNames.add(category.getName());
            }

            // Create an ArrayAdapter and set it to the spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProductCategory.setAdapter(adapter);

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String productName = editProductName.getText().toString().trim();
                    double productPrice = Double.parseDouble(editProductPrice.getText().toString().trim());
                    int productQuantity = Integer.parseInt(editProductQuantity.getText().toString().trim());
                    String productDescription = editProductDescription.getText().toString().trim();
                    String fileName = "profile_" + productName + "."+ imageUploadHandler.getFileExtension(getActivity(), selectedImageUri);
                    Uri imagePath = imageUploadHandler.saveImageToInternalStorage(getActivity(), selectedImage, fileName);
                    String productImage = imagePath.toString();
                    int id = product.getId();
                    String productCategory = spinnerProductCategory.getSelectedItem().toString().trim();

                    //find the category id from its name
                    int categoryId = -1;
                    for (Category category : categories) {
                        if (category.getName().equals(productCategory)) {
                            categoryId = category.getId();
                            break;
                        }
                    }

                    if(categoryId == -1){
                        Toast.makeText(getContext(), "no categories found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    updateProduct(id, productName, productPrice, productQuantity, productDescription, productImage, categoryId);

                }
            });
            builder.setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();

        }

    public int updateProduct(int id, String productName, double productPrice, int productQuantity, String productDescription, String productImage, int categoryId) {
        Product product = new Product(id, productName, productPrice, productQuantity, productDescription, productImage, categoryId);
        int result = db.updateProduct(product);
        if (result == 1) {
           // Toast.makeText(getContext(), "Product updated successfully", Toast.LENGTH_SHORT).show();
            Snackbar.make(getView(), "Product updated successfully", Snackbar.LENGTH_SHORT).show();
            productList= db.getAllProducts();
            manageProductAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "Product not updated", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private void saveProduct(String productName, double productPrice, int productQuantity, String productDescription, String productImage, int categoryId) {
        Product product = new Product(productName, productPrice, productQuantity, productDescription, productImage, categoryId);
        long result = db.addProduct(product);
        if (result == -1) {
            Toast.makeText(getContext(), "Product not saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Product saved successfully", Toast.LENGTH_SHORT).show();
        }
        productList= db.getAllProducts();
        manageProductAdapter.notifyDataSetChanged();
    }


}
