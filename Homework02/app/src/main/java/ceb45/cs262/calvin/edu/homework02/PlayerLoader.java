package ceb45.cs262.calvin.edu.homework02;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class PlayerLoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = PlayerLoader.class.getSimpleName();
    private String queryString;

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    public PlayerLoader(@NonNull Context context, String query) {
        super(context);
        this.queryString = query;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        if (queryString.length() == 0) {
            Log.e(LOG_TAG, "getPlayerListInfo called.");
            return NetworkUtils.getPlayerListInfo(queryString);
        }
        else {
            Log.e(LOG_TAG, "getPlayerIDInfo called.");
            return NetworkUtils.getPlayerIDInfo(queryString);
        }
    }
}
