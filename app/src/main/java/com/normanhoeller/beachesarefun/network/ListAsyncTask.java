package com.normanhoeller.beachesarefun.network;

import android.net.Uri;
import android.os.AsyncTask;

import com.normanhoeller.beachesarefun.beaches.BeachModel;

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

public class ListAsyncTask extends AsyncTask<String, Void, List<BeachModel>> {

    private final static String TAG = ListAsyncTask.class.getSimpleName();
    public final static String BASE_URL = "139.59.158.8:3000";
    private NetworkFragment fragment;

    public ListAsyncTask(NetworkFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected List<BeachModel> doInBackground(String... strings) {
        String page = strings[0];
        String json = loadPageOfPictures(page);
        return parseJson(json);
    }

    @Override
    protected void onPostExecute(List<BeachModel> beaches) {
        fragment.setResult(beaches);
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

    private List<BeachModel> parseJson(String json) {
        List<BeachModel> beaches = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(json);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject beach = jArray.getJSONObject(i);
                String id = beach.getString("_id");
                String name = beach.getString("name");
                String url = beach.getString("url");
                String width = beach.getString("width");
                String height = beach.getString("height");
                beaches.add(new BeachModel(id, name, url, width, height));
            }
            return beaches;
        } catch (JSONException e) {
            e.printStackTrace();
            return beaches;
        }
    }
}
