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
package com.phantomhive.exil.hellopics.Img_Editor.Views.MainImgZoomView;

import static com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards.createCheckerboard;
import static com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards.getBitmapRect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.SelectImageView;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.SelectToolsHandel.OnSelectArea;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.X_YFixer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.Zommer;
import com.phantomhive.exil.hellopics.R;

@SuppressLint("AppCompatCustomView")
public class MainImgZoomView extends ImageView
{
    Matrix matrix = new Matrix();

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    static final int CLICK = 3;
    int mode = NONE;
    boolean mZoomCase;

    PointF last = new PointF();
    PointF start = new PointF();
    float minScale = 1f;
    float maxScale = 20f;
    float[] m;

    float redundantXSpace, redundantYSpace;
    float width, height;
    float saveScale = 1f;
    float right, bottom, origWidth, origHeight, bmWidth, bmHeight;

    ScaleGestureDetector mScaleDetector;
    Context context;

    Bitmap createCheckerboard;
    private Bitmap MainBitmap;
    Paint paint;

    public MainImgZoomView(Context context) {
        super(context);
        init(context);
    }

    public MainImgZoomView(Context context, AttributeSet attr)
    {
        super(context, attr);
        super.setClickable(true);
        init(context);


    }

    public MainImgZoomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MainImgZoomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(@NonNull Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        matrix.setTranslate(1f, 1f);
        m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (createCheckerboard != null){
            canvas.drawBitmap(createCheckerboard,matrix,null);

            canvas.drawBitmap(MainBitmap,matrix,paint);

        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        matrix.getValues(m);
        float x = m[Matrix.MTRANS_X];
        float y = m[Matrix.MTRANS_Y];
        PointF curr = new PointF(event.getX(), event.getY());

        switch (event.getAction())
        {
            //when one finger is touching
            //set the mode to DRAG
            case MotionEvent.ACTION_DOWN:
                last.set(event.getX(), event.getY());
                start.set(last);
                mode = DRAG;
                break;
            //when two fingers are touching
            //set the mode to ZOOM
            case MotionEvent.ACTION_POINTER_DOWN:
                last.set(event.getX(), event.getY());
                start.set(last);
                mode = ZOOM;
                break;
            //when a finger moves
            //If mode is applicable move image
            case MotionEvent.ACTION_MOVE:
                //if the mode is ZOOM or
                //if the mode is DRAG and already zoomed
                if (mode == ZOOM || (mode == DRAG && saveScale > minScale))
                {
                    float deltaX = curr.x - last.x;// x difference
                    float deltaY = curr.y - last.y;// y difference
                    float scaleWidth = Math.round(origWidth * saveScale);// width after applying current scale
                    float scaleHeight = Math.round(origHeight * saveScale);// height after applying current scale
                    //if scaleWidth is smaller than the views width
                    //in other words if the image width fits in the view
                    //limit left and right movement
                    if (scaleWidth < width)
                    {
                        deltaX = 0;
                        if (y + deltaY > 0)
                            deltaY = -y;
                        else if (y + deltaY < -bottom)
                            deltaY = -(y + bottom);
                    }
                    //if scaleHeight is smaller than the views height
                    //in other words if the image height fits in the view
                    //limit up and down movement
                    else if (scaleHeight < height)
                    {
                        deltaY = 0;
                        if (x + deltaX > 0)
                            deltaX = -x;
                        else if (x + deltaX < -right)
                            deltaX = -(x + right);
                    }
                    //if the image doesnt fit in the width or height
                    //limit both up and down and left and right
                    else
                    {
                        if (x + deltaX > 0)
                            deltaX = -x;
                        else if (x + deltaX < -right)
                            deltaX = -(x + right);

                        if (y + deltaY > 0)
                            deltaY = -y;
                        else if (y + deltaY < -bottom)
                            deltaY = -(y + bottom);
                    }
                    //move the image with the matrix
                    matrix.postTranslate(deltaX, deltaY);
                    //set the last touch location to the current
                    last.set(curr.x, curr.y);
                }
                break;
            //first finger is lifted
            case MotionEvent.ACTION_UP:
                mode = NONE;
                int xDiff = (int) Math.abs(curr.x - start.x);
                int yDiff = (int) Math.abs(curr.y - start.y);
                if (xDiff < CLICK && yDiff < CLICK)
                    performClick();
                break;
            // second finger is lifted
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
        }
        setImageMatrix(matrix);
        invalidate();
        return true;
    }


    @Override
    public void setImageBitmap(Bitmap bm)
    {
        super.setImageBitmap(bm);
        bmWidth = bm.getWidth();
        bmHeight = bm.getHeight();
        MainBitmap = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());
        invalidate();
    }

    public void setMaxZoom(float x)
    {
        maxScale = x;
    }

    public void setZoomCase(boolean zoomCase)
    {
        mZoomCase = zoomCase;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector)
        {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector)
        {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = saveScale;
            saveScale *= mScaleFactor;

            if (saveScale > maxScale)
            {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            }
            else if (saveScale < minScale)
            {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }
            right = width * saveScale - width - (2 * redundantXSpace * saveScale);
            bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
            if (origWidth * saveScale <= width || origHeight * saveScale <= height)
            {
                matrix.postScale(mScaleFactor, mScaleFactor, width / 2, height / 2);
                if ( mScaleFactor< 1)
                {
                    matrix.getValues(m);
                    float x = m[Matrix.MTRANS_X];
                    float y = m[Matrix.MTRANS_Y];
                    if (mScaleFactor < 1)
                    {
                        if (Math.round(origWidth * saveScale) < width)
                        {
                            if (y < -bottom)
                                matrix.postTranslate(0, -(y + bottom));
                            else if (y > 0)
                                matrix.postTranslate(0, -y);
                        }
                        else
                        {
                            if (x < -right)
                                matrix.postTranslate(-(x + right), 0);
                            else if (x > 0)
                                matrix.postTranslate(-x, 0);
                        }
                    }
                }
            }
            else
            {
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
                matrix.getValues(m);
                float x = m[Matrix.MTRANS_X];
                float y = m[Matrix.MTRANS_Y];
                if (mScaleFactor < 1) {
                    if (x < -right)
                        matrix.postTranslate(-(x + right), 0);
                    else if (x > 0)
                        matrix.postTranslate(-x, 0);
                    if (y < -bottom)
                        matrix.postTranslate(0, -(y + bottom));
                    else if (y > 0)
                        matrix.postTranslate(0, -y);
                }
            }
            return true;
        }
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);


        //Fit to screen.
        float scale;
        float scaleX =  width / bmWidth;
        float scaleY = height / bmHeight;
        scale = Math.min(scaleX, scaleY);
        matrix.setScale(scale, scale);
        setImageMatrix(matrix);
        saveScale = 1f;

        // Center the image
        redundantYSpace = height - (scale * bmHeight) ;
        redundantXSpace = width - (scale * bmWidth);
        redundantYSpace /= 2;
        redundantXSpace /= 2;

        matrix.postTranslate(redundantXSpace, redundantYSpace);

        origWidth = width - 2 * redundantXSpace;
        origHeight = height - 2 * redundantYSpace;
        right = width * saveScale - width - (2 * redundantXSpace * saveScale);
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
        setImageMatrix(matrix);

        if (MainBitmap != null) {
            if (MainBitmap.hasAlpha()) {
                createCheckerboard = createCheckerboard((int) getBitmapRect(getImageMatrix(), MainBitmap, getWidth(), getHeight()).width(),
                        (int) getBitmapRect(getImageMatrix(), MainBitmap, getWidth(), getHeight()).height(), 20);
            }
        }
    }

}