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
package com.phantomhive.exil.hellopics.ui_Home.EditedImages;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phantomhive.exil.hellopics.Create_Art_Classes.Image_Item_ClickListener;
import com.phantomhive.exil.hellopics.R;
import com.phantomhive.exil.hellopics.ui_Home.User_Data_Account.imgDataAdapter;

import java.io.File;
import java.util.ArrayList;

public class AccountFragment extends Fragment implements Image_Item_ClickListener {

    RecyclerView editedImgsrecyclerView;
    imgDataAdapter imgDataAdapter;

    TextView EmptyText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        editedImgsrecyclerView = root.findViewById(R.id.Edited_img);
        EmptyText = root.findViewById(R.id.EmptyText);





        if (getHiddenImages(requireActivity()).isEmpty()) {
            EmptyText.setVisibility(View.VISIBLE);

        } else {
            EmptyText.setVisibility(View.GONE);
            imgDataAdapter = new imgDataAdapter(getHiddenImages(requireActivity()), AccountFragment.this, getActivity());
            editedImgsrecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            editedImgsrecyclerView.setAdapter(imgDataAdapter);
        }

        return root;
    }



    @Override
    public void OnChoosingTheMainPictureClick(String picturePath, Context PackageContext) {

    }

    @Override
    public void OnChoosingTheOneTimePictureClick(String picturePath, Context PackageContext) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public ArrayList<String> getHiddenImages(Context context) {
        File hiddenDir = new File(context.getFilesDir(), "TheInsideAlbumHelloPics"); // Private folder
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

    @Override
    public void onResume() {
        super.onResume();
        if (getHiddenImages(requireActivity()).isEmpty()) {
            EmptyText.setVisibility(View.VISIBLE);
        } else {
            EmptyText.setVisibility(View.GONE);
            imgDataAdapter = new imgDataAdapter(getHiddenImages(requireActivity()), AccountFragment.this, getActivity());
            editedImgsrecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            editedImgsrecyclerView.setAdapter(imgDataAdapter);
        }
    }

    /*
     @Entity(tableName = "edited_imgs")
    public class EditedImgs {
        @PrimaryKey(autoGenerate = true)
        private int id;

        private String imgName;

        @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
        private byte[] imgBlob;

        // Constructor, getters, and setters...

        public EditedImgs(int id, String imgName, byte[] imgBlob) {
            this.id = id;
            this.imgName = imgName;
            this.imgBlob = imgBlob;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgName() {
            return imgName;
        }

        public void setImgName(String imgName) {
            this.imgName = imgName;
        }

        public byte[] getImgBlob() {
            return imgBlob;
        }

        public void setImgBlob(byte[] imgBlob) {
            this.imgBlob = imgBlob;
        }
    }

    @Dao
    public interface EditedImgsDao {
        @Query("SELECT * FROM edited_imgs")
        List<EditedImgs> getAllEditedImgs();
    }
     */

}