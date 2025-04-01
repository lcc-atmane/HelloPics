package com.phantomhive.exil.hellopics.ui_Home.unsplashApi;

import static com.phantomhive.exil.hellopics.ui_Home.unsplashApi.ApiUtils.Base_Url_Key;

import com.phantomhive.exil.hellopics.ui_Home.unsplashApi.imagesData.ImagesModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiUnsplash {
    @Headers("Authorization: Client-ID " + Base_Url_Key)
    @GET("/photos/random")
    Call<ImagesModel> getImages();

}
