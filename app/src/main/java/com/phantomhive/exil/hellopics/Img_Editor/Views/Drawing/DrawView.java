/*
 * Copyright (C) 2024 HelloPics
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0
 * International License. You may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at:
 *     https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * You are free to share and adapt this work, provided that you give appropriate credit,
 * indicate if changes were made, and distribute your contributions under the same license.
 * However, you may not use this work for commercial purposes.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES, OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package com.phantomhive.exil.hellopics.Img_Editor.Views.Drawing;

import static com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards.createCheckerboard;
import static com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards.getBitmapRect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.DrawingActivity;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ColorDetector;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.X_YFixer;


import java.util.ArrayList;

public class DrawView extends View {

    String TAG= "CloneImageView ";
    ArrayList<PointF> pointF = new ArrayList<>();
    Path mPath = new Path();

    Path mPaths = new Path();

    // the Draw Path
    Path mDrawPath = new Path();
    // The Draw Path.
    Paint mDrawPaint;

    Bitmap mBitmap,b;

    Canvas mCanvas;
    Paint mPaint;

    X_YFixer xAndyFixer;
    X_YFixer DrawXAndYFixer;
    ViewZommer zommer;


    float brushSize =30;
    float hardness = 5;
    BitmapShader bitmapShader;
    Matrix matrix = new Matrix();
    boolean eraser = false;

    Bitmap bitmap;
    int mColor = Color.parseColor("#A6A061");
    boolean ColorLoc;
    Paint CircleLPaint;
    Context mContext;

    Paint bmpaint;
    Paint paint;
    PorterDuff.Mode mode;


    Paint Checkerboardpaint;
    Bitmap createCheckerboard;
    public DrawView(Context context) {
        super(context);
        init(context,null);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);init(context, attrs);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);init(context, attrs);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        mContext =  context;
        zommer = new ViewZommer(this,context);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setFilterBitmap(true);
        mPaint.setStrokeWidth(brushSize);

        /*
         // Create a bitmap with the desired brush texture
        Bitmap brushBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bruchfdgdgfsgfsgf);

        // Create a BitmapShader with the brush bitmap
        BitmapShader brushShader = new BitmapShader(brushBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mPaint.setShader(brushShader);
         */



        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));


        mDrawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDrawPaint.setColor(mColor);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setFilterBitmap(true);
        mDrawPaint.setStrokeWidth(brushSize);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);

        CircleLPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        bmpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bmpaint.setAntiAlias(true);
        bmpaint.setFilterBitmap(true);

        paint = new Paint();
        paint.setAlpha(100);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        Checkerboardpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Checkerboardpaint.setAntiAlias(true);
        Checkerboardpaint.setFilterBitmap(true);
        Checkerboardpaint.setDither(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.setMatrix(zommer.getCanvasMatrix());

        if (createCheckerboard != null){
            canvas.drawBitmap(createCheckerboard,zommer.DefaultXY().x,zommer.DefaultXY().y,null);
        }

        canvas.drawBitmap(mBitmap,zommer.getDefaultMatrix(),bmpaint);

        if (ColorLoc){

            canvas.drawCircle(DrawXAndYFixer.getFixedXYUsingMartix().x, DrawXAndYFixer.getFixedXYUsingMartix().y-(200/zommer.getScaleX()),
                        50/zommer.getScaleX(),CircleLPaint);


        }else {
            canvas.drawPath(mDrawPath, mDrawPaint);
        }


        if (eraser){
            canvas.drawBitmap(bitmap,zommer.getDefaultMatrix(),paint);
        }
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        zommer.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mBitmap.hasAlpha()){
            createCheckerboard= createCheckerboard((int) getBitmapRect(zommer.getDefaultMatrix(),bitmap,
                            getWidth(),getHeight()).width(),

                    (int) getBitmapRect(zommer.getDefaultMatrix(),bitmap,
                            getWidth(),getHeight()).height(),20);

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        xAndyFixer = new X_YFixer();
        DrawXAndYFixer = new X_YFixer();
    }


    public void setImageBitmap(Bitmap bm) {
        mBitmap = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());
        b = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        bitmapShader = new BitmapShader(b, tileMode, tileMode);
        mCanvas = new Canvas(mBitmap);
        zommer.setBitmap(bm);
    }

    public void setSourceImage(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        zommer.OnTouch(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN -> {
                if (zommer.readyToDraw()) {
                    DrawXAndYFixer.FixXYUsingMatrix(event.getX(), event.getY()
                            , zommer.getCanvasMatrix());
                    xAndyFixer.FixXYUsingMatrix(DrawXAndYFixer.getFixedXYUsingMartix().x, DrawXAndYFixer.getFixedXYUsingMartix().y
                            , zommer.getDefaultMatrix());
                    if (ColorLoc) {
                        mColor = ColorDetector.getColor((int) xAndyFixer.getFixedXYUsingMartix().x,
                                (int) xAndyFixer.getFixedXYUsingMartix().y,mBitmap);
                        CircleLPaint.setColor(mColor);

                        mPaint.setColor(mColor);
                        mDrawPaint.setColor(mColor);

                    } else {
                        mPath.reset();
                        if (hardness == 0) {
                            mPaint.setMaskFilter(null);
                            mDrawPaint.setMaskFilter(null);
                        } else {
                            mPaint.setMaskFilter(new BlurMaskFilter(hardness / zommer.getBmScaleX(), BlurMaskFilter.Blur.NORMAL));
                            mDrawPaint.setMaskFilter(new BlurMaskFilter(hardness / zommer.getScaleX(), BlurMaskFilter.Blur.NORMAL));
                        }

                        mPaint.setStrokeWidth(brushSize / zommer.getBmScaleX());
                        mDrawPaint.setStrokeWidth(brushSize / zommer.getScaleX());

                        mDrawPath.moveTo(DrawXAndYFixer.getFixedXYUsingMartix().x, DrawXAndYFixer.getFixedXYUsingMartix().y);
                        mPath.moveTo(xAndyFixer.getFixedXYUsingMartix().x, xAndyFixer.getFixedXYUsingMartix().y);

                    }
                }
                invalidate();
            }
            case MotionEvent.ACTION_MOVE -> {

                if (zommer.readyToDraw()) {
                    DrawXAndYFixer.FixXYUsingMatrix(event.getX(), event.getY()
                            , zommer.getCanvasMatrix());
                    xAndyFixer.FixXYUsingMatrix(DrawXAndYFixer.getFixedXYUsingMartix().x, DrawXAndYFixer.getFixedXYUsingMartix().y,
                            zommer.getDefaultMatrix());
                    if (ColorLoc) {
                        mColor = ColorDetector.getColor((int) xAndyFixer.getFixedXYUsingMartix().x,
                                (int) xAndyFixer.getFixedXYUsingMartix().y,mBitmap);
                        CircleLPaint.setColor(mColor);
                        mPaint.setColor(mColor);
                        mDrawPaint.setColor(mColor);
                    } else {
                        if (pointF.size() < 2) {
                            pointF.add(new PointF(xAndyFixer.getFixedXYUsingMartix().x, xAndyFixer.getFixedXYUsingMartix().y));
                            pointF.add(new PointF(DrawXAndYFixer.getFixedXYUsingMartix().x, DrawXAndYFixer.getFixedXYUsingMartix().y));
                        }
                        mPath.lineTo(xAndyFixer.getFixedXYUsingMartix().x, xAndyFixer.getFixedXYUsingMartix().y);
                        mDrawPath.lineTo(DrawXAndYFixer.getFixedXYUsingMartix().x, DrawXAndYFixer.getFixedXYUsingMartix().y);

                    }
                }

                //setImageBitmap(getFinalBitmap());
                invalidate();
            }
            case MotionEvent.ACTION_UP -> {
                DrawXAndYFixer.FixXYUsingMatrix(event.getX(), event.getY()
                        , zommer.getCanvasMatrix());
                xAndyFixer.FixXYUsingMatrix(DrawXAndYFixer.getFixedXYUsingMartix().x, DrawXAndYFixer.getFixedXYUsingMartix().y,
                        zommer.getDefaultMatrix());
                ColorLoc = false;
                ((DrawingActivity) mContext).drawLoc.setImageBitmap(
                        ColorDetector.getCroppedBitmap(
                                ((DrawingActivity) mContext).drawLoc,
                        mColor));

                if (!mPath.isEmpty()) {
                    mPaths.addPath(mPath);
                    mCanvas.drawPath(mPaths, mPaint);
                    mDrawPath.reset();
                    mPaths.reset();
                    mPath.reset();
                    if (zommer.Draw()) {
                        mDrawPath.lineTo(DrawXAndYFixer.getFixedXYUsingMartix().x, DrawXAndYFixer.getFixedXYUsingMartix().y);
                    }
                }
                invalidate();
            }
        }
        return true;
    }


    public Bitmap getFinalBitmap(){
        return mBitmap;
    }

    public void setEraser(boolean eraser){
        this.eraser =eraser;
        if (eraser){
            mPaint.setXfermode(null);
            mDrawPaint.setXfermode(null);
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            bitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
            matrix.setTranslate(0,0);
            bitmapShader.setLocalMatrix(matrix);
            mPaint.setShader(bitmapShader);
            mDrawPaint.setColor(Color.WHITE);
        }else {
            mPaint.setShader(null);
            mDrawPaint.setColor(mColor);

            if (mode == null){
                mPaint.setXfermode(null);
                mDrawPaint.setXfermode(null);
            }else {
                mPaint.setXfermode(new PorterDuffXfermode(mode));
                mDrawPaint.setXfermode(new PorterDuffXfermode(mode));
            }

            //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        }

        invalidate();
    }

    /**
     Draw the Selected path.
     **/
    private void CDrawPath(Canvas canvas) {
        canvas.drawPath(mDrawPath, mDrawPaint);

    }

    public void setDrawSize(int Size) {
        brushSize = Size;
        invalidate();
    }

    public void setDrawColor(int color) {
        mColor = color;
        mPaint.setColor(mColor);
        mDrawPaint.setColor(mColor);
        invalidate();
    }

    public void setDrawOpacity(int Opacity) {
        mPaint.setAlpha(Opacity);
        mDrawPaint.setAlpha(Opacity);
        invalidate();
    }

    public void setDrawHardness(int Hardness) {
        if (Hardness == 0){
            mPaint.setMaskFilter(null);
            mDrawPaint.setMaskFilter(null);
        }else {
            hardness = Hardness;
        }

    }

    public void setDrawMode( PorterDuff.Mode mode) {
        this.mode = mode;
        if (mode == null){
            mPaint.setXfermode(null);
            mDrawPaint.setXfermode(null);
        }else {
            mPaint.setXfermode(new PorterDuffXfermode(mode));
            mDrawPaint.setXfermode(new PorterDuffXfermode(mode));
        }
        invalidate();
    }


    public void setColorLoc(boolean b) {
        ColorLoc = b;
    }
}
