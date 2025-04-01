package com.phantomhive.exil.hellopics.ui_Home.User_Data_Account;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Image_Item_ClickListener;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class imgDataAdapter extends RecyclerView.Adapter<imgDataAdapter.myViewHolder> {

    ArrayList<String> mdata;
    Image_Item_ClickListener image_item_clickListener;
    Context context;


    public imgDataAdapter(ArrayList<String> mdata, Image_Item_ClickListener image_item_clickListener, Context context) {
        this.mdata = mdata;
        this.context = context;
        this.image_item_clickListener = image_item_clickListener;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saveinapp_item,
                parent, false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        String text;

        final Bitmap[] bitmap = {null};
        Glide.with(context)
                .asBitmap()
                .load(mdata.get(position))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bitmap[0] = resource;
                        holder.imgp.setImageBitmap(resource);

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        holder.imgp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Image image;
                image = Image.getInstance();
                Intent Editor = new Intent(context, Editor_main_Activity.class);
//                String Imagename = font.getFontname();
//                if (Imagename.contains(".")) {
//                    String fontNameWithoutExtension = Imagename.substring(0, Imagename.lastIndexOf('.'));
//                    fontHolder.textView.setText(fontNameWithoutExtension);
//                }else {
//                    fontHolder.textView.setText(font.getFontname());
//                }
                Editor.putExtra("imgname", getFileName(mdata.get(position)));
                image.setBitmap(bitmap[0]);
                image.setOriginalBitmap(bitmap[0]);
                context.startActivity(Editor);
            }
        });

        holder.imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean result = SaveImage(mdata.get(position),context);
                if (result) {
                    Toast.makeText(context, "Image is Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.imgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDeleted = deleteHiddenImage(mdata.get(position),context);
                if (isDeleted) {
                    removeItem(position);
                    Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    // Add this method to update the entire dataset
    public void updateData(ArrayList<String> newData) {
        this.mdata = newData;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < mdata.size()) {
            mdata.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mdata.size());
        }
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        ImageView imgp;
        ImageButton imgB;
        ImageButton imgSave;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imgp = itemView.findViewById(R.id.imagesView_savedimg);
            imgB = itemView.findViewById(R.id.savedimgoption);
            imgSave = itemView.findViewById(R.id.Saveimg);
        }
    }

    public boolean deleteHiddenImage(String imagePath, Context context) {
        File file = new File(imagePath);
        if (file.exists()) {
            return file.delete(); // Delete the file and return true if successful
        }
        return false; // Return false if the file doesn't exist
    }

    public boolean SaveImage(String hiddenImagePath, Context context) {
        // Define the path to the HelloPics folder in the Pictures directory
        File restoreDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "HelloPics");

        // Check if the hidden file exists
        File hiddenFile = new File(hiddenImagePath);
        if (!hiddenFile.exists()) return false; // If the file doesn't exist, return false

        // Create the HelloPics folder if it doesn't exist
        if (!restoreDir.exists()) restoreDir.mkdirs(); // Create folder if needed

        // Define the restored file location in HelloPics
        File restoredFile = new File(restoreDir, hiddenFile.getName()); // Restore with the same filename

        try {
            // Use MediaStore if on Android 10 and above (for Scoped Storage compatibility)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, hiddenFile.getName());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/HelloPics");

                // Insert the new image into MediaStore
                Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                try (OutputStream out = context.getContentResolver().openOutputStream(uri)) {
                    if (out == null) return false;

                    // Copy the image from the hidden location to the restored location
                    try (FileInputStream fis = new FileInputStream(hiddenFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            out.write(buffer, 0, length);
                        }
                    }
                }

            } else {
                // For Android versions below Android 10
                Files.copy(hiddenFile.toPath(), restoredFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            return true; // Successfully restored
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Restore failed
        }
    }



        public String getFileName(String filePath) {
        return new File(filePath).getName();
    }

//    public boolean SaveImage(String hiddenImagePath, Context context) {
//        File hiddenFile = new File(hiddenImagePath);
//        if (!hiddenFile.exists()) return false; // Return false if file doesn't exist
//
//        // Define the HelloPics album path (public)
//        File helloPicsDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "HelloPics");
//        if (!helloPicsDir.exists()) helloPicsDir.mkdirs(); // Create folder if not exists
//
//        File destFile = new File(helloPicsDir, hiddenFile.getName()); // Destination file
//
//        try {
//            Files.copy(hiddenFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING); // Copy file
//
//            // Refresh media scanner to show the image in the gallery
//            MediaScannerConnection.scanFile(context, new String[]{destFile.getAbsolutePath()}, null, null);
//
//            return true; // Success
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false; // Failure
//        }
//    }
}