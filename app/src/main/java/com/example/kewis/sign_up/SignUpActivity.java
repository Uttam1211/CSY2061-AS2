package com.example.kewis.sign_up;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kewis.R;
import com.example.kewis.databases.DatabaseHandler;
import com.example.kewis.databases.DatabaseHelper;
import com.example.kewis.login.LoginActivity;

import java.util.Random;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    EditText editFirstName, editLastName, editEmail, editPassword, editConfirmPassword, editMemorableWord;
    Button btnSignUp;
    TextView textViewAlreadyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editFirstName = findViewById(R.id.edit_first_name);
        editLastName = findViewById(R.id.edit_last_name);
        editEmail = findViewById(R.id.txtEmail_signup);
        editPassword = findViewById(R.id.txtPasswordSignup);
        editConfirmPassword = findViewById(R.id.txtRepeat_PasswordSignup);
        editMemorableWord = findViewById(R.id.txtMemorable_Word);
        btnSignUp = findViewById(R.id.btnSignupClick);
        textViewAlreadyAccount = findViewById(R.id.textView_already_account_from_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        textViewAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addUser() {
        String firstName = editFirstName.getText().toString();
        String lastName = editLastName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();
        String memorableWord = editMemorableWord.getText().toString();
        DatabaseHandler dbHandler = new DatabaseHandler(this,new DatabaseHelper(this));
        String username = generateUsername(firstName, lastName, dbHandler);



        // Confirm that the passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Confirm that none of the fields are empty
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || memorableWord.isEmpty() || username.isEmpty()) {
            Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a ContentValues object to hold the new user data
        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("email", email);
        // TODO: Password should be hashed and salted before storing
        values.put("password", password);
        values.put("memorable_word", memorableWord);
        values.put("username", username);
        values.put("user_type", "user");

        // Insert the new user data into the database
        long result = dbHandler.create("users", values);

        // Check the result of the insertion
        if (result == -1) {
            Toast.makeText(getApplicationContext(), "Failed to register. Try again.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Registration successful!, login now ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }

    }

    public static String generateUsername(String firstName, String lastName, DatabaseHandler dbHandler) {
        String namePart = (firstName.substring(0, Math.min(firstName.length(), 2)) +
                lastName.substring(0, Math.min(lastName.length(), 2))).toLowerCase();

        String username = "";
        boolean isUnique = false;

        while (!isUnique) {
            int uniqueNumber = new Random().nextInt(9000) + 1000; // This will generate a unique number between 1000 and 9999
            username = namePart + uniqueNumber;

            // Check if the username is unique
            Cursor cursor = dbHandler.read("users", new String[] {"username"}, "username=?", new String[] {username}, null, null, null, null);
            if (cursor.getCount() == 0) { // If the cursor count is 0, it means there are no entries with that username
                isUnique = true;
            }
            cursor.close();
        }

        return username;
    }



}