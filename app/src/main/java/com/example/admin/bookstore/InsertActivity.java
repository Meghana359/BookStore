package com.example.admin.bookstore;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.bookstore.R;
import com.example.admin.bookstore.Contracts.BookStoreContract.bookEntry;

public class InsertActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String currentBookName;
    private int currentQuantity;
    private int currentPrice;
    private String currentSupplierName;
    private String currentSupplierPhone;
    private Uri currentUri;
    private EditText mName;
    private EditText mPrice;
    private EditText mQuantity;
    private EditText mSupplierName;
    private EditText mSupplierPhoneNo;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Button increase = findViewById(R.id.increase);
        Button decrease = findViewById(R.id.decrease);
        Button order = findViewById(R.id.order);
        currentUri = getIntent().getData();

        if (currentUri == null) {
            setTitle("Add product");
            invalidateOptionsMenu();
            order.setVisibility(View.GONE);
        } else {
            setTitle("Edit");
            getLoaderManager().initLoader(1, null, this);
        }

        mName =findViewById(R.id.productName);
        mPrice = findViewById(R.id.price);
        mQuantity = findViewById(R.id.quantity);
        mSupplierName = findViewById(R.id.supplierName);
        mSupplierPhoneNo = findViewById(R.id.supplierPhoneNumber);
        mQuantity.setText("0");

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mQuantity.getText())) {
                    mQuantity.setText(String.valueOf(Integer.parseInt(mQuantity.getText().toString()) + 1));
                } else {
                    Toast.makeText(getApplicationContext(), "Enter quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mQuantity.getText().toString().equals("0") && !TextUtils.isEmpty(mQuantity.getText())) {
                    mQuantity.setText(String.valueOf(Integer.parseInt(mQuantity.getText().toString()) - 1));
                } else {
                    Toast.makeText(getApplicationContext(),"Enter quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+currentSupplierPhone)));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem addUpdateMenuItem = menu.findItem(R.id.save);
        addUpdateMenuItem.setTitle("Add Book");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                addProduct();
                if (flag) finish();
                break;

            case R.id.delete_product:
                DeleteConfirmation();
                break;
            case android.R.id.home:
                onBackPressed();
                break;


        }
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                bookEntry.COLUMN_ID,
                bookEntry.COLUMN_PRODUCT_NAME,
                bookEntry.COLUMN_PRICE,
                bookEntry.COLUMN_QUANTITY,
                bookEntry.COLUMN_SUPPLIER_NAME,
                bookEntry.COLUMN_SUPPLIER_PHONE_NUMBER,
        };

        return new CursorLoader(this, currentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            currentBookName = cursor.getString(cursor.getColumnIndexOrThrow(bookEntry.COLUMN_PRODUCT_NAME));
            currentPrice = cursor.getInt(cursor.getColumnIndexOrThrow(bookEntry.COLUMN_PRICE));
            currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(bookEntry.COLUMN_QUANTITY));
            currentSupplierName = cursor.getString(cursor.getColumnIndexOrThrow(bookEntry.COLUMN_SUPPLIER_NAME));
            currentSupplierPhone = cursor.getString(cursor.getColumnIndexOrThrow(bookEntry.COLUMN_SUPPLIER_PHONE_NUMBER));

            mName.setText(currentBookName);
            mPrice.setText(String.valueOf(currentPrice));
            mQuantity.setText(String.valueOf(currentQuantity));
            mSupplierName.setText(currentSupplierName);
            mSupplierPhoneNo.setText(currentSupplierPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mName.setText("");
        mPrice.setText("");
        mQuantity.setText("");
        mSupplierName.setText("");
        mSupplierPhoneNo.setText("");
    }

    private void addProduct() {
        String productName = mName.getText().toString().trim();
        String productPrice = mPrice.getText().toString().trim();
        String productQuantity = mQuantity.getText().toString().trim();
        String supplierName = mSupplierName.getText().toString().trim();
        String supplierPhone = mSupplierPhoneNo.getText().toString().trim();

        if (currentUri == null || TextUtils.isEmpty(productName) || TextUtils.isEmpty(productPrice) ||
                TextUtils.isEmpty(productQuantity) || TextUtils.isEmpty(supplierName) || TextUtils.isEmpty(supplierPhone) || supplierPhone.length() != 10) {
            if (TextUtils.isEmpty(productName)) {
                Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
                flag = false;
                return;
            } else {
                flag = true;
            }

            if (TextUtils.isEmpty(String.valueOf(productPrice))) {
                Toast.makeText(this, "Enter price", Toast.LENGTH_SHORT).show();
                flag = false;
                return;
            } else {
                flag = true;
            }

            if (TextUtils.isEmpty(String.valueOf(productQuantity))) {
                Toast.makeText(this, "Enter quantity", Toast.LENGTH_SHORT).show();
                flag = false;
                return;
            } else {
                flag = true;
            }


            if (TextUtils.isEmpty(supplierName)) {
                Toast.makeText(this, "Enter supplier name", Toast.LENGTH_SHORT).show();
                flag = false;
                return;
            } else {
                flag = true;
            }

            if (TextUtils.isEmpty(supplierPhone) ) {
                Toast.makeText(this, "Enter phone no", Toast.LENGTH_SHORT).show();
                flag = false;
                return;
            } else {
                flag = true;
            }
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(bookEntry.COLUMN_PRODUCT_NAME, productName);
        contentValues.put(bookEntry.COLUMN_PRICE, Integer.parseInt(productPrice));
        contentValues.put(bookEntry.COLUMN_QUANTITY, Integer.parseInt(productQuantity));
        contentValues.put(bookEntry.COLUMN_SUPPLIER_NAME, supplierName);
        contentValues.put(bookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhone);

        Uri newUri = getContentResolver().insert(bookEntry.CONTENT_URI, contentValues);
        if (newUri == null) {
            Toast.makeText(this, "Error inserting", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Successfully inserted", Toast.LENGTH_SHORT).show();
        }
    }



    private void deleteProduct() {
        if (currentUri != null) {
            int rowsDeleted = getContentResolver().delete(currentUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, "Error deleting", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Successfully deleted", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    private void DeleteConfirmation() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                deleteProduct();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}