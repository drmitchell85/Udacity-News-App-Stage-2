package com.example.android.newsappstage1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager.LoaderCallbacks;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Article>> {

    private String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String apiKey = BuildConfig.ApiKey;
    public static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?&show-fields=byline&api-key=" + apiKey;
    // OLD URL - https://content.guardianapis.com/search?q=economy&api-key

    private static final int ARTICLE_LOADER_ID = 1;

    private TextView mEmptyStateTextView;

    private ArticleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find reference to layout
        ListView articleListView = (ListView) findViewById(R.id.list);

        // Initialize adapter
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Attach adapter
        articleListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        articleListView.setEmptyView(mEmptyStateTextView);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article currentArticle = mAdapter.getItem(position);
                Uri articleUri = Uri.parse(currentArticle.getArticleUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                startActivity(websiteIntent);
            }
        });
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    /**
     * Override the three methods
     * Method 1: if LoaderManager has determined that the loader with our specified ID isn't
     * running, we should create a new one
     */

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        // ToDo: add code necessary for Preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pageSize = sharedPreferences.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default));

        String category = sharedPreferences.getString(
                getString(R.string.settings_category_key),
                getString(R.string.settings_category_default));

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("page-size", pageSize);

        // if statement required to ensure that "all" categories setting works
        if (!category.equals(getString(R.string.settings_category_default))) {
            uriBuilder.appendQueryParameter("section", category);
        }

        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        // Set empty state text to display "No earthquakes found"
        mEmptyStateTextView.setText(R.string.no_articles);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of earthquakes, then add them to the adapter's
        // data set. This will trigger the ListView to update
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Loader reset, so we can clear out our existing data
        mAdapter.clear();
    }

    /**
     * Initialize the contents of the Activity's options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate options menu specified in xml
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Setup specific action that occurs when any of the items in the Options menu are selected
     * Code Breakdown:
     * (A) passes the MenuItem that is selected
     * <p>
     * (B) An options menu may have one or more items - to determine which was selected, and what
     * action to take, call getItemId, which returns a unique ID for the menu item defined
     * by the android:id attribute in the menu resource
     * - Only one item, (android:id="@+id/action_settings"
     * <p>
     * (C) Then, match ID against known menu items to perform the appropriate action
     * - open SettingsActivity via an intent
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
