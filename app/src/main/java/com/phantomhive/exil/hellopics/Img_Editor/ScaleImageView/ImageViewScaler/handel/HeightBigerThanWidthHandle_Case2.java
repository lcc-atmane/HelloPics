package com.phantomhive.exil.hellopics.Img_Editor.ScaleImageView.ImageViewScaler.handel;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class HeightBigerThanWidthHandle_Case2 {
    public void NewDimensionofImageView(@NonNull ImageView imageView, int NewWidth, int NewHeightDefault, @NonNull Bitmap bitmap){

        imageView.requestLayout();
        // Set the new Height and Width to The imageView.
        imageView.getLayoutParams().height = NewHeightDefault;
        imageView.getLayoutParams().width = NewWidth;
        // Set Bitmap To the ImageView.
        imageView.setImageBitmap(bitmap);
    }
}
