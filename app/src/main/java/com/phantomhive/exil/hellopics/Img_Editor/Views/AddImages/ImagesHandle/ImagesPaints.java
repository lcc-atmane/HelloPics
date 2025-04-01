package com.phantomhive.exil.hellopics.Img_Editor.Views.AddImages.ImagesHandle;


import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Xfermode;

import com.phantomhive.exil.hellopics.Img_Editor.Views.AddImages.DrawType;

import java.util.ArrayList;

public class ImagesPaints {
     ArrayList<Paint> ImagesPaintList = new ArrayList<>();
    ArrayList<Integer> ImagesColorList = new ArrayList<>();
    ArrayList<PorterDuff.Mode> ImagesColorModeList = new ArrayList<>();
     private int Index;

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
     * @param index index of Paints
     */
    public void unloadingIndex(int index){
        Index = index;
    }

    public void AddPaint(DrawType drawType){
        Paint paint= null;
        if (drawType == DrawType.MERGE){
             paint = new Paint(Paint.ANTI_ALIAS_FLAG);
             paint.setAntiAlias(true);
             paint.setFilterBitmap(true);
             paint.setDither(true);
             paint.setAlpha(127);
        }else if (drawType == DrawType.ADD){
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);

        }
        ImagesPaintList.add(paint);
        ImagesColorList.add(Color.TRANSPARENT);
        ImagesColorModeList.add(PorterDuff.Mode.MULTIPLY);
    }

    public ArrayList<Paint> getImagesPaintList() {
        return ImagesPaintList;
    }

    public void removePaint(){
        ImagesPaintList.remove(getIndex());
        ImagesColorList.remove(getIndex());
        ImagesColorModeList.remove(getIndex());
        Index = ImagesPaintList.size()-1;
    }

    public void setOpacity(int Opacity){
        ImagesPaintList.get(getIndex()).setAlpha(Opacity);
    }

    public int getOpacity(int Index){
        return ImagesPaintList.get(Index).getAlpha();
    }

    public int getOpacity(){
        return ImagesPaintList.get(getIndex()).getAlpha();
    }

    public void setImageColor(int Color){
        ImagesColorList.set(getIndex(),Color);
        ColorFilter filter;
        if (ImagesColorModeList.get(getIndex())==null){
            filter = null;
        } else  {
            filter = new PorterDuffColorFilter(Color, ImagesColorModeList.get(getIndex()));
        }
        ImagesPaintList.get(getIndex()).setColorFilter(filter);
    }

    public void setImagesColorMode(PorterDuff.Mode mode) {
        ColorFilter filter;
        ImagesColorModeList.set(getIndex(),mode);
        if (mode == null){
            filter = null;
        }else {
           filter = new PorterDuffColorFilter(ImagesColorList.get(getIndex()),mode);

        }
        ImagesPaintList.get(getIndex()).setColorFilter(filter);
    }

    public int getImageColor(){
        return ImagesPaintList.get(getIndex()).getColor();
    }

    public void setXfermode(PorterDuff.Mode modeName){
        if (modeName == null){
            ImagesPaintList.get(getIndex()).setXfermode(null);
        }else {
            ImagesPaintList.get(getIndex()).setXfermode(new PorterDuffXfermode(modeName));
        }

    }

    public Xfermode getXfermode(){
        return ImagesPaintList.get(getIndex()).getXfermode();
    }

    public int size (){
        return ImagesPaintList.size();
    }
    public void clearPaints(){
        ImagesPaintList.clear();
        ImagesColorList.clear();
        ImagesColorModeList.clear();
    }
}
