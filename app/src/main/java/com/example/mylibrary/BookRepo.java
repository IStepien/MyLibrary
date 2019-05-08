package com.example.mylibrary;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepo {
    private BookDAO mBookDAO;
    private LiveData<List<BookModel>> mAllBooks;


    public BookRepo(Application application) {
        BookRoomDatabase db = BookRoomDatabase.getDatabase(application);
        mBookDAO = db.bookDAO();
        mAllBooks = db.bookDAO().getAllBooks();
    }

    LiveData<List<BookModel>> getAllBooks() {
        return mAllBooks;
    }

    public void insert(BookModel book) {
        new insertAsyncTask(mBookDAO).execute(book);
    }


    private static class insertAsyncTask extends AsyncTask<BookModel, Void, Void> {

        private BookDAO mAsyncTaskDao;

        insertAsyncTask(BookDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final BookModel... params) {
            mAsyncTaskDao.insertBook(params[0]);
            return null;
        }
    }
}
