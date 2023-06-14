package com.example.kewis.models;

/*
 String CREATE_WISHLIST_TABLE = "CREATE TABLE wishlist (" +
         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
         "user_id INTEGER NOT NULL, " + // Reference to the user who added the product to their wishlist
         "product_id INTEGER NOT NULL, " + // Reference to the added product
         "date_added DATETIME DEFAULT CURRENT_TIMESTAMP, " + // Automatically set the creation date
         "FOREIGN KEY(user_id) REFERENCES user(id), " + // Foreign key constraint for referencing the user table
         "FOREIGN KEY(product_id) REFERENCES products(id))"; // Foreign key constraint for referencing the products table
*/


public class WishList {
    private int id;
    private int userId;
    private int productId;
    private String dateAdded;
    private String name;
    private int price;
    private String image;

    public WishList(int user_id, int product_id, String product_name, int product_price, String product_image, String date_added) {
        this.userId = user_id;
        this.productId = product_id;
        this.name = product_name;
        this.price = product_price;
        this.image = product_image;
        this.dateAdded = date_added;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
