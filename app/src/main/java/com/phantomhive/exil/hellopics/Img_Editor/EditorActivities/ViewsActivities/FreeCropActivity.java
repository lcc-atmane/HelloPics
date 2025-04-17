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
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.Views.FreeCrop.FreeCropImageView;
import com.phantomhive.exil.hellopics.Img_Editor.Views.FreeCrop.PathType;
import com.phantomhive.exil.hellopics.R;

public class FreeCropActivity extends AppCompatActivity {
     Image image;
     ImageButton DoneFreeCrop,BackToHome;
     ImageButton Eraser,Repair;
     ImageButton RedoRight,RedoLeft;
     public SeekBar EraseSize,HardnessL,OpacityL;
     TextView textViewSize,textViewHardness,textViewOpacity;
     ConstraintLayout SettingLayout;
     FreeCropImageView freeCropImageView;
     ConstraintLayout constraintLayoutScale;
     Bitmap CroppedBitmap;

     ImageFilterView imageFilterView;
     ImageButton CropEraseImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_crop);
        EdgeToEdgeFixing(R.id.activity_free_cropL,this);

        image = Image.getInstance();
        DoneFreeCrop = findViewById(R.id.FreeCropDone);
        BackToHome = findViewById(R.id.FreeCropBack_to_Homefrom);
        Eraser = findViewById(R.id.EraserimageButton);
        Repair = findViewById(R.id.RepairimageButton);
        RedoRight = findViewById(R.id.FreeCropRedoRight);
        RedoLeft = findViewById(R.id.FreeCropRedoLeft);
        freeCropImageView = findViewById(R.id.FreeCropImageView);
        EraseSize = findViewById(R.id.FCerase_size_SB);
        OpacityL = findViewById(R.id.CropOpacity);
        HardnessL = findViewById(R.id.CropHardness);

        textViewSize = findViewById(R.id.textViewFCerase_size_SB);
        textViewOpacity = findViewById(R.id.CropTextOpacity);
        textViewHardness = findViewById(R.id.CropTextHardness);

        SettingLayout = findViewById(R.id.Eraser_settinglayout);
        constraintLayoutScale = findViewById(R.id.FreeCropImageViewscaler);
        CropEraseImage = findViewById(R.id.CropEraseImage);


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(FreeCropActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });


        //set Bitmap To freeCropImageView.

        Eraser.setBackgroundColor(Color.parseColor("#A6A061"));
        freeCropImageView.setImageBitmap(image.getBitmap());


        RedoLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FreeCropActivity.this, "in the next Version", Toast.LENGTH_SHORT).show();
            }
        });

        RedoRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FreeCropActivity.this, "in the next Version", Toast.LENGTH_SHORT).show();
            }
        });


        CropEraseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (freeCropImageView.getPathType() == PathType.ERASER){
                    CropEraseImage.setImageResource(R.drawable.crop_ic);
                    freeCropImageView.setPathType(PathType.BRUSH);
                }else{
                    CropEraseImage.setImageResource(R.drawable.cut_ic);
                    freeCropImageView.setPathType(PathType.ERASER);
                }

            }
        });

        //Eraser For erase the Bitmap!!.
        Eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FreeCropActivity.this, "Eraser", Toast.LENGTH_SHORT).show();
                Eraser.setBackgroundColor(Color.parseColor("#A6A061"));
                Repair.setBackgroundColor(Color.TRANSPARENT);
                freeCropImageView.setEraser(false);
            }
        });

        Eraser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowSetting(true);
                return true;
            }
        });

        // Repair for Repair the Bitmap
        Repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Repair.setBackgroundColor(Color.parseColor("#A6A061"));
                Eraser.setBackgroundColor(Color.TRANSPARENT);
                freeCropImageView.setEraser(true);
            }
        });

        Repair.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShowSetting(true);
                return true;
            }
        });
        EraseSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                freeCropImageView.SetEraserStrokeWidth(progress);
                textViewSize.setText(String.valueOf(progress));
            }

            @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    textViewSize.setText(R.string.Stroke);
            }
        });

        OpacityL.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                freeCropImageView.setOpacity(progress);
                textViewOpacity.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewOpacity.setText(R.string.Opacity);
            }
        });

        HardnessL.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                freeCropImageView.setHardness(progress);
                textViewHardness.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewHardness.setText(R.string.Hardness);
            }
        });


        // Done Cropping
        DoneFreeCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CroppedBitmap= freeCropImageView.getCroppedFreeImage();
                //CroppedBitmap= freeCropImageView.getb();
                //freeCropImageView.reset();
                //freeCropImageView.setImageBitmap(CroppedBitmap);

                image.setBitmap(CroppedBitmap);
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(FreeCropActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });

        // Back to Past Activity.
        BackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(FreeCropActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });
    }

    /*
    @Override
    public void onBackPressed() {
        if (CroppedBitmap!=null){
            image.setBitmap(CroppedBitmap);
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

    public void ShowSetting(boolean b){
        if (b){
            SettingLayout.setVisibility(View.VISIBLE);
        }else{
            SettingLayout.setVisibility(View.INVISIBLE);
        }
    }
}