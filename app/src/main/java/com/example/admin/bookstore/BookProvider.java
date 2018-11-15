package com.example.admin.bookstore;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.admin.bookstore.Contracts.BookStoreContract;
import com.example.admin.bookstore.R;
import com.example.admin.bookstore.Contracts.BookStoreContract.bookEntry;

public class BookProvider extends ContentProvider {

    private static final int PRODUCTS = 100;
    private static final int PRODUCT_ID = 101;

    private BookDatabaseHelper bookDatabaseHelper;
    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(BookStoreContract.CONTENT_AUTHORITY, BookStoreContract.PATH_PRODUCTS, PRODUCTS);
        uriMatcher.addURI(BookStoreContract.CONTENT_AUTHORITY, BookStoreContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
        bookDatabaseHelper = new BookDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase = bookDatabaseHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);

        switch (match) {
            case PRODUCTS:
                  cursor = sqLiteDatabase.query(bookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PRODUCT_ID:
                selection = bookEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(bookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Error " );
        }

        if (getContext() != null) cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Error");
        }
    }

    private Uri insertProduct(Uri uri, ContentValues contentValues) {
        if (getContext() != null) {
            String productName = contentValues.getAsString(bookEntry.COLUMN_PRODUCT_NAME);
            if (productName == null || TextUtils.isEmpty(productName))
                throw new IllegalArgumentException("Enter Name");

            Integer productPrice = contentValues.getAsInteger(bookEntry.COLUMN_PRICE);
            if (productPrice == null || productPrice < 0)
                throw new IllegalArgumentException("Enter price");

            Integer productQuantity = contentValues.getAsInteger(bookEntry.COLUMN_QUANTITY);
            if (productQuantity == null || productQuantity < 0)
                throw new IllegalArgumentException("Enter quantity");

            String supplierName = contentValues.getAsString(bookEntry.COLUMN_SUPPLIER_NAME);
            if (supplierName == null || TextUtils.isEmpty(supplierName))
                throw new IllegalArgumentException("Enter supplier name");

            String supplierContactNumber = contentValues.getAsString(bookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
            if (supplierContactNumber == null || TextUtils.isEmpty(supplierContactNumber))
                throw new IllegalArgumentException("Enter phone number");

        }

        SQLiteDatabase sqLiteDatabase = bookDatabaseHelper.getWritableDatabase();

        long rowId = sqLiteDatabase.insert(bookEntry.TABLE_NAME, null, contentValues);
        if (rowId == -1) {
            Toast.makeText(getContext(),"Error, row was not inserted",Toast.LENGTH_SHORT).show();
            return null;
        }

        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, rowId);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = bookDatabaseHelper.getWritableDatabase();
        int rowsDeleted;
        int match = uriMatcher.match(uri);

        switch (match) {
            case PRODUCTS:
                rowsDeleted = sqLiteDatabase.delete(bookEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case PRODUCT_ID:
                selection = bookEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = sqLiteDatabase.delete(bookEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Error");
        }

        if (rowsDeleted != 0 && getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);

            case PRODUCT_ID:
                selection = bookEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Error");
        }
    }

    private int updateProduct(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (getContext() != null) {
            if (contentValues.containsKey(bookEntry.COLUMN_PRODUCT_NAME)) {
                String productName = contentValues.getAsString(bookEntry.COLUMN_PRODUCT_NAME);
                if (productName == null || TextUtils.isEmpty(productName))
                    throw new IllegalArgumentException("Enter name");
            }

            if (contentValues.containsKey(bookEntry.COLUMN_PRICE)) {
                Integer productPrice = contentValues.getAsInteger(bookEntry.COLUMN_PRICE);
                if (productPrice == null || TextUtils.isEmpty(String.valueOf(productPrice)))
                    throw new IllegalArgumentException("Enter price");
            }

            if (contentValues.containsKey(bookEntry.COLUMN_QUANTITY)) {
                Integer productQuantity = contentValues.getAsInteger(bookEntry.COLUMN_QUANTITY);
                if (productQuantity == null || TextUtils.isEmpty(String.valueOf(productQuantity)))
                    throw new IllegalArgumentException("Enter quantity");
            }

            if (contentValues.containsKey(bookEntry.COLUMN_SUPPLIER_NAME)) {
                String supplierName = contentValues.getAsString(bookEntry.COLUMN_SUPPLIER_NAME);
                if (supplierName == null || TextUtils.isEmpty(supplierName))
                    throw new IllegalArgumentException("Enter supplier name");
            }

            if (contentValues.containsKey(bookEntry.COLUMN_SUPPLIER_PHONE_NUMBER)) {
                String supplierContactNumber = contentValues.getAsString(bookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
                if (supplierContactNumber == null || TextUtils.isEmpty(supplierContactNumber))
                    throw new IllegalArgumentException("Enter phone no");

            }
        }

        if (contentValues.size() == 0)
            return 0;

        SQLiteDatabase sqLiteDatabase = bookDatabaseHelper.getWritableDatabase();
        int rowsUpdated = sqLiteDatabase.update(bookEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        if (rowsUpdated != 0 && getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PRODUCTS:
                return bookEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return bookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Error");
        }
    }
}