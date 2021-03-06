package com.example.mylibrary.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibrary.model.BookModel;
import com.example.mylibrary.viewmodel.BookViewModel;
import com.example.mylibrary.adapter.BooksAdapter;
import com.example.mylibrary.R;

import java.util.List;
import java.util.stream.Collectors;

public class MyBooksActivity extends AppCompatActivity {
    private static final int EDIT_BOOK_REQUEST = 1;

    private BookViewModel bookViewModel;
    private BooksAdapter adapter;
    private Intent intent;

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
        intent = getIntent();
        bookViewModel.getAllBooks().observe(this, booksList -> {
            if (intent.hasExtra("Wishlist")) {
               List<BookModel> wishlist =  booksList.stream()
                        .filter(book-> book.getIsOnWishList())
                        .collect(Collectors.<BookModel>toList());
                adapter.setBooksList(wishlist);
            }
            else {
                List<BookModel> myBooks =  booksList.stream()
                        .filter(book-> !book.getIsOnWishList())
                        .collect(Collectors.<BookModel>toList());

                adapter.setBooksList(myBooks);
                adapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        return true;
    }

    public boolean onContextItemSelected(MenuItem item) {
        int clickedItemId = item.getOrder();
        int bookId = adapter.getBookAt(clickedItemId).getBookId();

        switch (item.getItemId()) {
            case (R.id.detailsBook):
                Intent intent = new Intent(MyBooksActivity.this, AddEditBookActivity.class);
                intent.putExtra("bookId", bookId);
                startActivityForResult(intent, EDIT_BOOK_REQUEST);
                break;
            case (R.id.deleteBook):
                new AlertDialog.Builder(this)
                        .setMessage(R.string.dialog_delete_message)
                        .setNegativeButton(R.string.dialog_delete_no, null)
                        .setPositiveButton(R.string.dialog_delete_yes, (dialog1, which) -> bookViewModel.delete(adapter.getBookAt(clickedItemId)))
                        .show();

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
            updatedBook.setAlreadyRead(data.getBooleanExtra("updatedIsAlreadyRead", false));
            updatedBook.setLent(data.getBooleanExtra("updatedIsLent", false));
            updatedBook.setRating(data.getFloatExtra("updatedRating", 0));
            updatedBook.setImageURI(data.getStringExtra("updatedImageURI"));
            updatedBook.setOnWishList(data.getBooleanExtra("updatedIsOnWishList", false));
            bookViewModel.update(updatedBook);
        }

    }

}