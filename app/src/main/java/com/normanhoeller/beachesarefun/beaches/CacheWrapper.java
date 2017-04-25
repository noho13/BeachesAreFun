package com.normanhoeller.beachesarefun.beaches;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * Created by normanMedicuja on 24/04/17.
 */

public class CacheWrapper {

    private LruCache<String, Bitmap> memCache;
    private int spanWidth;

    public CacheWrapper(LruCache<String, Bitmap> memCache, int spanWidth) {
        this.memCache = memCache;
        this.spanWidth = spanWidth;
    }

    public LruCache<String, Bitmap> getMemCache() {
        return memCache;
    }

    public String getSpanWidthAsString() {
        return String.valueOf(spanWidth);
    }

    public int getSpanWidth() {
        return spanWidth;
    }
}
