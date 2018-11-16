package ceb45.cs262.calvin.edu.homework02;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings({"JavaDoc", "ReturnInsideFinallyBlock"})
class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String PLAYER_LIST_URL = "https://calvincs262-monopoly.appspot.com/monopoly/v1/players";
    private static final String PLAYER_ID_URL = "https://calvincs262-monopoly.appspot.com/monopoly/v1/player/";

    /**
     * @return
     */
    @SuppressWarnings("finally")
    static String getPlayerListInfo() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String playerListJSONString = null;

        try {
            //Build up your query URI, limiting results to 10 items and printed books
            Uri builtURI = Uri.parse(PLAYER_LIST_URL).buildUpon().build();
            URL requestURL = new URL(builtURI.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
   /* Since it's JSON, adding a newline isn't necessary (it won't affect
      parsing) but it does make debugging a *lot* easier if you print out the
      completed buffer for debugging. */
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            playerListJSONString = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Connection failed.";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (playerListJSONString != null) {
                Log.e(LOG_TAG, playerListJSONString);
                return playerListJSONString;
            } else {

                return "";
            }
        }
    }

    /**
     * @param queryString
     * @return
     */
    @SuppressWarnings("finally")
    static String getPlayerIDInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String playerIDJSONString = null;

        try {
            Uri buildURI = Uri.parse(PLAYER_ID_URL).buildUpon().appendPath(queryString).build();

            URL requestURL = new URL(buildURI.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            playerIDJSONString = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Connection has failed.";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            if (playerIDJSONString != null) {
                Log.e(LOG_TAG, playerIDJSONString);
                return playerIDJSONString;
            } else {
                return "";
            }
        }
    }
}
