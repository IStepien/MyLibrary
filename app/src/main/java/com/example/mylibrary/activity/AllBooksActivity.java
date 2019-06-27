package com.example.mylibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibrary.model.BookModel;
import com.example.mylibrary.BookViewModel;
import com.example.mylibrary.BooksAdapter;
import com.example.mylibrary.R;

import java.util.List;

public class AllBooksActivity extends AppCompatActivity {

    private BookViewModel bookViewModel;
    BooksAdapter adapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_books);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new BooksAdapter();
        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);

        bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        bookViewModel.getAllBooks().observe(this, new Observer<List<BookModel>>() {
            @Override
            public void onChanged(List<BookModel> booksList) {
                adapter.setBooksList(booksList);
                Log.i("AllBooksActivity", "book list size " + booksList.size());
            }
        });

    }


    public boolean onContextItemSelected(MenuItem item) {
        int clickedItemId = item.getOrder();
        adapter.getBookAt(clickedItemId);


        switch (item.getItemId()) {
            case (R.id.detailsBook):
                Intent intent = new Intent(getApplicationContext(), BookDetailsActivity.class);
                intent.putExtra("bookId", clickedItemId);

                startActivity(intent);
                break;
            case (R.id.deleteBook):
                bookViewModel.delete(adapter.getBookAt(clickedItemId));
                Log.i("list size", " " + adapter.getItemCount());
                break;
        }
        return true;
    }
}