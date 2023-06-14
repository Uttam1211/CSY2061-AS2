package com.example.kewis.models;

   /*String CREATE_ORDERS_TABLE = "CREATE TABLE orders (" +
           "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
           "user_id INTEGER NOT NULL, " +
           "order_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
           "order_status TEXT NOT NULL, " +
           "FOREIGN KEY(user_id) REFERENCES user(id))";*/


public class Order {
    private int id;
    private int userId;
    private String orderDate;
    private String orderStatus;
    private String ProductName;
    private int quantity;


    public Order(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //seTQuantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }
}
