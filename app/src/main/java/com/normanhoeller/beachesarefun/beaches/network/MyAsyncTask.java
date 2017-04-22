package com.normanhoeller.beachesarefun.beaches.network;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.normanhoeller.beachesarefun.beaches.NetworkFragment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by norman on 22/04/17.
 */

public class MyAsyncTask extends AsyncTask<String, Void, String> {

    private final static String TAG = MyAsyncTask.class.getSimpleName();
    private final static String BASE_URL = "139.59.158.8:3000";
    private NetworkFragment fragment;

    public MyAsyncTask(NetworkFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected String doInBackground(String... strings) {
        String page = strings[0];
        return loadPageOfPictures(page);
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, s);
    }

    public String loadPageOfPictures(String page) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder
                .scheme("http")
                .encodedAuthority(BASE_URL)
                .appendPath("beaches")
                .appendQueryParameter("page", page);
        String urlAsString = uriBuilder.build().toString();
        try {
            URL url = new URL(urlAsString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return readStream(in);
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String readStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        String result = out.toString();
        reader.close();
        return result;
    }
}
