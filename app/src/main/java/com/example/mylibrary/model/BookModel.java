package com.example.mylibrary;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books_table")
public class BookModel {
    @PrimaryKey (autoGenerate = true)
    private int bookId;
    @NonNull
    private String bookTitle;
    @NonNull
    private String bookAuthor;
    private String bookLanguage;
    private boolean alreadyRead;
    private float rating;
    private boolean isLend;



    public BookModel(@NonNull String bookTitle, @NonNull String bookAuthor, String bookLanguage) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookLanguage = bookLanguage;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookLanguage() {
        return bookLanguage;
    }

    public void setBookLanguage(String bookLanguage) {
        this.bookLanguage = bookLanguage;
    }
}
