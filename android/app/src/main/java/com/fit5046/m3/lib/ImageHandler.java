package com.fit5046.m3.lib;

import android.graphics.Bitmap;

import com.fit5046.m3.entity.Movie;

// Used for async task of retrieving image from internet.
// Same with AsyncReponse interface, but its parameter is Bitmap.
public interface ImageHandler {

    void handleImage(Bitmap image, Movie movie);
}
