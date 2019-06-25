package com.example.mylibrary;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllBooksActivity extends AppCompatActivity {

    private BookViewModel bookViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_books);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final BooksAdapter adapter = new BooksAdapter();
        recyclerView.setAdapter(adapter);

        bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        bookViewModel.getAllBooks().observe(this, new Observer<List<BookModel>>() {
            @Override
            public void onChanged(List<BookModel> booksList) {
                adapter.setBooksList(booksList);
                Log.i("AllBooksActivity", "book list size " + booksList.size());
            }
        });
    }
}
