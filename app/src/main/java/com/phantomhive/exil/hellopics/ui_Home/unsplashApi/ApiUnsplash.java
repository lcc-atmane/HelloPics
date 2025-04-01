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
