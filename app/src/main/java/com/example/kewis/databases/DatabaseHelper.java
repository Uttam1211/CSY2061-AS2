package com.example.kewis.databases;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kewis.models.User;
import com.example.kewis.databases.DatabaseHandler;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    private SQLiteDatabase db;
    private Context context;
    public DatabaseHelper(Context context) {
        super(context, "kewis.db", null, 1);
        this.context = context;
        this.db= this.getWritableDatabase();
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public SQLiteDatabase getDatabase() {
        return this.db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table

        String CREATE_USERS_TABLE = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "username TEXT UNIQUE, " + // Ensure unique usernames
                "password TEXT, " +
                "memorable_word TEXT, " +
                "user_type TEXT, " +
                "date_created DATETIME DEFAULT CURRENT_TIMESTAMP, " + // Automatically set the creation date
                "last_login DATETIME, " + // Update this field when the user logs in
                "email TEXT UNIQUE, " + // Ensure unique email addresses
                "phone_number TEXT, " +
                "address TEXT,"+
                "profile_picture TEXT,"+
                "hobbies TEXT)";

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL UNIQUE, " + // Category name must be unique and not null
                "description TEXT, " +
                "parent_id INTEGER, " + // Reference to the parent category (for nested categories)
                "image_url TEXT, " + // URL to the category image
                "FOREIGN KEY(parent_id) REFERENCES categories(id))"; // Foreign key constraint for nested categories

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "price REAL NOT NULL, " +
                "currency TEXT DEFAULT '£', " +
                "sku TEXT UNIQUE, " + // Unique Stock Keeping Unit identifier
                "quantity INTEGER NOT NULL DEFAULT 0, " + // Product quantity available
                "category_id INTEGER, " + // Reference to the category table
                "image_url TEXT, " + // URL to the product image
                "date_added DATETIME DEFAULT CURRENT_TIMESTAMP, " + // Automatically set the creation date
                "last_updated DATETIME, " + // Update this field when the product is updated
                "active INTEGER NOT NULL DEFAULT 1, " + // Whether the product is active (1) or inactive (0)
                "FOREIGN KEY(category_id) REFERENCES categories(id))"; // Foreign key constraint to reference the categories table

        String CREATE_CART_TABLE = "CREATE TABLE cart (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " + // Reference to the user who added the product to their cart
                "product_id INTEGER NOT NULL, " + // Reference to the added product
                "quantity INTEGER NOT NULL DEFAULT 1, " + // Quantity of the product in the cart
                "date_added DATETIME DEFAULT CURRENT_TIMESTAMP, " + // Automatically set the creation date
                "FOREIGN KEY(user_id) REFERENCES user(id), " + // Foreign key constraint for referencing the user table
                "FOREIGN KEY(product_id) REFERENCES products(id))"; // Foreign key constraint for referencing the products table

        String CREATE_ORDERS_TABLE = "CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "order_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "order_status TEXT NOT NULL, " +
                "FOREIGN KEY(user_id) REFERENCES user(id))";

        String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE order_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "order_id INTEGER NOT NULL, " +
                "product_id INTEGER NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "FOREIGN KEY(order_id) REFERENCES orders(id), " +
                "FOREIGN KEY(product_id) REFERENCES products(id))";

        String CREATE_WISHLIST_TABLE = "CREATE TABLE wishlist (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " + // Reference to the user who added the product to their wishlist
                "product_id INTEGER NOT NULL, " + // Reference to the added product
                "date_added DATETIME DEFAULT CURRENT_TIMESTAMP, " + // Automatically set the creation date
                "FOREIGN KEY(user_id) REFERENCES user(id), " + // Foreign key constraint for referencing the user table
                "FOREIGN KEY(product_id) REFERENCES products(id))"; // Foreign key constraint for referencing the products table

        String CREATE_CONTENT_TABLE = "CREATE TABLE content (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "content_title_id INTEGER , " +
                "product_id INTEGER,"+// Reference to the user who created the content
                "date_created DATETIME DEFAULT CURRENT_TIMESTAMP, " + // Automatically set the creation date
                "FOREIGN KEY(content_title_id) REFERENCES content_title(id), " + // Foreign key constraint for referencing the user table
                "FOREIGN KEY(product_id) REFERENCES products(id))"; // Foreign key constraint for referencing the products table

        String CREATE_CONTENT_TITLE_TABLE = "CREATE TABLE content_title (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
               "feed_title TEXT)";

        String CREATE_REVIEWS_TABLE = "CREATE TABLE reviews (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " + // Reference to the user who wrote the review
                "product_id INTEGER NOT NULL, " + // Reference to the reviewed product
                "rating INTEGER NOT NULL, " + // Star rating (e.g., 1 to 5)
                "review_text TEXT, " + // Review text (optional)
                "date_created DATETIME DEFAULT CURRENT_TIMESTAMP, " + // Automatically set the creation date
                "FOREIGN KEY(user_id) REFERENCES user(id), " + // Foreign key constraint for referencing the user table
                "FOREIGN KEY(product_id) REFERENCES products(id))"; // Foreign key constraint for referencing the products table

        String CREATE_SHIPPING_ADDRESSES_TABLE = "CREATE TABLE shipping_addresses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " + // Reference to the user associated with the address
                "name TEXT NOT NULL, " + // Name of the recipient
                "street_address TEXT NOT NULL, " + // Street address
                "city TEXT NOT NULL, " + // City
                "state TEXT NOT NULL, " + // State or province
                "zip_code TEXT NOT NULL, " + // ZIP or postal code
                "country TEXT NOT NULL, " + // Country
                "phone_number TEXT NOT NULL, " + // Phone number
                "FOREIGN KEY(user_id) REFERENCES user(id))"; // Foreign key constraint for referencing the user table

        String CREATE_PAYMENT_METHODS_TABLE = "CREATE TABLE payment_methods (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " + // Reference to the user associated with the payment method
                "card_type TEXT NOT NULL, " + // Card type (e.g., Visa, MasterCard)
                "last_four_digits TEXT NOT NULL, " + // Last four digits of the card number
                "expiry_date TEXT NOT NULL, " + // Expiration date of the card
                "cardholder_name TEXT NOT NULL, " + // Name of the cardholder
                "FOREIGN KEY(user_id) REFERENCES user(id))"; // Foreign key constraint for referencing the user table

        String CREATE_COUPONS_TABLE = "CREATE TABLE coupons (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "code TEXT NOT NULL UNIQUE, " + // Unique coupon code
                "description TEXT, " + // Description of the coupon
                "discount_type TEXT NOT NULL, " + // Discount type (e.g., 'percentage' or 'fixed_amount')
                "discount_value REAL NOT NULL, " + // Discount value (e.g., percentage or fixed amount)
                "start_date DATETIME NOT NULL, " + // Start date of the coupon validity
                "end_date DATETIME NOT NULL, " + // End date of the coupon validity
                "min_order_value REAL, " + // Minimum order value to apply the coupon (optional)
                "max_discount_value REAL)"; // Maximum discount value allowed (optional)

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);
        db.execSQL(CREATE_WISHLIST_TABLE);
        db.execSQL(CREATE_CONTENT_TABLE);
        db.execSQL(CREATE_REVIEWS_TABLE);
        db.execSQL(CREATE_SHIPPING_ADDRESSES_TABLE);
        db.execSQL(CREATE_PAYMENT_METHODS_TABLE);
        db.execSQL(CREATE_COUPONS_TABLE);
        db.execSQL(CREATE_CONTENT_TITLE_TABLE);
        db.execSQL(CREATE_ORDER_ITEMS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //upgrade database schema if needed
    }





}
