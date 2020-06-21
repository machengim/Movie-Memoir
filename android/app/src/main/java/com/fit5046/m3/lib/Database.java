package com.fit5046.m3.lib;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fit5046.m3.entity.Watchlist;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Used for getting database connection.
@androidx.room.Database(entities = {Watchlist.class}, version = 2, exportSchema = false)
public abstract class Database extends RoomDatabase {

    public abstract WatchlistDao watchlistDao();
    private static Database INSTANCE;
    private static final int THREAD_NUMBER = 4;
    public static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);

    public static synchronized Database getInstance(final Context ctx) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(),
                    Database.class, "Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}
