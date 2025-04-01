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