package com.normanhoeller.beachesarefun.beaches.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.normanhoeller.beachesarefun.beaches.BeachModel;

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

    public ImageAsyncTask(ImageView imageView, String url) {
        this.imageViewReference = new WeakReference<>(imageView);
        this.url = url;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlAsString = strings[0];
        return downloadImageData(urlAsString);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                ImageAsyncTask taskPotentiallyInUse = BeachModel.getTaskForImageView(imageView);
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
}
