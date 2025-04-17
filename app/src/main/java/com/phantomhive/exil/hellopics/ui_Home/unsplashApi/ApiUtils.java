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
package com.phantomhive.exil.hellopics.ui_Home.unsplashApi;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiUtils {
    public static final String Base_Url="https://api.unsplash.com";
    public static final String Base_Url_Key="YourKeyHaHa";

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
