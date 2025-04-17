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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
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

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.ToolsName.ModeName;
import com.phantomhive.exil.hellopics.Img_Editor.Views.GradientView.GradientImageView;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ColorDetector;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.SeekbarValueSettings;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.Mode_Name;
import com.phantomhive.exil.hellopics.R;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.ArrayList;

public class GradientActivity extends AppCompatActivity {
    Image image;
    RecyclerView GradientRecyclerView;
    RecyclerView GradientMode;
    GradientImageView gradientImageView;
    GradientRecyclerViewAdapter GradientRecyclerViewadapter;
    CopyMode_Adapter GradientMode_adapter;

    ImageButton Done,Cancel;

    Bitmap Center;
    ViewFlipper viewFlipper;

    TextView Mode,Angle,opacity;

    public SeekBar AngleSeekBar,OpacitySeekBar;

    TextView AngleV,OpacityV;

    ConstraintLayout constraintLayout1,constraintLayout2;
    ImageButton DoneGCS,CancelGCS;
    ArrayList<Integer> c = new ArrayList<>() ;
    RecyclerView GCRL;
    Rainbow_Adapter rainbow_adapter;
    AlertDialog colorPickerDialog;
    PopupWindow popupWindow;
    boolean colorloc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient);
        EdgeToEdgeFixing(R.id.activity_gradientL,this);

        image = Image.getInstance();
        gradientImageView = findViewById(R.id.gradientImageView);
        GradientRecyclerView = findViewById(R.id.gradientRecycleView);
        GradientMode = findViewById(R.id.GradientMode);

        Done = findViewById(R.id.imageButton_Donegradient);
        Cancel = findViewById(R.id.imageButton_Backgradient);

        viewFlipper = findViewById(R.id.GradientFlipper);

        AngleSeekBar = findViewById(R.id.gradientAngelSeekbar);
        AngleV = findViewById(R.id.TextViewgradientAngel);

        OpacitySeekBar = findViewById(R.id.gradientOpacitySeekbar);
        OpacityV = findViewById(R.id.TextViewgradientOpacity);

        constraintLayout1 = findViewById(R.id.constraintLayoutG1);
        constraintLayout2 = findViewById(R.id.constraintLayoutG2);

        /*
         GCOLOR1 = findViewById(R.id.GCOLOR1);
        GCOLOR2 = findViewById(R.id.GCOLOR2);
        GCOLOR3 = findViewById(R.id.GCOLOR3);
        GCOLOR4 = findViewById(R.id.GCOLOR4);
         */
        GCRL = findViewById(R.id.GCOLORrecyclerView);
        DoneGCS = findViewById(R.id.doneShosing);
        CancelGCS = findViewById(R.id.cancelShosing);


        Mode = findViewById(R.id.textGradientMode);
        Angle = findViewById(R.id.textGradientAngle);
        opacity = findViewById(R.id.textGradientOpacity);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(GradientActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });

        gradientImageView.setImageBitmap(image.getBitmap());

        GradientRecyclerViewadapter = new GradientRecyclerViewAdapter(GradientList(),GradientActivity.this);


        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(GradientActivity.this,
                LinearLayoutManager.HORIZONTAL, false);

        GradientRecyclerView.setLayoutManager(horizontalLayoutManager);
        GradientRecyclerView.setAdapter(GradientRecyclerViewadapter);

        GradientMode_adapter = new CopyMode_Adapter(this,modenames());
        GradientMode.setAdapter(GradientMode_adapter);
        GradientMode.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        //rainbowC();


        c.add(Color.parseColor("#e81416"));
        c.add(Color.parseColor("#ffa500"));
        c.add(null);
        c.add(null);
        c.add(null);
        c.add(null);

        rainbow_adapter = new Rainbow_Adapter(this,rainbowC());
        GCRL.setAdapter(rainbow_adapter);
        GCRL.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Center = gradientImageView.getFinalBitmap();

                image.setBitmap(Center);
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(GradientActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(GradientActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                // onBackPressed();
            }
        });



        Mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setVisibility(View.VISIBLE);
                viewFlipper.setDisplayedChild(0);
                setRectTxtVVisibility(Mode,Angle,opacity);
            }
        });
        Angle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setVisibility(View.VISIBLE);
                viewFlipper.setDisplayedChild(1);
                setRectTxtVVisibility(Angle,Mode,opacity);
            }
        });
        opacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setVisibility(View.VISIBLE);
                viewFlipper.setDisplayedChild(2);
                setRectTxtVVisibility(opacity,Angle,Mode);
            }
        });


        AngleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gradientImageView.setAngle(progress);
                AngleV.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        OpacitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gradientImageView.setOpacity(progress);
                OpacityV.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



     /*
        GCOLOR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow!=null){
                    popupWindow.dismiss();
                }

                //showPopupWindow0(GCOLOR1,GCOLOR1);
            }
        });

        GCOLOR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow!=null){
                    popupWindow.dismiss();
                }
                //showPopupWindow0(GCOLOR2,GCOLOR2);
            }
        });

        GCOLOR3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow!=null){
                    popupWindow.dismiss();
                }
                //showPopupWindow0(GCOLOR3,GCOLOR3);

            }
        });

        GCOLOR4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow!=null){
                    popupWindow.dismiss();
                }
                //showPopupWindow0(GCOLOR4,GCOLOR4);

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

                gradientImageView.setGradient(cc);
                constraintLayout1.setVisibility(View.INVISIBLE);
                constraintLayout2.setVisibility(View.VISIBLE);
                c.clear();
                c.add(Color.parseColor("#e81416"));
                c.add(Color.parseColor("#ffa500"));
                c.add(null);
                c.add(null);
                c.add(null);
                c.add(null);
                rainbow_adapter.notifyDataSetChanged();
            }
        });

        CancelGCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout1.setVisibility(View.INVISIBLE);
                constraintLayout2.setVisibility(View.VISIBLE);
                c.clear();
                c.add(Color.parseColor("#e81416"));
                c.add(Color.parseColor("#ffa500"));
                c.add(null);
                c.add(null);
                c.add(null);
                c.add(null);
                rainbow_adapter.notifyDataSetChanged();
            }
        });

    }

    private ArrayList<Gradients> GradientList() {
        ArrayList<Gradients> gradients = new ArrayList<>();
        gradients.add(new Gradients(new int[]{
                Color.TRANSPARENT,Color.TRANSPARENT},0,"none"));

        gradients.add(new Gradients(new int[]{Color.parseColor("#E500FF"),Color.parseColor("#1AFF00")},0,"E5-1A"));
        gradients.add(new Gradients(new int[]{Color.parseColor("#5E1BE4"),Color.parseColor("#A1E41B")},0,"5E-A1"));
        gradients.add(new Gradients(new int[]{Color.parseColor("#3D54C2"),Color.parseColor("#C2AB3D")},0,"3D-C2"));
        gradients.add(new Gradients(new int[]{Color.parseColor("#17E8DB"),Color.parseColor("#E81724")},0,"17-E8"));
        gradients.add(new Gradients(new int[]{Color.parseColor("#A47E5B"),Color.parseColor("#5B81A4")},0,"A4-5B"));
        gradients.add(new Gradients(new int[]{Color.parseColor("#8B7485"),Color.parseColor("#748B7A")},0,"8B-74"));
        gradients.add(new Gradients(new int[]{Color.parseColor("#F80720"),Color.parseColor("#6F8690")},0,"F8-6F"));
        gradients.add(new Gradients(new int[]{Color.parseColor("#F36F0C"),Color.parseColor("#0C90F3")},0,"F3-0C"));
        gradients.add(new Gradients(new int[]{Color.parseColor("#EAF609"),Color.parseColor("#FF00FB")},0,"EA-FF"));
        gradients.add(new Gradients(new int[]{Color.parseColor("#3D6413"),Color.parseColor("#3A1364")},0,"3D-3A"));

        gradients.add(new Gradients(new int[]{Color.parseColor("#3D6413"),Color.TRANSPARENT,Color.parseColor("#3A1364")}, 0,"3D03A"));
        gradients.add(new Gradients(new int[]{Color.parseColor("#5E1BE4"),Color.TRANSPARENT,Color.parseColor("#A1E41B")},0,"5E-A1"));
        gradients.add(new Gradients(new int[]{Color.parseColor("#A47E5B"),Color.parseColor("#5B81A4"),Color.TRANSPARENT,Color.TRANSPARENT,Color.parseColor("#5B81A4"),Color.parseColor("#A47E5B")},0,"A405B"));

        gradients.add(new Gradients(new int[]{Color.TRANSPARENT,Color.TRANSPARENT},0,"More"));
        return gradients;


    }

    public class Gradients {
        int[] Colors;
        int angle;
        String name;

        public Gradients(int[] colors, int angle, String name) {
            Colors = colors;
            this.angle = angle;
            this.name = name;
        }

        public int[] getColors() {
            return Colors;
        }

        public void setColors(int[] colors) {
            Colors = colors;
        }

        public int getAngle() {
            return angle;
        }

        public void setAngle(int angle) {
            this.angle = angle;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    public class GradientRecyclerViewAdapter extends RecyclerView.Adapter<GradientRecyclerViewAdapter.MyViewHolder> {
        ArrayList<Gradients> Gradient;
        Context context;

        public GradientRecyclerViewAdapter(ArrayList<Gradients> Gradient, Context context) {
            this.Gradient = Gradient;
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View View = LayoutInflater.from(context).inflate(R.layout.gradient_item,parent,false);
            return new MyViewHolder(View);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            if (position>=Gradient.size()-1){
                holder.imageView_Tool.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        constraintLayout1.setVisibility(View.VISIBLE);
                        constraintLayout2.setVisibility(View.INVISIBLE);
                    }
                });
                holder.imageView_Tool.setImageDrawable(getResources().getDrawable(R.drawable.add_ic, null));

            }else {
                holder.imageView_Tool.post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = Bitmap.createBitmap(holder.imageView_Tool.getWidth(),
                                holder.imageView_Tool.getHeight(),
                                Bitmap.Config.ARGB_8888);

                        Canvas canvas = new Canvas(bitmap);

                        LinearGradient linearGradient = new LinearGradient(
                                0, 0,
                                holder.imageView_Tool.getWidth(),
                                holder.imageView_Tool.getHeight(),
                                Gradient.get(position).getColors(),
                                null,
                                Shader.TileMode.CLAMP);

                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setShader(linearGradient);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                        canvas.drawRect(0, 0,  bitmap.getWidth(),
                                bitmap.getHeight(), paint);

                        holder.imageView_Tool.setImageBitmap(bitmap);
                    }
                });

                holder.imageView_Tool.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });


                holder.imageView_Tool.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gradientImageView.setGradient(Gradient.get(position).getColors());
                    }
                });

            }

            holder.textView_Tool.setText(Gradient.get(position).getName());


        }

        @Override
        public int getItemCount() {
            return Gradient.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView_Tool;
            TextView textView_Tool;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView_Tool = itemView.findViewById(R.id.gradientlock);
                textView_Tool = itemView.findViewById(R.id.gradientname);

            }
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
                    case ModeName.NORMAL -> gradientImageView.setMode(null);
                    case ModeName.DARKEN -> gradientImageView.setMode(PorterDuff.Mode.DARKEN);
                    case ModeName.LIGHTEN -> gradientImageView.setMode(PorterDuff.Mode.LIGHTEN);
                    case ModeName.MULTIPLY -> gradientImageView.setMode(PorterDuff.Mode.MULTIPLY);
                    case ModeName.OVERLAY -> gradientImageView.setMode(PorterDuff.Mode.OVERLAY);
                    case ModeName.SCREEN -> gradientImageView.setMode(PorterDuff.Mode.SCREEN);
                    case ModeName.ADD -> gradientImageView.setMode(PorterDuff.Mode.ADD);
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


    public class Rainbow_Adapter extends RecyclerView.Adapter<Rainbow_Adapter.MyViewHolder> {

        Context context;
        ArrayList<Integer> rainbow;

        public Rainbow_Adapter(Context context, ArrayList<Integer> rainbow) {
            this.context = context;
            this.rainbow = rainbow;

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View View = LayoutInflater.from(context).inflate(R.layout.gradientcolorlist_item,parent,false);
            return new MyViewHolder(View);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.textView.post(new Runnable() {
                @Override
                public void run() {
                    holder.textView.setImageBitmap(ColorDetector.getCroppedBitmap(holder.textView,rainbow.get(position)));
                }
            });


            holder.textView.setOnClickListener(v -> {
                if (popupWindow!=null){
                    popupWindow.dismiss();

                }
                showPopupWindow0(holder.textView,holder.textView,position);
            });
        }

        @Override
        public int getItemCount() {
            return rainbow.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            ImageButton textView;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.GCOLOR);
            }
        }
    }

    /*
    @Override
    public void onBackPressed() {
        if (Center!=null){
            image.setBitmap(gradientImageView.getFinalBitmap());
        }
        Intent intent = Editor_main_Activity.fa.getIntent();
        finish();
        startActivity(intent);
        super.onBackPressed();
    }
     */



    private void setRectTxtVVisibility(TextView V,TextView InVa,TextView InVb){

        V.setTextColor(Color.parseColor("#A6A061"));

        InVa.setTextColor(Color.BLACK);
        InVb.setTextColor(Color.BLACK);

    }

    public void showValueGetter(TextView textView, SeekBar seekBar){

        SeekbarValueSettings seekbarValueSettings = new SeekbarValueSettings();
        seekbarValueSettings.OnValueManuelEdit(textView,GradientActivity.this,seekBar);
    }

    private void showPopupWindow0(View anchorView,ImageButton imageButton,int Position) {
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
                    gradientImageView.setColorLoc(false);
                    colorloc = false;
                }

                colorPickerDialog = new ColorPickerDialog.Builder(GradientActivity.this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(("Ok"),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        imageButton.setImageBitmap(ColorDetector.getCroppedBitmap(imageButton, envelope.getColor()));
                                        c.set(Position,envelope.getColor());
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
                gradientImageView.setColorLoc(true);
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
}