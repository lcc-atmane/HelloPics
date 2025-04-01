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
package com.phantomhive.exil.hellopics.Img_Editor.Views.AddImages.ImagesHandle;

import android.graphics.Bitmap;

import com.phantomhive.exil.hellopics.Img_Editor.Views.AddImages.DrawType;

import java.util.ArrayList;

public class Images {
     ArrayList<Bitmap> Images = new ArrayList<>();
     ArrayList<Bitmap> NoImages = new ArrayList<>();

     ArrayList<DrawType> drawTypes =  new ArrayList<>();
     int Index;

    public  void AddDrawType(DrawType drawType){
        drawTypes.add(drawType);
    }

    public  void AddImages(Bitmap rbitmap,Bitmap og){
        Images.add(rbitmap);
        NoImages.add(og);
    }

    public  void UpdateRealBitmap(Bitmap bitmap){
        Images.set(getIndex(),bitmap);
    }

    public  ArrayList<Bitmap> getImages() {
        return Images;
    }




    public  ArrayList<DrawType> getDrawTypes() {
        return drawTypes;
    }

    public  Bitmap Image(){
        return Images.get(getIndex());
    }


    public  Bitmap NoImage(){
        return NoImages.get(getIndex());
    }


    public  DrawType getDrawType(){
        return drawTypes.get(getIndex());
    }

    public int Size() {
        return Images.size();
    }

    public void removeImage(){
        Images.remove(getIndex());

        NoImages.remove(getIndex());

        drawTypes.remove(getIndex());
        Index =Images.size()-1;
    }

    public void clearImages (){
        Images.clear();
        NoImages.clear();
        drawTypes.clear();
    }

    public void setIndex(int index) {

        Index = index;
    }

    public int getIndex() {

        return Index;
    }

    public void unloadingIndex(){
        Index = 0;
    }

    /**
     * remove the previous Copies
     * @param index index of Copy
     */
    public void unloadingIndex(int index)
    {
        Index = index;
    }
}
