package com.normanhoeller.beachesarefun.beaches;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by normanMedicuja on 24/04/17.
 */

public class CacheWrapper {

    private LruCache<String, Bitmap> memCache;

    public CacheWrapper(LruCache<String, Bitmap> memCache) {
        this.memCache = memCache;
    }

    public LruCache<String, Bitmap> getMemCache() {
        return memCache;
    }
}
