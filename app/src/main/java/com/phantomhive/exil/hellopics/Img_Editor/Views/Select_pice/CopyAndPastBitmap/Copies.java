package com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.CopyAndPastBitmap;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Copies {
    static ArrayList<Bitmap> RealCopiesList = new ArrayList<>();

    static ArrayList<Bitmap> RealCopiesListnt = new ArrayList<>();
    static private int Index;

    public  void setIndex(int index)
    {
        Index = index;
    }

    public static int getIndex() {

        return Index;
    }

    public void unloadingIndex(){
        Index = 0;
    }

    /*
     * remove the previous Copies
     * @param index index of Copy
     */
    public void unloadingIndex(int index){

        Index = index;
    }
    public void AddCopy(Bitmap RealCopy){
        RealCopiesList.add(RealCopy);
        RealCopiesListnt.add(RealCopy);
    }


    public void UpdateRealBitmap(Bitmap bitmap){
        RealCopiesList.set(getIndex(),bitmap);
    }


    public Bitmap getRealBitmap(){
        return RealCopiesList.get(getIndex());
    }

    public ArrayList<Bitmap> getRealCopiesList() {
        return RealCopiesList;
    }

    public Bitmap getRealBitmapNt(){
        return RealCopiesListnt.get(getIndex());
    }

    public ArrayList<Bitmap> getRealCopiesListNt() {
        return RealCopiesListnt;
    }

    public void removeCopy(int Index){

        RealCopiesList.remove(Index);
        RealCopiesListnt.remove(Index);
        this.Index =RealCopiesList.size()-1;
    }

    public void removeCopy(){
        RealCopiesList.remove(getIndex());
        RealCopiesListnt.remove(getIndex());
        this.Index = RealCopiesList.size()-1;
    }

    public void copiesListClear(){
        RealCopiesList.clear();
        RealCopiesListnt.clear();
    }

    public int copiesNumber(){
        return RealCopiesList.size();
    }

}
