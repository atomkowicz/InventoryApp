package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.android.inventoryapp.data.WarehouseContract.ProductEntry;
import com.example.android.inventoryapp.data.WarehouseContract.SupplierEntry;

import static com.example.android.inventoryapp.data.WarehouseProvider.LOG_TAG;

/**
 * Database helper class
 */

public class WarehouseDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "warehouse.db";
    private static final int DATABASE_VERSION = 1;

    public WarehouseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public String SQL_CREATE_ENTRIES = "CREATE TABLE " + ProductEntry.TABLE_NAME +
            "(" +
            ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL," +
            ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL DEFAULT 0," +
            ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER DEFAULT 0," +
            ProductEntry.COLUMN_PRODUCT_PICTURE + " TEXT" +
            ");" +

            "CREATE TABLE " + SupplierEntry.TABLE_NAME +
            "(" +
            SupplierEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            SupplierEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, " +
            SupplierEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL" +
            ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(LOG_TAG, SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DROP_TABLES = "DROP TABLE " + ProductEntry.TABLE_NAME + ";" +
                "DROP TABLE " + SupplierEntry.TABLE_NAME + ";";

        db.execSQL(SQL_DROP_TABLES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }
}
