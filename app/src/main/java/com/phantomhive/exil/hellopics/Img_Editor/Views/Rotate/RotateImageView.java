package com.phantomhive.exil.hellopics.Img_Editor.Views.Rotate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class RotateImageView extends ImageView {
    Matrix matrix;
    float Rotation=0;
    float RRotation=0;
    float scaleX=1,scaleY=1;
    float SScaleX=1,SScaleY=1;

    public RotateImageView(Context context) {
        super(context);
        init(context, null);
    }

    public RotateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RotateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RotateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        matrix = new Matrix();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


     //Rotate Bitmap To Right using matrix.setRotate
    public Bitmap getRotated_right_Image() {


        final Drawable drawable = getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }

        // Get the original bitmap object.
        final Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
        Matrix matrix =new Matrix();
        Rotation = 90;
        RRotation += 90;
        SScaleX *=SScaleX;
        SScaleY *=SScaleY;
        matrix.setRotate(Rotation);
        this.matrix.postConcat(matrix);
        // Crop the subset from the original Bitmap.
        return Bitmap.createBitmap(originalBitmap,
                0,
                0,
                originalBitmap.getWidth(),
                originalBitmap.getHeight(),
                matrix, true);
    }



    //Rotate Bitmap To left using matrix.setRotate
    public Bitmap getRotated_left_Image() {
        final Drawable drawable = getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }

        // Get the original bitmap object.
        final Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
        Matrix matrix =new Matrix();
        Rotation =-90;
        RRotation -= 90;
        SScaleX *=SScaleX;SScaleY *=SScaleY;
        matrix.setRotate(Rotation);
        this.matrix.postConcat(matrix);
        // Crop the subset from the original Bitmap.
        return Bitmap.createBitmap(originalBitmap,
                0,
                0,
                originalBitmap.getWidth(),
                originalBitmap.getHeight(),
                matrix, true);
    }


    //Flip Bitmap Horizontal using matrix.setRotate
    public Bitmap getFliped_Horizontal_Image() {
        final Drawable drawable = getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }

        // Get the original bitmap object.
        final Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
        Matrix matrix =new Matrix();
        scaleX =-1;scaleY =1;
        SScaleX *=-1;SScaleY *=1;
        matrix.setScale(scaleX, scaleY);
        this.matrix.postConcat(matrix);
        // Crop the subset from the original Bitmap.
        return Bitmap.createBitmap(originalBitmap,
                0,
                0,
                originalBitmap.getWidth(),
                originalBitmap.getHeight(),
                matrix, true);
    }

    //Flip Bitmap Vertical using matrix.setRotate
    public Bitmap getFlipped_Vertical_Image() {
        final Drawable drawable = getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }

        // Get the original bitmap object.
        final Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
        Matrix matrix =new Matrix();
        scaleX =1;scaleY=-1;
        SScaleX *=1;SScaleY *=-1;
        matrix.setScale(    scaleX, scaleY);
        this.matrix.postConcat(matrix);
        // Crop the subset from the original Bitmap.
        return Bitmap.createBitmap(originalBitmap,
                0,
                0,
                originalBitmap.getWidth(),
                originalBitmap.getHeight(),
                matrix, true);
    }


    //Rotate Bitmap By degree using matrix.setRotate
    public Bitmap getRotated_Image_by_Degree(Bitmap originalBitmap,int Degree) {
        if (originalBitmap == null) {
            return null;
        }
        Matrix matrix =new Matrix();
        Rotation += Degree;
        RRotation +=Degree;
        //SScaleX *=scaleX;SScaleY *=scaleY;
        matrix.reset();
        matrix.postRotate(Rotation);
        Log.d(("TAG"), "getRotated_Image_by_Degree: "+SScaleX+" "+SScaleY);
        matrix.postScale(SScaleY,SScaleX);

        this.matrix.set(matrix);
        Bitmap bitmap = Bitmap.createBitmap(originalBitmap.getWidth(),originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(originalBitmap, 0,0,null);

        return Bitmap.createBitmap(bitmap,
                0,
                0,
                originalBitmap.getWidth(),
                originalBitmap.getHeight(),
                matrix, true);
    }

    public Bitmap getFinaleImage(Bitmap originalBitmap){

        Bitmap bitmap = Bitmap.createBitmap(originalBitmap.getWidth(),originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(originalBitmap, 0,0,null);
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(RRotation);
        matrix.postScale(SScaleX,SScaleY);

        // Crop the subset from the original Bitmap.
        return Bitmap.createBitmap(bitmap,
                0,
                0,
                originalBitmap.getWidth(),
                originalBitmap.getHeight(),
                this.matrix, true);
    }
}
