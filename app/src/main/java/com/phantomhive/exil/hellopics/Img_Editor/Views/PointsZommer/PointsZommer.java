package com.phantomhive.exil.hellopics.Img_Editor.Views.PointsZommer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


@SuppressLint("AppCompatCustomView")
public class PointsZommer extends ImageView {
    private Context mcontext;
    private Bitmap mBitmap;
    int mX,mY;
    public PointsZommer(Context context) {
        super(context);
        init(context,null);
    }

    public PointsZommer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public PointsZommer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    public PointsZommer(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        mcontext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawTouchedArea(canvas);
    }

    private void DrawTouchedArea(Canvas canvas) {
        if (getImage()!=null){
            Bitmap bitmap = Bitmap.createBitmap(getImage(),0,0,getWidth(),getHeight());
            canvas.drawBitmap(bitmap,mX,mY,null);

        }
    }

    public void setXY(int X , int Y){
        mX = X;
        mY = Y;
    }

    /**
     * Get the Cropped Oval Shape
     *
     * @return
     *
     */
    public Bitmap getImage() {
        final Drawable drawable = getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }
        //Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap output0 = ((BitmapDrawable) drawable).getBitmap();
        return output0;
    }
}

