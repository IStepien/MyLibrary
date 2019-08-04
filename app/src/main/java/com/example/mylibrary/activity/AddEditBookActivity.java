package com.example.mylibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mylibrary.BookRoomDatabase;
import com.example.mylibrary.R;
import com.example.mylibrary.model.BookModel;
import com.example.mylibrary.viewmodel.BookViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditBookActivity extends AppCompatActivity {

    @BindView(R.id.editTextBookTitle) EditText editTextBookTitle;
    @BindView(R.id.editTextBookAuthor) EditText editTextBookAuthor;
    @BindView(R.id.editTextBorrower) EditText editTextBorrower;
    @BindView(R.id.spinnerLanguage) Spinner spinnerLanguage;
    @BindView(R.id.checkBoxIsAlreadyRead) CheckBox checkBoxIsAlreadyRead;
    @BindView(R.id.checkBoxIsLent) CheckBox checkBoxIsLent;
    @BindView(R.id.checkBoxisOnWishList) CheckBox checkBoxIsOnWishList;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.button_add_book) Button addBookButton;
    @BindView(R.id.button_edit_book) Button editBookButton;
    @BindView(R.id.imageButtonDelete) ImageView deleteImageButton;
    @BindView(R.id.add_book_layout) ConstraintLayout addEditBookLayout;


    private BookModel newBook;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String imageFilePath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent.hasExtra("bookId")) {
            final int bookId = intent.getIntExtra("bookId", -1);


            BookViewModel bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
            bookViewModel.getAllBooks().observe(this, booksList -> {
                BookModel currentBook = booksList.stream()
                        .filter(book -> book.getBookId() == bookId)
                        .findFirst()
                        .get();
                editTextBookTitle.setText(currentBook.getBookTitle());
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.languages_array, android.R.layout.simple_spinner_dropdown_item);
                spinnerLanguage.setAdapter(adapter);
                spinnerLanguage.setSelection(adapter.getPosition(currentBook.getBookLanguage()));
                editTextBookAuthor.setText(currentBook.getBookAuthor());
                editTextBorrower.setText(currentBook.getBorrower());
                checkBoxIsAlreadyRead.setChecked(currentBook.isAlreadyRead());
                checkBoxIsLent.setChecked(currentBook.isLent());
                ratingBar.setRating(currentBook.getRating());
                checkBoxIsOnWishList.setChecked(currentBook.getIsOnWishList());

                if (currentBook.getImageURI() != null) {
                    imageView.setTag(currentBook.getImageURI());
                    Picasso.get().load(Uri.parse(currentBook.getImageURI())).fit().centerCrop().into(imageView);
                    Picasso.get().setLoggingEnabled(true);
                }
                if (!currentBook.isLent()) {
                    setVisibility(editTextBorrower);
                }
                if (!currentBook.isAlreadyRead()) {
                    setVisibility(ratingBar);
                }

                checkBoxIsAlreadyRead.setOnClickListener(v -> setVisibility(ratingBar));
                checkBoxIsLent.setOnClickListener(v -> setVisibility(editTextBorrower));

                freezeLayout(addEditBookLayout);

                setVisibility(editBookButton);
                setVisibility(deleteImageButton);

                editBookButton.setOnClickListener(v -> {
                    unFreezeLayout(addEditBookLayout);
                    editBookButton.setVisibility(View.INVISIBLE);
                });

                deleteImageButton.setOnClickListener(v -> {
                    imageView.setTag("");
                    imageView.setImageResource(android.R.color.transparent);
                });

                addBookButton.setOnClickListener(v -> {

                    Intent intent1 = getIntent();
                    Integer bookId1 = intent1.getIntExtra("bookId", -1);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("bookId", bookId1);
                    resultIntent.putExtra("updatedTitle", editTextBookTitle.getText().toString());
                    resultIntent.putExtra("updatedLanguage", spinnerLanguage.getSelectedItem().toString());
                    resultIntent.putExtra("updatedAuthor", editTextBookAuthor.getText().toString());
                    resultIntent.putExtra("updatedBorrower", editTextBorrower.getText().toString());
                    resultIntent.putExtra("updatedIsAlreadyRead", checkBoxIsAlreadyRead.isChecked());
                    resultIntent.putExtra("updatedIsLent", checkBoxIsLent.isChecked());
                    resultIntent.putExtra("updatedRating", ratingBar.getRating());
                    resultIntent.putExtra("updatedIsOnWishList", checkBoxIsOnWishList.isChecked());

                    if (imageView.getTag() != null) {
                        resultIntent.putExtra("updatedImageURI", imageView.getTag().toString());
                    }
                    setResult(RESULT_OK, resultIntent);
                    finish();

                });


            });


        } else {

            setVisibility(ratingBar);
            setVisibility(editTextBorrower);

            checkBoxIsAlreadyRead.setOnClickListener(v -> setVisibility(ratingBar));
            checkBoxIsLent.setOnClickListener(v -> setVisibility(editTextBorrower));

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_spinner_dropdown_item);
            spinnerLanguage.setAdapter(adapter);
            addBookButton.setOnClickListener(v -> saveBook());
        }
    }

    private void saveBook() {

        final String bookTitle = editTextBookTitle.getText().toString().trim();
        final String bookAuthor = editTextBookAuthor.getText().toString().trim();
        final String bookLanguage = spinnerLanguage.getSelectedItem().toString().trim();
        final String borrower = editTextBorrower.getText().toString().trim();
        final boolean isAlreadyRead = checkBoxIsAlreadyRead.isChecked();
        final float rating = ratingBar.getRating();
        final boolean isLent = checkBoxIsLent.isChecked();
        final boolean isOnWishList = checkBoxIsOnWishList.isChecked();
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
                newBook.setOnWishList(isOnWishList);
                if (bookImageURI != null) {
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

        if (intent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (imageFile != null) {
                Uri imageURI = FileProvider.getUriForFile(this, "com.example.mylibrary.provider", imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.chooser_title)), REQUEST_TAKE_PHOTO);
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            File imageFile = new File(imageFilePath);
            if (imageFile.exists()) {
                Picasso.get().load(Uri.fromFile(imageFile)).fit().centerCrop().into(imageView);
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


    private static void setVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void freezeLayout(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            if (i != layout.indexOfChild(editBookButton)) {
                View view = layout.getChildAt(i);
                view.setEnabled(false);
            }
        }
    }

    private void unFreezeLayout(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            if (i != layout.indexOfChild(editBookButton)) {
                View view = layout.getChildAt(i);
                view.setEnabled(true);
            }
        }
    }


}