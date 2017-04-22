package com.normanhoeller.beachesarefun.beaches.network;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Created by norman on 22/04/17.
 */

public class DrawableWrapper extends ColorDrawable {

    private final WeakReference<ImageAsyncTask> bitmapDownloaderTaskReference;

    public DrawableWrapper(WeakReference<ImageAsyncTask> bitmapDownloaderTaskReference) {
        this.bitmapDownloaderTaskReference = bitmapDownloaderTaskReference;
        setColor(getRandomBackgroundColor());
    }

    private int getRandomBackgroundColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }

    public ImageAsyncTask getBitmapDownloaderTaskReference() {
        return bitmapDownloaderTaskReference.get();
    }
}
