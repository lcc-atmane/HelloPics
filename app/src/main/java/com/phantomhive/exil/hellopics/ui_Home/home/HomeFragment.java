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
package com.phantomhive.exil.hellopics.ui_Home.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Editor_main_Activity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.HiddenSecrets.HiddenSecretsPasswordEnteringActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.HiddenSecrets.HiddenSecretsPasswordSettingActivity;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.mainActivity.Image;
import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.HiddenSecrets.HiddenSecretsSettings.PasswordSettings;
import com.phantomhive.exil.hellopics.ui_Home.unsplashApi.SharedViewModel;
import com.phantomhive.exil.hellopics.ui_Home.unsplashApi.imagesData.ImagesModel;
import com.phantomhive.exil.hellopics.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

    FragmentActivity m;

    ImagesModel imagesModels;

    CircleImageView RandomImageView;
    ImageButton RandomImgRefresh;
    ProgressBar RandomImgProgress;


    //HiddenSecrets
    Button StartHiddenSecrets;
    TextView LearnAboutHiddenSecrets;

    Button OpenHiddenSecrets;
    PasswordSettings passwordSettings;

    ConstraintLayout OpenHiddenSecretsLayout,StartHiddenSecretsLayout;

    SharedViewModel sharedViewModel;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
     m = (FragmentActivity)getContext();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RandomImageView = root.findViewById(R.id.TheRandomImage);

        RandomImgRefresh  = root.findViewById(R.id.RandomImgRefresh);
        RandomImgProgress = root.findViewById(R.id.RandomImgProgress);

        OpenHiddenSecretsLayout  = root.findViewById(R.id.OpenHiddenSecretsLayout);
        StartHiddenSecretsLayout = root.findViewById(R.id.StartHiddenSecretsLayout);

        StartHiddenSecrets  = root.findViewById(R.id.StartHiddenSecretsBtn);
        LearnAboutHiddenSecrets = root.findViewById(R.id.LearnMoreAboutHiddenSecretes);

        OpenHiddenSecrets= root.findViewById(R.id.OpenHiddenSecrets);
        passwordSettings = new PasswordSettings(getContext());
        // Initialize ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observe LiveData for image loading
        sharedViewModel.getImageLiveData().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap != null) {
                RandomImageView.setImageBitmap(bitmap);
            }
        });

        // Observe LiveData for loading state
        sharedViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            RandomImgProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Observe LiveData for error messages
        sharedViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch data when fragment is created (or based on your logic)
        sharedViewModel.fetchData();



        try {
            if (passwordSettings.getPassword()==null){
                OpenHiddenSecretsLayout.setVisibility(View.GONE);
                StartHiddenSecretsLayout.setVisibility(View.VISIBLE);
            }else {
                OpenHiddenSecretsLayout.setVisibility(View.VISIBLE);
                StartHiddenSecretsLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        LearnAboutHiddenSecrets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Keep Your Photos Safe & Hidden!")
                        .setMessage("With this feature, you can you securely hide your personal photos in just a few taps!" +
                                " Simply select the images you want to hide, and the app will move them to a hidden, " +
                                "private folder that only you can access. Once hidden, the images will disappear from your gallery, " +
                                "keeping them safe from prying eyes. You can easily view, restore, or permanently delete hidden images whenever you want." +
                                " With a simple and intuitive design, protecting your privacy has never been easier!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

                AlertDialog dialog = builder.create();
                dialog.show();

                // Center the text inside the AlertDialog
                TextView messageView = dialog.findViewById(android.R.id.message);
                if (messageView != null) {
                    messageView.setGravity(Gravity.CENTER);
                }

            }
        });

        RandomImgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Observe LiveData for image loading
                sharedViewModel.fetchNewImage();
            }
        });


        RandomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Image image;
                image = Image.getInstance();
                Intent Editor = new Intent(getContext(), Editor_main_Activity.class);
                Glide.with(getContext())
                        .asBitmap()
                        .load(sharedViewModel.getImageUrl())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                image.setBitmap(resource);
                                image.setOriginalBitmap(resource);
                                getContext().startActivity(Editor);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }
        });

        StartHiddenSecrets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent PasswordSetting = new Intent(getContext(), HiddenSecretsPasswordSettingActivity.class);
                startActivity(PasswordSetting);

            }
        });

        OpenHiddenSecrets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent PasswordEntering = new Intent(getContext(), HiddenSecretsPasswordEnteringActivity.class);
                startActivity(PasswordEntering);

            }
        });


        return root;
    }






    @Override
    public void onResume() {
        super.onResume();
        try {
            if (passwordSettings.getPassword()==null){

                OpenHiddenSecretsLayout.setVisibility(View.GONE);
                StartHiddenSecretsLayout.setVisibility(View.VISIBLE);
            }else {
                OpenHiddenSecretsLayout.setVisibility(View.VISIBLE);
                StartHiddenSecretsLayout.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public class SharedViewModel extends ViewModel {
//        private final MutableLiveData<Boolean> isDataLoaded = new MutableLiveData<>(false);
//
//        public LiveData<Boolean> getIsDataLoaded() {
//            return isDataLoaded;
//        }
//
//        public void fetchData() {
//            if (Boolean.TRUE.equals(isDataLoaded.getValue())) {
//                return; // Already loaded, do nothing
//            }
//            getUnsplashData(); // Call your API function
//            isDataLoaded.setValue(true); // Mark as loaded
//        }
//
//        private void getUnsplashData(){
//            // Show progress bar before starting the request
//            RandomImgProgress.setVisibility(View.VISIBLE);
//
//            getApiUnsplash().getImages().enqueue(new Callback<ImagesModel>() {
//                @Override
//                public void onResponse(@NonNull Call<ImagesModel> call, @NonNull Response<ImagesModel> response) {
//                    if (response.isSuccessful()) {
//                        imagesModels = response.body();
//                        if (imagesModels != null) {
//                            Glide.with(getContext())
//                                    .asBitmap()
//                                    .load(imagesModels.getUrls().getSmall())
//                                    .apply(new RequestOptions().centerCrop())
//                                    .listener(new RequestListener<Bitmap>() {
//                                        @Override
//                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                                            RandomImgProgress.setVisibility(View.GONE);
//                                            Toast.makeText(m, "Failed to load image", Toast.LENGTH_SHORT).show();
//                                            return false;
//                                        }
//
//                                        @Override
//                                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                                            RandomImgProgress.setVisibility(View.GONE);
//                                            return false;
//                                        }
//                                    })
//                                    .into(RandomImageView);
//                        }
//                    } else {
//                        RandomImgProgress.setVisibility(View.GONE);
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<ImagesModel> call, @NonNull Throwable t) {
//                    RandomImgProgress.setVisibility(View.GONE);
//                    Toast.makeText(m, "Failed to load image", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
}