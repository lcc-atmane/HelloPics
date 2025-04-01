package com.phantomhive.exil.hellopics.Img_Editor.Views.AdjustImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AI extends View {

    private ViewZommer zommer;
    private Bitmap originalBitmap;
    private Bitmap adjustedBitmap;
    private Bitmap previewBitmap;
    private Mat originalMat;
    private Mat adjustedMat;
    private Mat previewMat;

    // Adjustment parameters with their ranges
    private float brightness = 0;    // Range: -100 to 100
    private float contrast = 0;      // Range: -1 to 1
    private float saturation = 0;    // Range: -1 to 1
    private float sharpness = 0;     // Range: 0 to 5
    private float highlights = 0;    // Range: -100 to 100
    private float shadows = 0;       // Range: -100 to 100
    private float temperature = 0;   // Range: -100 to 100
    private float grain = 0;         // Range: 0 to 1.5
    private float fade = 0;          // Range: 0 to 1
    private float vibrance = 0;      // Range: -1 to 1
    private float exposure = 0;      // Range: -2 to 2
    private float vignette = 0;
    private float hue = 0;          //range: -180 to 180
    private ExecutorService executor;
    private Handler mainHandler;
    private Runnable adjustmentRunnable;
    private static final long DEBOUNCE_DELAY = 200; // milliseconds

    public AI(Context context, AttributeSet attrs) {
        super(context, attrs);
        zommer = new ViewZommer(this, context);
        System.loadLibrary("opencv_java4");
        executor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        zommer.onMeasure(widthMeasureSpec, heightMeasureSpec);
        invalidate();
    }

    public void setImage(Bitmap bitmap) {
        zommer.setBitmap(bitmap);

        originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        adjustedBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);

        // Create a smaller preview bitmap
        int previewWidth = bitmap.getWidth() / 4;
        int previewHeight = bitmap.getHeight() / 4;
        previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, true);

        originalMat = new Mat();
        adjustedMat = new Mat();
        previewMat = new Mat();

        Utils.bitmapToMat(originalBitmap, originalMat);
        Utils.bitmapToMat(adjustedBitmap, adjustedMat);
        Utils.bitmapToMat(previewBitmap, previewMat);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (adjustedBitmap != null) {
            canvas.drawBitmap(adjustedBitmap, zommer.getDefaultMatrix(), null);
        }
    }

    private void debounceAdjustment() {
        mainHandler.removeCallbacks(adjustmentRunnable);
        adjustmentRunnable = () -> executor.execute(this::adjustImage);
        mainHandler.postDelayed(adjustmentRunnable, DEBOUNCE_DELAY);
    }

    private void adjustImage() {
        if (originalMat == null || previewMat == null) return;

        Mat workingMat = previewMat.clone();

        // Apply adjustments to workingMat
        applyAdjustments(workingMat);

        // Update preview bitmap
        Utils.matToBitmap(workingMat, previewBitmap);

        // Update full resolution bitmap on UI thread
        mainHandler.post(() -> {
            adjustedMat = originalMat.clone();
            applyAdjustments(adjustedMat);
            Utils.matToBitmap(adjustedMat, adjustedBitmap);
            invalidate();
        });

        workingMat.release();
    }

    private void applyAdjustments(Mat mat) {
        // Check if the image has an alpha channel
        int channels = mat.channels();
        boolean hasAlpha = channels == 4;

        Mat colorMat = new Mat();
        Mat alphaMat = new Mat();

        if (hasAlpha) {
            List<Mat> channelList = new ArrayList<>();
            Core.split(mat, channelList);
            Core.merge(channelList.subList(0, 3), colorMat);
            alphaMat = channelList.get(3).clone();
            for (Mat m : channelList) m.release();
        } else {
            colorMat = mat.clone();
        }

        // Apply brightness
        Core.add(colorMat, new Scalar(brightness, brightness, brightness), colorMat);

        // Apply contrast
        if (contrast != 0) {
            double alphaContrast = Math.exp(contrast * 0.5f);
            double betaContrast = 128 * (1 - alphaContrast);
            colorMat.convertTo(colorMat, -1, alphaContrast, betaContrast);
        }

        // Apply saturation
        if (saturation != 0) {
            Mat hsvMat = new Mat();
            Imgproc.cvtColor(colorMat, hsvMat, Imgproc.COLOR_BGR2HSV);
            float adjustedSaturation = saturation + 1.0f;
            Core.multiply(hsvMat, new Scalar(1, adjustedSaturation, 1), hsvMat);
            Imgproc.cvtColor(hsvMat, colorMat, Imgproc.COLOR_HSV2BGR);
            hsvMat.release();
        }

        // Apply sharpness
        if (sharpness > 0) {
            Mat blurredMat = new Mat();
            Imgproc.GaussianBlur(colorMat, blurredMat, new Size(0, 0), 3);
            Mat sharpenedMat = new Mat();
            Core.addWeighted(colorMat, 1 + sharpness, blurredMat, -sharpness, 0, sharpenedMat);
            Core.addWeighted(colorMat, 1 - sharpness * 0.2, sharpenedMat, sharpness * 0.2, 0, colorMat);
            blurredMat.release();
            sharpenedMat.release();
        }

        // Apply highlights and shadows
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

        // Apply temperature
        Core.add(colorMat, new Scalar(temperature, 0, -temperature), colorMat);

        // Apply grain
        if (grain > 0) {
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

            noiseMat.release();
            for (Mat channel : channelsGrain) {
                channel.release();
            }
            grainMat.release();
        }

        // Apply fade
        if (fade > 0) {
            float fadeContrast = 1 - fade * 0.5f;
            float fadeBrightness = fade * 64;
            colorMat.convertTo(colorMat, -1, fadeContrast, fadeBrightness);
        }

        // Apply vibrance
        if (vibrance != 0) {
            Mat hsvVibrance = new Mat();
            Imgproc.cvtColor(colorMat, hsvVibrance, Imgproc.COLOR_BGR2HSV);

            List<Mat> hsvChannels = new ArrayList<>();
            Core.split(hsvVibrance, hsvChannels);
            Mat saturationVibrance = hsvChannels.get(1).clone();

            Mat saturationFloat = new Mat();
            saturationVibrance.convertTo(saturationFloat, CvType.CV_32F);

            Core.divide(saturationFloat, new Scalar(255.0), saturationFloat);

            Mat ones = Mat.ones(saturationFloat.size(), saturationFloat.type());
            Mat scaleMat = new Mat();
            Core.subtract(ones, saturationFloat, scaleMat);
            Core.multiply(scaleMat, new Scalar(vibrance), scaleMat);
            Core.add(scaleMat, new Scalar(1.0), scaleMat);
            ones.release();

            Core.multiply(saturationFloat, scaleMat, saturationFloat);

            Core.min(saturationFloat, new Scalar(1.0), saturationFloat);
            Core.max(saturationFloat, new Scalar(0.0), saturationFloat);

            Core.multiply(saturationFloat, new Scalar(255.0), saturationVibrance);
            saturationVibrance.convertTo(saturationVibrance, CvType.CV_8U);

            hsvChannels.set(1, saturationVibrance);
            Core.merge(hsvChannels, hsvVibrance);

            Imgproc.cvtColor(hsvVibrance, colorMat, Imgproc.COLOR_HSV2BGR);

            for (Mat m : hsvChannels) {
                m.release();
            }
            saturationFloat.release();
            scaleMat.release();
            hsvVibrance.release();
        }

        // Apply exposure
        colorMat.convertTo(colorMat, -1, Math.pow(2, exposure), 0);

        // Merge alpha channel back if it exists
        if (hasAlpha) {
            List<Mat> finalChannels = new ArrayList<>();
            Core.split(colorMat, finalChannels);
            finalChannels.add(alphaMat);
            Core.merge(finalChannels, mat);
            for (Mat m : finalChannels) m.release();
            alphaMat.release();
        } else {
            colorMat.copyTo(mat);
        }

        colorMat.release();
    }

    // Setter methods
    public void setBrightness(float brightness) {
        this.brightness = Math.max(-100, Math.min(100, brightness));
        debounceAdjustment();
    }

    public void setContrast(float contrast) {
        this.contrast = Math.max(-1f, Math.min(1f, contrast));
        debounceAdjustment();
    }

    public void setSaturation(float saturation) {
        this.saturation = Math.max(-1.0f, Math.min(1.0f, saturation));
        debounceAdjustment();
    }

    public void setSharpness(float sharpness) {
        this.sharpness = Math.max(0, Math.min(5, sharpness));
        debounceAdjustment();
    }

    public void setHighlights(float highlights) {
        this.highlights = Math.max(-100, Math.min(100, highlights));
        debounceAdjustment();
    }

    public void setShadows(float shadows) {
        this.shadows = Math.max(-100, Math.min(100, shadows));
        debounceAdjustment();
    }

    public void setTemperature(float temperature) {
        this.temperature = Math.max(-100, Math.min(100, temperature));
        debounceAdjustment();
    }

    public void setGrain(float grain) {
        this.grain = Math.max(0, Math.min(1.5f, grain));
        debounceAdjustment();
    }

    public void setFade(float fade) {
        this.fade = Math.max(0, Math.min(1, fade));
        debounceAdjustment();
    }

    public void setVibrance(float vibrance) {
        this.vibrance = Math.max(-1, Math.min(1, vibrance));
        debounceAdjustment();
    }

    public void setExposure(float exposure) {
        this.exposure = Math.max(-2, Math.min(2, exposure));
        debounceAdjustment();
    }

    public void setVignette(float vignette) {
        this.vignette = Math.max(-1, Math.min(1, vignette));
    }

    public void setHue(float hue) {
        this.hue = Math.max(-1, Math.min(1, hue));
    }
    public Bitmap getAdjustedImage() {
        return adjustedBitmap;
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

         */

