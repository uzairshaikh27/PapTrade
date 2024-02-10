package com.example.paptrade;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WatchlistManager {
    private static final String WATCHLIST_PREF_KEY = "WatchlistPreferences"; // Updated key

    private Context context;
    private List<StockItem> watchlist;

    public WatchlistManager(Context context) {
        this.context = context;
        this.watchlist = loadWatchlist();
    }
    public void removeStockFromWatchlist(StockItem stock) {
        watchlist.remove(stock);
        saveWatchlist();

    }
    public List<StockItem> getWatchlist() {
        return watchlist;
    }

    public void addStockToWatchlist(StockItem stock) {
        watchlist.add(stock);
        saveWatchlist();
    }

    private List<StockItem> loadWatchlist() {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(WATCHLIST_PREF_KEY, Context.MODE_PRIVATE);

        String watchlistJson = sharedPreferences.getString(WATCHLIST_PREF_KEY, "");
        if (!watchlistJson.isEmpty()) {
            Type type = new TypeToken<List<StockItem>>() {}.getType();
            return new Gson().fromJson(watchlistJson, type);
        } else {
            return new ArrayList<>();
        }
    }

    public void addStock(StockItem stock) {
        List<StockItem> watchlist = getWatchlist();
        watchlist.add(stock);
        saveWatchlist();
    }

    public void saveWatchlist() {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(WATCHLIST_PREF_KEY, Context.MODE_PRIVATE); // Updated key

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WATCHLIST_PREF_KEY, new Gson().toJson(watchlist)); // Updated key
        editor.apply();
    }
}
