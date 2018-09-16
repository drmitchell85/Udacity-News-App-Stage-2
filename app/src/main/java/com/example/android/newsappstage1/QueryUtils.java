package com.example.android.newsappstage1;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final int READ_TIMEOUT_MILLISECONDS = 10000;
    private static final int CONNECT_TIMEOUT_MILLISECONDS = 15000;
    private static final int RESPONSE_CODE_200 = 200;

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Creating a private constructor
     * This class is meant to hold static variables and methods to be accessed directly from
     * the class name QueryUtiles (and an object instance will not be needed)
     */

    private QueryUtils() {

    }

    /**
     * Query the Guardian dataset and return list of objects
     * Tie all of the private methods together
     */
    public static List<Article> fetchArticleData(String requestUrl) {
        // create URL object
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        // Extract relevant fields from the JSON response and create a list
        List<Article> articles = extractFeatureFromJson(jsonResponse);

        // Return the list
        return articles;
    }

    /**
     * Returns new URL object from the given string URL
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response
     * Should include throw of IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT_MILLISECONDS /* milliseconds */);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT_MILLISECONDS /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response
            if (urlConnection.getResponseCode() == RESPONSE_CODE_200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the InputSteam into a String which contains the whole JSON response from server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of Article objects that have been built from parsed JSON response
     */
    private static ArrayList<Article> extractFeatureFromJson(String articleJSON) {
        // If the JSON String is empty / null, return early
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }


        // Create an empty ArrayList to store articles to
        ArrayList<Article> articles = new ArrayList<>();

        // Parse JSON response string
        try {
            JSONObject baseJSONResponse = new JSONObject(articleJSON);
            JSONObject responseObject = baseJSONResponse.getJSONObject("response");
            JSONArray resultsArray = responseObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject currentResults = resultsArray.getJSONObject(i);

                String articleName = currentResults.getString("webTitle");
                String articleType = currentResults.getString("pillarName");
                String articleSubject = currentResults.getString("sectionName");
                String url = currentResults.getString("webUrl");
                String date = currentResults.getString("webPublicationDate");

                JSONObject fieldsObject = currentResults.getJSONObject("fields");
                String author = fieldsObject.getString("byline");


                Article article = new Article(
                        articleName, url, articleType, articleSubject, date, author);
                articles.add(article);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the article JSON results", e);
        }

        // return list of articles
        return articles;
    }

}
























