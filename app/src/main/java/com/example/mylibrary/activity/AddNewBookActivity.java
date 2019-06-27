package com.example.mylibrary.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.model.BookModel;
import com.example.mylibrary.BookRoomDatabase;
import com.example.mylibrary.R;

public class AddNewBookActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText editTextBookTitle, editTextBookLanguage, editTextBookAuthor, editTextBorrower;
    private CheckBox checkBoxIsAlreadyRead, checkBoxIsLend;
    private RatingBar ratingBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        editTextBookTitle = findViewById(R.id.editTextBookTitle);
        editTextBookLanguage = findViewById(R.id.editTextBookLanguage);
        editTextBookAuthor = findViewById(R.id.editTextBookAuthor);

        editTextBorrower = findViewById(R.id.editTextBorrower);
        checkBoxIsAlreadyRead = findViewById(R.id.checkBoxIsAlreadyRead);
        checkBoxIsLend = findViewById(R.id.checkBoxIsLend);
        ratingBar = findViewById(R.id.ratingBar);

        findViewById(R.id.button_add_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBook();
            }
        });
    }

    private void saveBook() {
        final String bookTitle = editTextBookTitle.getText().toString().trim();
        final String bookAuthor = editTextBookAuthor.getText().toString().trim();
        final String bookLanguage = editTextBookLanguage.getText().toString().trim();
        final String borrower = editTextBorrower.getText().toString().trim();
        final boolean isAlreadyRead = checkBoxIsAlreadyRead.isChecked();
        final float rating = ratingBar.getRating();
        final boolean isLend = checkBoxIsLend.isChecked();

        if (bookTitle.isEmpty()) {
            editTextBookTitle.setError("Field required");
            editTextBookTitle.requestFocus();
            return;
        }
        if (bookAuthor.isEmpty()) {
            editTextBookAuthor.setError("Field required");
            editTextBookAuthor.requestFocus();
            return;
        }

        class SaveBook extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                BookModel newBook = new BookModel();
                newBook.setBookAuthor(bookAuthor);
                newBook.setBookLanguage(bookLanguage);
                newBook.setBookTitle(bookTitle);
                newBook.setBorrower(borrower);
                newBook.setAlreadyRead(isAlreadyRead);
                newBook.setLend(isLend);
                newBook.setRating(rating);


                BookRoomDatabase.getDatabase(getApplicationContext()).getDatabaseInstance()
                        .bookDAO()
                        .insertBook(newBook);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Book added", Toast.LENGTH_SHORT).show();
            }
        }
        SaveBook saveBook = new SaveBook();
        saveBook.execute();
    }

}