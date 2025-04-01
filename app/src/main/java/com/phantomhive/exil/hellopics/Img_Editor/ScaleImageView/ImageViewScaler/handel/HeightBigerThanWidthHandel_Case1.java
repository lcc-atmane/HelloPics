package com.phantomhive.exil.hellopics.Img_Editor.ScaleImageView.ImageViewScaler.handel;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class HeightBigerThanWidthHandel_Case1 {
    public void NewDimensionofImageView(@NonNull ImageView imageView, int NewHeight, int NewWidthDefault,@NonNull Bitmap bitmap){

        imageView.requestLayout();
        // Set the new Height and Width to The imageView.
        imageView.getLayoutParams().height = NewHeight;
        imageView.getLayoutParams().width = NewWidthDefault;
        // Set Bitmap To the ImageView.
        imageView.setImageBitmap(bitmap);
    }
}
