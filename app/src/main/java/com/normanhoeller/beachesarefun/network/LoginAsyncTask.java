package com.normanhoeller.beachesarefun.network;

import android.os.AsyncTask;
import android.util.Log;

import com.normanhoeller.beachesarefun.login.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by norman on 22/04/17.
 */

public class LoginAsyncTask extends AsyncTask<String, Void, User> {

    private RetainedFragment fragment;

    public LoginAsyncTask(RetainedFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected User doInBackground(String... strings) {
        String url = strings[0];
        String payload = strings[1];
        return post(url, payload);
    }

    @Override
    protected void onPostExecute(User user) {
        fragment.setLoginResult(user);
    }

    private User post(String urlString, String json) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setFixedLengthStreamingMode(json.length());
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Cache-Control", "no-cache");

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(out, json);

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String token = urlConnection.getHeaderField("x-auth");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return readStream(in, token);
            } else {
                InputStream in = new BufferedInputStream(urlConnection.getErrorStream());
                return readStream(in, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private void writeStream(OutputStream os, String json) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(json);
            writer.flush();
            writer.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private User readStream(InputStream in, String token) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        String result = out.toString();
        Log.d("Login", result);
        reader.close();
        return parseJson(result, token);
    }

    private User parseJson(String json, String token) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String id = jsonObject.getString("_id");
            String email = jsonObject.getString("email");
            return new User(id, email, token);
        } catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
