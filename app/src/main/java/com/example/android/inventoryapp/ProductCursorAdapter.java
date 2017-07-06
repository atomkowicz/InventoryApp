package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.inventoryapp.data.WarehouseContract.ProductEntry;

import java.text.DecimalFormat;
import java.util.Locale;


public class ProductCursorAdapter extends CursorAdapter {

    private CatalogActivity catalogActivity = new CatalogActivity();


    public ProductCursorAdapter(CatalogActivity context, Cursor c) {
        super(context, c, 0);
        this.catalogActivity = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.list_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.list_price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.list_quantity);
        ImageView productImageView = (ImageView) view.findViewById(R.id.list_image);
        Button saleButton = (Button) view.findViewById(R.id.sale_btn);

        final Long id = cursor.getLong(cursor.getColumnIndex(ProductEntry._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME));
        String price = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));
        final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        String image = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PICTURE));

        nameTextView.setText(name + Long.toString(id));
        quantityTextView.setText(String.valueOf(quantity));
        productImageView.setImageURI(Uri.parse(image));
        priceTextView.setText(price + " EUR");

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catalogActivity.onSaleBtnClick(id, quantity);
                notifyDataSetChanged();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catalogActivity.onListItemClick(id);
            }
        });
    }


}
