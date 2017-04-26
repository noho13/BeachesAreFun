package com.normanhoeller.beachesarefun;

import android.app.Application;
import android.net.http.HttpResponseCache;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by norman on 26/04/17.
 */

public class BeachApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            File httpCacheDir = new File(getCacheDir(), "http");
            long httpCacheSize = 50 * 1024 * 1024; // 50 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i(TAG, "HTTP response cache installation failed:" + e);
        }

    }
}
