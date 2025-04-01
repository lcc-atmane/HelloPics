package com.phantomhive.exil.hellopics.Create_Art_Classes.GalleryAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg.All_images_Activity;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Gallery_picture_data.Folders_Data;
import com.phantomhive.exil.hellopics.R;

import java.util.ArrayList;

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.Myviewhoder> {

    ArrayList<Folders_Data>folders_data;
    Context context;
    String useType;
    All_images_Activity all_images_activity;
    public FoldersAdapter(All_images_Activity  all_images_adapter, String useType, ArrayList<Folders_Data> folders_data, Context context) {
        this.all_images_activity = all_images_adapter;
        this.folders_data = folders_data;
        this.context = context;
        this.useType = useType;

    }

    @NonNull
    @Override
    public Myviewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View gallery_intro = inflater.inflate(R.layout.folders_intro, parent, false);
        return new Myviewhoder(gallery_intro);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewhoder holder, int position) {
       Folders_Data  folder = folders_data.get(position);
        Glide.with(context)
                .load(folder.getFirstPic())
                .apply(new RequestOptions().centerCrop())
                .into(holder.folders_img);

        int Nfolders = folder.getNumberOfPics();
        String NameFolder = folder.getFolderName();
        holder.Text_folder_Nimg.setText(String.valueOf(Nfolders));
        holder.text_folder_name.setText(NameFolder);
        holder.folders_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_images_activity.refreshImagesList(folder.getPath(),folder.getFolderName());

                //move.putExtra("recyclerItemSize",getCardsOptimalWidth(4));
                // move.putExtra("folderPath",folder.getPath());
                //                move.putExtra("folderName",folder.getFolderName());
                //                move.putExtra("useType",useType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders_data.size();
    }

    public class Myviewhoder extends RecyclerView.ViewHolder {
        ImageView folders_img;
        TextView text_folder_name;
        TextView Text_folder_Nimg;
        public Myviewhoder(@NonNull View itemView) {
            super(itemView);
            folders_img = itemView.findViewById(R.id.image_folder);
            text_folder_name = itemView.findViewById(R.id.folder_name);
            Text_folder_Nimg = itemView.findViewById(R.id.folder_N_imgs);
        }
    }
}
