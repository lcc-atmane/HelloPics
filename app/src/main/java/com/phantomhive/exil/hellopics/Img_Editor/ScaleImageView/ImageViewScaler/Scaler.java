package com.phantomhive.exil.hellopics.Img_Editor.ScaleImageView.ImageViewScaler;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.phantomhive.exil.hellopics.Img_Editor.ScaleImageView.ImageViewScaler.handel.HeightBigerThanWidthHandel_Case1;
import com.phantomhive.exil.hellopics.Img_Editor.ScaleImageView.ImageViewScaler.handel.HeightBigerThanWidthHandle_Case2;
import com.phantomhive.exil.hellopics.Img_Editor.ScaleImageView.ImageViewScaler.handel.WidthBigerThanHeigthHandel;
import com.phantomhive.exil.hellopics.Img_Editor.ScaleImageView.ImageViewScaler.handel.WidthEqualsToHeightHandle;

/*
 Resize ImageView (Height and Width) Depending on the Height and Width ConstraintLayout
 Or any Height and Width like the Height of Screen , And with Full respect for the bitmap
 Size.
  */
public class Scaler {
        // The Height of screen.
        private static int mScreenHeight;

        // The Width of screen.
        private static int mScreenWidth;

        // The Height of ImageView.
        private static int mImageViewHeight;

        // The Width of ImageView.
        private static int mImageViewWidth;

        // The Height of ConstraintLayout.
        private static int mConstraintLayoutHeight;

        // The Width of ConstraintLayout.
        private static int mConstraintLayoutWidth;

        // The Height of Bitmap.
        private static int mBitmapHeight;

        // The Width of Bitmap.
        private static int mBitmapWidth;
        //....
        private static final HeightBigerThanWidthHandel_Case1 heightBigerThanWidthHandel_case1 = new HeightBigerThanWidthHandel_Case1();

        //.....
        private static final HeightBigerThanWidthHandle_Case2 heightBigerThanWidthHandle_case2 = new HeightBigerThanWidthHandle_Case2();

        //.....
        private static final WidthBigerThanHeigthHandel widthBigerThanHeigthHandel = new WidthBigerThanHeigthHandel();

        //.....
        private static final WidthEqualsToHeightHandle widthEqualsToHeightHandle = new WidthEqualsToHeightHandle();


         // Depending on the Height and Width ConstraintLayout
         public void ImageViewScaler (@NonNull Context context, @NonNull ImageView imageView, @NonNull ConstraintLayout constraintLayout, @NonNull Bitmap bitmap){

             // Get the Height and Width of ImageView.
             mImageViewHeight = imageView.getHeight();
             mImageViewWidth = imageView.getWidth();

             // Get the Height and Width of ConstraintLayout.
             mConstraintLayoutHeight = constraintLayout.getHeight();
             mConstraintLayoutWidth = constraintLayout.getWidth();

             // Get the Height and Width of ConstraintLayout.
             mBitmapHeight = bitmap.getHeight();
             mBitmapWidth = bitmap.getWidth();

             // Get Quotient of Height and Width of Bitmap.
             final float BitmapQuotient_H_W = (float) mBitmapHeight/(float) mBitmapWidth;

             // The new Width where Bitmap Height greater than Width.
             final float NewWidth1 = mConstraintLayoutHeight/BitmapQuotient_H_W;

             /*
             In the Case where Bitmap Height greater than Width.
             in this Case the Height of the (image) is automatically fills the Heigth.
              */
             if (mBitmapHeight>mBitmapWidth){
                 /* Treat a special case when the Width of ImageView
                    is larger than the Width of ConstraintLayout
                    Because The Width of Bitmap (image) is automatically fills the Width
                    of ImageView,even when the Bitmap (image) Height is greater
                    than Bitmap (image) Width.
                 */
                 if (NewWidth1>mConstraintLayoutWidth){
                     // Case 1.
                     //Calculate the newHeight.
                     final float NewHeight1 = mConstraintLayoutWidth*BitmapQuotient_H_W;
                     final float NewHeight2 =  Math.round(NewHeight1);
                     heightBigerThanWidthHandel_case1.NewDimensionofImageView(imageView, (int) NewHeight2,mConstraintLayoutWidth,bitmap);
                 }else {
                     // Case 2.
                      final double NewWidth2 = Math.ceil(NewWidth1);
                      heightBigerThanWidthHandle_case2.NewDimensionofImageView(imageView, (int) NewWidth2,mConstraintLayoutHeight,bitmap);
                 }

             }else {
                 // Get Quotient of Height and Width of Bitmap.
                 final float BitmapQuotient_W_H = (float) mBitmapWidth/(float) mBitmapHeight;

                 // The new Width where Bitmap Height greater than Width.
                 final float NewHeight1 = mConstraintLayoutWidth/BitmapQuotient_W_H;

                 /*
                   In the Case where Bitmap Width greater than Height.
                   in this Case the Width of the (image) is automatically fills the Width.
                    */
                 if (mBitmapWidth>mBitmapHeight){
                     final float NewHeight2 = Math.round(NewHeight1);
                     widthBigerThanHeigthHandel.NewDimensionofImageView(imageView,(int)NewHeight2,mConstraintLayoutWidth,bitmap);
                 }

                 /*
                   In the Case where Bitmap Width Equals to Height.
                   in this Case the Width of the (image) is automatically fills the Width.
                    */
             } if (mBitmapHeight==mBitmapWidth){
                   widthEqualsToHeightHandle.NewDimensionofImageView(imageView,mConstraintLayoutWidth,mConstraintLayoutWidth,bitmap);
             }

         }


