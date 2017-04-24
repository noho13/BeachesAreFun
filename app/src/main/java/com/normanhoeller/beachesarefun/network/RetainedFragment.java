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

import com.normanhoeller.beachesarefun.Utils;
import com.normanhoeller.beachesarefun.beaches.BeachModel;
import com.normanhoeller.beachesarefun.beaches.ui.BeachListFragment;
import com.normanhoeller.beachesarefun.beaches.ui.BeachesActivity;
import com.normanhoeller.beachesarefun.login.LoginActivity;
import com.normanhoeller.beachesarefun.login.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by norman on 22/04/17.
 */

public class RetainedFragment extends Fragment {

    public static final String FRAG_TAG = "network_fragment";
    private final static String TAG = RetainedFragment.class.getSimpleName();
    private LruCache<String, Bitmap> memCache;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public LruCache<String, Bitmap> getMemCache() {
        return memCache;
    }

    public void loadPageOfPictures(int page) {
        String url = Utils.getStringURL("beaches", String.valueOf(page));
        new ListAsyncTask(this).execute(String.valueOf(url));
    }

    public void setResult(List<BeachModel> beaches) {
        BeachListFragment beachListFragment = (BeachListFragment) getFragmentManager().findFragmentById(android.R.id.content);
        if (beachListFragment != null) {
            beachListFragment.setBeaches(beaches);
        }
    }

    public void postPayload(String url, String payload) {
        new LoginAsyncTask(this).execute(url, payload);
    }

    public void setLoginResult(User user) {
        if (user != null) {
            Utils.storeToken(getContext(), user.getToken());
            Intent startImages = new Intent(getActivity(), BeachesActivity.class);
            startImages.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startImages);
        }
    }

    public void logoutCurrentUser() {
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
