package com.example.mylibrary;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


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
                            .build();
                }
            }
        }
        return DATABASE_INSTANCE;
    }

    public BookRoomDatabase getDatabaseInstance(){
        return DATABASE_INSTANCE;
    }

}