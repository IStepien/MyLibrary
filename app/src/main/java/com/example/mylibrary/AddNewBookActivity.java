package com.example.mylibrary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class AddNewBookActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText editTextBookTitle, editTextBookLanguage, editTextBookAuhor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        editTextBookTitle = findViewById(R.id.editTextBookTitle);
        editTextBookLanguage = findViewById(R.id.editTextBookLanguage);
        editTextBookAuhor = findViewById(R.id.editTextBookAuthor);

        findViewById(R.id.button_add_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBook();
            }
        });
    }

    private void saveBook() {
        final String bookTitle = editTextBookTitle.getText().toString().trim();
        final String bookAuthor = editTextBookAuhor.getText().toString().trim();
        final String bookLanguage = editTextBookLanguage.getText().toString().trim();

        if (bookTitle.isEmpty() || bookAuthor.isEmpty()) {
            editTextBookTitle.setError("Field required");
            editTextBookTitle.requestFocus();
            return;
        }

        class SaveBook extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                BookModel newBook = new BookModel();
                newBook.setBookAuthor(bookAuthor);
                newBook.setBookTitle(bookTitle);
                newBook.setBookLanguage(bookLanguage);

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