package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ImageStretch.Mesh;
import com.phantomhive.exil.hellopics.R;

public class StretchActivity extends AppCompatActivity {

    Mesh imageStretch;
    Image image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
           
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stretch);
        imageStretch = findViewById(R.id.imageStretch);
        image = Image.getInstance();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Finish current activity to go back
                finish();
                // Start the same activity again
                Intent backToMain = new Intent(StretchActivity.this, Editor_main_Activity.class);
                startActivity(backToMain);
            }
        });
        imageStretch.setImageBitmap(image.getBitmap());
    }
}