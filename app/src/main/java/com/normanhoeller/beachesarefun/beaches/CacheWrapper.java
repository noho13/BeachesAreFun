package com.normanhoeller.beachesarefun.beaches;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.normanhoeller.beachesarefun.network.BitmapStore;

/**
 * Created by normanMedicuja on 24/04/17.
 */

public class CacheWrapper {

    private BitmapStore bitmapStore;
    private LruCache<String, Bitmap> memCache;
    private int spanWidth;

    public CacheWrapper(BitmapStore bitmapStore, LruCache<String, Bitmap> memCache, int spanWidth) {
        this.bitmapStore = bitmapStore;
        this.memCache = memCache;
        this.spanWidth = spanWidth;
    }

    public LruCache<String, Bitmap> getMemCache() {
        return memCache;
    }

    public BitmapStore getBitmapStore() {
        return bitmapStore;
    }

    public int getSpanWidth() {
        return spanWidth;
    }
}
