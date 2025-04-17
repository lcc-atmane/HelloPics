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
