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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.Views.PerspectiveScale.PerspectiveScale;
import com.phantomhive.exil.hellopics.R;

public class ScaleActivity extends AppCompatActivity {
    Image image;
    ConstraintLayout constraintLayout;
    PerspectiveScale ScaleImageView;
    ImageButton DoneScale, BackHome;
    TextView BottomTop,RightLeft;
    ConstraintLayout RL,BT;

    SeekBar RightTilt, LeftTilt;
    SeekBar TopTilt, BottomTilt;

    TextView textRight,textTop,textBottom,textLeft;

    Bitmap Originalbitmap;
    Bitmap BitmapFinishScale;

    Bitmap bm;

    Canvas canvas;
    Matrix matrix = new Matrix();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale);
        EdgeToEdgeFixing(R.id.activity_scaleL,this);

        image = Image.getInstance();
        constraintLayout = findViewById(R.id.constraint_Scaler_Scale);
        ScaleImageView = findViewById(R.id.ScaleImagView);

        DoneScale = findViewById(R.id.DoneScale);
        BackHome = findViewById(R.id.Back_to_HomefromScale);
        BottomTop = findViewById(R.id.Bottom_Top);
        RightLeft = findViewById(R.id.Right_Left);

        RL =findViewById(R.id.ConsRightLeft);
        BT =findViewById(R.id.ConsBottomTop);

        RightTilt = findViewById(R.id.seekBartiltup);
        LeftTilt = findViewById(R.id.seekBartiltdown);
        TopTilt = findViewById(R.id.seekBarScaleWidth);
        BottomTilt = findViewById(R.id.seekBarScaleHeight);

        textRight = findViewById(R.id.textViewRightRespective);
        textLeft = findViewById(R.id.textViewLeftRespective);
        textTop = findViewById(R.id.textViewTopRespective);
        textBottom = findViewById(R.id.textViewBottomRespective);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(ScaleActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });


        // set Bitmap To ImageView.
        ScaleImageView.setImageBitmap(image.getBitmap());



        Originalbitmap = image.getBitmap();

        //Originalbitmap = ViewZommer.getScaledBitmap(image.getBitmap(),ScaleimageView);

        bm = Bitmap.createBitmap(Originalbitmap.getWidth(), Originalbitmap.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bm);

        int hdH = Originalbitmap.getHeight()/2;
        int HeightMax = Originalbitmap.getHeight()-hdH;
        BottomTilt.setMax(HeightMax);
        textBottom.setText("Bottom");

        int hdW = Originalbitmap.getWidth()/2;
        int WidthMax = Originalbitmap.getWidth()-hdW;
        TopTilt.setMax(WidthMax);
        textTop.setText("Top");


        int hdU = Originalbitmap.getHeight()/2;
        int tiltMax = Originalbitmap.getHeight()-hdU;
        RightTilt.setMax(tiltMax);
        textRight.setText("Right");

        int hdD = Originalbitmap.getWidth()/2;
        int DownMax = Originalbitmap.getWidth()-hdD;
        LeftTilt.setMax(DownMax);
        textLeft.setText("left");


        BottomTilt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ScaleImageView.TiltBitmap(RightTilt.getProgress(),LeftTilt.getProgress(),BottomTilt.getProgress(),TopTilt.getProgress());//
                textBottom.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textBottom.setText("Bottom");
            }
        });
        TopTilt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ScaleImageView.TiltBitmap(RightTilt.getProgress(),LeftTilt.getProgress(),BottomTilt.getProgress(),TopTilt.getProgress());//
                textTop.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textTop.setText("Top");
            }
        });
        RightTilt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ScaleImageView.TiltBitmap(RightTilt.getProgress(),LeftTilt.getProgress(),BottomTilt.getProgress(),TopTilt.getProgress());//
                textRight.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textRight.setText("Right");
            }
        });
        LeftTilt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ScaleImageView.TiltBitmap(RightTilt.getProgress(),LeftTilt.getProgress(),BottomTilt.getProgress(),TopTilt.getProgress());//
                textLeft.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textLeft.setText("left");
            }
        });

      DoneScale.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              BitmapFinishScale = ScaleImageView.getFinalBitmap();

              image.setBitmap(BitmapFinishScale);
              // Finish current activity to go back
              finish();
              // Start the same activity again
              Intent backToMain = new Intent(ScaleActivity.this, Editor_main_Activity.class);
              startActivity(backToMain);

              //onBackPressed();
          }
      });

        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(ScaleActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });
        RightLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RightLeft.setTextColor(Color.parseColor("#A6A061"));
                BottomTop.setTextColor(Color.BLACK);
                RL.setVisibility(View.VISIBLE);
                BT.setVisibility(View.INVISIBLE);
            }
        });

        BottomTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomTop.setTextColor(Color.parseColor("#A6A061"));
                RightLeft.setTextColor(Color.BLACK);
                BT.setVisibility(View.VISIBLE);
                RL.setVisibility(View.INVISIBLE);
            }
        });
    }

    /*
    @Override
    public void onBackPressed() {
        if (BitmapFinishScale !=null){
            image.setBitmap(ScaleImageView.getFinalBitmap());
            Intent intent = Editor_main_Activity.fa.getIntent();
            finish();
            startActivity(intent);
            super.onBackPressed();
        }else {
            Intent intent = Editor_main_Activity.fa.getIntent();
            finish();
            startActivity(intent);
            super.onBackPressed();
        }
    }
     */


    public void TiltBitmap(int widthRight,int widthLeft,int heightBottom, int heightTop){

        widthRight = RightTilt.getProgress();
        widthLeft = LeftTilt.getProgress();

        heightBottom = BottomTilt.getProgress();
        heightTop = TopTilt.getProgress();



        float[] src = {0, 0,
                Originalbitmap.getWidth(), 0,
                Originalbitmap.getWidth(), Originalbitmap.getHeight(),
                0, Originalbitmap.getHeight()};



        float[] dst = {  heightTop*-1,  widthLeft *-1, // top left
                Originalbitmap.getWidth()-heightTop*-1, widthRight *-1, // top right
                Originalbitmap.getWidth()-heightBottom*-1, Originalbitmap.getHeight() - widthRight *-1, // bottom right
                heightBottom*-1, Originalbitmap.getHeight()- widthLeft *-1 // bottom left
        };
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        matrix.setPolyToPoly(src,0,dst,0,4);

        // Calculate scaling factor based on tilt
        float scaleX = Math.max(1, (Originalbitmap.getWidth() + Math.abs(widthLeft) + Math.abs(widthRight)) / Originalbitmap.getWidth());
        float scaleY = Math.max(1, (Originalbitmap.getHeight() + Math.abs(heightTop) + Math.abs(heightBottom)) / Originalbitmap.getHeight());
        float scale = Math.max(scaleX, scaleY);

        // Apply scaling
        matrix.postScale(scale, scale, Originalbitmap.getWidth() / 2f, Originalbitmap.getHeight() / 2f);
        canvas.drawBitmap(Originalbitmap,matrix,paint);
        ScaleImageView.setImageBitmap(bm);
    }
}