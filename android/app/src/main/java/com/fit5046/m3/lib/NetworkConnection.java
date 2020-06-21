package com.fit5046.m3.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//Same functionality as AsyncRequest but need to start threads manually.
public class NetworkConnection {

    // All keys are moved to assets/secret, so this file is just a sample.
    public static final String LOCAL_BASE_URL = "http://10.0.2.2:8080/m3/webresources/";
    public static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String TMDB_KEY = "";
    public static final String GOOGLE_KEY = "";
    public static final String GOOGLE_GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    public static final String TWITTER_TOKEN = "";
    public static final String TWITTER_TOKEN_SECRET = "";
    public static final String TWITTER_CONSUMER_KEY = "L3mGAlaG0Bryo0aug1YBsBVow";
    public static final String TWITTER_CONSUMER_KEY_SECRET = "";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w";

    private OkHttpClient client;
    private String url;

    public NetworkConnection() {
        this.client = new OkHttpClient();
    }

    // Used to build a complete url according to the action type and input.
    public void buildUrl(int op, String input) {
        switch (op) {
            case 1:  // get method to localhost. input is the method url.
            case 4:  // post method to localhost. input is the method url.
                url = LOCAL_BASE_URL + input; break;
            // search movies on TMDB. input is the keyword.
            case 2:
                url = TMDB_BASE_URL + "search/movie?api_key=" + TMDB_KEY + "&query=" + input; break;
            //search movie detail by id.
            case 3:
                url = TMDB_BASE_URL + "movie/" + input + "?api_key=" + TMDB_KEY; break;
            case 5:
                url = GOOGLE_GEOCODE_URL + input + "&key=" + GOOGLE_KEY; break;
            case 6:
                url = IMAGE_BASE_URL + input; break;
            default:
                break;
        }
    }

    // send request. Post requests need a serialized json object.
    public String request(String method, String json) {
        String results = null;

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();

        if (method.equals("post") && json != null) {
            RequestBody body = RequestBody.create(json.getBytes());
            request = new Request.Builder().url(url).post(body).build();
        }

        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    // Used to request images. Similar to ImageLoader class, but no need for an interface.
    public Bitmap requestImage() {
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
}
