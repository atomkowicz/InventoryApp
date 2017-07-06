package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.android.inventoryapp.data.WarehouseContract;
import com.example.android.inventoryapp.data.WarehouseContract.ProductEntry;

import static com.example.android.inventoryapp.data.WarehouseProvider.LOG_TAG;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ProductCursorAdapter adapter;
    private static final int PRODUCT_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_catalog);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the pet data
        ListView productListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);

        // Create empty adapter
        adapter = new ProductCursorAdapter(this, null);
        productListView.setAdapter(adapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pAdapterView, View pView, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI_PRODUCTS, id);
                intent.setData(currentProductUri);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_insert_dummy_data) {
            insertProduct();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_all_entries) {
            deleteAllProducts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onListItemClick(long id) {
        Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

        Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI_PRODUCTS, id);
        intent.setData(currentProductUri);

        startActivity(intent);
    }

    public void onSaleBtnClick(long id, int quantity) {
        Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI_PRODUCTS, id);

        int newQuantity = quantity > 0 ? quantity - 1 : 0;

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantity);

        int rowsAffected = getContentResolver().update(currentProductUri, values, null, null);
        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(this, getString(R.string.catalog_update_failed),
                    Toast.LENGTH_SHORT).show();
        } else if (quantity != newQuantity) {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.catalog_update_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void insertProduct() {
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Coffee cup");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 2900);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 2);

        Uri imgUri = Uri.parse("android.resource://" + WarehouseContract.CONTENT_AUTHORITY + "/" + R.drawable.mug1);
        Log.v(LOG_TAG, imgUri.toString());

        values.put(ProductEntry.COLUMN_PRODUCT_PICTURE, imgUri.toString());

        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI_PRODUCTS, values);
        Log.v(LOG_TAG, newUri.toString());
    }

    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(ProductEntry.CONTENT_URI_PRODUCTS, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from products database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_PICTURE};

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, ProductEntry.CONTENT_URI_PRODUCTS,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
