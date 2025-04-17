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
package com.phantomhive.exil.hellopics.Create_Art_Classes;


import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;
import static com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards.createCheckerboard;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.textfield.TextInputEditText;
import com.phantomhive.exil.hellopics.Create_Art_Classes.GalleryAdapters.GalleryIntroAdapter;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg.All_images_Activity;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Gallery_picture_data.Images_data;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.HistorySettings.HistorySqliteDataBase;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.HistorySettings.SaveInHistory;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.Editor_Adapters.AsapRation_Adapter;
import com.phantomhive.exil.hellopics.Img_Editor.Interface.itemOnClick;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.ToolsName.AsapRationName_Number;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.AsapRatio_ND;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;
import com.phantomhive.exil.hellopics.R;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import org.jetbrains.annotations.Nullable;


import java.util.ArrayList;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChooseImageArtActivity extends AppCompatActivity implements Image_Item_ClickListener, itemOnClick {
       // RecyclerView for the firsts images jnnj.
       RecyclerView recyclerView_Gallery_intro;
       // start All_images_Activity.
       TextView see_All;
       //list for getting images path (files).
       ArrayList<Images_data>images;
       // Adapter of first images.
       GalleryIntroAdapter _galleryIntroAdapter;
       Image image;

    boolean a = false ,b= false;
    boolean On=true;
    Bitmap bitmap;
    int Bcolor = Color.TRANSPARENT;
    AlertDialog colorPickerDialog ;

    RecyclerView recyclerViewRatio;
    ArrayList<AsapRatio_ND> asapRatio_nds;
    AsapRation_Adapter asapRation_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shose__art_);
        EdgeToEdgeFixing(R.id.chooseArtL,this);


        image = Image.getInstance();

        //findViewById.
        recyclerView_Gallery_intro =findViewById(R.id.RecycleView_intro_to_gallery);
        see_All = findViewById(R.id.textView_See_All);
        recyclerViewRatio = findViewById(R.id.Paintsizes);


        //set the editing Tools Arraylist and set adapter.
        asapRatio_nds = ToolsHome();
        if (asapRatio_nds.isEmpty()){
            Log.d("Tools_Home_Arraylist","Arraylist is empty");
        }
        else {
            asapRation_adapter = new AsapRation_Adapter(ChooseImageArtActivity.this,asapRatio_nds,this);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ChooseImageArtActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewRatio.setLayoutManager(horizontalLayoutManager);
            recyclerViewRatio.setAdapter(asapRation_adapter);
        }

        // start All_images_Activity.
        see_All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AllImages = new Intent(ChooseImageArtActivity.this, All_images_Activity.class);
                startActivity(AllImages);
            }
        });
            // set the images files to list.
        images = getAllImagesByFolder();

        if(images.isEmpty()){
            //wen list is empty.
            Toast.makeText(this, "empty folder", Toast.LENGTH_SHORT).show();
        }
        // set adrpter to recyclerView.
        _galleryIntroAdapter = new GalleryIntroAdapter(images, ChooseImageArtActivity.this,this);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(ChooseImageArtActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_Gallery_intro.setLayoutManager(horizontalLayoutManagaer);
        recyclerView_Gallery_intro.setAdapter(_galleryIntroAdapter);
    }



    //get image data from storage
    // from https://medium.com/codex/android-simple-image-gallery-30c0f00abe64

    public ArrayList<Images_data> getAllImagesByFolder(){
        ArrayList<Images_data> images = new ArrayList<>();
        Uri allVideosuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = ChooseImageArtActivity.this.getContentResolver().query( allVideosuri, projection, null,null, null);

        try {
            cursor.moveToFirst();
            do{
                Images_data pic = new Images_data();
                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                images.add(pic);
            }while(cursor.moveToNext());
            cursor.close();
            ArrayList<Images_data> reSelection = new ArrayList<>();
            for(int i = images.size()-1;i > -1;i--){
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menu Connection.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu_creat_art, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean result;
        if (item.getItemId() == R.id.Back_to) {
            getOnBackPressedDispatcher().onBackPressed();
            result = true;
        } else {
            result = super.onOptionsItemSelected(item);
        }
        return result;
    }

    public boolean isBitmapTooLarge(Bitmap bitmap, Context context) {
        // Get the maximum texture size
        int maxTextureSize = getMaxTextureSize(context);

        // Get bitmap dimensions
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // Check if either dimension exceeds the max texture size
        return (bitmapWidth > maxTextureSize || bitmapHeight > maxTextureSize);
    }

    private int getMaxTextureSize(Context context) {
        // Get the GL10 instance
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        // Initialize display
        int[] version = new int[2];
        egl.eglInitialize(display, version);

        // Query total number of configurations
        int[] totalConfigurations = new int[1];
        egl.eglGetConfigs(display, null, 0, totalConfigurations);

        // Query actual list configurations
        EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
        egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

        int[] textureSize = new int[1];
        int maximumTextureSize = 0;

        // Iterate through all the configurations to get the maximum texture size
        for (int i = 0; i < totalConfigurations[0]; i++) {
            // Only need to check for width since opengl textures are always squared
            egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

            if (maximumTextureSize < textureSize[0])
                maximumTextureSize = textureSize[0];
        }

        // Release
        egl.eglTerminate(display);

        return maximumTextureSize;
    }
    public static int calculateSampleSize(
            int originalWidth, int originalHeight, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        if (originalHeight > reqHeight || originalWidth > reqWidth) {
            final int halfHeight = originalHeight / 2;
            final int halfWidth = originalWidth / 2;

            // Calculate the largest sampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / sampleSize) >= reqHeight
                    && (halfWidth / sampleSize) >= reqWidth) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

    public Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap,
                                                int reqWidth, int reqHeight) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int sampleSize = calculateSampleSize(width, height, reqWidth, reqHeight);

        if (sampleSize > 1) {
            int newWidth = width / sampleSize;
            int newHeight = height / sampleSize;
            return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        }
        return bitmap;
    }
    @Override
    public void OnChoosingTheMainPictureClick(String picturePath, Context PackageContext) {
        setMainPictureAndStartEditor(picturePath, PackageContext);
    }

    @Override
    public void OnChoosingTheOneTimePictureClick(String picturePath, Context PackageContext) {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setMainPictureAndStartEditor(String picturePath, Context PackageContext){
       Intent Editor = new Intent(PackageContext, Editor_main_Activity.class);
       Glide.with(PackageContext)
               .asBitmap()
               .load(picturePath)
               .into(new CustomTarget<Bitmap>() {
                   @Override
                   public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                       //boolean isTooBig = isBitmapTooLarge(resource, ChooseImageArtActivity.this);

                       // Get screen dimensions
                       DisplayMetrics displayMetrics = new DisplayMetrics();
                       WindowManager windowManager = (WindowManager) ChooseImageArtActivity.this.getSystemService(Context.WINDOW_SERVICE);
                       if (windowManager != null) {
                           windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
                       }
                       int screenWidth = displayMetrics.widthPixels;
                       int screenHeight = displayMetrics.heightPixels;


                       image.setBitmap(decodeSampledBitmapFromBitmap(resource,screenWidth,screenHeight));
                       image.setOriginalBitmap(decodeSampledBitmapFromBitmap(resource,screenWidth,screenHeight));
                       startActivity(Editor);
                   }

                   @Override
                   public void onLoadCleared(@Nullable Drawable placeholder) {

                   }
               });

   }
    // Fill the Arraylist of editing tools.
    @SuppressLint("UseCompatLoadingForDrawables")
    public  ArrayList<AsapRatio_ND> ToolsHome(){
        ArrayList<AsapRatio_ND> editingtools = new ArrayList<>();
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.CROPFREE,getResources().getDrawable(R.drawable.add_icc,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.SQUARE_INST,getResources().getDrawable(R.drawable.square_inst,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.PORTRAIT_INST,getResources().getDrawable(R.drawable.portrait_inst,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.STORY_INST,getResources().getDrawable(R.drawable.story_inst,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.POST_TWIT,getResources().getDrawable(R.drawable.post_twit,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.HEADER_TWIT,getResources().getDrawable(R.drawable.header_twit_ic,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.POST_FB,getResources().getDrawable(R.drawable.post_fb,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.COVER_FB,getResources().getDrawable(R.drawable.cover_fb_ic,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.COVER_YTB,getResources().getDrawable(R.drawable.youtub,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.POST_PINTEREST,getResources().getDrawable(R.drawable.post_pin,null)));
        return editingtools;
    }

    private void createBitmapAndStartEditor(int w, int h) {
        bitmap = Bitmap.createBitmap(w
                ,h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Bcolor);
        Intent Editor = new Intent(ChooseImageArtActivity.this, Editor_main_Activity.class);
        On = true;
        image.setBitmap(bitmap);
        image.setOriginalBitmap(bitmap);
        startActivity(Editor);
    }

    public void ShowImageResizeDialogue(int wdth,int heth){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Enter Details");

        bitmap = Bitmap.createBitmap(wdth
                ,heth, Bitmap.Config.ARGB_8888);

        Bitmap bm = Bitmap.createBitmap(bitmap);
        Canvas canvas = new Canvas(bm);
        canvas.drawColor(Bcolor);
        // Create the layout for the AlertDialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(50, 50, 50, 50);


        // Create the ImageView at the top (larger)
        CircleImageView topImageView = new CircleImageView(this);
        LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(400, 400); // Adjust the size of the larger image as needed
        topImageView.setLayoutParams(topParams);
        layout.addView(topImageView);

        if (bm.hasAlpha()){
            bm =createCheckerboard(topParams.width,topParams.height,20);

        }
        topImageView.setImageBitmap(bm);


        // Create the FrameLayout to hold the small square at the bottom-left
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Create the small square ImageView at the bottom-left
        ImageView bottomLeftSquare = new ImageView(this);
        bottomLeftSquare.setImageResource(R.drawable.color_ic); // Replace 'your_small_square_image' with the resource ID of the small square image you want to display
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(60, 60); // Adjust the size of the small square as needed
        params.gravity = Gravity.END | Gravity.BOTTOM;
        bottomLeftSquare.setLayoutParams(params);
        frameLayout.addView(bottomLeftSquare);

        // Add the FrameLayout to the main layout
        layout.addView(frameLayout);


        // Create the TextView and EditText for the first input
        TextView textView1 = new TextView(this);
        textView1.setText("Width:");
        textView1.setTextColor(Color.BLACK);
        textView1.setTextSize(16);
        layout.addView(textView1);

        // Create the EditText fields
        TextInputEditText editText1 = new TextInputEditText (this);
        editText1.setHint("Width");
        editText1.setGravity(Gravity.CENTER);
        editText1.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText1.setText(String.valueOf(wdth));
        layout.addView(editText1);

        // Create the ImageView
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.relation_ic);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setPadding(0, 20, 0, 20);
        layout.addView(imageView);


        // Create the TextView and EditText for the second input
        TextView textView2 = new TextView(this);
        textView2.setText("Height:");
        textView2.setTextColor(Color.BLACK);
        textView2.setTextSize(16);
        layout.addView(textView2);

        TextInputEditText  editText2 = new TextInputEditText (this);
        editText2.setHint("Height");
        editText2.setGravity(Gravity.CENTER);
        editText2.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText2.setText(String.valueOf(heth));
        layout.addView(editText2);


        // Set the layout to the AlertDialog
        alertDialogBuilder.setView(layout);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Width = editText1.getText().toString().trim();
                String Height = editText2.getText().toString().trim();

                // Do something with the entered text
                // For example, display a Toast message

                if (!Width.isEmpty()&&!Height.isEmpty()){
                    int w = Integer.parseInt(Width);
                    int h = Integer.parseInt(Height);
                    if (h!=0&&w!=0){
                        int pixels = ((w*h)/1000000);
                        if (pixels<10){
                            createBitmapAndStartEditor(w,h);
                        }else {
                            Toast.makeText(ChooseImageArtActivity.this, "The image cannot be larger than 10 megaPixel", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ChooseImageArtActivity.this, "The value of H:W should be more then 0", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ChooseImageArtActivity.this, "The value of H:W should be more then 0", Toast.LENGTH_SHORT).show();
                }


            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                On = true;
                dialog.cancel();
            }
        });

        // Show the AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    if (!b){
                        String Width = editText1.getText().toString();
                        int w = Integer.parseInt(Width);
                        if (w!=0){
                            a = true;
                            float division  = ((float)(wdth)/(float)(heth));
                            float newH = w/division;
                            editText2.setText(String.valueOf((int) newH));
                        }else {
                            editText2.setText(String.valueOf(1));
                        }

                    }
                } catch(NumberFormatException ex){
                    editText2.setText("");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                a = false;
            }
        };

        TextWatcher watcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!a){
                        b = true;
                        String Height = editText2.getText().toString();
                        int h = Integer.parseInt(Height);
                        if (h!=0){
                            float division1  = ((float)(heth)/(float)(wdth));
                            float newW = h/division1;

                            editText1.setText(String.valueOf((int)newW));
                        }else {
                            editText1.setText(String.valueOf(1));
                        }

                    }
                }catch(NumberFormatException ex){
                    editText1.setText("");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                b = false;
            }
        };

        if (!On){
            imageView.setImageResource(R.drawable.close_ic);
            editText1.removeTextChangedListener(watcher);
            editText2.removeTextChangedListener(watcher2);
        }else {
            imageView.setImageResource(R.drawable.relation_ic);
            editText1.addTextChangedListener(watcher);
            editText2.addTextChangedListener(watcher2);
        }

        bottomLeftSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerDialog = new ColorPickerDialog.Builder(ChooseImageArtActivity.this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(("Ok"),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        Bcolor = envelope.getColor();
                                        Bitmap bm = Bitmap.createBitmap(bitmap);
                                        Canvas canvas = new Canvas(bm);
                                        canvas.drawColor(Bcolor);
                                        topImageView.setImageBitmap(bm);
                                    }
                                })
                        .setNegativeButton(("Cancel"),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                        .attachAlphaSlideBar(true) // the default value is true.
                        .attachBrightnessSlideBar(true)  // the default value is true.
                        .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                        .show();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                On = !On;
                if (!On){
                    imageView.setImageResource(R.drawable.close_ic);
                    editText1.removeTextChangedListener(watcher);
                    editText2.removeTextChangedListener(watcher2);
                }else {
                    imageView.setImageResource(R.drawable.relation_ic);
                    editText1.addTextChangedListener(watcher);
                    editText2.addTextChangedListener(watcher2);
                }
            }
        });
        if (!On){
            editText1.removeTextChangedListener(watcher);
            editText2.removeTextChangedListener(watcher2);
        }else {
            editText1.addTextChangedListener(watcher);
            editText2.addTextChangedListener(watcher2);
        }
    }

    @Override
    public void ToolInAsapRatioItemOnclick(String ToolsName) {
        switch (ToolsName){
            case AsapRationName_Number.CROPFREE:
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                On = false;
                ShowImageResizeDialogue(width,height);
                break;
                case AsapRationName_Number.SQUARE_INST:
                    On = true;
                    ShowImageResizeDialogue(1080,1080);
                    break;
                case AsapRationName_Number.PORTRAIT_INST:
                    On = true;
                    ShowImageResizeDialogue(1080,1350);
                    break;
                case AsapRationName_Number.STORY_INST:
                    On = true;
                    ShowImageResizeDialogue(1080,1920);
                    break;
                case AsapRationName_Number.POST_TWIT:
                    On = true;
                    ShowImageResizeDialogue(1600,900 );
                break;
                case AsapRationName_Number.POST_FB:
                    On = true;
                    ShowImageResizeDialogue(1200 ,630);
                    break;
                case AsapRationName_Number.COVER_YTB:
                    On = true;
                    ShowImageResizeDialogue(2560 ,1440);
                    break;
                    case AsapRationName_Number.HEADER_TWIT:
                        On = true;
                        ShowImageResizeDialogue(1500  ,500 );
                    break;
                    case AsapRationName_Number.COVER_FB:
                        On = true;
                        ShowImageResizeDialogue(820,312);
                    break;
                    case AsapRationName_Number.POST_PINTEREST:
                        On = true;
                        ShowImageResizeDialogue(1000,1500);
                        break;
        }
    }

    @Override
    public void ToolInAdjustItemOnClick(String ToolsName) {

    }

}
