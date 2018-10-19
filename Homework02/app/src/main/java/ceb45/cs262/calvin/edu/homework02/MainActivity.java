package ceb45.cs262.calvin.edu.homework02;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
                implements LoaderManager.LoaderCallbacks<String> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText editTextQueryString;
    private TextView textViewSearchResults;
    private String queryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        editTextQueryString = findViewById(R.id.search_string);
        textViewSearchResults = findViewById(R.id.search_results);

        if(getSupportLoaderManager().getLoader(0)!=null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
        queryString = "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void fetch(View view) {
        queryString = editTextQueryString.getText().toString();

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;

        if (networkInfo != null && networkInfo.isConnected()) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle,this);

            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.loading_in_process);
        } else {
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.no_connection);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new PlayerLoader(this, Objects.requireNonNull(bundle).getString("queryString"));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        // If string s is empty, then connection failed.
        if (s.contains("Connection failed!")){
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.connection_failed);
            Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_SHORT).show();
            return;
        }
        if(s.length() == 0){
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.no_results_found);
            Toast.makeText(this, R.string.no_results_found, Toast.LENGTH_SHORT).show();
            return;
        }

        // obtain the JSON array of results items
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray;

            // Condition to handle getting the player list
            if(queryString.length() == 0){
                itemsArray = jsonObject.getJSONArray("items");

                Log.e(LOG_TAG, "Length of itemsArray: " + itemsArray.length());

                //Iterate through the results
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject player = itemsArray.getJSONObject(i); //Get the current item
                    String id = "id";
                    String email = "email";
                    String name = "default";

                    try {
                        id = player.getString("id");
                        email = player.getString("emailAddress");
                        name = player.getString("name");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //If information field requested exists, update the TextViews and return
                    if (id != null || email != null || name != null)  {
                        textViewSearchResults.append("\n\nid: " + id + "\n" + "email: " + email + "\n" + "name: " + name + "\n");
                    }
                    else{
                        textViewSearchResults.append("\n\nFailure to retrieve any information for this particular player!\n");
                    }
                }
                return;
            }
            // Condition to get a specific player in list
            else{
                String id = "no id found";
                String email = "no email found";
                String name = "no name found";

                try {
                    id = jsonObject.getString("id");
                    email = jsonObject.getString("emailAddress");
                    name = jsonObject.getString("name");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //If information field requested exists, update the TextViews and return
                if (id != null || email != null || name != null) {
                    textViewSearchResults.append("\n\nid: " + id + "\n" + "email: " + email + "\n" + "name: " + name + "\n");
                    return;
                }
            }

            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.display_failure);
            Toast.makeText(this, R.string.display_failure, Toast.LENGTH_SHORT).show();

        } catch (Exception ex){
            textViewSearchResults.setText("");
            textViewSearchResults.setText(R.string.json_failure);
            Toast.makeText(this, R.string.json_failure, Toast.LENGTH_SHORT).show();
            ex.printStackTrace();

        } finally{
            Log.e(LOG_TAG,"Finished query process!");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
