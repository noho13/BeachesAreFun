package com.normanhoeller.beachesarefun.network;

import android.os.AsyncTask;

import com.normanhoeller.beachesarefun.beaches.Beach;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by norman on 22/04/17.
 */

public class ListAsyncTask extends AsyncTask<String, Void, List<Beach>> {

    private final static String TAG = ListAsyncTask.class.getSimpleName();
    private RetainedFragment fragment;

    ListAsyncTask(RetainedFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected List<Beach> doInBackground(String... strings) {
        String urlAsString = strings[0];
        String json = loadPageOfPictures(urlAsString);
        return parseJson(json);
    }

    @Override
    protected void onPostExecute(List<Beach> beaches) {
        fragment.setResult(beaches);
    }

    private String loadPageOfPictures(String urlAsString) {
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

    private List<Beach> parseJson(String json) {
        List<Beach> beaches = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(json);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject beach = jArray.getJSONObject(i);
                String id = beach.getString("_id");
                String name = beach.getString("name");
                String url = beach.getString("url");
                String width = beach.getString("width");
                String height = beach.getString("height");
                beaches.add(new Beach(id, name, url, width, height));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beaches;
    }
}
