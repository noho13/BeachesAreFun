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

    private final WeakReference<ImageView> imageViewReference;
    private String url;
    private LruCache<String, Bitmap> memoryCache;

    public ImageAsyncTask(ImageView imageView, String url, LruCache<String, Bitmap> memCache) {
        this.imageViewReference = new WeakReference<>(imageView);
        this.url = url;
        this.memoryCache = memCache;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlAsString = strings[0];
        Bitmap bitmap = memoryCache.get(urlAsString);
        if (bitmap != null) {
            return bitmap;
        }
        bitmap = downloadImageData(urlAsString);
        addBitmapToMemoryCache(urlAsString, bitmap);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            return;
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
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return BitmapFactory.decodeStream(in);
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUrl() {
        return url;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }
}
