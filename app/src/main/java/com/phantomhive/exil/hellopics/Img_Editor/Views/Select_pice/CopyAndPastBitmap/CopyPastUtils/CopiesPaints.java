package com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.CopyAndPastBitmap.CopyPastUtils;


import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;

import java.util.ArrayList;

public class CopiesPaints {
    ArrayList<Paint> CopiesPaintList = new ArrayList<>();
    //ArrayList<Paint> RectOnTopPaint = new ArrayList<>();

    ArrayList<Integer> ImagesCopyColorList = new ArrayList<>();
    ArrayList<PorterDuff.Mode> ImagesCopyColorModeList = new ArrayList<>();
    static private int Index;

    public  void setIndex(int index) {
        Index = index;
    }

    public static int getIndex() {
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

    public void AddPaint(){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        CopiesPaintList.add(paint);
        ImagesCopyColorList.add(Color.TRANSPARENT);
        ImagesCopyColorModeList.add(PorterDuff.Mode.MULTIPLY);
    }

    public ArrayList<Paint> getCopiesPaintList() {
        return CopiesPaintList;
    }

    public void removePaint(int Index){
        CopiesPaintList.remove(Index);
        ImagesCopyColorList.remove(Index);
        ImagesCopyColorModeList.remove(Index);
        this.Index =CopiesPaintList.size()-1;
    }

    public void removePaint(){
        CopiesPaintList.remove(getIndex());
        ImagesCopyColorList.remove(getIndex());
        ImagesCopyColorModeList.remove(getIndex());
        Index = CopiesPaintList.size()-1;
    }

    public void setOpacity(int Index ,int Opacity){
        CopiesPaintList.get(Index).setAlpha(Opacity);
    }

    public void setOpacity(int Opacity){
        CopiesPaintList.get(getIndex()).setAlpha(Opacity);
    }

    public int getOpacity(int Index){
        return CopiesPaintList.get(Index).getAlpha();
    }

    public int getOpacity(){
        return CopiesPaintList.get(getIndex()).getAlpha();
    }


    public void setImageColor(int Index ,int Color){
        ColorFilter filter = new PorterDuffColorFilter(Color, PorterDuff.Mode.MULTIPLY);
        CopiesPaintList.get(Index).setColorFilter(filter);
    }

    public void setImageColor(int Color){
        ImagesCopyColorList.set(getIndex(),Color);
        ColorFilter filter;
        if (ImagesCopyColorModeList.get(getIndex())==null){
             filter = null;
        } else  {
            filter = new PorterDuffColorFilter(Color, ImagesCopyColorModeList.get(getIndex()));
        }

        CopiesPaintList.get(getIndex()).setColorFilter(filter);
    }

    public void setImagesColorMode(PorterDuff.Mode mode) {
        ColorFilter filter;
        ImagesCopyColorModeList.set(getIndex(),mode);
        if (mode == null){
            filter = null;
        }else {
            filter = new PorterDuffColorFilter(ImagesCopyColorList.get(getIndex()),mode);

        }
        CopiesPaintList.get(getIndex()).setColorFilter(filter);
    }

    public int getImageColor(int Index){
        return CopiesPaintList.get(Index).getColor();
    }

    public int getImageColor(){
        return CopiesPaintList.get(getIndex()).getColor();
    }

    public void setXfermode(PorterDuff.Mode modeName){
        if (modeName == null){
            CopiesPaintList.get(getIndex()).setXfermode(null);
        }else {
            CopiesPaintList.get(getIndex()).setXfermode(new PorterDuffXfermode(modeName));
        }

    }

    public Xfermode getXfermode(){
        return CopiesPaintList.get(getIndex()).getXfermode();
    }

    public void setXfermode(int Index, PorterDuff.Mode modeName){
        CopiesPaintList.get(Index).setXfermode(new PorterDuffXfermode(modeName));
    }

    public Xfermode getXfermode(int Index){
        return CopiesPaintList.get(Index).getXfermode();
    }

    public int size (){
        return CopiesPaintList.size();
    }
    public void clearPaints(){
        CopiesPaintList.clear();
        ImagesCopyColorList.clear();
        ImagesCopyColorModeList.clear();
    }
}
