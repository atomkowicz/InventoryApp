package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

public class WarehouseContract {

  private WarehouseContract() {}
  public static final class ProductEntry implements BaseColumns {

    public static final String TABLE_NAME = "products";
    public static final String _ID = BaseColumns._ID;
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_PICTURE = "picture";
  }

  public static final class SupplierEntry implements BaseColumns {

    public static final String TABLE_NAME = "suppliers";
    public static final String _ID = BaseColumns._ID;
    public static final String COLUMN_SUPPLIER_NAME = "name";
    public static final String COLUMN_SUPPLIER_EMAIL = "email";
  }
}
