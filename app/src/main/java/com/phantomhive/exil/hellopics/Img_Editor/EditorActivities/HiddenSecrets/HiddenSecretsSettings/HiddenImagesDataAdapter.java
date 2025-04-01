package com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.HiddenSecrets.HiddenSecretsSettings;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class HiddenImagesDataAdapter extends RecyclerView.Adapter<HiddenImagesDataAdapter.myViewHolder> {

    ArrayList<String> mdata;
    Image_Item_ClickListener image_item_clickListener;
    Context context;


    public HiddenImagesDataAdapter(ArrayList<String> mdata, Image_Item_ClickListener image_item_clickListener, Context context) {
        this.mdata = mdata;
        this.context = context;
        this.image_item_clickListener = image_item_clickListener;
    }

    @NonNull
    @Override
    public HiddenImagesDataAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hiddensecrets_item,
                parent, false);

        return new HiddenImagesDataAdapter.myViewHolder(view);
    }


    // Add this method to update the entire dataset
    public void updateData(ArrayList<String> newData) {
        this.mdata = newData;
        notifyDataSetChanged();
    }

    // Method to delete an item from the list
    public void deleteItem(int position) {
        mdata.remove(position); // Remove the item from the data source
        notifyItemRemoved(position); // Notify the adapter that the item was removed
        notifyItemRangeChanged(position, mdata.size()); // Notify that items might have shifted
    }
    // Add this method to remove a single item
    public void removeItem(int position) {
        if (position >= 0 && position < mdata.size()) {
            mdata.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mdata.size());
        }
    }
    @Override
    public void onBindViewHolder(@NonNull HiddenImagesDataAdapter.myViewHolder holder, int position) {
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
                Editor.putExtra("imgname", mdata.get(position));
                image.setBitmap(bitmap[0]);
                image.setOriginalBitmap(bitmap[0]);
                context.startActivity(Editor);
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

        holder.imgRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isDeleted = restoreImage(mdata.get(position));
                if (isDeleted) {
                    removeItem(position);
                    Toast.makeText(context, "Restored successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to Restore", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }


    public boolean deleteHiddenImage(String imagePath, Context context) {
        File file = new File(imagePath);
        if (file.exists()) {
            return file.delete(); // Delete the file and return true if successful
        }
        return false; // Return false if the file doesn't exist
    }
    public void deleteAllHiddenImages(Context context) {
        File hiddenDir = new File(context.getFilesDir(), "HiddenSecretsHelloPics");
        if (hiddenDir.exists() && hiddenDir.isDirectory()) {
            File[] files = hiddenDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete(); // Delete each file
                }
            }
        }
    }
    
    

    public boolean restoreImage(String hiddenImagePath) {
        String restoreDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File hiddenFile = new File(hiddenImagePath);
        if (!hiddenFile.exists()) return false; // If the file doesn't exist, return false

        File restoreDir = new File(restoreDirPath);
        if (!restoreDir.exists()) restoreDir.mkdirs(); // Create restore directory if needed

        File restoredFile = new File(restoreDir, hiddenFile.getName()); // Restore with the same filename

        try {
            Files.copy(hiddenFile.toPath(), restoredFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            hiddenFile.delete(); // Delete from hidden folder after restoring
            return true; // Successfully restored
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Restore failed
        }
    }

    public void restoreAllHiddenImages(Context context, String restoreDirPath) {
        File hiddenDir = new File(context.getFilesDir(), "HiddenSecretsHelloPics");
        if (!hiddenDir.exists() || !hiddenDir.isDirectory()) return; // No hidden images found

        File restoreDir = new File(restoreDirPath);
        if (!restoreDir.exists()) restoreDir.mkdirs(); // Create restore directory if needed

        File[] files = hiddenDir.listFiles();
        if (files != null) {
            for (File file : files) {
                restoreImage(file.getAbsolutePath()); // Restore each image
            }
        }
    }



    public boolean restoreImageToOriginalPlace(String hiddenImagePath, Context context) {
        File hiddenFile = new File(hiddenImagePath);
        if (!hiddenFile.exists()) return false; // File not found

        // Decode original path from filename (replace underscores back to slashes)
        String originalPath = hiddenFile.getName().replace("_", "/");

        File restoredFile = new File(originalPath);
        File parentDir = restoredFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) parentDir.mkdirs(); // Ensure parent folder exists

        try {
            Files.copy(hiddenFile.toPath(), restoredFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            hiddenFile.delete(); // Remove from hidden folder after restoring
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        ImageView imgp;
        ImageButton imgB;
        ImageButton imgRestore;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imgp = itemView.findViewById(R.id.imagesView_Hiddenimg);
            imgB = itemView.findViewById(R.id.deletehiddenimg);
            imgRestore = itemView.findViewById(R.id.Restoreimg);

        }
    }


}
