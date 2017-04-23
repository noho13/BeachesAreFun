package com.normanhoeller.beachesarefun;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by norman on 23/04/17.
 */

public class Utils {

    public static final int PAGE_SIZE = 6;
    private static final String TOKEN = "token";
    private static final String BASE_URL = "139.59.158.8:3000";

    public static String getStringURL(String path, String queryParamValue) {
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
}
