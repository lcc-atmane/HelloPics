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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.textfield.TextInputEditText;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg.All_images_Activity;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg.Gallery_UseType;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.Editor_Adapters.AsapRation_Adapter;
import com.phantomhive.exil.hellopics.Img_Editor.Interface.itemOnClick;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.ToolsName.AsapRationName_Number;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.ToolsName.ModeName;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ColorDetector;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.SeekbarValueSettings;
import com.phantomhive.exil.hellopics.Img_Editor.Views.imageBorders.ImageBorders;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.AsapRatio_ND;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.Mode_Name;
import com.phantomhive.exil.hellopics.R;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.ArrayList;


public class ImageBorderActivity extends AppCompatActivity implements itemOnClick {

    Image image;
    ImageBorders imageBorders;
    ImageButton doneBordering, cancelBordering;
    public SeekBar secondBorder;
    TextView Rotate;
    ImageButton Rotatebtn;
    private Bitmap BitmapFinalCrop;
    AlertDialog colorPickerDialog ;
    public ImageButton Border1Color,ImageBackground;

    RecyclerView recyclerViewRatio;
    ArrayList<AsapRatio_ND> asapRatio_nds;
    AsapRation_Adapter asapRation_adapter;
    PopupWindow popupWindow;
    boolean colorloc =  false;

