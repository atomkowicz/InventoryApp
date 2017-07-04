package com.example.android.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class WarehouseContract {
  public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
  public static final String PATH_PRODUCTS = "products";
  public static final String PATH_SUPPLIERS = "suppliers";

  private WarehouseContract() {}
  public static final class ProductEntry implements BaseColumns {
    public static final Uri CONTENT_URI_PRODUCTS = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

    public static final String TABLE_NAME = "products";
    public static final String _ID = BaseColumns._ID;
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_PICTURE = "picture";

    public static final String CONTENT_LIST_TYPE_PRODUCTS =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

    public static final String CONTENT_ITEM_TYPE_PRODUCTS =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
  }

  public static final class SupplierEntry implements BaseColumns {
    public static final Uri CONTENT_URI_SUPPLIERS = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SUPPLIERS);

    public static final String TABLE_NAME = "suppliers";
    public static final String _ID = BaseColumns._ID;
    public static final String COLUMN_SUPPLIER_NAME = "name";
    public static final String COLUMN_SUPPLIER_EMAIL = "email";
  }
}
