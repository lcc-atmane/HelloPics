package com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.CopyAndPastBitmap.CopyPastUtils;

import android.graphics.PointF;

import java.util.ArrayList;

public class CopiesPosition {
    ArrayList<PointF> CopiesPosition = new ArrayList<>();

    ArrayList<PointF> RealCopiesPosition = new ArrayList<>();
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
    public void AddPosition(float x,float y,float Rx,float Ry){
        PointF point = new PointF();
        point.set(x, y);
        CopiesPosition.add(point);

        PointF Realpoint = new PointF();
        Realpoint.set(Rx, Ry);
        RealCopiesPosition.add(Realpoint);
    }

    public ArrayList<PointF> CopiesPositionList()
    {
        return CopiesPosition;
    }


    public ArrayList<PointF> RealCopiesPositionList()
    {
        return RealCopiesPosition;
    }
    public PointF CopyPosition(int Index){
        return CopiesPosition.get(Index);
    }

    public PointF CopyPosition(){
        return CopiesPosition.get(getIndex());
    }

    public PointF RealCopyPosition(){
        return RealCopiesPosition.get(getIndex());
    }

    public void removePosition(int Index){
        CopiesPosition.remove(Index);
        this.Index =CopiesPosition.size()-1;
    }

    public void removePosition(){
        CopiesPosition.remove(getIndex());
        RealCopiesPosition.remove(getIndex());
        Index =CopiesPosition.size()-1;
    }

    public void updatePosition(int Index, float x, float y){
        PointF point = new PointF();
        point.set(x,y);
        CopiesPosition.set(Index,point);
    }

    public void updatePosition(float x, float y,float Rx,float Ry){
        PointF point = new PointF(x,y);
        CopiesPosition.set(getIndex(),point);

        PointF Realpoint = new PointF(Rx,Ry);
        RealCopiesPosition.set(getIndex(),Realpoint);
    }

    public int size (){
        return CopiesPosition.size();
    }

    public void clearPositions (){
        CopiesPosition.clear();
        RealCopiesPosition.clear();
    }
}