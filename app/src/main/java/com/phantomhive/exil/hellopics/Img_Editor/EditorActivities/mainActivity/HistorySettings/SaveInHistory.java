package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.HistorySettings;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg.All_images_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveInHistory {
    public static void SaveImageForHistory(Bitmap finalBitmap, Context context, String name) {
        // Define the hidden directory
        File hiddenDir = new File(context.getFilesDir(), "TheHistoryAlbum");
        if (!hiddenDir.exists()) hiddenDir.mkdirs(); // Create if not exists

        // Generate the file name
        String fileName = name;
        File file = new File(hiddenDir, fileName);

        if (file.exists()) {
            Toast.makeText(context, "This name is already used", Toast.LENGTH_SHORT).show();
        }

        try (FileOutputStream out = new FileOutputStream(file)) {
            // Use the same compression format and quality
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Notify the system that a new file has been created
        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("HiddenStorage", "Scanned " + path + ":");
                        Log.i("HiddenStorage", "-> uri=" + uri);
                        new Handler(Looper.getMainLooper()).post(() ->
                                Toast.makeText(context, "Image hidden successfully",
                                        Toast.LENGTH_SHORT).show());
                    }
                });

        HistorySqliteDataBase dbHelper;
        dbHelper = new HistorySqliteDataBase(context);
        // Insert Sample Data
        boolean inserted = dbHelper.insertData(
                file.getAbsolutePath(),
                name);
        if (inserted) {
            Toast.makeText(context, "Data Inserted", Toast.LENGTH_SHORT).show();
        }
    }

    public static void deleteAllHistoryImages(Context context) {
        // Get the directory path
        File hiddenDir = new File(context.getFilesDir(), "TheHistoryAlbum");

        if (hiddenDir.exists() && hiddenDir.isDirectory()) {
            File[] files = hiddenDir.listFiles(); // Get all files in the directory

            if (files != null) {
                for (File file : files) {
                    file.delete(); // Delete each file
                }
            }
        }

        HistorySqliteDataBase dbHelper;
        dbHelper = new HistorySqliteDataBase(context);
        // Insert Sample Data
        boolean inserted = dbHelper.deleteAllData();
        if (inserted) {
            Toast.makeText(context, "Data Deleted", Toast.LENGTH_SHORT).show();
        }
        // Notify system that files are removed
        MediaScannerConnection.scanFile(context, new String[]{hiddenDir.toString()}, null,
                (path, uri) -> Log.i("HiddenStorage", "Deleted files in " + path));

        Toast.makeText(context, "All history images deleted", Toast.LENGTH_SHORT).show();
    }

}
