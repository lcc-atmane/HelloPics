package com.phantomhive.exil.hellopics.Create_Art_Classes.Fragments_Shoser_F_allimg;

import static com.phantomhive.exil.hellopics.Create_Art_Classes.GalleryAdapters.AllimagesAdapter.SIMGN;
import static com.phantomhive.exil.hellopics.EdgetoEdgeFix.EdgeToEdgeFix.EdgeToEdgeFixing;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.phantomhive.exil.hellopics.Create_Art_Classes.GalleryAdapters.AllimagesAdapter;
import com.phantomhive.exil.hellopics.Create_Art_Classes.GalleryAdapters.FoldersAdapter;

import com.phantomhive.exil.hellopics.Create_Art_Classes.Image_Item_ClickListener;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Gallery_picture_data.Folders_Data;
import com.phantomhive.exil.hellopics.Create_Art_Classes.Gallery_picture_data.Images_data;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.R;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

// Same as All_Folders_Fragment
public class All_images_Activity extends AppCompatActivity implements Image_Item_ClickListener, AllimagesAdapter.OnAddImages {
    RecyclerView ImagesList;
    AllimagesAdapter allImages_adapter;
    ArrayList<Images_data> images;
    String UseType = Gallery_UseType.CHOOSE_MAIN_IMG;
    ConstraintLayout constraintLayout;
    public ImageButton DoneSelect,CancelSelect;
    public RecyclerView AddedImagesRecyclerView;
    AddedImagesListAdapter addedImagesListAdapter;
    ArrayList<String> AddedImages = new ArrayList<>();

    // RecyclerView for the images folders.
    RecyclerView folders_recycle_view;
    // Adapter of images Folders.
    FoldersAdapter folders_adapter;
    //list for getting Folder path (files).
    ArrayList<Folders_Data> folders_data;

    String folderPath = null;
    String folderName;

