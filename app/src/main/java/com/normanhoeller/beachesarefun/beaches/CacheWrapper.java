package com.normanhoeller.beachesarefun.beaches;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.normanhoeller.beachesarefun.network.DiscCache;

/**
 * Created by normanMedicuja on 24/04/17.
 */

public class CacheWrapper {

    private DiscCache discCache;
    private LruCache<String, Bitmap> memCache;
    private int spanWidth;

    public CacheWrapper(DiscCache discCache, LruCache<String, Bitmap> memCache, int spanWidth) {
        this.discCache = discCache;
        this.memCache = memCache;
        this.spanWidth = spanWidth;
    }

    public LruCache<String, Bitmap> getMemCache() {
        return memCache;
    }

    public DiscCache getDiscCache() {
        return discCache;
    }

    public String getSpanWidthAsString() {
        return String.valueOf(spanWidth);
    }

    public int getSpanWidth() {
        return spanWidth;
    }
}
