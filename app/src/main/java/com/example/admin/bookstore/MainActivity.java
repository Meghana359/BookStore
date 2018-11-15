package com.example.admin.bookstore;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.bookstore.R;
import com.example.admin.bookstore.BookCursorAdapter;
import com.example.admin.bookstore.Contracts.BookStoreContract.bookEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private BookCursorAdapter bookCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        ListView booksList = findViewById(R.id.products_list);
        TextView emptyView = findViewById(R.id.empty_text_view);
        booksList.setEmptyView(emptyView);

        bookCursorAdapter = new BookCursorAdapter(this, null);
        booksList.setAdapter(bookCursorAdapter);
        getLoaderManager().initLoader(1, null, this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InsertActivity.class));
            }
        });

        booksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getApplicationContext(), InsertActivity.class)
                        .setData(ContentUris.withAppendedId(bookEntry.CONTENT_URI, l)));
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {
                bookEntry.COLUMN_ID,
                bookEntry.COLUMN_PRODUCT_NAME,
                bookEntry.COLUMN_PRICE,
                bookEntry.COLUMN_QUANTITY,
                bookEntry.COLUMN_SUPPLIER_NAME,
                bookEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };

        return new CursorLoader(
                this,
                bookEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        bookCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        bookCursorAdapter.swapCursor(null);
    }
}