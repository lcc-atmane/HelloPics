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
package com.phantomhive.exil.hellopics.ui_Home.HomeActivity;


import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.phantomhive.exil.hellopics.Create_Art_Classes.ChooseImageArtActivity;
import com.phantomhive.exil.hellopics.R;

public class HomeActivity extends AppCompatActivity {
    //error if permission Denied.
    AlertDialog.Builder error;
    AlertDialog.Builder momo;
    // Start ChoseArtActivity
    FloatingActionButton floating_Button_Gallery;



    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 78) {
                        Intent intent = result.getData();
                    }
                }
            });

    ActivityResultLauncher<String> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            isGranted->{
                if (isGranted){
                    Intent Gallery = new Intent(HomeActivity.this, ChooseImageArtActivity.class);
                    startActivity(Gallery);
                }else {
                    boolean showRationale;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
                        showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // API 29 - 32
                        showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                    } else { // API 28 and below
                        showRationale = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                    error = new AlertDialog.Builder(HomeActivity.this);
                    error.setTitle("Notice");
                    error.setMessage("We need access to the files to get Pictures");
                    error.setCancelable(false);
                    error.setPositiveButton(Html.fromHtml("<font color='#A6A061'>Ok</font>", HtmlCompat.FROM_HTML_MODE_LEGACY), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (showRationale){
                                tackPermission();
                            }else {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                activityResultLauncher.launch(intent);
                            }

                        }
                    });


                    error.setNegativeButton(Html.fromHtml("<font color='#A6A061'>No</font>", HtmlCompat.FROM_HTML_MODE_LEGACY), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finishAffinity();
                            System.exit(0);
                        }
                    });
                    AlertDialog dialog = error.create();
                    dialog.show();
                }

            });


    private FirebaseRemoteConfig firebaseRemoteConfig;
    private static final int UPDATE_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseApp.initializeApp(this);
        EdgeToEdgeFixing(R.id.ActivityHomeL,this);

        // Initialize Firebase Remote Config
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(120) // Refresh every 2 minutes
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        // Fetch Remote Config values
        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            checkForForcedUpdate();
                        }
                    }
                });

        // Initialize Play Store In-App Updates

        floating_Button_Gallery = findViewById(R.id.floating_start_Gallery);


        // Get the NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        // Get NavController and set up BottomNavigationView
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            BottomNavigationView navView = findViewById(R.id.nav_view);
            NavigationUI.setupWithNavController(navView, navController);

            // Prevent recreation of fragments
            navView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == navController.getCurrentDestination().getId()) {
                    return true; // Prevent reloading the same fragment
                }
                return NavigationUI.onNavDestinationSelected(item, navController);
            });
        }

        //FL onClick
        floating_Button_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start chooseArtActivity
                tackPermission();
            }
        });

}
    // Check if forced update is needed
    private void checkForForcedUpdate() {
        int requiredVersion = (int) firebaseRemoteConfig.getLong("force_update_version");
        int currentVersion = getCurrentVersion();

        if (currentVersion < requiredVersion) {
            showUpdateDialog();
        }
//        else {
//            checkForOptionalUpdate(); // If no forced update, check for optional update
//        }
    }

    // Get the current app version
    private int getCurrentVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Show force update dialog
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Required");
        builder.setMessage("A new version is available. Please update to continue.");

        // Create a LinearLayout to hold the buttons and the clickable TextView
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        // Create the "Visit this link manually" message
        TextView manualLinkText = new TextView(this);
        manualLinkText.setText("If the link doesn't open, Copy this link and open it : ");
        manualLinkText.setTextColor(Color.BLACK);
        manualLinkText.setTextSize(16);
        manualLinkText.setGravity(Gravity.CENTER);
        manualLinkText.setPadding(20, 10, 20, 10);

        // Create the "Copy Link" clickable text
        TextView copyLinkText = new TextView(this);
        copyLinkText.setText("Click here to copy the link");
        copyLinkText.setTextColor(Color.parseColor("#A6A061"));
        copyLinkText.setTextSize(16);
        copyLinkText.setGravity(Gravity.CENTER);
        copyLinkText.setPadding(20, 10, 20, 10);
        copyLinkText.setClickable(true);
        copyLinkText.setTypeface(null, Typeface.BOLD);
        copyLinkText.setOnClickListener(v -> {
            // Copy the URL to clipboard
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("URL", "https://lcc-atmane.github.io/HelloPicsSite/");
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Link copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        // Add the TextViews to the layout
        layout.addView(manualLinkText);
        layout.addView(copyLinkText);

        // Set the custom layout inside the dialog
        builder.setView(layout);

        // "Update" Button
        builder.setPositiveButton("Update", (dialog, which) -> {
            // Create an intent to open the URL in a browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lcc-atmane.github.io/HelloPicsSite/"));
            intent.setPackage("com.android.chrome");  // Ensure the intent opens in Chrome
            startActivity(intent);
            finish();
        });

        // "Exit" Button
        builder.setNegativeButton("Exit", (dialog, which) -> finish());

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_REQUEST_CODE && resultCode != RESULT_OK) {
            Log.e("Update", "Update failed!");
        }
    }

    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent Gallery = new Intent(HomeActivity.this, ChooseImageArtActivity.class);
                    startActivity(Gallery);
                    //in 10 android or billow
                } else {
                    boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                    momo = new AlertDialog.Builder(HomeActivity.this);
                    momo.setTitle("Notice");
                    momo.setMessage("We need access to the files to get Pictures");
                    momo.setCancelable(false);
                    momo.setPositiveButton(Html.fromHtml("<font color='#A6A061'>Ok</font>", HtmlCompat.FROM_HTML_MODE_LEGACY), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (showRationale){
                                tackPermission();
                            }else {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                activityResultLauncher.launch(intent);
                            }

                        }
                    });
                    momo.setNegativeButton(Html.fromHtml("<font color='#A6A061'>No</font>", HtmlCompat.FROM_HTML_MODE_LEGACY), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finishAffinity();
                            System.exit(0);
                        }
                    });
                    AlertDialog dialog = momo.create();
                    dialog.show();
                }
            }

    }

    public void tackPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 and above
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                openGallery();
            } else {
                // Request permission for images
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 and 12
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                openGallery();
            } else {
                // Request write external storage permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        } else {
            // For Android 10 and below
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                openGallery();
            } else {
                // Request write external storage permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    // Separate method for opening gallery
    private void openGallery() {
        Intent Gallery = new Intent(HomeActivity.this, ChooseImageArtActivity.class);
        startActivity(Gallery);
    }


    public void tackPermission1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) ==
                    PackageManager.PERMISSION_GRANTED) {
                Intent Gallery = new Intent(HomeActivity.this, ChooseImageArtActivity.class);
                startActivity(Gallery);
            }else {
                launchSomeActivity.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }

        }else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                Intent Gallery = new Intent(HomeActivity.this, ChooseImageArtActivity.class);
                startActivity(Gallery);
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }
        }
    }
    public void tackPermissionm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 and above
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                // All permissions granted
                openGallery();
            } else {
                // Request permission for images
                String[] permissions = new String[]{
                        Manifest.permission.READ_MEDIA_IMAGES
                };
                ActivityCompat.requestPermissions(this, permissions, 100);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 and 12
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(intent);
                }
            } else {
                openGallery();
            }
        } else {
            // For Android 10 and below
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // All permissions granted
                openGallery();
            } else {
                // Request permissions
                String[] permissions = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                ActivityCompat.requestPermissions(this, permissions, 100);
            }
        }
    }
}
