package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.android.inventoryapp.data.WarehouseContract.ProductEntry;
import com.example.android.inventoryapp.data.WarehouseContract.SupplierEntry;

/**
 * Database helper class
 */

public class ProductDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "warehouse.db";
    private static final int DATABASE_VERSION = 1;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + ProductEntry.TABLE_NAME +
                "(" +
                ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProductEntry.COLUMN_PRODUCT_NAME + " TEXT," +
                ProductEntry.COLUMN_PRODUCT_PRICE + " TEXT NOT NULL DEFAULT 0," +
                ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER DEFAULT 0," +
                ProductEntry.COLUMN_PRODUCT_PICTURE + " TEXT NOT NULL," +

                "CREATE TABLE " + SupplierEntry.TABLE_NAME +
                "(" +
                SupplierEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SupplierEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, " +
                SupplierEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL); ";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_CREATE_ENTRIES =

                "DROP TABLE " + ProductEntry.TABLE_NAME + ";" +
                "DROP TABLE " + SupplierEntry.TABLE_NAME + ";" +

                "CREATE TABLE " + ProductEntry.TABLE_NAME +
                "(" +
                ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProductEntry.COLUMN_PRODUCT_NAME + " TEXT," +
                ProductEntry.COLUMN_PRODUCT_PRICE + " TEXT NOT NULL DEFAULT 0," +
                ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER DEFAULT 0," +
                ProductEntry.COLUMN_PRODUCT_PICTURE + " TEXT NOT NULL," +

                "CREATE TABLE " + SupplierEntry.TABLE_NAME +
                "(" +
                SupplierEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SupplierEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, " +
                SupplierEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL); ";

        db.execSQL(SQL_CREATE_ENTRIES);
    }
}
