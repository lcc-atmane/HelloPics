package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity;

import android.app.Application;
import android.graphics.Bitmap;

public class Image extends Application {
    private static Image instance;  // Static instance
    private Bitmap bitmap;
    private Bitmap original;

    public static Image getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public Bitmap getOriginalBitmap() {
        return original;
    }

    public void setOriginalBitmap(Bitmap original) {
        this.original = original;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void refactor() {
        bitmap = null;
    }
}

