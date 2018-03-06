package com.vikrant.booklistingapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * Created by Vikrant on 26-01-2018.
 */

public class QueryUtils {
    private QueryUtils() {
    }
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<Book> fetchBooksData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Book> books = extractBooks(jsonResponse);
        return books;
    }

    private static URL createUrl(String stringurl) {
        URL url = null;
        try {
            url = new URL(stringurl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem Building the Url ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the books JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    public static List<Book> extractBooks(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }
        List<Book> books = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            JSONArray booksArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < booksArray.length(); i++) {
                JSONObject currentBook = booksArray.getJSONObject(i);
                JSONObject properties = currentBook.getJSONObject("volumeInfo");
                String title = "-----";
                try {
                    title = properties.getString("title");
                } catch (JSONException e) {
                }

                String publisher = "-----";

                try {
                    publisher = properties.getString("publisher");
                } catch (JSONException e) {
                }

                String authors = "-------";
                try {
                    JSONArray authorsArray = properties.getJSONArray("authors");
                    authors = authorsArray.get(0).toString();
                } catch (JSONException e) {
                }

                String imageString = "";
                try {
                    JSONObject imageObject = properties.getJSONObject("imageLinks");
                    imageString = imageObject.getString("smallThumbnail");
                } catch (JSONException e) {

                }

                Bitmap FullImageBitMap = BitmapFactory.decodeResource(Resources.getSystem(),
                        R.drawable.image);
                try {
                    URL url = new URL(imageString);
                    InputStream is = url.openConnection().getInputStream();
                    FullImageBitMap = BitmapFactory.decodeStream(is);


                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                books.add(new Book(title, authors, publisher, FullImageBitMap));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the books JSON results", e);
        }
        return books;
    }
}