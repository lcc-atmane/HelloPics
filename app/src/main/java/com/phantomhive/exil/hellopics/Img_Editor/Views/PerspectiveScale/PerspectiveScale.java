package com.phantomhive.exil.hellopics.Img_Editor.Views.PerspectiveScale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.phantomhive.exil.hellopics.Img_Editor.Views.Cropper.edge.Edge;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.Zommer;


public class PerspectiveScale extends View {
    Bitmap Originalbitmap;
    Matrix matrix;
    Matrix RealMatrix;
    Paint paint;
    int width,height;
    float bmWidth,bmHeight;
    float redundantXSpace,redundantYSpace;
    float scale;

    ViewZommer viewZommer;
    Paint Rectpaint;
     Paint Linespaint;
     int gridSize = 10;

    public PerspectiveScale(Context context) {
        super(context);
        init(context, null);
    }

    public PerspectiveScale(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PerspectiveScale(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public PerspectiveScale(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init (Context context, AttributeSet attrs) {
        viewZommer = new ViewZommer(this,context);

        Rectpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Rectpaint.setStrokeWidth(7);
        Rectpaint.setAlpha(125);
        Rectpaint.setStyle(Paint.Style.STROKE);
        Rectpaint.setColor(Color.parseColor("#A6A061"));

        matrix = new Matrix();
        RealMatrix = new Matrix();

        Linespaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Linespaint.setColor(Color.parseColor("#A6A061"));
        Linespaint.setStrokeWidth(2);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewZommer.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = View.MeasureSpec.getSize(widthMeasureSpec);
        height = View.MeasureSpec.getSize(heightMeasureSpec);

        //Fit to screen.
        float scaleX =  width / bmWidth;
        float scaleY = height / bmHeight;
        scale = Math.min(scaleX, scaleY);
        matrix.setScale(scale, scale);
        RealMatrix.setScale(1,1);

        // Center the image
        redundantYSpace = height - (scale * bmHeight) ;
        redundantXSpace = width - (scale * bmWidth);
        redundantYSpace /= 2;
        redundantXSpace /= 2;
        matrix.postTranslate(redundantXSpace, redundantYSpace);
        RealMatrix.postTranslate(0, 0);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Originalbitmap!=null){
            canvas.drawBitmap(Originalbitmap,matrix,paint);
            canvas.drawRect(getBitmapRect(),Rectpaint);

            // Calculate cell size
            float cellWidth = getBitmapRect().width() / gridSize;
            float cellHeight = getBitmapRect().height() / gridSize;

            // Draw vertical lines
            for (int i = 1; i < gridSize; i++) {
                float x = getBitmapRect().left + i * cellWidth;
                canvas.drawLine(x, getBitmapRect().top, x, getBitmapRect().bottom, Linespaint);
            }

            // Draw horizontal lines
            for (int i = 1; i < gridSize; i++) {
                float y = getBitmapRect().top + i * cellHeight;
                canvas.drawLine(getBitmapRect().left, y, getBitmapRect().right, y, Linespaint);
            }
        }


    }
    public void setImageBitmap(Bitmap bm) {
        viewZommer.setBitmap(bm);
        Originalbitmap = bm;
        bmWidth = bm.getWidth();
        bmHeight = bm.getHeight();
        invalidate();
    }

    public void TiltBitmap(int widthRight,int widthLeft,int heightBottom, int heightTop){

        float[] src = {0, 0,
                Originalbitmap.getWidth(), 0,
                Originalbitmap.getWidth(), Originalbitmap.getHeight(),
                0, Originalbitmap.getHeight()};



        float[] dst = {  heightTop*-1,  widthLeft *-1, // top left
                Originalbitmap.getWidth()-heightTop*-1, widthRight *-1, // top right
                Originalbitmap.getWidth()-heightBottom*-1, Originalbitmap.getHeight() - widthRight *-1, // bottom right
                heightBottom*-1, Originalbitmap.getHeight()- widthLeft *-1 // bottom left
        };

        matrix.reset();
        matrix.setPolyToPoly(src,0,dst,0,4);
        matrix.postScale(scale,scale);
        matrix.postTranslate(redundantXSpace, redundantYSpace);

        RealMatrix.reset();
        RealMatrix.setPolyToPoly(src,0,dst,0,4);
        RealMatrix.postScale(1,1);
        RealMatrix.postTranslate(0, 0);

        // Calculate scaling factor based on tilt
        float scaleX = Math.max(1, (Originalbitmap.getWidth() + Math.abs(widthLeft) + Math.abs(widthRight)) / Originalbitmap.getWidth());
        float scaleY = Math.max(1, (Originalbitmap.getHeight() + Math.abs(heightTop) + Math.abs(heightBottom)) / Originalbitmap.getHeight());
        float scale = Math.max(scaleX, scaleY);

        invalidate();

    }
    public  final RectF getBitmapRect()
    {
        RectF rect = new RectF();

        if ( Originalbitmap == null)
        {
            return rect;
        }

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        viewZommer.getDefaultMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)

        final int      origW = Originalbitmap.getWidth();
        final int      origH = Originalbitmap.getHeight();

        // Calculate the actual dimensions
        final float actW = origW * scaleX;
        final float actH = origH * scaleY;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = getWidth();
        int imgViewH = getHeight();

        rect.top  =  (imgViewH - actH) / 2;
        rect.left =  (imgViewW - actW) / 2;

        rect.bottom = rect.top + actH;
        rect.right  = rect.left + actW;

        return rect;
    }
    public Bitmap getFinalBitmap(){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        Bitmap bitmap = Bitmap.createBitmap(Originalbitmap.getWidth(),Originalbitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(Originalbitmap,RealMatrix,paint);

        return bitmap;
    }
}
