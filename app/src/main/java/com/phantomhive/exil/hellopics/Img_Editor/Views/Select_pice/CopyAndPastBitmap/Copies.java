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
