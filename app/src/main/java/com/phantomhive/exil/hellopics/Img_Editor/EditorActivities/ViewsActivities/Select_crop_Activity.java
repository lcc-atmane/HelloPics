package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities;

import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.Editor_Adapters.CopyMode_Adapter;
import com.phantomhive.exil.hellopics.Img_Editor.ScaleImageView.ImageViewScaler.Scaler;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.ToolsName.ModeName;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.CropCutShape;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.SelectImageView;

import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.Mode_Name;
import com.phantomhive.exil.hellopics.R;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class Select_crop_Activity extends AppCompatActivity {
    SelectImageView selectImageView;

    Image image;
    Scaler scaler;

    ConstraintLayout constraintLayoutScale;
    ConstraintLayout constraintLayoutOption;
    ConstraintLayout constraintLayoutCopySetting;
    ConstraintLayout MainConstraintLayout, TopConstraintLayout, PastConstraintLayout;
    ConstraintLayout SettingOpacityConstraint;

    ImageButton DoneEditSelect,BackToHome;
    ImageButton CropSelection, CutSelection, CloseConstOption, CopySelection, PastSelection;
    ImageButton RedoRight,ModeGlobal,RedoLeft;
    ImageButton DonePast,CancelPast;
    ImageButton Oval,Rect,Free,Brush;

    Bitmap SelectedCroppedBitmap;
    Bitmap SelectedEditedBitmap;
    Bitmap TheCenterBitmap;

    public SeekBar OpacitySeekBar,RotateSeekBar;
    TextView CopyOpacityValue;
    TextView RotateCopyValue;

    ViewFlipper flipper;
    RecyclerView ModeList;
    RecyclerView imageCopyColorFilterMode;
    CopyMode_Adapter copyMode_adapter;
    ImagesFilterMode_Adapter imagesFilterMode_Adapter;
    ArrayList<Mode_Name> mode_names = new ArrayList<>();

    AlertDialog colorPickerDialog ;

    ImageView SetColor,DetectColor;
    ImageView copy_Rotate_left, copy_Rotate_Right;
    ImageView copy_Flip_Horizontal,copy_Flip_Vertical;
    ImageButton EraseBrush,DoneErase,CancelErase,RestoreBrush;
    ConstraintLayout EraseCopiedBmSetting;
    public SeekBar EraseCopiedBmOpacitySeekBar,EraseCopiedBmSizeSeekBar,EraseCopiedBmHardnessSeekBar;
    TextView EraseCopiedBmOpacity,EraseCopiedBmHardness,EraseCopiedBmSize;

    pastSettingsAdapter pastSettingsAdapter;
     RecyclerView recyclerView;
     PopupWindow popupWindow;
     int color = Color.TRANSPARENT;
     boolean colorloc= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_crop_);
        EdgeToEdgeFixing(R.id.activity_select_crop_L,this);

        ModeList = findViewById(R.id.ModeList);
        imageCopyColorFilterMode = findViewById(R.id.imageCopyColorFilterMode);
        selectImageView = findViewById(R.id.SelectImageView);
        constraintLayoutScale = findViewById(R.id.SelectImageViewscaler);
        constraintLayoutOption = findViewById(R.id.OptionCstl);
        constraintLayoutCopySetting = findViewById(R.id.CopySettingconstraintLayout);
        MainConstraintLayout = findViewById(R.id.CheckForDoneOrNoConstraintLayout);
        TopConstraintLayout = findViewById(R.id.topconstraintLayout);
        PastConstraintLayout = findViewById(R.id.PastconstraintLayout);

        DonePast = findViewById(R.id.DonepastImgBtn);
        CancelPast = findViewById(R.id.CancelpastImgBtn);

        DoneEditSelect = findViewById(R.id.DoneSelectCropping);
        BackToHome = findViewById(R.id.Back_to_HomefromSelectCrop);

        CropSelection = findViewById(R.id.CropSeliction);
        CutSelection = findViewById(R.id.CutSeliction);
        CloseConstOption = findViewById(R.id.InvisibleConst);
        CopySelection = findViewById(R.id.CopySelction);
        PastSelection = findViewById(R.id.pastSelection);

        RedoRight = findViewById(R.id.imageButtonRedoRight);
        ModeGlobal = findViewById(R.id.ModeGlobal);
        RedoLeft = findViewById(R.id.imageButtonRedoLeft);

        Rect = findViewById(R.id.imageButtonRect);
        Oval = findViewById(R.id.imageButtonOval);
        Free = findViewById(R.id.imageButtonFreeSelect);
        Brush = findViewById(R.id.imageButtonBrushSelect);

        flipper = findViewById(R.id.fliper);
        SettingOpacityConstraint = findViewById(R.id.OpacitySettingOfCopy);


        OpacitySeekBar = findViewById(R.id.OpacityOfCopyValueseekBar);
        RotateSeekBar = findViewById(R.id.RotateCopy);


        recyclerView = findViewById(R.id.PastSetting);

        CopyOpacityValue = findViewById(R.id.textViewOpacityCopy);
        RotateCopyValue = findViewById(R.id.RotateCopyValue);

        copy_Rotate_left = findViewById(R.id.copy_Rotate_left);
        copy_Rotate_Right = findViewById(R.id.copy_Rotate_Right);
        copy_Flip_Horizontal = findViewById(R.id.copy_filp_Horizontal);
        copy_Flip_Vertical = findViewById(R.id.copy_filp_Vertical);

        SetColor = findViewById(R.id.SetColor);
        DetectColor = findViewById(R.id.detectColor);

        EraseBrush = findViewById(R.id.EraseBrush);
        EraseBrush.setBackgroundColor(Color.parseColor("#A6A061"));
        RestoreBrush = findViewById(R.id.RestoreBrush);
        DoneErase = findViewById(R.id.DoneErase);
        CancelErase = findViewById(R.id.CancelErase);


        EraseCopiedBmSetting  = findViewById(R.id.EraseCopiedBmSetting);

        EraseCopiedBmOpacitySeekBar = findViewById(R.id.EraseCopiedBmOpacity);
        EraseCopiedBmSizeSeekBar = findViewById(R.id.EraseCopiedBmSize);
        EraseCopiedBmHardnessSeekBar =findViewById(R.id.EraseCopiedBmHardness);

        EraseCopiedBmOpacity = findViewById(R.id.EraseCopiedBmOpacityValue);
        EraseCopiedBmSize = findViewById(R.id.EraseCopiedBmSizeValue);
        EraseCopiedBmHardness = findViewById(R.id.EraseCopiedBmHardnessValue);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (selectImageView.isInPaste()) {
                    selectImageView.CancelPast();
                    constraintLayoutCopySetting.setVisibility(View.INVISIBLE);
                    imageCopyColorFilterMode.setVisibility(View.GONE);
                    EraseCopiedBmSetting.setVisibility(View.GONE);
                    flipper.setVisibility(View.INVISIBLE);
                } else {
                    // Finish current activity to go back
                    finish();
                    // Start the same activity again
                    Intent backToMain = new Intent(Select_crop_Activity.this, Editor_main_Activity.class);
                    startActivity(backToMain);
                }

            }
        });

        PastSelection.setVisibility(View.GONE);
        Rect.setBackgroundColor(Color.parseColor("#A6A061"));


        if (!pastSettings().isEmpty()){
            pastSettingsAdapter = new pastSettingsAdapter(Select_crop_Activity.this,pastSettings());
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(Select_crop_Activity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);
            recyclerView.setAdapter(pastSettingsAdapter);
        }



        image = Image.getInstance();
        scaler = new Scaler();
        mode_names = modenames();
        if (mode_names.isEmpty()){
            Log.d("Tools_Home_ArrayList","ArrayList is empty");
        }else {
            copyMode_adapter = new CopyMode_Adapter(this,mode_names,selectImageView);
            ModeList.setAdapter(copyMode_adapter);
            ModeList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        }

        imagesFilterMode_Adapter = new ImagesFilterMode_Adapter(this,modenames(),selectImageView);
        imageCopyColorFilterMode.setAdapter(imagesFilterMode_Adapter);
        imageCopyColorFilterMode.setLayoutManager(new LinearLayoutManager(
                this,LinearLayoutManager.HORIZONTAL,false));

        OpacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectImageView.setOpacity(progress);
                CopyOpacityValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        RotateSeekBar.setOnSeekBarChangeListener(RotateSeekBarChangeListener);


      selectImageView.setImageBitmap(image.getBitmap());
      //scaler.ImageViewScaler(Select_crop_Activity.this,selectImageView, constraintLayoutScale,image.getBitmap());



        /*
        chose the Rect
         */
        Rect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageView.RectOrOval(CropCutShape.RECTANGLE);
                Rect.setBackgroundColor(Color.parseColor("#A6A061"));
                Oval.setBackgroundColor(Color.TRANSPARENT);
                Free.setBackgroundColor(Color.TRANSPARENT);
                Brush.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        /*
        chose the Oval
         */
        Oval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageView.RectOrOval(CropCutShape.OVAL);
                Oval.setBackgroundColor(Color.parseColor("#A6A061"));
                Rect.setBackgroundColor(Color.TRANSPARENT);
                Free.setBackgroundColor(Color.TRANSPARENT);
                Brush.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        /*
        chose The Free
         */
        Free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageView.RectOrOval(CropCutShape.FREE);
                Free.setBackgroundColor(Color.parseColor("#A6A061"));
                Rect.setBackgroundColor(Color.TRANSPARENT);
                Oval.setBackgroundColor(Color.TRANSPARENT);
                Brush.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        /*
        chose the Rect
         */
        Brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageView.RectOrOval(CropCutShape.BRUSH);
                Brush.setBackgroundColor(Color.parseColor("#A6A061"));
                Oval.setBackgroundColor(Color.TRANSPARENT);
                Free.setBackgroundColor(Color.TRANSPARENT);
                Rect.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        // in other Version Soon
        RedoRight.setOnClickListener(v -> Toast.makeText(Select_crop_Activity.this, " in other Version Soon", Toast.LENGTH_SHORT).show());

        ModeGlobal.setOnClickListener(v -> {
            if (!selectImageView.isModeGlobal()){
                ModeGlobal.setImageResource(R.drawable.add_ic);
                selectImageView.setModeGlobal(true);
            }else {
                ModeGlobal.setImageResource(R.drawable.loop_ic);
                selectImageView.setModeGlobal(false);
                selectImageView.Clear();
            }

        });
        // in other Version Soon
        RedoLeft.setOnClickListener(v -> Toast.makeText(Select_crop_Activity.this, " in other Version Soon", Toast.LENGTH_SHORT).show());

        selectImageView.ClickCircles(new SelectImageView.ClickCircles() {
            @Override
            public void ClickTopLeft(boolean b, SelectImageView selectImageView) {
                if (b){
                    selectImageView.RemoveCopy();
                }
            }

            @Override
            public void ClickTopRight(boolean b, SelectImageView selectImageView) {

            }

            @Override
            public void ClickBottomRight(boolean b, SelectImageView selectImageView) {

            }

            @Override
            public void ClickBottomLeft(boolean b, SelectImageView selectImageView) {
                if (b){
                    selectImageView.AddCopy();
                }
            }
        });
        // Crop the Selection Area.
        CropSelection.setOnClickListener(v -> {
            if (selectImageView.AreaSelected()) {
                SelectedCroppedBitmap = selectImageView.getCroppedImage();

                selectImageView.CancelPast();
                image.setBitmap(SelectedCroppedBitmap);

                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(Select_crop_Activity.this, Editor_main_Activity.class);
                startActivity(backToMain);


                // Select_crop_Activity.this.onBackPressed();
            } else {
                Toast.makeText(this, "Please select area", Toast.LENGTH_SHORT).show();
            }
        });

        // Cut the Selection Area.
        CutSelection.setOnClickListener(v -> {
            if (selectImageView.AreaSelected()) {
                selectImageView.clear();
                selectImageView.Copy();
                SelectedEditedBitmap = selectImageView.getCutImage();
                selectImageView.setImageBitmap(SelectedEditedBitmap);
                selectImageView.resetCut();
                PastSelection.setVisibility(View.VISIBLE);
            }else {
                Toast.makeText(this, "Please select area", Toast.LENGTH_SHORT).show();
            }

            //scaler.ImageViewScaler(Select_crop_Activity.this,selectImageView, constraintLayoutScale,SelectedEditedBitmap);


        });

        // Effect in the Selection Area.
        CopySelection.setOnClickListener(v ->{
            if (selectImageView.AreaSelected()) {

            selectImageView.clear();
            selectImageView.Copy();
            PastSelection.setVisibility(View.VISIBLE);
            }else {
                Toast.makeText(this, "Please select area", Toast.LENGTH_SHORT).show();
            }
        });

       //Case user Done From Select The Past Area.
        DonePast.setOnClickListener(v -> {
            SelectedEditedBitmap = selectImageView.getFinalAfterPastImage();
            selectImageView.DonePast();
            selectImageView.setImageBitmap(SelectedEditedBitmap);
            constraintLayoutCopySetting.setVisibility(View.INVISIBLE);
            imageCopyColorFilterMode.setVisibility(View.GONE);
            EraseCopiedBmSetting.setVisibility(View.GONE);
            flipper.setVisibility(View.INVISIBLE);
        });

        //Case user Cancel the past Process.
        CancelPast.setOnClickListener(v -> {
            selectImageView.CancelPast();
            constraintLayoutCopySetting.setVisibility(View.INVISIBLE);
            imageCopyColorFilterMode.setVisibility(View.GONE);
            EraseCopiedBmSetting.setVisibility(View.GONE);
            flipper.setVisibility(View.INVISIBLE);
        });

        // Effect in the Selection Area.
        PastSelection.setOnClickListener(v -> {
            if (selectImageView.isCopied()){
                selectImageView.Past();
                constraintLayoutCopySetting.setVisibility(View.VISIBLE);
            }

        });

        SetColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (colorloc){
                    selectImageView.setColorLoc(false);
                    colorloc = false;
                }
                colorPickerDialog = new ColorPickerDialog.Builder(Select_crop_Activity.this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(("Ok"),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        color = envelope.getColor();
                                        selectImageView.setCopyBackgroundColor(color);
                                        /*
                                         Bitmap bitmap = Bitmap.createBitmap(drawLoc.getWidth(),
                                                drawLoc.getHeight(),
                                                Bitmap.Config.ARGB_8888);
                                        Canvas canvas = new Canvas(bitmap);
                                        canvas.drawColor(color);
                                        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
                                        p.setColor(Color.BLACK);
                                        p.setStyle(Paint.Style.STROKE);
                                        p.setStrokeWidth(10f);
                                        canvas.drawPoint(drawLoc.getWidth()/2f,
                                                drawLoc.getHeight()/2f,p);
                                        drawLoc.setImageBitmap(bitmap);
                                        mDrawView.setDrawColor(color);
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
                        .setBottomSpace(12) // set a bottom space between the last slide bar and buttons.
                        .show();
            }
        });

        DetectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorloc = false;
                selectImageView.setColorLoc(true);
            }
        });

        copy_Flip_Horizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageView.flipH();
            }
        });

        copy_Flip_Vertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageView.flipV();
            }
        });

        copy_Rotate_Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageView.RotateR();
            }
        });

        copy_Rotate_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageView.RotateL();
            }
        });


        DoneErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageView.DoneErase();
                flipper.setVisibility(View.INVISIBLE);
                EraseCopiedBmSetting.setVisibility(View.GONE);
            }
        });

        CancelErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageView.CancelErase();
                flipper.setVisibility(View.INVISIBLE);
                EraseCopiedBmSetting.setVisibility(View.GONE);
            }
        });

        EraseBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageView.setE_R(true);
                EraseBrush.setBackgroundColor(Color.parseColor("#A6A061"));
                RestoreBrush.setBackgroundColor(Color.TRANSPARENT);
            }
        });


        RestoreBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageView.setE_R(false);
                RestoreBrush.setBackgroundColor(Color.parseColor("#A6A061"));
                EraseBrush.setBackgroundColor(Color.TRANSPARENT);
            }
        });


        EraseBrush.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (EraseCopiedBmSetting.getVisibility() ==View.GONE){
                    EraseCopiedBmSetting.setVisibility(View.VISIBLE);
                } else {
                    EraseCopiedBmSetting.setVisibility(View.GONE);
                }
                return true;
            }
        });

        RestoreBrush.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (EraseCopiedBmSetting.getVisibility() ==View.GONE){
                    EraseCopiedBmSetting.setVisibility(View.VISIBLE);
                } else {
                    EraseCopiedBmSetting.setVisibility(View.GONE);
                }
                return true;
            }
        });
        
        EraseCopiedBmSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectImageView.setEraseCopiedBmSize(progress);
                EraseCopiedBmSize.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        EraseCopiedBmOpacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectImageView.setEraseCopiedBmOpacity(progress);
                EraseCopiedBmOpacity.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        EraseCopiedBmHardnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectImageView.setEraseCopiedBmHardness((float) progress);
                EraseCopiedBmHardness.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        

        //set invisible to close constoption
        CloseConstOption.setOnClickListener(v -> ShowOptions(false));

        // Win the user done from editing in the selected Area.
        DoneEditSelect.setOnClickListener(v -> {
            if (SelectedEditedBitmap!=null){
                TheCenterBitmap = SelectedEditedBitmap;

                selectImageView.CancelPast();

                image.setBitmap(SelectedEditedBitmap);

            }
            // Finish current activity to go back
            finish();
            // Start the same activity again
            Intent backToMain = new Intent(Select_crop_Activity.this, Editor_main_Activity.class);
            startActivity(backToMain);

           // onBackPressed();
        });

        // Back To home if nothing Happen.
        BackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImageView.CancelPast();
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(Select_crop_Activity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                // onBackPressed()
            }
        });

    }

    SeekBar.OnSeekBarChangeListener RotateSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            selectImageView.setRotateDegree(progress);
            RotateCopyValue.setText(String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };




    public void ShowOptions(boolean b){
        if (b){
            constraintLayoutOption.setVisibility(View.VISIBLE);
        }else{
            constraintLayoutOption.setVisibility(View.INVISIBLE);
        }
    }

    public void HideToPosition(boolean b){

        if (b){
            constraintLayoutOption.setVisibility(View.INVISIBLE);
            MainConstraintLayout.setVisibility(View.INVISIBLE);
            TopConstraintLayout.setVisibility(View.INVISIBLE);
            PastConstraintLayout.setVisibility(View.VISIBLE);

        }else{
            constraintLayoutOption.setVisibility(View.INVISIBLE);
            MainConstraintLayout.setVisibility(View.VISIBLE);
            TopConstraintLayout.setVisibility(View.VISIBLE);
            PastConstraintLayout.setVisibility(View.INVISIBLE);

        }
    }
    public void ShowSettingType(String s){
        switch (s) {
            case SettingType.OPACITY -> {
                selectImageView.setEraseMode(false);
                flipper.setVisibility(View.VISIBLE);
                imageCopyColorFilterMode.setVisibility(View.GONE);
                EraseCopiedBmSetting.setVisibility(View.GONE);
                flipper.setDisplayedChild(0);
            }
            case SettingType.MODE -> {
                selectImageView.setEraseMode(false);
                flipper.setVisibility(View.VISIBLE);
                imageCopyColorFilterMode.setVisibility(View.GONE);
                EraseCopiedBmSetting.setVisibility(View.GONE);
                flipper.setDisplayedChild(1);
            }
            case SettingType.BACKGROUND -> {
                selectImageView.setEraseMode(false);
                flipper.setVisibility(View.VISIBLE);
                imageCopyColorFilterMode.setVisibility(View.VISIBLE);
                EraseCopiedBmSetting.setVisibility(View.GONE);
                flipper.setDisplayedChild(6);
            }
            case SettingType.MATRIX -> {
                selectImageView.setEraseMode(false);
                flipper.setVisibility(View.VISIBLE);
                imageCopyColorFilterMode.setVisibility(View.GONE);
                EraseCopiedBmSetting.setVisibility(View.GONE);
                flipper.setDisplayedChild(3);
            }
            case SettingType.ROTATE -> {
                selectImageView.setEraseMode(false);
                flipper.setVisibility(View.VISIBLE);
                imageCopyColorFilterMode.setVisibility(View.GONE);
                EraseCopiedBmSetting.setVisibility(View.GONE);
                flipper.setDisplayedChild(4);
            }
            case SettingType.ERASE -> {
                selectImageView.setEraseMode(true);
                flipper.setVisibility(View.VISIBLE);
                imageCopyColorFilterMode.setVisibility(View.GONE);
                EraseCopiedBmSetting.setVisibility(View.GONE);
                flipper.setDisplayedChild(5);
            }
            case SettingType.DOWNLOAD -> {
                selectImageView.setEraseMode(false);
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fileName = "Image-Copied"+ n ;//+".jpg"
                String stringFormat;
                Bitmap.CompressFormat compressFormat;
                if (selectImageView.getCurrentImage().hasAlpha()){
                    compressFormat = Bitmap.CompressFormat.PNG;
                    stringFormat = ".png";
                }else {
                    compressFormat = Bitmap.CompressFormat.JPEG;
                    stringFormat = ".jpg";
                }
                SaveImage(selectImageView.getCurrentImage(),fileName,stringFormat,compressFormat);
            }
        }
    }

    private void SaveImage(Bitmap finalBitmap,String name,String stringformat,Bitmap.CompressFormat compressFormat) {

        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/HelloPics");
        myDir.mkdirs();

        File file = new File (myDir,name.trim()+stringformat);
        if (file.exists ()){
            Toast.makeText(this, "this name is already used", Toast.LENGTH_SHORT).show();
        }else {


            //file.delete();
            try {

                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(compressFormat, 100, out);

                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(
                                            Select_crop_Activity.this,
                                            "image saved success\n"+"Scanned " + path + ":\n"+ uri,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
        }
    }

    class pastSettingsAdapter extends RecyclerView.Adapter<pastSettingsAdapter.MyViewHolder> {

        Context context;
        ArrayList<PastSettings> tool_names;

        private int selectedPosition = RecyclerView.NO_POSITION;
        public pastSettingsAdapter(Context context, ArrayList<PastSettings> tool_names) {
            this.context = context;
            this.tool_names = tool_names;
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
                    .load(tool_names.get(position).getToolimg())
                    .into(holder.imageView);
            holder.textView.setText(tool_names.get(position).getTool());

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowSettingType(tool_names.get(position).getTool());
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

            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    flipper.setVisibility(View.INVISIBLE);
                    return true;
                }
            });

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

    public ArrayList<PastSettings> pastSettings(){
        ArrayList<PastSettings> pastSetting = new ArrayList<>();
        pastSetting.add(new PastSettings(SettingType.OPACITY, getResources().getDrawable(R.drawable.opacity_ic, null)));
        pastSetting.add(new PastSettings(SettingType.MODE, getResources().getDrawable(R.drawable.merge_ic, null)));
        pastSetting.add(new PastSettings(SettingType.BACKGROUND, getResources().getDrawable(R.drawable.color_ic, null)));
        pastSetting.add(new PastSettings(SettingType.MATRIX, getResources().getDrawable(R.drawable.simple_rotation, null)));
        pastSetting.add(new PastSettings(SettingType.ROTATE, getResources().getDrawable(R.drawable.rotate_right_ic, null)));
        pastSetting.add(new PastSettings(SettingType.ERASE, getResources().getDrawable(R.drawable.eraser_ic, null)));
        pastSetting.add(new PastSettings(SettingType.DOWNLOAD, getResources().getDrawable(R.drawable.dowload_ic, null)));
        //pastSetting.add(new PastSettings(SettingType.FILTER, getResources().getDrawable(R.drawable.photo_filter_ic, null)));
        return pastSetting;
    }

    public class PastSettings{
        String Tool;
        Drawable Toolimg;

        public PastSettings(String tool, Drawable toolimg) {
            Tool = tool;
            Toolimg = toolimg;
        }

        public String getTool() {
            return Tool;
        }

        public void setTool(String tool) {
            Tool = tool;
        }

        public Drawable getToolimg() {
            return Toolimg;
        }

        public void setToolimg(Drawable toolimg) {
            Toolimg = toolimg;
        }
    }
    private static class SettingType{
        static final String OPACITY ="opacity";
        static final String MODE ="Mode";
        static final String BACKGROUND="Background";
        static final String MATRIX="Matrix";
        static final String ROTATE = "Rotate";
        static final String ERASE="Erase";
        static final String DOWNLOAD="download";
        static final String FILTER="Filter";
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


    public static class ImagesFilterMode_Adapter extends RecyclerView.Adapter<ImagesFilterMode_Adapter.MyViewHolder> {
        Context context;
        ArrayList<Mode_Name> Mode_names;
        SelectImageView AddImagesView;
        int selectedItemPosition = -1;

        public ImagesFilterMode_Adapter(Context context, ArrayList<Mode_Name> mode_names, SelectImageView addImagesImageView) {
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

    public static class CopyToolsName{
        public final static String OPACITY = "Opacity";
        public final static String MODE = "Mode";
        public final static String COLOR = "Color";
        public static final String ROTATE = "Rotate";
        public static final String SCALE = "Scale";
        
    }

    public static class CopyTools {
        String ToolsName_H;
        Drawable ToolsImg_H;

        public CopyTools(String toolsName_H, Drawable toolsImg_H) {
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

}