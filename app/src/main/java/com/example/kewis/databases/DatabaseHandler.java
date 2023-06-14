package com.example.kewis.databases;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.kewis.models.Cart;
import com.example.kewis.models.Category;
import com.example.kewis.models.Feed;
import com.example.kewis.models.Order;
import com.example.kewis.models.Product;
import com.example.kewis.models.User;
import com.example.kewis.models.WishList;
import com.example.kewis.ui.wishlist.WishlistFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private Context context;



    public DatabaseHandler(Context context, DatabaseHelper databaseHelper) {
        this.context = context;
        this.dbHelper = databaseHelper;
        this.db = dbHelper.getWritableDatabase();

    }
    public  boolean isOpen(){
        return db !=null && db.isOpen();
    }


    // CREATE method
    public long create(String table, ContentValues values) {
        return db.insert(table, null, values);
    }


    // READ method
    public Cursor read(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    //join read method
    public Cursor joinRead(String table, String join, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        String query = SQLiteQueryBuilder.buildQueryString(false, table + " " + join, columns, selection, groupBy, having, orderBy, limit);
        return db.rawQuery(query, selectionArgs);
    }


    // UPDATE method
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return db.update(table, values, whereClause, whereArgs);
    }

    // DELETE method
    public int delete(String table, String whereClause, String[] whereArgs) {
        return db.delete(table, whereClause, whereArgs);
    }

    public User authenticateUser(String email, String password) {
        String[] columns = {"id", "email", "password", "user_type"};
        String selection = "email = ? AND password = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = null;
        User user = null;

        try {

            cursor = read("users", columns, selection, selectionArgs, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("id")));
                String userType = cursor.getString(cursor.getColumnIndexOrThrow("user_type"));

                user = new User(id, email, password, userType);
                updateLastLogin(email);
            }
        } catch (IllegalArgumentException e) {
            // Handle the exception if the specified column does not exist
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return user;
    }

    public void createDefaultAdmin() {
        // Check if the admin account already exists
        String[] columns = {"username"};
        String selection = "username = ?";
        String[] selectionArgs = {"admin"};
        Cursor cursor = read("users", columns, selection, selectionArgs, null, null, null, "1");

        // If the admin account does not exist, create it
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put("first_name", "Admin");
            values.put("last_name", "Admin");
            values.put("email", "admin@admin.com");
            values.put("password", "admin123");
            values.put("memorable_word", "adminword");
            values.put("username", "admin");
            values.put("user_type", "admin");

            create("users", values);
        }

        cursor.close();
    }

    //default categories
    public void createDefaultCategories() {
        String[] categoryNames = {"Electronics", "Books", "Fashion", "Home and Kitchen"};
        String[] categoryImages = {
                "https://i.ibb.co/K7RGWNq/pexels-karol-d-325153.jpg",
                "https://i.ibb.co/kKfv9jt/fmh3-6uhn-190123.jpg",
                "https://i.ibb.co/HzMTV6h/fashion-polo-shirt-men.jpg",
                "https://i.ibb.co/pzBP0tt/OR7ZSK0.jpg"
        };

        String[] columns = {"name"};
        String selection = "name = ?";

        for (int i = 0; i < categoryNames.length; i++) {
            String[] selectionArgs = {categoryNames[i]};
            Cursor cursor = read("categories", columns, selection, selectionArgs, null, null, null, "1");
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put("name", categoryNames[i]);
                values.put("description", categoryNames[i]);
                values.put("image_url", categoryImages[i]);

                create("categories", values);
            }

            cursor.close();
        }
    }



    public void updateLastLogin(String email){
        ContentValues values = new ContentValues();
        values.put("last_login", getCurrentDateTime());
        String whereClause = "email = ?";
        String[] whereArgs = {email};
        update("users", values, whereClause, whereArgs);
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public long addCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("description", category.getDescription());
        values.put("image_url", category.getImageUrl());

        return create("categories", values);
    }

    public long addProduct(Product product){
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("description", product.getDescription());
        values.put("price", product.getPrice());
        values.put("image_url", product.getImageUrl());
        values.put("category_id", product.getCategoryId());
        values.put("quantity", product.getQuantity());

        return create("products", values);


    }
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "id",
                "name",
                "description",
                "image_url"
        };

        Cursor cursor = read("categories", projection, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));
                Category category = new Category(name, description, imageUrl);
                category.setId(id);
                categories.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return categories;
    }

    public List<Category> readAllCategories() {
        List<Category> categories = new ArrayList<>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "id",
                "name",
                "description",
                "image_url"
        };

        Cursor cursor = read("categories", projection, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));
                Category category = new Category(id, name, description, imageUrl);
                categories.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return categories;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "id",
                "name",
                "description",
                "price",
                "image_url",
                "category_id",
                "quantity"
        };

        Cursor cursor = read("products", projection, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                Product product = new Product(name,price,quantity,description,imageUrl,categoryId);
                product.setId(id);
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return products;
    }
