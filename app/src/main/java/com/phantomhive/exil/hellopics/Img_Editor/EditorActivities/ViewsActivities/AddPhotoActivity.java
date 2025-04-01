/*
 * Copyright (C) 2024 Your Name (or Your Company)
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

import static com.phantomhive.exil.hellopics.Create_Art_Classes.GalleryAdapters.AllimagesAdapter.SIMGN;
import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg.All_images_Activity;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg.Gallery_UseType;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.ToolsName.ModeName;
import com.phantomhive.exil.hellopics.Img_Editor.Views.AddImages.AddImagesImageView;
import com.phantomhive.exil.hellopics.Img_Editor.Views.AddImages.DrawType;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.Mode_Name;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;
import com.phantomhive.exil.hellopics.R;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;



public class AddPhotoActivity extends AppCompatActivity {
    AddImagesImageView addImagesImageView;
    Image image;
    Bitmap result =null;
    RecyclerView ToolsRecyclerView;
    FloatingActionButton AddImage;
    ArrayList<ImagesTools> ImagesTools;
    ImagesToolsAdapter ImgToolsAdapter;
    ImageButton DoneAdd,CancelAdd;

    ConstraintLayout AddOptions;

    ViewFlipper viewFlipper;
    public SeekBar ImagesOpacity,ImagesRotate,ImagesScale;
    TextView TextOpacity,TextRotate,TextScale;

    RecyclerView ImagesMode;
    RecyclerView imageColorFilterMode;
    ImagesMode_Adapter imagesMode_adapter;
    ImagesFilterMode_Adapter imagesFilterMode_adapter;
    ImageView Image_Rotate_left, Image_Rotate_Right;
    ImageView Image_Flip_Horizontal, Image_Flip_Vertical;
    ImageButton SetColor,DetectColor;
    AlertDialog colorPickerDialog ;

    BottomSheetDialog bottomSheetDialog;

    ProgressBar progressBar;

    ImageButton EEB,CEB,DEB,ERB;
    ConstraintLayout EraseAddedBmSetting;
    public SeekBar EraseAddedBmOpacitySeekBar,EraseAddedBmSizeSeekBar,EraseAddedBmHardnessSeekBar;
    TextView EraseAddedBmOpacity,EraseAddedBmHardness,EraseAddedBmSize;


    ExecutorService executorService;
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {


                    if (result.getResultCode() == 78) {

                        Intent intent = result.getData();
                        if (intent != null) {

                            ArrayList<String> bitmapPathList = intent.getStringArrayListExtra("BITMAP_LIST");
                            if (bitmapPathList!=null){
                                progressBar.setVisibility(View.VISIBLE);
                                int totalImages = bitmapPathList.size();

                                AtomicInteger counter = new AtomicInteger(0);
                                ArrayList<Bitmap> RealBitmapList = new ArrayList<>();
                                for (int i = 0; i < totalImages; i++) {
                                    Glide.with(AddPhotoActivity.this)
                                            .asBitmap()
                                            .load(bitmapPathList.get(i))
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    // Add the loaded bitmap to the ScaledBitmapList

                                                    executorService = Executors.newFixedThreadPool(1);

                                                    Future<Bitmap> futureBitmap = executorService.submit(() ->addImagesImageView.decodeSampledBitmapFromBitmap(resource,
                                                            addImagesImageView.getWidth(),
                                                            addImagesImageView.getHeight()));

                                                    try {
                                                        Bitmap bitmap = futureBitmap.get(); // Get the result (blocking)
                                                        if (bitmap != null) {
                                                            RealBitmapList.add(bitmap);
                                                        } else {
                                                            Toast.makeText(AddPhotoActivity.this, "Error repeat", Toast.LENGTH_SHORT).show();

                                                            // Handle the case where the bitmap couldn't be loaded
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }



                                                    // Increment the counter
                                                    int currentCount = counter.incrementAndGet();

                                                    // Check if all images have been loaded
                                                    if (currentCount == totalImages) {
                                                        // All images have been loaded
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        addImagesImageView.addDrawImages(RealBitmapList);

                                                        // Process the ScaledBitmapList here
                                                    }
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    // Handle the case where the image load is cleared
                                                    // This may occur if the target is reused or cleared
                                                }
                                            });
                                }
                            }



                        }
                    }
                }
            });
    boolean colorloc = false;

    /*
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == 78) {
                    progressBar.setVisibility(View.VISIBLE);
                    Intent intent = result.getData();
                    if (intent != null) {
                        ArrayList<String> bitmapPathList = intent.getStringArrayListExtra("BITMAP_LIST");
                        int totalImages = bitmapPathList.size();
                        ArrayList<Bitmap> bitmapList = new ArrayList<>();

                        Executor executor = Executors.newFixedThreadPool(4); // Limit concurrent tasks to 4

                        // Use CountDownLatch to wait for all images to be loaded
                        CountDownLatch latch = new CountDownLatch(totalImages);

                        for (int i = 0; i < totalImages; i++) {
                            final int currentIndex = i; // Keep track of the index for the callback

                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap bitmap = Glide.with(AddPhotoActivity.this)
                                        .asBitmap()
                                        .load(bitmapPathList.get(currentIndex))
                                        .submit() // Submit the image load request
                                        .get(); // Wait for the result

                                    bitmapList.add(addImagesImageView.decodeSampledBitmapFromBitmap(bitmap,
                                        addImagesImageView.getWidth(),
                                        addImagesImageView.getHeight()));

                                    latch.countDown();
                                }
                            });
                        }

                        try {
                            // Wait until all images are loaded
                            latch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // All images have been loaded
                        progressBar.setVisibility(View.INVISIBLE);
                        addImagesImageView.addDrawImages(bitmapList);
                        // Process the bitmapList here
                    }
                }
            }
        });

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        EdgeToEdgeFixing(R.id.activity_add_photoL,this);

        image =Image.getInstance();
        addImagesImageView = findViewById(R.id.AddImagesImageView);
        progressBar = findViewById(R.id.AddPhotosProgressBar);
        DoneAdd = findViewById(R.id.DoneAddingImg);
        CancelAdd = findViewById(R.id.CancelAddingImg);

        AddImage = findViewById(R.id.AddImageFloatingButton);
        ToolsRecyclerView = findViewById(R.id.ImageOptionRecycleView);

        AddOptions = findViewById(R.id.AddOption);


        viewFlipper = findViewById(R.id.ImagesToolsFlipper);
        ImagesOpacity = findViewById(R.id.ImageOpacity);
        ImagesRotate =findViewById(R.id.ImageRotate);
        ImagesScale = findViewById(R.id.ImageScale);
        ImagesMode = findViewById(R.id.ImagesMode);

        imageColorFilterMode = findViewById(R.id.imageColorFilterMode);

        TextRotate = findViewById(R.id.TextViewRotateImage);
        TextOpacity = findViewById(R.id.TextViewOpacityImage);
        TextScale = findViewById(R.id.TextViewScaleImage);


        // Get screen dimensions
        Bitmap bitmap;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) AddPhotoActivity.this.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        }
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        // Check if the bitmap size is smaller than the screen resolution
        if (image.getBitmap().getWidth() < screenWidth && image.getBitmap().getHeight() < screenHeight) {
            bitmap = ViewZommer.getScaledBitmapBasedOnScreen(image.getBitmap(),Math.max(screenWidth,screenHeight),Math.max(screenWidth,screenHeight));
        }else {
            bitmap =image.getBitmap();
        }

        if (bitmap == null) {
            Toast.makeText(AddPhotoActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        } else {
            addImagesImageView.setImageBitmap(bitmap);
        }

        Image_Rotate_left = findViewById(R.id.Image_Rotate_left);
        Image_Rotate_Right = findViewById(R.id.Image_Rotate_Right);
        Image_Flip_Horizontal = findViewById(R.id.Image_filp_Horizontal);
        Image_Flip_Vertical = findViewById(R.id.Image_filp_Vertical);

        SetColor = findViewById(R.id.SetAdeddimgColor);
        DetectColor = findViewById(R.id.detectAdeddimgColor);


        CEB = findViewById(R.id.IBCancelErase);
        DEB = findViewById(R.id.IBDoneErase);
        EEB = findViewById(R.id.IBRestourneErase);
        EEB.setBackgroundColor(Color.parseColor("#A6A061"));
        ERB = findViewById(R.id.IBRestoreErase);

        EraseAddedBmSetting  = findViewById(R.id.EraseAddedBmSetting);

        EraseAddedBmOpacitySeekBar = findViewById(R.id.EraseAddedBmOpacity);
        EraseAddedBmSizeSeekBar = findViewById(R.id.EraseAddedBmSize);
        EraseAddedBmHardnessSeekBar =findViewById(R.id.EraseAddedBmHardness);
        
        EraseAddedBmOpacity = findViewById(R.id.EraseAddedBmOpacityValue);
        EraseAddedBmSize = findViewById(R.id.EraseAddedBmSizeValue);
        EraseAddedBmHardness = findViewById(R.id.EraseAddedBmHardnessValue);
        
        bottomSheetDialog = new BottomSheetDialog(AddPhotoActivity.this);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SIMGN = 0;
                addImagesImageView.CancelAdd();
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(AddPhotoActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });

        if (!ImagesToolsList().isEmpty()){
            ImagesTools = ImagesToolsList();
            ImgToolsAdapter = new ImagesToolsAdapter(AddPhotoActivity.this,ImagesTools,addImagesImageView);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(AddPhotoActivity.this, LinearLayoutManager.HORIZONTAL, false);
            ToolsRecyclerView.setLayoutManager(horizontalLayoutManager);
            ToolsRecyclerView.setAdapter(ImgToolsAdapter);
        }

        if (!ModeNames().isEmpty()){
            imagesMode_adapter = new ImagesMode_Adapter(this, ModeNames(), addImagesImageView);
            ImagesMode.setAdapter(imagesMode_adapter);
            ImagesMode.setLayoutManager(new LinearLayoutManager(AddPhotoActivity.this,LinearLayoutManager.HORIZONTAL,false));
        }

        if (!ModeNames().isEmpty()){
            imagesFilterMode_adapter = new ImagesFilterMode_Adapter(this, ModeNames(), addImagesImageView);
            imageColorFilterMode.setAdapter(imagesFilterMode_adapter);
            imageColorFilterMode.setLayoutManager(new LinearLayoutManager(AddPhotoActivity.this,
                    LinearLayoutManager.HORIZONTAL,false));
        }

        addImagesImageView.ClickCircles(new AddImagesImageView.ClickCircles() {
            @Override
            public void ClickTopLeft(boolean b, AddImagesImageView addImagesImageView) {
                if (b){
                    addImagesImageView.RemoveCopy();
                    SIMGN -= 1;

                }
            }

            @Override
            public void ClickTopRight(boolean b, AddImagesImageView addImagesImageView) {
            }

            @Override
            public void ClickBottomRight(boolean b, AddImagesImageView addImagesImageView) {

            }

            @Override
            public void ClickBottomLeft(boolean b, AddImagesImageView addImagesImageView) {
                if (b){
                    addImagesImageView.AddCopy();
                }
            }
        });

        AddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAdPhotoTypeDialog();
            }
        });

        DoneAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SIMGN = 0;
                result = addImagesImageView.getFinalImage();
                addImagesImageView.DoneAdd();
                addImagesImageView.setImageBitmap(result);


                image.setBitmap(result);
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(AddPhotoActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });

        CancelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SIMGN = 0;
                addImagesImageView.CancelAdd();
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(AddPhotoActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
               // onBackPressed();
            }
        });

        Image_Flip_Horizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImagesImageView.flipH();
            }
        });

        Image_Flip_Vertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImagesImageView.flipV();
            }
        });

        Image_Rotate_Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImagesImageView.RotateR();
            }
        });

        SetColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (colorloc){
                    addImagesImageView.setColorLoc(false);
                    colorloc = false;
                }
                colorPickerDialog = new ColorPickerDialog.Builder(AddPhotoActivity.this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(("Ok"),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        addImagesImageView.setImagesColor(envelope.getColor());
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

        DetectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorloc = true;
                addImagesImageView.setColorLoc(true);
            }
        });

        Image_Rotate_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImagesImageView.RotateL();
            }
        });

        ImagesOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                addImagesImageView.setImagesOpacity(progress);
                TextOpacity.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ImagesRotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                addImagesImageView.setRotateDegree(progress);
                TextRotate.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        ImagesScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                TextScale.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        CEB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImagesImageView.CancelErase();
                viewFlipper.setVisibility(View.INVISIBLE);
                EraseAddedBmSetting.setVisibility(View.GONE);
            }
        });

        DEB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImagesImageView.DoneErase();
                viewFlipper.setVisibility(View.INVISIBLE);
                EraseAddedBmSetting.setVisibility(View.GONE);
            }
        });

        EEB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImagesImageView.setE_R(true);
                EEB.setBackgroundColor(Color.parseColor("#A6A061"));
                ERB.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        ERB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImagesImageView.setE_R(false);
                ERB.setBackgroundColor(Color.parseColor("#A6A061"));
                EEB.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        
        
        
        EEB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (EraseAddedBmSetting.getVisibility() ==View.GONE){
                    EraseAddedBmSetting.setVisibility(View.VISIBLE);
                } else {
                    EraseAddedBmSetting.setVisibility(View.GONE);
                }
                return true;
            }
        });

        ERB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (EraseAddedBmSetting.getVisibility() ==View.GONE){
                    EraseAddedBmSetting.setVisibility(View.VISIBLE);
                } else {
                    EraseAddedBmSetting.setVisibility(View.GONE);
                }
                return true;
            }
        });



        EraseAddedBmSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                addImagesImageView.setEraseAddedBmSize(progress);
                EraseAddedBmSize.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        EraseAddedBmOpacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                addImagesImageView.setEraseAddedBmOpacity(progress);
                EraseAddedBmOpacity.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        EraseAddedBmHardnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                addImagesImageView.setEraseAddedBmHardness((float) progress);
                EraseAddedBmHardness.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    //Designed Alert Dialog For Selection the Tools
    public void ShowAdPhotoTypeDialog() {
        bottomSheetDialog.setContentView(R.layout.addphotodialog);
        @SuppressLint("CutPasteId") ImageButton Merge = bottomSheetDialog.findViewById(R.id.MergeImagesImageButton);
        @SuppressLint("CutPasteId") ImageButton Add = bottomSheetDialog.findViewById(R.id.AddImagesImageButton);

        assert Merge != null;
        Merge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImagesImageView.SetDrawType(DrawType.MERGE);
                Intent AllImages = new Intent(AddPhotoActivity.this, All_images_Activity.class);
                AllImages.putExtra("USE_TYPE", Gallery_UseType.MULTIPLE_SELECT);
                activityResultLauncher.launch(AllImages);
                bottomSheetDialog.cancel();


            }
        });

        assert Add != null;
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImagesImageView.SetDrawType(DrawType.ADD);
                Intent AllImages = new Intent(AddPhotoActivity.this, All_images_Activity.class);
                AllImages.putExtra("USE_TYPE", Gallery_UseType.MULTIPLE_SELECT);
                activityResultLauncher.launch(AllImages);
                bottomSheetDialog.cancel();


            }
        });
        bottomSheetDialog.show();
    }

    private ArrayList<ImagesTools> ImagesToolsList(){
        ArrayList<ImagesTools> mode_names = new ArrayList<>();
        mode_names.add(new ImagesTools(ImagesToolsName.OPACITY,
                getResources().getDrawable(R.drawable.opacity_ic, null)));
        mode_names.add(new ImagesTools(ImagesToolsName.MODE,
                getResources().getDrawable(R.drawable.merge_ic, null)));
        mode_names.add(new ImagesTools(ImagesToolsName.COLOR,
                getResources().getDrawable(R.drawable.color_ic, null)));
        mode_names.add(new ImagesTools(ImagesToolsName.F_R,
                getResources().getDrawable(R.drawable.simple_rotation, null)));
        //mode_names.add(new ImagesTools(ImagesToolsName.SCALE, getResources().getDrawable(R.drawable.scale, null)));
        mode_names.add(new ImagesTools(ImagesToolsName.ROTATE,
                getResources().getDrawable(R.drawable.rotate_right_ic, null)));
       // mode_names.add(new ImagesTools(ImagesToolsName.PERSPECTIVE, getResources().getDrawable(R.drawable.perspective_crop_ic, null)));
        mode_names.add(new ImagesTools(ImagesToolsName.ERASE,
                getResources().getDrawable(R.drawable.eraser_ic, null)));
        return mode_names;
    }

    public void ShowSettingType(String settingType){
        switch (settingType){
            case ImagesToolsName.OPACITY:
                addImagesImageView.setEraseMode(false);
                viewFlipper.setVisibility(View.VISIBLE);
                imageColorFilterMode.setVisibility(View.GONE);
                EraseAddedBmSetting.setVisibility(View.GONE);
                viewFlipper.setDisplayedChild(0);
                break;
            case ImagesToolsName.MODE:
                addImagesImageView.setEraseMode(false);
                imageColorFilterMode.setVisibility(View.GONE);
                EraseAddedBmSetting.setVisibility(View.GONE);
                viewFlipper.setVisibility(View.VISIBLE);
                viewFlipper.setDisplayedChild(1);
                break;
            case ImagesToolsName.COLOR:
                addImagesImageView.setEraseMode(false);
                viewFlipper.setVisibility(View.VISIBLE);
                imageColorFilterMode.setVisibility(View.VISIBLE);
                EraseAddedBmSetting.setVisibility(View.GONE);
                viewFlipper.setDisplayedChild(7);
                break;
            case ImagesToolsName.ROTATE:
                imageColorFilterMode.setVisibility(View.GONE);
                EraseAddedBmSetting.setVisibility(View.GONE);
                addImagesImageView.setEraseMode(false);
                viewFlipper.setVisibility(View.VISIBLE);
                viewFlipper.setDisplayedChild(3);
                break;
            case ImagesToolsName.SCALE:
                imageColorFilterMode.setVisibility(View.GONE);
                EraseAddedBmSetting.setVisibility(View.GONE);
                addImagesImageView.setEraseMode(false);
                viewFlipper.setVisibility(View.VISIBLE);
                viewFlipper.setDisplayedChild(4);
                break;
            case ImagesToolsName.F_R:
                imageColorFilterMode.setVisibility(View.GONE);
                EraseAddedBmSetting.setVisibility(View.GONE);
                addImagesImageView.setEraseMode(false);
                viewFlipper.setVisibility(View.VISIBLE);
                viewFlipper.setDisplayedChild(5);
                break;
            case ImagesToolsName.ERASE :
                imageColorFilterMode.setVisibility(View.GONE);
                EraseAddedBmSetting.setVisibility(View.GONE);
                addImagesImageView.setEraseMode(true);
                viewFlipper.setVisibility(View.VISIBLE);
                viewFlipper.setDisplayedChild(6);
                break;
            case ImagesToolsName.PERSPECTIVE:
                imageColorFilterMode.setVisibility(View.GONE);
                EraseAddedBmSetting.setVisibility(View.GONE);
                addImagesImageView.setEraseMode(false);
               /*
                addImagesImageView.setPerspertivefixeCase(true);
                viewFlipper.setVisibility(View.GONE);
                ToolsRecyclerView.setVisibility(View.GONE);
                PerspectiveEdit.setVisibility(View.VISIBLE);//
                */
                break;
        }
    }

    public static class ImagesToolsName{
        public final static String OPACITY = "Opacity";
        public final static String MODE = "Mode";
        public final static String COLOR = "Color";
        public static final String F_R = "F&R";
        public static final String ROTATE = "Rotate";
        public static final String SCALE = "Scale";
        public static final String PERSPECTIVE = "perspective";
        public static final String ERASE = "Erase";
    }

    public static class ImagesTools{
        String ToolsName_H;
        Drawable ToolsImg_H;

        public ImagesTools(String toolsName_H, Drawable toolsImg_H) {
            ToolsName_H = toolsName_H;
            ToolsImg_H = toolsImg_H;
        }

        public String getToolsName_H() {
            return ToolsName_H;
        }

        public Drawable getToolsImg_H() {
            return ToolsImg_H;
        }

        public void setToolsName_H(String toolsName_H) {
            ToolsName_H = toolsName_H;
        }

        public void setToolsImg_H(Drawable toolsImg_H) {
            ToolsImg_H = toolsImg_H;
        }
    }

    private ArrayList<Mode_Name> ModeNames(){
        ArrayList<Mode_Name> mode_names = new ArrayList<>();
        mode_names.add(new Mode_Name(ModeName.NORMAL));
        mode_names.add(new Mode_Name(ModeName.OVERLAY));
        mode_names.add(new Mode_Name(ModeName.SCREEN));
        mode_names.add(new Mode_Name(ModeName.MULTIPLY));
        mode_names.add(new Mode_Name(ModeName.DARKEN));
        mode_names.add(new Mode_Name(ModeName.LIGHTEN));
        mode_names.add(new Mode_Name(ModeName.ADD));

        return mode_names;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(executorService!=null){
            executorService.shutdown(); // Shutdown the executor when the activity is destroyed
        }
        
    }




    /*@Override
    public void onBackPressed() {
        SIMGN = 0;
        // Finish current activity to go back
        finish();
        // Start the same activity again
        Intent backToMain = new Intent(AddPhotoActivity.this, Editor_main_Activity.class);
        startActivity(backToMain);

        super.onBackPressed();

        if (result!=null){
            image.setBitmap(result);
            addImagesImageView.DoneAdd();
            Intent intent = Editor_main_Activity.fa.getIntent();
            finish();
            startActivity(intent);
            super.onBackPressed();
        }else{
            addImagesImageView.CancelAdd();
            Intent intent = Editor_main_Activity.fa.getIntent();
            finish();
            startActivity(intent);
            super.onBackPressed();
        }

    }*/

    public byte[] convertImageToByteArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.JPEG, 90, stream);

        return stream.toByteArray();

    }

    public void MargeAddOptions(int Visibility){
        AddOptions.setVisibility(Visibility);

    }
    public void FlipperSetVisibility(){
        viewFlipper.setVisibility(View.INVISIBLE);
    }


    public static class ImagesMode_Adapter extends RecyclerView.Adapter<ImagesMode_Adapter.MyViewHolder> {
        Context context;
        ArrayList<Mode_Name> Mode_names;
        AddImagesImageView AddImagesView;
        int selectedItemPosition = -1;

        public ImagesMode_Adapter(Context context, ArrayList<Mode_Name> mode_names, AddImagesImageView addImagesImageView) {
            this.context = context;
            Mode_names = mode_names;
            this.AddImagesView = addImagesImageView;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View View = LayoutInflater.from(context).inflate(R.layout.copymodeitem,parent,false);
            return new MyViewHolder(View);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.textView.setText(Mode_names.get(position).getModename());

            if (selectedItemPosition == position) {
                holder.textView.setTextColor(Color.parseColor("#A6A061")); // Change to your desired color
            } else {
                holder.textView.setTextColor(Color.BLACK); // Reset to default color
            }

            holder.textView.setOnClickListener(v -> {

                int previousSelectedItem = selectedItemPosition;
                selectedItemPosition = position;

                // Notify the adapter about the item change to trigger a UI update
                notifyItemChanged(previousSelectedItem);
                notifyItemChanged(selectedItemPosition);

                switch (Mode_names.get(position).getModename()) {
                    case ModeName.NORMAL -> AddImagesView.setImagesMode(null);
                    case ModeName.DARKEN -> AddImagesView.setImagesMode(PorterDuff.Mode.DARKEN);
                    case ModeName.LIGHTEN -> AddImagesView.setImagesMode(PorterDuff.Mode.LIGHTEN);
                    case ModeName.MULTIPLY -> AddImagesView.setImagesMode(PorterDuff.Mode.MULTIPLY);
                    case ModeName.OVERLAY -> AddImagesView.setImagesMode(PorterDuff.Mode.OVERLAY);
                    case ModeName.SCREEN -> AddImagesView.setImagesMode(PorterDuff.Mode.SCREEN);
                    case ModeName.ADD -> AddImagesView.setImagesMode(PorterDuff.Mode.ADD);

                }
            });
        }

        @Override
        public int getItemCount() {
            return Mode_names.size();
        }

        public  class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.CopyModeText);
            }
        }
    }


    public static class ImagesFilterMode_Adapter extends RecyclerView.Adapter<ImagesFilterMode_Adapter.MyViewHolder> {
        Context context;
        ArrayList<Mode_Name> Mode_names;
        AddImagesImageView AddImagesView;
        int selectedItemPosition = -1;

        public ImagesFilterMode_Adapter(Context context, ArrayList<Mode_Name> mode_names, AddImagesImageView addImagesImageView) {
            this.context = context;
            Mode_names = mode_names;
            this.AddImagesView = addImagesImageView;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View View = LayoutInflater.from(context).inflate(R.layout.copymodeitem,parent,false);
            return new MyViewHolder(View);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.textView.setText(Mode_names.get(position).getModename());

            if (selectedItemPosition == position) {
                holder.textView.setTextColor(Color.parseColor("#A6A061")); // Change to your desired color
            } else {
                holder.textView.setTextColor(Color.BLACK); // Reset to default color
            }

            holder.textView.setOnClickListener(v -> {

                int previousSelectedItem = selectedItemPosition;
                selectedItemPosition = position;

                // Notify the adapter about the item change to trigger a UI update
                notifyItemChanged(previousSelectedItem);
                notifyItemChanged(selectedItemPosition);

                switch (Mode_names.get(position).getModename()) {
                    case ModeName.NORMAL -> AddImagesView.setImagesColorMode(null);
                    case ModeName.DARKEN -> AddImagesView.setImagesColorMode(PorterDuff.Mode.DARKEN);
                    case ModeName.LIGHTEN -> AddImagesView.setImagesColorMode(PorterDuff.Mode.LIGHTEN);
                    case ModeName.MULTIPLY -> AddImagesView.setImagesColorMode(PorterDuff.Mode.MULTIPLY);
                    case ModeName.OVERLAY -> AddImagesView.setImagesColorMode(PorterDuff.Mode.OVERLAY);
                    case ModeName.SCREEN -> AddImagesView.setImagesColorMode(PorterDuff.Mode.SCREEN);
                    case ModeName.ADD -> AddImagesView.setImagesColorMode(PorterDuff.Mode.ADD);
                }
            });
        }

        @Override
        public int getItemCount() {
            return Mode_names.size();
        }

        public  class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.CopyModeText);
            }
        }
    }
    public class ImagesToolsAdapter extends RecyclerView.Adapter<ImagesToolsAdapter.MyViewHolder> {

        Context context;
        ArrayList<AddPhotoActivity.ImagesTools> tool_names;
        AddImagesImageView textOnImage;

        private int selectedPosition = RecyclerView.NO_POSITION;
        public ImagesToolsAdapter(Context context, ArrayList<ImagesTools> tool_names, AddImagesImageView textOnImage) {
            this.context = context;
            this.tool_names = tool_names;
            this.textOnImage = textOnImage;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.text_image_tools_item,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Glide.with(context)
                    .load(tool_names.get(position).getToolsImg_H())
                    .into(holder.imageView);
            holder.textView.setText(tool_names.get(position).getToolsName_H());
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowSettingType(tool_names.get(position).getToolsName_H());
                    // Update selected position
                    int previousSelected = selectedPosition;
                    selectedPosition = holder.getAbsoluteAdapterPosition();

                    // Notify adapter of the changes
                    notifyItemChanged(previousSelected);
                    notifyItemChanged(selectedPosition);
                }
            });

            // Set the background color based on selection
            if (selectedPosition == position) {
                holder.itemView.setBackgroundColor(Color.parseColor("#A6A061"));
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }


        @Override
        public int getItemCount() {
            return tool_names.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView_Tool_Text);
                textView = itemView.findViewById(R.id.textView_Tool_text);
            }
        }
    }
    /*
     public class CheckerboardDrawable extends Drawable {
        private Paint paintLight, paintDark;
        private int squareSize;

        public CheckerboardDrawable(int lightColor, int darkColor, int squareSize) {
            this.squareSize = squareSize;
            paintLight = new Paint();
            paintLight.setColor(lightColor);
            paintDark = new Paint();
            paintDark.setColor(darkColor);
        }

        @Override
        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            for (int y = bounds.top; y < bounds.bottom; y += squareSize) {
                for (int x = bounds.left; x < bounds.right; x += squareSize) {
                    Paint paint = ((x / squareSize + y / squareSize) % 2 == 0) ? paintLight : paintDark;
                    canvas.drawRect(x, y, x + squareSize, y + squareSize, paint);
                }
            }
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }

        // Implement other necessary methods (setAlpha, setColorFilter, getOpacity)
    }


         int lightGray = Color.rgb(200, 200, 200);
        int darkGray = Color.rgb(150, 150, 150);
        int squareSize = 10; // Size in pixels
        CheckerboardDrawable background = new CheckerboardDrawable(lightGray, darkGray, squareSize);
        addImagesImageView.setBackground(background);
     */


}
