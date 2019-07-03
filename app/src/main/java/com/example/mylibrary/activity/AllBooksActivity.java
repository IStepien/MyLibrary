package com.example.mylibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mylibrary.model.BookModel;
import com.example.mylibrary.viewmodel.BookViewModel;
import com.example.mylibrary.adapter.BooksAdapter;
import com.example.mylibrary.R;

import java.util.List;

public class AllBooksActivity extends AppCompatActivity {
    public static final int EDIT_BOOK_REQUEST = 1;

    private BookViewModel bookViewModel;
    private BooksAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_books);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
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
                adapter.notifyDataSetChanged();

            }
        });

    }


    public boolean onContextItemSelected(MenuItem item) {
        int clickedItemId = item.getOrder();
        int bookId = adapter.getBookAt(clickedItemId).getBookId();


        switch (item.getItemId()) {
            case (R.id.detailsBook):
                Intent intent = new Intent(AllBooksActivity.this, BookDetailsActivity.class);
                intent.putExtra("clickedItemId", clickedItemId);
                intent.putExtra("bookId", bookId);
                startActivityForResult(intent, EDIT_BOOK_REQUEST);
                break;
            case (R.id.deleteBook):
                bookViewModel.delete(adapter.getBookAt(clickedItemId));
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == EDIT_BOOK_REQUEST && resultCode == RESULT_OK) {
            BookModel updatedBook = new BookModel();
            Integer id = data.getIntExtra("bookId", -1);
            updatedBook.setBookId(id);
            updatedBook.setBookTitle(data.getStringExtra("updatedTitle"));
            updatedBook.setBookLanguage(data.getStringExtra("updatedLanguage"));
            updatedBook.setBookAuthor(data.getStringExtra("updatedAuthor"));
            updatedBook.setBorrower(data.getStringExtra("updatedBorrower"));
            updatedBook.setAlreadyRead( data.getBooleanExtra("updatedIsAlreadyRead",false));
            updatedBook.setLent(data.getBooleanExtra("updatedIsLent",false));
            updatedBook.setRating(data.getFloatExtra("updatedRating", 0));
            bookViewModel.update(updatedBook);

        }


    }
}