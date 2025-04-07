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
package com.phantomhive.exil.hellopics.ui_Home.HomeActivity;


import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;

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
        new AlertDialog.Builder(this)
                .setTitle("Update Required")
                .setMessage("A new version is available. Please update to continue.")
                .setCancelable(false)
                .setPositiveButton("Update", (dialog, which) -> {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    finish();
                })
                .setNegativeButton("Exit", (dialog, which) -> finish())
                .show();
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