    TextView textView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_imagas_);
        EdgeToEdgeFixing(R.id.All_images_ActivityL,this);


        if (getIntent().getStringExtra("USE_TYPE") != null){
            UseType = getIntent().getStringExtra("USE_TYPE");
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SIMGN = 0;
                Intent resultIntent = new Intent();
                setResult(78, resultIntent);
                finish();
            }
        });

        //CollapsingToolbarLayout tableLayout = findViewById(R.id.AlbumscollapsingToolbarLayout);
        ImagesList = findViewById(R.id.Imageslist);
        constraintLayout = findViewById(R.id.AddImagesOption);
        DoneSelect = findViewById(R.id.DoneSelectAddImages);
        CancelSelect = findViewById(R.id.CancelSelectAddImages);
        AddedImagesRecyclerView = findViewById(R.id.AddedImageList);

        textView = findViewById(R.id.foldername);

        folders_recycle_view = findViewById(R.id.all_Folders_recycle);

        if (folderPath == null){
            images = getAllImagesByFolder();
        }else {
            images = getAllImagesByFolder(folderPath);
        }


        if(getfolders().isEmpty()){
            //wen list is empty.
            Toast.makeText(All_images_Activity.this, "No Images", Toast.LENGTH_SHORT).show();;
        }else {
            folders_data = getfolders();
            //set adapter to recyclerView.
            folders_adapter=new FoldersAdapter(this,UseType,folders_data,this);
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(All_images_Activity.this, LinearLayoutManager.HORIZONTAL, false);
            folders_recycle_view.setLayoutManager(horizontalLayoutManager);
            folders_recycle_view.setAdapter(folders_adapter);
        }

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(All_images_Activity.this, LinearLayoutManager.HORIZONTAL, false);
        AddedImagesRecyclerView.setLayoutManager(horizontalLayoutManager);

        if (!AddedImages.isEmpty()){
            addedImagesListAdapter = new AddedImagesListAdapter(this,AddedImages);
            AddedImagesRecyclerView.setAdapter(addedImagesListAdapter);
        }
        refreshImagesList(getfolders().get(0).getPath(),getfolders().get(0).getFolderName());

        // set the folder files to list.



    }


    public ArrayList<Images_data> getAllImagesByFolder(){
        ArrayList<Images_data> images = new ArrayList<>();
        Uri allVideosuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = getContentResolver().query( allVideosuri, projection, null,null, null);

        try {
            assert cursor != null;
            cursor.moveToFirst();
            do{
                Images_data pic = new Images_data();
                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                images.add(pic);
            }while(cursor.moveToNext());
            cursor.close();
            ArrayList<Images_data> reSelection = new ArrayList<>();
            for(int i = images.size()-1;i > -1;i--){
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    public ArrayList<Images_data> getAllImagesByFolder(String path){
        ArrayList<Images_data> images = new ArrayList<>();
        Uri allVideosuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = All_images_Activity.this.getContentResolver().query( allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[] {"%"+path+"%"}, null);

        try {
            assert cursor != null;
            cursor.moveToFirst();
            do{
                Images_data pic = new Images_data();
                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                images.add(pic);
            }while(cursor.moveToNext());
            cursor.close();
            ArrayList<Images_data> reSelection = new ArrayList<>();
            for(int i = images.size()-1;i > -1;i--){
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    // from https://medium.com/codex/android-simple-image-gallery-30c0f00abe64.
    public ArrayList<Folders_Data> getfolders(){
        ArrayList<Folders_Data> picFolders = new ArrayList<>();

        String[] projection0 = {MediaStore.Images.Media._ID};
        Cursor cursor0 = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection0,
                null,
                null,
                null
        );

        int numberOfImages = cursor0 != null ? cursor0.getCount() : 0;
        if (cursor0 != null) {
            cursor0.close();
        }

// Get all images first
// picFolders.add(new Folders_Data(null,"All",numberOfImages,getAllImagesByFolder().get(0).getPicturePath()));
        ArrayList<Images_data> allImages = getAllImagesByFolder();

        // Only add "All" folder if there are images
        if (!allImages.isEmpty()) {
            picFolders.add(new Folders_Data(null, "All", numberOfImages, allImages.get(0).getPicturePath()));
        } else {
            // Add "All" folder without a path image
            picFolders.add(new Folders_Data(null, "All", 0, null));
        }

        ArrayList<String> picPaths = new ArrayList<>();
        Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor = getContentResolver().query(allImagesuri, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do{
                Folders_Data folds = new Folders_Data();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                //String folderpaths =  datapath.replace(name,"");
                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder+"/"));
                folderpaths = folderpaths+folder+"/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);

                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                    folds.addpics();
                    picFolders.add(folds);
                }else{
                    for(int i = 1;i<picFolders.size();i++){
                        if(picFolders.get(i).getPath().equals(folderpaths)){
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            }while(cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 1;i < picFolders.size();i++){
            Log.d("picture folders",picFolders.get(i).getFolderName()+" and path = "+picFolders.get(i).getPath()+" "
                    +picFolders.get(i).getNumberOfPics());
        }
        return picFolders;
    }


    public boolean isBitmapTooLarge(Bitmap bitmap) {
        int maxTextureSize = getMaxTextureSize();
        Log.d("TAG", "isBitmapTooLarge: "+getMaxTextureSize());
        return bitmap.getWidth() > maxTextureSize || bitmap.getHeight() > maxTextureSize;
    }
    public int getMaxTextureSize() {
        int[] maxTextureSize = new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
        return maxTextureSize[0];
    }

    public static int calculateSampleSize(
            int originalWidth, int originalHeight, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        if (originalHeight > reqHeight || originalWidth > reqWidth) {
            final int halfHeight = originalHeight / 2;
            final int halfWidth = originalWidth / 2;

            // Calculate the largest sampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / sampleSize) >= reqHeight
                    && (halfWidth / sampleSize) >= reqWidth) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

    public Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap,
                                                int reqWidth, int reqHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int sampleSize = calculateSampleSize(width, height, reqWidth, reqHeight);

        if (sampleSize > 1) {
            int newWidth = width / sampleSize;
            int newHeight = height / sampleSize;
            return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        }
        return bitmap;
    }
    @Override
    public void OnChoosingTheMainPictureClick(String picturePath, Context PackageContext) {
           setMainPictureAndStartEditor(picturePath, PackageContext);
    }

    @Override
    public void OnChoosingTheOneTimePictureClick(String picturePath, Context PackageContext) {
        setVisibilityToAddOption(false);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("BITMAP", picturePath);
        setResult(78, resultIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setMainPictureAndStartEditor(String picturePath, Context PackageContext){
        setVisibilityToAddOption(false);
        Image image;
        image = Image.getInstance();
        Intent Editor = new Intent(PackageContext, Editor_main_Activity.class);
        Editor.putExtra("folderPath",picturePath);
        Glide.with(PackageContext)
                .asBitmap()
                .load(picturePath)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        // Get screen dimensions
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        WindowManager windowManager = (WindowManager) All_images_Activity.this.getSystemService(Context.WINDOW_SERVICE);
                        if (windowManager != null) {
                            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
                        }
                        int screenWidth = displayMetrics.widthPixels;
                        int screenHeight = displayMetrics.heightPixels;



                        image.setBitmap(decodeSampledBitmapFromBitmap(resource,screenWidth,screenHeight));
                        image.setOriginalBitmap(decodeSampledBitmapFromBitmap(resource,screenWidth,screenHeight));
                        startActivity(Editor);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void setVisibilityToAddOption(boolean b) {
        if (b){
            constraintLayout.setVisibility(View.VISIBLE);
        }else
            constraintLayout.setVisibility(View.GONE);
    }

    public void setInfo(String useType) {
        this.UseType = useType;

    }


    public void refreshImagesList(String folderPath, String folderName){
        this.folderPath = folderPath;
        this.folderName = folderName;
        textView.setText(folderName);

        if (folderPath == null){
            images = getAllImagesByFolder();
        }else {
            images = getAllImagesByFolder(folderPath);
        }

        if(images.isEmpty()){
            Toast.makeText(All_images_Activity.this, "empty folder", Toast.LENGTH_SHORT).show();
        }else {
            allImages_adapter = new AllimagesAdapter(this,
                    UseType,images,All_images_Activity.this,
                    this);
            ImagesList.setAdapter(allImages_adapter);
            ImagesList.setLayoutManager(new GridLayoutManager(this,3));
            allImages_adapter.setOnAddImages(this);
        }

    }

    @Override
    public void AddImages(String imgPath) {
        setVisibilityToAddOption(true);
        AddedImages.add(imgPath);
        addedImagesListAdapter = new AddedImagesListAdapter(this,AddedImages);
        AddedImagesRecyclerView.setAdapter(addedImagesListAdapter);
    }

    @Override
    public void DoneAdding(ArrayList<String> imgPaths) {
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("BITMAP_LIST", imgPaths);
        setResult(78, resultIntent);
        finish();
       imgPaths.clear();
    }

    @Override
    public void CancelAdding(String useType,ArrayList<String> imgList) {
        SIMGN = 0;
        imgList.clear();
        if (useType.equals(Gallery_UseType.MULTIPLE_SELECT)){
            Intent AddPhotoActivity = new Intent(this, com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.AddPhotoActivity.class);
            finish();
            startActivity(AddPhotoActivity);
        }
    }


    public class AddedImagesListAdapter extends RecyclerView.Adapter<AddedImagesListAdapter.MyviewHoder> {
        Context context;
        ArrayList<String> addedImages;

        public AddedImagesListAdapter(Context context, ArrayList<String> addedImages) {
            this.context = context;
            this.addedImages = addedImages;
        }

        public void updateItemColor( String img) {
            addedImages.add(img);
            notifyDataSetChanged();

        }
        @NonNull
        @Override
        public AddedImagesListAdapter.MyviewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View AddedImages = inflater.inflate(R.layout.addedimageslist_item, parent, false);
            return new AddedImagesListAdapter.MyviewHoder(AddedImages);
        }

        @Override
        public void onBindViewHolder(@NonNull AddedImagesListAdapter.MyviewHoder holder, int position) {
            Glide.with(context).asBitmap().load(addedImages.get(position)).apply(new RequestOptions().centerCrop()).into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        SIMGN -= 1;
                        addedImages.remove(position);
                        AllimagesAdapter.bitmapList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, addedImages.size());
                }
            });
        }

        @Override
        public int getItemCount() {
            return addedImages.size();
        }

        public class MyviewHoder extends RecyclerView.ViewHolder {
            ImageView imageView;
            public MyviewHoder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.AddedImage);
            }
        }
    }
}