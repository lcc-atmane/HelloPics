package com.phantomhive.exil.hellopics.Create_Art_Classes.GalleryAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg.All_images_Activity;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg.Gallery_UseType;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Image_Item_ClickListener;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Gallery_picture_data.Images_data;
import com.phantomhive.exil.hellopics.R;

import java.util.ArrayList;

public class AllimagesAdapter extends RecyclerView.Adapter<AllimagesAdapter.MyviewHoder> {


    public static int SIMGN = 0;
    ArrayList<Images_data> images_data ;
    Context context;
    Image_Item_ClickListener image_item_clickListener;
    String useType;
    public static ArrayList<String> bitmapList = new ArrayList<>(); ;
    All_images_Activity all_images_activity;

    OnAddImages onAddImages;
    public AllimagesAdapter(All_images_Activity all_images_activity , String useType, ArrayList<Images_data> images_data,
                            Context context, Image_Item_ClickListener image_item_clickListener) {
        this.all_images_activity = all_images_activity;
        this.images_data = images_data;
        this.context = context;
        this.image_item_clickListener = image_item_clickListener;
        this.useType = useType;

    }

    public AllimagesAdapter() {
    }

    @NonNull
    @Override
    public AllimagesAdapter.MyviewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View gallery_intro = inflater.inflate(R.layout.galley_intro, parent, false);
        return new AllimagesAdapter.MyviewHoder(gallery_intro);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHoder holder, int position) {
        Glide.with(context).asBitmap().load(images_data.get(position).getPicturePath()).apply(new RequestOptions().centerCrop()).into(holder.imageView);

        switch (useType) {

            case Gallery_UseType.CHOOSE_MAIN_IMG :

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (image_item_clickListener!=null){
                                image_item_clickListener.OnChoosingTheMainPictureClick(images_data.get(position).getPicturePath(), context);
                            }
                        }
                    });
                break;
            case Gallery_UseType.ONE_SELECT:

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (image_item_clickListener!=null){
                            image_item_clickListener.OnChoosingTheOneTimePictureClick(images_data.get(position).getPicturePath(), context);
                        }
                    }
                });
                break;
            case Gallery_UseType.MULTIPLE_SELECT:

                all_images_activity.DoneSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!bitmapList.isEmpty()){
                            Intent resultIntent = new Intent();
                            resultIntent.putStringArrayListExtra("BITMAP_LIST", bitmapList);
                            all_images_activity.setResult(78, resultIntent);
                            all_images_activity.finish();
                            bitmapList.clear();
                        }else {
                            CancelSelect();
                        }

                        //DoneSelect(context);
                    }
                });

                all_images_activity.CancelSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CancelSelect();
                    }
                });

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SIMGN < 7) {
                            SIMGN++;
                            bitmapList.add(images_data.get(position).getPicturePath());
                            if (onAddImages!=null){
                                onAddImages.AddImages(images_data.get(position).getPicturePath());
                            }
                        } else {
                            Toast.makeText(context, "The limit is 7 Image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }

    }



    @Override
    public int getItemCount() {
        return images_data.size();
    }

    public class MyviewHoder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyviewHoder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagesView_intro);
        }
    }

    public void CancelSelect(){
        SIMGN = 0;
        bitmapList.clear();
        if (useType.equals(Gallery_UseType.MULTIPLE_SELECT)){
            Intent resultIntent = new Intent();
            all_images_activity.setResult(78, resultIntent);
            all_images_activity.finish();
        }
    }

    public interface OnAddImages {
        void AddImages(String imgPath);
        void DoneAdding(ArrayList<String> imgPaths);
        void CancelAdding(String useType,ArrayList<String> imgList);
    }
    public void setOnAddImages(OnAddImages onAdd_Images){
        onAddImages = onAdd_Images;
    }
}
    /*
     if (resource.getWidth()>resource.getHeight()| resource.getWidth()==resource.getHeight()){
                                                 Fresource = Bitmap.createScaledBitmap(resource,
                                                         ScaleTools.addImagesImageView().getWidth() ,FH ,true);
                                             }else if (resource.getWidth()<resource.getHeight() ){
                                             if (FH<ScaleTools.addImagesImageView().getHeight()) {
                                                 Fresource = Bitmap.createScaledBitmap(resource,
                                                         ScaleTools.addImagesImageView().getWidth() ,FH ,true);
                                             }else {
                                                 Fresource = Bitmap.createScaledBitmap(resource,FW,
                                                         ScaleTools.addImagesImageView().getHeight() ,true);
                                             }
                                         }




     */
