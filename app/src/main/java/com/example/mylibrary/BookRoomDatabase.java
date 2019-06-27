package com.example.mylibrary;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mylibrary.model.BookModel;


@Database(entities = {BookModel.class}, version = 2)
public abstract class BookRoomDatabase extends RoomDatabase {
    public abstract BookDAO bookDAO();

    private static volatile BookRoomDatabase DATABASE_INSTANCE;

    public static BookRoomDatabase getDatabase(final Context context) {
        if (DATABASE_INSTANCE == null) {
            synchronized (BookRoomDatabase.class) {
                if (DATABASE_INSTANCE == null) {
                    DATABASE_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BookRoomDatabase.class, "book_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return DATABASE_INSTANCE;
    }
    public BookRoomDatabase getDatabaseInstance(){
        return DATABASE_INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(DATABASE_INSTANCE).execute();
        }

    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private BookDAO bookDAO;

        private PopulateDBAsyncTask(BookRoomDatabase db) {
            bookDAO = db.bookDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            BookModel bookModel = new BookModel();
            bookModel.setBookTitle("Example title");
            bookModel.setBookAuthor("Author");
            bookDAO.insertBook(bookModel);

            return null;
        }
    }
}