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
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg.All_images_Activity;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg.Gallery_UseType;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.Interface.fontNameOnClick;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.ToolsName.ModeName;
import com.phantomhive.exil.hellopics.Img_Editor.Views.TextOnImage.TextOnImage;

import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ColorDetector;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.SeekbarValueSettings;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.Mode_Name;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;
import com.phantomhive.exil.hellopics.R;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class TextOnImageActivity extends AppCompatActivity implements fontNameOnClick, TextOnImage.ToDoWhenItIsOnTouchActionListener {

    private String mToolPicked;

    TextOnImage TextImageView;
    Image image;
    FloatingActionButton AddText;
    AlertDialog.Builder builder;
    RecyclerView recyclerView;
    ImageView DoneTextDrawn,CancelTextDrawn,EraseText;
    Bitmap TheDrawnBitmap;
    ArrayList<TextTools>textTools = new ArrayList<>();
    TextToolsAdapter textToolsAdapter;
    AlertDialog colorPickerDialog;

    ViewFlipper TextToolsFlipper,TextBigToolsFlipper;

    public SeekBar OpacitySeekBar,TextSizeSeekBar,TextStrokeSeekBar,TextBorderSeekBar,
            TextRotateSeekBar,TextScaleXSeekBar, TextSkewXSeekBar
            ,TextLetterSpacingSeekBar,TextLineSpacingSeekBar,TextBendSeekBar;
    RecyclerView TextMode,TextFont;
    TextMode_Adapter TextMode_adapter;
    FontAdapter fontAdapter;

    TextView TextOpacity,TextSize,TextStroke,TextBorders,
            TextRotate,TextScaleX, TextSkewX,
            TextLetterSpacing,TextLineSpacing,TextBend;

    TextView LineSpacing,LetterSpacing;
    TextView XShadowPosition,YShadowPosition;
    TextView TextCornerOpacity,TextCornerCorners;

    TextView None,Stroke,Borders,BordersColor;

    public float Shr = 6f, Shx = 10f,Shy = 10f;
    int ShC = Color.parseColor("#A6A061");

    ImageButton ShColor,ShPosition,ShRedious;
    TextView Shpyv,Shpxv;
    public SeekBar Shpysb,Shpxsb;
    TextView Shpr;
    public SeekBar Shprsb;

    public SeekBar cornerSeekb,opacitySeekb;
    public TextView cornerSeekbV,opacitySeekbV;
    ImageButton RectCorners,RectBackgroud,RectColor,RectScale;

    String BackgroundType;
    TextView Top,Bottom,Right,Left;
    public SeekBar TopSeekBar,BottomSeekBar,RightSeekBar,LeftSeekBar;
    TextView TopV,BottomV,RightV,LeftV;
    ImageButton setTextColor,detectTextColor;

    public Switch RectOnOF;

    PopupWindow popupWindowGradient;
    PopupWindow popupWindowBorders;
    PopupWindow popupWindowHighlighter;
    PopupWindow popupWindowShadow;

    String Case;
    boolean colorloc = false;

    public ArrayList<Integer> c = new ArrayList<>() ;
    RecyclerView GCRL;
    Rainbow_Adapter rainbow_adapter;
    ImageButton DoneGCS,CancelGCS;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 78) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            String bitmapPath= intent.getStringExtra("BITMAP");

                            Glide.with(TextOnImageActivity.this)
                                    .asBitmap()
                                    .load(bitmapPath)
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            TextImageView.setTextImg(resource,BackgroundType);

                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                            // Handle the case where the image load is cleared
                                            // This may occur if the target is reused or cleared
                                        }
                                    });
                        }
                    }else {
                        Intent intent = result.getData();
                        if (intent != null) {
                            Uri uri = intent.getData();
                            if (uri != null) {
                                String fileName = getFileName(TextOnImageActivity.this, uri);
                                if (fileName != null && (fileName.endsWith(".ttf") || fileName.endsWith(".otf"))) {
                                    try {
                                        File fontFile = copyFileToInternalStorage(TextOnImageActivity.this, uri, fileName);
                                        if (fontFile != null) {
                                            Typeface tf = Typeface.createFromFile(fontFile);
                                            TextImageView.setTextFont(tf);

                                            Log.d("TAG", "Font loaded successfully: " + fontFile.getPath());
                                        } else {
                                            showErrorToast(TextOnImageActivity.this);
                                        }
                                    } catch (Exception e) {
                                        Log.e("TAG", "Error loading font", e);
                                        showErrorToast(TextOnImageActivity.this);
                                    }
                                } else {
                                    showErrorToast(TextOnImageActivity.this);
                                }
                            } else {
                                showErrorToast(TextOnImageActivity.this);
                            }
                        }


                    }
                    
                }
            });
 /*
                             if (filePath!=null){
                                if (filePath.endsWith(".ttf") | filePath.endsWith(".otf")) {
                                    Typeface tf = Typeface.createFromFile(filePath);
                                    Log.d("TAG", "onActivityResult: not"+filePath);
                                    TextImageView.setTextFont(tf);
                                }else {
                                    Toast.makeText(TextOnImageActivity.this, "We only Support .ttf/.otf files\n please find the Right file", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(TextOnImageActivity.this, "We only Support .ttf/.otf files\n please find the Right file", Toast.LENGTH_SHORT).show();
                            }
                             */

    /*Intent intent = result.getData();
                       if (intent != null) {
                           String fileName = getFileName(TextOnImageActivity.this,intent.getData());

                               try {
                                   File fontFile = copyFileToInternalStorage(TextOnImageActivity.this, intent.getData(), fileName);
                                   if (fontFile != null) {
                                       Typeface tf = Typeface.createFromFile(fontFile);
                                       TextImageView.setTextFont(tf);
                                       Log.d("TAG", "Font loaded successfully: " + fontFile.getAbsolutePath());
                                   } else {
                                       Toast.makeText(TextOnImageActivity.this, "We only Support .ttf/.otf files\n please find the Right file", Toast.LENGTH_SHORT).show();
                                   }
                               } catch (Exception e) {
                                   throw new RuntimeException(e);
                               }


                           Log.d("TAG", "onActivityResult: not"+fileName+" "+intent.getData().getPath());



                       }

                        */
    private static String getFileName(TextOnImageActivity activity, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private static void showErrorToast(TextOnImageActivity activity) {
        Toast.makeText(activity, "We only support .ttf/.otf files. Please select the correct file.", Toast.LENGTH_SHORT).show();
    }
    private File copyFileToInternalStorage(TextOnImageActivity activity, Uri uri, String fileName) throws Exception {
        if (uri == null) return null; // Skip if URI is null

        File fontDir = new File(activity.getFilesDir(), "SavedFonts"); // Create a folder for fonts in the app's internal storage
        if (!fontDir.exists()) fontDir.mkdirs(); // Ensure the directory exists

        // Extract filename from URI
        try (Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) fileName = cursor.getString(nameIndex); // Update the fileName
            }
        }

        File destFile = new File(fontDir, fileName); // Create the destination file inside the "SavedFonts" folder

        // Save the font file to internal storage
        try (InputStream inputStream = activity.getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destFile; // Return the saved font file
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_on_image);
        EdgeToEdgeFixing(R.id.activity_text_on_imageL,this);

        image = Image.getInstance();
        builder = new AlertDialog.Builder(this);

        TextImageView = findViewById(R.id.TextImageView);
        AddText = findViewById(R.id.AddTextFloatingButton);

        DoneTextDrawn = findViewById(R.id.DoneAdjusting);
        CancelTextDrawn = findViewById(R.id.CancelAdjusting);
        EraseText = findViewById(R.id.EraseText);

        recyclerView = findViewById(R.id.TextOptionRecycleView);

        TextToolsFlipper = findViewById(R.id.TextToolFlipper);
        TextBigToolsFlipper = findViewById(R.id.TextBigToolFlipper);

        OpacitySeekBar = findViewById(R.id.TextOpacity);
        TextSizeSeekBar = findViewById(R.id.TextSize);
        TextStrokeSeekBar =findViewById(R.id.TextStroke);
        TextBorderSeekBar =findViewById(R.id.TextStroke_Borders);
        TextRotateSeekBar = findViewById(R.id.TextRotate);
        TextScaleXSeekBar = findViewById(R.id.TextScaleX);
        TextSkewXSeekBar = findViewById(R.id.TextSkew);
        TextLetterSpacingSeekBar = findViewById(R.id.TextLetterSpacing);
        TextLineSpacingSeekBar = findViewById(R.id.TextLineSpacing);
        TextBendSeekBar = findViewById(R.id.TextBend);

        TextMode = findViewById(R.id.TextMode);
        TextFont = findViewById(R.id.TextFont);

        TextOpacity = findViewById(R.id.TextOpacityValue);
        TextSize = findViewById(R.id.TextSizeValue);
        TextStroke = findViewById(R.id.TextStrokeValue);
        TextBorders = findViewById(R.id.TextBordersValue);
        TextRotate = findViewById(R.id.TextRotateValue);
        TextScaleX = findViewById(R.id.TextScaleXValue);
        TextSkewX = findViewById(R.id.TextSkewValue);
        TextLetterSpacing = findViewById(R.id.TextLetterSpacingValue);
        TextLineSpacing = findViewById(R.id.TextLineSpacingValue);
        TextBend = findViewById(R.id.TextBendValue);

        LetterSpacing = findViewById(R.id.LetterSpacing);
        LineSpacing = findViewById(R.id.LineSpacing);

        XShadowPosition = findViewById(R.id.XShadowPosition);
        YShadowPosition = findViewById(R.id.YShadowPosition);

        TextCornerOpacity = findViewById(R.id.TextCornerOpacity);
        TextCornerCorners = findViewById(R.id.TextCornerCorners);


        None =findViewById(R.id.TextViewNone);
        Stroke=findViewById(R.id.TextViewStroke);
        Borders=findViewById(R.id.TextViewBorders);
        BordersColor = findViewById(R.id.Borders_Color);


        GCRL = findViewById(R.id.GCOLOTextRrecyclerView);
        DoneGCS = findViewById(R.id.doneTextShosing);
        CancelGCS = findViewById(R.id.cancelTextShosing);


        ShColor = findViewById(R.id.ShadowColor);
        ShRedious = findViewById(R.id.ShadowRedous);
        ShPosition = findViewById(R.id.ShadowPosition);

        Shpxv =findViewById(R.id.TextShX);
        Shpyv =findViewById(R.id.TextShY);



        Shpxsb =findViewById(R.id.TextShadowpx);
        Shpysb =findViewById(R.id.TextShadowpy);

        Shprsb=findViewById(R.id.Textredious);
        Shpr =findViewById(R.id.TextShridiousV);


        RectCorners = findViewById(R.id.TextRectCorners);
        RectBackgroud = findViewById(R.id.TextRectbackgroud);
        RectColor = findViewById(R.id.TextRectcolor);

        RectScale = findViewById(R.id.TextRectScale);

        Top = findViewById(R.id.TopCorner);
        Bottom = findViewById(R.id.BottomCorner);
        Right = findViewById(R.id.RightCorner);
        Left = findViewById(R.id.LeftCorner);

        TopSeekBar = findViewById(R.id.TopCornerseekBar);
        BottomSeekBar = findViewById(R.id.BottomCornerseekBar);
        RightSeekBar = findViewById(R.id.RightCornerseekBar);
        LeftSeekBar = findViewById(R.id.leftCornerseekBar);

        TopV = findViewById(R.id.TopCornerseekBarTextView);
        BottomV = findViewById(R.id.BottomCornerseekBarTextView);
        RightV = findViewById(R.id.RightCornerseekBarTextView);
        LeftV = findViewById(R.id.LeftCornerseekBarTextView);


        cornerSeekb = findViewById(R.id.TextRectCornerv);
        opacitySeekb = findViewById(R.id.TextRectOpacitv);

        cornerSeekbV = findViewById(R.id.CornerRV);
        opacitySeekbV = findViewById(R.id.OpacityRV);


        RectOnOF = findViewById(R.id.RectOnOf);

        setTextColor = findViewById(R.id.SetTextColor);
        detectTextColor = findViewById(R.id.detectTextColor);


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                TextImageView.CancelDrawn();
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(TextOnImageActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });


        // Get screen dimensions
        Bitmap bitmap;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) TextOnImageActivity.this.getSystemService(Context.WINDOW_SERVICE);
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
            Toast.makeText(TextOnImageActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        } else {
            TextImageView.setImageBitmap(bitmap);
        }


        if (!modenames().isEmpty()){
            TextMode_adapter = new TextMode_Adapter(this, modenames());
            TextMode.setAdapter(TextMode_adapter);
            TextMode.setLayoutManager(new LinearLayoutManager(TextOnImageActivity.this,LinearLayoutManager.HORIZONTAL,false));
        }

        if (!Fonts().isEmpty()){
            fontAdapter = new FontAdapter(this, Fonts(),this);
            TextFont.setAdapter(fontAdapter);
            TextFont.setLayoutManager(new LinearLayoutManager(TextOnImageActivity.this,LinearLayoutManager.HORIZONTAL,false));
        }

        c.add(Color.parseColor("#e81416"));
        c.add(Color.parseColor("#ffa500"));
        c.add(null);
        c.add(null);
        c.add(null);
        c.add(null);

        rainbow_adapter = new Rainbow_Adapter(this,rainbowC());
        GCRL.setAdapter(rainbow_adapter);
        GCRL.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        TextImageView.setToDoWhenItIsOnTouchActionListener(this);

        OpacitySeekBar.setOnSeekBarChangeListener(OpacitySeekBarChangeListener);


        TextSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextImageView.setTextSize(progress);
                TextSize.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        TextScaleXSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float ScaleX = ((float) progress / 100f);
                TextImageView.setTextScaleX(ScaleX);
                TextScaleX.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        TextRotateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextImageView.setTextRotation(progress);
                TextRotate.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        TextSkewXSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float ScaleX = -((float) progress / 100f);
                TextImageView.setTextSkewX(ScaleX);
                TextSkewX.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        TextLetterSpacingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float ScaleX = ((float) progress / 100f);
                TextImageView.setLetterSpacing(ScaleX);
                TextLetterSpacing.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        TextLineSpacingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float ScaleX = ((float) progress / 100f);
                TextImageView.setLineSpacing(ScaleX);
                TextLineSpacing.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        LetterSpacing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Case = "Letter";
                ShowSeekBarType(Case,LetterSpacing,LineSpacing,
                        TextLetterSpacingSeekBar,TextLineSpacingSeekBar,
                        TextLetterSpacing,TextLineSpacing);
            }
        });

        LineSpacing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Case  = "Line";
                ShowSeekBarType(Case,LineSpacing,LetterSpacing,
                        TextLineSpacingSeekBar,TextLetterSpacingSeekBar,
                        TextLineSpacing,TextLetterSpacing);
            }
        });

        YShadowPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Case = "Y";
                ShowSeekBarType(Case,YShadowPosition,XShadowPosition,
                        Shpysb,Shpxsb,
                        Shpyv,Shpxv);
            }
        });

        XShadowPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Case  = "X";
                ShowSeekBarType(Case,XShadowPosition,YShadowPosition,
                        Shpxsb,Shpysb,
                        Shpxv,Shpyv);
            }
        });

        TextCornerOpacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Case = "opacity";
                ShowSeekBarType(Case,TextCornerOpacity,TextCornerCorners,
                        opacitySeekb,cornerSeekb,
                        opacitySeekbV,cornerSeekbV);
            }
        });

        TextCornerCorners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Case  = "corner";
                ShowSeekBarType(Case,TextCornerCorners,TextCornerOpacity,
                        cornerSeekb,opacitySeekb,
                        cornerSeekbV,opacitySeekbV);
            }
        });
        TextBendSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextImageView.setBend(-progress);
                TextBend.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        None.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                None.setTextColor(Color.parseColor("#A6A061"));
                Stroke.setTextColor(Color.BLACK);
                Borders.setTextColor(Color.BLACK);
                TextBigToolsFlipper.setVisibility(View.INVISIBLE);
                //TextImageView.setDrawTextBorder(false);
                TextImageView.setTextFill();
            }
        });

        Stroke.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Stroke.setTextColor(Color.parseColor("#A6A061"));
                None.setTextColor(Color.BLACK);
                TextBigToolsFlipper.setVisibility(View.VISIBLE);
                TextBigToolsFlipper.setDisplayedChild(0);
                Borders.setTextColor(Color.BLACK);
                TextImageView.setTextStroke(TextImageView.getTextStoke());


            }
        });

        Borders.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Borders.setTextColor(Color.parseColor("#A6A061"));
                None.setTextColor(Color.BLACK);
                Stroke.setTextColor(Color.BLACK);
                TextBigToolsFlipper.setVisibility(View.VISIBLE);
                TextBigToolsFlipper.setDisplayedChild(1);
            }
        });

        BordersColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopupWindowForBorders(TextBigToolsFlipper);

            }
        });

        TextStrokeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextImageView.setTextStroke(progress);
                TextStroke.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        TextBorderSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextImageView.setBorderTextStroke(progress);
                TextBorders.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });



        if (!TextToolsList().isEmpty()){
            textTools = TextToolsList();
            textToolsAdapter = new TextToolsAdapter(TextOnImageActivity.this,textTools,TextImageView);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(TextOnImageActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);
            recyclerView.setAdapter(textToolsAdapter);
        }

        EraseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextImageView.getEraser()){
                    TextImageView.setEraser(true);
                }else {
                    TextImageView.setEraser(false);
                }

            }
        });

        DoneTextDrawn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TheDrawnBitmap = TextImageView.getFinalImage();
               TextImageView.setImageBitmap(TheDrawnBitmap);
               TextImageView.DoneDrawn();
                TextImageView.CancelDrawn();
                image.setBitmap(TheDrawnBitmap);
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(TextOnImageActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

               //onBackPressed();
            }
        });

        CancelTextDrawn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextImageView.CancelDrawn();

                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(TextOnImageActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });



        AddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextBigToolsFlipper.setVisibility(View.INVISIBLE);
                TextToolsFlipper.setVisibility(View.INVISIBLE);
                ShowTextRitter(0,"");
            }
        });

        ShColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextBigToolsFlipper.setVisibility(View.INVISIBLE);
                showPopupWindowForShadow(ShColor);
            }
        });
        ShPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextBigToolsFlipper.setVisibility(View.VISIBLE);
                TextBigToolsFlipper.setDisplayedChild(2);
            }
        });

        ShRedious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextBigToolsFlipper.setVisibility(View.VISIBLE);
                TextBigToolsFlipper.setDisplayedChild(3);
            }
        });

        Shpxsb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

               Shx = progress;
               TextImageView.setTextShadowPosition(Shr,Shx,Shy);
               Shpxv.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Shpysb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Shy = progress;
                TextImageView.setTextShadowPosition(Shr,Shx,Shy);
                Shpyv.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        Shprsb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Shr = progress;
                TextImageView.setTextShadowPosition(Shr,Shx,Shy);
                 Shpr.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        RectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindowForHighlighter(RectColor);

            }
        });

        RectScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextBigToolsFlipper.setVisibility(View.VISIBLE);
                TextBigToolsFlipper.setDisplayedChild(5);
            }
        });

        RectBackgroud.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                BackgroundType = "RECT";
                Intent intent = new Intent(TextOnImageActivity.this, All_images_Activity.class);
                intent.putExtra("USE_TYPE", Gallery_UseType.ONE_SELECT);
                activityResultLauncher.launch(intent);
            }
        });

        RectCorners.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                TextBigToolsFlipper.setVisibility(View.VISIBLE);
                TextBigToolsFlipper.setDisplayedChild(4);
            }
        });

        RectOnOF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RectOnOF.isChecked()){
                    TextImageView.setHighlighter();
                }else {
                    TextImageView.removeHighlighter();
                }
            }
        });

        cornerSeekb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextImageView.setHighlighterCorners(progress);
                cornerSeekbV.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        setTextColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextToolsFlipper.setVisibility(View.INVISIBLE);
                if (colorloc){
                    TextImageView.setColorLoc(false, TextOnImage.colorLocType.TEXT,null);
                    colorloc = false;
                }
                colorPickerDialog = new ColorPickerDialog.Builder(TextOnImageActivity.this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(("Ok"),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        TextImageView.setTextColor(envelope.getColor());
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

        detectTextColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorloc = true;
                TextImageView.setColorLoc(true, TextOnImage.colorLocType.TEXT,null);
            }
        });

        opacitySeekb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextImageView.setHighlighterOpacity(progress);
                opacitySeekbV.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        TextImageView.ClickCircles(new TextOnImage.ClickCircles() {
            @Override
            public void ClickTopLeft(boolean b, TextOnImage textOnImage) {
                if (b){
                    textOnImage.RemoveText();
                }
            }

            @Override
            public void ClickTopRight(boolean b, TextOnImage textOnImage) {

            }

            @Override
            public void ClickBottomRight(boolean b, TextOnImage textOnImage) {

            }

            @Override
            public void ClickBottomLeft(boolean b, TextOnImage textOnImage) {
                if (b){
                    textOnImage.AddText();
                }
            }
        });




        /*
         Gardientangle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextImageView.SetGradientangle(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
         Gardiecolo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow1(Gardiecolo1);
            }
        });

        Gardiecolo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow2(Gardiecolo2);
            }
        });

         */


        DoneGCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int size = 0;
                for (Integer value : c) {
                    if (value != null) {
                        size++;
                    }
                }
                int[]cc = new int[size];

                int index = 0;
                for (Integer value : c) {
                    if (value != null) {
                        cc[index] = value;
                        index++;
                    }
                }

                TextImageView.AddGradientColors(cc);

                c.clear();
                c.add(Color.parseColor("#e81416"));
                c.add(Color.parseColor("#ffa500"));
                c.add(null);
                c.add(null);
                c.add(null);
                c.add(null);

                for (int i = 0; i < rainbowC().size(); i++) {
                    rainbow_adapter.updateItemColor(i,rainbowC().get(i));
                }

            }
        });

        CancelGCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c.clear();
                c.add(Color.parseColor("#e81416"));
                c.add(Color.parseColor("#ffa500"));
                c.add(null);
                c.add(null);
                c.add(null);
                c.add(null);
                for (int i = 0; i < rainbowC().size(); i++) {
                    rainbow_adapter.updateItemColor(i,rainbowC().get(i));
                }
            }
        });

        Top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRectSeekBarVisibility(TopSeekBar,BottomSeekBar,RightSeekBar,LeftSeekBar,
                        TopV,BottomV,RightV,LeftV);
                setRectTxtVVisibility(Top,Bottom,Right,Left);

            }
        });

        Bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRectSeekBarVisibility(BottomSeekBar,TopSeekBar,RightSeekBar,LeftSeekBar,
                        BottomV,RightV,TopV,LeftV);
                setRectTxtVVisibility(Bottom,Top,Right,Left);
            }
        });

        Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRectSeekBarVisibility(RightSeekBar,BottomSeekBar,TopSeekBar,LeftSeekBar,
                        RightV,BottomV,TopV,LeftV);
                setRectTxtVVisibility(Right,Bottom,Top,Left);
            }
        });

        Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRectSeekBarVisibility(LeftSeekBar,BottomSeekBar,TopSeekBar,RightSeekBar,
                        LeftV,BottomV,TopV,RightV);
                setRectTxtVVisibility(Left,Bottom,Top,Right);
            }
        });




        TopSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextImageView.setTextRectTop(progress);
                TopV.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        BottomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextImageView.setTextRectBottom(progress);
                BottomV.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        RightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextImageView.setTextRectRight(progress);
                RightV.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        LeftSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextImageView.setTextRectLeft(progress);
                LeftV.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void ShowSeekBarType(String Case,TextView Shosing1,TextView Shosing2,
                                 SeekBar seekBar1,SeekBar seekBar2,
                                 TextView imageButton1,TextView imageButton2) {
        if (Case.equals(this.Case)){
            Shosing1.setTextColor(Color.parseColor("#A6A061"));
            Shosing2.setTextColor(Color.BLACK);

            seekBar2.setVisibility(View.INVISIBLE);
            imageButton2.setVisibility(View.INVISIBLE);

            seekBar1.setVisibility(View.VISIBLE);
            imageButton1.setVisibility(View.VISIBLE);
        }else {
            Shosing1.setTextColor(Color.BLACK);
            Shosing2.setTextColor(Color.parseColor("#A6A061"));

            seekBar2.setVisibility(View.VISIBLE);
            imageButton2.setVisibility(View.VISIBLE);

            seekBar1.setVisibility(View.INVISIBLE);
            imageButton1.setVisibility(View.INVISIBLE);
        }
    }

    public void ShowTextRitter(int Case,String g) {
        LayoutInflater inflater = TextOnImageActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.textdialog_item, null);
        EditText editText = dialogView.findViewById(R.id.TextEditText);
        if (Case != 0){
            editText.setText(g);
        }
        builder.setMessage("please, enter the text")
                .setCancelable(true)
                .setTitle("Text")
                .setView(dialogView)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = editText.getText().toString().trim();
                        Toast.makeText(TextOnImageActivity.this, text, Toast.LENGTH_SHORT).show();
                        Rect bounds2 = new Rect();
                        Paint paint = new Paint();
                        paint.getTextBounds(text, 0, text.length(), bounds2);

                        if (Case == 0){
                            if (text.equals("")){
                                TextImageView.SetText("tap to Change");
                            }else {
                                TextImageView.SetText(text);
                            }
                        }else{
                            if (text.equals("")){
                                TextImageView.SetText("tap to Change");
                            }else {
                                TextImageView.ChangeText(text);
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create()
                .show();
    }

    public void MargeAddOptions(int Visibility){
        recyclerView.setVisibility(Visibility);

    }
    public void FlipperSetVisibility(){
        TextBigToolsFlipper.setVisibility(View.INVISIBLE);
        TextToolsFlipper.setVisibility(View.INVISIBLE);
    }
    SeekBar.OnSeekBarChangeListener OpacitySeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            TextImageView.setTextOpacity(progress);
            TextOpacity.setText(String.valueOf(progress));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    };

    public void showValueGetter(TextView textView, SeekBar seekBar){
        SeekbarValueSettings seekbarValueSettings = new SeekbarValueSettings();
        seekbarValueSettings.OnValueManuelEdit(textView,TextOnImageActivity.this,seekBar);
    }
    /*


    @Override
    public void onBackPressed() {

        super.onBackPressed();

         if (TheDrawnBitmap!=null){
            TextImageView.CancelDrawn();
            image.setBitmap(TextImageView.getFinalImage());
            Intent intent = Editor_main_Activity.fa.getIntent();
            finish();
            startActivity(intent);
            super.onBackPressed();
        }else{
            TextImageView.CancelDrawn();
            Intent intent = Editor_main_Activity.fa.getIntent();
            finish();
            startActivity(intent);
            super.onBackPressed();
        }


    }*/

    @Override
    public void fontNameOnClick(String font,String name) {
            ApplyFont(font,name);
    }

    @Override
    public void SetGradColorInLocMode(Integer position,int color) {
        c.set(position,color);
        rainbow_adapter.updateItemColor(position, color);

    }

    public static class TextToolsName{
       public final static String COLOR = "Color";
       public final static String OPACITY = "Opacity";
        public final static String MODE = "Mode";
       public final static String FONT_SIZE = "Size";
       public final static String FONT_TEXT = "Font";

       public static final String STROKE = "Stroke";
       public static final String SPACING = "Spacing";
       public static final String ROTATE = "Rotate";
       public static final String SCALE = "Scale";
       public static final String SKEW = "Skew";
       public static final String IMAGE = "Image";
       public static final String GRADIENT = "Gradient";
       public static final String BEND = "Bend";
       public static final String SHADOW = "Shadow";
       public static final String HIGHLIGHTER = "Highlighter ";
    }

    private ArrayList<TextTools> TextToolsList(){
        ArrayList<TextTools> mode_names = new ArrayList<>();
        mode_names.add(new TextTools(TextToolsName.COLOR,
                getResources().getDrawable(R.drawable.color_ic, null)));
        mode_names.add(new TextTools(TextToolsName.GRADIENT,
                getResources().getDrawable(R.drawable.gradient_ic, null)));
        mode_names.add(new TextTools(TextToolsName.OPACITY,
                getResources().getDrawable(R.drawable.opacity_ic, null)));
        mode_names.add(new TextTools(TextToolsName.MODE,
                getResources().getDrawable(R.drawable.merge_ic, null)));
        mode_names.add(new TextTools(TextToolsName.FONT_SIZE,
                getResources().getDrawable(R.drawable.font_size_ic, null)));
        mode_names.add(new TextTools(TextToolsName.FONT_TEXT,
                getResources().getDrawable(R.drawable.text_font, null)));
        mode_names.add(new TextTools(TextToolsName.STROKE,
                getResources().getDrawable(R.drawable.stroke_ic, null)));
        mode_names.add(new TextTools(TextToolsName.IMAGE,
                getResources().getDrawable(R.drawable.add_image_ic, null)));
        mode_names.add(new TextTools(TextToolsName.SPACING,
                getResources().getDrawable(R.drawable.spacing_ic, null)));
        mode_names.add(new TextTools(TextToolsName.ROTATE,
                getResources().getDrawable(R.drawable.rotate_right_ic, null)));
        mode_names.add(new TextTools(TextToolsName.SCALE,
                getResources().getDrawable(R.drawable.scale, null)));
        mode_names.add(new TextTools(TextToolsName.SKEW,
                getResources().getDrawable(R.drawable.perspective_ic, null)));
        mode_names.add(new TextTools(TextToolsName.BEND,
                getResources().getDrawable(R.drawable.bend_ic, null)));
        mode_names.add(new TextTools(TextToolsName.SHADOW,
                getResources().getDrawable(R.drawable.shadows_ic, null)));
        mode_names.add(new TextTools(TextToolsName.HIGHLIGHTER,
                getResources().getDrawable(R.drawable.marker_ic, null)));
        return mode_names;
    }

    public void ShowTextTools(String textToolsName){
        switch (textToolsName){
            case TextToolsName.COLOR:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(13);
                break;

            case TextToolsName.GRADIENT:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(9);
                /*

                    Gardiecolo1.post(new Runnable() {
                    @Override
                    public void run() {

                        Gardiecolo1.setImageBitmap(ColorDetector.getCroppedBitmap(Gardiecolo1,Color1));
                    }
                });

                Gardiecolo2.post(new Runnable() {
                    @Override
                    public void run() {
                        Gardiecolo2.setImageBitmap(ColorDetector.getCroppedBitmap(Gardiecolo2,Color2));
                    }
                });*/

                break;
            case TextToolsName.OPACITY:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(0);
                break;
            case TextToolsName.FONT_SIZE:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(1);
                break;
            case TextToolsName.FONT_TEXT:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(8);
                break;
            case TextToolsName.STROKE:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(2);
                break;
            case TextToolsName.IMAGE:
                BackgroundType = "TEXT";
                Intent intent = new Intent(TextOnImageActivity.this, All_images_Activity.class);
                intent.putExtra("USE_TYPE", Gallery_UseType.ONE_SELECT);
                activityResultLauncher.launch(intent);
                break;
            case TextToolsName.ROTATE:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(3);
                break;
            case TextToolsName.SCALE:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(4);
                break;
            case TextToolsName.SKEW:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(5);
                break;
            case TextToolsName.SPACING:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(6);
                break;
            case TextToolsName.MODE:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(7);
                break;
            case TextToolsName.BEND:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(10);
                break;
            case TextToolsName.SHADOW:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(11);
                TextImageView.setTextShadowPosition(Shr,Shx,Shy);
                TextImageView.setTextShadowColor(ShC);
                break;
            case TextToolsName.HIGHLIGHTER:
                TextToolsFlipper.setVisibility(View.VISIBLE);
                TextToolsFlipper.setDisplayedChild(12);
                TextImageView.setHighlighter();
                RectOnOF.setChecked(true);
                break;
        }
    }

    public static class TextTools{
        String ToolsName_H;
        Drawable ToolsImg_H;

        public TextTools(String toolsName_H, Drawable toolsImg_H) {
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

    private ArrayList<Mode_Name> modenames(){
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

    public class TextMode_Adapter extends RecyclerView.Adapter<TextMode_Adapter.MyViewHolder> {
        Context context;
        ArrayList<Mode_Name> Mode_names;
        int selectedItemPosition = -1;

        public TextMode_Adapter(Context context, ArrayList<Mode_Name> mode_names) {
            this.context = context;
            Mode_names = mode_names;

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

                //holder.textView.setTextColor(Color.parseColor("#A6A061"));

                switch (Mode_names.get(position).getModename()) {
                    case ModeName.NORMAL -> TextImageView.setTextMode(null);
                    case ModeName.DARKEN -> TextImageView.setTextMode(PorterDuff.Mode.DARKEN);
                    case ModeName.LIGHTEN -> TextImageView.setTextMode(PorterDuff.Mode.LIGHTEN);
                    case ModeName.MULTIPLY -> TextImageView.setTextMode(PorterDuff.Mode.MULTIPLY);
                    case ModeName.OVERLAY -> TextImageView.setTextMode(PorterDuff.Mode.OVERLAY);
                    case ModeName.SCREEN -> TextImageView.setTextMode(PorterDuff.Mode.SCREEN);
                    case ModeName.ADD -> TextImageView.setTextMode(PorterDuff.Mode.ADD);
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

    public static class FontName {
        public static String airstrike = "Airstrike";
        public static String arcade = "Arcade";
        public static String Calinastiyademo = "Calinastiya demo";
        public static String College = "College";
        public static String Edosz = "Edosz";
        public static String GhastlyPanic = "Ghastly Panic";
        public static String HeyComic = "HeyComic";
        public static String Huglove = "Hug love";
        public static String LoveLady = "Love Lady";
        public static String Minecraft = "Minecraft";
        public static String OriginTech = "OriginTech";
        public static String ZFF = "ZFont";

    }

    public static class FontPath {
        private static final String PathSource="font/";
        public static String airstrike = PathSource+"airstrike.ttf";
        public static String arcade = PathSource+"Arcade.ttf";
        public static String Calinastiyademo = PathSource+"Calinastiyademo.ttf";
        public static String College = PathSource+"college.ttf";
        public static String Edosz = PathSource+"edosz.ttf";
        public static String GhastlyPanic = PathSource+"GhastlyPanic.ttf";
        public static String HeyComic = PathSource+"HeyComic.ttf";
        public static String Huglove = PathSource+"Huglove.ttf";
        public static String LoveLady = PathSource+"LoveLady.ttf";
        public static String Minecraft = PathSource+"Minecraft.ttf";
        public static String OriginTech = PathSource+"OriginTech.ttf";
        public static String ZFF = PathSource+"zfont.ttf";
    }

    public class fonts {
        String fontname;
        String fontpath;

        public fonts(String fontname, String fontpath) {
            this.fontname = fontname;
            this.fontpath = fontpath;
        }

        public String getFontname() {
            return fontname;
        }

        public void setFontname(String fontname) {
            this.fontname = fontname;
        }

        public String getFontpath() {
            return fontpath;
        }

        public void setFontpath(String fontpath) {
            this.fontpath = fontpath;
        }
    }

    public ArrayList<fonts> Fonts(){
        ArrayList<fonts> fontsd = new ArrayList<>();
        fontsd.add(new fonts(FontName.airstrike, FontPath.airstrike));
        fontsd.add(new fonts(FontName.arcade, FontPath.arcade));
        fontsd.add(new fonts(FontName.Calinastiyademo, FontPath.Calinastiyademo));
        fontsd.add(new fonts(FontName.College, FontPath.College));
        fontsd.add(new fonts(FontName.Edosz, FontPath.Edosz));
        fontsd.add(new fonts(FontName.GhastlyPanic, FontPath.GhastlyPanic));
        fontsd.add(new fonts(FontName.HeyComic, FontPath.HeyComic));
        fontsd.add(new fonts(FontName.Huglove, FontPath.Huglove));
        fontsd.add(new fonts(FontName.LoveLady, FontPath.LoveLady));
        fontsd.add(new fonts(FontName.Minecraft, FontPath.Minecraft));
        fontsd.add(new fonts(FontName.OriginTech, FontPath.OriginTech));
        fontsd.add(new fonts(FontName.ZFF, FontPath.ZFF));
        for (int i = 0; i < getSavedFonts(TextOnImageActivity.this).size(); i++) {
            fontsd.add(new fonts(getSavedFonts(TextOnImageActivity.this).get(i).getName(),
                    getSavedFonts(TextOnImageActivity.this).get(i).getPath()));

            Log.d("TAG", "Fonts: "+getSavedFonts(TextOnImageActivity.this).get(i).getName()+"  "
                    +getSavedFonts(TextOnImageActivity.this).get(i).getPath());
        }


        return fontsd;
    }
    public class FontAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int VIEW_TYPE_FONT = 0;
        private static final int VIEW_TYPE_IMAGE = 1;

        private Context context;
        private ArrayList<fonts> fontList;
        private fontNameOnClick fontClick;
        

        public FontAdapter(Context context, ArrayList<fonts> fontList, fontNameOnClick fontClick) {
            this.context = context;
            this.fontList = fontList;
            this.fontClick = fontClick;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_FONT) {
                View view = LayoutInflater.from(context).inflate(R.layout.copymodeitem, parent, false);
                return new FontViewHolder(view);
            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.lastimageinthelist, parent, false);
                return new ImageViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if (holder instanceof FontViewHolder) {
                FontViewHolder fontHolder = (FontViewHolder) holder;
                fonts font = fontList.get(position);
                String fontName = font.getFontname();
                if (fontName.contains(".")) {
                    String fontNameWithoutExtension = fontName.substring(0, fontName.lastIndexOf('.'));
                    fontHolder.textView.setText(fontNameWithoutExtension);
                }else {
                    fontHolder.textView.setText(font.getFontname());
                }


                // Check if the font is in the internal storage or assets
                File fontFile = new File(TextOnImageActivity.this.getFilesDir(), "SavedFonts/" + font.getFontname());
                Log.d("TAG", "Font file path: " + fontFile.getAbsolutePath());
                // Try applying font from internal storage first
                if (fontFile.exists()) {
                    Typeface typeface = Typeface.createFromFile(fontFile);
                    fontHolder.textView.setTypeface(typeface);
                    Log.d("TAG", "Font applied from internal storage: " + fontFile.getPath());
                } else {
                    Typeface tf = Typeface.createFromAsset(TextOnImageActivity.this.getAssets(), font.getFontpath());
                    fontHolder.textView.setTypeface(tf);
                }


                fontHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fontClick.fontNameOnClick(font.getFontpath(), font.getFontname());
                    }
                });
            } else if (holder instanceof ImageViewHolder) {
                ImageViewHolder imageHolder = (ImageViewHolder) holder;
                imageHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        activityResultLauncher.launch(intent);
                    }
                });
                // Add any click listener or additional logic for the image here if needed
            }
        }

        @Override
        public int getItemCount() {
            return fontList.size() + 1; // +1 for the image at the end
        }
        public void updateData(ArrayList<fonts> newFontList) {
            this.fontList = newFontList;
            notifyDataSetChanged();  // Notify the adapter that the data set has changed
        }
        @Override
        public int getItemViewType(int position) {
            return (position == fontList.size()) ? VIEW_TYPE_IMAGE : VIEW_TYPE_FONT;
        }

        public class FontViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public FontViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.CopyModeText);
            }
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.lastItemImage);
            }
        }
    }


    public class TextToolsAdapter extends RecyclerView.Adapter<TextToolsAdapter.MyViewHolder> {

        Context context;
        ArrayList<TextTools> tool_names;
        TextOnImage textOnImage;

        private int selectedPosition = RecyclerView.NO_POSITION;
        public TextToolsAdapter(Context context, ArrayList<TextOnImageActivity.TextTools> tool_names,
                                TextOnImage textOnImage) {
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
                    TextBigToolsFlipper.setVisibility(View.INVISIBLE);
                    ShowTextTools(tool_names.get(position).getToolsName_H());
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

    public void ApplyFont(String fontName, String name){

        // Check if the font is in the internal storage or assets
        File fontFile = new File(this.getFilesDir(), "SavedFonts/" + name);
        Log.d("TAG", "Font file path: " + fontFile.getAbsolutePath());
        // Try applying font from internal storage first
        if (fontFile.exists()) {
            Typeface typeface = Typeface.createFromFile(fontFile);
            TextImageView.setTextFont(typeface);
            Log.d("TAG", "Font applied from internal storage: " + fontFile.getPath());
        } else {
            Typeface tf = Typeface.createFromAsset(this.getAssets(), fontName);
            TextImageView.setTextFont(tf);
        }


    }

    private ArrayList<String> scanFilesWithExtensions() {
        ArrayList<String> filePathList = new ArrayList<>();
        File rootDirectory = Environment.getExternalStorageDirectory();
        getFilesWithExtension(rootDirectory, ".fft", filePathList);
        getFilesWithExtension(rootDirectory, ".otf", filePathList);
        return filePathList;
    }

    private void getFilesWithExtension(File directory, String extension, ArrayList<String> filePathList) {
        File[] fileList = directory.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isDirectory()) {
                    getFilesWithExtension(file, extension, filePathList);
                } else {
                    String filePath = file.getAbsolutePath();
                    if (filePath.endsWith(extension)) {
                        filePathList.add(filePath);
                    }
                }
            }
        }
    }

    private void setRectSeekBarVisibility(SeekBar V,SeekBar InVa,SeekBar InVb,SeekBar InVd,TextView TV,TextView TInVa,TextView TInVb,TextView TInVd){
        V.setVisibility(View.VISIBLE);
        InVa.setVisibility(View.INVISIBLE);
        InVb.setVisibility(View.INVISIBLE);
        InVd.setVisibility(View.INVISIBLE);

        TV.setVisibility(View.VISIBLE);
        TInVa.setVisibility(View.INVISIBLE);
        TInVb.setVisibility(View.INVISIBLE);
        TInVd.setVisibility(View.INVISIBLE);
    }


    private void setRectTxtVVisibility(TextView V,TextView InVa,TextView InVb,TextView InVd){
        V.setTextColor(Color.parseColor("#A6A061"));
/*
 V.setVisibility(View.VISIBLE);
      InVa.setVisibility(View.INVISIBLE);
      InVb.setVisibility(View.INVISIBLE);
      InVd.setVisibility(View.INVISIBLE);

 */



        InVa.setTextColor(Color.BLACK);
        InVb.setTextColor(Color.BLACK);
        InVd.setTextColor(Color.BLACK);
    }

    private void showPopupWindowForGradient(View anchorView, ImageButton imageButton, int Position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        // Find buttons in the custom layout
        ImageButton button1 = popupView.findViewById(R.id.button1);
        ImageButton button2 = popupView.findViewById(R.id.button2);

        // Set click listeners for the buttons in the custom layout
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button1 click
                if (colorloc){
                    TextImageView.setColorLoc(false, TextOnImage.colorLocType.GRAD,Position);
                    colorloc = false;
                }

                colorPickerDialog = new ColorPickerDialog.Builder(TextOnImageActivity.this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(("Ok"),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {

                                        c.set(Position,envelope.getColor());
                                        rainbow_adapter.updateItemColor(Position, envelope.getColor());
                                        /*
                                        imageButton.setImageBitmap(ColorDetector.getCroppedBitmap(imageButton, envelope.getColor()));
                                        c.set(Position,envelope.getColor());
                                         */

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
                popupWindowGradient.dismiss();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button2 click
                colorloc = true;
                TextImageView.setColorLoc(true, TextOnImage.colorLocType.GRAD,Position);
                popupWindowGradient.dismiss();
            }
        });

        popupWindowGradient = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Set the location of the popup relative to the anchor view
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int anchorX = location[0];
        int anchorY = location[1];

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindowGradient.showAtLocation(
                anchorView,
                Gravity.NO_GRAVITY,
                anchorX- (popupView.getMeasuredWidth() / 2) + (anchorView.getWidth() / 2),
                anchorY-popupView.getMeasuredHeight()
        );

        Log.d("TAG", "showPopupWindow2: "+anchorView.getWidth());
        // Dismiss the popup when clicking outside
        popupWindowGradient.setOutsideTouchable(true);
        popupWindowGradient.setFocusable(true);
        popupWindowGradient.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

    private void showPopupWindowForBorders(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        // Find buttons in the custom layout
        ImageButton button1 = popupView.findViewById(R.id.button1);
        ImageButton button2 = popupView.findViewById(R.id.button2);

        // Set click listeners for the buttons in the custom layout
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (colorloc){
                    TextImageView.setColorLoc(false, TextOnImage.colorLocType.BORD,null);
                    colorloc = false;
                }

                colorPickerDialog = new ColorPickerDialog.Builder(TextOnImageActivity.this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(("Ok"),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        TextImageView.setBorderTextColor(envelope.getColor());
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
                popupWindowBorders.dismiss();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button2 click
                colorloc = true;
                TextImageView.setColorLoc(true, TextOnImage.colorLocType.BORD,null);
                popupWindowBorders.dismiss();
            }
        });

        popupWindowBorders = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Set the location of the popup relative to the anchor view
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int anchorX = location[0];
        int anchorY = location[1];

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindowBorders.showAtLocation(
                anchorView,
                Gravity.NO_GRAVITY,
                anchorX- (popupView.getMeasuredWidth() / 2) + (anchorView.getWidth() / 2),
                anchorY-popupView.getMeasuredHeight()
        );

        Log.d("TAG", "showPopupWindow2: "+anchorView.getWidth());
        // Dismiss the popup when clicking outside
        popupWindowBorders.setOutsideTouchable(true);
        popupWindowBorders.setFocusable(true);
        popupWindowBorders.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }
    private void showPopupWindowForHighlighter(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        // Find buttons in the custom layout
        ImageButton button1 = popupView.findViewById(R.id.button1);
        ImageButton button2 = popupView.findViewById(R.id.button2);

        // Set click listeners for the buttons in the custom layout
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (colorloc){
                    TextImageView.setColorLoc(false, TextOnImage.colorLocType.HIG,null);
                    colorloc = false;
                }

                colorPickerDialog = new ColorPickerDialog.Builder(TextOnImageActivity.this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(("Ok"),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        TextImageView.setHighlighterColor(envelope.getColor());
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
                popupWindowHighlighter.dismiss();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button2 click
                colorloc = true;
                TextImageView.setColorLoc(true, TextOnImage.colorLocType.HIG,null);
                popupWindowHighlighter.dismiss();
            }
        });

        popupWindowHighlighter = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Set the location of the popup relative to the anchor view
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int anchorX = location[0];
        int anchorY = location[1];

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindowHighlighter.showAtLocation(
                anchorView,
                Gravity.NO_GRAVITY,
                anchorX - (popupView.getMeasuredWidth() / 2) + (anchorView.getWidth() / 2),
                anchorY - popupView.getMeasuredHeight()
        );

        Log.d("TAG", "showPopupWindow2: " + anchorView.getWidth());
        // Dismiss the popup when clicking outside
        popupWindowHighlighter.setOutsideTouchable(true);
        popupWindowHighlighter.setFocusable(true);
        popupWindowHighlighter.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    private void showPopupWindowForShadow(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        // Find buttons in the custom layout
        ImageButton button1 = popupView.findViewById(R.id.button1);
        ImageButton button2 = popupView.findViewById(R.id.button2);

        // Set click listeners for the buttons in the custom layout
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (colorloc){
                    TextImageView.setColorLoc(false, TextOnImage.colorLocType.SHAD,null);
                    colorloc = false;
                }

                colorPickerDialog = new ColorPickerDialog.Builder(TextOnImageActivity.this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(("Ok"),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        ShC = envelope.getColor();
                                        TextImageView.setTextShadowColor(ShC);
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
                popupWindowShadow.dismiss();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button2 click
                colorloc = true;
                TextImageView.setColorLoc(true, TextOnImage.colorLocType.SHAD,null);
                popupWindowShadow.dismiss();
            }
        });

        popupWindowShadow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Set the location of the popup relative to the anchor view
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int anchorX = location[0];
        int anchorY = location[1];

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindowShadow.showAtLocation(
                anchorView,
                Gravity.NO_GRAVITY,
                anchorX - (popupView.getMeasuredWidth() / 2) + (anchorView.getWidth() / 2),
                anchorY - popupView.getMeasuredHeight()
        );

        Log.d("TAG", "showPopupWindow2: " + anchorView.getWidth());
        // Dismiss the popup when clicking outside
        popupWindowShadow.setOutsideTouchable(true);
        popupWindowShadow.setFocusable(true);
        popupWindowShadow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (popupWindowGradient != null && popupWindowGradient.isShowing()) {
            Rect popupRect = new Rect();
            popupWindowGradient.getContentView().getGlobalVisibleRect(popupRect);

            if (!popupRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                popupWindowGradient.dismiss();
            }
        }
        if (popupWindowBorders != null && popupWindowBorders.isShowing()) {
            Rect popupRect = new Rect();
            popupWindowBorders.getContentView().getGlobalVisibleRect(popupRect);

            if (!popupRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                popupWindowBorders.dismiss();
            }
        }
        if (popupWindowHighlighter != null && popupWindowHighlighter.isShowing()) {
            Rect popupRect = new Rect();
            popupWindowHighlighter.getContentView().getGlobalVisibleRect(popupRect);

            if (!popupRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                popupWindowHighlighter.dismiss();
            }
        }
        if (popupWindowShadow != null && popupWindowShadow.isShowing()) {
            Rect popupRect = new Rect();
            popupWindowShadow.getContentView().getGlobalVisibleRect(popupRect);

            if (!popupRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                popupWindowShadow.dismiss();
            }
        }
        return super.dispatchTouchEvent(event);
    }
    public class Rainbow_Adapter extends RecyclerView.Adapter<Rainbow_Adapter.MyViewHolder> {

        Context context;
        ArrayList<Integer> rainbow;

        public Rainbow_Adapter(Context context, ArrayList<Integer> rainbow) {
            this.context = context;
            this.rainbow = rainbow;

        }

        public void updateItemColor(int position, int color) {
            if (position >= 0 && position < rainbow.size()) {
                rainbow.set(position,color);
                notifyItemChanged(position);
            }
        }
        @NonNull
        @Override
        public Rainbow_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View View = LayoutInflater.from(context).inflate(R.layout.gradientcolorlist_item,parent,false);
            return new Rainbow_Adapter.MyViewHolder(View);
        }

        @Override
        public void onBindViewHolder(@NonNull Rainbow_Adapter.MyViewHolder holder, int position) {
            holder.imageButton.post(new Runnable() {
                @Override
                public void run() {
                    holder.imageButton.setImageBitmap(ColorDetector.getCroppedBitmap(holder.imageButton,rainbow.get(position)));
                }
            });


            holder.imageButton.setOnClickListener(v -> {
                if (popupWindowGradient !=null){
                    popupWindowGradient.dismiss();

                }
                showPopupWindowForGradient(holder.imageButton,holder.imageButton,position);
            });
        }

        @Override
        public int getItemCount() {
            return rainbow.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageButton imageButton;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageButton = itemView.findViewById(R.id.GCOLOR);
            }
        }
    }
    private ArrayList<Integer> rainbowC(){
        ArrayList<Integer> mode_names = new ArrayList<>();
        mode_names.add(Color.parseColor("#e81416"));
        mode_names.add(Color.parseColor("#ffa500"));
        mode_names.add(Color.WHITE);
        mode_names.add(Color.WHITE);
        mode_names.add(Color.WHITE);
        mode_names.add(Color.WHITE);
        return mode_names;
    }

    public void saveFont(String fontPath, Context context) {
        File srcFile = new File(fontPath);
        if (!srcFile.exists()) return; // Skip if file doesn't exist

        File fontDir = new File(context.getFilesDir(), "SavedFonts"); // Private folder
        if (!fontDir.exists()) fontDir.mkdirs(); // Create if not exists

        File destFile = new File(fontDir, srcFile.getName());

        try {
            Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            // Optionally delete the original file
            // srcFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<File> getSavedFonts(Context context) {
        File fontDir = new File(context.getFilesDir(), "SavedFonts"); // Path where fonts are saved
        if (!fontDir.exists() || !fontDir.isDirectory()) {
            return new ArrayList<>(); // Return an empty list if no fonts are saved
        }

        File[] files = fontDir.listFiles((dir, name) -> name.endsWith(".ttf") || name.endsWith(".otf"));

        return files != null ? new ArrayList<>(Arrays.asList(files)) : new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get the updated list of fonts
        ArrayList<fonts> updatedFontList = Fonts();
        // Check if the font list has changed
        // If adapter is already initialized, just update the data
        fontAdapter.updateData(updatedFontList); // Add this method in FontAdapter

    }
}
