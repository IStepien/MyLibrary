package com.example.mylibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.R;
import com.github.clans.fab.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FloatingActionButton  fabAddNewBook = findViewById(R.id.fabAddNewBook);
        fabAddNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddEditBookActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton  fabShowAllBooks = findViewById(R.id.fabShowAllBooks);
        fabShowAllBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllBooksActivity.class);
                startActivity(intent);
            }
        });


    }




}
