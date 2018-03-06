package com.vikrant.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Vikrant on 26-01-2018.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);
        TextView titleView = listItemView.findViewById(R.id.title);
        titleView.setText(currentBook.getTitle());
        TextView authorView = listItemView.findViewById(R.id.author);
        authorView.setText(currentBook.getAuthor());
        TextView publisherView = listItemView.findViewById(R.id.publisher);
        publisherView.setText(currentBook.getPublisher());
        ImageView BookImage = listItemView.findViewById(R.id.BookImage);
        View loadingIndicator = listItemView.findViewById(R.id.loading_indicator2);
        loadingIndicator.setVisibility(View.GONE);
        BookImage.setImageBitmap(currentBook.getImage());

        return listItemView;
    }

}