//getProductByCategory

    public List<Product> getProductByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "id",
                "name",
                "description",
                "price",
                "image_url",
                "category_id",
                "quantity"
        };

        String selection = "category_id = ?";
        String[] selectionArgs = {String.valueOf(categoryId)};

        Cursor cursor = read("products", projection, selection, selectionArgs, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                Product product = new Product(name,price,quantity,description,imageUrl,categoryId);
                product.setId(id);
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return products;
    }

    public int updateCategory(Category category) {
            ContentValues values = new ContentValues();
            values.put("name", category.getName());
            values.put("description", category.getDescription());
            values.put("image_url", category.getImageUrl());

            // updating row
            int rowsUpdated = 0;
            try {
                rowsUpdated = db.update("categories", values, "id = ?", new String[]{String.valueOf(category.getId())});
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d("DatabaseHandler", "updateCategory: rowsUpdated = " + rowsUpdated);
            return rowsUpdated;
        }

        public int updateProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("description", product.getDescription());
        values.put("price", product.getPrice());
        values.put("image_url", product.getImageUrl());
        values.put("category_id", product.getCategoryId());
        values.put("quantity", product.getQuantity());

        int rowsUpdated = update("products", values, "id = ?", new String[]{String.valueOf(product.getId())});
        return rowsUpdated;
        }



    public long updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put("first_name", user.getFirst_name());
        values.put("last_name", user.getLast_name());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("memorable_word", user.getMemorable_word());
        values.put("phone_number", user.getPhone_number());
        values.put("username", user.getUsername());
        values.put("address", user.getAddress());
        values.put("user_type", user.getUsertype());
        values.put("profile_picture", user.getProfilePicture());

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(user.getId())};

        return update("users", values, whereClause, whereArgs);

    }

    public long addUser(User user) {
        ContentValues values = new ContentValues();
        values.put("first_name", user.getFirst_name());
        values.put("last_name", user.getLast_name());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("memorable_word", user.getMemorable_word());
        values.put("phone_number", user.getPhone_number());
        values.put("username", user.getUsername());
        values.put("address", user.getAddress());
        values.put("user_type", user.getUsertype());
        Log.d(TAG, "addUser: "+user.getProfilePicture());
        values.put("profile_picture", user.getProfilePicture());
        String profilePicture = user.getProfilePicture();
        Log.d(TAG, "Profile Picture Length: " + (profilePicture != null ? profilePicture : "null"));
        values.put("profile_picture", profilePicture);


        return create("users", values);



    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "id",
                "first_name",
                "last_name",
                "email",
                "password",
                "memorable_word",
                "phone_number",
                "username",
                "address",
                "user_type",
                "profile_picture",
        };

        Cursor cursor = read("users", projection, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String memorableWord = cursor.getString(cursor.getColumnIndexOrThrow("memorable_word"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String userType = cursor.getString(cursor.getColumnIndexOrThrow("user_type"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phone_number"));
                String profilePicture = cursor.getString(cursor.getColumnIndexOrThrow("profile_picture"));

                User user = new User(firstName, lastName, email, password, phoneNumber, memorableWord, address, userType, username, profilePicture);
                user.setId(id);
                user.setUsername(username);
                users.add(user);
            } while (cursor.moveToNext());
        }
return users;
    }

    public long addFeed(Feed feed) {
        ContentValues values = new ContentValues();
        values.put("feed_title", feed.getFeedName());
        return create("content_title", values);
    }

    public List<Feed> getAllFeeds() {
        List<Feed> feeds = new ArrayList<>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "id",
                "feed_title",
        };

        Cursor cursor = read("content_title", projection, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("feed_title"));

                Feed feed = new Feed(title);
                feed.setId(id);
                feeds.add(feed);
            } while (cursor.moveToNext());
        }
        return feeds;
    }

    public boolean insertCart(int user_id, int product_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user_id);
        contentValues.put("product_id", product_id);
        long result = db.insert("cart", null, contentValues);
        db.close();

        //if data as inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean insertWishlist(int user_id, int product_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user_id);
        contentValues.put("product_id", product_id);
        long result = db.insert("wishlist", null, contentValues);
        db.close();

        //if data as inserted incorrectly it will return -1
        return result != -1;
    }

    public boolean isInCart(int user_id, int product_id) {
        String selection = "user_id = ? AND product_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(user_id), String.valueOf(product_id)};
        Cursor cursor = read("cart", null, selection, selectionArgs, null, null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    public boolean isInCartByUser(int user_id) {
        String selection = "user_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(user_id)};
        Cursor cursor = read("cart", null, selection, selectionArgs, null, null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean isInWishlist(int user_id, int product_id) {
        String selection = "user_id = ? AND product_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(user_id), String.valueOf(product_id)};
        Cursor cursor = read("wishlist", null, selection, selectionArgs, null, null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public List<Cart> getAllCartItems(int userId) {
        List<Cart> cartItems = new ArrayList<>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "cart.id",
                "cart.user_id",
                "cart.product_id",
                "products.name",
                "products.price",
                "products.image_url",
                "cart.date_added",
                "cart.quantity",
        };

        // Implement the JOIN query between "cart" and "products" table on "product_id"
        String selection = "cart.user_id = ?";
        String join = "INNER JOIN products ON cart.product_id = products.id";
        String[] selectionArgs = new String[]{String.valueOf(userId)};

        Cursor cursor = joinRead("cart", join, projection, selection, selectionArgs, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int user_id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                int product_id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                String product_name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int product_price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                String product_image = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));
                String date_added = cursor.getString(cursor.getColumnIndexOrThrow("date_added"));

                Cart cartItem = new Cart(user_id, product_id, product_name, product_price, product_image, date_added);
                cartItem.setId(id);
                cartItems.add(cartItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return cartItems;
    }

    public long insertOrder(int userId) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", userId);
        contentValues.put("order_status", "pending");
        return db.insert("orders", null, contentValues);
    }

    public void insertOrderItem(long orderId, int productId, int quantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("order_id", orderId);
        contentValues.put("product_id", productId);
        contentValues.put("quantity", quantity);
        db.insert("order_items", null, contentValues);
    }

    public List<Order> getAllOrders(int userId) {
        List<Order> orders = new ArrayList<>();

        String selectQuery = "SELECT orders.id as order_id, orders.order_date, orders.order_status, products.name, order_items.quantity " +
                "FROM orders " +
                "JOIN order_items ON orders.id = order_items.order_id " +
                "JOIN products ON order_items.product_id = products.id " +
                "WHERE orders.user_id = " + userId;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                // Set order ID, product name, quantity, order date, and order status
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow("order_id")));
                order.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                order.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                order.setOrderDate(cursor.getString(cursor.getColumnIndexOrThrow("order_date")));
                order.setOrderStatus(cursor.getString(cursor.getColumnIndexOrThrow("order_status")));

                orders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return orders;
    }

    public boolean isInWishlistByUser(int userId) {
        String selection = "user_id = ? ";
        String[] selectionArgs = new String[]{String.valueOf(userId)};
        Cursor cursor = read("wishlist", null, selection, selectionArgs, null, null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public List<WishList> getWishlist(int userId) {
        List<WishList> wishlist = new ArrayList<>();


        String[] projection = {
                "wishlist.id",
                "wishlist.user_id",
                "wishlist.product_id",
                "products.name",
                "products.price",
                "products.image_url",
                "wishlist.date_added",
        };
        // Implement the JOIN query between "wishlist" and "products" table on "product_id"
        String selection = "wishlist.user_id = ?";
        String join = "INNER JOIN products ON wishlist.product_id = products.id";
        String[] selectionArgs = new String[]{String.valueOf(userId)};

        Cursor cursor = joinRead("wishlist", join, projection, selection, selectionArgs, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int user_id = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                int product_id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                String product_name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int product_price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                String product_image = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));
                String date_added = cursor.getString(cursor.getColumnIndexOrThrow("date_added"));

                WishList wishlistItem = new WishList(user_id, product_id, product_name, product_price, product_image, date_added);
                wishlistItem.setId(id);
                wishlist.add(wishlistItem);
            }while (cursor.moveToNext());
        }
    cursor.close();
    return wishlist;
    }
}

