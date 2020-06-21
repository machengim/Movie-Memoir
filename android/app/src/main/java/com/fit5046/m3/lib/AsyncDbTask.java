package com.fit5046.m3.lib;

import android.content.Context;
import android.os.AsyncTask;

import com.fit5046.m3.entity.Watchlist;
import com.google.gson.Gson;

// AsyncTask for database operation.
public class AsyncDbTask extends AsyncTask<Void, Void, Watchlist> {

    private Database db;
    // op is used to indicate the action type (which one of CRUD).
    private int op;
    private Watchlist watchlist;
    // delegate is used to pass an object that implements the AsyncResponse interface
    // to process the callback function of async task.
    private AsyncResponse delegate;

    public AsyncDbTask(int op, Watchlist watchlist, AsyncResponse delegate,
        Context ctx) {
        db = Database.getInstance(ctx);
        this.op = op;
        this.watchlist = watchlist;
        this.delegate = delegate;
    }

    @Override
    protected Watchlist doInBackground(Void... voids) {
        switch (op) {
            case 1:
                return db.watchlistDao().findByID(watchlist.id);
            case 2:
                db.watchlistDao().insert(watchlist); break;
            case 3:
                db.watchlistDao().update(watchlist); break;
            case 4:
                db.watchlistDao().delete(watchlist); break;
            default:
                break;
        }

        return null;
    }

    // Invoke processFinish() method of requested object to handle the response.
    @Override
    protected void onPostExecute(Watchlist w) {
        String s = null;
        if (w != null) {
            s = new Gson().toJson(w);
        }

        delegate.processFinish(s);
    }

}
