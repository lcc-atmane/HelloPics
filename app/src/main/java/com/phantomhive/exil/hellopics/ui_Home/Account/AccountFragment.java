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
package com.phantomhive.exil.hellopics.ui_Home.Account;

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