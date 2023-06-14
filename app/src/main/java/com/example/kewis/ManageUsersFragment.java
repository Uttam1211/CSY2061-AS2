package com.example.kewis;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kewis.databases.DatabaseHandler;
import com.example.kewis.databases.DatabaseHelper;
import com.example.kewis.models.User;
import com.example.kewis.recylerViews.ManageUsersAdapter;
import com.example.kewis.sign_up.SignUpActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageUsersFragment extends Fragment implements ManageUsersAdapter.OnUserClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageUsersFragment newInstance(String param1, String param2) {
        ManageUsersFragment fragment = new ManageUsersFragment();
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
    private ManageUsersAdapter manageUsersAdapter;
    private List<User> usersList;
    private FloatingActionButton fabAddUser;
    private DatabaseHandler db;
    private ImageView imageView;
    private Bitmap selectedImage;
    private ImageUploadHandler imageUploadHandler;
    private ActivityResultLauncher<Intent> mGetContent;
    Uri selectedImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_users, container, false);
         recyclerView = view.findViewById(R.id.recyclerView_display_users);
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         recyclerView.setHasFixedSize(true);

         db = new DatabaseHandler(getActivity(), new DatabaseHelper(getActivity()));

         usersList = new ArrayList<>();
         usersList = db.getAllUsers();

            manageUsersAdapter = new ManageUsersAdapter(getContext(), usersList, this);
            recyclerView.setAdapter(manageUsersAdapter);

            fabAddUser = view.findViewById(R.id.fab_add_user);
            fabAddUser.setOnClickListener(v -> {
                showAddUserDialog();
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
                                    imageView.setImageBitmap(selectedImage);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });



    return view;
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_users, null);
        builder.setView(dialogView);

       EditText editTextFirstName = dialogView.findViewById(R.id.text_add_userFirstName);
       EditText editTextLastName = dialogView.findViewById(R.id.text_add_userLastName);
       EditText editTextEmail = dialogView.findViewById(R.id.text_add_userEmail);
       EditText editTextUsername = dialogView.findViewById(R.id.text_add_userUsername);//set visibility to gone
       EditText editTextPassword = dialogView.findViewById(R.id.text_add_userPassword);
       EditText editTextPhone = dialogView.findViewById(R.id.text_add_userPhoneNumber);
       EditText editTextMemorableWord = dialogView.findViewById(R.id.text_add_userMemorableName);
       EditText editTextAddress = dialogView.findViewById(R.id.text_add_userAddress);
       RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup_userType);
       Button buttonUploadImage = dialogView.findViewById(R.id.button_add_userProfile);
       imageView = dialogView.findViewById(R.id.imageView_add_userProfile);

        imageUploadHandler = new ImageUploadHandler(mGetContent);
    buttonUploadImage.setOnClickListener(v -> {
    imageUploadHandler.openImagePicker();
    });

       builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String memorableWord = editTextMemorableWord.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
               String username = SignUpActivity.generateUsername(firstName, lastName, db);
                String userType = ((RadioButton)dialogView.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();

                //get file extension
               String fileExtension = imageUploadHandler.getFileExtension(getActivity(), selectedImageUri );
               String fileName = "profile_" + username + "." + fileExtension;
               Uri imagePath = imageUploadHandler.saveImageToInternalStorage(getActivity(), selectedImage, fileName);
                String profileImage = imagePath.toString();
               Log.d(TAG, "byte profile image "+ profileImage);

                User user = new User(firstName, lastName, email, password, phone, memorableWord, address, userType, username, profileImage);
                long id = db.addUser(user);
                if(id != -1){
                    Toast.makeText(getActivity(), "user added successfully", Toast.LENGTH_SHORT).show();
                    selectedImage= null;
                }else{
                    Toast.makeText(getActivity(), "failed to add user", Toast.LENGTH_SHORT).show();
                }
           }
       });

       builder.setNegativeButton("Cancel" ,null);

         AlertDialog alertDialog = builder.create();
            alertDialog.show();



    }

    @Override
    public void onEditClick(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_users, null);
        builder.setView(dialogView);

        TextView textViewTitle = dialogView.findViewById(R.id.dialog_add_product_title);
        textViewTitle.setText("Edit User");

        EditText editTextFirstName = dialogView.findViewById(R.id.text_add_userFirstName);
        EditText editTextLastName = dialogView.findViewById(R.id.text_add_userLastName);
        EditText editTextEmail = dialogView.findViewById(R.id.text_add_userEmail);
        EditText editTextPassword = dialogView.findViewById(R.id.text_add_userPassword);
        EditText editTextPhone = dialogView.findViewById(R.id.text_add_userPhoneNumber);
        EditText editTextMemorableWord = dialogView.findViewById(R.id.text_add_userMemorableName);
        EditText editTextAddress = dialogView.findViewById(R.id.text_add_userAddress);
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup_userType);
        Button buttonUploadImage = dialogView.findViewById(R.id.button_add_userProfile);
        imageView = dialogView.findViewById(R.id.imageView_add_userProfile);

        imageUploadHandler = new ImageUploadHandler(mGetContent);
        buttonUploadImage.setOnClickListener(v -> {
            imageUploadHandler.openImagePicker();
        });



         //set data to edit text
        editTextFirstName.setText(user.getFirst_name());
        editTextLastName.setText(user.getLast_name());
        editTextEmail.setText(user.getEmail());
        editTextPassword.setText(user.getPassword());
        editTextPhone.setText(user.getPhone_number());
        editTextMemorableWord.setText(user.getMemorable_word());
        editTextAddress.setText(user.getAddress());
        buttonUploadImage.setText("Change Profile Image");
        Toast.makeText(getActivity(), "profile image "+ user.getProfilePicture(), Toast.LENGTH_SHORT).show();

        String profilePictureUriString = user.getProfilePicture();

        Log.d("ProfilePicture", "Uri: " + profilePictureUriString);
        Glide.with(getContext())
                .load(profilePictureUriString)
                .placeholder(R.drawable.no_pictures)
                .error(R.drawable.baseline_hide_image_24)
                .into(imageView);


        if(user.getUsertype().equals("Admin")){
            radioGroup.check(R.id.radioButtonAdmin);
        }else{
            radioGroup.check(R.id.radioButtonUser);
        }

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String memorableWord = editTextMemorableWord.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String userType = ((RadioButton)dialogView.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();

                int id = user.getId();
                String username = user.getUsername();

                String fileName = "profile_" + username + "."+ imageUploadHandler.getFileExtension(getActivity(), selectedImageUri);
                Uri imagePath = imageUploadHandler.saveImageToInternalStorage(getActivity(), selectedImage, fileName);
                String profileImage = imagePath.toString();

                User user = new User(id,firstName, lastName, email, password, phone, memorableWord, address, userType, username, profileImage);
                long ids = db.updateUser(user);
                if(ids == -1){
                    Toast.makeText(getActivity(), "Failed to update user", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "User updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onDeleteClick(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete User");
        builder.setMessage("Are you sure you want to delete this user?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] whereArgs = {String.valueOf(user.getId())};
                int count = db.delete("users", "id = ?", whereArgs);

                if (count > 0){
                    Toast.makeText(getActivity(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                usersList.remove(user);}
                else
                    Toast.makeText(getActivity(), "Failed to delete user", Toast.LENGTH_SHORT).show();
            }
            });

        builder.setNegativeButton("No", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        }
}
