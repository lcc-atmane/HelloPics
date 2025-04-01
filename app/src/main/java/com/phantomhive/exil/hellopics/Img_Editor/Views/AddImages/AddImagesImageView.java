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
package com.phantomhive.exil.hellopics.Img_Editor.Views.AddImages;

import static com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards.createCheckerboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.AddPhotoActivity;
import com.phantomhive.exil.hellopics.Img_Editor.Views.AddImages.ImagesHandle.Images;
import com.phantomhive.exil.hellopics.Img_Editor.Views.AddImages.ImagesHandle.ImagesMatrix;
import com.phantomhive.exil.hellopics.Img_Editor.Views.AddImages.ImagesHandle.ImagesPaints;
import com.phantomhive.exil.hellopics.Img_Editor.Views.AddImages.ImagesHandle.ImagesPosition;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ColorDetector;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.X_YFixer;
import com.phantomhive.exil.hellopics.R;

import java.util.ArrayList;


public class AddImagesImageView extends View {

    private float DownX, DownY, X, Y,
            DownRealTimeX, DownRealTimeY, RealTimeX, RealTimeY;;
    private DrawType drawType;
    private Bitmap mBitmap;
    private Context mContext;

    // the RightX and BottomY of The Position of Images Bitmap in the View.
    float rightCopy, bottomCopy;
    RectF RectDst;

    boolean ShowCadre;

    boolean mRotate;
    boolean mScale;

    float mScaleSource;

    Bitmap Rt, Rz, Cs, Cp;
    ClickCircles mClickCircles;

    ViewZommer zommer;
    X_YFixer mxAndyFixer;

    ImagesMatrix ImagesMatrix = new ImagesMatrix();
    Images Images = new Images();
    ImagesPaints ImagesPaints = new ImagesPaints();
    ImagesPosition ImagesPosition = new ImagesPosition();


    Canvas EraseCanvas;
    Canvas rEraseCanvas;
    Paint ErasePaint;
    private int ErasePaintOpacity = 255;
    private float ErasePaintSTROKEWIDTH = 30f;
    private float ErasePaintHardness = 45f;
    Paint VErasePaint;
    boolean EraseMode = false;

    boolean Restore = true;
    Path ErasePath;
    Path rErasePath;
    Path VErasePath;
    Bitmap EraseBm;
    Bitmap rEraseBm;
    boolean ColorLoc;
    Integer mColor;
    Paint CircleLPaint;

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    ScaleGestureDetector mScaleDetector;


    Paint Checkerboardpaint;
    Bitmap createCheckerboard;
    public AddImagesImageView(Context context) {
        super(context);
        init(context);
    }

