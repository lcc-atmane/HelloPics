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