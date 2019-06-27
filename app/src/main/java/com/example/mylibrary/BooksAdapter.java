package com.example.mylibrary;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mylibrary.model.BookModel;

import java.util.ArrayList;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder> {

    private List<BookModel> booksList = new ArrayList<>();


    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);

        return new BooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksViewHolder holder, int position) {
        holder.textViewBookTitle.setText(booksList.get(position).getBookTitle());
        holder.textViewBookAuthor.setText(booksList.get(position).getBookAuthor());

    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public void setBooksList(List<BookModel> booksList) {
        this.booksList = booksList;
        notifyDataSetChanged();
    }

    public BookModel getBookAt(int position) {
        return booksList.get(position);
    }

    class BooksViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView textViewBookTitle, textViewBookAuthor;

        public BooksViewHolder(View itemView) {
            super(itemView);

            textViewBookAuthor = itemView.findViewById(R.id.textViewBookAuthor);
            textViewBookTitle = itemView.findViewById(R.id.textViewBookTitle);
            itemView.setOnCreateContextMenuListener(this);

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("choose option");
            menu.add(0, R.id.detailsBook, getAdapterPosition(), "details");
            menu.add(0, R.id.deleteBook, getAdapterPosition(), "delete");
        }
    }

}
