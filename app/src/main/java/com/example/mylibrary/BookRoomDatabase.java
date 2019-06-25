package com.example.mylibrary;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {BookModel.class}, version = 1)
public abstract class BookRoomDatabase extends RoomDatabase {
    public abstract BookDAO bookDAO();

    private static volatile BookRoomDatabase DATABASE_INSTANCE;

    static BookRoomDatabase getDatabase(final Context context) {
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
            bookDAO.insertBook(new BookModel("Example title", "author", "language"));

            return null;
        }
    }
}