    public AddImagesImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddImagesImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AddImagesImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        Rt = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rotate_ic);
        Rz = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.resize_ic);
        Cs = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.closeimg_ic);
        Cp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.copycopy_ic);


        zommer = new ViewZommer(this, context);
        mxAndyFixer = new X_YFixer();


        ErasePath = new Path();
        rErasePath = new Path();
        VErasePath = new Path();
        // mBrushPaint
        ErasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        ErasePaint.setAntiAlias(true);
        ErasePaint.setStrokeWidth(ErasePaintSTROKEWIDTH);
        ErasePaint.setStyle(Paint.Style.STROKE);
        ErasePaint.setStrokeJoin(Paint.Join.ROUND);
        ErasePaint.setStrokeCap(Paint.Cap.ROUND);

        VErasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        VErasePaint.setColor(Color.parseColor("#A6A061"));
        VErasePaint.setAntiAlias(true);
        VErasePaint.setAlpha(127);
        VErasePaint.setStrokeWidth(ErasePaintSTROKEWIDTH);
        VErasePaint.setStyle(Paint.Style.STROKE);
        VErasePaint.setStrokeJoin(Paint.Join.ROUND);
        VErasePaint.setStrokeCap(Paint.Cap.ROUND);

        CircleLPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Checkerboardpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Checkerboardpaint.setAntiAlias(true);
        Checkerboardpaint.setFilterBitmap(true);
        Checkerboardpaint.setDither(true);
    }


    public void setImageBitmap(Bitmap bm) {
        mBitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
        zommer.setBitmap(bm);
    }

    public Bitmap getImageBitmap() {
        return mBitmap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        zommer.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mBitmap.hasAlpha()){
            createCheckerboard= createCheckerboard((int) CheckerBoards.getBitmapRect(zommer.getDefaultMatrix(),mBitmap,
                            getWidth(),getHeight()).width(),

                    (int) CheckerBoards.getBitmapRect(zommer.getDefaultMatrix(),mBitmap,
                            getWidth(),getHeight()).height(),20);

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setMatrix(zommer.getCanvasMatrix());
        if (createCheckerboard != null){
            canvas.drawBitmap(createCheckerboard,zommer.DefaultXY().x,zommer.DefaultXY().y,null);
        }

        canvas.drawBitmap(mBitmap,zommer.getDefaultMatrix(),Checkerboardpaint);
        DrawMergeAndAddedImages(canvas);

        DrawImagesCadre(canvas);

        if (ColorLoc){
            canvas.drawCircle(mxAndyFixer.getFixedXYUsingMartix().x,
                    mxAndyFixer.getFixedXYUsingMartix().y-(200/zommer.getScaleX()),
                    50/zommer.getScaleX(),CircleLPaint);
        }
        if (EraseMode) {
            canvas.drawPath(VErasePath, VErasePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mode==ZOOM){
            mScaleDetector.onTouchEvent(event);
        }

        if (mode==NONE) {
            zommer.OnTouch(event);
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN -> {
                mxAndyFixer.FixXYUsingMatrix(event.getX(), event.getY(), zommer.getCanvasMatrix());
                DownX = mxAndyFixer.getFixedXYUsingMartix().x;
                DownY = mxAndyFixer.getFixedXYUsingMartix().y;

                X_YFixer xYFixer = new X_YFixer();
                xYFixer.FixXYUsingMatrix(DownX,DownY,zommer.getDefaultMatrix());
                DownRealTimeX = xYFixer.getFixedXYUsingMartix().x;
                DownRealTimeY = xYFixer.getFixedXYUsingMartix().y;

                if (ColorLoc) {
                    mColor = ColorDetector.getColor((int) X_YFixer.getfixedXYUsingMartix(mxAndyFixer.getFixedXYUsingMartix().x,
                                    mxAndyFixer.getFixedXYUsingMartix().y,zommer.getDefaultMatrix()).x,

                            (int)  X_YFixer.getfixedXYUsingMartix(mxAndyFixer.getFixedXYUsingMartix().x,
                                    mxAndyFixer.getFixedXYUsingMartix().y,zommer.getDefaultMatrix()).y,mBitmap);
                    CircleLPaint.setColor(mColor);
                }else {
                    ActionDown(mxAndyFixer.getFixedXYUsingMartix().x, mxAndyFixer.getFixedXYUsingMartix().y);
                }


                // Get image matrix values and place them in an array.
                final float[] matrixValues = new float[9];
                if (ImagesMatrix.Size() > 0) {
                    ImagesMatrix.getMatrix().getValues(matrixValues);

                    // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
                    final float scaleX = matrixValues[Matrix.MSCALE_X];
                    ErasePaint.setStrokeWidth(ErasePaintSTROKEWIDTH / (scaleX * zommer.getScaleX()));
                    VErasePaint.setStrokeWidth(ErasePaintSTROKEWIDTH / zommer.getScaleX());
                }
                invalidate();
            }
            case MotionEvent.ACTION_MOVE -> {
                mxAndyFixer.FixXYUsingMatrix(event.getX(), event.getY(), zommer.getCanvasMatrix());
                if (ColorLoc) {
                    mColor = ColorDetector.getColor((int) X_YFixer.getfixedXYUsingMartix(mxAndyFixer.getFixedXYUsingMartix().x,
                                    mxAndyFixer.getFixedXYUsingMartix().y,zommer.getDefaultMatrix()).x,
                            (int)  X_YFixer.getfixedXYUsingMartix(mxAndyFixer.getFixedXYUsingMartix().x,
                                    mxAndyFixer.getFixedXYUsingMartix().y,zommer.getDefaultMatrix()).y,mBitmap);
                    CircleLPaint.setColor(mColor);
                }else {
                    ActionMove(mxAndyFixer.getFixedXYUsingMartix().x, mxAndyFixer.getFixedXYUsingMartix().y);
                }
                invalidate();
            }
            case MotionEvent.ACTION_POINTER_DOWN -> {
                if (Images.Size() > 0) {
                    if (!EraseMode){
                        float scaleImageCenterX =  (float) (Images.Image().getWidth()) / 2;
                        float scaleImageCenterY = (float) (Images.Image().getHeight()) / 2;

                        float divx = 1f-ImagesMatrix.getScaleValue().x;
                        float divy = 1f-ImagesMatrix.getScaleValue().y;

                        float fx = (scaleImageCenterX*divx);
                        float fy = (scaleImageCenterY*divy);


                        X_YFixer xYFixer = new X_YFixer();
                        xYFixer.FixXYUsingMatrix(event.getX(),event.getY(),zommer.getCanvasMatrix());
                        if (xYFixer.getFixedXYUsingMartix().x >= (ImagesPosition.ImagePosition().x+fx) &&
                                xYFixer.getFixedXYUsingMartix().x  <= ((float) Images.Image().getWidth() * ImagesMatrix.getScaleValue().x) +
                                        (ImagesPosition.ImagePosition().x+fx) &&
                                xYFixer.getFixedXYUsingMartix().y  >= (ImagesPosition.ImagePosition().y+fy) &&
                                xYFixer.getFixedXYUsingMartix().y  <= ((float) Images.Image().getHeight() * ImagesMatrix.getScaleValue().y) +
                                        (ImagesPosition.ImagePosition().y+fy)) {
                            Log.d("TAG", "ZZZZZZZzzzzzzz: ");
                            mode = ZOOM;
                        }
                    }
                }

                return true;
            }
            case MotionEvent.ACTION_UP,MotionEvent.ACTION_POINTER_UP -> {
                mRotate = false;
                mScale = false;
                mxAndyFixer.FixXYUsingMatrix(event.getX(), event.getY(), zommer.getCanvasMatrix());
                mScaleSource = mxAndyFixer.getFixedXYUsingMartix().x;
                ActionUp(mxAndyFixer.getFixedXYUsingMartix().x, mxAndyFixer.getFixedXYUsingMartix().y);
                mode = NONE;
                invalidate();
            }
        }
        return true;
    }

    private void ActionUp(float x, float y) {
        ColorLoc = false;
        //mScaleSource = x;

        if (mColor!=null){
                setImagesColor(mColor);
        }
        mColor = null;
        if (EraseMode) {
            if (Restore) {
                ErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

                ErasePaint.setAlpha(ErasePaintOpacity);

                EraseCanvas.drawPath(ErasePath, ErasePaint);
                rEraseCanvas.drawPath(rErasePath, ErasePaint);

            } else {
                ErasePaint.setXfermode(null);

                ErasePaint.setAlpha(ErasePaintOpacity);

                EraseCanvas.drawPath(ErasePath, ErasePaint);
                rEraseCanvas.drawPath(rErasePath, ErasePaint);

                ErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                ErasePaint.setAlpha(255);

                EraseCanvas.drawBitmap(Images.NoImage(), 0, 0, ErasePaint);
                rEraseCanvas.drawBitmap(Images.NoImage(), 0, 0, ErasePaint);
            }

            Images.UpdateRealBitmap(rEraseBm);
            ErasePath.reset();
            rErasePath.reset();
            VErasePath.reset();
        }
        invalidate();
    }


    private void ActionDown(float x, float y) {

        if (Images.Size() > 0) {
            for (int i = 0; i < Images.Size(); i++) {

                float scaleImageCenterX =  (float) (Images.getImages().get(i).getWidth()) / 2;
                float scaleImageCenterY = (float) (Images.getImages().get(i).getHeight()) / 2;

                float divx = 1f-ImagesMatrix.getMatrixs().get(i).getScaleV().x;
                float divy = 1f-ImagesMatrix.getMatrixs().get(i).getScaleV().y;

                float fx = (scaleImageCenterX*divx);
                float fy = (scaleImageCenterY*divy);

                RectF rectF = new RectF( ImagesPosition.ImagesPositionList().get(i).x+fx, ImagesPosition.ImagesPositionList().get(i).y+fy,
                        ( ImagesPosition.ImagesPositionList().get(i).x+fx)+(Images.getImages().get(i).getWidth()
                                *ImagesMatrix.getMatrixs().get(i).getScaleV().x),
                        ( ImagesPosition.ImagesPositionList().get(i).y+fy)+(Images.getImages().get(i).getHeight()
                                *ImagesMatrix.getMatrixs().get(i).getScaleV().y));

                if (rectF.contains(x,y)
                        &&!isClicked(x, y, RectDst.left-(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX()))
                        &&!isClicked(x, y, RectDst.right+(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX()))
                        &&!isClicked(x, y, RectDst.right+(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX()))
                        &&!isClicked(x, y, RectDst.left-(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX()))){

                    mode = DRAG;

                    Images.setIndex(i);
                    ImagesPosition.setIndex(i);
                    ImagesMatrix.setIndex(i);
                    ImagesPaints.setIndex(i);

                    ((AddPhotoActivity) mContext).ImagesOpacity.setProgress(ImagesPaints.getOpacity());
                    // ((AddPhotoActivity)mContext).ImagesScale.setProgress(Math.round(ImagesMatrix.getScaleValue().x*10f));

                    ((AddPhotoActivity) mContext).MargeAddOptions(VISIBLE);
                    X = ImagesPosition.ImagePosition().x;
                    Y = ImagesPosition.ImagePosition().y;

                    RealTimeX = ImagesPosition.ImageRealTimePosition().x;
                    RealTimeY = ImagesPosition.ImageRealTimePosition().y;

                    ShowCadre = true;
                }else {
                    ShowCadre = false;
                }
            }
            if (EraseMode) {
                ShowCadre = true;
                EraseBm = Bitmap.createBitmap(Images.Image().getWidth(), Images.Image().getHeight(),
                        Images.Image().getConfig());
                EraseCanvas = new Canvas(EraseBm);
                EraseCanvas.drawBitmap(Images.Image(), 0, 0, null);

                rEraseBm = Bitmap.createBitmap(Images.Image().getWidth(), Images.Image().getHeight(),
                        Images.Image().getConfig());
                rEraseCanvas = new Canvas(rEraseBm);
                rEraseCanvas.drawBitmap(Images.Image(), 0, 0, null);

                if (zommer.readyToDraw()) {
                    X_YFixer xAndyFixer1 = new X_YFixer();
                    xAndyFixer1.FixXYUsingMatrix(x, y, ImagesMatrix.getMatrix());
                    ErasePath.moveTo(xAndyFixer1.getFixedXYUsingMartix().x, xAndyFixer1.getFixedXYUsingMartix().y);
                    rErasePath.moveTo(xAndyFixer1.getFixedXYUsingMartix().x, xAndyFixer1.getFixedXYUsingMartix().y);
                    VErasePath.moveTo(x, y);
                }

            }else {
                // Case On touche the Remove Button
                mClickCircles.ClickTopLeft(isClicked(x, y, RectDst.left-(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX())),
                        this);
                // Case On touche For Rotate.
                mClickCircles.ClickTopRight(isClicked(x, y, RectDst.right+(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX())),
                        this);
                // Case On touche Copy Button To Copy The Copy
                mClickCircles.ClickBottomRight(isClicked(x, y, RectDst.right+(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX())),
                        this);

                mClickCircles.ClickBottomLeft(isClicked(x, y,RectDst.left-(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX())),
                        this);

                if (isClicked(x, y, RectDst.right+(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX()))) {
                    mRotate = true;
                }

                if (isClicked(x, y, RectDst.right+(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX()))) {

                    mScale = true;
                    mScaleSource = x;
                }

            }

        }
        invalidate();
    }

    private void ActionMove(float x, float y) {
        if (EraseMode) {
            if (zommer.readyToDraw()) {

                if (ErasePaintHardness == 0f){
                    ErasePaint.setMaskFilter(null);
                    VErasePaint.setMaskFilter(null);

                }else {
                    // Get image matrix values and place them in an array.
                    final float[] matrixValues = new float[9];
                    if (ImagesMatrix.Size() > 0) {
                        ImagesMatrix.getMatrix().getValues(matrixValues);

                        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
                        final float scaleX = matrixValues[Matrix.MSCALE_X];

                        ErasePaint.setMaskFilter(new BlurMaskFilter(ErasePaintHardness /(scaleX * zommer.getScaleX()), BlurMaskFilter.Blur.NORMAL));
                        VErasePaint.setMaskFilter(new BlurMaskFilter(ErasePaintHardness /zommer.getScaleX(), BlurMaskFilter.Blur.NORMAL));
                    }
                }

                X_YFixer xAndyFixer1 = new X_YFixer();
                xAndyFixer1.FixXYUsingMatrix(x, y, ImagesMatrix.getMatrix());
                ErasePath.lineTo(xAndyFixer1.getFixedXYUsingMartix().x, xAndyFixer1.getFixedXYUsingMartix().y);
                rErasePath.lineTo(xAndyFixer1.getFixedXYUsingMartix().x, xAndyFixer1.getFixedXYUsingMartix().y);
                VErasePath.lineTo(x, y);
            }
        } else {
            if (mScale) {

/*
                 if (x > mScaleSource) {
                    PointF DrawXyScale = new PointF();
                    PointF RealXyScale = new PointF();
                    DrawXyScale.set(ImagesMatrix.getScaleValue().x + 0.02f, ImagesMatrix.getScaleValue().y + 0.02f);
                    RealXyScale.set(ImagesMatrix.getRScaleValue().x + (0.02f * dvx), ImagesMatrix.getRScaleValue().y + (0.02f * dvy));
                    setScale(DrawXyScale, RealXyScale);


                } else if (mScaleSource > x) {
                    PointF DrawXyScale = new PointF();
                    PointF RealXyScale = new PointF();
                    DrawXyScale.set(ImagesMatrix.getScaleValue().x - 0.02f, ImagesMatrix.getScaleValue().y - 0.02f);
                    RealXyScale.set(ImagesMatrix.getRScaleValue().x - (0.02f * dvx), ImagesMatrix.getRScaleValue().y - (0.02f * dvy));
                    setScale(DrawXyScale, RealXyScale);

                }
                 */
                float dvx = ImagesMatrix.getRScaleValue().x / ImagesMatrix.getScaleValue().x;
                float dvy = ImagesMatrix.getRScaleValue().y / ImagesMatrix.getScaleValue().y;


                float deltaX = x - DownX;
                float deltaY = y-DownY;

                float scaleFactorX = ImagesMatrix.getScaleValue().x + Math.max(deltaX, deltaY) / Math.max(getWidth(), getHeight());

                float scaleFactorY = ImagesMatrix.getScaleValue().y + Math.max(deltaX, deltaY) / Math.max(getWidth(), getHeight());


                scaleFactorX = Math.max(scaleFactorX, 0.005f);

                scaleFactorY = Math.max(scaleFactorY, 0.005f);

                PointF DrawXyScale = new PointF();
                PointF RealXyScale = new PointF();

                DrawXyScale.set(scaleFactorX,scaleFactorY);
                RealXyScale.set((scaleFactorX * dvx),  (scaleFactorY * dvy));
                setScale(DrawXyScale, RealXyScale);

            } else if (mRotate) {

                int rotationAngleDegrees = 0;

                double rotationAngleRadians = Math.atan2(x - (getWidth() / 2.0f), (getHeight() /2.0f)-y);
                rotationAngleDegrees += (int) Math.toDegrees(rotationAngleRadians);

                if (rotationAngleDegrees<0){
                    setRotateDegree(rotationAngleDegrees + 360);
                }else {
                    setRotateDegree(rotationAngleDegrees);
                }

                ((AddPhotoActivity) mContext).ImagesRotate.setProgress(ImagesMatrix.getRotationDegree());
            } else {
                if (Images.Size() > 0) {
                    if (mode == DRAG) {
                        float scaleImageCenterX =  (float) (Images.Image().getWidth()) / 2;
                        float scaleImageCenterY = (float) (Images.Image().getHeight()) / 2;

                        float divx = 1f-ImagesMatrix.getScaleValue().x;
                        float divy = 1f-ImagesMatrix.getScaleValue().y;

                        float fx = (scaleImageCenterX*divx);
                        float fy = (scaleImageCenterY*divy);

                        if (x >= (ImagesPosition.ImagePosition().x+fx) &&
                                x <= ((float) Images.Image().getWidth() * ImagesMatrix.getScaleValue().x) +
                                        (ImagesPosition.ImagePosition().x+fx) &&
                                y >= (ImagesPosition.ImagePosition().y+fy) &&
                                y <= ((float) Images.Image().getHeight() * ImagesMatrix.getScaleValue().y) +
                                        (ImagesPosition.ImagePosition().y+fy)) {

                            X += x - DownX;
                            Y += y - DownY;

                            X_YFixer xYFixer1 = new X_YFixer();
                            xYFixer1.FixXYUsingMatrix(x,y,zommer.getDefaultMatrix());

                            RealTimeX += xYFixer1.getFixedXYUsingMartix().x - DownRealTimeX;
                            RealTimeY += xYFixer1.getFixedXYUsingMartix().y - DownRealTimeY;

                            ImagesPosition.updatePosition(X, Y,RealTimeX,RealTimeY);
                            ImagesMatrix.updatePosition(X, Y, Images.Image());

                            DownX = x;
                            DownY = y;

                            X_YFixer xYFixer = new X_YFixer();
                            xYFixer.FixXYUsingMatrix(x,y,zommer.getDefaultMatrix());
                            DownRealTimeX = xYFixer.getFixedXYUsingMartix().x;
                            DownRealTimeY = xYFixer.getFixedXYUsingMartix().y;

                        }

                        ShowCadre = true;

                    }else {
                        ShowCadre = false;
                    }
                }
            }
        }
        invalidate();
    }

    public void DrawMergeAndAddedImages(Canvas canvas) {
        Paint PaintOfRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        PaintOfRect.setColor(Color.parseColor("#A6A061"));
        PaintOfRect.setStyle(Paint.Style.STROKE);
        PaintOfRect.setStrokeWidth(2);

        Paint PaintOfCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        PaintOfCircle.setColor(Color.parseColor("#A6A061"));

        for (int i = 0; i < Images.Size(); i++) {
               /*
               //canvas.drawBitmap(Images.getScaledImages().get(i),ImagesPosition.ImagesPositionList().get(i).x,ImagesPosition.ImagesPositionList().get(i).y,ImagesPaints.getImagesPaintList().get(i))
               ; Matrix matrix = new Matrix();
                float [] dst;

                dst = new float[]{
                        RectDstPer.left, RectDstPer.top, // top left point
                        RectDstPer.right, RectDstPer.top, // top right point
                        RectDstPer.right, RectDstPer.bottom, // bottom right point
                        RectDstPer.left, RectDstPer.bottom // bottom left point ImagesMatrix.getMatrixs().get(i)
                };
                matrix.setPolyToPoly(dst,0,src,0,4);
                */
           canvas.drawBitmap(Images.getImages().get(i), ImagesMatrix.getMatrixs().get(i).getMatrix(), ImagesPaints.getImagesPaintList().get(i));

            //canvas.drawBitmap(applyMultiplyWithTransparency(Images.getImages().get(i),i), ImagesMatrix.getMatrixs().get(i).getMatrix(), null);
        }

        /*
         // Create a temporary bitmap and canvas once, outside the loop
            Bitmap tempBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas tempCanvas = new Canvas(tempBitmap);

            for (int i = 0; i < Images.Size(); i++) {
                // Clear the temporary bitmap
                tempBitmap.eraseColor(Color.TRANSPARENT);

                // Draw the current image onto the temporary canvas
                tempCanvas.drawBitmap(Images.getImages().get(i), ImagesMatrix.getMatrixs().get(i).getMatrix(), null);

                // Draw the temporary bitmap onto the main canvas using the blend mode
                canvas.drawBitmap(tempBitmap, 0, 0, ImagesPaints.getImagesPaintList().get(i));
            }
         */
    }
    /*
    if (Images.Size() == 0) return;
            // Create a temporary bitmap and canvas
            Bitmap tempBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas tempCanvas = new Canvas(tempBitmap);

            // Draw the background or first image without blending
            tempCanvas.drawBitmap(Images.getImages().get(0), ImagesMatrix.getMatrixs().get(0).getMatrix(), null);

            // Blend subsequent images
            for (int i = 1; i < Images.Size(); i++) {
                Bitmap currentImage = Images.getImages().get(i);
                Matrix currentMatrix = ImagesMatrix.getMatrixs().get(i).getMatrix();
                Paint currentPaint = ImagesPaints.getImagesPaintList().get(i);

                // Create another temporary bitmap for the current image
                Bitmap imageBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas imageCanvas = new Canvas(imageBitmap);

                // Draw the current image onto its own canvas
                imageCanvas.drawBitmap(currentImage, currentMatrix, null);

                // Blend this image with the accumulated result
                tempCanvas.drawBitmap(imageBitmap, 0, 0, currentPaint);

                // Clean up
                imageBitmap.recycle();
            }

            // Draw the final result onto the main canvas
            canvas.drawBitmap(tempBitmap, 0, 0, null);

            // Clean up
            tempBitmap.recycle();



     */
    public void DrawImagesCadre(Canvas canvas){
        if (Images.Size()>0){
        Paint PaintOfRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        PaintOfRect.setColor(Color.parseColor("#A6A061"));
        PaintOfRect.setStyle(Paint.Style.STROKE);
        PaintOfRect.setStrokeWidth(2/zommer.getScaleX());

        Paint PaintOfCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        PaintOfCircle.setColor(Color.parseColor("#A6A061"));

            float scaleImageCenterX =  (float) (Images.Image().getWidth()) / 2;
            float scaleImageCenterY = (float) (Images.Image().getHeight()) / 2;

        float divx = 1f-ImagesMatrix.getScaleValue().x;
        float divy = 1f-ImagesMatrix.getScaleValue().y;

            float fx = (scaleImageCenterX*divx);
            float fy = (scaleImageCenterY*divy);


        rightCopy = (ImagesPosition.ImagePosition().x+fx) + ((float)Images.Image().getWidth()*ImagesMatrix.getScaleValue().x);
        bottomCopy = (ImagesPosition.ImagePosition().y+fy) + ((float)Images.Image().getHeight()*ImagesMatrix.getScaleValue().y);

        RectDst = new RectF(ImagesPosition.ImagePosition().x+fx,
                ImagesPosition.ImagePosition().y+fy,
                rightCopy,
                bottomCopy);


        if (Images.getDrawType() == DrawType.ADD){

                if (ShowCadre){
                    if (EraseMode){
                    canvas.drawRect(RectDst, PaintOfRect);
                    }else{
                        canvas.drawRect(RectDst, PaintOfRect);
                        // cancel Past.
                        Bitmap sc = Bitmap.createScaledBitmap(Cs,(int) ((25/zommer.getScaleX())*2),(int) ((25/zommer.getScaleX())*2),true);
                        canvas.drawCircle(RectDst.left-(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX()),25/zommer.getScaleX(),PaintOfCircle);
                        canvas.drawBitmap(sc,(RectDst.left-(25/zommer.getScaleX()))-(float)sc.getWidth()/2,
                                (RectDst.top-(25/zommer.getScaleX()))-(float)sc.getHeight()/2,null);

                        // Rotate Past.
                        Bitmap tr = Bitmap.createScaledBitmap(Rt, (int) ((25/zommer.getScaleX())*2),(int) ((25/zommer.getScaleX())*2),true);
                        canvas.drawCircle(RectDst.right+(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX()),25/zommer.getScaleX(),PaintOfCircle);
                        canvas.drawBitmap(tr,(RectDst.right+(25/zommer.getScaleX()))-(float)tr.getWidth()/2,
                                (RectDst.top-(25/zommer.getScaleX()))-(float)tr.getHeight()/2,null);

                        // Scale Past.
                        Bitmap zr = Bitmap.createScaledBitmap(Rz,(int) ((25/zommer.getScaleX())*2),(int) ((25/zommer.getScaleX())*2),true);
                        canvas.drawCircle(RectDst.right+(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX()),25/zommer.getScaleX(),PaintOfCircle);
                        canvas.drawBitmap(zr,(RectDst.right+(25/zommer.getScaleX()))-(float)zr.getWidth()/2,
                                (RectDst.bottom+(25/zommer.getScaleX()))-(float)zr.getHeight()/2,null);

                        // Copy the Copy.
                        Bitmap pC = Bitmap.createScaledBitmap(Cp,(int) ((25/zommer.getScaleX())*2),(int) ((25/zommer.getScaleX())*2),true);
                        canvas.drawCircle(RectDst.left-(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX()),25/zommer.getScaleX(),PaintOfCircle);
                        canvas.drawBitmap(pC,(RectDst.left-(25/zommer.getScaleX()))-(float)pC.getWidth()/2,
                                (RectDst.bottom+(25/zommer.getScaleX()))-(float)pC.getHeight()/2,null);
                    }


            }
        }
        }
    }

    /*
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap,
                                                int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;


        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }


    public Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap,
                                                       int reqWidth, int reqHeight) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int sampleSize = calculateSampleSize(width, height, reqWidth, reqHeight);

        if (sampleSize > 1) {
            int newWidth = width / sampleSize;
            int newHeight = height / sampleSize;


            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap.CompressFormat format = scaledBitmap.hasAlpha() ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;

            scaledBitmap.compress(format, 10, stream);

            byte[] byteArray = stream.toByteArray();
            Log.d("TA", "decodeSampledBitmapFromBitmap: "+newHeight+" "+newWidth);

            try {
                return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            } finally {
                scaledBitmap.recycle();  // Recycle the scaled bitmap to free up memory
            }
        }

        return bitmap;
    }
     */


    public static int calculateSampleSize(
            int originalWidth, int originalHeight, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        if (originalHeight > reqHeight || originalWidth > reqWidth) {
            final int halfHeight = originalHeight / 2;
            final int halfWidth = originalWidth / 2;

            // Calculate the largest sampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / sampleSize) >= reqHeight
                    && (halfWidth / sampleSize) >= reqWidth) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

    public Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap,
                                                       int reqWidth, int reqHeight) {

         int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int sampleSize = calculateSampleSize(width, height, reqWidth, reqHeight);

        if (sampleSize > 1) {
            int newWidth = width / sampleSize;
            int newHeight = height / sampleSize;
            Log.d("TA", "decodeSampledBitmapFromBitmap: "+newHeight+" "+newWidth);
            return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        }
        Log.d("TA2222", "decodeSampledBitmapFromBitmap: ");
        return bitmap;
    }



    public void addDrawImages(ArrayList<Bitmap> RealBitmapList){

        for (int i = 0; i <RealBitmapList.size(); i++) {

        float division0 = (float) RealBitmapList.get(i).getHeight()/(float) RealBitmapList.get(i).getWidth();
        float division1 = (float) RealBitmapList.get(i).getWidth()/(float) RealBitmapList.get(i).getHeight();


        float Drawx = 0,Drawy = 0;
        float Realx = 0,Realy = 0;
        float height=0,width=0;
        float Realwidth=0,Realheight;

        if (getDrawType()== DrawType.MERGE){
            if (getImageBitmap().getHeight()>
                    getImageBitmap().getWidth()){


                if (getBitmapRect().height()/division0
                        <getBitmapRect().width()){

                    float rd = (float) RealBitmapList.get(i).getWidth()/(float) RealBitmapList.get(i).getHeight();

                    width = (getBitmapRect().width());
                    height = (width/rd);

                    Realwidth = getImageBitmap().getWidth();
                    Realheight =  (Realwidth/rd);
                }else {
                    float rd = (float) RealBitmapList.get(i).getHeight()/(float) RealBitmapList.get(i).getWidth();

                    height =getBitmapRect().height();
                    width = (height/rd);


                    Realheight = getImageBitmap().getHeight();
                    Realwidth = (Realheight/rd);

                }

                Drawx = width / (float) RealBitmapList.get(i).getWidth();
                Drawy = height / (float) RealBitmapList.get(i).getHeight();

                Realx = Realwidth / (float) RealBitmapList.get(i).getWidth();
                Realy = Realheight / (float) RealBitmapList.get(i).getHeight();

            }else {
                if(getBitmapRect().width()/division1
                        <getBitmapRect().height()){
                    float rd = (float) RealBitmapList.get(i).getHeight()/(float) RealBitmapList.get(i).getWidth();

                    height = getBitmapRect().height();
                    width = (height/rd);


                    Realheight = getImageBitmap().getHeight();
                    Realwidth = (Realheight/rd);
                }else{
                    float rd = (float) RealBitmapList.get(i).getWidth()/(float) RealBitmapList.get(i).getHeight();

                    width = (getBitmapRect().width());
                    height = (width/rd);

                    Realwidth = getImageBitmap().getWidth();
                    Realheight =  (Realwidth/rd);
                }

                Drawx = width / (float) RealBitmapList.get(i).getWidth();
                Drawy = height / (float) RealBitmapList.get(i).getHeight();

                Realx = Realwidth / (float) RealBitmapList.get(i).getWidth();
                Realy = Realheight / (float) RealBitmapList.get(i).getHeight();

            }
        }
        else if (getDrawType() == DrawType.ADD){
            if (getImageBitmap().getHeight()> getImageBitmap().getWidth()){
                float rd = (float) RealBitmapList.get(i).getHeight()/(float) RealBitmapList.get(i).getWidth();

                width = (getBitmapRect().width()/2);
                height =(width*rd);


                Realwidth =  (float)(getImageBitmap().getWidth()/2);
                Realheight =  (Realwidth*rd);

                Drawx = width / (float) RealBitmapList.get(i).getWidth();
                Drawy = height / (float) RealBitmapList.get(i).getHeight();

                Realx = Realwidth / (float) RealBitmapList.get(i).getWidth();
                Realy = Realheight / (float) RealBitmapList.get(i).getHeight();

            }else {

                float rd = (float) RealBitmapList.get(i).getWidth()/(float) RealBitmapList.get(i).getHeight();

                height =  (getBitmapRect().height()/2);
                width = (height*rd);

                Realheight =(float) (getImageBitmap().getHeight()/2);
                Realwidth =  (Realheight*rd);

                Drawx = width / (float) RealBitmapList.get(i).getWidth();
                Drawy = height / (float) RealBitmapList.get(i).getHeight();

                Realx = Realwidth / (float) RealBitmapList.get(i).getWidth();
                Realy = Realheight / (float) RealBitmapList.get(i).getHeight();

            }
        }


        // Add the new list of bitmaps to the cache
        //bitmapCache.put(i, ScaledBitmapList.get(i));

        Images.AddImages(RealBitmapList.get(i),RealBitmapList.get(i));

        Images.AddDrawType(getDrawType());

        ImagesPaints.AddPaint(getDrawType());

        ImagesPosition.AddPosition((float) (getWidth() - (RealBitmapList.get(i).getWidth())) /2,
                (float) (getHeight() - (RealBitmapList.get(i).getHeight())) /2

        ,(float) (getImageBitmap().getWidth() - (RealBitmapList.get(i).getWidth())) /2,
                (float) (getImageBitmap().getHeight() - (RealBitmapList.get(i).getHeight())) /2);

        Matrix matrix = new Matrix();
        matrix.reset();

        matrix.postRotate(0,(float) RealBitmapList.get(i).getWidth() /2, (float) RealBitmapList.get(i).getHeight() /2);

        matrix.postScale(Drawx,Drawy,(float) RealBitmapList.get(i).getWidth() /2, (float) RealBitmapList.get(i).getHeight() /2);

        matrix.postTranslate((float) (getWidth() - (RealBitmapList.get(i).getWidth())) /2,
                (float) (getHeight() - (RealBitmapList.get(i).getHeight())) /2);
            //matrix.postTranslate((getWidth() -(width))/2, (getHeight() - (height))/2);

        PointF DrawXyScale = new PointF(Drawx,Drawy);
        PointF RealXyScale = new PointF(Realx,Realy);

        ImagesMatrix.AddMatrix(matrix,0,DrawXyScale,RealXyScale);}

        invalidate();
    }


    public void SetDrawType(DrawType drawType){
        this.drawType = drawType;
    }

    public DrawType getDrawType() {
        return drawType;
    }

    public void CancelAdd(){
        Images.clearImages();
        ImagesPosition.clearPositions();
        ImagesMatrix.clearMatrices();
        ImagesPaints.clearPaints();

        Images.unloadingIndex();
        ImagesMatrix.unloadingIndex();
        ImagesPosition.unloadingIndex();
        ImagesPaints.unloadingIndex();
    }

    public void DoneAdd(){
        Images.clearImages();
        ImagesPosition.clearPositions();
        ImagesMatrix.clearMatrices();
        ImagesPaints.clearPaints();

        Images.unloadingIndex();
        ImagesMatrix.unloadingIndex();
        ImagesPosition.unloadingIndex();
        ImagesPaints.unloadingIndex();
    }

    public Bitmap getFinalImage() {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);


        X_YFixer xAndyFixer = new X_YFixer();

        // Create a mutable copy of the original bitmap
        Bitmap bitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

        for (int i = 0; i < Images.Size(); i++) {
            xAndyFixer.FixXYUsingMatrix(ImagesPosition.ImagesPositionList().get(i).x, ImagesPosition.ImagesPositionList().get(i).y,
                    zommer.getDefaultMatrix());

            ImagesMatrix.setRotationAndScaleAndTranslate(i,
                    Images.getImages().get(i),
                    ImagesPosition.ImagesRealTimePositionList().get(i).x, ImagesPosition.ImagesRealTimePositionList().get(i).y);

            // Create a new bitmap for each image, with a white background
            Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            //tempBitmap.eraseColor(Color.WHITE);
            Canvas tempCanvas = new Canvas(tempBitmap);

            // Draw the image on the temp canvas
            tempCanvas.drawBitmap(Images.getImages().get(i), ImagesMatrix.getMatrixs().get(i).getMatrix(), paint);


            // Create another temporary bitmap to hold the blended result
            Bitmap blendedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas blendedCanvas = new Canvas(blendedBitmap);

            // Draw the current state of the main bitmap onto the blended canvas
            blendedCanvas.drawBitmap(bitmap, 0, 0, paint);

            // Draw the temp bitmap onto the blended canvas with the blend mode
            blendedCanvas.drawBitmap(tempBitmap, 0, 0, ImagesPaints.getImagesPaintList().get(i));

            // Now, we need to restore the alpha values from the original image
            int[] blendedPixels = new int[blendedBitmap.getWidth() * blendedBitmap.getHeight()];
            int[] tempPixels = new int[tempBitmap.getWidth() * tempBitmap.getHeight()];
            blendedBitmap.getPixels(blendedPixels, 0, blendedBitmap.getWidth(), 0, 0, blendedBitmap.getWidth(), blendedBitmap.getHeight());
            tempBitmap.getPixels(tempPixels, 0, tempBitmap.getWidth(), 0, 0, tempBitmap.getWidth(), tempBitmap.getHeight());

            for (int j = 0; j < blendedPixels.length; j++) {
                int alpha = Color.alpha(tempPixels[j]);
                blendedPixels[j] = (blendedPixels[j] & 0x00FFFFFF) | (alpha << 24);
            }

            blendedBitmap.setPixels(blendedPixels, 0, blendedBitmap.getWidth(), 0, 0, blendedBitmap.getWidth(), blendedBitmap.getHeight());

            // Draw the blended bitmap back onto the main canvas
            canvas.drawBitmap(blendedBitmap, 0, 0, paint);
        }

        return bitmap;
    }


    /*Returns the bitmap position inside an imageView.
     * @param imageView source ImageView
     * @return Rect position of the bitmap in the ImageView
     */

    /*
    Returns the bitmap position inside an imageView.
    * @param imageView source ImageView
    * @return Rect position of the bitmap in the ImageView
      */
    public  final RectF getBitmapRect()
    {
        RectF rect = new RectF();

        final Bitmap drawable = mBitmap;
        if ( drawable == null)
        {
            return rect;
        }

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        zommer.getDefaultMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Bitmap d     = mBitmap;
        final int      origW = d.getWidth();
        final int      origH = d.getHeight();

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
    public  final RectF getBitmapRectg() {
        Bitmap bitmap = ViewZommer.getScaledBitmapBasedOnView(getImageBitmap(),this);


        return new RectF(0,0,bitmap.getWidth(),bitmap.getHeight());
    }

    /////

    public void setImagesMode(PorterDuff.Mode mode) {
        ImagesPaints.setXfermode(mode);
        invalidate();
    }

    public  Bitmap applyMultiplyWithTransparency(Bitmap source,int i) {
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);


        // Draw the source bitmap as is
        canvas.drawBitmap(source, 0, 0, null);

        canvas.drawBitmap(source, 0, 0, ImagesPaints.getImagesPaintList().get(i));

        return result;
    }

    public void setImagesColor(int color) {
        ImagesPaints.setImageColor(color);
        invalidate();
    }

    public void setImagesColorMode(PorterDuff.Mode mode) {
        ImagesPaints.setImagesColorMode(mode);
        invalidate();
    }

    public void setImagesOpacity(int opacity) {
        ImagesPaints.setOpacity(opacity);
        invalidate();
    }

    public void setColorLoc(boolean b) {
         ColorLoc = b;
         invalidate();
    }

    public void setEraseMode(boolean m){
        EraseMode = m;
        ShowCadre = true;

        for (int i = 0; i <Images.Size(); i++) {
            if (EraseMode){
                ImagesMatrix.setTRotation(i,0, ImagesPosition.ImagesPositionList().get(i).x,
                        ImagesPosition.ImagesPositionList().get(i).y, Images.getImages().get(i));
            }else {
                ImagesMatrix.setTRotation(i,ImagesMatrix.getMatrixs().get(i).getRotateV(),
                        ImagesPosition.ImagesPositionList().get(i).x,
                        ImagesPosition.ImagesPositionList().get(i).y,
                        Images.getImages().get(i));
            }
        }

        invalidate();
    }

    public void setEraseAddedBmOpacity(int opacity) {
        this.ErasePaintOpacity = opacity;
        invalidate();
    }

    public void setEraseAddedBmSize(int size) {
        ErasePaintSTROKEWIDTH = size;
        invalidate();
    }

    public void setEraseAddedBmHardness(float hardness) {
        ErasePaintHardness = hardness;
        invalidate();
    }

    public void setE_R(boolean b) {
        Restore = b;
        invalidate();
    }

    public boolean getE_R(){
        return Restore;
    }

    public void CancelErase() {
        for (int i = 0; i <Images.Size(); i++) {
            ImagesMatrix.setTRotation(i,ImagesMatrix.getMatrixs().get(i).getRotateV(),
                    ImagesPosition.ImagesPositionList().get(i).x,
                    ImagesPosition.ImagesPositionList().get(i).y,
                    Images.getImages().get(i));
        }


        Images.UpdateRealBitmap(Images.NoImage());
        EraseMode = false;
        invalidate();
    }

    public void DoneErase() {
        for (int i = 0; i <Images.Size(); i++) {
            ImagesMatrix.setTRotation(i,ImagesMatrix.getMatrixs().get(i).getRotateV(),
                    ImagesPosition.ImagesPositionList().get(i).x,
                    ImagesPosition.ImagesPositionList().get(i).y,
                    Images.getImages().get(i));
        };
        EraseMode = false;
        invalidate();
    }

    public void setRotateDegree(int degree) {
        ImagesMatrix.setRotation(degree, ImagesPosition.ImagePosition().x,
                ImagesPosition.ImagePosition().y, Images.Image());
        invalidate();
    }


    public void setScale(PointF scale, PointF RealScale){
        ImagesMatrix.setScale(scale,RealScale,ImagesPosition.ImagePosition().x, ImagesPosition.ImagePosition().y, Images.Image());
        invalidate();
    }

    public void setPerspectiveFixeCase(boolean percase){

    }
    public void flipV(){
        Matrix m = new Matrix();
        int Cx = Images.Image().getHeight()/2;
        int Cy = Images.Image().getWidth()/2;
        m.setScale(1, -1, Cx, Cy);

        Bitmap Rbitmap = Bitmap.createBitmap(Images.Image(), 0, 0,
                Images.Image().getWidth(),
                Images.Image().getHeight(),
                m, true);
        Images.UpdateRealBitmap(Rbitmap);
        invalidate();
    }

    public void flipH(){
        Matrix m = new Matrix();
        int Cx = Images.Image().getHeight()/2;
        int Cy = Images.Image().getWidth()/2;
        m.setScale(-1, 1, Cx, Cy);

        Bitmap Rbitmap = Bitmap.createBitmap(Images.Image(), 0, 0,
                Images.Image().getWidth(),
                Images.Image().getHeight(),
                m, true);
        Images.UpdateRealBitmap(Rbitmap);
        invalidate();
    }
    public void RotateR(){
        setRotateDegree(ImagesMatrix.getRotationDegree()+90);
        invalidate();
    }
    public void RotateL(){
        setRotateDegree(ImagesMatrix.getRotationDegree()+(-90));
        invalidate();
    }

    public void AddCopy() {
        float dx ,dy;
        float rx ,ry;
        float height,width;
        float RWidth,RHeight;





        if (getImageBitmap().getHeight()> getImageBitmap().getWidth()){

            ImagesPosition.AddPosition(ImagesPosition.ImagePosition().x+60f,
                    ImagesPosition.ImagePosition().y+60f,

                    ImagesPosition.ImageRealTimePosition().x+(60f/zommer.getDBmScaleY()),
                    ImagesPosition.ImageRealTimePosition().y+(60f/zommer.getDBmScaleY()));

            float rd = (float) Images.Image().getHeight()/(float) Images.Image().getWidth();

            width = (getBitmapRect().width()/2);
            height =(width*rd);


            RWidth =  (float)(getImageBitmap().getWidth()/2);
            RHeight =  (RWidth*rd);

            dx = width / (float) Images.Image().getWidth();
            dy = height / (float) Images.Image().getHeight();

            rx = RWidth / (float) Images.Image().getWidth();
            ry = RHeight / (float) Images.Image().getHeight();

        }else {

            ImagesPosition.AddPosition(ImagesPosition.ImagePosition().x+60,
                    ImagesPosition.ImagePosition().y+60,

                    ImagesPosition.ImageRealTimePosition().x+(60f/zommer.getDBmScaleX()),
                    ImagesPosition.ImageRealTimePosition().y+(60f/zommer.getDBmScaleX()));

            float rd = (float) Images.Image().getWidth()/(float) Images.Image().getHeight();

            height =  (getBitmapRect().height()/2);
            width = (height*rd);

            RHeight =(float) (getImageBitmap().getHeight()/2);
            RWidth =  (RHeight*rd);

            dx = width / (float) Images.Image().getWidth();
            dy = height / (float) Images.Image().getHeight();

            rx = RWidth / (float) Images.Image().getWidth();
            ry = RHeight / (float) Images.Image().getHeight();

        }

        /*
          float sx,sy;
        float h,w;

        float rsx,rsy;
        float rh,rw;

        if (getImageBitmap().getHeight()>getImageBitmap().getWidth()){

            float d = (float) Images.ScaledImages().getHeight()/(float) Images.ScaledImages().getWidth();
            w = getBitmapRect().width() /2f;
            h = w*d;
            sx = w / (float) Images.ScaledImages().getWidth();
            sy = h/ (float) Images.ScaledImages().getHeight();

            rw = getImageBitmap().getWidth() /2f;
            rh = rw*d;

            rsx = rw / (float) Images.ScaledImages().getWidth();
            rsy = rh/ (float) Images.ScaledImages().getHeight();
        }else{
            float d = (float) Images.ScaledImages().getWidth()/(float) Images.ScaledImages().getHeight();
            h = getBitmapRect().height() /2f;
            w = h*d;
            sx = w / (float) Images.ScaledImages().getWidth();
            sy = h/ (float) Images.ScaledImages().getHeight();

            rh = getImageBitmap().getHeight() /2f;
            rw = rh*d;
            rsx = rw / (float) Images.ScaledImages().getWidth();
            rsy = rh/ (float) Images.ScaledImages().getHeight();
        }*/

        Matrix matrix = new Matrix();

        matrix.reset();
        matrix.postRotate(0,(float) Images.Image().getWidth() /2, (float) Images.Image().getHeight() /2);
        matrix.postScale(dx,dy,(float) Images.Image().getWidth() /2, (float) Images.Image().getHeight() /2);
        matrix.postTranslate(ImagesPosition.ImagePosition().x+60f,
                ImagesPosition.ImagePosition().y+60f);


        Images.AddImages(Images.Image(),Images.NoImage());

        Images.AddDrawType(DrawType.ADD);

        PointF DrawXyScale = new PointF(dx,dy);
        PointF RealXyScale = new PointF(rx,ry);

        ImagesMatrix.AddMatrix(matrix,0,DrawXyScale,RealXyScale);

        ImagesPaints.AddPaint(DrawType.ADD);
        invalidate();
    }

    public int getIndex(){
        return Images.getIndex();
    }

    public void RemoveCopy() {
        if (Images.Size() != 1){
            Images.removeImage();
            ImagesPosition.removePosition();
            ImagesPaints.removePaint();
            ImagesMatrix.removeMatrix();
            invalidate();
        }else {
            ((AddPhotoActivity) mContext).MargeAddOptions(INVISIBLE);
            ((AddPhotoActivity) mContext).FlipperSetVisibility();
            Images.removeImage();
            ImagesPosition.removePosition();
            ImagesPaints.removePaint();
            ImagesMatrix.removeMatrix();

            Images.unloadingIndex();
            ImagesPosition.unloadingIndex();
            ImagesPaints.unloadingIndex();
            ImagesMatrix.unloadingIndex();
        }
    }

    /**
     *  for more Visit --->  <a href="https://stackoverflow.com/a/16792888">...</a>  & ---> <a href="https://stackoverflow.com/a/36146901">...</a>
     * @param downX X position of user Finger touche in Screen
     * @param downY Y position of user Finger touche in Screen
     * @param x X position of Circle in the screen
     * @param y X position of Circle in the screen
     * @return distance.
     */
    private boolean isClicked(float downX, float downY, float x, float y){
        int distance = (int) Math.sqrt(((downX - x) * (downX - x)) +
                ((downY - y) * (downY - y)) );
        ShowCadre = true;
        return distance < 25/zommer.getScaleX();
    }
    private float calculateScaleFactor(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     *
     * @param clickCircles The callback that will run
     */
    public void ClickCircles(ClickCircles clickCircles){
        this.mClickCircles = clickCircles;
    }


    public interface ClickCircles {

        /**
         *
         * @param b
         * @param addImagesImageView
         */
        void ClickTopLeft(boolean b, AddImagesImageView addImagesImageView);

        /**
         *
         * @param b
         * @param addImagesImageView
         */
        void ClickTopRight(boolean b, AddImagesImageView addImagesImageView);

        /**
         *
         * @param b
         * @param addImagesImageView
         */
        void ClickBottomRight(boolean b, AddImagesImageView addImagesImageView);

        /**
         *
         * @param b
         * @param addImagesImageView
         */
        void ClickBottomLeft(boolean b, AddImagesImageView addImagesImageView);
    }


      private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector)
        {

            float scaleImageCenterX =  (float) (Images.Image().getWidth()) / 2;
            float scaleImageCenterY = (float) (Images.Image().getHeight()) / 2;

            float divx = 1f-ImagesMatrix.getScaleValue().x;
            float divy = 1f-ImagesMatrix.getScaleValue().y;

            float fx = (scaleImageCenterX*divx);
            float fy = (scaleImageCenterY*divy);
            if (Images.Size() > 0) {
                if (!EraseMode){
                    mxAndyFixer.FixXYUsingMatrix(detector.getFocusX(), detector.getFocusY(), zommer.getCanvasMatrix());
                    for (int i = 0; i < Images.Size(); i++) {
                        if ((mxAndyFixer.getFixedXYUsingMartix().x >= (ImagesPosition.ImagesPositionList().get(i).x+fx) &&
                                mxAndyFixer.getFixedXYUsingMartix().x <= (((float) Images.getImages().get(i).getWidth()) *
                                        (ImagesMatrix.getMatrixs().get(i).getScaleV().x)) +
                                        (ImagesPosition.ImagesPositionList().get(i).x+fx))&&

                                ( mxAndyFixer.getFixedXYUsingMartix().y >= (ImagesPosition.ImagesPositionList().get(i).y+fy) &&
                                        mxAndyFixer.getFixedXYUsingMartix().y <= ((float) Images.getImages().get(i).getHeight() *
                                                (ImagesMatrix.getMatrixs().get(i).getScaleV().y)) +
                                                (ImagesPosition.ImagesPositionList().get(i).y+fy))) {
                            mode = ZOOM;
                        }
                    }
                }

            }
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
           if (mode==ZOOM){
               float dvx = ImagesMatrix.getRScaleValue().x / ImagesMatrix.getScaleValue().x;
               float dvy = ImagesMatrix.getRScaleValue().y / ImagesMatrix.getScaleValue().y;



               PointF DrawXyScale = new PointF();
               PointF RealXyScale = new PointF();

               // Calculate the new scale factors
               float newScaleX = ImagesMatrix.getScaleValue().x * detector.getScaleFactor();
               float newScaleY = ImagesMatrix.getScaleValue().y * detector.getScaleFactor();

               // Clamp the scale factors to a minimum of 0.001
               newScaleX = Math.max(newScaleX, 0.005f);
               newScaleY = Math.max(newScaleY, 0.005f);


               // Calculate the new real scale factors
               float newRealScaleX = ImagesMatrix.getRScaleValue().x * detector.getScaleFactor();
               float newRealScaleY = ImagesMatrix.getRScaleValue().y * detector.getScaleFactor();

               // Clamp the real scale factors to a minimum of 0.001
               newRealScaleX = Math.max(newRealScaleX, 0.005f);
               newRealScaleY = Math.max(newRealScaleY, 0.005f);


               DrawXyScale.set(newScaleX,newScaleY);
               RealXyScale.set(newRealScaleX,newRealScaleY);

               setScale(DrawXyScale, RealXyScale);

           }


            invalidate();
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            mode = NONE;
            //((ImageBorderActivity)(context)).firstBorder.setProgress((int) (saveScaleX*100));
        }
    }



    private PointF calculateMidPoint(MotionEvent event) {
        float x1 = event.getX(0);
        float y1 = event.getY(0);
        float x2 = event.getX(1);
        float y2 = event.getY(1);
        return new PointF((x1 + x2) / 2, (y1 + y2) / 2);
    }

    private float calculateDistance(MotionEvent event) {
        float x1 = event.getX(0);
        float y1 = event.getY(0);
        float x2 = event.getX(1);
        float y2 = event.getY(1);
        float dx = x1 - x2;
        float dy = y1 - y2;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
