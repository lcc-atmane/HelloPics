package com.phantomhive.exil.hellopics.ui_Home.unsplashApi;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiUtils {
    public static final String Base_Url="https://api.unsplash.com";
    public static final String Base_Url_Key="-zo60-OteL5sUIrXVo-4bpzHzidqXzfiAzsrcSYe7Rw";

    public static Retrofit retrofit = null;

    public static ApiUnsplash getApiUnsplash() {
        if (retrofit == null){
            retrofit = new Retrofit.Builder().
                    baseUrl(Base_Url).
                    addConverterFactory(GsonConverterFactory.create())
                    .build();
            Log.d("TAG", "its repeting ");
        }

        return retrofit.create(ApiUnsplash.class);
    }

}
