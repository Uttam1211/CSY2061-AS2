package com.example.kewis.models;

public class Product {

    private int id;
    private String name;
    private String description;
    private double price;
    private String currency;
    private String sku;
    private int quantity;
    private int categoryId;
    private String imageUrl;
    private String dateAdded;
    private String lastUpdated;
    private int active;

    public Product(int id, String name, String description, double price, String currency, String sku, int quantity, int categoryId, String imageUrl, String dateAdded, String lastUpdated, int active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.sku = sku;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.dateAdded = dateAdded;
        this.lastUpdated = lastUpdated;
        this.active = active;
    }

    public Product(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Product(String productName, double productPrice, int productQuantity, String productDescription, String productImage, int categoryId) {
        this.name = productName;
        this.price = productPrice;
        this.quantity = productQuantity;
        this.description = productDescription;
        this.imageUrl = productImage;
        this.categoryId = categoryId;
    }

    public Product(int id, String productName, double productPrice, int productQuantity, String productDescription, String productImage, int categoryId) {
        this.id = id;
        this.name = productName;
        this.price = productPrice;
        this.quantity = productQuantity;
        this.description = productDescription;
        this.imageUrl = productImage;
        this.categoryId = categoryId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
