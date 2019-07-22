package com.example.mylibrary;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mylibrary.model.BookModel;

import java.util.List;

@Dao
public interface BookDAO {
    @Insert
    void insertBook(BookModel book);

    @Delete
    void deleteBook(BookModel book);

    @Query("DELETE FROM books_table")
    void deleteAllBooks();

    @Update
    void updateBook(BookModel book);

    @Query("SELECT * FROM books_table")
    LiveData<List<BookModel>> getAllBooks();

    @Query("SELECT * FROM books_table WHERE bookId=:bookId")
    LiveData<BookModel> getBookById(int bookId);

    @Query("SELECT * FROM books_table WHERE bookTitle LIKE :search")
    LiveData<List<BookModel>> getBookByTitle(String search);

    @Query("SELECT * FROM books_table WHERE bookAuthor LIKE :search")
    LiveData<List<BookModel>> getBookByAuthor(String search);
}
