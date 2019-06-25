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

    public LiveData<List<BookModel>> getAllBooks() {

        return mAllBooks;
    }

    public void insert(BookModel book) {
        new InsertAsyncTask(mBookDAO).execute(book);
    }
    public void update(BookModel book){
        new UpdateAsyncTask(mBookDAO).execute(book);

    }
    public void delete (BookModel book){
        new UpdateAsyncTask(mBookDAO).execute(book);

    }
    public void deleteAllBooks (){
        new DeleteAllAsyncTask(mBookDAO).execute();

    }

    private static class InsertAsyncTask extends AsyncTask<BookModel, Void, Void> {

        private BookDAO mAsyncTaskDao;

       private  InsertAsyncTask(BookDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final BookModel... books) {
            mAsyncTaskDao.insertBook(books[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<BookModel, Void, Void> {

        private BookDAO mAsyncTaskDao;

        private  UpdateAsyncTask(BookDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final BookModel... books) {
            mAsyncTaskDao.updateBook(books[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<BookModel, Void, Void> {

        private BookDAO mAsyncTaskDao;

        private  DeleteAsyncTask(BookDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final BookModel... books) {
            mAsyncTaskDao.deleteBook(books[0]);
            return null;
        }
    }
    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private BookDAO mAsyncTaskDao;

        private  DeleteAllAsyncTask(BookDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            mAsyncTaskDao.deleteAllBooks();
            return null;
        }
    }
}
