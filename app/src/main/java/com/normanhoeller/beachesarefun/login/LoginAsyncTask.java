package com.normanhoeller.beachesarefun.login;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

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

public class LoginAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String payload = strings[1];
        return post(url, payload);
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("Login", s);
    }

    private String post(String urlString, String json) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setFixedLengthStreamingMode(json.length());
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Cache-Control", "no-cache");
//            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(out, json);

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String token = urlConnection.getHeaderField("x-auth");
                saveToken(token);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return readStream(in);
            } else {
                InputStream in = new BufferedInputStream(urlConnection.getErrorStream());
                return readStream(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
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

    private void saveToken(String token) {
        Log.d("Login", "token: " + token);
    }
}
