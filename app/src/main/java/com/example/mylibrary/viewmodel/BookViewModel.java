package com.example.mylibrary.viewmodel;


import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.mylibrary.model.BookModel;
import com.example.mylibrary.repo.BookRepo;
import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private BookRepo mRepo;
    private LiveData<List<BookModel>> mAllBooks;
    private LiveData<List<BookModel>> mWishList;

    public BookViewModel(@NonNull Application application) {
        super(application);
        mRepo = new BookRepo(application);
        mAllBooks = mRepo.getAllBooks();

    }

    public LiveData<List<BookModel>> getAllBooks() {
        return mAllBooks;
    }

    public LiveData<BookModel> getBookById(int id) {
        return mRepo.getBookById(id);
    }

    public void insert(BookModel book) {
        mRepo.insert(book);
    }

    public void update(BookModel book) {
        mRepo.update(book);
    }

    public void delete(BookModel book) {
        mRepo.delete(book);
    }


    public void deleteAllBooks() {
        mRepo.deleteAllBooks();
    }

}
