package com.vikrant.booklistingapp;

import android.graphics.Bitmap;

/**
 * Created by Vikrant on 26-01-2018.
 */

public class Book {
    private String mTitle;
    private String mAuthor;
    private String mPublisher;
    private Bitmap mFullImage;

    public Book(String Title, String Author, String Publisher, Bitmap FullImage) {
        mTitle = Title;
        mAuthor = Author;
        mPublisher = Publisher;
        mFullImage = FullImage;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public Bitmap getImage() {
        return mFullImage;
    }


}
