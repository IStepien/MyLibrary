package com.example.mylibrary.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mylibrary.BookViewModel;

import com.example.mylibrary.R;
import com.example.mylibrary.model.BookModel;

import java.util.List;


public class BookDetailsActivity extends AppCompatActivity {

    BookViewModel bookViewModel;
    Integer bookID;
    private EditText editTextBookTitle, editTextBookLanguage, editTextBookAuthor, editTextBorrower;
    private CheckBox checkBoxIsAlreadyRead, checkBoxIsLend;
    private RatingBar ratingBar;
    private Button addBookButton;
    ConstraintLayout addBookLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        editTextBookTitle = findViewById(R.id.editTextBookTitle);
        editTextBookLanguage = findViewById(R.id.editTextBookLanguage);
        editTextBookAuthor = findViewById(R.id.editTextBookAuthor);

        editTextBorrower = findViewById(R.id.editTextBorrower);
        checkBoxIsAlreadyRead = findViewById(R.id.checkBoxIsAlreadyRead);
        checkBoxIsLend = findViewById(R.id.checkBoxIsLend);
        ratingBar = findViewById(R.id.ratingBar);
        addBookButton=findViewById(R.id.button_add_book);
        addBookButton.setText("edit");


        addBookLayout = findViewById(R.id.add_book_layout);
        for ( int i = 0; i < addBookLayout.getChildCount();  i++ ){
            if(i!=addBookLayout.indexOfChild(addBookButton)){
                View view = addBookLayout.getChildAt(i);
                view.setEnabled(false);
            }

        }

        Intent intent = getIntent();
        bookID = intent.getIntExtra("bookId", -1);


        bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        bookViewModel.getAllBooks().observe(this, new Observer<List<BookModel>>() {
            @Override
            public void onChanged(List<BookModel> booksList) {
               BookModel currentBook =  booksList.get(bookID);
                editTextBookTitle.setText(currentBook.getBookTitle());
                editTextBookLanguage.setText(currentBook.getBookLanguage());
                editTextBookAuthor.setText(currentBook.getBookAuthor());

                editTextBorrower.setText(currentBook.getBorrower());
                checkBoxIsAlreadyRead.setChecked(currentBook.isAlreadyRead());
                checkBoxIsLend.setChecked(currentBook.isLend());
                ratingBar.setRating(currentBook.getRating());

            }
        });

    }
}