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

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.Editor_Adapters.AdjustToolsAdapter;
import com.phantomhive.exil.hellopics.Img_Editor.Interface.itemOnClick;
import com.phantomhive.exil.hellopics.Img_Editor.Views.AdjustImage.ImageAdjustmentView;
import com.phantomhive.exil.hellopics.Img_Editor.Views.AdjustImage.SavedAdjustment.FilterSqlite;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.SeekbarValueSettings;
import com.phantomhive.exil.hellopics.R;

import java.util.ArrayList;



public class AdjustImageActivity extends AppCompatActivity implements itemOnClick {
    ImageAdjustmentView AdjustImageView;
    Image image;
    RecyclerView recyclerView;
    AdjustToolsAdapter adjustToolsAdapter;
    ViewFlipper AdjustToolsFlipper;
    SeekBar Brightness,Contrast,Saturation, Sharpness,Highlights,Shadows,Temp,Grain,Fade,Exposure,Vibrance,Vignette,Hue,Tint;
    TextView textViewBrightness,textViewContrast,textViewSaturation, textViewSharpness,textViewHighlights,textViewShadows,
            textViewTemp,textViewGrain,textViewFade,textViewExposure,textViewVibrance,textViewVignette,textViewHue,textViewTint;

    Bitmap AdjustedBitmap;
    ImageButton DoneAdjust,CancelAdjust;
    ImageButton EraseAdjust;
    ImageButton SaveAsFilter;
    private boolean EraseMode = false;

    ConstraintLayout DoneErasingLayout,DoneAdjustingLayout,AdjustTools,EraseTools;
    ImageButton DoneErasing,CancelErasing;
    ImageButton EraseAdjustButton,RestoreAdjustButton;

