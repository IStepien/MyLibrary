package com.example.mylibrary;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookViewModel extends AndroidViewModel {
   private BookRepo mRepo;
   private LiveData<List<BookModel>> mAllBooks;

    public BookViewModel(@NonNull Application application) {
        super(application);
        mRepo = new BookRepo(application);
        mAllBooks = mRepo.getAllBooks();
    }

    LiveData<List<BookModel>> getAllBooks() {
        return mAllBooks;
    }

    public void insert(BookModel book) {
       mRepo.insert(book);
    }

}
