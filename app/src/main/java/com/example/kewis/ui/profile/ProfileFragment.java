package com.example.kewis.ui.profile;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kewis.ImageUploadHandler;
import com.example.kewis.R;
import com.example.kewis.SessionManager;
import com.example.kewis.databases.DatabaseHandler;
import com.example.kewis.databases.DatabaseHelper;
import com.example.kewis.databinding.FragmentProfileBinding;

import java.io.IOException;


public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private FragmentProfileBinding binding;
    private SessionManager sessionManager;
    TextView txtFirstName,txtLastName,txtEmail,txtPhone,txtAddress,txtUsername,txtMemorableWords,txtHobbies,txtLastLogin;
    DatabaseHandler db;
    private Bitmap selectedImage;
    private ImageUploadHandler imageUploadHandler;
    private ActivityResultLauncher<Intent> mGetContent;
    Uri selectedImageUri;
    String username,memorableWords,hobbies,profilePicture,firstName,lastName,email,phoneNumber,address,lastLogin;
ImageView imgProfile;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sessionManager = new SessionManager(getActivity());
        txtFirstName = binding.profileFirstName;
        txtLastName = binding.profileLastName;
        txtEmail = binding.profileEmail;
        txtPhone = binding.profilePhoneNumber;
        txtAddress = binding.profileAddress;
        txtUsername = binding.profileUsernames;
        txtMemorableWords = binding.profileMemorableWords;
        txtHobbies = binding.profileHobbies;
        txtLastLogin = binding.profileLastLogin;
        imgProfile = binding.profileImage;

        ImageView btnBackToDashboard = binding.backButtonUserPage;
        btnBackToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

db = new DatabaseHandler(getActivity(), new DatabaseHelper(getActivity()));

        int userId = sessionManager.getUserId();
String[] columns = {"first_name","last_name","email","phone_number","address","username","memorable_word","hobbies","profile_picture","last_login"};
        Cursor cursor = db.read("users",columns,"id=?",new String[]{String.valueOf(userId)},null,null,null,null);

        if(cursor.moveToFirst()){
            firstName=cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            txtFirstName.setText(firstName);
            lastName=cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            txtLastName.setText(lastName);
            email=cursor.getString(cursor.getColumnIndexOrThrow("email"));
            txtEmail.setText(email);
            phoneNumber=cursor.getString(cursor.getColumnIndexOrThrow("phone_number"));
            txtPhone.setText(phoneNumber);
            address=cursor.getString(cursor.getColumnIndexOrThrow("address"));
            txtAddress.setText(address);
            username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            txtUsername.setText(username);
            memorableWords=cursor.getString(cursor.getColumnIndexOrThrow("memorable_word"));
            txtMemorableWords.setText(memorableWords);
            hobbies=cursor.getString(cursor.getColumnIndexOrThrow("hobbies"));
            txtHobbies.setText(hobbies);
            lastLogin = cursor.getString(cursor.getColumnIndexOrThrow("last_login"));
            txtLastLogin.setText(lastLogin);
            String profilePicture = cursor.getString(cursor.getColumnIndexOrThrow("profile_picture"));

            Glide.with(getActivity()).load(profilePicture).placeholder(R.drawable.no_pictures).error(R.drawable.baseline_person_outline_24).into(imgProfile);
        }

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
                                    imgProfile.setImageBitmap(selectedImage);
                                    uploadImg();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });


        imageUploadHandler = new ImageUploadHandler(mGetContent);

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUploadHandler.openImagePicker();
            }
        });

        ImageView btnEditProfile = binding.editUserProfile;
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserdetails();
            }
        });
        return root;
    }

    private void editUserdetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_user_profile, null);
        builder.setView(dialogView);

        TextView txtFirstName = dialogView.findViewById(R.id.text_add_userFirstName);
        TextView txtLastName = dialogView.findViewById(R.id.text_add_userLastName);
        TextView txtEmail = dialogView.findViewById(R.id.text_add_userEmail);
        TextView txtPhone = dialogView.findViewById(R.id.text_add_userPhoneNumber);
        TextView txtAddress = dialogView.findViewById(R.id.text_add_userAddress);
        TextView txtMemorableWords = dialogView.findViewById(R.id.text_add_userMemorableName);
        TextView txtHobbies = dialogView.findViewById(R.id.text_add_userHobbies);
        TextView txtPassword = dialogView.findViewById(R.id.text_add_userPassword);

        //get user details

        int userId = sessionManager.getUserId();
        txtFirstName.setText(firstName);
        txtLastName.setText(lastName);
        txtEmail.setText(email);
        txtPhone.setText(phoneNumber);
        txtAddress.setText(address);
        txtMemorableWords.setText(memorableWords);
        txtHobbies.setText(hobbies);
        txtPassword.setText("**********");

        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String firstName = txtFirstName.getText().toString().trim();
                String lastName = txtLastName.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String phone = txtPhone.getText().toString().trim();
                String address = txtAddress.getText().toString().trim();
                String memorableWords = txtMemorableWords.getText().toString().trim();
                String hobbies = txtHobbies.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                ContentValues cv = new ContentValues();
                cv.put("first_name", firstName);
                cv.put("last_name", lastName);
                cv.put("email", email);
                cv.put("phone_number", phone);
                cv.put("address", address);
                cv.put("memorable_word", memorableWords);
                cv.put("hobbies", hobbies);
                cv.put("password", password);

                String[] whereArgs = new String[]{String.valueOf(userId)};

                int rowsUpdated = db.update("users", cv, "id=?", whereArgs);


                if (rowsUpdated > 0) {
                    Toast.makeText(getActivity(), "User details updated successfully", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getActivity(), "User details not updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void uploadImg() {


        String fileExtension = imageUploadHandler.getFileExtension(getActivity(), selectedImageUri );
        String fileName = "profile_" + username + "." + fileExtension;
        Uri imagePath = imageUploadHandler.saveImageToInternalStorage(getActivity(), selectedImage, fileName);
        String profileImage = imagePath.toString();

        ContentValues cv = new ContentValues();
        cv.put("profile_picture", profileImage);
        String[] whereArgs = new String[]{username};

        int rowsUpdated = db.update("users", cv, "username=?", whereArgs);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}