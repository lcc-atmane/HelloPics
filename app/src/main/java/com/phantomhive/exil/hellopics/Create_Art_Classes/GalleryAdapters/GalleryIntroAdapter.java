package com.phantomhive.exil.hellopics.Create_Art_Classes.GalleryAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Image_Item_ClickListener;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Gallery_picture_data.Images_data;
import com.phantomhive.exil.hellopics.R;

import java.util.ArrayList;

public class GalleryIntroAdapter extends RecyclerView.Adapter<GalleryIntroAdapter.MyviewHoder> {

    ArrayList<Images_data> images_data;
    Context context;

    public GalleryIntroAdapter(ArrayList<Images_data> images_data, Context context, Image_Item_ClickListener image_item_clickListener) {
        this.images_data = images_data;
        this.context = context;
        this.image_item_clickListener = image_item_clickListener;
    }

    Image_Item_ClickListener image_item_clickListener;
      int Limit = 20;

    @NonNull
    @Override
    public MyviewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View gallery_intro = inflater.inflate(R.layout.galley_intro, parent, false);
        return new MyviewHoder(gallery_intro);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHoder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context)
                .asBitmap()
                .load(images_data.get(position).getPicturePath())
                .centerCrop()
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_item_clickListener.OnChoosingTheMainPictureClick(images_data.get(position).getPicturePath(),context);

            }
        });



    }

    @Override
    public int getItemCount() {
        if (images_data.size()<=Limit){
            return images_data.size();
        }else{
            return Limit;
        }

    }

    public class MyviewHoder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyviewHoder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagesView_intro);
        }
    }

}
