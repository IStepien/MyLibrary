package com.example.mylibrary.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.model.BookModel;
import com.example.mylibrary.BookRoomDatabase;
import com.example.mylibrary.R;

import static com.example.mylibrary.activity.BookDetailsActivity.setVisibility;

public class AddNewBookActivity extends AppCompatActivity {

    private EditText editTextBookTitle, editTextBookAuthor, editTextBorrower;
    private CheckBox checkBoxIsAlreadyRead, checkBoxIsLent;
    private RatingBar ratingBar;
    private Spinner spinnerLanguage;
    private BookModel newBook;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);


        editTextBookTitle = findViewById(R.id.editTextBookTitle);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        editTextBookAuthor = findViewById(R.id.editTextBookAuthor);
        editTextBorrower = findViewById(R.id.editTextBorrower);
        checkBoxIsAlreadyRead = findViewById(R.id.checkBoxIsAlreadyRead);
        checkBoxIsLent = findViewById(R.id.checkBoxIsLent);
        ratingBar = findViewById(R.id.ratingBar);
        Button addNewBookButton = findViewById(R.id.button_add_book);
        setVisibility(ratingBar);
        setVisibility(editTextBorrower);

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
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);
        addNewBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBook();
            }
        });
    }

    protected void saveBook() {

        final String bookTitle = editTextBookTitle.getText().toString().trim();
        final String bookAuthor = editTextBookAuthor.getText().toString().trim();
        final String bookLanguage = spinnerLanguage.getSelectedItem().toString().trim();
        final String borrower = editTextBorrower.getText().toString().trim();
        final boolean isAlreadyRead = checkBoxIsAlreadyRead.isChecked();
        final float rating = ratingBar.getRating();
        final boolean isLent = checkBoxIsLent.isChecked();


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

                newBook = new BookModel();

                newBook.setBookAuthor(bookAuthor);
                newBook.setBookLanguage(bookLanguage);
                newBook.setBookTitle(bookTitle);
                newBook.setAlreadyRead(isAlreadyRead);
                newBook.setRating(rating);
                newBook.setLent(isLent);
                newBook.setBorrower(borrower);


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