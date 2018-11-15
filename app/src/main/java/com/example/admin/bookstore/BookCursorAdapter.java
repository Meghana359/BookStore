package com.example.admin.bookstore;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.bookstore.R;
import com.example.admin.bookstore.Contracts.BookStoreContract.bookEntry;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.book_list_view, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final TextView mName = view.findViewById(R.id.product_name);
        TextView mPrice = view.findViewById(R.id.product_price);
        TextView mQuantity = view.findViewById(R.id.product_quantity);
        TextView mSupplierName = view.findViewById(R.id.product_supplier_name);

        Button sell = view.findViewById(R.id.sell);
        Button order = view.findViewById(R.id.order_button);

        String bookName = cursor.getString(cursor.getColumnIndexOrThrow(bookEntry.COLUMN_PRODUCT_NAME));
        String price = "Rs. " + String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(bookEntry.COLUMN_PRICE)));
        final String quantity = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(bookEntry.COLUMN_QUANTITY)));
        String supplierName = cursor.getString(cursor.getColumnIndexOrThrow(bookEntry.COLUMN_SUPPLIER_NAME));
        final String supplierPhone = cursor.getString(cursor.getColumnIndexOrThrow(bookEntry.COLUMN_SUPPLIER_PHONE_NUMBER));
        final int row_id = cursor.getInt(cursor.getColumnIndexOrThrow(bookEntry.COLUMN_ID));

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri newUri = ContentUris.withAppendedId(bookEntry.CONTENT_URI, row_id);
                if (Integer.parseInt(quantity) > 0) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(bookEntry.COLUMN_QUANTITY, Integer.parseInt(quantity) - 1);
                    context.getContentResolver().update(newUri, contentValues, null, null);
                } else {
                    Toast.makeText(context, "Out of Stock", Toast.LENGTH_SHORT).show();
                }
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + supplierPhone)));
            }
        });

        mName.setText(bookName);
        mPrice.setText(price);
        String productQuantityText = quantity + " pieces";
        mQuantity.setText(productQuantityText);
        mSupplierName.setText(supplierName);
    }
}