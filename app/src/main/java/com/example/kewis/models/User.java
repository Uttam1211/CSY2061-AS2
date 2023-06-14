package com.example.kewis.models;

public class User {
    private int id;
    private String email;
    private String password;
    private String usertype;
    private String first_name;
    private String last_name;
    private String username;
    private String memorable_word;
    private String date_created;
    private String last_login;
    private String phone_number;
    private String address;
    private String profileImage;


    public User(int id, String email, String password, String usertype) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.usertype = usertype;
    }

    public User(String firstName, String lastName, String email, String password, String phone, String memorableWord, String address, String userType) {
        this.first_name = firstName;
        this.last_name = lastName;
        this.email = email;
        this.password = password;
        this.phone_number = phone;
        this.memorable_word = memorableWord;
        this.address = address;
        this.usertype = userType;
    }

    public User(int id, String firstName, String lastName, String email, String password, String phone, String memorableWord, String address, String userType, String username, String profileImage) {
        this.id = id;
        this.first_name = firstName;
        this.last_name = lastName;
        this.email = email;
        this.password = password;
        this.phone_number = phone;
        this.memorable_word = memorableWord;
        this.address = address;
        this.usertype = userType;
        this.username = username;
        this.profileImage = profileImage;
    }

    public User(String firstName, String lastName, String email, String password, String phone, String memorableWord, String address, String userType, String username, String profileImage) {
        this.first_name = firstName;
        this.last_name = lastName;
        this.email = email;
        this.password = password;
        this.phone_number = phone;
        this.memorable_word = memorableWord;
        this.address = address;
        this.usertype = userType;
        this.username = username;
        this.profileImage = profileImage;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMemorable_word() {
        return memorable_word;
    }

    public void setMemorable_word(String memorable_word) {
        this.memorable_word = memorable_word;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfilePicture() {
        return profileImage;
    }

    public void setProfilePicture(String profilePicture) {
        this.profileImage = profilePicture;
    }


}
