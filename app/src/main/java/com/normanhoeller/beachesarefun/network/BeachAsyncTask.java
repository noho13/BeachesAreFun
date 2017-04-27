package com.normanhoeller.beachesarefun.network;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.normanhoeller.beachesarefun.BeachError;
import com.normanhoeller.beachesarefun.Utils;
import com.normanhoeller.beachesarefun.beaches.Beach;
import com.normanhoeller.beachesarefun.login.User;

import org.json.JSONException;

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
        BeachRequest request = params[0];
        BeachResult result = new BeachResult(request.getOperationType());
        String urlAsString;
        switch (request.getOperationType()) {
            case Utils.REGISTER:
            case Utils.LOGIN:
                urlAsString = Utils.getStringURL(request.getPath(), null);
                setResult(postRequest(urlAsString, request.getPayload()), result);
                break;
            case Utils.BEACHES:
                urlAsString = Utils.getStringURL(request.getPath(), String.valueOf(request.getPage()));
                List<Beach> beaches = BeachParser.parseBeaches(getRequest(urlAsString, null));
                result.setBeachList(beaches);
                break;
            case Utils.USER_INFO:
                urlAsString = Utils.getStringURL(request.getPath(), null);
                setResult(getRequest(urlAsString, request.getToken()), result);
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
    public String getRequest(String urlAsString, String token) {
        try {
            URL url = new URL(urlAsString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            urlConnection.addRequestProperty("Cache-Control", "max-stale=" + maxStale);
            urlConnection.setUseCaches(true);
            if (!TextUtils.isEmpty(token)) {
                urlConnection.addRequestProperty("x-auth", token);
            }

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
    public String postRequest(String urlString, String payload) {
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

    private void setResult(String resultString, BeachResult result) {
        try {
            User user = BeachParser.parseUser(resultString);
            result.setUser(user);
        } catch (JSONException e) {
            e.printStackTrace();
            // in a real world example we would give specific error information
            result.setBeachError(new BeachError("oops, an error occurred"));
        }
    }

}
