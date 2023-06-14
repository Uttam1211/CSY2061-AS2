package com.example.kewis.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kewis.MainActivity;
import com.example.kewis.R;
import com.example.kewis.SessionManager;
import com.example.kewis.databases.DatabaseHandler;
import com.example.kewis.databases.DatabaseHelper;
import com.example.kewis.models.User;
import com.example.kewis.sign_up.SignUpActivity;
import com.example.kewis.ui.dashboard.DashboardFragment;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private EditText txtEmail, txtPassword;
    private TextView txtVContinueAsGuest, txtVSignUpPrompt, txtForgetPassword;
    private Button btnLogin;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         sessionManager = new SessionManager(this);
         String email = sessionManager.getUserEmail();

         if(!email.isEmpty()){
             Intent myIntentToDashboard = new Intent(this, MainActivity.class);
             startActivity(myIntentToDashboard);
             finish();
         }

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnlogin);
        txtForgetPassword = findViewById(R.id.txtVForgetPass);
        txtVSignUpPrompt = findViewById(R.id.txtVSignUpPrompt);
        txtVContinueAsGuest = findViewById(R.id.txtVContinueAsGuest);

        String preEmail = getIntent().getStringExtra("email");
        txtEmail.setText(preEmail);

        dbHelper = new DatabaseHelper(this);

        DatabaseHandler db = new DatabaseHandler(this, DatabaseHelper.getInstance(this));
        db.createDefaultAdmin();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
          }
        });

        txtVSignUpPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntentToSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(myIntentToSignUp);
            }
        });
       txtVContinueAsGuest.setOnClickListener(v -> {
            Intent myIntentToDashboard = new Intent(this, MainActivity.class);
            startActivity(myIntentToDashboard);
        });
    }

    public void loginUser(){
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        DatabaseHandler db = new DatabaseHandler(this, DatabaseHelper.getInstance(this));
        User user = db.authenticateUser(email, password);

        if (user != null) {
            // save user details in session
            SessionManager sessionManager = new SessionManager(this);
            sessionManager.setUserEmail(user.getEmail());
            sessionManager.setUserPassword(user.getPassword());
            sessionManager.setUserType(user.getUsertype());
            sessionManager.setUserId(user.getId());
            sessionManager.setIsLoggedIn(true);
            Intent myIntentToDashboard = new Intent(this, MainActivity.class);
            myIntentToDashboard.putExtra("email",email);
            startActivity(myIntentToDashboard);
            finish();


        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}

