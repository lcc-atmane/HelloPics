package com.phantomhive.exil.hellopics.FirstActivities;
/*
Splash Activity and the first activity to welcome the user
 */
import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.ads.MobileAds;
import com.phantomhive.exil.hellopics.R;
import com.phantomhive.exil.hellopics.ui_Home.HomeActivity.HomeActivity;

public class SplashScreenActivity extends AppCompatActivity {
    // time to pass to the Second Activity.
    int splash = 4000;
    //for ensure that the user is using the app for first time or not (true=first time,false=not first time).
    boolean firstlog;
    // for save the confirmation volue (true or false).
    SharedPreferences msharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        EdgeToEdgeFixing(R.id.splash_screen_activityL,this);

        // Initialize the Google Mobile Ads SDK on a background thread.
        MobileAds.initialize(this, initializationStatus -> {});
                // save true in the start.
                msharedPreferences = getSharedPreferences("loginornot",MODE_PRIVATE);
                firstlog = msharedPreferences.getBoolean("firstlog",true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // if the first time start shosing_SingOrSkip_Activity.
                if (firstlog){
                    Intent shoselogeornot = new Intent(SplashScreenActivity.this, StartEditingActivity.class);
                    startActivity(shoselogeornot);
                    finish();
                }// if is not the firt time start HomeActivity.
                else {
                    Intent HomeActivity = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(HomeActivity);
                    finish();
                }
            }
        },splash);


    }
}