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
