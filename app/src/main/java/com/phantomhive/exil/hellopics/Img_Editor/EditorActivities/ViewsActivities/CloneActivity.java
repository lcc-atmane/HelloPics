package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities;

import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.ToolsName.ModeName;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Clone.CloneImageView;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.SeekbarValueSettings;
import com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames.Mode_Name;
import com.phantomhive.exil.hellopics.R;

import java.util.ArrayList;

public class CloneActivity extends AppCompatActivity {
    CloneImageView cloneImageView;
    Image image;
    ImageButton cloneLoc,cloneBrush,cloneEraser,cloneMode;
    ImageButton doneClone,cancelClone;
    Bitmap BitmapFinalCrop;
    public SeekBar OpacitySeekBar,CloneSizeSeekBar,CloneHardnessSeekBar;
    TextView CloneOpacity,CloneHardness,CloneSize;
    RecyclerView recyclerView;
    ViewFlipper CloneToolsFlipper;
    CopyMode_Adapter copyMode_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clone);
        EdgeToEdgeFixing(R.id.activity_cloneL,this);

        cloneImageView = findViewById(R.id.CloneImageView);
        cloneLoc = findViewById(R.id.CloneLoc);
        cloneBrush = findViewById(R.id.CloneBrush);
        cloneEraser = findViewById(R.id.CloneEraser);
        cloneMode = findViewById(R.id.CloneMode);

        doneClone = findViewById(R.id.doneClone);
        cancelClone = findViewById(R.id.cancelClone);

        image = Image.getInstance();
        cloneImageView.setImageBitmap(image.getBitmap());
        cloneImageView.setSourceImage(image.getBitmap());

        CloneToolsFlipper = findViewById(R.id.CloneToolFlipper);
        OpacitySeekBar = findViewById(R.id.CloneOpacity);
        CloneSizeSeekBar = findViewById(R.id.CloneSize);
        CloneHardnessSeekBar =findViewById(R.id.CloneHardness);

        recyclerView = findViewById(R.id.cloneModeList);

        CloneOpacity = findViewById(R.id.CloneOpacityValue);
        CloneSize = findViewById(R.id.CloneSizeValue);
        CloneHardness = findViewById(R.id.CloneHardnessValue);


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(CloneActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });



        if (modenames().isEmpty()){
            Log.d("Tools_Home_ArrayList","ArrayList is empty");
        }else {
            copyMode_adapter = new CopyMode_Adapter(this,modenames());
            recyclerView.setAdapter(copyMode_adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        }

        cloneLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cloneImageView.getLocDetect(true);
            }
        });

        cloneBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloneToolsFlipper.setVisibility(View.INVISIBLE);
                cloneImageView.getLocDraw(true);
            }
        });

        cloneEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloneToolsFlipper.setVisibility(View.INVISIBLE);
                cloneImageView.setEraser(true);
            }
        });

        cloneMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CloneToolsFlipper.getVisibility() ==View.INVISIBLE){
                    CloneToolsFlipper.setVisibility(View.VISIBLE);
                    CloneToolsFlipper.setDisplayedChild(1);
                } else {
                CloneToolsFlipper.setVisibility(View.INVISIBLE);
                }
            }
        });
        cloneBrush.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (CloneToolsFlipper.getVisibility() ==View.INVISIBLE){
                    CloneToolsFlipper.setVisibility(View.VISIBLE);
                    CloneToolsFlipper.setDisplayedChild(0);
                }else {
                    CloneToolsFlipper.setVisibility(View.INVISIBLE);
                }

                return true;
            }
        });

        cloneEraser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (CloneToolsFlipper.getVisibility() ==View.INVISIBLE){
                    CloneToolsFlipper.setVisibility(View.VISIBLE);
                    CloneToolsFlipper.setDisplayedChild(0);
                }else {
                    CloneToolsFlipper.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });

        doneClone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapFinalCrop = cloneImageView.getFinalBitmap();

                 image.setBitmap(BitmapFinalCrop);
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(CloneActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);

                //onBackPressed();
            }
        });

        cancelClone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(CloneActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
               // onBackPressed();
            }
        });


        CloneSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cloneImageView.setCloneSize(progress);
                CloneSize.setText(String.valueOf(progress));
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
                cloneImageView.setCloneOpacity(progress);
                CloneOpacity.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

       CloneHardnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cloneImageView.setCloneHardness(progress);
                CloneHardness.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
        public void onBindViewHolder(@NonNull CopyMode_Adapter.MyViewHolder holder, int position) {
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
                    case ModeName.NORMAL -> cloneImageView.seCloneMode(null);
                    case ModeName.DARKEN -> cloneImageView.seCloneMode(PorterDuff.Mode.DARKEN);
                    case ModeName.LIGHTEN -> cloneImageView.seCloneMode(PorterDuff.Mode.LIGHTEN);
                    case ModeName.MULTIPLY -> cloneImageView.seCloneMode(PorterDuff.Mode.MULTIPLY);
                    case ModeName.OVERLAY -> cloneImageView.seCloneMode(PorterDuff.Mode.OVERLAY);
                    case ModeName.SCREEN -> cloneImageView.seCloneMode(PorterDuff.Mode.SCREEN);
                    case ModeName.ADD -> cloneImageView.seCloneMode(PorterDuff.Mode.ADD);
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
        seekbarValueSettings.OnValueManuelEdit(textView,CloneActivity.this,seekBar);
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