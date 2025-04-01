package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.HiddenSecrets;

import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.phantomhive.exil.hellopics.Create_Art_Classes.ChooseImageArtActivity;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg.All_images_Activity;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg.Gallery_UseType;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Image_Item_ClickListener;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.HiddenSecrets.HiddenSecretsSettings.HiddenImagesDataAdapter;
import com.phantomhive.exil.hellopics.R;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HiddenSecretsImagesListActivity extends AppCompatActivity implements Image_Item_ClickListener {
    TextView HiddenSecretsEmpty;
    FloatingActionButton AddToHiddenSecrets;
    RecyclerView HiddenSecretsImagesList;
    HiddenImagesDataAdapter hiddenImagesDataAdapter;

    Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
    int imgQuality = 100;
    private ActivityResultLauncher<IntentSenderRequest> intentSenderLauncher;
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {


                    if (result.getResultCode() == 78) {

                        Intent intent = result.getData();
                        if (intent != null) {

                            ArrayList<String> bitmapPathList = intent.getStringArrayListExtra("BITMAP_LIST");
                            if (bitmapPathList != null) {
                                int totalImages = bitmapPathList.size();

                                for (int i = 0; i < totalImages; i++) {
                                    hideImage(bitmapPathList.get(i), HiddenSecretsImagesListActivity.this);
                                    deleteImage(bitmapPathList.get(i));


                                }

                            }

                         }
                    }
                }
            });
    AlertDialog.Builder erroor;
    AlertDialog.Builder momo;
    ActivityResultLauncher<String> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            isGranted->{
                if (isGranted){
                    Intent Gallery = new Intent(HiddenSecretsImagesListActivity.this, ChooseImageArtActivity.class);
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
                    erroor = new AlertDialog.Builder(HiddenSecretsImagesListActivity.this);
                    erroor.setTitle("Notice");
                    erroor.setMessage("We need access to the files to get Pictures");
                    erroor.setCancelable(false);
                    erroor.setPositiveButton(Html.fromHtml("<font color='#A6A061'>Ok</font>", HtmlCompat.FROM_HTML_MODE_LEGACY), new DialogInterface.OnClickListener() {
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


                    erroor.setNegativeButton(Html.fromHtml("<font color='#A6A061'>No</font>", HtmlCompat.FROM_HTML_MODE_LEGACY), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finishAffinity();
                            System.exit(0);
                        }
                    });
                    AlertDialog dialog = erroor.create();
                    dialog.show();
                }

            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_secrets_images_list);
        EdgeToEdgeFixing(R.id.activity_hidden_secrets_images_listL,this);

        HiddenSecretsEmpty = findViewById(R.id.HiddenSecretsEmpty);
        HiddenSecretsImagesList = findViewById(R.id.HiddenSecretsImagesList);
        AddToHiddenSecrets = findViewById(R.id.AddToHiddenSecrets);

        if (getHiddenImages(this).isEmpty()) {
            HiddenSecretsEmpty.setVisibility(View.VISIBLE);
            HiddenSecretsImagesList.setVisibility(View.GONE);
        } else {
            HiddenSecretsEmpty.setVisibility(View.GONE);
            HiddenSecretsImagesList.setVisibility(View.VISIBLE);

            hiddenImagesDataAdapter = new HiddenImagesDataAdapter(getHiddenImages(this), this, this);
            HiddenSecretsImagesList.setLayoutManager(new GridLayoutManager(this, 3));
            HiddenSecretsImagesList.setAdapter(hiddenImagesDataAdapter);
        }

        AddToHiddenSecrets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               tackPermission();
            }
        });

        intentSenderLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Toast.makeText(this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Image deletion canceled or failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
                //in 10 android or billow
            } else {
                boolean showRationale;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
                    showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // API 29 - 32
                    showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                } else { // API 28 and below
                    showRationale = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                momo = new AlertDialog.Builder(HiddenSecretsImagesListActivity.this);
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


    public void hideImage(String imagePath, Context context) {
        File srcFile = new File(imagePath);
        if (!srcFile.exists()) return; // Skip if file doesn't exist

        File hiddenDir = new File(context.getFilesDir(), "HiddenSecretsHelloPics"); // Private folder
        if (!hiddenDir.exists()) hiddenDir.mkdirs(); // Create if not exists

        File destFile = new File(hiddenDir, srcFile.getName());

        try {
            Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            srcFile.delete(); // Remove from gallery


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean hideImageUsingORG(String imagePath, Context context) {
        File originalFile = new File(imagePath);
        if (!originalFile.exists()) return false; // Check if the file exists

        File hiddenDir = new File(context.getFilesDir(), "HiddenSecretsHelloPics");
        if (!hiddenDir.exists()) hiddenDir.mkdirs(); // Create hidden folder if not exists

        // Encode original path into filename (replace slashes with underscores)
        String encodedName = imagePath.replace("/", "_");
        File hiddenFile = new File(hiddenDir, encodedName);

        try {
            Files.copy(originalFile.toPath(), hiddenFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            originalFile.delete(); // Delete original after hiding
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String> getHiddenImages(Context context) {
        File hiddenDir = new File(context.getFilesDir(), "HiddenSecretsHelloPics"); // Private folder
        ArrayList<String> imagePaths = new ArrayList<>();

        if (hiddenDir.exists() && hiddenDir.isDirectory()) {
            File[] files = hiddenDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        imagePaths.add(file.getAbsolutePath()); // Store image path
                    }
                }
            }
        }
        return imagePaths; // Return list of hidden image paths
    }



    // Delete an image based on its file path
    private void deleteImage(String filePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Use MediaStore.createDeleteRequest for Android 11 and above
            deleteImageWithRequest(filePath);
        } else {
            // Use the traditional approach for Android 10 and below
            Uri uri = getImageUriFromPath(filePath);
            if (uri != null) {
                try {
                    int rowsDeleted = getContentResolver().delete(uri, null, null);
                    if (rowsDeleted > 0) {
                        Toast.makeText(this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to delete image", Toast.LENGTH_SHORT).show();
                    }
                } catch (SecurityException e) {
                    Toast.makeText(this, "Permission denied: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                File file = new File(filePath);
                if (file.exists() && file.delete()) {
                    Toast.makeText(this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to delete image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Delete an image for Android 11 and above using MediaStore.createDeleteRequest

    private void deleteImageWithRequest(String filePath) {
        Uri uri = getImageUriFromPath(filePath);

        if (uri != null) {
            List<Uri> uriList = Collections.singletonList(uri);

            try {
                PendingIntent deleteRequest = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    deleteRequest = MediaStore.createDeleteRequest(getContentResolver(), uriList);
                }

                assert deleteRequest != null;
                IntentSenderRequest request = new IntentSenderRequest.Builder(deleteRequest.getIntentSender()).build();
                intentSenderLauncher.launch(request);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Delete request failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "File not found in MediaStore", Toast.LENGTH_SHORT).show();
        }
    }

    // Convert a file path to a URI in the MediaStore
    private Uri getImageUriFromPath(String filePath) {
        String[] projection = {MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.DATA + "=?";
        String[] selectionArgs = new String[]{filePath};

        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(queryUri, projection, selection, selectionArgs, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                cursor.close();
                return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            }
            cursor.close();
        }
        return null;
    }

    // Handle the result of the delete request
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Image deletion canceled or denied by user", Toast.LENGTH_SHORT).show();
            }
        }



    }



    public void DeleteFile(String filePath) {
        try {
            File file = new File(filePath);
            ContentResolver contentResolver = getContentResolver();

            // Get URI for the file using MediaStore
            String where = MediaStore.MediaColumns.DATA + "=?";
            String[] selectionArgs = new String[] { file.getAbsolutePath() };
            Uri filesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            // Try to delete using ContentResolver
            int deleted = contentResolver.delete(filesUri, where, selectionArgs);

            if (deleted > 0) {
                // Successfully deleted
                Toast.makeText(HiddenSecretsImagesListActivity.this, "Image deleted", Toast.LENGTH_SHORT).show();
                // Refresh media store
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            } else {
                // If MediaStore delete failed, try direct deletion
                if (file.exists() && file.delete()) {
                    Toast.makeText(HiddenSecretsImagesListActivity.this, "Image deleted", Toast.LENGTH_SHORT).show();
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                } else {
                    Toast.makeText(HiddenSecretsImagesListActivity.this, "Failed to delete image", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "Error deleting file: " + e.getMessage());
            Toast.makeText(HiddenSecretsImagesListActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void OnChoosingTheMainPictureClick(String picturePath, Context PackageContext) {

    }

    @Override
    public void OnChoosingTheOneTimePictureClick(String picturePath, Context PackageContext) {

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
        Intent AllImages = new Intent(HiddenSecretsImagesListActivity.this, All_images_Activity.class);
        AllImages.putExtra("USE_TYPE", Gallery_UseType.MULTIPLE_SELECT);
        activityResultLauncher.launch(AllImages);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!getHiddenImages(this).isEmpty() && hiddenImagesDataAdapter != null) {
//            // If adapter is already initialized, just update the data
//            hiddenImagesDataAdapter.updateData(getHiddenImages(this));
//        }

        if (getHiddenImages(this).isEmpty()) {
            HiddenSecretsEmpty.setVisibility(View.VISIBLE);
            HiddenSecretsImagesList.setVisibility(View.GONE);
        } else {
            HiddenSecretsEmpty.setVisibility(View.GONE);
            HiddenSecretsImagesList.setVisibility(View.VISIBLE);

            hiddenImagesDataAdapter = new HiddenImagesDataAdapter(getHiddenImages(this), this, this);
            HiddenSecretsImagesList.setLayoutManager(new GridLayoutManager(this, 3));
            HiddenSecretsImagesList.setAdapter(hiddenImagesDataAdapter);
        }
    }
}
