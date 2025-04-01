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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Rotate.RotateImageView;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.Zommer;
import com.phantomhive.exil.hellopics.R;

public class RotateImageActivity extends AppCompatActivity {
      Image image;
      Bitmap BitmapOnRotate;
      Bitmap BitmapfinishRotate;
      RotateImageView rotateImageView;
      ImageView imageViewDoneRotate,imageViewBaktoHome,imageViewSimpleRotate
              ,imageViewDegreeRotate,imageViewdoneshosedegre;
      ImageView imageView_Rotate_left,imageView_Rotate_Right;
      ImageView imageView_Flip_Horizontal,imageView_Flip_Vertical;
      ConstraintLayout constraintLayoutScaler,constraintLayoutSimpleRotate
              ,constraintLayoutDegreeRotate;
      EditText EdittextDegree;
      String Dgreetext;
      int Dgreeint;
      Zommer zommer = new Zommer();
      Bitmap ScaledBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_image);
        EdgeToEdgeFixing(R.id.activity_rotate_imageL,this);

         image =Image.getInstance();
         EdittextDegree = findViewById(R.id.TextNumberDegree);

         imageViewdoneshosedegre = findViewById(R.id.Rotate_By_Degre);
         imageViewSimpleRotate = findViewById(R.id.imageViewSimplerotate);
         imageViewDegreeRotate = findViewById(R.id.imageViewuserDegreeRotate);
         rotateImageView = findViewById(R.id.RotateImagView);
         imageViewBaktoHome = findViewById(R.id.Back_to_HomefromRotate);
         imageViewDoneRotate = findViewById(R.id.DoneRotate);
         imageView_Rotate_left = findViewById(R.id.imageView_Rotate_left);
         imageView_Rotate_Right = findViewById(R.id.imageView_Rotate_Right);
         imageView_Flip_Horizontal = findViewById(R.id.imageView_filp_Horizontal);
         imageView_Flip_Vertical = findViewById(R.id.imageView_filp_Vertical);

         constraintLayoutScaler = findViewById(R.id.constraint_Scaler_Rotate);
         constraintLayoutDegreeRotate = findViewById(R.id.constraintLayout_input_degre_user);
         constraintLayoutSimpleRotate = findViewById(R.id.constraintLayoutSimpleRotate);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(RotateImageActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });


        //Load image as a bitmap.
         constraintLayoutScaler.post(new Runnable() {
             @Override
             public void run() {
                 if (image.getBitmap()==null){
                     Toast.makeText(RotateImageActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                 }else {
                     ScaledBitmap = zommer.getScaledBitmap(image.getBitmap(),rotateImageView);
                     rotateImageView.setImageBitmap(ScaledBitmap);
                 }
             }
         });


        //Simple Rotate
        imageViewSimpleRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayoutDegreeRotate.setVisibility(View.INVISIBLE);
                constraintLayoutSimpleRotate.setVisibility(View.VISIBLE);
            }
        });

        // Rotate by Input
        imageViewDegreeRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayoutSimpleRotate.setVisibility(View.INVISIBLE);
                constraintLayoutDegreeRotate.setVisibility(View.VISIBLE);
            }
        });


        //Rotate by Dgree
        imageViewdoneshosedegre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dgreetext = EdittextDegree.getText().toString().trim();
                if (!Dgreetext.equals("")){
                    try {
                        Dgreeint = Integer.parseInt(Dgreetext);
                        if (Dgreeint>360){
                            Toast.makeText(RotateImageActivity.this, "The angle must be between 0 and 360 degrees", Toast.LENGTH_SHORT).show();
                        }else {
                            BitmapOnRotate = rotateImageView.getRotated_Image_by_Degree(ScaledBitmap,Dgreeint);
                            Toast.makeText(RotateImageActivity.this, String.valueOf(Dgreeint), Toast.LENGTH_SHORT).show();
                            rotateImageView.setImageBitmap(BitmapOnRotate);                        }
                    }catch (NumberFormatException numberFormatException){
                        Log.d("Dgreeint error","Something not god there");
                    }

                }

            }
        });


         //Rotate Bitamp To Right
         imageView_Rotate_Right.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 BitmapOnRotate = rotateImageView.getRotated_right_Image();
                 rotateImageView.setImageBitmap(BitmapOnRotate);             }
         });

          //Rotate Bitamp To Left
          imageView_Rotate_left.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   BitmapOnRotate = rotateImageView.getRotated_left_Image();
                   rotateImageView.setImageBitmap(BitmapOnRotate);               }
         });

        //Flip Bitamp Horizontal
        imageView_Flip_Horizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapOnRotate = rotateImageView.getFliped_Horizontal_Image();
                rotateImageView.setImageBitmap(BitmapOnRotate);
            }
        });

        //Flip Bitamp Vertical
        imageView_Flip_Vertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapOnRotate = rotateImageView.getFlipped_Vertical_Image();
                rotateImageView.setImageBitmap(BitmapOnRotate);            }
        });

        //Done Rotate
        imageViewDoneRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapfinishRotate = rotateImageView.getFinaleImage(image.getBitmap());

                image.setBitmap(BitmapfinishRotate);
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(RotateImageActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                // onBackPressed();
            }
        });

        //Back to main
        imageViewBaktoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(RotateImageActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });
    }

    /*
    @Override
    public void onBackPressed() {
        if (BitmapfinishRotate !=null){
            image.setBitmap(BitmapfinishRotate);
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

}