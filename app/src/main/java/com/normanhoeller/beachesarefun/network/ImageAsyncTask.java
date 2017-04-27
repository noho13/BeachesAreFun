package com.normanhoeller.beachesarefun.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.normanhoeller.beachesarefun.beaches.Beach;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by norman on 22/04/17.
 */

public class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = ImageAsyncTask.class.getSimpleName();
    private final WeakReference<ImageView> imageViewReference;
    private String url;
    private LruCache<String, Bitmap> memoryCache;
    private DiscCache discCache;

    public ImageAsyncTask(ImageView imageView, String url, LruCache<String, Bitmap> memCache, DiscCache discCache) {
        this.imageViewReference = new WeakReference<>(imageView);
        this.url = url;
        this.memoryCache = memCache;
        this.discCache = discCache;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlAsString = strings[0];

        Bitmap bitmap = memoryCache.get(urlAsString);
        if (bitmap != null) {
            Log.d(TAG, "image from memCache");
            return bitmap;
        }
        bitmap = discCache.get(urlAsString);
        if (bitmap != null) {
            Log.d(TAG, "image from discCache");
            addBitmapToCache(urlAsString, bitmap);
            return bitmap;
        }

        Log.d(TAG, "image from network");
        bitmap = downloadImageData(urlAsString);
        if (bitmap != null) {
            Log.d(TAG, "add image to cache");
            addBitmapToCache(urlAsString, bitmap);
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                ImageAsyncTask taskPotentiallyInUse = Beach.getTaskForImageView(imageView);
                if (this == taskPotentiallyInUse) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private Bitmap downloadImageData(String urlAsString) {
        try {
            URL url = new URL(urlAsString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // no http caching since resource not cachable - bummer: max-age=0
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            // ignore here
            return null;
        }
    }

    public String getUrl() {
        return url;
    }

    public void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }

        if (!discCache.isBitmapInCache(key)) {
            discCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }
}