    SeekBar AdjustOpacity, AdjustSize, AdjustHardness;
    TextView AdjustOpacityValue, AdjustSizeValue, AdjustHardnessValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_image);
        EdgeToEdgeFixing(R.id.activity_adjust_imageL,this);

        image = Image.getInstance();

        recyclerView = findViewById(R.id.AdjustToolsRecyclerView);
        AdjustImageView = findViewById(R.id.AdjustImageView);
        AdjustToolsFlipper = findViewById(R.id.AdjustToolFlipper);

        DoneAdjust = findViewById(R.id.DoneAdjusting);
        CancelAdjust = findViewById(R.id.CancelAdjusting);

        EraseAdjust = findViewById(R.id.EraseAdjust);
        SaveAsFilter = findViewById(R.id.saveasafilter);

        //SeekBars.
        Brightness = findViewById(R.id.Brightness);
        Contrast = findViewById(R.id.Contrast);
        Saturation = findViewById(R.id.Saturation);
        Sharpness = findViewById(R.id.Sharpness);
        Highlights = findViewById(R.id.Highlights);
        Shadows = findViewById(R.id.Shadows);
        Temp = findViewById(R.id.Temp);
        Grain = findViewById(R.id.Grain);
        Fade = findViewById(R.id.Fade);
        Exposure = findViewById(R.id.Exposure);
        Vibrance = findViewById(R.id.Vibrance);
        Hue = findViewById(R.id.Hue);
        Vignette = findViewById(R.id.Vignette);
        Tint= findViewById(R.id.Tint);


        //textView.
        textViewBrightness = findViewById(R.id.textViewBrightness);
        textViewContrast = findViewById(R.id.textViewContrast);
        textViewSaturation = findViewById(R.id.textViewSaturation);
        textViewSharpness = findViewById(R.id.textViewSharpness);
        textViewHighlights = findViewById(R.id.textViewHighlights);
        textViewShadows = findViewById(R.id.textViewShadows);
        textViewTemp = findViewById(R.id.textViewTemp);
        textViewGrain = findViewById(R.id.textViewGrain);
        textViewFade = findViewById(R.id.textViewFade);
        textViewExposure = findViewById(R.id.textViewExposure);
        textViewVibrance = findViewById(R.id.textViewVibrance);
        textViewHue = findViewById(R.id.textViewHue);
        textViewVignette= findViewById(R.id.textViewVignette);
        textViewTint= findViewById(R.id.textViewTint);


        DoneErasingLayout= findViewById(R.id.DoneErasingLayout);
        DoneAdjustingLayout= findViewById(R.id.DoneAdjustingLayout);
        AdjustTools= findViewById(R.id.AdjustTools);
        EraseTools= findViewById(R.id.EraseTools);

        DoneErasing= findViewById(R.id.DoneErasing);
        CancelErasing= findViewById(R.id.CancelErasing);

        EraseAdjustButton= findViewById(R.id.EraseAdjustButton);
        EraseAdjustButton.setBackgroundColor(Color.parseColor("#A6A061"));

        RestoreAdjustButton= findViewById(R.id.RestoreAdjustButton);


        AdjustOpacity = findViewById(R.id.AdjsutOpacity);
        AdjustSize = findViewById(R.id.AdjsutSize);
        AdjustHardness = findViewById(R.id.AdjsutHardness);

        AdjustOpacityValue = findViewById(R.id.AdjsutOpacityValue);
        AdjustSizeValue = findViewById(R.id.AdjsutSizeValue);
        AdjustHardnessValue = findViewById(R.id.AdjsutHardnessValue);



        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(AdjustImageActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });


        if (!AdjustTools().isEmpty()) {
            adjustToolsAdapter = new AdjustToolsAdapter(AdjustTools(), AdjustImageActivity.this, this);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(AdjustImageActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);
            recyclerView.setAdapter(adjustToolsAdapter);
        }
        AdjustImageView.setImage(image.getBitmap());


        Brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AdjustImageView.setBrightness(progress);
                textViewBrightness.setText(String.valueOf(progress));
                Log.d("Brightness", "onProgressChanged: "+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Contrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float contrast = (progress / 100f);
                AdjustImageView.setContrast(contrast);
                textViewContrast.setText(String.valueOf(progress));

                Log.d("contrast", "onProgressChanged: "+contrast);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Saturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /*float Saturation = progress/10f; */

                float Saturation ;

                Saturation = progress/100f; // Adjust this value as needed
                Log.d("Saturation", "onProgressChanged1: "+Saturation);

                AdjustImageView.setSaturation(Saturation);
                textViewSaturation.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Sharpness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float Sharpness = progress/100f;
                AdjustImageView.setSharpness(Sharpness);
                textViewSharpness.setText(String.valueOf(progress));

                Log.d("Clarity", "onProgressChanged1: "+Sharpness);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

        Highlights.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AdjustImageView.setHighlights(progress);
                textViewHighlights.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {



            }
        });

        Shadows.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AdjustImageView.setShadows(progress);
                textViewShadows.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Temp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AdjustImageView.setTemperature(progress);
                textViewTemp.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Grain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float Grain = progress/100f;
                AdjustImageView.setGrain(Grain);
                textViewGrain.setText(String.valueOf(progress));

                Log.d("Grain", "onProgressChanged1: "+Grain);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

        Fade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float Fade = progress/100f;
                AdjustImageView.setFade(Fade);
                textViewFade.setText(String.valueOf(progress));

                Log.d("Fade", "onProgressChanged1: "+Fade);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

        Exposure.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float Exposure = progress/100f;
                AdjustImageView.setExposure(Exposure);
                textViewExposure.setText(String.valueOf(progress));

                Log.d("Exposure", "onProgressChanged1: "+Exposure);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Vibrance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float Vibrance = progress/100f;
                AdjustImageView.setVibrance(Vibrance);
                textViewVibrance.setText(String.valueOf(progress));

                Log.d("Vibrance", "onProgressChanged1: "+Vibrance);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Vignette.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float Vignette = progress/100f;
                AdjustImageView.setVignette(Vignette);
                textViewVignette.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Hue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AdjustImageView.setHue(progress);
                textViewHue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Tint.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float Tint = progress/100f;
                AdjustImageView.setTint(progress);
                textViewTint.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        DoneAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdjustedBitmap = AdjustImageView.getAdjustedImage();

                image.setBitmap(AdjustedBitmap);
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(AdjustImageActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });

        CancelAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(AdjustImageActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });

        EraseAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DoneAdjustingLayout.setVisibility(View.INVISIBLE);
                AdjustTools.setVisibility(View.INVISIBLE);

                DoneErasingLayout.setVisibility(View.VISIBLE);
                EraseTools.setVisibility(View.VISIBLE);

                /*

                 */
                AdjustImageView.setEraseMode(true);

            }
        });

        SaveAsFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdjustImageActivity.this);
                builder.setTitle("Enter Your Effect Name");

                EditText input = new EditText(AdjustImageActivity.this);
                input.setHint("Name");

                LinearLayout layout = new LinearLayout(AdjustImageActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(50, 20, 50, 20);
                layout.addView(input);
                layout.setGravity(Gravity.CENTER);

                builder.setView(layout);

                builder.setPositiveButton("OK", (dialog, which) -> {
                    if (!input.getText().toString().trim().isEmpty()){
                        String text = input.getText().toString();
                        // Handle the text input
                        FilterSqlite dbHelper = new FilterSqlite(AdjustImageActivity.this);

                        // Insert a new filter
                        boolean inserted = dbHelper.insertData(
                                text,
                                AdjustImageView.getFilter()
                        );
                        if (inserted) {
                            Toast.makeText(AdjustImageActivity.this, "Filter Saved Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdjustImageActivity.this, "Failed to Save Filter", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(AdjustImageActivity.this, "Please Choose a Name", Toast.LENGTH_SHORT).show();
                    }

                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        DoneErasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdjustImageView.setEraseMode(false);

                DoneErasingLayout.setVisibility(View.INVISIBLE);
                EraseTools.setVisibility(View.INVISIBLE);

                DoneAdjustingLayout.setVisibility(View.VISIBLE);
                AdjustTools.setVisibility(View.VISIBLE);

            }
        });

        CancelErasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdjustImageView.restartErase();
                AdjustImageView.setEraseMode(false);
                DoneErasingLayout.setVisibility(View.INVISIBLE);
                EraseTools.setVisibility(View.INVISIBLE);

                DoneAdjustingLayout.setVisibility(View.VISIBLE);
                AdjustTools.setVisibility(View.VISIBLE);
            }
        });


        EraseAdjustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdjustImageView.setEraseType(false);
                EraseAdjustButton.setBackgroundColor(Color.parseColor("#A6A061"));
                RestoreAdjustButton.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        RestoreAdjustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdjustImageView.setEraseType(true);
                RestoreAdjustButton.setBackgroundColor(Color.parseColor("#A6A061"));
                EraseAdjustButton.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        AdjustOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AdjustImageView.setEraseOpacity(progress);
                AdjustOpacityValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                AdjustOpacityValue.setText(R.string.Opacity);

            }
        });

        AdjustSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AdjustImageView.setEraseSize(progress);
                AdjustSizeValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                AdjustSizeValue.setText(R.string.Size);
            }
        });

        AdjustHardness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("TAG", "onProgressChanged: "+(float) progress);
                AdjustHardnessValue.setText(String.valueOf(progress));
                AdjustImageView.setEraseHardness((float) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                AdjustHardnessValue.setText(R.string.Hardness);

            }
        });

    }

    @Override
    public void ToolInAsapRatioItemOnclick(String ToolsName) {

    }

    @Override
    public void ToolInAdjustItemOnClick(String ToolsName) {
        ShowTextTools(ToolsName);
    }

    public void ShowTextTools(String AdjustToolsName){
        switch (AdjustToolsName) {
            case adjustTools_name.BRIGHTNESS -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(0);
            }
            case adjustTools_name.CONTRAST -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(1);
            }
            case adjustTools_name.SATURATION -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(2);
            }
            case adjustTools_name.SHARPNESS -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(3);
            }
            case adjustTools_name.HIGHLIGHTS -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(4);
            }
            case adjustTools_name.SHADOWS -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(5);
            }
            case adjustTools_name.TEMP -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(6);
            }

            case adjustTools_name.FADE -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(7);
            }
            case adjustTools_name.GRAIN -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(8);
            }
            case adjustTools_name.VIBRANCE -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(9);
            }
            case adjustTools_name.EXPOSURE -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(10);
            }
            case adjustTools_name.VIGNETTE -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(11);
            }
            case adjustTools_name.HUE -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(12);
            }
            case adjustTools_name.TINT -> {
                AdjustToolsFlipper.setVisibility(View.VISIBLE);
                AdjustToolsFlipper.setDisplayedChild(13);
            }
        }
    }
    
    public class Adjust_Tools_Name {
        String RToolName;
        Drawable RToolsImg;

        public Adjust_Tools_Name() {
        }

        public Adjust_Tools_Name(String RToolName, Drawable RToolsImg) {
            this.RToolName = RToolName;
            this.RToolsImg = RToolsImg;
        }

        public String getRToolName() {
            return RToolName;
        }

        public Drawable getRToolsImg() {
            return RToolsImg;
        }

        public void setRToolName(String RToolName) {
            this.RToolName = RToolName;
        }

        public void setRToolsImg(Drawable RToolsImg) {
            this.RToolsImg = RToolsImg;
        }
    }

    static class adjustTools_name {
        public static final String BRIGHTNESS = "Brightness";
        public static final String CONTRAST = "Contrast";
        public static final String SATURATION = "Saturation";
        public static final String SHARPNESS = "Sharpness";
        public static final String HIGHLIGHTS = "Highlights";
        public static final String SHADOWS = "Shadows";
        public static final String TEMP = "Temp";
        public static final String GRAIN = "Grain";
        public static final String FADE = "Fade";
        public static final String VIBRANCE = "Vibrance";
        public static final String EXPOSURE = "Exposure";
        public static final String VIGNETTE = "Vignette";
        public static final String HUE = "Hue";
        public static final String TINT = "Tint";
    }
    // Fill the ArrayList of editing tools.
    @SuppressLint("UseCompatLoadingForDrawables")
    public ArrayList<Adjust_Tools_Name> AdjustTools() {
        ArrayList<Adjust_Tools_Name> AdjustTools = new ArrayList<>();
        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.BRIGHTNESS, getResources().getDrawable(R.drawable.brightness_ic, null)));

        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.CONTRAST, getResources().getDrawable(R.drawable.contrast_ic, null)));

        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.SATURATION, getResources().getDrawable(R.drawable.saturation_ic, null)));

        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.SHARPNESS, getResources().getDrawable(R.drawable.sharpen_ic, null)));

        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.VIGNETTE, getResources().getDrawable(R.drawable.vignette_ic, null)));

        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.HIGHLIGHTS,getResources().getDrawable(R.drawable.highliter_ic,null)));

        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.SHADOWS,getResources().getDrawable(R.drawable.shadows_ic,null)));

        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.TEMP,getResources().getDrawable(R.drawable.temp_ic,null)));

        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.HUE,getResources().getDrawable(R.drawable.hue_ic,null)));

        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.TINT,getResources().getDrawable(R.drawable.tint_ic,null)));

        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.FADE, getResources().getDrawable(R.drawable.fade_ic, null)));

        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.GRAIN,getResources().getDrawable(R.drawable.grain_ic,null)));

        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.VIBRANCE,getResources().getDrawable(R.drawable.vibrance_ic,null)));

        AdjustTools.add(new Adjust_Tools_Name(adjustTools_name.EXPOSURE,getResources().getDrawable(R.drawable.exposure_ic,null)));
        return AdjustTools;
    }

    /*
     @Override
    public void onBackPressed() {
        if (AdjustedBitmap!=null){
            image.setBitmap(AdjustImageView.getAdjustedImage());
            Intent intent = Editor_main_Activity.fa.getIntent();
            finish();
            startActivity(intent);
            super.onBackPressed();
        }else{
            Intent intent = Editor_main_Activity.fa.getIntent();
            finish();
            startActivity(intent);
            super.onBackPressed();
        }
    }
     */


    public void showValueGetter(TextView textView, SeekBar seekBar){
        SeekbarValueSettings seekbarValueSettings = new SeekbarValueSettings();
        seekbarValueSettings.OnValueManuelEdit(textView,AdjustImageActivity.this,seekBar);
    }
}