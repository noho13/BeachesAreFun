package com.normanhoeller.beachesarefun.beaches;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.normanhoeller.beachesarefun.Utils;
import com.normanhoeller.beachesarefun.network.ImageAsyncTask;

import java.lang.ref.WeakReference;

/**
 * Created by norman on 22/04/17.
 */

public class BeachModel {

    private String _id;
    private String name;
    private String url;
    private String width;
    private String height;

    public BeachModel(String _id, String name, String url, String width, String height) {
        this._id = _id;
        this.name = name;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    @BindingAdapter("android:layout_width")
    public static void setLayoutWidth(View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter(value = {"imageUrl", "memCache"}, requireAll = false)
    public static void setImageBitmap(ImageView imageView, String imageUrl, LruCache<String, Bitmap> memCache) {
        if (noOnGoingDownloadWork(imageView, imageUrl)) {
            ImageAsyncTask imageAsyncTask = new ImageAsyncTask(imageView, imageUrl, memCache);
            DrawableWrapper drawableWrapper = new DrawableWrapper(new WeakReference<>(imageAsyncTask));
            imageView.setImageDrawable(drawableWrapper);
            imageAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageUrl);
        }
    }

    private static boolean noOnGoingDownloadWork(ImageView imageView, String imageUrl) {
        ImageAsyncTask ongoingTask = getTaskForImageView(imageView);
        if (ongoingTask != null) {
            if (ongoingTask.getUrl() == null || !ongoingTask.getUrl().equalsIgnoreCase(imageUrl)) {
                ongoingTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    public static ImageAsyncTask getTaskForImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof DrawableWrapper) {
            return ((DrawableWrapper) drawable).getBitmapDownloaderTaskReference();
        }
        return null;
    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String imageUrl() {
        return Utils.getStringURL(url, null);
    }

    public int getWidthInDP() {
        return Integer.parseInt(width);
    }

    public int getHeightInDP() {
        return Integer.parseInt(height);
    }
}
