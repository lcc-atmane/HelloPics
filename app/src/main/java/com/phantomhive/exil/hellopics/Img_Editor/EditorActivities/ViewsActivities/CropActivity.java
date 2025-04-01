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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.Editor_Adapters.AsapRation_Adapter;
import com.phantomhive.exil.hellopics.Img_Editor.Interface.itemOnClick;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.ToolsName.AsapRationName_Number;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Cropper.CropImageView;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.AsapRatio_ND;
import com.phantomhive.exil.hellopics.R;

import java.util.ArrayList;

public class CropActivity extends AppCompatActivity implements itemOnClick {
    Bitmap BitmapFinalCrop;
    CropImageView cropImageView;
    ImageView imageViewCrop,imageViewBaktoHome;
    Image image;

    RecyclerView recyclerViewRatio;
    ArrayList<AsapRatio_ND> asapRatio_nds;
    AsapRation_Adapter asapRation_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        EdgeToEdgeFixing(R.id.CropActivityL,this);

        cropImageView = findViewById(R.id.CroperImagView);
        imageViewCrop = findViewById(R.id.DoneCropping);
        imageViewBaktoHome = findViewById(R.id.Back_to_HomefromCrop);
        recyclerViewRatio = findViewById(R.id.AsapRatioRecyleView);

        image = Image.getInstance();


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(CropActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });


        //Load image as a bitmap.

        cropImageView.setImageBitmap(image.getBitmap());

        //set the editing Tools Arraylist and set adapter.
        asapRatio_nds = ToolsHome();
        if (asapRatio_nds.isEmpty()){
            Log.d("Tools_Home_Arraylist","Arraylist is empty");
        }
        else {
            asapRation_adapter = new AsapRation_Adapter(CropActivity.this,asapRatio_nds,this);
            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(CropActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewRatio.setLayoutManager(horizontalLayoutManagaer);
            recyclerViewRatio.setAdapter(asapRation_adapter);
        }

        //Crop Bitmap and back to main
        imageViewCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapFinalCrop = cropImageView.getCroppedImage();
                //scalerImageView.ScaleImageView(cropImageView,CropActivity.this, BitmapfinishCrop,constraintLayoutCrop);

                image.setBitmap(BitmapFinalCrop);
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(CropActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });
        //back to main
        imageViewBaktoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(CropActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                // onBackPressed();
            }
        });
    }

    // Fill the Arraylist of editing tools.
    @SuppressLint("UseCompatLoadingForDrawables")
    public  ArrayList<AsapRatio_ND> ToolsHome(){
        ArrayList<AsapRatio_ND> editingtools = new ArrayList<>();
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.CROPFREE,getResources().getDrawable(R.drawable.crop_free_ic,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.SQUARE_INST,getResources().getDrawable(R.drawable.square_inst,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.PORTRAIT_INST,getResources().getDrawable(R.drawable.portrait_inst,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.STORY_INST,getResources().getDrawable(R.drawable.story_inst,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.POST_TWIT,getResources().getDrawable(R.drawable.post_twit,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.HEADER_TWIT,getResources().getDrawable(R.drawable.header_twit_ic,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.POST_FB,getResources().getDrawable(R.drawable.post_fb,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.COVER_FB,getResources().getDrawable(R.drawable.cover_fb_ic,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.COVER_YTB,getResources().getDrawable(R.drawable.youtub,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.MORE,getResources().getDrawable(R.drawable.add_ic,null)));
        return editingtools;
    }

    /*
      @Override
    public void onBackPressed() {
        if (BitmapFinalCrop !=null){
            image.setBitmap(BitmapFinalCrop);
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


    @Override
    public void ToolInAsapRatioItemOnclick(String ToolsName) {
        switch (ToolsName){
            // true
            case AsapRationName_Number.CROPFREE:
                cropImageView.setAspectRatio(1,1);
                cropImageView.setFixedAspectRatio(false);
                cropImageView.setGuidelines(1);
                break;
            // true
            case AsapRationName_Number.SQUARE_INST:
                 cropImageView.setAspectRatio(1,1);
                 cropImageView.setFixedAspectRatio(true);
                 cropImageView.setGuidelines(1);
                break;
                // true
            case AsapRationName_Number.PORTRAIT_INST:
                cropImageView.setAspectRatio(4,5);
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setGuidelines(1);
                break;
                // true
            case AsapRationName_Number.STORY_INST:
                cropImageView.setAspectRatio(9,16);
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setGuidelines(1);
                break;
                // true
            case AsapRationName_Number.POST_TWIT:
            case AsapRationName_Number.POST_FB:
            case AsapRationName_Number.COVER_YTB:
                cropImageView.setAspectRatio(16,9);
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setGuidelines(1);
                break;
                // true
            case AsapRationName_Number.HEADER_TWIT:
                // false!!
            case AsapRationName_Number.COVER_FB:
                cropImageView.setAspectRatio(3,1);
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setGuidelines(1);
                break;
            // true.
            case AsapRationName_Number.POST_PINTEREST:
                cropImageView.setAspectRatio(2,3);
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setGuidelines(1);
                break;
            case AsapRationName_Number.MORE:
                ShowImageResizeDialogue();
                break;
        }
    }

    public void ShowImageResizeDialogue(){

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Enter Details");

        // Create the layout for the AlertDialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(50, 50, 50, 50);


        // Create the TextView and EditText for the first input
        TextView textView1 = new TextView(this);
        textView1.setText("X-ration:");
        textView1.setTextColor(Color.BLACK);
        textView1.setTextSize(16);
        layout.addView(textView1);

        // Create the EditText fields
        TextInputEditText editText1 = new TextInputEditText (this);
        editText1.setHint("X-ration");
        editText1.setGravity(Gravity.CENTER);
        editText1.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText1.setText(String.valueOf(1));
        layout.addView(editText1);

        // Create the ImageView
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.relation_ic);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setPadding(0, 20, 0, 20);
        layout.addView(imageView);


        // Create the TextView and EditText for the second input
        TextView textView2 = new TextView(this);
        textView2.setText("Y-ration");
        textView2.setTextColor(Color.BLACK);
        textView2.setTextSize(16);
        layout.addView(textView2);

        TextInputEditText  editText2 = new TextInputEditText (this);
        editText2.setHint("Y-ration");
        editText2.setGravity(Gravity.CENTER);
        editText2.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText2.setText(String.valueOf(1));
        layout.addView(editText2);



        // Set the layout to the AlertDialog
        alertDialogBuilder.setView(layout);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Width = editText1.getText().toString().trim();
                String Height = editText2.getText().toString().trim();

                if (!Width.isEmpty()&&!Height.isEmpty()){
                    int x = Integer.parseInt(Width);
                    int y = Integer.parseInt(Height);

                    if (y>0&&x>0){
                        cropImageView.setAspectRatio(x,y);
                        cropImageView.setFixedAspectRatio(true);
                        cropImageView.setGuidelines(1);
                    }else {
                        Toast.makeText(CropActivity.this, "The value of x:y should be more then 0", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(CropActivity.this, "The value of x:y should be more then 0", Toast.LENGTH_SHORT).show();
                }


            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the AlertDialog
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void ToolInAdjustItemOnClick(String ToolsName) {

    }
}