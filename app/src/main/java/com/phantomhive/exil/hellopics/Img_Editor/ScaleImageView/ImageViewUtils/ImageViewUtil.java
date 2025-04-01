package com.phantomhive.exil.hellopics.Img_Editor.ScaleImageView.ImageViewUtils;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;

/**
 * Utility class that deals with operations with an ImageView.
 */
public class ImageViewUtil {

    /**
     * Gets the rectangular position of a Bitmap if it were placed inside a View
     * with scale type set to {@link ImageView#ScaleType #CENTER_INSIDE}.
     *
     * @param bitmap the Bitmap
     * @param view the parent View of the Bitmap
     * @return the rectangular position of the Bitmap
     */
    public static RectF getBitmapRectCenterInside(Bitmap bitmap, View view) {

        final float bitmapWidth = (float) bitmap.getWidth();
        final float bitmapHeight =(float)bitmap.getHeight();
        final float viewWidth = (float)view.getWidth();
        final float viewHeight = (float)view.getHeight();

        return getBitmapRectCenterInsideHelper(bitmapWidth, bitmapHeight, viewWidth, viewHeight);
    }

    /**
     * Gets the rectangular position of a Bitmap if it were placed inside a View
     * with scale type set to {@link ImageView#ScaleType #CENTER_INSIDE}.
     *
     * @param bitmapWidth the Bitmap's width
     * @param bitmapHeight the Bitmap's height
     * @param viewWidth the parent View's width
     * @param viewHeight the parent View's height
     * @return the rectangular position of the Bitmap
     */
    public static RectF getBitmapRectCenterInside(float bitmapWidth,
                                                  float bitmapHeight,
                                                  float viewWidth,
                                                  float viewHeight)
    {
        return getBitmapRectCenterInsideHelper(bitmapWidth, bitmapHeight, viewWidth, viewHeight);
    }

    /**
     * Helper that does the work of the above functions. Gets the rectangular
     * position of a Bitmap if it were placed inside a View with scale type set
     * to {@link ImageView#ScaleType #CENTER_INSIDE}.
     *
     * @param bitmapWidth the Bitmap's width
     * @param bitmapHeight the Bitmap's height
     * @param viewWidth the parent View's width
     * @param viewHeight the parent View's height
     * @return the rectangular position of the Bitmap
     */
    private static RectF getBitmapRectCenterInsideHelper(float bitmapWidth,
                                                        float bitmapHeight,
                                                        float viewWidth,
                                                        float viewHeight) {
        double resultWidth;
        double resultHeight;
        float resultX;
        float resultY;

        double viewToBitmapWidthRatio = Double.POSITIVE_INFINITY;
        double viewToBitmapHeightRatio = Double.POSITIVE_INFINITY;

        // Checks if either width or height needs to be fixed
        if (viewWidth < bitmapWidth) {
            viewToBitmapWidthRatio = (double) viewWidth / (double) bitmapWidth;
        }
        if (viewHeight < bitmapHeight) {
            viewToBitmapHeightRatio = (double) viewHeight / (double) bitmapHeight;
        }

        // If either needs to be fixed, choose smallest ratio and calculate from
        // there
        if (viewToBitmapWidthRatio != Double.POSITIVE_INFINITY || viewToBitmapHeightRatio != Double.POSITIVE_INFINITY)
        {
            if (viewToBitmapWidthRatio <= viewToBitmapHeightRatio) {
                resultWidth = viewWidth;
                resultHeight = (bitmapHeight * resultWidth / bitmapWidth);
            }
            else {
                resultHeight = viewHeight;
                resultWidth = (bitmapWidth * resultHeight / bitmapHeight);
            }
        }
        // Otherwise, the picture is within frame layout bounds. Desired width
        // is simply picture size
        else {
            resultHeight = bitmapHeight;
            resultWidth = bitmapWidth;
        }

        // Calculate the position of the bitmap inside the ImageView.
        if (resultWidth == viewWidth) {
            resultX = 0;
            resultY =  Math.round((viewHeight - resultHeight) / 2);
        } else if (resultHeight == viewHeight) {
            resultX =  Math.round((viewWidth - resultWidth) / 2);
            resultY = 0;
        }
        else {
            resultX =  Math.round((viewWidth - resultWidth) / 2);
            resultY =  Math.round((viewHeight - resultHeight) / 2);
        }

        final RectF result = new RectF(resultX,
                resultY,
                (float) (resultX +  Math.ceil(resultWidth)),
                (float) (resultY +  Math.ceil(resultHeight)));

        return result;
    }
}
