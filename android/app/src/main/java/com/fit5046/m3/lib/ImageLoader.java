package com.fit5046.m3.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.fit5046.m3.entity.Memoir;
import com.fit5046.m3.entity.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// Used to retrieving image from internet using AsyncTask.
public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {

    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w";

    private OkHttpClient client;
    private String url;
    // the object invoked this request and will handle the response.
    private ImageHandler delegate;
    private Movie movie;

    /***
     * URL sample: https://image.tmdb.org/t/p/w500/iZf0KyrE25z1sage4SYFLCCrMi9.jpg
     * @param movie: which movie this image belongs to.
     * @param dimen: the width of image, could be 200, 300 or 500.
     * @param delegate: the object invoked this request.
     */
    public ImageLoader(Movie movie, int dimen, ImageHandler delegate) {
        this.client = new OkHttpClient();
        this.url = IMAGE_BASE_URL + dimen + movie.getPosterPath();
        this.delegate = delegate;
        this.movie = movie;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap image = null;
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();

        try {
            Response response = client.newCall(request).execute();
            InputStream is = Objects.requireNonNull(response.body()).byteStream();
            image = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    @Override
    protected void onPostExecute(Bitmap image) {
        delegate.handleImage(image, movie);
    }
}