    // Depending on the Height and Width Screen
    public void ImageViewScalerScreen (@NonNull Context context, @NonNull ImageView imageView, @NonNull Bitmap bitmap){
        // Get the Height and Width of Screen.
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenHeight = displayMetrics.heightPixels;
        mScreenHeight = displayMetrics.widthPixels;

        // Get the Height and Width of ImageView.
        if (imageView==null){
            return;
        }
        mImageViewHeight = imageView.getHeight();
        mImageViewWidth = imageView.getWidth();

        // Get the Height and Width of ConstraintLayout.
        if (bitmap == null){
            return;
        }
        mBitmapHeight = bitmap.getHeight();
        mBitmapWidth = bitmap.getWidth();

        // Get Quotient of Height and Width of Bitmap.
        final float BimapQuotient_H_W = (float) mBitmapHeight/(float) mBitmapWidth;

        // The new Width where Bitmap Height greater than Width.
        final float NewWidth1 = mScreenHeight/BimapQuotient_H_W;

             /*
             In the Case where Bitmap Height greater than Width.
             in this Case the Height of the (image) is automatically fills the Heigth.
              */
        if (mBitmapHeight>mBitmapWidth){
                 /*Treat a special case when the Width of ImageView
                    is larger than the Width of ConstraintLayout
                    Because The Width of Bitmap (image) is automatically fills the Width
                    of ImageView,even when the Bitmap (image) Height is greater
                    than Bitmap (image) Width.

                 */

            if (NewWidth1>mScreenWidth){
                // Case 1.
                //Calcul the newHight.
                final float NewHeight1 = mScreenWidth*BimapQuotient_H_W;
                final float NewHeight2 =  Math.round(NewHeight1);
                heightBigerThanWidthHandel_case1.NewDimensionofImageView(imageView, (int) NewHeight2,mScreenWidth,bitmap);
            }else {
                // Case 2.
                final double NewWidth2 = Math.ceil(NewWidth1);
                heightBigerThanWidthHandle_case2.NewDimensionofImageView(imageView, (int) NewWidth2,mScreenHeight,bitmap);
            }

        }else {
            // Get Quotient of Height and Width of Bitmap.
            final float BimapQuotient_W_H = (float) mBitmapWidth/(float) mBitmapHeight;

            // The new Width where Bitmap Height greater than Width.
            final float NewHeight1 = mScreenWidth/BimapQuotient_W_H;

                 /*
                   In the Case where Bitmap Width greater than Height.
                   in this Case the Width of the (image) is automatically fills the Width.
                    */
            if (mBitmapWidth>mBitmapHeight){
                final float NewHeight2 = Math.round(NewHeight1);
                widthBigerThanHeigthHandel.NewDimensionofImageView(imageView,(int)NewHeight2,mScreenWidth,bitmap);
            }

                 /*
                   In the Case where Bitmap Width Equals to Height.
                   in this Case the Width of the (image) is automatically fills the Width.
                    */
        } if (mBitmapHeight==mBitmapWidth){
            widthEqualsToHeightHandle.NewDimensionofImageView(imageView,mScreenWidth,mScreenWidth,bitmap);
        }
    }
}
