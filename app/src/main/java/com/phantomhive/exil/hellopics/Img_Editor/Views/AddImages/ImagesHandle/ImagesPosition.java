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

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

public class ImagesPosition {
     ArrayList<PointF> ImagesPosition = new ArrayList<>();
    ArrayList<PointF> ImagesRealTimePosition = new ArrayList<>();
     int Index;

    public   void setIndex(int index) {

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

    public void AddPosition(float x,float y,float RTx,float RTy){
        PointF point = new PointF();
        point.set(x, y);
        ImagesPosition.add(point);

        PointF RealTimePoint = new PointF();
        RealTimePoint.set(RTx, RTy);
        ImagesRealTimePosition.add(RealTimePoint);
    }

    public ArrayList<PointF> ImagesPositionList()
    {
        return ImagesPosition;
    }


    public ArrayList<PointF> ImagesRealTimePositionList()
    {
        return ImagesRealTimePosition;
    }


    @SuppressLint("NotConstructor")
    public PointF ImagePosition(){
        return ImagesPosition.get(getIndex());
    }

    public PointF ImageRealTimePosition(){
        return ImagesRealTimePosition.get(getIndex());
    }


    public void removePosition(){
        ImagesPosition.remove(getIndex());
        ImagesRealTimePosition.remove(getIndex());
        Index = ImagesPosition.size()-1;
    }

    public void updatePosition(float x, float y,float RTx, float RTy){
        PointF point = new PointF(x,y);
        ImagesPosition.set(getIndex(),point);

        PointF RealTimePoint = new PointF(RTx,RTy);
        ImagesRealTimePosition.set(getIndex(),RealTimePoint);
    }

    public int size (){
        return ImagesPosition.size();
    }

    public void clearPositions (){
        ImagesPosition.clear();
        ImagesRealTimePosition.clear();    }
}
