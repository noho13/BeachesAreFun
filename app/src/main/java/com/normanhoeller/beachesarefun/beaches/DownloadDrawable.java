package com.normanhoeller.beachesarefun.beaches;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.normanhoeller.beachesarefun.network.ImageAsyncTask;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Created by norman on 22/04/17.
 */

class DownloadDrawable extends ColorDrawable {

    private final WeakReference<ImageAsyncTask> bitmapDownloaderTaskReference;

    DownloadDrawable(WeakReference<ImageAsyncTask> bitmapDownloaderTaskReference) {
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

    ImageAsyncTask getBitmapDownloaderTaskReference() {
        return bitmapDownloaderTaskReference.get();
    }
}
