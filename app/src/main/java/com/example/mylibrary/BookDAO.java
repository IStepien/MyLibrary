package com.example.mylibrary;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBook(BookModel book);

    @Delete
    void deleteBook(BookModel book);

    @Query("DELETE FROM books_table")
    void deleteAll();

    @Update
    void update(BookModel book);

    @Query("SELECT * FROM books_table")
   LiveData<List<BookModel>> getAllBooks();

    @Query("SELECT * FROM books_table WHERE bookTitle LIKE :search")
    public List<BookModel> getBookByTitle(String search);

    @Query("SELECT * FROM books_table WHERE bookAuthor LIKE :search")
    public List<BookModel> getBookByAuthor(String search);
}
