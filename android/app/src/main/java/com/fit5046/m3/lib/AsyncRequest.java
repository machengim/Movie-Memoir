package com.fit5046.m3.lib;

import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// Used for async task.
public class AsyncRequest extends AsyncTask<Void, Void, String> {

    private static final String LOCAL_BASE_URL = "http://10.0.2.2:8080/m3/webresources/";
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String TMDB_KEY = "";  // see assets/secret;

    private OkHttpClient client;
    private String url;
    private AsyncResponse delegate;

    /***
     *
     * @param option: action type;
     * @param input: sometimes it means the methodURl, sometimes the id, depending on the action type.
     * @param delegate: the object which implements AsyncResponse interface that has invoked this request.
     */
    public AsyncRequest(int option, String input, AsyncResponse delegate) {
        this.client = new OkHttpClient();
        this.delegate = delegate;

        switch (option) {
            case 1:  // get method to localhost. input is the method url.
                url = LOCAL_BASE_URL + input; break;
            // search movies on TMDB. input is the keyword.
            case 2:
                url = TMDB_BASE_URL + "search/movie?api_key=" + TMDB_KEY + "&query=" + input; break;
            //search movie detail by id.
            case 3:
                url = TMDB_BASE_URL + "movie/" + input + "?api_key=" + TMDB_KEY; break;
            default:
                break;
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        String results = null;
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            results = Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    // Process the response by the object which implements the AsyncResponse interface.
    @Override
    protected void onPostExecute(String results) {
        delegate.processFinish(results);
    }
}
