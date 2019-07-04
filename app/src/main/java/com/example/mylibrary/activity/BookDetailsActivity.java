package com.example.mylibrary.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mylibrary.R;
import com.example.mylibrary.model.BookModel;
import com.example.mylibrary.viewmodel.BookViewModel;

import java.util.List;


public class BookDetailsActivity extends AppCompatActivity {


    private EditText editTextBookTitle,  editTextBookAuthor, editTextBorrower;
    private CheckBox checkBoxIsAlreadyRead, checkBoxIsLent;
    private RatingBar ratingBar;
    private Button editBookButton, addBookButton;
    private ConstraintLayout addBookLayout;
    private BookModel currentBook;
    private Spinner spinnerLanguage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        editTextBookTitle = findViewById(R.id.editTextBookTitle);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        editTextBookAuthor = findViewById(R.id.editTextBookAuthor);
        editTextBorrower = findViewById(R.id.editTextBorrower);
        checkBoxIsAlreadyRead = findViewById(R.id.checkBoxIsAlreadyRead);
        checkBoxIsLent = findViewById(R.id.checkBoxIsLent);
        ratingBar = findViewById(R.id.ratingBar);
        addBookButton = findViewById(R.id.button_add_book);
        editBookButton = findViewById(R.id.button_edit_book);


        Intent intent = getIntent();
        final int clickedItemId = intent.getIntExtra("clickedItemId", -1);

        BookViewModel bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        bookViewModel.getAllBooks().observe(this, new Observer<List<BookModel>>() {
            @Override
            public void onChanged(@Nullable List<BookModel> booksList) {
                currentBook = booksList.get(clickedItemId);
                editTextBookTitle.setText(currentBook.getBookTitle());
                ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(getApplicationContext(), R.array.languages_array, android.R.layout.simple_spinner_dropdown_item);
                spinnerLanguage.setAdapter(adapter);
                spinnerLanguage.setSelection(adapter.getPosition(currentBook.getBookLanguage()));
                editTextBookAuthor.setText(currentBook.getBookAuthor());
                editTextBorrower.setText(currentBook.getBorrower());
                checkBoxIsAlreadyRead.setChecked(currentBook.isAlreadyRead());
                checkBoxIsLent.setChecked(currentBook.isLent());
                ratingBar.setRating(currentBook.getRating());

                if (!currentBook.isLent()) {
                    setVisibility(editTextBorrower);
                }
                if (!currentBook.isAlreadyRead()) {
                    setVisibility(ratingBar);
                }

                checkBoxIsAlreadyRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setVisibility(ratingBar);
                    }
                });
                checkBoxIsLent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setVisibility(editTextBorrower);
                    }
                });

                addBookLayout = findViewById(R.id.add_book_layout);
                for (int i = 0; i < addBookLayout.getChildCount(); i++) {
                    if (i != addBookLayout.indexOfChild(editBookButton)) {
                        View view = addBookLayout.getChildAt(i);
                        view.setEnabled(false);
                    }

                }
                setVisibility(editBookButton);

                editBookButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < addBookLayout.getChildCount(); i++) {
                            if (i != addBookLayout.indexOfChild(editBookButton)) {
                                View view = addBookLayout.getChildAt(i);
                                view.setEnabled(true);
                                editBookButton.setVisibility(View.INVISIBLE);
                            }

                        }

                    }
                });

                addBookButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = getIntent();
                        Integer bookId = intent.getIntExtra("bookId", -1);

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("bookId", bookId);
                        resultIntent.putExtra("updatedTitle", editTextBookTitle.getText().toString());
                        resultIntent.putExtra("updatedLanguage", spinnerLanguage.getSelectedItem().toString());
                        resultIntent.putExtra("updatedAuthor", editTextBookAuthor.getText().toString());
                        resultIntent.putExtra("updatedBorrower", editTextBorrower.getText().toString());
                        resultIntent.putExtra("updatedIsAlreadyRead", checkBoxIsAlreadyRead.isChecked());
                        resultIntent.putExtra("updatedIsLent", checkBoxIsLent.isChecked());
                        resultIntent.putExtra("updatedRating", ratingBar.getRating());
                        setResult(RESULT_OK, resultIntent);
                        finish();

                    }
                });


            }
        });


    }

    public static void setVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }


}
