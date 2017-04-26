package com.normanhoeller.beachesarefun.network;

import android.os.AsyncTask;

import com.normanhoeller.beachesarefun.Utils;
import com.normanhoeller.beachesarefun.beaches.Beach;
import com.normanhoeller.beachesarefun.login.User;

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
import java.util.List;

/**
 * Created by normanMedicuja on 26/04/17.
 */

public class BeachAsyncTask extends AsyncTask<BeachRequest, Void, BeachResult> implements BeachAPI {


    private RetainedFragment retainedFragment;


    public BeachAsyncTask(RetainedFragment fragment) {
        retainedFragment = fragment;
    }

    @Override
    protected BeachResult doInBackground(BeachRequest... params) {
        BeachResult result = new BeachResult();
        BeachRequest request = params[0];
        switch (request.getOperationType()) {
            case Utils.REGISTER:
            case Utils.LOGIN:
                User user = BeachParser.parseUser(postCredentials(request.getPath(), request.getPayload()));
                result.setUser(user);
                break;
            case Utils.BEACHES:
                List<Beach> beaches = BeachParser.parseBeaches(getBeaches(request.getPath(), request.getPage()));
                result.setBeachList(beaches);
                break;
            default:
                throw new IllegalArgumentException("this operation is not supported");
        }
        return result;
    }

    @Override
    protected void onPostExecute(BeachResult result) {
        retainedFragment.setBeachResult(result);
    }

    @Override
    public String getBeaches(String path, int page) {
        String urlAsString = Utils.getStringURL(path, String.valueOf(page));
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

    @Override
    public String postCredentials(String path, String payload) {
        String urlString = Utils.getStringURL(path, null);
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setFixedLengthStreamingMode(payload.length());
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Cache-Control", "no-cache");

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(out, payload);

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String token = urlConnection.getHeaderField("x-auth");
                Utils.storeToken(retainedFragment.getContext(), token);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return readStream(in);
            } else {
                InputStream in = new BufferedInputStream(urlConnection.getErrorStream());
                return readStream(in);
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

    private void writeStream(OutputStream os, String json) {
        BufferedWriter writer;
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

}
