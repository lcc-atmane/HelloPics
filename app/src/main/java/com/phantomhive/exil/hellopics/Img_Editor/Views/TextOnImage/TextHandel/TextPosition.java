package com.phantomhive.exil.hellopics.Img_Editor.Views.TextOnImage.TextHandel;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

public class TextPosition {
    ArrayList<PointF> TextPositionList = new ArrayList<>();
    static int Index;

    public  void setIndex(int index) {

        Index = index;
    }

    public  int getIndex() {

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

    public void AddPosition(float x,float y){
        PointF point = new PointF();
        point.set(x, y);
        TextPositionList.add(point);
    }

    public ArrayList<PointF> TextPositionList()
    {
        return TextPositionList;
    }


    @SuppressLint("NotConstructor")
    public PointF TextPosition(){
        return TextPositionList.get(getIndex());
    }


    public void removePosition(){
        TextPositionList.remove(getIndex());
        Index = TextPositionList.size()-1;
    }

    public void updatePosition(float x, float y){
        PointF point = new PointF(x,y);
        TextPositionList.set(getIndex(),point);
    }

    public int size (){
        return TextPositionList.size();
    }
    public void clearPositions (){
        TextPositionList.clear();
    }
}