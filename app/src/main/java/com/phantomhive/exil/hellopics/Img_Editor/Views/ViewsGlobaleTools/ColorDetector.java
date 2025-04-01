package com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ColorDetector {

   public static int getColor(int x,int y,Bitmap bitmap){
       if ((x >= 0 &&
               y >= 0)
               &&
               (x < bitmap.getWidth() &&
                       y < bitmap.getHeight())){
           return bitmap.getPixel(x ,y);
       }else {
           return Color.WHITE;
       }

   }

    public static Bitmap getCroppedBitmap(View view,int color) {
        Bitmap output = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10f);




        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(view.getWidth() / 2f, view.getHeight() / 2f,
                Math.min(view.getWidth() , view.getHeight() )/ 2f, paint);
        canvas.drawPoint(view.getWidth() / 2f, view.getHeight() / 2f,p);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
}
