package com.normanhoeller.beachesarefun.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.normanhoeller.beachesarefun.R;
import com.normanhoeller.beachesarefun.beaches.BeachListFragment;
import com.normanhoeller.beachesarefun.beaches.BeachModel;
import com.normanhoeller.beachesarefun.beaches.BeachesActivity;
import com.normanhoeller.beachesarefun.login.LoginActivity;
import com.normanhoeller.beachesarefun.login.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by norman on 22/04/17.
 */

public class NetworkFragment extends Fragment {

    public static final String FRAG_TAG = "network_fragment";
    private final static String TAG = NetworkFragment.class.getSimpleName();
    public static final String TOKEN = "token";
    public static final int PAGE_SIZE = 6;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void loadPageOfPictures(int page) {
        new ListAsyncTask(this).execute(String.valueOf(page));
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
            storeToken(user.getToken());
            Intent startImages = new Intent(getActivity(), BeachesActivity.class);
            startActivity(startImages);
            getActivity().finish();
        }
    }

    public void storeToken(String token) {
        if (!TextUtils.isEmpty(token)) {
            Context context = getActivity();
            SharedPreferences sharedPref = context.getSharedPreferences(
                    getString(R.string.preference_file_key), MODE_PRIVATE);
            sharedPref.edit().putString(TOKEN, token).apply();
        }
    }

    public String retrieveToken() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        return preferences.getString(NetworkFragment.TOKEN, null);
    }

    public void logoutCurrentUser() {
        final String token = retrieveToken();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://139.59.158.8:3000/user/logout");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    httpCon.setDoOutput(true);
                    connection.setRequestProperty(
                            "x-auth", token);
                    connection.setRequestMethod("DELETE");
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    Log.d(TAG, "responseCode: " + responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        clearSharedPrefsAndLaunchLoginActivity();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void clearSharedPrefsAndLaunchLoginActivity() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), MODE_PRIVATE);
        prefs.edit().clear().apply();

        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
