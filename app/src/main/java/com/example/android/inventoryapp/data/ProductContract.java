package com.example.android.inventoryapp.data;

public class ProductContract {
  
  private ProductContract() {}
  public static final class ProductContract implements BaseColumns {
  
    public final static String TABLE_NAME = "products"
    public final static String _ID = BaseColumns._ID;
    public final static String name = "name";
    public final static String availability = "availability";
    public final static String material = "material";
    //possible values for material
    public static final int MATERIAL_NOT_SPECIFIED = 0;
    public static final int MATERIAL_WOOD = 1;
    public static final int MATERIAL_PLASTIC = 2;
  }
}
