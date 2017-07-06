package com.example.android.inventoryapp;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.android.inventoryapp.data.WarehouseContract.ProductEntry;

import java.text.NumberFormat;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText nameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private ImageView pictureImageView;
    String quantityString;
    Uri imageUri;

    private boolean productHasChanged = false;
    private static final int PRODUCT_LOADER = 0;
    private static final int MAX_PRODUCT_QUANTITY = 999999;
    private static final int MIN_PRODUCT_QUANTITY = 0;
    Uri currentProductUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_editor);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        currentProductUri = intent.getData();

        if (currentProductUri == null) {
            setTitle("Add Product");
        } else {
            setTitle("Edit Product");
            getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
        }

        nameEditText = (EditText) findViewById(R.id.edit_name);
        priceEditText = (EditText) findViewById(R.id.edit_price);
        quantityEditText = (EditText) findViewById(R.id.edit_quantity);
        pictureImageView = (ImageView) findViewById(R.id.imageView);

        nameEditText.setOnTouchListener(mTouchListener);
        priceEditText.setOnTouchListener(mTouchListener);
        quantityEditText.setOnTouchListener(mTouchListener);
        pictureImageView.setOnTouchListener(mTouchListener);

        Button pickImageButton = (Button) findViewById(R.id.btn_pick);
        pickImageButton.setOnTouchListener(mTouchListener);
        Button decreaseQuantityButton = (Button) findViewById(R.id.decrease_quantity_btn);
        decreaseQuantityButton.setOnTouchListener(mTouchListener);
        Button increaseQuantityButton = (Button) findViewById(R.id.increase_quantity_btn);
        increaseQuantityButton.setOnTouchListener(mTouchListener);

        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trySelector();
                productHasChanged = true;
            }
        });

        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityString = quantityEditText.getText().toString().trim();
                int quantity = !quantityString.isEmpty() ? Integer.parseInt(quantityString) : 0;
                quantity = quantity < MAX_PRODUCT_QUANTITY ? quantity + 1 : MAX_PRODUCT_QUANTITY;
                quantityEditText.setText(Integer.toString(quantity));
            }
        });

        decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityString = quantityEditText.getText().toString().trim();
                int quantity = !quantityString.isEmpty() ? Integer.parseInt(quantityString) : 0;
                quantity = quantity > MIN_PRODUCT_QUANTITY ? quantity - 1 : MIN_PRODUCT_QUANTITY;
                quantityEditText.setText(Integer.toString(quantity));
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProduct();
                finish();
            }
        });
    }

    private void saveProduct() {
        String name = nameEditText.getText().toString().trim();
        // replace all nondigit from string input, this will also remove white spaces
        String priceString = priceEditText.getText().toString().replaceAll("\\D", "");
        quantityString = quantityEditText.getText().toString().trim();


        int price = !TextUtils.isEmpty(priceString) ? Integer.parseInt(priceString) : 0;
        int quantity = !TextUtils.isEmpty(quantityString) ? Integer.parseInt(quantityString) : 0;
        String image = imageUri != null ? imageUri.toString() :
                "android.resource://com.example.android.inventoryapp/2130837600";

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, name);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(ProductEntry.COLUMN_PRODUCT_PICTURE, image);

        if (currentProductUri == null && TextUtils.isEmpty(name)) {
            return;
        }

        if (currentProductUri == null) {
            setTitle("Add Product");
            invalidateOptionsMenu();
            Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI_PRODUCTS, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_new_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Prepare the loader.  Either re-connect with an existing one,
            // or start a new one.
            int rowsAffected = getContentResolver().update(currentProductUri, values, null, null);
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_new_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            productHasChanged = true;
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_order) {
            sendOrderToSupplier();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_product) {
            deleteProduct();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteProduct() {
        if (currentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(currentProductUri, null, null);
            Log.v("CatalogActivity", rowsDeleted + " rows deleted from products database");
            finish();
            Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void sendOrderToSupplier() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, "supplier@email.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Ordering product:" + nameEditText.getText().toString().trim());
        intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // you will actually use after this query.
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_PICTURE};

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, currentProductUri,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int pictureColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PICTURE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String picture = cursor.getString(pictureColumnIndex);

            // Update the views on the screen with the values from the database
            nameEditText.setText(name);

            double properPrice = price * 0.01;
            String formattedPrice = getFormatedPrice(properPrice);

            priceEditText.setText(formattedPrice);
            quantityEditText.setText(Integer.toString(quantity));
            pictureImageView.setImageURI(Uri.parse(picture));
            imageUri = Uri.parse(picture);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
    }

    public String getFormatedPrice(double price) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        return (format.format(price));
    }

    public void trySelector() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
        openSelector();
    }

    private void openSelector() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select picture"), 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openSelector();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                pictureImageView.setImageURI(imageUri);
                pictureImageView.invalidate();
            }
        }
    }
}
