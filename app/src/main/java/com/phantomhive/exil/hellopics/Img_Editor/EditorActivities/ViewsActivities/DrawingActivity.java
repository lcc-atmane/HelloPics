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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.ToolsName.ModeName;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Drawing.DrawView;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ColorDetector;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.SeekbarValueSettings;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.Mode_Name;
import com.phantomhive.exil.hellopics.R;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.ArrayList;

public class DrawingActivity extends AppCompatActivity {
    DrawView mDrawView;
    ImageButton doneDraw,cancelDraw;
    Image image;
    Bitmap BitmapFinalCrop;
    int color = Color.parseColor("#A6A061");
    ImageButton drawBrush,drawEraser,drawMode;
    public ImageView drawLoc;
    public SeekBar drawOpacitySeekBar,drawSizeSeekBar,drawHardnessSeekBar;
    TextView drawOpacity,drawHardness,drawSize;
    PopupWindow popupWindow;
    boolean colorloc = false;
    CopyMode_Adapter copyMode_adapter;

    RecyclerView recyclerView;
    ViewFlipper drawToolsFlipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        EdgeToEdgeFixing(R.id.activity_drawingL,this);

        mDrawView = findViewById(R.id.drawView);
        doneDraw = findViewById(R.id.doneDraw);
        cancelDraw = findViewById(R.id.cancelDraw);


        recyclerView = findViewById(R.id.drawModeList);
        drawToolsFlipper = findViewById(R.id.drawToolFlipper);

        drawOpacitySeekBar = findViewById(R.id.drawOpacity);
        drawSizeSeekBar = findViewById(R.id.drawSize);
        drawHardnessSeekBar = findViewById(R.id.drawHardnessSeekBar);

        drawOpacity = findViewById(R.id.drawOpacityValue);
        drawSize = findViewById(R.id.drawSizeValue);
        drawHardness = findViewById(R.id.drawHardness);


        drawLoc = findViewById(R.id.ColorLoc);
        drawBrush = findViewById(R.id.drawBrush);
        drawEraser = findViewById(R.id.drawEraser);
        drawMode = findViewById(R.id.drawMode);

        image = Image.getInstance();


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(DrawingActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });


        mDrawView.setImageBitmap(image.getBitmap());
        mDrawView.setSourceImage(image.getBitmap());


        if (modenames().isEmpty()){
            Log.d("Tools_Home_ArrayList","ArrayList is empty");
        }else {
            copyMode_adapter = new CopyMode_Adapter(this,modenames());
            recyclerView.setAdapter(copyMode_adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        }

        drawLoc.post(new Runnable() {
            @Override
            public void run() {
                drawLoc.setImageBitmap(ColorDetector.getCroppedBitmap(drawLoc,
                        color));
            }
        });


        drawEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawToolsFlipper.setVisibility(View.INVISIBLE);
                mDrawView.setEraser(true);
                mDrawView.setColorLoc(false);
            }
        });

        drawMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawToolsFlipper.getVisibility() ==View.INVISIBLE){
                    drawToolsFlipper.setVisibility(View.VISIBLE);
                    drawToolsFlipper.setDisplayedChild(1);
                }else {
                    drawToolsFlipper.setVisibility(View.INVISIBLE);
                }
            }
        });
        drawBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawToolsFlipper.setVisibility(View.INVISIBLE);
                mDrawView.setEraser(false);
                mDrawView.setColorLoc(false);
            }
        });

        drawLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(drawLoc);
                //
            }
        });

        doneDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapFinalCrop = mDrawView.getFinalBitmap();

                image.setBitmap(BitmapFinalCrop);
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(DrawingActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });

        cancelDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(DrawingActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });

        drawBrush.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (drawToolsFlipper.getVisibility() ==View.INVISIBLE){
                    drawToolsFlipper.setVisibility(View.VISIBLE);
                    drawToolsFlipper.setDisplayedChild(0);
                }else {
                    drawToolsFlipper.setVisibility(View.INVISIBLE);
                }

                return true;
            }
        });

        drawEraser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (drawToolsFlipper.getVisibility() ==View.INVISIBLE){
                    drawToolsFlipper.setVisibility(View.VISIBLE);
                    drawToolsFlipper.setDisplayedChild(0);
                }else {
                    drawToolsFlipper.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });

        drawSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDrawView.setDrawSize(progress);
                drawSize.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                drawSize.setText(R.string.Size);

            }
        });

        drawOpacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDrawView.setDrawOpacity(progress);
                drawOpacity.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                drawOpacity.setText(R.string.Opacity);
            }
        });

        drawHardnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDrawView.setDrawHardness(progress);
                drawHardness.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                drawHardness.setText(R.string.Hardness);
            }
        });



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
            // Change background color if the item is selected
            if (selectedItemPosition == position) {
                holder.textView.setTextColor(Color.parseColor("#A6A061")); // Change to your desired color
            } else {
                holder.textView.setTextColor(Color.BLACK); // Reset to default color
            }
            holder.textView.setOnClickListener(v -> {mDrawView.setEraser(false);
                int previousSelectedItem = selectedItemPosition;
                selectedItemPosition = position;

                // Notify the adapter about the item change to trigger a UI update
                notifyItemChanged(previousSelectedItem);
                notifyItemChanged(selectedItemPosition);


                switch (Mode_names.get(position).getModename()) {
                    case ModeName.NORMAL -> mDrawView.setDrawMode(null);
                    case ModeName.DARKEN -> mDrawView.setDrawMode(PorterDuff.Mode.DARKEN);
                    case ModeName.LIGHTEN -> mDrawView.setDrawMode(PorterDuff.Mode.LIGHTEN);
                    case ModeName.MULTIPLY -> mDrawView.setDrawMode(PorterDuff.Mode.MULTIPLY);
                    case ModeName.OVERLAY -> mDrawView.setDrawMode(PorterDuff.Mode.OVERLAY);
                    case ModeName.SCREEN -> mDrawView.setDrawMode(PorterDuff.Mode.SCREEN);
                    case ModeName.ADD -> mDrawView.setDrawMode(PorterDuff.Mode.ADD);
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

    public void showvaluegetter(TextView textView, SeekBar seekBar){
        SeekbarValueSettings seekbarValueSettings = new SeekbarValueSettings();
        seekbarValueSettings.OnValueManuelEdit(textView,DrawingActivity.this,seekBar);
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
                    mDrawView.setColorLoc(false);
                    colorloc = false;
                }
                AlertDialog colorPickerDialog = new ColorPickerDialog.Builder(DrawingActivity.this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(("Ok"),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        color = envelope.getColor();
                                        drawLoc.setImageBitmap(ColorDetector.getCroppedBitmap(drawLoc,
                                                color));
                                        mDrawView.setDrawColor(color);
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
                popupWindow.dismiss();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle button2 click
                colorloc = true;
                mDrawView.setColorLoc(true);
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

}