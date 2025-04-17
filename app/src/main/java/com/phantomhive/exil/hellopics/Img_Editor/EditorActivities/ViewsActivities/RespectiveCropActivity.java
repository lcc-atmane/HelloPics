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
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.Views.RespectiveCrop.RespectiveCrop;
import com.phantomhive.exil.hellopics.R;

public class RespectiveCropActivity extends AppCompatActivity {

    RespectiveCrop respectiveCropActivity;
    ConstraintLayout constraintLayoutScale;
    ImageButton crop;
    Image image;
    ImageButton BackHome;
    Bitmap CroppedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respective_crop);
        EdgeToEdgeFixing(R.id.activity_respective_cropL,this);

        respectiveCropActivity = findViewById(R.id.RespectiveCropImageView);
        constraintLayoutScale = findViewById(R.id.RespectiveCropScaler);
        crop = findViewById(R.id.DoneRespectiveCropping);
        BackHome =findViewById(R.id.Back_to_HomefromRespectiveCrop);
        image = Image.getInstance();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(RespectiveCropActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });

        // Scale ImageView.
        respectiveCropActivity.setImageBitmap(image.getBitmap());

        //Crop Bitmap and back to main.
       /*
       crop.setOnClickListener(v -> {
             respectiveCropActivity.getImage();
                 });
         */

        crop.setOnClickListener(v -> {
            CroppedBitmap =respectiveCropActivity.getImage();

            image.setBitmap(CroppedBitmap);
            // Finish current activity to go back
            finish();
            // Start the same activity again
            Intent backToMain = new Intent(RespectiveCropActivity.this, Editor_main_Activity.class);
            startActivity(backToMain);

           // onBackPressed();
        });

        //back to main.
        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(RespectiveCropActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed()
            }
        });
    }

    /*
    @Override
    public void onBackPressed() {
        Intent intent;
        intent = Editor_main_Activity.fa.getIntent();
        if (CroppedBitmap!=null){
            image.setBitmap(CroppedBitmap);
             finish();
             startActivity(intent);
             super.onBackPressed();
        }else {
           finish();
           startActivity(intent);
           super.onBackPressed();
        }
    }
     */

}