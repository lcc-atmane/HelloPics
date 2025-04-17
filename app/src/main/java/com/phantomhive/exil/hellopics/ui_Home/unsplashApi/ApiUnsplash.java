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
