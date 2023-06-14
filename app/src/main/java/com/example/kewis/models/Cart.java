package com.example.kewis.models;

/*
  String CREATE_CART_TABLE = "CREATE TABLE cart (" +
          "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
          "user_id INTEGER NOT NULL, " + // Reference to the user who added the product to their cart
          "product_id INTEGER NOT NULL, " + // Reference to the added product
          "quantity INTEGER NOT NULL DEFAULT 1, " + // Quantity of the product in the cart
          "date_added DATETIME DEFAULT CURRENT_TIMESTAMP, " + // Automatically set the creation date
          "FOREIGN KEY(user_id) REFERENCES user(id), " + // Foreign key constraint for referencing the user table
          "FOREIGN KEY(product_id) REFERENCES products(id))"; // Foreign key constraint for referencing the products table
*/


public class Cart {
    private int id;
    private int userId;
    private int productId;
    private int quantity;
    private String dateAdded;
    private String name;
    private int price;
    private String image;


    public Cart(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Cart(int id, int userId, int productId, int quantity, String dateAdded) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.dateAdded = dateAdded;
    }

    public Cart(int user_id, int product_id, String product_name, int product_price, String product_image, String dateAdded) {
        this.userId = user_id;
        this.productId = product_id;
        this.name = product_name;
        this.price = product_price;
        this.image = product_image;
        this.dateAdded = dateAdded;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
public void addName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
public void addPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
public void addImage(String image) {
        this.image = image;
    }
    public String getImage() {
        return image;
    }

}
