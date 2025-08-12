package com.dn.woofwell;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CartDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cart.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "carts";
    private static final String ID = "id";
    private static final String ITEM_NAMES = "itemName";
    private static final String ITEM_COUNTS = "itemCount";
    private static final String ITEMS_PRICE = "itemTotal";
    private static final String ITEM_DESC = "itemDesc";
    private static final String ITEM_IMAGE = "itemImage";
    private static final String SINGLE_PRICE = "singlePrice";
    private static final String YOUTUBE = "youtube";
    private static final String BRAND = "brand";
    private static final String TYPE = "type";
    private static final String AGE = "age";
    private static final String SELLER_NAME = "sellerName";
    private static final String SELLER_ID = "sellerId";
    private static final String KEY = "key";

    public CartDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ITEM_NAMES + " TEXT,"
                + ITEM_COUNTS + " TEXT,"
                + ITEMS_PRICE + " TEXT,"
                + ITEM_DESC + " TEXT,"
                + ITEM_IMAGE + " TEXT,"
                + SINGLE_PRICE + " TEXT,"
                + YOUTUBE + " TEXT,"
                + BRAND + " TEXT,"
                + TYPE + " TEXT,"
                + AGE + " TEXT,"
                + SELLER_NAME + " TEXT,"
                + SELLER_ID + " TEXT,"
                + KEY + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addCarts(String itemName, String itemCount, String itemTotal, String itemDesc, String itemImage, String singlePrice, String youtube, String brand, String type, String age, String sellerName, String sellerId, String key) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ITEM_NAMES, itemName);
        values.put(ITEM_COUNTS, itemCount);
        values.put(ITEMS_PRICE, itemTotal);
        values.put(ITEM_DESC, itemDesc);
        values.put(ITEM_IMAGE, itemImage);
        values.put(SINGLE_PRICE, singlePrice);
        values.put(YOUTUBE, youtube);
        values.put(BRAND, brand);
        values.put(TYPE, type);
        values.put(AGE, age);
        values.put(SELLER_NAME, sellerName);
        values.put(SELLER_ID, sellerId);
        values.put(KEY, key);

        Log.d("CartDB", "addCart: Adding " + itemName + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();

        return result;
    }

    public List<Cart> getCarts() {
        List<Cart> cartList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String itemName = cursor.getString(cursor.getColumnIndex(ITEM_NAMES));
                    String itemCount = cursor.getString(cursor.getColumnIndex(ITEM_COUNTS));
                    String itemDesc = cursor.getString(cursor.getColumnIndex(ITEM_DESC));
                    String itemTotal = cursor.getString(cursor.getColumnIndex(ITEMS_PRICE));
                    String itemImage = cursor.getString(cursor.getColumnIndex(ITEM_IMAGE));
                    String singlePrice = cursor.getString(cursor.getColumnIndex(SINGLE_PRICE));
                    String youtube = cursor.getString(cursor.getColumnIndex(YOUTUBE));
                    String brand = cursor.getString(cursor.getColumnIndex(BRAND));
                    String type = cursor.getString(cursor.getColumnIndex(TYPE));
                    String age = cursor.getString(cursor.getColumnIndex(AGE));
                    String sellerName = cursor.getString(cursor.getColumnIndex(SELLER_NAME));
                    String sellerId = cursor.getString(cursor.getColumnIndex(SELLER_ID));
                    String key = cursor.getString(cursor.getColumnIndex(KEY));

                    Cart cart = new Cart(itemName, itemCount, itemTotal, itemDesc, itemImage, singlePrice, youtube, brand, type, age, sellerName, sellerId, key);
                    cartList.add(cart);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("CartDB", "Error while fetching carts", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return cartList;
    }

    public void deleteItem(String itemName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, ITEM_NAMES + "=?", new String[] { itemName });
        db.close();
    }

    public void deleteAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
