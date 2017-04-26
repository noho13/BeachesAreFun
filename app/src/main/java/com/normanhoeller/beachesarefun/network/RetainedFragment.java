package com.normanhoeller.beachesarefun.network;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.normanhoeller.beachesarefun.BeachError;
import com.normanhoeller.beachesarefun.Callback;
import com.normanhoeller.beachesarefun.R;
import com.normanhoeller.beachesarefun.Utils;
import com.normanhoeller.beachesarefun.login.LoginActivity;
import com.normanhoeller.beachesarefun.login.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by norman on 22/04/17.
 */

public class RetainedFragment extends Fragment {

    public static final String FRAG_TAG = "retained_fragment";
    private final static String TAG = RetainedFragment.class.getSimpleName();
    private Callback callback;
    private LruCache<String, Bitmap> memCache;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public LruCache<String, Bitmap> getMemCache() {
        if (memCache == null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            memCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
        return memCache;
    }

    public void loadPageOfPictures(int page) {
        BeachRequest request = BeachRequest.createBeachRequest(Utils.BEACHES, page, "beaches");
        new BeachAsyncTask(this).execute(request);
    }

    public void setBeachResult(BeachResult result) {
        if (callback == null || result == null) {
            return;
        }
        switch (result.getResultType()) {
            case Utils.LOGIN:
            case Utils.REGISTER:
                callback.setUserResult(result.getUser());
                break;
            case Utils.BEACHES:
                callback.setBeachesResult(result.getBeachList());
                break;
            default:
                throw new IllegalArgumentException("result type not supported");
        }
    }

    public void handleError(BeachError error) {
        if (callback != null) {
            callback.setUserResult(new User(error.getErrorText()));
        }
    }

    public void postUserCredentials(BeachRequest request) {
        if (Utils.isNetworkAvailable(getContext())) {
            new BeachAsyncTask(this).execute(request);
        } else {
            handleError(new BeachError(getString(R.string.no_internet)));
        }

    }

    public void logoutCurrentUser() {
        if (!Utils.isNetworkAvailable(getContext())) {
            handleError(new BeachError(getString(R.string.no_internet)));
            return;
        }

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    URL url = new URL(Utils.getStringURL("user/logout", null));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty(
                            "x-auth", Utils.retrieveToken(getContext()));
                    connection.setRequestMethod("DELETE");
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    Log.d(TAG, "responseCode: " + responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                Context context = getContext();
                if (aBoolean) {
                    if (context != null) {
                        Utils.clearToken(context);
                        launchLoginActivity();
                    }
                }
            }
        }.execute();
    }

    private void launchLoginActivity() {
        Intent startFromNew = new Intent(getActivity(), LoginActivity.class);
        startFromNew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startFromNew);
    }
}
