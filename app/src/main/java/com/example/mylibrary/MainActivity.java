package com.example.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import com.github.clans.fab.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton fabAddNewBook, fabShowAllBooks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fabAddNewBook = findViewById(R.id.fabAddNewBook);
        fabAddNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNewBookActivity.class);
                startActivity(intent);
            }
        });

        fabShowAllBooks = findViewById(R.id.fabShowAllBooks);
        fabShowAllBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllBooksActivity.class);
                startActivity(intent);
            }
        });

    }


}