    RecyclerView recyclerView;
    CopyMode_Adapter copyMode_adapter;
    ImageButton BorderMergebtn;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 78) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            String bitmapPath= intent.getStringExtra("BITMAP");

                            Glide.with(ImageBorderActivity.this)
                                    .asBitmap()
                                    .load(bitmapPath)
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            imageBorders.setBackgroundImage(resource);
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
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_border);
        EdgeToEdgeFixing(R.id.activity_image_borderL,this);

        image = Image.getInstance();

        imageBorders = findViewById(R.id.imageBorders);

        secondBorder = findViewById(R.id.SecondBorder);
        Rotate = findViewById(R.id.BorderRotate);
        Rotatebtn = findViewById(R.id.BorderRotatebtn);

        recyclerView = findViewById(R.id.BorderMode);
        BorderMergebtn = findViewById(R.id.BorderMergebtn);

        doneBordering = findViewById(R.id.doneBordering);
        cancelBordering = findViewById(R.id.cancelBordering);

        Border1Color = findViewById(R.id.Border1Color);
        ImageBackground = findViewById(R.id.Image_B);

        recyclerViewRatio = findViewById(R.id.RatiosRecycleView);


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(ImageBorderActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });

        imageBorders.setImageBitmap(image.getBitmap());
        imageBorders.setImageSource(image.getBitmap());


        imageBorders.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ImageBorderActivity.this, "To Scale The Image and move it\nyou can zoom in&out and move it With your fingers ", Toast.LENGTH_LONG).show();
                imageBorders.Fit(1,1);
            }
        });

        Border1Color.post(new Runnable() {
            @Override
            public void run() {
                Border1Color.setImageBitmap(ColorDetector.getCroppedBitmap(Border1Color,Color.parseColor("#A6A061")));
            }
        });

        if (modenames().isEmpty()){
            Log.d("Tools_Home_ArrayList","ArrayList is empty");
        }else {
            copyMode_adapter = new CopyMode_Adapter(this,modenames());
            recyclerView.setAdapter(copyMode_adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        }

        //set the editing Tools Arraylist and set adapter.
        asapRatio_nds = ToolsHome();
        if (asapRatio_nds.isEmpty()){
            Log.d("Tools_Home_Arraylist","Arraylist is empty");
        }
        else {
            asapRation_adapter = new AsapRation_Adapter(ImageBorderActivity.this,asapRatio_nds,this);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(
                    ImageBorderActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewRatio.setLayoutManager(horizontalLayoutManager);
            recyclerViewRatio.setAdapter(asapRation_adapter);
        }


        doneBordering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapFinalCrop = imageBorders.getFinalBitmap();

                image.setBitmap(BitmapFinalCrop);
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(ImageBorderActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });

        cancelBordering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(ImageBorderActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });

        Border1Color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(Border1Color);
            }
        });




        ImageBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageBorderActivity.this, All_images_Activity.class);
                intent.putExtra("USE_TYPE", Gallery_UseType.ONE_SELECT);
                activityResultLauncher.launch(intent);
            }
        });

        secondBorder.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                recyclerView.setVisibility(View.GONE);
                Rotate.setVisibility(View.VISIBLE);
                imageBorders.setImageRotate(progress);
                Rotate.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Rotate.setText(R.string.Rotate);
            }
        });

        Rotatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                Rotate.setVisibility(View.VISIBLE);
                SeekbarValueSettings seekbarValueSettings = new SeekbarValueSettings();
                seekbarValueSettings.OnValueManuelEdit(Rotate,ImageBorderActivity.this,secondBorder);
            }
        });

        BorderMergebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               recyclerView.setVisibility(View.VISIBLE);
               Rotate.setVisibility(View.GONE);
            }
        });
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

    public class CopyMode_Adapter extends RecyclerView.Adapter<CopyMode_Adapter.MyViewHolder> {
        Context context;
        ArrayList<Mode_Name> Mode_names;
        int selectedItemPosition = -1;

        public CopyMode_Adapter(Context context, ArrayList<Mode_Name> mode_names) {
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
                    case ModeName.NORMAL -> imageBorders.setBorderMode(null);
                    case ModeName.DARKEN -> imageBorders.setBorderMode(PorterDuff.Mode.DARKEN);
                    case ModeName.LIGHTEN -> imageBorders.setBorderMode(PorterDuff.Mode.LIGHTEN);
                    case ModeName.MULTIPLY -> imageBorders.setBorderMode(PorterDuff.Mode.MULTIPLY);
                    case ModeName.OVERLAY -> imageBorders.setBorderMode(PorterDuff.Mode.OVERLAY);
                    case ModeName.SCREEN -> imageBorders.setBorderMode(PorterDuff.Mode.SCREEN);
                    case ModeName.ADD -> imageBorders.setBorderMode(PorterDuff.Mode.ADD);
                }
            });
        }

        @Override
        public int getItemCount() {
            return Mode_names.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.CopyModeText);
            }
        }
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
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.POST_PINTEREST,getResources().getDrawable(R.drawable.post_pin,null)));
        editingtools.add(new AsapRatio_ND(AsapRationName_Number.MORE,getResources().getDrawable(R.drawable.add_ic,null)));
        return editingtools;
    }
    private void showPopupWindow(View anchorView) {
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
                    imageBorders.setColorLoc(false);
                    colorloc = false;
                }
                colorPickerDialog = new ColorPickerDialog.Builder(ImageBorderActivity.this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(("Ok"),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        imageBorders.setBorderColor(envelope.getColor());
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
                popupWindow.dismiss();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button2 click
                colorloc = true;
                imageBorders.setColorLoc(true);
                popupWindow.dismiss();
            }
        });

        popupWindow = new PopupWindow(
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
        popupWindow.showAtLocation(
                anchorView,
                Gravity.NO_GRAVITY,
                anchorX- (popupView.getMeasuredWidth() / 2) + (anchorView.getWidth() / 2),
                anchorY-popupView.getMeasuredHeight()
        );

        Log.d("TAG", "showPopupWindow2: "+anchorView.getWidth());
        // Dismiss the popup when clicking outside
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (popupWindow != null && popupWindow.isShowing()) {
            Rect popupRect = new Rect();
            popupWindow.getContentView().getGlobalVisibleRect(popupRect);

            if (!popupRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                popupWindow.dismiss();
            }
        }
        return super.dispatchTouchEvent(event);
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
                int width = image.getBitmap().getWidth();
                int height = image.getBitmap().getHeight();

                // Calculate the greatest common divisor (GCD)
                int gcd = imageBorders.gcd(width, height);

                // Calculate the aspect ratio in its simplest form (x:y)
                int aspectRatioX = width / gcd;
                int aspectRatioY = height / gcd;
                imageBorders.Fit(aspectRatioX,aspectRatioY);
                break;
            // true
            case AsapRationName_Number.SQUARE_INST:
                imageBorders.Fit(1,1);
                break;
            // true
            case AsapRationName_Number.PORTRAIT_INST:
                imageBorders.Fit(4,5);
                break;
            // true
            case AsapRationName_Number.STORY_INST:
                imageBorders.Fit(9,16);
                break;
            // true
            case AsapRationName_Number.POST_TWIT:
            case AsapRationName_Number.POST_FB:
            case AsapRationName_Number.COVER_YTB:
                imageBorders.Fit(16,9);
                break;
            // true
            case AsapRationName_Number.HEADER_TWIT:
                // false!!
            case AsapRationName_Number.COVER_FB:
                imageBorders.Fit(3,1);
                break;
            // true.
            case AsapRationName_Number.POST_PINTEREST:
                imageBorders.Fit(2,3);
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
                        imageBorders.Fit(x,y);
                    }else {
                        Toast.makeText(ImageBorderActivity.this, "The value of x:y should be more then 0", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ImageBorderActivity.this, "The value of x:y should be more then 0", Toast.LENGTH_SHORT).show();
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