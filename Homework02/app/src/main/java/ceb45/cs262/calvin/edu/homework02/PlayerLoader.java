package ceb45.cs262.calvin.edu.homework02;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

@SuppressWarnings("JavaDoc")
class PlayerLoader extends AsyncTaskLoader<String> {

    private static final String LOG_TAG = PlayerLoader.class.getSimpleName();
    private final String queryString;

    /**
     * No parameters
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    /**
     * @param context
     * @param query
     */
    public PlayerLoader(@NonNull Context context, String query) {
        super(context);
        this.queryString = query;
    }

    /**
     * @return
     */
    @Nullable
    @Override
    public String loadInBackground() {
        if (queryString.length() == 0) {
            Log.e(LOG_TAG, "getPlayerListInfo called.");
            return NetworkUtils.getPlayerListInfo();
        } else {
            Log.e(LOG_TAG, "getPlayerIDInfo called.");
            return NetworkUtils.getPlayerIDInfo(queryString);
        }
    }
}
