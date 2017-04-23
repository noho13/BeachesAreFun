package com.normanhoeller.beachesarefun.beaches;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.normanhoeller.beachesarefun.network.ImageAsyncTask;

import java.lang.ref.WeakReference;
import java.util.Random;

import static com.normanhoeller.beachesarefun.network.ListAsyncTask.BASE_URL;

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

    @BindingAdapter("app:imageUrl")
    public static void setImageBitmap(ImageView imageView, String imageUrl) {
        if (noOnGoingDownloadWork(imageView, imageUrl)) {
            ImageAsyncTask imageAsyncTask = new ImageAsyncTask(imageView, imageUrl);
            DrawableWrapper drawableWrapper = new DrawableWrapper(new WeakReference<>(imageAsyncTask));
            imageView.setImageDrawable(drawableWrapper);
            imageAsyncTask.execute(imageUrl);
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
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder
                .scheme("http")
                .encodedAuthority(BASE_URL)
                .appendEncodedPath(url);
        return uriBuilder.build().toString();
    }

    public int getWidthInDP() {
        int widthInPixel = Integer.parseInt(width);
//        Log.d("Model", "width: " + widthInPixel);
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthInPixel, displaymetrics);
//        Log.d("Model", "dp: " + dp);
        return widthInPixel;
    }

    public int getHeightInDP() {
        int heightInPixel = Integer.parseInt(height);
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightInPixel, displaymetrics);
        return heightInPixel;
    }

    public int getRandomBackgroundColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }


}
