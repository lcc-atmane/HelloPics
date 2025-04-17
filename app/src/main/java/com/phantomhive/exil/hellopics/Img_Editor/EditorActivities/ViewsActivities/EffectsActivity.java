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
package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities;

import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.Views.AdjustImage.SavedAdjustment.Filter;
import com.phantomhive.exil.hellopics.Img_Editor.Views.AdjustImage.SavedAdjustment.FilterSqlite;
import com.phantomhive.exil.hellopics.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;


public class EffectsActivity extends AppCompatActivity {

    RecyclerView EffectsList;
    ImageView EffectImageView;
    EffectsAdapter effectsAdapter;

    private FilterSqlite filterSqlite;
    ArrayList<Effects> effects = new ArrayList<>();
    TextView EffectIsEmptyText;
    Image image;
    ImageButton Done,Cancel;
    ProgressBar progressBar;
    Bitmap EffectedBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effects);
        EdgeToEdgeFixing(R.id.activity_effectsL,this);

        System.loadLibrary("opencv_java4");
        EffectImageView = findViewById(R.id.EffectsimageView);
        EffectsList = findViewById(R.id.EffectsList);
        EffectIsEmptyText = findViewById(R.id.EffectIsEmptyText);
        progressBar = findViewById(R.id.SettingEffectProgressBar);

        Done = findViewById(R.id.DoneEffect);
        Cancel = findViewById(R.id.CancelEffect);
        image = Image.getInstance();


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(EffectsActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });

        if (image.getBitmap() == null) {
            Toast.makeText(EffectsActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        } else {
            EffectImageView.setImageBitmap(image.getBitmap());
        }



        filterSqlite = new FilterSqlite(this);


        // Get all filters
        ArrayList<Filter> filters = filterSqlite.getAllFilters();
        ArrayList<Effects> effects = new ArrayList<>();

        for (Filter filter : filters) {
            effects.add(new Effects(filter.getName(), filter.getFilterValues()));
        }

        //set the editing Tools Arraylist and set adapter.

        if (effects.isEmpty()) {
            EffectIsEmptyText.setVisibility(View.VISIBLE);
        } else {
            EffectIsEmptyText.setVisibility(View.GONE);
            effectsAdapter = new EffectsAdapter(effects, EffectsActivity.this);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(EffectsActivity.this, LinearLayoutManager.HORIZONTAL, false);
            EffectsList.setLayoutManager(horizontalLayoutManager);
            EffectsList.setAdapter(effectsAdapter);
        }

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (EffectedBitmap!= null){
                    image.setBitmap(EffectedBitmap);
                }
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(EffectsActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(EffectsActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });
    }


    private class Effects {
        String effectName;
        float [] effect;

        public Effects(String effectName, float[] effect) {
            this.effectName = effectName;
            this.effect = effect;
        }

        public String getEffectName() {
            return effectName;
        }

        public void setEffectName(String effectName) {
            this.effectName = effectName;
        }

        public float[] getEffect() {
            return effect;
        }

        public void setEffect(float[] effect) {
            this.effect = effect;
        }
    }
    public class EffectsAdapter extends RecyclerView.Adapter<EffectsAdapter.MyViewHolder> {
        ArrayList<Effects> editingtools;
        Context context;

        public EffectsAdapter(ArrayList<Effects> editingtools, Context context) {
            this.editingtools = editingtools;
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View View = LayoutInflater.from(context).inflate(R.layout.effects_item, parent, false);
            return new MyViewHolder(View);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            Mat mat = new Mat();
            Utils.bitmapToMat(image.getBitmap(), mat);
            Glide.with(context)
            .load(applyAdjustments(mat,editingtools.get(position).getEffect()))
                    .into(holder.imageView_Tool);


            holder.textView_Tool.setText(editingtools.get(position).getEffectName());
            holder.imageView_Tool.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE); // Show ProgressBar

                    new Thread(() -> {
                        Mat mat = new Mat();
                        Utils.bitmapToMat(image.getBitmap(), mat);
                        Bitmap effectBitmap = applyAdjustments(mat, editingtools.get(position).getEffect());

                        Mat matR = new Mat();
                        Utils.bitmapToMat(image.getBitmap(), matR);
                        Bitmap effectedBitmap = applyAdjustments(matR, editingtools.get(position).getEffect());

                        runOnUiThread(() -> {
                            EffectImageView.setImageBitmap(effectBitmap);
                            EffectedBitmap = effectedBitmap;
                            progressBar.setVisibility(View.GONE); // Hide ProgressBar
                        });
                    }).start();

                }
            });
        }

        @Override
        public int getItemCount() {
            return editingtools.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView_Tool;
            TextView textView_Tool;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView_Tool = itemView.findViewById(R.id.imageView_Effect);
                textView_Tool = itemView.findViewById(R.id.textView_Effect);

            }
        }
    }

    private void applyVignette(Bitmap bitmap,float [] filter) {
        if (filter[11] == 0) return;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float radius = (float) (Math.sqrt(width * width + height * height) / 2f);

        RadialGradient vignetteMask;
        if (filter[11]>0){
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
        vignetteScrimPaint.setAlpha((int) (255 * Math.abs(filter[11])));

        // Draw the vignette onto the bitmap
        bitmapCanvas.drawRect(0, 0, width, height, vignetteScrimPaint);
    }
    public Bitmap applyAdjustments(Mat mat,float [] filter ) {
        Bitmap bitmap = Bitmap.createBitmap(image.getBitmap(),0,0,image.getBitmap().getWidth(),image.getBitmap().getHeight());
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
        if ( filter[0]!=0){
            Core.add(colorMat, new Scalar( filter[0],  filter[0],  filter[0]), colorMat);
        }

        //*** Apply contrast ***//
        if (filter[1] != 0) {
            double alphaContrast = Math.exp(filter[1] * 0.5f);
            double betaContrast = 128 * (1 - alphaContrast);
            colorMat.convertTo(colorMat, -1, alphaContrast, betaContrast);
        }

        //*** Apply saturation ***/
        if (filter[2]!=0){
            Mat hsvMat = new Mat();
            Imgproc.cvtColor(colorMat, hsvMat, Imgproc.COLOR_BGR2HSV);
            float adjustedSaturation = filter[2] + 1.0f;
            Core.multiply(hsvMat, new Scalar(1, adjustedSaturation, 1), hsvMat);
            Imgproc.cvtColor(hsvMat, colorMat, Imgproc.COLOR_HSV2BGR);
            hsvMat.release();
        }

        // *** Apply Sharpness (local Sharpness enhancement) ***//
        if (filter[3]!=0){
            Mat blurredMat = new Mat();
            Imgproc.GaussianBlur(colorMat, blurredMat, new Size(0, 0), 3);
            Mat sharpenedMat = new Mat();
            Core.addWeighted(colorMat, 1 + filter[3], blurredMat, -filter[3], 0, sharpenedMat);
            Core.addWeighted(colorMat, 1 - filter[3] * 0.2, sharpenedMat, filter[3] * 0.2, 0, colorMat);
            blurredMat.release();
            sharpenedMat.release();
        }

        // *** Apply highlights and shadows separately ***//
        // Convert to LAB color space
        if (filter[4] != 0 || filter[5] != 0) {
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

            if (filter[4] != 0) {
                Mat highlightWeight = new Mat();
                Core.subtract(L_norm, new Scalar(highlightThreshold), highlightWeight);
                Core.divide(highlightWeight, new Scalar(1.0 - highlightThreshold), highlightWeight);
                Core.max(highlightWeight, new Scalar(0.0), highlightWeight);
                Core.min(highlightWeight, new Scalar(1.0), highlightWeight);

                Mat highlightAdjustment = new Mat();
                Core.multiply(highlightWeight, new Scalar(filter[4] * 0.75), highlightAdjustment);
                Core.add(L_float, highlightAdjustment, L_float);

                highlightWeight.release();
                highlightAdjustment.release();
            }

            if (filter[5] != 0) {
                Mat shadowWeight = new Mat();
                Mat scalarShadowThreshold = Mat.ones(L_norm.size(), L_norm.type());
                Core.multiply(scalarShadowThreshold, new Scalar(shadowThreshold), scalarShadowThreshold);
                Core.subtract(scalarShadowThreshold, L_norm, shadowWeight);
                Core.divide(shadowWeight, new Scalar(shadowThreshold), shadowWeight);
                Core.max(shadowWeight, new Scalar(0.0), shadowWeight);
                Core.min(shadowWeight, new Scalar(1.0), shadowWeight);

                Mat shadowAdjustment = new Mat();
                Core.multiply(shadowWeight, new Scalar(filter[5] * 0.75), shadowAdjustment);
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
        if (filter[6]!=0){
            Core.add(colorMat, new Scalar(filter[6], 0, -filter[6]), colorMat);
        }

        // *** Apply grain ***//
        if (filter[7] != 0) {
            Mat grainMat = new Mat();
            colorMat.convertTo(grainMat, CvType.CV_32FC3, 1.0 / 255.0);

            Mat noiseMat = new Mat(colorMat.size(), CvType.CV_32FC1);
            Core.randn(noiseMat, 0, filter[7] * 0.1);

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
        if (filter[8]!=0){
            float fadeContrast = 1 - filter[8] * 0.5f;
            float fadeBrightness = filter[8] * 64;
            colorMat.convertTo(colorMat, -1, fadeContrast, fadeBrightness);
        }

        // *** Apply Vibrance ***//
        if (filter[9] != 0) {
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
            Core.multiply(scaleMat, new Scalar(filter[9]), scaleMat); // scaleMat = vibrance * (1 - saturationFloat)
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
        if (filter[12] != 0) {
            Mat hsvMat = new Mat();
            Imgproc.cvtColor(colorMat, hsvMat, Imgproc.COLOR_BGR2HSV);

            List<Mat> hsvChannels = new ArrayList<>();
            Core.split(hsvMat, hsvChannels);

            Mat hueChannel = hsvChannels.get(0);

            // Convert hue shift from [-180, 180] to OpenCV's 0-180 range
            double hueShift = filter[12] / 2.0;  // Since OpenCV hue channel is in [0, 180] (not 360)

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
        if (filter[13] != 0) {
            // Scale the tint value down for a subtler effect
            float tintStrength = 0.5f; // Adjust this value as needed (0 to 1)
            float adjustedTint = filter[13] * tintStrength / 100.0f; // Scale to -1 to 1 range

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
        if (filter[10]!=0){
            colorMat.convertTo(colorMat, -1, Math.pow(2, filter[10]), 0);
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

        Utils.matToBitmap(mat, bitmap);

        applyVignette(bitmap,filter);
        return bitmap;
    }

    public Bitmap applyAdjustmentsToResult(Mat mat,float [] filter ) {
        Bitmap bitmap = Bitmap.createBitmap(image.getBitmap(),0,0,image.getBitmap().getWidth(),image.getBitmap().getHeight());
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
        if ( filter[0]!=0){
            Core.add(colorMat, new Scalar( filter[0],  filter[0],  filter[0]), colorMat);
        }

        //*** Apply contrast ***//
        if (filter[1] != 0) {
            double alphaContrast = Math.exp(filter[1] * 0.5f);
            double betaContrast = 128 * (1 - alphaContrast);
            colorMat.convertTo(colorMat, -1, alphaContrast, betaContrast);
        }

        //*** Apply saturation ***/
        if (filter[2]!=0){
            Mat hsvMat = new Mat();
            Imgproc.cvtColor(colorMat, hsvMat, Imgproc.COLOR_BGR2HSV);
            float adjustedSaturation = filter[2] + 1.0f;
            Core.multiply(hsvMat, new Scalar(1, adjustedSaturation, 1), hsvMat);
            Imgproc.cvtColor(hsvMat, colorMat, Imgproc.COLOR_HSV2BGR);
            hsvMat.release();
        }

        // *** Apply Sharpness (local Sharpness enhancement) ***//
        if (filter[3]!=0){
            Mat blurredMat = new Mat();
            Imgproc.GaussianBlur(colorMat, blurredMat, new Size(0, 0), 3);
            Mat sharpenedMat = new Mat();
            Core.addWeighted(colorMat, 1 + filter[3], blurredMat, -filter[3], 0, sharpenedMat);
            Core.addWeighted(colorMat, 1 - filter[3] * 0.2, sharpenedMat, filter[3] * 0.2, 0, colorMat);
            blurredMat.release();
            sharpenedMat.release();
        }

        // *** Apply highlights and shadows separately ***//
        // Convert to LAB color space
        if (filter[4] != 0 || filter[5] != 0) {
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

            if (filter[4] != 0) {
                Mat highlightWeight = new Mat();
                Core.subtract(L_norm, new Scalar(highlightThreshold), highlightWeight);
                Core.divide(highlightWeight, new Scalar(1.0 - highlightThreshold), highlightWeight);
                Core.max(highlightWeight, new Scalar(0.0), highlightWeight);
                Core.min(highlightWeight, new Scalar(1.0), highlightWeight);

                Mat highlightAdjustment = new Mat();
                Core.multiply(highlightWeight, new Scalar(filter[4] * 0.75), highlightAdjustment);
                Core.add(L_float, highlightAdjustment, L_float);

                highlightWeight.release();
                highlightAdjustment.release();
            }

            if (filter[5] != 0) {
                Mat shadowWeight = new Mat();
                Mat scalarShadowThreshold = Mat.ones(L_norm.size(), L_norm.type());
                Core.multiply(scalarShadowThreshold, new Scalar(shadowThreshold), scalarShadowThreshold);
                Core.subtract(scalarShadowThreshold, L_norm, shadowWeight);
                Core.divide(shadowWeight, new Scalar(shadowThreshold), shadowWeight);
                Core.max(shadowWeight, new Scalar(0.0), shadowWeight);
                Core.min(shadowWeight, new Scalar(1.0), shadowWeight);

                Mat shadowAdjustment = new Mat();
                Core.multiply(shadowWeight, new Scalar(filter[5] * 0.75), shadowAdjustment);
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
        if (filter[6]!=0){
            Core.add(colorMat, new Scalar(filter[6], 0, -filter[6]), colorMat);
        }

        // *** Apply grain ***//
        if (filter[7] != 0) {
            Mat grainMat = new Mat();
            colorMat.convertTo(grainMat, CvType.CV_32FC3, 1.0 / 255.0);

            Mat noiseMat = new Mat(colorMat.size(), CvType.CV_32FC1);
            Core.randn(noiseMat, 0, filter[7] * 0.1);

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
        if (filter[8]!=0){
            float fadeContrast = 1 - filter[8] * 0.5f;
            float fadeBrightness = filter[8] * 64;
            colorMat.convertTo(colorMat, -1, fadeContrast, fadeBrightness);
        }

        // *** Apply Vibrance ***//
        if (filter[9] != 0) {
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
            Core.multiply(scaleMat, new Scalar(filter[9]), scaleMat); // scaleMat = vibrance * (1 - saturationFloat)
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
        if (filter[12] != 0) {
            Mat hsvMat = new Mat();
            Imgproc.cvtColor(colorMat, hsvMat, Imgproc.COLOR_BGR2HSV);

            List<Mat> hsvChannels = new ArrayList<>();
            Core.split(hsvMat, hsvChannels);

            Mat hueChannel = hsvChannels.get(0);

            // Convert hue shift from [-180, 180] to OpenCV's 0-180 range
            double hueShift = filter[12] / 2.0;  // Since OpenCV hue channel is in [0, 180] (not 360)

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
        if (filter[13] != 0) {
            // Scale the tint value down for a subtler effect
            float tintStrength = 0.5f; // Adjust this value as needed (0 to 1)
            float adjustedTint = filter[13] * tintStrength / 100.0f; // Scale to -1 to 1 range

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
        if (filter[10]!=0){
            colorMat.convertTo(colorMat, -1, Math.pow(2, filter[10]), 0);
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

        Utils.matToBitmap(mat, bitmap);

        applyVignette(bitmap,filter);
        return bitmap;
    }

}