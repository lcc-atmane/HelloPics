/*
 * Copyright (C) 2025 HelloPics
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice;

import static com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards.createCheckerboard;

import android.annotation.SuppressLint;
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
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.Select_crop_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.ScaleImageView.ImageViewUtils.ImageViewUtil;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.CopyAndPastBitmap.Copies;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.CopyAndPastBitmap.CopyPastUtils.CopiesMatrix;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.CopyAndPastBitmap.CopyPastUtils.CopiesPaints;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.CopyAndPastBitmap.CopyPastUtils.CopiesPosition;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.SelectToolsHandel.OnSelectArea;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.SelectToolsHandel.SelectTools;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ColorDetector;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.X_YFixer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.Zommer;
import com.phantomhive.exil.hellopics.R;

import java.util.ArrayList;


public class SelectImageView extends View {
    private static final String TAG = "SelectImageView" ;

    // The bounding box around the Bitmap that we are cropping.
    RectF mBitmapRect = new RectF();

    // The Cropped Bitmap Specification Holder.
    OnSelectArea onSelectArea;

   // Determine if We gonna Draw Oval Or Rect Or Free(Path) , Set Rect As A default choose.
    CropCutShape mCropCutShape= CropCutShape.RECTANGLE;
    CropCutShape mCropCutShapeHandel;

    // the Bitmap in The SelectedImageView.

    // Context Of The Activity.
    Context mContext;

    // if Bitmap Is Copied or not and if is Pasted Or Not.
    boolean isCopied = false;
    boolean isInPaste;
    boolean ShowCadre =true;

    // the RightX and BottomY of The Position of copied Bitmap in the View.
    float rightCopy, bottomCopy;

    // the Holding of Position.
    RectF RectDst = new RectF();

    // if The user Want to move Bitmap.
    boolean MoveCopyB;

    // value x & y win user down in screen usd for correction of the image transfer position
    float DownX,DownY, DownRealTimeX, DownRealTimeY,X, Y, RealTimeX, RealTimeY;

    ArrayList<Integer> mTouchePoint = new ArrayList<>();

    Copies mCopies = new Copies();
    CopiesPosition mCopiesPosition = new CopiesPosition();
    CopiesPaints mCopiesPaints = new CopiesPaints();
    CopiesMatrix mCopiesMatrix = new CopiesMatrix();


    DrawSelectTool mDrawSelectTool = new DrawSelectTool();
    boolean ModeGlobal = false ;
    ClickCircles mClickCircles;




    ViewZommer zommer;
    X_YFixer mxAndyFixer;
    Bitmap mBitmap;

    RectF displayedImageRect;

    boolean mRotate;
    boolean mScale;
    float mScaleSource;

    Bitmap Rt,Rz,Cs,Cp;

    Canvas EraseCanvas;
    Paint ErasePaint;
    private int ErasePaintOpacity = 255;
    private float ErasePaintSTROKEWIDTH = 30f;
    private float ErasePaintHardness = 45f;
    Paint VErasePaint;
    boolean EraseMode = false;

    boolean Erase = true;
    Path ErasePath;
    Path VErasePath;
    Bitmap EraseBm;
    boolean ColorLoc;
    Integer mColor;
    Paint CircleLPaint;


    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    ScaleGestureDetector mScaleDetector;
    boolean ActionFinished;

    Paint Checkerboardpaint;
    Bitmap createCheckerboard;

    public SelectImageView(Context context) {
        super(context);
        init(context);
    }

    public SelectImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public SelectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SelectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
         init(context);
    }


    private void init(@NonNull Context context) {
        mContext = context;
        zommer = new ViewZommer(this,context);
        mxAndyFixer = new X_YFixer();
        onSelectArea = new OnSelectArea();

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        Rt = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rotate_ic);
        Rz = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.resize_ic);
        Cs = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.closeimg_ic);
        Cp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.copycopy_ic);

        ErasePath = new Path();
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.setMatrix(zommer.getCanvasMatrix());

        if (createCheckerboard != null){
            canvas.drawBitmap(createCheckerboard,zommer.DefaultXY().x,zommer.DefaultXY().y,null);
        }

        canvas.drawBitmap(mBitmap,zommer.getDefaultMatrix(),Checkerboardpaint);
        // the Original Bitmap Coordination.
        mBitmapRect = getBitmapRect();
        // Draw Selected select tool.
        // TODO: 9/6/2024 for the select area
        mDrawSelectTool.DrawTheSelectTool(canvas,mCropCutShapeHandel, ActionFinished, zommer.getScaleX(), ModeGlobal);
        // Draw Copies.
        DrawCopiedBm(canvas);

        if (ColorLoc){
            canvas.drawCircle(mxAndyFixer.getFixedXYUsingMartix().x,
                    mxAndyFixer.getFixedXYUsingMartix().y-(200/zommer.getScaleX()),
                    50/zommer.getScaleX(),CircleLPaint);
        }

        if (EraseMode){
            // canvas.rotate(mCopiesMatrix.getRotationDegree(),getWidth()/2f,getHeight()/2f);
            canvas.drawPath(VErasePath,VErasePaint);
        }
    }
    public void drawTriangle(Canvas canvas, Paint paint, int x, int y, int width) {
        int halfWidth = width / 2;

        Path path = new Path();
        path.moveTo(x, y - halfWidth); // Top
        path.lineTo(x - halfWidth, y + halfWidth); // Bottom left
        path.lineTo(x + halfWidth, y + halfWidth); // Bottom right
        path.lineTo(x, y - halfWidth); // Back to Top
        path.close();

        canvas.drawPath(path, paint);
    }

    public void drawRhombus(Canvas canvas, Paint paint, int x, int y, int width) {
        int halfWidth = width / 2;

        Path path = new Path();
        path.moveTo(x, y + halfWidth); // Top
        path.lineTo(x - halfWidth, y); // Left
        path.lineTo(x, y - halfWidth); // Bottom
        path.lineTo(x + halfWidth, y); // Right
        path.lineTo(x, y + halfWidth); // Back to Top
        path.close();

        canvas.drawPath(path, paint);
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        zommer.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mBitmap!=null){
            displayedImageRect = ImageViewUtil.getBitmapRectCenterInside(mBitmap, this);
            createCheckerboard= createCheckerboard((int) CheckerBoards.getBitmapRect(zommer.getDefaultMatrix(),mBitmap,
                            getWidth(),getHeight()).width(),

                    (int) CheckerBoards.getBitmapRect(zommer.getDefaultMatrix(),mBitmap,
                            getWidth(),getHeight()).height(),20);
        }


    }


    public void setImageBitmap(Bitmap bm)
    {
        mBitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
        zommer.setBitmap(bm);
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

                DownRealTimeX = mxAndyFixer.getFixedXYUsingMartix().x;
                DownRealTimeY = mxAndyFixer.getFixedXYUsingMartix().y;

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
                if (mCopiesMatrix.getCopiesMatrixList().size() > 0) {
                    mCopiesMatrix.CopyMatrix().getMatrix().getValues(matrixValues);

                    // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
                    final float scaleX = matrixValues[Matrix.MSCALE_X];
                    ErasePaint.setStrokeWidth(ErasePaintSTROKEWIDTH / (scaleX * zommer.getScaleX()));
                    VErasePaint.setStrokeWidth(ErasePaintSTROKEWIDTH / zommer.getScaleX());
                }
                invalidate();

                return true;
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

                return true;
            }
            case MotionEvent.ACTION_POINTER_DOWN -> {
                if (mCopies.copiesNumber() > 0) {
                    if (!EraseMode) {
                        float scaleImageCenterX =  (mCopies.getRealBitmap().getWidth()) / 2f;
                        float scaleImageCenterY = (mCopies.getRealBitmap().getHeight()) / 2f;
                        float divx = 1f-mCopiesMatrix.getScaleValue().x;
                        float divy = 1f-mCopiesMatrix.getScaleValue().y;

                        float fx = (scaleImageCenterX*divx);
                        float fy = (scaleImageCenterY*divy);

                        X_YFixer xYFixer = new X_YFixer();
                        xYFixer.FixXYUsingMatrix(event.getX(),event.getY(),zommer.getCanvasMatrix());
                        if (xYFixer.getFixedXYUsingMartix().x >= (mCopiesPosition.CopyPosition().x+fx) &&
                                xYFixer.getFixedXYUsingMartix().x  <= ((float) mCopies.getRealBitmap().getWidth() * mCopiesMatrix.getScaleValue().x) +
                                        (mCopiesPosition.CopyPosition().x+fx) &&
                                xYFixer.getFixedXYUsingMartix().y  >= (mCopiesPosition.CopyPosition().y+fy) &&
                                xYFixer.getFixedXYUsingMartix().y  <= ((float) mCopies.getRealBitmap().getHeight() * mCopiesMatrix.getScaleValue().y) +
                                        (mCopiesPosition.CopyPosition().y+fy)) {

                            mode = ZOOM;
                        }
                    }
                }
                return true;
            }
            case MotionEvent.ACTION_UP,MotionEvent.ACTION_POINTER_UP -> {
                mxAndyFixer.FixXYUsingMatrix(event.getX(), event.getY(), zommer.getCanvasMatrix());
                ActionUp(mxAndyFixer.getFixedXYUsingMartix().x, mxAndyFixer.getFixedXYUsingMartix().y);
                mode = NONE;
                invalidate();
                return true;
            }
        }
        return false;
    }

    // Touche Methods///////////////////////////////////////////////////////////////////////////////

    /**
     * Handles a {@link MotionEvent#ACTION_DOWN} event
     * @param x the x-coordinate of the down action
     * @param y the y-coordinate of the down action
     */
    private void ActionDown(float x, float y){
        mTouchePoint = new ArrayList<>();
        if (!isInPaste){
            mCropCutShapeHandel = RectOrOval();
        }
        // Case On touche Button of The Copy Setting
        if (isInPaste) {
            if (!EraseMode){
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


                if (isClicked(x,y, RectDst.right+(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX()))){
                    mRotate = true;
                }

                if (isClicked(x,y, RectDst.right+(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX()))){
                    mScale = true;
                    mScaleSource = x;
                }
            }

        }

        if (isInPaste){
            isCopied = true;
            for (int i = 0 ; i < mCopiesPosition.size();i++){

                float scaleImageCenterX =  (mCopies.getRealCopiesList().get(i).getWidth()) / 2f;
                float scaleImageCenterY = (mCopies.getRealCopiesList().get(i).getHeight()) / 2f;


                float divx = 1f-mCopiesMatrix.getCopiesMatrixList().get(i).getScaleV().x;
                float divy = 1f-mCopiesMatrix.getCopiesMatrixList().get(i).getScaleV().y;

                float fx = (scaleImageCenterX*divx);
                float fy = (scaleImageCenterY*divy);


                RectF rectF = new RectF( mCopiesPosition.CopiesPositionList().get(i).x+fx, mCopiesPosition.CopiesPositionList().get(i).y+fy,
                        ( mCopiesPosition.CopiesPositionList().get(i).x+fx)+(mCopies.getRealCopiesList().get(i).getWidth()
                                *mCopiesMatrix.getCopiesMatrixList().get(i).getScaleV().x),
                        ( mCopiesPosition.CopiesPositionList().get(i).y+fy)+(mCopies.getRealCopiesList().get(i).getHeight()
                                *mCopiesMatrix.getCopiesMatrixList().get(i).getScaleV().y));
                if (rectF.contains(x, y)
                        &&!isClicked(x, y, RectDst.left-(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX()))
                        &&!isClicked(x, y, RectDst.right+(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX()))
                        &&!isClicked(x, y, RectDst.right+(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX()))
                        &&!isClicked(x, y, RectDst.left-(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX()))){

                    mode = DRAG;
                    mCopies.setIndex(i);
                    mCopiesPosition.setIndex(i);
                    mCopiesPaints.setIndex(i);
                    mCopiesMatrix.setIndex(i);

                    X = mCopiesPosition.CopyPosition().x;
                    Y = mCopiesPosition.CopyPosition().y;

                    RealTimeX = mCopiesPosition.RealCopyPosition().x;
                    RealTimeY = mCopiesPosition.RealCopyPosition().y;

                    MoveCopyB = true;
                    ShowCadre = true;
                    ((Select_crop_Activity) mContext).OpacitySeekBar.setProgress(getOpacity());
                    //((Select_crop_Activity) mContext).RotateSeekBar.setProgress(getRotateDegree());
                }else {
                    ShowCadre = false;
                }

            }

            float scaleImageCenterX =  (mCopies.getRealBitmap().getWidth()) / 2f;
            float scaleImageCenterY = (mCopies.getRealBitmap().getHeight()) / 2f;


            float divx = 1f-mCopiesMatrix.getScaleValue().x;
            float divy = 1f-mCopiesMatrix.getScaleValue().y;

            float fx = (scaleImageCenterX*divx);
            float fy = (scaleImageCenterY*divy);


            if (x >= mCopiesPosition.CopyPosition().x+fx && x <=  (mCopies.getRealBitmap().getWidth()*mCopiesMatrix.getScaleValue().x) + (mCopiesPosition.CopyPosition().x+fx) &&
                    y >= mCopiesPosition.CopyPosition().y+fy && y <= (mCopies.getRealBitmap().getHeight()*mCopiesMatrix.getScaleValue().y) + (mCopiesPosition.CopyPosition().y+fy)){
                MoveCopyB = true;
                ShowCadre = true;
            }else {
                ShowCadre = false;
            }
        }else {
            ((Select_crop_Activity) mContext).ShowOptions(false);
            onSelectArea.OnDown(x,y,zommer.getDefaultMatrix(),mBitmapRect,isInPaste,mCropCutShapeHandel,ModeGlobal);

           // TODO: 9/6/2024 for the select area
            if (!ModeGlobal){
                RectF bounds = new RectF();
                SelectTools.getDrawCropPath().computeBounds(bounds, true);
                if (bounds.contains(x, y)){
                    ActionFinished = true;
                }else {
                    ActionFinished = false;
                }
            }else {
                ActionFinished = false;
            }

        }

        if (EraseMode){
            EraseBm = Bitmap.createBitmap(mCopies.getRealBitmap().getWidth(),mCopies.getRealBitmap().getHeight(),
                    mCopies.getRealBitmap().getConfig());
            EraseCanvas = new Canvas(EraseBm);
            EraseCanvas.drawBitmap(mCopies.getRealBitmap(),0,0,null);

            if (zommer.readyToDraw()){
                X_YFixer xAndyFixer1 = new X_YFixer();
                ErasePath.moveTo(
                        xAndyFixer1.FixXYUsingMatrix(x,y, mCopiesMatrix.CopyMatrix().getMatrix(),mCopiesMatrix.getRotationDegree()).x,
                        xAndyFixer1.FixXYUsingMatrix(x,y,mCopiesMatrix.CopyMatrix().getMatrix(),mCopiesMatrix.getRotationDegree()).y);
                VErasePath.moveTo(x,y);
            }

        }
    }

    /**
     * Handles a {@link MotionEvent#ACTION_MOVE} event
     * @param x the x-coordinate of the move action
     * @param y the y-coordinate of the move action
     */
    private void ActionMove(float x, float y){
        mTouchePoint.add((int) x);
        if (EraseMode){
            if (zommer.readyToDraw()){

                if (ErasePaintHardness ==0f){
                    ErasePaint.setMaskFilter(null);
                    VErasePaint.setMaskFilter(null);

                }else {
                    // Get image matrix values and place them in an array.
                    final float[] matrixValues = new float[9];
                    if (mCopiesMatrix.getCopiesMatrixList().size() > 0) {
                        mCopiesMatrix.CopyMatrix().getMatrix().getValues(matrixValues);

                        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
                        final float scaleX = matrixValues[Matrix.MSCALE_X];

                        ErasePaint.setMaskFilter(new BlurMaskFilter(ErasePaintHardness /(scaleX * zommer.getScaleX()), BlurMaskFilter.Blur.NORMAL));
                        VErasePaint.setMaskFilter(new BlurMaskFilter(ErasePaintHardness /zommer.getScaleX(), BlurMaskFilter.Blur.NORMAL));
                    }
                }


                X_YFixer xAndyFixer1 = new X_YFixer();
                ErasePath.lineTo(
                        xAndyFixer1.FixXYUsingMatrix(x,y, mCopiesMatrix.CopyMatrix().getMatrix(),mCopiesMatrix.getRotationDegree()).x,
                        xAndyFixer1.FixXYUsingMatrix(x,y,mCopiesMatrix.CopyMatrix().getMatrix(),mCopiesMatrix.getRotationDegree()).y);
                VErasePath.lineTo(x,y);
            }
        }else {
            if (mScale){
                float dvx=mCopiesMatrix.getRScaleValue().x/mCopiesMatrix.getScaleValue().x;
                float dvy=mCopiesMatrix.getRScaleValue().y/mCopiesMatrix.getScaleValue().y;

                /*
                if (x>mScaleSource){
                    PointF RealXyScale = new PointF();
                    PointF DrawXyScale = new PointF();
                    DrawXyScale.set( mCopiesMatrix.getScaleValue().x+0.02f,mCopiesMatrix.getScaleValue().y+0.02f);
                    RealXyScale.set(mCopiesMatrix.getRScaleValue().x+(0.02f*dvx),mCopiesMatrix.getRScaleValue().y+(0.02f*dvy));
                    setScale(DrawXyScale,RealXyScale);

                }else if (mScaleSource>x){
                    PointF RealXyScale = new PointF();
                    PointF DrawXyScale = new PointF();
                    DrawXyScale.set( mCopiesMatrix.getScaleValue().x-0.02f,mCopiesMatrix.getScaleValue().y-0.02f);
                    RealXyScale.set(mCopiesMatrix.getRScaleValue().x-(0.02f*dvx),mCopiesMatrix.getRScaleValue().y-(0.02f*dvy));
                    setScale(DrawXyScale,RealXyScale);
                }
                 */

                float deltaX = x - DownX;
                float deltaY = y-DownY;

                float scaleFactorX = mCopiesMatrix.getScaleValue().x + Math.max(deltaX, deltaY) / Math.max(getWidth(), getHeight());

                float scaleFactorY = mCopiesMatrix.getScaleValue().y + Math.max(deltaX, deltaY) / Math.max(getWidth(), getHeight());

                scaleFactorX = Math.max(scaleFactorX, 0.005f);

                scaleFactorY = Math.max(scaleFactorY, 0.005f);


                PointF DrawXyScale = new PointF();
                PointF RealXyScale = new PointF();

                DrawXyScale.set(scaleFactorX,scaleFactorY);
                RealXyScale.set((scaleFactorX * dvx),  (scaleFactorY * dvy));
                setScale(DrawXyScale, RealXyScale);

            }else if (mRotate){

                int rotationAngleDegrees = 0 ;
                double rotationAngleRadians = Math.atan2(x - (float) getWidth()/2, (float)getHeight()/2 - y);
                rotationAngleDegrees += (int) Math.toDegrees(rotationAngleRadians);

                if (rotationAngleDegrees<0){
                    setRotateDegree(rotationAngleDegrees + 360);
                }else {
                    setRotateDegree(rotationAngleDegrees);

                }

            }else {
                if (isInPaste){
                    // On Move one of The Copied Bitmaps
                    if (!mCopiesPosition.CopiesPositionList().isEmpty()){
                        if (mode == DRAG) {
                            float scaleImageCenterX =  (mCopies.getRealBitmap().getWidth()) / 2f;
                            float scaleImageCenterY = (mCopies.getRealBitmap().getHeight()) / 2f;
                            float divx = 1f-mCopiesMatrix.getScaleValue().x;
                            float divy = 1f-mCopiesMatrix.getScaleValue().y;

                            float fx = (scaleImageCenterX*divx);
                            float fy = (scaleImageCenterY*divy);

                            RectF rectF = new RectF(mCopiesPosition.CopyPosition().x+fx,mCopiesPosition.CopyPosition().y+fy,
                                    (mCopiesPosition.CopyPosition().x+fx)+(mCopies.getRealBitmap().getWidth()*mCopiesMatrix.getScaleValue().x),
                                    (mCopiesPosition.CopyPosition().y+fy)+(mCopies.getRealBitmap().getHeight()*mCopiesMatrix.getScaleValue().y));
                            if (MoveCopyB && rectF.contains(x, y)){
                                X += x - DownX;
                                Y += y - DownY;

                                RealTimeX +=  (x - DownRealTimeX);
                                RealTimeY +=  (y - DownRealTimeY);

                                mCopiesPosition.updatePosition(X,Y,RealTimeX,RealTimeY);
                                mCopiesMatrix.updatePosition(X,Y,mCopies.getRealBitmap());

                                DownX = x;
                                DownY = y;

                                DownRealTimeX = x;
                                DownRealTimeY = y;

                                ShowCadre = true;
                        }

                        }else {
                            ShowCadre = false;
                        }
                    }
                    // Normal Select
                }else {
                    if (zommer.readyToDraw()) {
                        if (mTouchePoint.size()>2){
                            onSelectArea.OnMove(x, y,zommer.getDefaultMatrix(),isInPaste, mCropCutShapeHandel);
                        }
                    }
                }
            }
        }

    }

    /**
     * Handles a {@link MotionEvent#ACTION_UP} event
     * @param y the y-coordinate of the up action
     * @param x the x-coordinate of the up action
     */
    private void ActionUp( float x, float y){
        mRotate = false;
        mScale = false;
        ColorLoc = false;
        //mScaleSource = x;

        if (isInPaste){
            if (mColor!=null){
                setCopyBackgroundColor(mColor);
            }
            mColor = null;
        }
        if (EraseMode){
            if (Erase){
                ErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                ErasePaint.setAlpha( ErasePaintOpacity);
                EraseCanvas.drawPath(ErasePath,ErasePaint);
            }else {
                ErasePaint.setXfermode(null);

                ErasePaint.setAlpha( ErasePaintOpacity);

                EraseCanvas.drawPath(ErasePath,ErasePaint);

                ErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                ErasePaint.setAlpha(255);

                EraseCanvas.drawBitmap(mCopies.getRealBitmapNt(),0,0,ErasePaint);
            }
            mCopies.UpdateRealBitmap(EraseBm);

            ErasePath.reset();
            VErasePath.reset();
        }else{
            if (zommer.readyToDraw()) {
                if (mCropCutShapeHandel==CropCutShape.OVAL | mCropCutShapeHandel==CropCutShape.RECTANGLE){
                    onSelectArea.OnUp(x,y,zommer.getDefaultMatrix(), isInPaste,mCropCutShapeHandel,ModeGlobal);
                }
                if (mCropCutShapeHandel==CropCutShape.FREE ){
                    if ( mTouchePoint.size()>12){
                        onSelectArea.OnUp(x,y,zommer.getDefaultMatrix(),isInPaste,mCropCutShapeHandel,ModeGlobal);
                    }
                }
            }

            if (!isInPaste && AreaSelected()){
                ((Select_crop_Activity) mContext).ShowOptions(true);

            }
        }
        ActionFinished = true;
    }

    // Draw Copies Method ////////////////////////////////////////////////////////////////////////////////

    /**
     * Draw The List of Bitmaps < Copy and Past Methods >
     * @param canvas to draw Copied Bitmap List
     */
    private void DrawCopiedBm(Canvas canvas){
        if (isInPaste){
            Paint PaintOfRect = new Paint(Paint.ANTI_ALIAS_FLAG);
            PaintOfRect.setColor(Color.parseColor("#A6A061"));
            PaintOfRect.setStyle(Paint.Style.STROKE);
            PaintOfRect.setStrokeWidth(2/zommer.getScaleX());

            Paint PaintOfCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
            PaintOfCircle.setColor(Color.parseColor("#A6A061"));

            for (int i = 0; i <mCopies.copiesNumber();i++){
                canvas.drawBitmap(mCopies.getRealCopiesList().get(i),
                        mCopiesMatrix.getCopiesMatrixList().get(i).getMatrix(),
                       mCopiesPaints.getCopiesPaintList().get(i));
            }

            if ( !mCopiesPosition.CopiesPositionList().isEmpty()){

                float scaleImageCenterX =  (mCopies.getRealBitmap().getWidth()) / 2f;
                float scaleImageCenterY = (mCopies.getRealBitmap().getHeight()) / 2f;


                float divx = 1f-mCopiesMatrix.getScaleValue().x;
                float divy = 1f-mCopiesMatrix.getScaleValue().y;

                float fx = (scaleImageCenterX*divx);
                float fy = (scaleImageCenterY*divy);

                rightCopy = (mCopiesPosition.CopyPosition().x+fx) +  (mCopies.getRealBitmap().getWidth()*mCopiesMatrix.getScaleValue().x);
                bottomCopy = (mCopiesPosition.CopyPosition().y+fy) +  (mCopies.getRealBitmap().getHeight()*mCopiesMatrix.getScaleValue().y);

                RectDst = new RectF(mCopiesPosition.CopyPosition().x+fx,
                        mCopiesPosition.CopyPosition().y+fy,
                        rightCopy,bottomCopy);

                    if (ShowCadre){
                        if (EraseMode){
                            canvas.drawRect(RectDst,PaintOfRect);
                        }else {
                            canvas.drawRect(RectDst,PaintOfRect);
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

    // Set and Get Methods /////////////////////////////////////////////////////////////////////////

    /**
     * Select Crop Or Cut Shape;
     * @param cropCutShape The Type Of Shape
     */
    public void RectOrOval(CropCutShape cropCutShape){
        mCropCutShape = cropCutShape;
    }

    private CropCutShape RectOrOval(){
        return mCropCutShape;
    }

    public void setEraseMode(boolean m){
        EraseMode = m;
        ShowCadre = true;
        for (int i = 0; i <mCopies.copiesNumber(); i++) {
            if (EraseMode){
                mCopiesMatrix.setTRotation(i,0,
                        mCopiesPosition.CopiesPositionList().get(i).x,
                        mCopiesPosition.CopiesPositionList().get(i).y,
                        mCopies.getRealCopiesList().get(i));
            }else {
                mCopiesMatrix.setTRotation(i,mCopiesMatrix.getCopiesMatrixList().get(i).getRotateV(),
                        mCopiesPosition.CopiesPositionList().get(i).x,
                        mCopiesPosition.CopiesPositionList().get(i).y,
                        mCopies.getRealCopiesList().get(i));
            }

        }
        invalidate();
    }

    public void setEraseCopiedBmSize(int size) {
        ErasePaintSTROKEWIDTH = size;
        invalidate();
    }

    public void setEraseCopiedBmOpacity(int opacity) {
        ErasePaintOpacity = opacity;
        invalidate();
    }

    public void setEraseCopiedBmHardness(float hardness) {
        ErasePaintHardness = hardness;
        invalidate();

    }

    public void setE_R(boolean b) {
        Erase = b;
        invalidate();
    }

    public boolean getE_R(){
        return Erase;
    }

    public void DoneErase() {
        EraseMode = false;
        for (int i = 0; i <mCopies.copiesNumber(); i++) {
            mCopiesMatrix.setTRotation(i,mCopiesMatrix.getCopiesMatrixList().get(i).getRotateV(),
                    mCopiesPosition.CopiesPositionList().get(i).x,
                    mCopiesPosition.CopiesPositionList().get(i).y,
                    mCopies.getRealCopiesList().get(i));
        }
        invalidate();
    }
    public void CancelErase() {
        EraseMode = false;
        mCopies.UpdateRealBitmap(mCopies.getRealBitmapNt());
        for (int i = 0; i <mCopies.copiesNumber(); i++) {
            mCopiesMatrix.setTRotation(i,mCopiesMatrix.getCopiesMatrixList().get(i).getRotateV(),
                    mCopiesPosition.CopiesPositionList().get(i).x,
                    mCopiesPosition.CopiesPositionList().get(i).y,
                    mCopies.getRealCopiesList().get(i));
        }

        invalidate();
    }

    /**
     *
     * @param Global type Of Mode of selection
     */
    public void setModeGlobal(boolean Global){
        ModeGlobal = Global;
    }

    /**
     *
     * @return The Mode Case if is it true or false
     */
    public boolean isModeGlobal() {
        return ModeGlobal;
    }


    // Crop Method ////////////////////////////////////////////////////////////////////////

    /**
     * The final bitmap Of The Cropped Bitmap Area Using Path.
     * For More info Visit : <a href="https://stackoverflow.com/a/12089127">...</a>.
     * @return The Final Free Cropped Image.
     */
    public Bitmap getCroppedImage(){

        Bitmap output = mBitmap;

        // Create empty Bitmap to hold the Cropped Area.
        Bitmap fullScreenBitmap = Bitmap.createBitmap(output.getWidth(), output.getHeight(), output.getConfig());
        Canvas canvas = new Canvas(fullScreenBitmap);

        // Crop out the selected portion of the image...
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        canvas.drawPath(SelectTools.getCropPath(), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, 0, 0, paint);

        // Create a bitmap with just the cropped area.
        Region region = new Region();
        Region clip = new Region(0, 0, fullScreenBitmap.getWidth(), fullScreenBitmap.getHeight());
        region.setPath(SelectTools.getCropPath(), clip);
        Rect bounds = region.getBounds();

        return Bitmap.createBitmap(fullScreenBitmap, bounds.left, bounds.top, bounds.width(), bounds.height());

    }


    // Cut Method /////////////////////////////////////////////////////////////////////////

    /**
     * The final bitmap Of The Cropped Bitmap Area Using Path.
     * For More info Visit : <a href="https://stackoverflow.com/a/12089127">...</a>.
     * @return The Final Free Cropped Image.
     */
    public Bitmap getCutImage(){

        Bitmap output = mBitmap;

        //... for more information Visit --> https://stackoverflow.com/a/54041053

        // Create empty Bitmap to hold the Cropped Area.
        Bitmap fullScreenBitmap = Bitmap.createBitmap(output.getWidth(), output.getHeight(), output.getConfig());
        Canvas canvas = new Canvas(fullScreenBitmap);

        // Scale The path to fit The real Bitmap
        Path path = new Path(SelectTools.getCropPath());

        // Cut out the selected portion of the image...
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        Paint paintt = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        canvas.drawBitmap(output,0,0,paintt);
        canvas.drawPath(path, paint);

        return fullScreenBitmap;
    }


    public boolean AreaSelected(){
        Path path = new Path();
        path.set(SelectTools.getCropPath());

        Region region = new Region();
        Region clip = new Region(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        region.setPath(path, clip);
        Rect bounds = region.getBounds();
        if ( ! (bounds.width()<=2 || bounds.height() <=2)){
            return true;
        }else {
            return false;
        }

    }

    // Copy And Past Methods ///////////////////////////////////////////////////////////////////////
    /**
     * Copy The selected Area
     */
    public void Copy (){
        isCopied = true;

        Region region = new Region();
        Region clip = new Region(0, 0, getWidth(), getHeight());
        region.setPath(SelectTools.getScaledCropPath(), clip);
        Rect bounds = region.getBounds();

        mCopies.AddCopy(getCopiedImage());
        mCopiesPaints.AddPaint();


        float dx = (float) getScaledCopiedImageV1().getWidth() / (float) getCopiedImage().getWidth();
        float dy = (float) getScaledCopiedImageV1().getHeight() / (float) getCopiedImage().getHeight();

        float scaleImageCenterX =  (mCopies.getRealBitmap().getWidth()) / 2f;
        float scaleImageCenterY = (mCopies.getRealBitmap().getHeight()) / 2f;


        float divx = 1f-dx;
        float divy = 1f-dy;

        float fx = (scaleImageCenterX*divx);
        float fy = (scaleImageCenterY*divy);
        mCopiesPosition.AddPosition( (bounds.left-fx), (bounds.top-fy),bounds.left,bounds.top);


        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(0,(mCopies.getRealBitmap().getWidth())/2f,(mCopies.getRealBitmap().getHeight())/2f);
        matrix.postScale(dx,dy,(mCopies.getRealBitmap().getWidth())/2f,(mCopies.getRealBitmap().getHeight())/2f);
        matrix.postTranslate(bounds.left-fx, bounds.top-fy);

        PointF pointF2 = new PointF(1,1);
        PointF pointF = new PointF(dx,dy);
        mCopiesMatrix.AddMatrix(matrix,0,pointF,pointF2);

        invalidate();
    }

    /**
     * past the Copied Bitmap
     */
    public void Past(){
        isInPaste = true;
        SelectTools.ClearArea();
        if (ModeGlobal){
            mCropCutShapeHandel = null;
        }
        invalidate();
        ((Select_crop_Activity) mContext).HideToPosition(true);
    }

    /**
     *
     * @return
     *
     */
    public boolean isCopied() {
        return isCopied;
    }

    /**
     *
     * @return
     */
    public boolean isInPaste() {
        return isInPaste;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *
     */
    public void RemoveCopy(){
        if (mCopies.copiesNumber() != 1){
            mCopies.removeCopy();
            mCopiesPosition.removePosition();
            mCopiesPaints.removePaint();
            mCopiesMatrix.removeMatrix();
        invalidate();
        }else {
            Toast.makeText(mContext, "This is The Original Copy", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     */
    public void AddCopy(){
        if (mCopies.copiesNumber()<=9){
            X =  (mCopiesPosition.CopyPosition().x+50);
            Y =  (mCopiesPosition.CopyPosition().y+50);

            mCopies.AddCopy(mCopies.getRealBitmap());
            mCopiesPosition.AddPosition(mCopiesPosition.CopyPosition().x + 50,
                    mCopiesPosition.CopyPosition().y + 50,mCopiesPosition.RealCopyPosition().x + 50,
                    mCopiesPosition.RealCopyPosition().y + 50);
            mCopiesPaints.AddPaint();

            Matrix matrix = new Matrix();
            float dx = (float) getScaledCopiedImageV1().getWidth() / (float) getCopiedImage().getWidth();
            float dy = (float) getScaledCopiedImageV1().getHeight() / (float) getCopiedImage().getHeight();
            matrix.reset();
            matrix.postRotate(0,(mCopies.getRealBitmap().getWidth())/2f,(mCopies.getRealBitmap().getHeight())/2f);
            matrix.postScale(dx,dy,(mCopies.getRealBitmap().getWidth())/2f,(mCopies.getRealBitmap().getHeight())/2f);
            matrix.postTranslate(mCopiesPosition.CopyPosition().x + 50,
                    mCopiesPosition.CopyPosition().y + 50);

            PointF pointF2 = new PointF(1,1);
            PointF pointF = new PointF(dx,dy);
            mCopiesMatrix.AddMatrix(matrix,0,pointF,pointF2);
        }else {
            Toast.makeText(mContext, "10 Copy is The limit !", Toast.LENGTH_SHORT).show();
        }
        invalidate();
    }

    // Copies Paint Setting ////////////////////////////////////////////////////////////////////////
    public void setOpacity(int Opacity){
        mCopiesPaints.setOpacity(Opacity);
        invalidate();
    }

    public int getOpacity(){
        return mCopiesPaints.getOpacity();
    }

    public void setRotateDegree(int degree) {
        mCopiesMatrix.setRotation(degree,mCopies.getRealBitmap(),mCopiesPosition.CopyPosition().x,mCopiesPosition.CopyPosition().y);
        invalidate();
    }


    public int getRotateDegree() {
        return mCopiesMatrix.getRotationDegree();
    }

    public void setScale(PointF scale,PointF RealScale) {
        mCopiesMatrix.setScale(scale,RealScale,mCopiesPosition.CopyPosition().x,mCopiesPosition.CopyPosition().y,mCopies.getRealBitmap());
        invalidate();
    }

    public void flipV(){
        Matrix Rm = new Matrix();
        int Rx = mCopies.getRealBitmap().getHeight()/2;
        int Ry = mCopies.getRealBitmap().getWidth()/2;
        Rm.setScale(1, -1, Rx, Ry);
        Bitmap RBitmap = Bitmap.createBitmap(mCopies.getRealBitmap(), 0, 0,
                mCopies.getRealBitmap().getWidth(),
                mCopies.getRealBitmap().getHeight(),
                Rm, true);
        mCopies.UpdateRealBitmap(RBitmap);
        invalidate();
    }

    public void flipH(){

        Matrix Rm = new Matrix();
        int Rx = mCopies.getRealBitmap().getHeight()/2;
        int Ry = mCopies.getRealBitmap().getWidth()/2;
        Rm.setScale(-1, 1, Rx, Ry);

        Bitmap RBitmap = Bitmap.createBitmap(mCopies.getRealBitmap(), 0, 0,
                mCopies.getRealBitmap().getWidth(),
                mCopies.getRealBitmap().getHeight(),
                Rm, true);

        mCopies.UpdateRealBitmap(RBitmap);
        invalidate();
    }

    public void RotateR(){
        setRotateDegree(mCopiesMatrix.getRotationDegree()+90);
        invalidate();
    }
    public void RotateL(){
        setRotateDegree(mCopiesMatrix.getRotationDegree()-90);
        invalidate();
    }

    public void setCopyMode(PorterDuff.Mode Mode){
        mCopiesPaints.setXfermode(Mode);
        invalidate();
    }


    public void setCopyBackgroundColor(int color) {
        mCopiesPaints.setImageColor(color);
        invalidate();
    }

    public void setImagesColorMode(PorterDuff.Mode mode) {
        mCopiesPaints.setImagesColorMode(mode);
        invalidate();
    }
    public void setColorLoc(boolean b) {
        ColorLoc = b;
        invalidate();
    }

    /**
     * The final bitmap Of The Cropped Bitmap Area Using Path.
     * For More info Visit : <a href="https://stackoverflow.com/a/12089127">...</a>.
     * @return The Final Free Cropped Image.
     */
    public Bitmap getScaledCopiedImageV1(){

        // Create a Scaled Copy of bitmap with just the cropped area.
        Region ScaledRegion= new Region();
        Region ScaledClip = new Region(0, 0, getWidth(), getHeight());
        ScaledRegion.setPath(SelectTools.getScaledCropPath(), ScaledClip);
        Rect ScaledBounds = ScaledRegion.getBounds();
        return Bitmap.createScaledBitmap(getCopiedImage(),ScaledBounds.width(),ScaledBounds.height(),true);
    }

    /**
     * The final bitmap Of The Cropped Bitmap Area Using Path.
     * For More info Visit : <a href="https://stackoverflow.com/a/12089127">...</a>.
     * @return The Final Free Cropped Image.
     */
    public Bitmap getCopiedImage(){

        Bitmap output = mBitmap;

        // Create empty Bitmap to hold the Cropped Area.
        Bitmap fullScreenBitmap = Bitmap.createBitmap(output.getWidth(), output.getHeight(), output.getConfig());
        Canvas canvas = new Canvas(fullScreenBitmap);

        Path path = new Path(SelectTools.getCropPath());

        // Crop out the selected portion of the image...
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, 0, 0, paint);

        // Create a bitmap with just the cropped area.
        Region region = new Region();
        Region clip = new Region(0, 0, fullScreenBitmap.getWidth(), fullScreenBitmap.getHeight());
        region.setPath(path, clip);
        Rect bounds = region.getBounds();

        return Bitmap.createBitmap(fullScreenBitmap,  (bounds.left),
                (bounds.top), (bounds.width()),
                (bounds.height()));
    }

   /*
    public Bitmap getCopiedImage() {
    final Drawable drawable = getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }

        Bitmap original = ((BitmapDrawable) drawable).getBitmap();
        Path path = new Path(SelectTools.getCropPath());

        // Get the bounds of the path
        RectF bounds = new RectF();
        path.computeBounds(bounds, true);

        // Create a bitmap of the size of the path bounds
        Bitmap result = Bitmap.createBitmap(Math.round(bounds.width()),
                Math.round(bounds.height()),
                Bitmap.Config.ARGB_8888);

        // Create a canvas and translate it so that the path is drawn at (0,0)
        Canvas canvas = new Canvas(result);
        canvas.translate(-bounds.left, -bounds.top);

        // Clip the canvas to the path
        canvas.clipPath(path);

        // Draw the original bitmap onto the clipped canvas
        canvas.drawBitmap(original, 0, 0, null);

        return result;
    }

    final Drawable drawable = getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }

        // Get the original bitmap object.
        final Bitmap output = ((BitmapDrawable) drawable).getBitmap();

        X_YFixer xAndyFixer = new X_YFixer();

        // Create a mutable copy of the original bitmap
        Bitmap bitmap = output.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

        for (int i = 0; i < mCopies.copiesNumber(); i++) {
            xAndyFixer.FixXYUsingMatrix(mCopiesPosition.RealCopiesPositionList().get(i).x, mCopiesPosition.RealCopiesPositionList().get(i).y,
                    zommer.getDefaultMatrix());

            mCopiesMatrix.setRotationAndScaleAndTranslate(i,
                    mCopies.getRealCopiesList().get(i),
                    mCopiesPosition.RealCopiesPositionList().get(i).x, mCopiesPosition.RealCopiesPositionList().get(i).y);

            // Create a new bitmap for each image, with a white background
            Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            //tempBitmap.eraseColor(Color.WHITE);
            Canvas tempCanvas = new Canvas(tempBitmap);

            if (mCopiesMatrix.getCopiesMatrixList().get(i).getRealScaleV().x==1){
                mCopiesPaints.getCopiesPaintList().get(i).setFilterBitmap(false);
            }
            // Draw the image on the temp canvas
            tempCanvas.drawBitmap(mCopies.getRealCopiesList().get(i), mCopiesMatrix.getCopiesMatrixList().get(i).getMatrix(), null);


            // Create another temporary bitmap to hold the blended result
            Bitmap blendedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas blendedCanvas = new Canvas(blendedBitmap);

            // Draw the current state of the main bitmap onto the blended canvas
            blendedCanvas.drawBitmap(bitmap, 0, 0, null);

            // Draw the temp bitmap onto the blended canvas with the blend mode
            blendedCanvas.drawBitmap(tempBitmap, 0, 0, mCopiesPaints.getCopiesPaintList().get(i));

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
            canvas.drawBitmap(blendedBitmap, 0, 0, null);
        }

        return bitmap;
    */

    /**
     * the final image After Past
     * @return the final Bitmap After Past the CopiedBitmap or The list of CopiedBitmaps
     */
    public Bitmap getFinalAfterPastImage() {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        X_YFixer xAndyFixer = new X_YFixer();

        // Create a mutable copy of the original bitmap
        Bitmap bitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

        for (int i = 0; i < mCopies.copiesNumber(); i++) {
            xAndyFixer.FixXYUsingMatrix(mCopiesPosition.RealCopiesPositionList().get(i).x,mCopiesPosition.RealCopiesPositionList().get(i).y,
                    zommer.getDefaultMatrix());

            mCopiesMatrix.setRotationAndScaleAndTranslate(i,
                    mCopies.getRealCopiesList().get(i),
                    (xAndyFixer.getFixedXYUsingMartix().x),
                    (xAndyFixer.getFixedXYUsingMartix().y));

            // Create a new bitmap for each image, with a white background
            Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            //tempBitmap.eraseColor(Color.WHITE);
            Canvas tempCanvas = new Canvas(tempBitmap);

            // Draw the image on the temp canvas
            tempCanvas.drawBitmap(mCopies.getRealCopiesList().get(i), mCopiesMatrix.getCopiesMatrixList().get(i).getMatrix(), paint);


            // Create another temporary bitmap to hold the blended result
            Bitmap blendedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas blendedCanvas = new Canvas(blendedBitmap);

            // Draw the current state of the main bitmap onto the blended canvas
            blendedCanvas.drawBitmap(bitmap, 0, 0, paint);

            // Draw the temp bitmap onto the blended canvas with the blend mode
            blendedCanvas.drawBitmap(tempBitmap, 0, 0, mCopiesPaints.getCopiesPaintList().get(i));

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

    public Bitmap getCurrentImage() {
        return mCopies.getRealBitmap();
    }

    /**
     * Done From Pasting
     */
    public void DonePast(){
        ShowCadre = true;
        isInPaste = false;
        MoveCopyB = false;
        isCopied = false;
        EraseMode = false;
        for (int i = 0; i <mCopies.copiesNumber(); i++) {
            mCopiesMatrix.setTRotation(i,mCopiesMatrix.getCopiesMatrixList().get(i).getRotateV(),
                    mCopiesPosition.CopiesPositionList().get(i).x,
                    mCopiesPosition.CopiesPositionList().get(i).y,
                    mCopies.getRealCopiesList().get(i));
        }
        ((Select_crop_Activity) mContext).HideToPosition(false);

        clear();
        onSelectArea.reset();
        RectDst.setEmpty();
        invalidate();
    }

    public void clear(){
        mColor = null;
        mCopies.copiesListClear();
        mCopiesPosition.clearPositions();
        mCopiesPaints.clearPaints();
        mCopiesMatrix.clearMatrices();

        mCopies.unloadingIndex();
        mCopiesPosition.unloadingIndex();
        mCopiesPaints.unloadingIndex();
        mCopiesMatrix.unloadingIndex();
    }


    /**
     * Cancel the Past Process
     */
    public void CancelPast(){
        ShowCadre = true;
        isInPaste = false;
        MoveCopyB = false;
        isCopied = false;
        EraseMode = false;
        ((Select_crop_Activity) mContext).HideToPosition(false);
        clear();

        onSelectArea.reset();

        RectDst.setEmpty();
        invalidate();
    }


    /**
     * Cancel the Past Process
     */
    public void Clear(){
        ShowCadre = true;
        isInPaste = false;
        MoveCopyB = false;
        isCopied = false;

        onSelectArea.reset();

        RectDst.setEmpty();
        invalidate();
    }

    public void resetCut(){
        onSelectArea.reset();
        SelectTools.setAreaEmpty();
        SelectTools.ClearArea();
    }
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

    /**
     * if The Bitmap is Valid for editing and cropping
     * @param H bitmap height
     * @param W bitmap width
     */
    private boolean BitmapValidity(int H, int W, Context context){
        if (H <= 1 & W <= 1){
            Log.d(TAG, "getCroppedImage: Bitmap is Small");
            Toast.makeText(context, "OopsThe image height and Width is small", Toast.LENGTH_SHORT).show();
            return true ;
        }else if (H <= 1){
            Log.d(TAG, "getCroppedImage: Bitmap is Small");
            Toast.makeText(context, "Oops,The image height is small", Toast.LENGTH_SHORT).show();
            return true;
        }else if (W <= 1){
            Log.d(TAG, "getCroppedImage: Bitmap is Small");
            Toast.makeText(context, "Oops,The image Width is small", Toast.LENGTH_SHORT).show();
            return true;
        }else {
            return false;
        }
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
         * @param selectImageView
         */
        void ClickTopLeft(boolean b, SelectImageView selectImageView);

        /**
         *
         * @param b
         * @param selectImageView
         */
        void ClickTopRight(boolean b, SelectImageView selectImageView);

        /**
         *
         * @param b
         * @param selectImageView
         */
        void ClickBottomRight(boolean b, SelectImageView selectImageView);

        /**
         *
         * @param b
         * @param selectImageView
         */
        void ClickBottomLeft(boolean b, SelectImageView selectImageView);
    }

    /**
     *  for more Visit --->  <a href="https://stackoverflow.com/a/16792888">...</a>  & ---> <a href="https://stackoverflow.com/a/36146901">...</a>
     * @param downX X position of touche of user Finger in Screen
     * @param downY Y position of touche of user Finger in Screen
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


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector)
        {

            float scaleImageCenterX =  (float) (mCopies.getRealBitmap().getWidth()) / 2;
            float scaleImageCenterY = (float) (mCopies.getRealBitmap().getHeight()) / 2;

            float divx = 1f-mCopiesMatrix.getScaleValue().x;
            float divy = 1f-mCopiesMatrix.getScaleValue().y;

            float fx = (scaleImageCenterX*divx);
            float fy = (scaleImageCenterY*divy);
            if (mCopies.copiesNumber() > 0) {
                if (!EraseMode){
                    mxAndyFixer.FixXYUsingMatrix(detector.getFocusX(), detector.getFocusY(), zommer.getCanvasMatrix());
                    for (int i = 0; i < mCopies.copiesNumber(); i++) {
                        if ((mxAndyFixer.getFixedXYUsingMartix().x >= (mCopiesPosition.CopiesPositionList().get(i).x+fx) &&
                                mxAndyFixer.getFixedXYUsingMartix().x <= (((float) mCopies.getRealCopiesList().get(i).getWidth()) *
                                        (mCopiesMatrix.getCopiesMatrixList().get(i).getScaleV().x)) +
                                        (mCopiesPosition.CopiesPositionList().get(i).x+fx))&&

                                ( mxAndyFixer.getFixedXYUsingMartix().y >= (mCopiesPosition.CopiesPositionList().get(i).y+fy) &&
                                        mxAndyFixer.getFixedXYUsingMartix().y <= ((float) mCopies.getRealCopiesList().get(i).getHeight() *
                                                (mCopiesMatrix.getCopiesMatrixList().get(i).getScaleV().y)) +
                                                (mCopiesPosition.CopiesPositionList().get(i).y+fy))) {

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

                PointF DrawXyScale = new PointF();
                PointF RealXyScale = new PointF();



                // Calculate the new scale factors
                float newScaleX = mCopiesMatrix.getScaleValue().x * detector.getScaleFactor();
                float newScaleY = mCopiesMatrix.getScaleValue().y * detector.getScaleFactor();

                // Clamp the scale factors to a minimum of 0.001
                newScaleX = Math.max(newScaleX, 0.005f);
                newScaleY = Math.max(newScaleY, 0.005f);


                // Calculate the new real scale factors
                float newRealScaleX = mCopiesMatrix.getRScaleValue().x * detector.getScaleFactor();
                float newRealScaleY = mCopiesMatrix.getRScaleValue().y * detector.getScaleFactor();

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
}
/*
 float scaleImageCenterX =  (mCopies.getRealCopiesList().get(i).getWidth()) / 2f;
                float scaleImageCenterY = (mCopies.getRealCopiesList().get(i).getHeight()) / 2f;


                float divx = 1f-mCopiesMatrix.getCopiesMatrixList().get(i).getRealScaleV().x;
                float divy = 1f-mCopiesMatrix.getCopiesMatrixList().get(i).getRealScaleV().y;

                float fx = (scaleImageCenterX*divx);
                float fy = (scaleImageCenterY*divy);

                canvas.drawRect(mCopiesPosition.CopiesPositionList().get(i).x+fx,
                        mCopiesPosition.CopiesPositionList().get(i).y+fy,
                        (mCopiesPosition.CopiesPositionList().get(i).x+fx) +
                                (mCopies.getRealCopiesList().get(i).getWidth()*mCopiesMatrix.getCopiesMatrixList().get(i).getRealScaleV().x),
                        (mCopiesPosition.CopiesPositionList().get(i).y+fy) +
                                (mCopies.getRealCopiesList().get(i).getHeight()*mCopiesMatrix.getCopiesMatrixList().get(i).getRealScaleV().y),
                        mCopiesPaints.getRectOnTopPaint().get(i));
 */