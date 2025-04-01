package com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class CheckerBoards {

    public static Bitmap createCheckerboard(int width, int height, int cellSize) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        for (int y = 0; y < height; y += cellSize) {
            for (int x = 0; x < width; x += cellSize) {
                if (((x / cellSize) + (y / cellSize)) % 2 == 0) {
                    paint.setColor(Color.LTGRAY); // Light gray square
                } else {
                    paint.setColor(Color.WHITE); // White square
                }
                canvas.drawRect(x, y, x + cellSize, y + cellSize, paint);
            }
        }
        return bitmap;
    }

    /*
   Returns the bitmap position inside an imageView.
   * @param imageView source ImageView
   * @return Rect position of the bitmap in the ImageView
     */
    public  static RectF getBitmapRect(Matrix mat,Bitmap dr,int w,int h)
    {
        RectF rect = new RectF();

        final Bitmap drawable = dr;
        if ( drawable == null)
        {

            return rect;
        }

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        mat.getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Bitmap d     = dr;
        final int      origW = d.getWidth();
        final int      origH = d.getHeight();

        // Calculate the actual dimensions
        final float actW = origW * scaleX;
        final float actH = origH * scaleY;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = w;
        int imgViewH = h;

        rect.top  =  (imgViewH - actH) / 2;
        rect.left =  (imgViewW - actW) / 2;

        rect.bottom = rect.top + actH;
        rect.right  = rect.left + actW;

        return rect;
    }
}
