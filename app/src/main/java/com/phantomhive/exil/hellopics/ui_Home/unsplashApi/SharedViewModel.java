package com.phantomhive.exil.hellopics.ui_Home.unsplashApi;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.phantomhive.exil.hellopics.ui_Home.unsplashApi.ApiUnsplash;
import com.phantomhive.exil.hellopics.ui_Home.unsplashApi.ApiUtils;
import com.phantomhive.exil.hellopics.ui_Home.unsplashApi.imagesData.ImagesModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharedViewModel extends AndroidViewModel {
    private final MutableLiveData<Bitmap> imageLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isDataLoaded = new MutableLiveData<>(false);

    private final ApiUnsplash apiService = ApiUtils.getApiUnsplash(); // Retrofit instance

    private String imageUrl;  // Image URL variable

    public SharedViewModel(@NonNull Application application) {
        super(application);
    }

    // Getters for LiveData
    public LiveData<Bitmap> getImageLiveData() {
        return imageLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsDataLoaded() {
        return isDataLoaded;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Method to fetch image data
    public void fetchData() {
        if (Boolean.TRUE.equals(isDataLoaded.getValue())) {
            return; // Already loaded, no need to fetch again
        }

        isLoading.setValue(true);  // Set loading to true before fetching data

        apiService.getImages().enqueue(new Callback<ImagesModel>() {
            @Override
            public void onResponse(@NonNull Call<ImagesModel> call, @NonNull Response<ImagesModel> response) {
                isLoading.setValue(false);  // Set loading to false after receiving the response

                if (response.isSuccessful() && response.body() != null) {
                    // Get the small image URL to load the image and the regular URL for storage
                    String smallImageUrl = response.body().getUrls().getSmall();
                    imageUrl = response.body().getUrls().getRegular();

                    // Load the image using the small URL
                    loadImage(smallImageUrl);
                    isDataLoaded.setValue(true);  // Set dataLoaded to true after successful fetch
                } else {
                    errorMessage.setValue("Failed to load image");  // Error handling
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImagesModel> call, @NonNull Throwable t) {
                isLoading.setValue(false);  // Set loading to false on failure
                errorMessage.setValue("Failed to load image");  // Set error message
                Log.e("SharedViewModel", "API Failure: " + t.getMessage());
            }
        });
    }

    // Method to reload the image using the stored image URL
    // Fetch a new image from the API
    public void fetchNewImage() {
        isLoading.setValue(true);  // Set loading to true before fetching new image

        apiService.getImages().enqueue(new Callback<ImagesModel>() {
            @Override
            public void onResponse(@NonNull Call<ImagesModel> call, @NonNull Response<ImagesModel> response) {
                isLoading.setValue(false);  // Set loading to false after receiving the response

                if (response.isSuccessful() && response.body() != null) {
                    // Get the small image URL to load the image
                    String smallImageUrl = response.body().getUrls().getSmall();
                    imageUrl = response.body().getUrls().getRegular();
                    // Load the new image
                    loadImage(smallImageUrl);
                    isDataLoaded.setValue(true);  // Set dataLoaded to true after successful fetch
                } else {
                    errorMessage.setValue("Failed to load image");  // Error handling
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImagesModel> call, @NonNull Throwable t) {
                isLoading.setValue(false);  // Set loading to false on failure
                errorMessage.setValue("Failed to load image");  // Set error message
                Log.e("SharedViewModel", "API Failure: " + t.getMessage());
            }
        });
    }

    // Method to load an image using Glide
    private void loadImage(String url) {
        Glide.with(getApplication())  // Use the application context
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageLiveData.setValue(resource);  // Update the LiveData with the new image
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle cleanup when Glide cancels or clears the image
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        errorMessage.setValue("Failed to load image");  // Handle image load failure
                    }
                });
    }
}


