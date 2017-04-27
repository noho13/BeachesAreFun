package com.normanhoeller.beachesarefun;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by norman on 23/04/17.
 */

public class Utils {

    public static final int REGISTER = 1;
    public static final int LOGIN = 0;
    public static final int BEACHES = 2;
    public static final int USER_INFO = 3;
    public static final int PAGE_SIZE = 6;
    private static final String TOKEN = "token";
    private static final String BASE_URL = "139.59.158.8:3000";
    public static final String START_LOADING = "start_loading";

    public static String getStringURL(@NonNull String path, @Nullable String queryParamValue) {

        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder
                .scheme("http")
                .encodedAuthority(BASE_URL)
                .appendEncodedPath(path);

        if (!TextUtils.isEmpty(queryParamValue)) {
            uriBuilder.appendQueryParameter("page", queryParamValue);
        }
        return uriBuilder.build().toString();
    }

    public static String retrieveToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return preferences.getString(Utils.TOKEN, null);
    }

    public static void storeToken(Context context, String token) {
        if (!TextUtils.isEmpty(token)) {
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.preference_file_key), MODE_PRIVATE);
            sharedPref.edit().putString(Utils.TOKEN, token).apply();
        }
    }

    public static void clearToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
