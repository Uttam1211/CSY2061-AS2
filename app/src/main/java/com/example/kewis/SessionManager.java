package com.example.kewis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.kewis.login.LoginActivity;


public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private static final String KEY_USER_TYPE = "userType";
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUserEmail(String email) {
        editor.putString("USER_EMAIL", email).apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString("USER_EMAIL", "");
    }

    public void setUserPassword(String password) {
        editor.putString("USER_PASSWORD", password).apply();
    }

    public String getUserPassword() {
        return sharedPreferences.getString("USER_PASSWORD", "");
    }

    public void setUserType(String userType) {
        editor.putString("USER_TYPE", userType).apply();
    }

    public String getUserType() {
        return sharedPreferences.getString("USER_TYPE", "");
    }

    public void setUserId(int id) {
        editor.putInt("USER_ID", id).apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt("USER_ID", 0);
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        editor.putBoolean("IS_LOGGED_IN", isLoggedIn).apply();
    }

    public boolean getIsLoggedIn() {
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false);
    }

    public void clear() {
        editor.clear();
        setIsLoggedIn(false);
        editor.apply();
    }
}
