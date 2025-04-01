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


package com.phantomhive.exil.hellopics.FirstActivities;
import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.phantomhive.exil.hellopics.R;
import com.phantomhive.exil.hellopics.ui_Home.HomeActivity.HomeActivity;

public class StartEditingActivity extends AppCompatActivity {
    Button btnSkip;

    ImageView imageView_top,imageView_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shosing_singorskip_);
        EdgeToEdgeFixing(R.id.activity_shosing_singorskip_L,this);

        btnSkip = findViewById(R.id.btnSkiplogin);
        imageView_top = findViewById(R.id.imageView_top);
        imageView_logo =findViewById(R.id.logo_image);


        //set scaled image top of design.
        Bitmap bitmap_image_top = BitmapFactory.decodeResource(this.getResources(),R.drawable.shosingimgdecore);
        Bitmap bitmap_scaled_of_image_top = Bitmap.createScaledBitmap(bitmap_image_top,bitmap_image_top.getWidth()/2,bitmap_image_top.getHeight()/2,true);
        imageView_top.setImageBitmap(bitmap_scaled_of_image_top);



        // Ovverride the Signup by Skip and Save false to the SharedPreferences.
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });


    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Beta App Notice");
        builder.setMessage("This is a beta version of the app. If you encounter any errors, please report them to:\n\nsilvanagraphics.app@gmail.com\n\nYou can also leave a review on the Play Store to share your feedback!");

// Create a LinearLayout to hold the buttons
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

// Create the "Copy Email" clickable text
        TextView copyEmailText = new TextView(this);
        copyEmailText.setText("Copy Email");
        copyEmailText.setPaintFlags(copyEmailText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        copyEmailText.setTextColor(Color.parseColor("#A6A061"));
        copyEmailText.setPadding(20, 10, 20, 10);
        copyEmailText.setTextSize(16);
        copyEmailText.setGravity(Gravity.CENTER);
        copyEmailText.setClickable(true);
        copyEmailText.setTypeface(null, Typeface.BOLD);
        copyEmailText.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Email", "silvanagraphics.app@gmail.com");
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Email copied!", Toast.LENGTH_SHORT).show();
        });

// Create the "Leave a Review" clickable text
        TextView reviewText = new TextView(this);
        reviewText.setText("Leave a Review");
        reviewText.setPaintFlags(copyEmailText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        reviewText.setTextColor(Color.parseColor("#A6A061"));
        reviewText.setPadding(20, 10, 20, 10);
        reviewText.setTextSize(16);
        reviewText.setGravity(Gravity.CENTER);
        reviewText.setClickable(true);
        reviewText.setTypeface(null, Typeface.BOLD);
        reviewText.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        });

// Add the TextViews to the layout
        layout.addView(copyEmailText);
        layout.addView(reviewText);

// Set the custom layout inside the dialog
        builder.setView(layout);

// OK Button to dismiss the dialog
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Save false to say it's not the first time opening the app
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loginornot", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstlog", false);
            editor.apply();

            // Start HomeActivity
            Intent HomePage = new Intent(StartEditingActivity.this, HomeActivity.class);
            startActivity(HomePage);
            finish();
        });

// Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();


    }
//    public static Activity fa;
//   // void onCreate()
//    {
//        fa = this;
//    }


}