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
package com.phantomhive.exil.hellopics.Img_Editor.Views.AdjustImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.X_YFixer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageAdjustmentView extends View {

    private Paint paint;

    ViewZommer zommer;
    private Bitmap originalBitmap;
    private Bitmap adjustedBitmap;
    private Mat originalMat;
    private Mat adjustedMat;

    ViewZommer Scaledzommer;
    private Bitmap ScaledoriginalBitmap;
    private Bitmap ScaledadjustedBitmap;
    private Mat ScaledoriginalMat;
    private Mat ScaledadjustedMat;

    // Adjustment parameters with their ranges
    private float brightness = 0;    // Range: -100 to 100
    private float contrast = 0;      // Range: 0.5 to 1.5
    private float saturation = 0;    // Range: -1 to 1
    private float Sharpness = 0;       // Range: 0 to 5
    private float highlights = 0;    // Range: -100 to 100
    private float shadows = 0;       // Range: -100 to 100
    private float temperature = 0;   // Range: -100 to 100
    private float grain = 0;         // Range: 0 to 1.5
    private float fade = 0;          // Range: 0 to 1
    private float vibrance = 0;      // Range: -1 to 1
    private float exposure = 0;      // Range: -1 to 1
    private float vignette = 0;      // Range: -1 to 1
    private float hue = 0;            //Range: -180 to 180
    private float tint = 0;           //Range: -100 to 100

    float [] filter = new float[14];
    private boolean EraseMode = false;
    private boolean Restore = false;
    private X_YFixer AdjustedBmxAndyFixer;

    private Paint DrawErasePaint;
    private float PaintSTROKEWIDTH = 30f;
    private float AdjustmentHardness = 45f;
    private int opacity =255;
    private Paint AdjustedBmErasePaint;
    private Paint ScaledAdjustedBmErasePaint;

    private Path DrawErasePath;
    private Path AdjustedBmErasePath;
    private Path ScaledAdjustedBmErasePath;

    private Canvas ScaledAdjustedBmCanvas;
    private Canvas AdjustedBmCanvas;

    Bitmap AdjustedBmEraseBm;
    Bitmap ScaledAdjustedBmEraseBm;

    private ExecutorService executor;
    private Handler mainHandler;
    private Runnable adjustmentRunnable;
    private static final long DEBOUNCE_DELAY = 200; // milliseconds
    boolean doneHandling = true;

    public ImageAdjustmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        DrawErasePath = new Path();

        DrawErasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        DrawErasePaint.setColor(Color.parseColor("#A6A061"));
        DrawErasePaint.setAntiAlias(true);
        DrawErasePaint.setAlpha(127);
        DrawErasePaint.setStrokeWidth(PaintSTROKEWIDTH);
        DrawErasePaint.setStyle(Paint.Style.STROKE);
        DrawErasePaint.setStrokeJoin(Paint.Join.ROUND);
        DrawErasePaint.setStrokeCap(Paint.Cap.ROUND);

        ScaledAdjustedBmErasePath = new Path();
        AdjustedBmErasePath = new Path();

        AdjustedBmErasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        AdjustedBmErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        AdjustedBmErasePaint.setAntiAlias(true);
        AdjustedBmErasePaint.setStrokeWidth(PaintSTROKEWIDTH);
        AdjustedBmErasePaint.setStyle(Paint.Style.STROKE);
        AdjustedBmErasePaint.setStrokeJoin(Paint.Join.ROUND);
        AdjustedBmErasePaint.setStrokeCap(Paint.Cap.ROUND);

        ScaledAdjustedBmErasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ScaledAdjustedBmErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        ScaledAdjustedBmErasePaint.setAntiAlias(true);
        ScaledAdjustedBmErasePaint.setStrokeWidth(PaintSTROKEWIDTH);
        ScaledAdjustedBmErasePaint.setStyle(Paint.Style.STROKE);
        ScaledAdjustedBmErasePaint.setStrokeJoin(Paint.Join.ROUND);
        ScaledAdjustedBmErasePaint.setStrokeCap(Paint.Cap.ROUND);


        AdjustedBmxAndyFixer = new X_YFixer();
        zommer = new ViewZommer(this,context);
        Scaledzommer = new ViewZommer(this,context);
        System.loadLibrary("opencv_java4");

        executor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        zommer.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Scaledzommer.onMeasure(widthMeasureSpec, heightMeasureSpec);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ScaledoriginalBitmap = getScaledBitmap(originalBitmap).copy(Bitmap.Config.ARGB_8888, true);
        ScaledadjustedBitmap = ScaledoriginalBitmap.copy(Bitmap.Config.ARGB_8888, true);

        ScaledAdjustedBmEraseBm = Bitmap.createBitmap(ScaledadjustedBitmap.getWidth(),ScaledadjustedBitmap.getHeight(),ScaledadjustedBitmap.getConfig());
        ScaledAdjustedBmCanvas = new Canvas(ScaledAdjustedBmEraseBm);

        ScaledoriginalMat = new Mat();
        ScaledadjustedMat = new Mat();

        Scaledzommer.setBitmap(ScaledoriginalBitmap);

        Utils.bitmapToMat(ScaledoriginalBitmap, ScaledoriginalMat);
        Utils.bitmapToMat(ScaledadjustedBitmap, ScaledadjustedMat);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        zommer.OnTouch(event);
        Scaledzommer.OnTouch(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN -> {
                AdjustedBmxAndyFixer.FixXYUsingMatrix(event.getX(), event.getY(), zommer.getCanvasMatrix());
                ActionDown(AdjustedBmxAndyFixer.getFixedXYUsingMartix().x, AdjustedBmxAndyFixer.getFixedXYUsingMartix().y);
                invalidate();
            }
            case MotionEvent.ACTION_MOVE -> {
                AdjustedBmxAndyFixer.FixXYUsingMatrix(event.getX(), event.getY(), zommer.getCanvasMatrix());
                ActionMove(AdjustedBmxAndyFixer.getFixedXYUsingMartix().x, AdjustedBmxAndyFixer.getFixedXYUsingMartix().y);
                invalidate();
            }
            case MotionEvent.ACTION_POINTER_DOWN -> {
                return true;
            }
            case MotionEvent.ACTION_UP,MotionEvent.ACTION_POINTER_UP -> {
                AdjustedBmxAndyFixer.FixXYUsingMatrix(event.getX(), event.getY(), zommer.getCanvasMatrix());
                ActionUp(AdjustedBmxAndyFixer.getFixedXYUsingMartix().x, AdjustedBmxAndyFixer.getFixedXYUsingMartix().y);
                invalidate();
            }
        }
        return true;
    }
    private void ActionDown(float x, float y) {

        if (EraseMode) {
                AdjustedBmErasePaint.setStrokeWidth(PaintSTROKEWIDTH / (zommer.getBmScaleX()));
                ScaledAdjustedBmErasePaint.setStrokeWidth(PaintSTROKEWIDTH / (Scaledzommer.getBmScaleX()));
                DrawErasePaint.setStrokeWidth(PaintSTROKEWIDTH / (zommer.getScaleX()));

                if (zommer.readyToDraw()) {
                    X_YFixer ScaledAdjustedBmxAndyFixer = new X_YFixer();
                    ScaledAdjustedBmxAndyFixer.FixXYUsingMatrix(x, y, Scaledzommer.getDefaultMatrix());

                    X_YFixer AdjustedBmxAndyFixer = new X_YFixer();
                    AdjustedBmxAndyFixer.FixXYUsingMatrix(x, y, zommer.getDefaultMatrix());

                    ScaledAdjustedBmErasePath.moveTo(ScaledAdjustedBmxAndyFixer.getFixedXYUsingMartix().x, ScaledAdjustedBmxAndyFixer.getFixedXYUsingMartix().y);
                    AdjustedBmErasePath.moveTo(AdjustedBmxAndyFixer.getFixedXYUsingMartix().x, AdjustedBmxAndyFixer.getFixedXYUsingMartix().y);
                    DrawErasePath.moveTo(x, y);
                }

            }
        invalidate();
    }

    private void ActionMove(float x, float y) {
        if (EraseMode) {
            if (zommer.readyToDraw()) {


                if (AdjustmentHardness == 0f){
                    DrawErasePaint.setMaskFilter(null);
                    AdjustedBmErasePaint.setMaskFilter(null);
                    ScaledAdjustedBmErasePaint.setMaskFilter(null);

                }else {
                    
                    DrawErasePaint.setMaskFilter(new BlurMaskFilter(AdjustmentHardness /zommer.getScaleX(), BlurMaskFilter.Blur.NORMAL));
                    AdjustedBmErasePaint.setMaskFilter(new BlurMaskFilter(AdjustmentHardness /(zommer.getBmScaleX()), BlurMaskFilter.Blur.NORMAL));
                    ScaledAdjustedBmErasePaint.setMaskFilter(new BlurMaskFilter(AdjustmentHardness /Scaledzommer.getBmScaleX(), BlurMaskFilter.Blur.NORMAL));


                }

                X_YFixer ScaledAdjustedBmxAndyFixer = new X_YFixer();
                ScaledAdjustedBmxAndyFixer.FixXYUsingMatrix(x, y, Scaledzommer.getDefaultMatrix());

                X_YFixer AdjustedBmxAndyFixer = new X_YFixer();
                AdjustedBmxAndyFixer.FixXYUsingMatrix(x, y, zommer.getDefaultMatrix());

                ScaledAdjustedBmErasePath.lineTo(ScaledAdjustedBmxAndyFixer.getFixedXYUsingMartix().x, ScaledAdjustedBmxAndyFixer.getFixedXYUsingMartix().y);
                AdjustedBmErasePath.lineTo(AdjustedBmxAndyFixer.getFixedXYUsingMartix().x, AdjustedBmxAndyFixer.getFixedXYUsingMartix().y);
                DrawErasePath.lineTo(x, y);
            }
        }
        invalidate();
    }

    private void ActionUp(float x, float y) {

        if (EraseMode) {
            if (Restore){
                AdjustedBmErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                ScaledAdjustedBmErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

                AdjustedBmErasePaint.setAlpha(opacity);
                ScaledAdjustedBmErasePaint.setAlpha(opacity);

                ScaledAdjustedBmCanvas.drawPath(ScaledAdjustedBmErasePath, ScaledAdjustedBmErasePaint);
                AdjustedBmCanvas.drawPath(AdjustedBmErasePath, AdjustedBmErasePaint);

                AdjustedBmErasePaint.setXfermode(null);
                ScaledAdjustedBmErasePaint.setXfermode(null);

            }else {
                AdjustedBmErasePaint.setXfermode(null);
                ScaledAdjustedBmErasePaint.setXfermode(null);

                AdjustedBmErasePaint.setAlpha(opacity);
                ScaledAdjustedBmErasePaint.setAlpha(opacity);

                ScaledAdjustedBmCanvas.drawPath(ScaledAdjustedBmErasePath, ScaledAdjustedBmErasePaint);
                AdjustedBmCanvas.drawPath(AdjustedBmErasePath, AdjustedBmErasePaint);

                AdjustedBmErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                ScaledAdjustedBmErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                AdjustedBmErasePaint.setAlpha(255);
                ScaledAdjustedBmErasePaint.setAlpha(255);

                ScaledAdjustedBmCanvas.drawBitmap(ScaledoriginalBitmap, 0, 0, ScaledAdjustedBmErasePaint);
                AdjustedBmCanvas.drawBitmap(originalBitmap, 0, 0, AdjustedBmErasePaint);


            }
            DrawErasePath.reset();
            AdjustedBmErasePath.reset();
            ScaledAdjustedBmErasePath.reset();
        }
        invalidate();
    }


    public void setImage(Bitmap bitmap) {
        zommer.setBitmap(bitmap);

        originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        adjustedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        AdjustedBmEraseBm = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());

        AdjustedBmCanvas = new Canvas(AdjustedBmEraseBm);

        originalMat = new Mat();
        adjustedMat = new Mat();

        Utils.bitmapToMat(originalBitmap, originalMat);
        Utils.bitmapToMat(adjustedBitmap, adjustedMat);

        invalidate();
    }

    public static Bitmap getScaledBitmap(Bitmap mBitmap){
        float division0 = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();
        float division1 = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();
        int FW = (int) (500*division1);
        int FH = (int) (500*division0);
        Bitmap mSBitmap = null;
        if (mBitmap.getWidth()>mBitmap.getHeight() | mBitmap.getWidth()==mBitmap.getHeight()){
            mSBitmap = Bitmap.createScaledBitmap(mBitmap,
                    500 ,FH ,true);


        }else if (mBitmap.getWidth()<mBitmap.getHeight() ){
            if (FH<500) {
                mSBitmap = Bitmap.createScaledBitmap(mBitmap,
                        500 ,FH ,true);

            }else {
                mSBitmap = Bitmap.createScaledBitmap(mBitmap,FW,
                        500 ,true);

            }
        }

        return Bitmap.createBitmap(mSBitmap,0,0,mSBitmap.getWidth(),
                mSBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (doneHandling){
            canvas.setMatrix(zommer.getCanvasMatrix());
            if (adjustedBitmap != null) {
                canvas.drawBitmap(adjustedBitmap,zommer.getDefaultMatrix(), paint);
                canvas.drawBitmap(AdjustedBmEraseBm,zommer.getDefaultMatrix(), paint);
            }
        }else {
            canvas.setMatrix(Scaledzommer.getCanvasMatrix());
            if (ScaledadjustedBitmap != null) {
                canvas.drawBitmap(ScaledadjustedBitmap,Scaledzommer.getDefaultMatrix(), paint);
                canvas.drawBitmap(ScaledAdjustedBmEraseBm,Scaledzommer.getDefaultMatrix(), paint);
            }
        }
        if(EraseMode){
            canvas.drawPath(DrawErasePath,DrawErasePaint);
        }
    }

    private void applyVignette(Bitmap bitmap) {
        if (vignette == 0) return;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float radius = (float) (Math.sqrt(width * width + height * height) / 2f);

        RadialGradient vignetteMask;
        if (vignette>0){
            vignetteMask = new RadialGradient(
                    width / 2f, height / 2f, radius,
                    Color.TRANSPARENT,
                    Color.BLACK,
                    Shader.TileMode.CLAMP);
        }else {
            vignetteMask = new RadialGradient(
                    width / 2f, height / 2f, radius,
                    Color.TRANSPARENT,
                    Color.WHITE,
                    Shader.TileMode.CLAMP);
        }


        Paint vignetteScrimPaint = new Paint();
        vignetteScrimPaint.setShader(vignetteMask);
        vignetteScrimPaint.setAntiAlias(true);
        vignetteScrimPaint.setDither(true);

        // Create a new canvas with the bitmap to draw the vignette
        Canvas bitmapCanvas = new Canvas(bitmap);

        // Set the blend mode and opacity for the vignette effect
        vignetteScrimPaint.setAlpha((int) (255 * Math.abs(vignette)));

        // Draw the vignette onto the bitmap
        bitmapCanvas.drawRect(0, 0, width, height, vignetteScrimPaint);
        invalidate();
    }

    private void debounceAdjustment() {
        mainHandler.removeCallbacks(adjustmentRunnable);
        adjustmentRunnable = () -> executor.execute(this::adjustImage);
        mainHandler.postDelayed(adjustmentRunnable, DEBOUNCE_DELAY);
    }


    private void adjustImage() {
        if (originalMat == null || this.adjustedMat == null) return;
        // Update full resolution bitmap on UI thread
        mainHandler.post(() -> {
        adjustedMat = originalMat.clone();

           //originalMat.copyTo(this.adjustedMat);
        applyAdjustments(adjustedMat);
        // Convert to Bitmap
        Utils.matToBitmap(adjustedMat, adjustedBitmap);
        //*** Apply Vignette ***//
         applyVignette(adjustedBitmap);
        doneHandling = true;
        });
    }

    private void ScaledAdjustImage() {
        doneHandling = false;
        if (ScaledoriginalMat == null || ScaledadjustedMat == null) return;
        // Update full resolution bitmap on UI thread

        ScaledadjustedMat = ScaledoriginalMat.clone();

        //originalMat.copyTo(this.adjustedMat);

        applyAdjustments(ScaledadjustedMat);

        // Convert to Bitmap
        Utils.matToBitmap(ScaledadjustedMat, ScaledadjustedBitmap);

        //*** Apply Vignette ***//
        applyVignette(ScaledadjustedBitmap);
        invalidate();
    }

    public void applyAdjustments(Mat mat) {
        // Check if the image has an alpha channel
        int channels = mat.channels();
        boolean hasAlpha = channels == 4;

        Mat colorMat = new Mat();
        Mat alphaMat = new Mat();

        if (hasAlpha) {
            // Split the channels into B, G, R, and A
            List<Mat> channelList = new ArrayList<>();
            Core.split(mat, channelList);
            // Merge B, G, R channels into colorMat
            Core.merge(channelList.subList(0, 3), colorMat);
            // Extract the alpha channel
            alphaMat = channelList.get(3).clone();
            // Release original channels
            for (Mat m : channelList) m.release();
        } else {
            colorMat = mat.clone();
        }

        // *** Apply brightness ***//
        if (brightness!=0){
            Core.add(colorMat, new Scalar(brightness, brightness, brightness), colorMat);
        }

        //*** Apply contrast ***//
        if (contrast != 0) {
            double alphaContrast = Math.exp(contrast * 0.5f);
            double betaContrast = 128 * (1 - alphaContrast);
            colorMat.convertTo(colorMat, -1, alphaContrast, betaContrast);
        }

        //*** Apply saturation ***/
        if (saturation!=0){
            Mat hsvMat = new Mat();
            Imgproc.cvtColor(colorMat, hsvMat, Imgproc.COLOR_BGR2HSV);
            float adjustedSaturation = saturation + 1.0f;
            Core.multiply(hsvMat, new Scalar(1, adjustedSaturation, 1), hsvMat);
            Imgproc.cvtColor(hsvMat, colorMat, Imgproc.COLOR_HSV2BGR);
            hsvMat.release();
        }

        // *** Apply Sharpness (local Sharpness enhancement) ***//
        if (Sharpness!=0){
            Mat blurredMat = new Mat();
            Imgproc.GaussianBlur(colorMat, blurredMat, new Size(0, 0), 3);
            Mat sharpenedMat = new Mat();
            Core.addWeighted(colorMat, 1 + Sharpness, blurredMat, -Sharpness, 0, sharpenedMat);
            Core.addWeighted(colorMat, 1 - Sharpness * 0.2, sharpenedMat, Sharpness * 0.2, 0, colorMat);
            blurredMat.release();
            sharpenedMat.release();
        }

        // *** Apply highlights and shadows separately ***//
        // Convert to LAB color space
        if (highlights != 0 || shadows != 0) {
            Mat labMat = new Mat();
            Imgproc.cvtColor(colorMat, labMat, Imgproc.COLOR_BGR2Lab);

            List<Mat> labChannels = new ArrayList<>();
            Core.split(labMat, labChannels);
            Mat L = labChannels.get(0).clone();
            Mat L_float = new Mat();
            L.convertTo(L_float, CvType.CV_32F);

            Mat L_norm = new Mat();
            Core.divide(L_float, new Scalar(255.0), L_norm);

            double highlightThreshold = 0.7;
            double shadowThreshold = 0.3;

            if (highlights != 0) {
                Mat highlightWeight = new Mat();
                Core.subtract(L_norm, new Scalar(highlightThreshold), highlightWeight);
                Core.divide(highlightWeight, new Scalar(1.0 - highlightThreshold), highlightWeight);
                Core.max(highlightWeight, new Scalar(0.0), highlightWeight);
                Core.min(highlightWeight, new Scalar(1.0), highlightWeight);

                Mat highlightAdjustment = new Mat();
                Core.multiply(highlightWeight, new Scalar(highlights * 0.75), highlightAdjustment);
                Core.add(L_float, highlightAdjustment, L_float);

                highlightWeight.release();
                highlightAdjustment.release();
            }

            if (shadows != 0) {
                Mat shadowWeight = new Mat();
                Mat scalarShadowThreshold = Mat.ones(L_norm.size(), L_norm.type());
                Core.multiply(scalarShadowThreshold, new Scalar(shadowThreshold), scalarShadowThreshold);
                Core.subtract(scalarShadowThreshold, L_norm, shadowWeight);
                Core.divide(shadowWeight, new Scalar(shadowThreshold), shadowWeight);
                Core.max(shadowWeight, new Scalar(0.0), shadowWeight);
                Core.min(shadowWeight, new Scalar(1.0), shadowWeight);

                Mat shadowAdjustment = new Mat();
                Core.multiply(shadowWeight, new Scalar(shadows * 0.75), shadowAdjustment);
                Core.subtract(L_float, shadowAdjustment, L_float);

                shadowWeight.release();
                shadowAdjustment.release();
                scalarShadowThreshold.release();
            }

            Core.min(L_float, new Scalar(255.0), L_float);
            Core.max(L_float, new Scalar(0.0), L_float);

            Mat adjustedL = new Mat();
            L_float.convertTo(adjustedL, CvType.CV_8U);

            labChannels.set(0, adjustedL);
            Core.merge(labChannels, labMat);

            Imgproc.cvtColor(labMat, colorMat, Imgproc.COLOR_Lab2BGR);

            labMat.release();
            for (Mat channel : labChannels) channel.release();
            L.release();
            L_float.release();
            L_norm.release();
            adjustedL.release();
        }

        // *** Apply temperature (color balance) ***//
        if (temperature!=0){
            Core.add(colorMat, new Scalar(temperature, 0, -temperature), colorMat);
        }

        // *** Apply grain ***//
        if (grain != 0) {
            Mat grainMat = new Mat();
            colorMat.convertTo(grainMat, CvType.CV_32FC3, 1.0 / 255.0);

            Mat noiseMat = new Mat(colorMat.size(), CvType.CV_32FC1);
            Core.randn(noiseMat, 0, grain * 0.1);

            List<Mat> channelsGrain = new ArrayList<>();
            Core.split(grainMat, channelsGrain);
             for (Mat channel : channelsGrain) {
                 Core.add(channel, noiseMat, channel);
             }

             Core.merge(channelsGrain, grainMat);

             Core.min(grainMat, new Scalar(1.0, 1.0, 1.0), grainMat);
             Core.max(grainMat, new Scalar(0.0, 0.0, 0.0), grainMat);

             grainMat.convertTo(colorMat, CvType.CV_8UC3, 255.0);

             // Clean up
            noiseMat.release();
            for (Mat channel : channelsGrain) {
                channel.release();
            }
            grainMat.release();
        }

        // *** Apply fade ***//
        if (fade!=0){
            float fadeContrast = 1 - fade * 0.5f;
            float fadeBrightness = fade * 64;
            colorMat.convertTo(colorMat, -1, fadeContrast, fadeBrightness);
        }

        // *** Apply Vibrance ***//
        if (vibrance != 0) {
            Mat hsvVibrance = new Mat();
            Imgproc.cvtColor(colorMat, hsvVibrance, Imgproc.COLOR_BGR2HSV);

            // Split HSV channels
            List<Mat> hsvChannels = new ArrayList<>();
            Core.split(hsvVibrance, hsvChannels);
            Mat hue = hsvChannels.get(0);
            Mat saturationVibrance = hsvChannels.get(1).clone();
            Mat value = hsvChannels.get(2);

            // Convert saturation to float for precision
            Mat saturationFloat = new Mat();
            saturationVibrance.convertTo(saturationFloat, CvType.CV_32F);

            // Normalize saturation to range [0,1]
            Core.divide(saturationFloat, new Scalar(255.0), saturationFloat);

            // Compute scaling factors: scale = 1 + vibrance * (1 - saturation)
            Mat ones = new Mat(saturationFloat.size(), saturationFloat.type(), new Scalar(1.0));
            Mat scaleMat = new Mat();
            Core.subtract(ones, saturationFloat, scaleMat); // scaleMat = 1 - saturationFloat
            Core.multiply(scaleMat, new Scalar(vibrance), scaleMat); // scaleMat = vibrance * (1 - saturationFloat)
            Core.add(scaleMat, new Scalar(1.0), scaleMat); // scaleMat = 1 + vibrance * (1 - saturationFloat)
            ones.release();

            // Apply scaling factors to saturation
            Core.multiply(saturationFloat, scaleMat, saturationFloat);

            // Clamp saturation to [0,1]
            Core.min(saturationFloat, new Scalar(1.0), saturationFloat);
            Core.max(saturationFloat, new Scalar(0.0), saturationFloat);

            // Convert back to 8-bit
            Core.multiply(saturationFloat, new Scalar(255.0), saturationVibrance);
            saturationVibrance.convertTo(saturationVibrance, CvType.CV_8U);

            // Merge back HSV channels
            hsvChannels.set(1, saturationVibrance);
            Core.merge(hsvChannels, hsvVibrance);

            // Convert back to BGR
            Imgproc.cvtColor(hsvVibrance, colorMat, Imgproc.COLOR_HSV2BGR);

            // Release temporary mats
            for (Mat m : hsvChannels) {
                m.release();
            }
            saturationFloat.release();
            scaleMat.release();
            hsvVibrance.release();
        }

        // *** Apply Hue adjustment ***//
        if (hue != 0) {
            Mat hsvMat = new Mat();
            Imgproc.cvtColor(colorMat, hsvMat, Imgproc.COLOR_BGR2HSV);

            List<Mat> hsvChannels = new ArrayList<>();
            Core.split(hsvMat, hsvChannels);

            Mat hueChannel = hsvChannels.get(0);

            // Convert hue shift from [-180, 180] to OpenCV's 0-180 range
            double hueShift = hue / 2.0;  // Since OpenCV hue channel is in [0, 180] (not 360)

            // Create a look-up table for the hue shift
            byte[] lut = new byte[256];
            for (int i = 0; i < 256; i++) {
                double shiftedHue = i + hueShift;
                if (shiftedHue > 180) {
                    shiftedHue -= 180;
                } else if (shiftedHue < 0) {
                    shiftedHue += 180;
                }
                lut[i] = (byte) Math.round(shiftedHue);
            }
            Mat lookUpTable = new Mat(1, 256, CvType.CV_8U);
            lookUpTable.put(0, 0, lut);

            // Apply the look-up table to the hue channel
            Mat shiftedHueChannel = new Mat();
            Core.LUT(hueChannel, lookUpTable, shiftedHueChannel);

            // Replace the hue channel
            shiftedHueChannel.copyTo(hueChannel);

            // Merge channels and convert back to BGR
            Core.merge(hsvChannels, hsvMat);
            Imgproc.cvtColor(hsvMat, colorMat, Imgproc.COLOR_HSV2BGR);

            // Release temporary mats
            hsvMat.release();
            lookUpTable.release();
            shiftedHueChannel.release();
            for (Mat channel : hsvChannels) {
                channel.release();
            }
        }

        //*** Apply Tint ****//
        if (tint != 0) {
            // Scale the tint value down for a subtler effect
            float tintStrength = 0.5f; // Adjust this value as needed (0 to 1)
            float adjustedTint = tint * tintStrength / 100.0f; // Scale to -1 to 1 range

            // Create a temporary mat to hold the tinted values
            Mat tintedMat = colorMat.clone();


            // Apply tint adjustment based on the sign of adjustedTint
            if (adjustedTint > 0) {
                // Increase magenta (R and B)
                Core.add(tintedMat, new Scalar(adjustedTint * 255, 0, adjustedTint * 255), tintedMat);
            } else {
                // Increase green (G)
                Core.add(tintedMat, new Scalar(0, -adjustedTint * 255, 0), tintedMat);
            }

            // Clamp the values to ensure they remain within valid range [0, 255]
            Core.min(tintedMat, new Scalar(255, 255, 255), tintedMat);
            Core.max(tintedMat, new Scalar(0, 0, 0), tintedMat);

            // Blend the original colorMat and the tintedMat
            float blendFactor = 0.5f; // Adjust this factor for harmony (0 to 1)
            Core.addWeighted(colorMat, 1 - blendFactor, tintedMat, blendFactor, 0, colorMat);

            // Release the temporary mat
            tintedMat.release();
        }

        // *** Apply exposure ***//
        if (exposure!=0){
            colorMat.convertTo(colorMat, -1, Math.pow(2, exposure), 0);
        }

        // Merge alpha channel back if it exists
        if (hasAlpha) {
            List<Mat> finalChannels = new ArrayList<>();
            Core.split(colorMat, finalChannels);
            finalChannels.add(alphaMat); // Add the original alpha channel
            Core.merge(finalChannels, mat);
            // Release temporary mats
            for (Mat m : finalChannels) m.release();
            alphaMat.release();
        } else {
            colorMat.copyTo(mat);        }

        // Release temporary mats
        colorMat.release();

        invalidate();
    }

    // Updated setter methods with appropriate ranges
    public void setBrightness(float brightness) {
        this.brightness = Math.max(-100, Math.min(100, brightness));
        filter[0] = this.brightness;
        debounceAdjustment();
        ScaledAdjustImage();
    }

    public void setContrast(float contrast) {
        this.contrast = Math.max(-1f, Math.min(1f, contrast));
        filter[1] = this.contrast;
        debounceAdjustment();
        ScaledAdjustImage();
    }

    public void setSaturation(float saturation) {
        this.saturation = Math.max(-1.0f, Math.min(1.0f, saturation));
        filter[2] = this.saturation;
        debounceAdjustment();
        ScaledAdjustImage();
    }

    public void setSharpness(float sharpness) {
        this.Sharpness = Math.max(0, Math.min(5, sharpness));
        filter[3] = this.Sharpness;
        debounceAdjustment();
        ScaledAdjustImage();
    }

    public void setHighlights(float highlights) {
        this.highlights = Math.max(-100, Math.min(100, highlights));
        filter[4] = this.highlights;
        debounceAdjustment();
        ScaledAdjustImage();
    }

    public void setShadows(float shadows) {
        this.shadows = Math.max(-100, Math.min(100, shadows));
        filter[5] = this.shadows;
        debounceAdjustment();
        ScaledAdjustImage();
    }

    public void setTemperature(float temperature) {
        this.temperature = Math.max(-100, Math.min(100, temperature));
        filter[6] = this.temperature;
        debounceAdjustment();
        ScaledAdjustImage();
    }

    public void setGrain(float grain) {
        this.grain = Math.max(0, Math.min(1.5f, grain));
        filter[7] = this.grain;
        debounceAdjustment();
        ScaledAdjustImage();
    }

    public void setFade(float fade) {
        this.fade = Math.max(0, Math.min(1, fade));
        filter[8] = this.fade;
        debounceAdjustment();
        ScaledAdjustImage();
    }

    public void setVibrance(float vibrance) {
        this.vibrance = Math.max(-1, Math.min(1, vibrance));
        filter[9] = this.vibrance;
        debounceAdjustment();
        ScaledAdjustImage();
    }

    public void setHue(float hue) {
        this.hue = Math.max(-180, Math.min(180, hue))*-1;
        filter[12] = this.hue;
        debounceAdjustment();
        ScaledAdjustImage();
    }

    public void setExposure(float exposure) {
        this.exposure = Math.max(-1, Math.min(1, exposure));
        filter[10] = this.exposure;
        debounceAdjustment();
        ScaledAdjustImage();
    }

    public void setVignette(float vignette) {
        this.vignette = Math.max(-1, Math.min(1, vignette));
        filter[11] = this.vignette;
        debounceAdjustment();
        ScaledAdjustImage();
    }


    public void setTint(float tint) {
        this.tint= Math.max(-100, Math.min(100, tint));
        filter[13] = this.tint;
        debounceAdjustment();
        ScaledAdjustImage();
    }

    public float[] getFilter() {
        return filter;
    }

    public void setEraseMode(boolean mode) {
        EraseMode = mode;
        invalidate();
    }

    public void restartErase() {
        AdjustedBmEraseBm = Bitmap.createBitmap(originalBitmap.getWidth(),originalBitmap.getHeight(),originalBitmap.getConfig());
        ScaledAdjustedBmEraseBm = Bitmap.createBitmap(ScaledadjustedBitmap.getWidth(),ScaledadjustedBitmap.getHeight(),ScaledadjustedBitmap.getConfig());
        AdjustedBmCanvas = new Canvas(AdjustedBmEraseBm);
        ScaledAdjustedBmCanvas = new Canvas(ScaledAdjustedBmEraseBm);
        invalidate();
    }

    public void setEraseType(boolean restore) {
        this.Restore = restore;
        invalidate();
    }
    public void setEraseOpacity(int opacity) {
        this.opacity = opacity;
        invalidate();
    }

    public void setEraseSize(int size) {
        PaintSTROKEWIDTH = size;
        invalidate();
    }

    public void setEraseHardness(float hardness) {
        AdjustmentHardness = hardness;
        invalidate();
    }

    public Bitmap getAdjustedImage() {
        Bitmap bitmap = Bitmap.createBitmap(originalBitmap.getWidth(),originalBitmap.getHeight(),originalBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(adjustedBitmap,0,0,paint);
        canvas.drawBitmap(AdjustedBmEraseBm,0,0,paint);
        return bitmap;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        executor.shutdown();
    }

}

/*
 public void adjustImageButWithAlphaTurningBlack () {
        if (originalMat == null || adjustedMat == null) return;

        originalMat.copyTo(adjustedMat);

        // Apply brightness//
        Core.add(adjustedMat, new Scalar(brightness, brightness, brightness), adjustedMat);

        // Apply contrast
        // Apply contrast with support for negative values
        if (contrast!=0){
            double alpha = Math.exp(contrast*0.5f); //scaling the contrast by multiplying it with 0.5 - it can be changed to control the force of the contrast-
            double beta = 128 * (1 - alpha);
            adjustedMat.convertTo(adjustedMat, -1, alpha, beta);
        }


        // Apply saturation
        Mat hsvMat = new Mat();
        Imgproc.cvtColor(adjustedMat, hsvMat, Imgproc.COLOR_BGR2HSV);
        // Map saturation from [-1, 1] to [0, 2]
        float adjustedSaturation = saturation + 1.0f;
        Core.multiply(hsvMat, new Scalar(1, adjustedSaturation, 1), hsvMat);
        Imgproc.cvtColor(hsvMat, adjustedMat, Imgproc.COLOR_HSV2BGR);


        // Apply clarity (local contrast enhancement//
        // Create a blurred version of the original image
        Mat blurredMat = new Mat();
        Imgproc.GaussianBlur(adjustedMat, blurredMat, new Size(0, 0), 3); // You can adjust the kernel size for more/less blur
        // Calculate the sharpened image by subtracting the blurred image from the original
        Mat sharpenedMat = new Mat();
        Core.addWeighted(adjustedMat, 1 + Sharpness, blurredMat, -Sharpness, 0, sharpenedMat);
        // Combine the sharpened image with the original to retain colors
        Core.addWeighted(adjustedMat, 1 - Sharpness * 0.2, sharpenedMat, Sharpness * 0.2, 0, adjustedMat);


        // Apply highlights and shadows/
        Mat highMask = new Mat();
        Mat lowMask = new Mat();
        Core.inRange(adjustedMat, new Scalar(200, 200, 200), new Scalar(255, 255, 255), highMask);
        Core.inRange(adjustedMat, new Scalar(0, 0, 0), new Scalar(50, 50, 50), lowMask);
        Core.add(adjustedMat, new Scalar(highlights, highlights, highlights), adjustedMat, highMask);
        Core.add(adjustedMat, new Scalar(shadows, shadows, shadows), adjustedMat, lowMask);

        // Apply temperature (color balance)
        Core.add(adjustedMat, new Scalar(temperature, 0, -temperature), adjustedMat);

    //Apply grain
        if (grain > 0) {
        Mat grainMat = new Mat();
        adjustedMat.convertTo(grainMat, CvType.CV_32FC3, 1.0 / 255.0);

        Mat noiseMat = new Mat(adjustedMat.size(), CvType.CV_32FC1);
        Core.randn(noiseMat, 0, grain * 0.1);  // Adjust the 0.1 factor to control grain intensity

        // Split the image into color channels
        List<Mat> channels = new ArrayList<>();
        Core.split(grainMat, channels);

        // Apply noise to each channel
        for (Mat channel : channels) {
            Core.add(channel, noiseMat, channel);
        }

        // Merge the channels back
        Core.merge(channels, grainMat);

        // Ensure values are in the valid range
        Core.min(grainMat, new Scalar(1.0, 1.0, 1.0), grainMat);
        Core.max(grainMat, new Scalar(0.0, 0.0, 0.0), grainMat);

        grainMat.convertTo(adjustedMat, CvType.CV_8UC3, 255.0);

        // Clean up
        noiseMat.release();
        for (Mat channel : channels) {
            channel.release();
        }
    }


    // Apply fade
    float fadeContrast = 1 - fade * 0.5f;
    float fadeBrightness = fade * 64;
        adjustedMat.convertTo(adjustedMat, -1, fadeContrast, fadeBrightness);

    // Apply vibrance
    Mat hsvVibrance = new Mat();
        Imgproc.cvtColor(adjustedMat, hsvVibrance, Imgproc.COLOR_BGR2HSV);
        Core.multiply(hsvVibrance, new Scalar(1, 1 + vibrance, 1), hsvVibrance);
        Imgproc.cvtColor(hsvVibrance, adjustedMat, Imgproc.COLOR_HSV2BGR);

    // Apply exposure
        adjustedMat.convertTo(adjustedMat, -1, Math.pow(2, exposure), 0);

        Utils.matToBitmap(adjustedMat, adjustedBitmap);
    invalidate();



}
 */

 /*

        // Apply highlights and shadows

        // Convert to LAB color space
        Mat labMat = new Mat();
        Imgproc.cvtColor(colorMat, labMat, Imgproc.COLOR_BGR2Lab);

        // Split the LAB channels
        List<Mat> labChannels = new ArrayList<>();
        Core.split(labMat, labChannels);
        Mat L = labChannels.get(0);

        // Create masks for highlights and shadows
        Mat highlightMask = new Mat();
        Mat shadowMask = new Mat();
        Imgproc.threshold(L, highlightMask, 180, 255, Imgproc.THRESH_BINARY);
        Imgproc.threshold(L, shadowMask, 70, 255, Imgproc.THRESH_BINARY_INV);

        // Apply highlights adjustment
        Mat adjustedL = L.clone();
        adjustedL.convertTo(adjustedL, CvType.CV_32F);
        Core.add(adjustedL, new Scalar(highlights * 0.75), adjustedL, highlightMask);

        // Apply shadows adjustment
        Core.add(adjustedL, new Scalar(shadows * 0.75), adjustedL, shadowMask);

        // Ensure values are within 0-255 range
        Core.min(adjustedL, new Scalar(255), adjustedL);
        Core.max(adjustedL, new Scalar(0), adjustedL);

        adjustedL.convertTo(adjustedL, CvType.CV_8U);

        // Merge the adjusted L channel back
        labChannels.set(0, adjustedL);
        Core.merge(labChannels, labMat);

        // Convert back to BGR
        Imgproc.cvtColor(labMat, colorMat, Imgproc.COLOR_Lab2BGR);

        // Release temporary Mats
        labMat.release();
        for (Mat channel : labChannels) channel.release();
        highlightMask.release();
        shadowMask.release();
        adjustedL.release();




        //other one
        // **** Apply highlights and shadows uniformly with optimizations *** //
// Apply highlights and shadows uniformly across the entire image

// Convert to LAB color space
Mat labMat = new Mat();
Imgproc.cvtColor(colorMat, labMat, Imgproc.COLOR_BGR2Lab);

// Split the LAB channels
List<Mat> labChannels = new ArrayList<>();
Core.split(labMat, labChannels);
Mat L = labChannels.get(0);  // Direct reference, no clone needed

// Convert L channel to float for precision
L.convertTo(L, CvType.CV_32F);  // In-place conversion

// Define thresholds for highlights and shadows
double highlightThreshold = 180.0; // Using 0-255 range directly (instead of 0.7 normalized)
double shadowThreshold = 70.0;

// Create highlight and shadow weights
Mat highlightWeight = new Mat();
Mat shadowWeight = new Mat();

// Calculate highlight weight: increases with lightness
Core.subtract(L, new Scalar(highlightThreshold), highlightWeight);
Core.max(highlightWeight, new Scalar(0.0), highlightWeight);  // Clamp to [0, 75]
Core.divide(highlightWeight, new Scalar(255.0 - highlightThreshold), highlightWeight); // Normalize to [0,1]

// Calculate shadow weight: increases as lightness decreases
Mat scalarShadowThreshold = Mat.ones(L.size(), L.type());
Core.multiply(scalarShadowThreshold, new Scalar(shadowThreshold), scalarShadowThreshold);
Core.subtract(scalarShadowThreshold, L, shadowWeight);
Core.max(shadowWeight, new Scalar(0.0), shadowWeight);  // Clamp to [0, 70]
Core.divide(shadowWeight, new Scalar(shadowThreshold), shadowWeight);  // Normalize to [0,1]

// Calculate and apply adjustments directly to L channel
Mat highlightAdjustment = new Mat();
Mat shadowAdjustment = new Mat();

// Apply highlights adjustment
Core.multiply(highlightWeight, new Scalar(highlights * 0.75), highlightAdjustment);
Core.add(L, highlightAdjustment, L);  // In-place addition

// Apply shadows adjustment
Core.multiply(shadowWeight, new Scalar(shadows * 0.75), shadowAdjustment);
Core.subtract(L, shadowAdjustment, L);  // In-place subtraction

// Ensure values are within 0-255 range
Core.min(L, new Scalar(255.0), L);
Core.max(L, new Scalar(0.0), L);

// Convert L channel back to 8-bit
L.convertTo(L, CvType.CV_8U);  // In-place conversion

// Merge the adjusted L channel back into LAB image
labChannels.set(0, L);
Core.merge(labChannels, labMat);

// Convert back to BGR color space
Imgproc.cvtColor(labMat, colorMat, Imgproc.COLOR_Lab2BGR);

// Release temporary Mats to free memory (only necessary ones)
labMat.release();
highlightWeight.release();
shadowWeight.release();
highlightAdjustment.release();
shadowAdjustment.release();
scalarShadowThreshold.release();  // Release the shadow threshold mat




// *** Apply Hue adjustment ***
if (hue != 0) {
    Mat hsvMat = new Mat();
    Imgproc.cvtColor(colorMat, hsvMat, Imgproc.COLOR_BGR2HSV);

    List<Mat> hsvChannels = new ArrayList<>();
    Core.split(hsvMat, hsvChannels);

    Mat hueChannel = hsvChannels.get(0);

    // Convert hue shift from [-180, 180] to OpenCV's 0-180 range
    double hueShift = hue / 2.0;  // Since OpenCV hue channel is in [0, 180] (not 360)

    // Apply the hue shift and wrap around
    for (int row = 0; row < hueChannel.rows(); row++) {
        for (int col = 0; col < hueChannel.cols(); col++) {
            double[] hueVal = hueChannel.get(row, col);

            // Apply hue shift
            hueVal[0] += hueShift;

            // Wrap around: If the value exceeds 180 or goes below 0, we wrap it
            if (hueVal[0] > 180) {
                hueVal[0] -= 180;
            } else if (hueVal[0] < 0) {
                hueVal[0] += 180;
            }

            hueChannel.put(row, col, hueVal);
        }
    }

    // Merge channels and convert back to BGR
    Core.merge(hsvChannels, hsvMat);
    Imgproc.cvtColor(hsvMat, colorMat, Imgproc.COLOR_HSV2BGR);

    // Release temporary mats
    hsvMat.release();
    for (Mat channel : hsvChannels) {
        channel.release();
    }
}

 // *** Apply vignette effect
        if (vignette != 0) {
Mat vignetteMap = new Mat(colorMat.size(), CvType.CV_32FC1);
Point center = new Point(colorMat.width() / 2.0, colorMat.height() / 2.0);
double maxDist = Math.sqrt(center.x * center.x + center.y * center.y);

            for (int y = 0; y < colorMat.height(); y++) {
        for (int x = 0; x < colorMat.width(); x++) {
double dist = Math.sqrt(Math.pow(x - center.x, 2) + Math.pow(y - center.y, 2));
double vignetteStrength = 1.0 - (dist / maxDist) * Math.abs(vignette);
                    vignetteMap.put(y, x, vignetteStrength);
                }
                        }

List<Mat> colorChannels = new ArrayList<>();
            Core.split(colorMat, colorChannels);

            for (Mat channel : colorChannels) {
        channel.convertTo(channel, CvType.CV_32F);
                Core.multiply(channel, vignetteMap, channel);
                channel.convertTo(channel, CvType.CV_8U);
            }

                    Core.merge(colorChannels, colorMat);

// Release temporary mats
            vignetteMap.release();
            for (Mat channel : colorChannels) {
        channel.release();
            }
                    }


                     private void applyVignette(Canvas canvas, float vignetteIntensity) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        float radius = (float) (Math.sqrt(width * width + height * height) / 2f);
        RadialGradient vignette = new RadialGradient(
                width / 2f, height / 2f, radius,
                Color.TRANSPARENT,
                Color.BLACK,
                Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setShader(vignette);
        paint.setAntiAlias(true);
        paint.setDither(true);

        // Create a new canvas with a transparent bitmap to draw the vignette
        Bitmap vignetteBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas vignetteCanvas = new Canvas(vignetteBuffer);

        // Draw the radial gradient on the vignette canvas
        vignetteCanvas.drawRect(0, 0, width, height, paint);

        // Set the blend mode and opacity for the vignette effect
        paint.setShader(null);
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        paint.setAlpha((int) (255 * Math.abs(vignetteIntensity)));

        // Draw the vignette onto the main canvas
        canvas.drawBitmap(vignetteBuffer, 0, 0, paint);

        // Clean up
        vignetteBuffer.recycle();
    }
         */
