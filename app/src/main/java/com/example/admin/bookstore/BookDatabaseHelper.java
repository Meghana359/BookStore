package com.example.admin.bookstore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.admin.bookstore.Contracts.BookStoreContract.bookEntry;

public class BookDatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = BookDatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "bookStore.db";
    private static final int DATABASE_VERSION = 1;

    BookDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_PRODUCT_TABLE =
                "CREATE TABLE " + bookEntry.TABLE_NAME + "("
                        + bookEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + bookEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                        + bookEntry.COLUMN_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                        + bookEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                        + bookEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                        + bookEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL)";

        sqLiteDatabase.execSQL(CREATE_PRODUCT_TABLE);
        Log.d(LOG_TAG, "Products Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String DELETE_PRODUCT_TABLE =
                "DROP TABLE IF EXISTS " + bookEntry.TABLE_NAME;
        sqLiteDatabase.execSQL(DELETE_PRODUCT_TABLE);
        onCreate(sqLiteDatabase);
    }
}