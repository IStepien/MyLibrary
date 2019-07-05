package com.example.mylibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.mylibrary.model.BookModel;
import com.example.mylibrary.BookRoomDatabase;
import com.example.mylibrary.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.mylibrary.activity.BookDetailsActivity.setVisibility;

public class AddNewBookActivity extends AppCompatActivity {

    private EditText editTextBookTitle, editTextBookAuthor, editTextBorrower;
    private CheckBox checkBoxIsAlreadyRead, checkBoxIsLent;
    private RatingBar ratingBar;
    private Spinner spinnerLanguage;
    private ImageView imageView;
    private BookModel newBook;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String imageFilePath;


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
        imageView = findViewById(R.id.imageView);
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_spinner_dropdown_item);
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
        final Object bookImageURI = imageView.getTag();


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
                if(bookImageURI!=null) {
                    newBook.setImageURI(bookImageURI.toString());
                }

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

    public void addImage(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null){
            File imageFile =null;
            try {
                imageFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(imageFile!=null){
                Uri imageURI = FileProvider.getUriForFile(this, "com.example.mylibrary.provider", imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);}
            }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            File imageFile = new File(imageFilePath);
            if(imageFile.exists()){
                imageView.setImageURI(Uri.fromFile(imageFile));
                imageView.setTag(Uri.fromFile(imageFile));
            }


        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();

        return image;
    }
}