package com.example.mylibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;



import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.R;
import com.github.clans.fab.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FloatingActionButton  fabAddNewBook = findViewById(R.id.fabAddNewBook);
        fabAddNewBook.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddEditBookActivity.class);
            startActivity(intent);
        });

        FloatingActionButton  fabShowAllBooks = findViewById(R.id.fabShowAllBooks);
        fabShowAllBooks.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MyBooksActivity.class);
            startActivity(intent);
        });
        FloatingActionButton  fabShowMyWishlist = findViewById(R.id.fabShowMyWishlist);
        fabShowMyWishlist.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MyBooksActivity.class);
            intent.putExtra("Wishlist", 0);
            startActivity(intent);
            Log.i("mainActivity", "fab Wishlist clicked");
        });

    }




}
