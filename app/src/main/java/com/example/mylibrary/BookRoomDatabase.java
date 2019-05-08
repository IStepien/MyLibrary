package com.example.mylibrary;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {BookModel.class}, version = 1)
public abstract class BookRoomDatabase extends RoomDatabase {
    public abstract BookDAO bookDAO();

    private static volatile BookRoomDatabase INSTANCE;

    static BookRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BookRoomDatabase.class, "book_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}