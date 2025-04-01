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
package com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.SelectToolsHandel;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.CropCutShape;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.X_YFixer;


public class OnSelectArea {
    //
    SelectTools selectTools = new SelectTools();

    //
    private float right;

    //
    private float bottom;

    //
    private float left;

    //
    private float top;

    //
    private RectF BitmapRect = new RectF();

    //
    Path CropPath = new Path();
    Path ScalePath = new Path();
    Path DrawPath = new Path();

    Path DrawSelectAreaPath = new Path();

    private float startingX;
    private float startingY;

    private final RectF DrawRectF = new RectF();
    private final RectF ScaleRectF = new RectF();

    float X,Y,rX,rY;

    X_YFixer xAndyFixer =new X_YFixer();
    boolean MovePath = false;
    float DownX,DownY,rDownX,rDownY;

    /**
     *
     * @param x the x-coordinate of the down action
     * @param y the y-coordinate of the down action
     * @param isInPast check if it is in phase of past
     * @param cropCutShape shape of selector
     */

    public void OnDown(float x, float y,Matrix matrix, RectF BitmapRect, boolean isInPast, CropCutShape cropCutShape, boolean ModeGlobal){

        this.BitmapRect = BitmapRect;
        isWidthAndHeightNull(right,left,bottom,top);
        xAndyFixer.FixXYUsingMatrix(x,y,matrix);
        RectF bounds = new RectF();
        SelectTools.getDrawCropPath().computeBounds(bounds, true);

        if (bounds.contains(x, y) && !ModeGlobal){
            MovePath = true;
            DownX = x;
            DownY = y;
            rDownX = xAndyFixer.getFixedXYUsingMartix().x;
            rDownY = xAndyFixer.getFixedXYUsingMartix().y;
        }else {
            MovePath = false;
            Rectangle(x,y);
            if (!isInPast && !ModeGlobal){
                DrawPath.reset();
                DrawSelectAreaPath.reset();
                ScalePath.reset();
                CropPath.reset();
                SelectTools.ClearArea();
                SelectTools.setAreaEmpty();
            }

            if (!isInPast && cropCutShape == CropCutShape.FREE){
                DrawSelectAreaPath.reset();
                if (DrawPath.isEmpty()&& !SelectTools.getDrawCropPath().isEmpty()){
                    DrawPath.set(SelectTools.getDrawCropPath());
                    ScalePath.set(SelectTools.getScaledCropPath());
                    CropPath.set(SelectTools.getCropPath());
                }
                DrawSelectAreaPath.moveTo(x, y);
                DrawPath.moveTo(x, y);
                ScalePath.moveTo(x, y);
                CropPath.moveTo(xAndyFixer.getFixedXYUsingMartix().x,xAndyFixer.getFixedXYUsingMartix().y);
                //selectTools.setCropPathArea(CropPath, ScalePath, DrawPath);
                selectTools.setDrawSelectAreaPath(DrawSelectAreaPath);
            }
        }
    }

    /**
     *
     * @param x the x-coordinate of the down action
     * @param y the y-coordinate of the down action

     * @param isinPast check if it is in phase of past
     * @param cropCutShape Shape of Selector.
     */
    public void OnMove(float x, float y,Matrix matrix, boolean isinPast, CropCutShape cropCutShape){


        isWidthAndHeightNull(right, left, bottom, top);
        xAndyFixer.FixXYUsingMatrix(x,y,matrix);
        //Path originalPath;
        //originalPath = new Path(SelectTools.getScaledCropPath());
        // Path path = new Path();SelectTools.getDrawCropPath().set(path);
        if (MovePath){
            X = x - DownX;
            Y = y - DownY;

            rX = xAndyFixer.getFixedXYUsingMartix().x - rDownX;
            rY = xAndyFixer.getFixedXYUsingMartix().y - rDownY;

            SelectTools.getDrawCropPath().offset(X,Y);
            SelectTools.getScaledCropPath().offset(X,Y);
            SelectTools.getCropPath().offset(rX,rY);

            DownX = x;
            DownY = y;
            rDownX = xAndyFixer.getFixedXYUsingMartix().x;
            rDownY = xAndyFixer.getFixedXYUsingMartix().y;

        }else {
            makeCalculations(x, y);
            if (!isinPast && cropCutShape == CropCutShape.FREE) {

                ScalePath.lineTo(x, y);
                DrawPath.lineTo(x, y);
                DrawSelectAreaPath.lineTo(x, y);
                CropPath.lineTo(xAndyFixer.getFixedXYUsingMartix().x,xAndyFixer.getFixedXYUsingMartix().y);

                //selectTools.setCropPathArea(CropPath, ScalePath, DrawPath);
                selectTools.setDrawSelectAreaPath(DrawSelectAreaPath);
            }
        }


    }

    /**
     *
     * @param x the x-coordinate of the down action
     * @param y the y-coordinate of the down action
     */
    public void OnUp(float x, float y,Matrix matrix, boolean isinPast, CropCutShape cropCutShape, boolean ModeGlobal){

        xAndyFixer.FixXYUsingMatrix(x,y,matrix);
        xAndyFixer.FixRect(left,top,right,bottom,matrix);

        //RectF Just For Drawing.
        DrawRectF.right =  right;
        DrawRectF.left =  left;
        DrawRectF.bottom =  bottom;
        DrawRectF.top =  top;

        //RectF Just For Drawing.
        ScaleRectF.right =  right;
        ScaleRectF.left =  left;
        ScaleRectF.bottom =  bottom;
        ScaleRectF.top =  top;

        if (MovePath){
            float deltaX = x - DownX;
            float deltaY = y - DownY;

            float rDeltaX = xAndyFixer.getFixedXYUsingMartix().x - rDownX;
            float rDeltaY = xAndyFixer.getFixedXYUsingMartix().y - rDownY;

            // Use a temporary path to avoid multiplication
            Path tempDrawPath = new Path(SelectTools.getDrawCropPath());
            Path tempScaledPath = new Path(SelectTools.getScaledCropPath());
            Path tempCropPath = new Path(SelectTools.getCropPath());

            tempDrawPath.offset(deltaX, deltaY);
            tempScaledPath.offset(deltaX, deltaY);
            tempCropPath.offset(rDeltaX, rDeltaY);

            // Update the paths
            selectTools.setCropPathArea(tempCropPath, tempScaledPath, tempDrawPath);

            DownX = x;
            DownY = y;
            rDownX = xAndyFixer.getFixedXYUsingMartix().x;
            rDownY = xAndyFixer.getFixedXYUsingMartix().y;

            MovePath = false;
        }else if (!isinPast){
            if (cropCutShape == CropCutShape.RECTANGLE) {
                selectTools.AddRect(xAndyFixer.getFixedRectF(), DrawRectF, ScaleRectF);
            }
            if (cropCutShape == CropCutShape.OVAL) {
                selectTools.AddOval(xAndyFixer.getFixedRectF(), DrawRectF, ScaleRectF);
            }
            if (cropCutShape == CropCutShape.FREE) {
                CropPath.quadTo(X_YFixer.getfixedXYUsingMartix(startingX, startingY,matrix).x,
                        X_YFixer.getfixedXYUsingMartix(startingX, startingY,matrix).y,
                        xAndyFixer.getFirstFixedXY().x,xAndyFixer.getFirstFixedXY().y);

                DrawSelectAreaPath.quadTo(x, y, startingX, startingY);
                DrawPath.quadTo(x, y, startingX, startingY);

                ScalePath.quadTo(x, y, startingX, startingY);

                // TODO: 9/7/2024 fix from here
                if (!ModeGlobal) {
                    selectTools.setCropPathArea(CropPath,ScalePath,DrawPath);
                    Log.d("TAG", "OnUp: 111");
                }
                selectTools.setDrawSelectAreaPath(DrawSelectAreaPath);
            }

            if (ModeGlobal) {
                if (cropCutShape == CropCutShape.RECTANGLE) {
                    selectTools.AddRect(xAndyFixer.getFixedRectF(), DrawRectF, ScaleRectF);
                } else if (cropCutShape == CropCutShape.OVAL) {
                    selectTools.AddOval(xAndyFixer.getFixedRectF(), DrawRectF, ScaleRectF);
                } else if (cropCutShape == CropCutShape.FREE) {
                    //TODO: 9/7/2024 fix from here
                    selectTools.AddPath(CropPath,ScalePath,DrawPath);
                    Log.d("TAG", "OnUp: 222");
                }
            }
        }

        isWidthAndHeightNull(right,left,bottom,top);
    }

    public void reset(){
        DrawPath.reset();
        ScalePath.reset();
        CropPath.reset();
        DrawSelectAreaPath.reset();
        SelectTools.ClearArea();
        SelectTools.setAreaEmpty();
    }
    public void Rectangle(float startingX, float startingY) {
        this.startingX = startingX;
        this.startingY = startingY;
        makeCalculations(startingX,startingY);
    }

    /**
     * Makes calculations made on object on Action_UP
     **/
    public void makeCalculations(float endX, float endY) {
        boolean flag1=false, flag2=false;
        if(endX<startingX) flag1=true;
        if(endY<startingY) flag2=true;

        if(flag1 && flag2)
        {
            left=endX;
            top=endY;
            right=startingX;
            bottom=startingY;
        }
        else if(flag1)
        {
            left=endX;
            top=startingY;
            right=startingX;
            bottom=endY;
        }
        else if(flag2)
        {
            left=startingX;
            top=endY;
            right=endX;
            bottom=startingY;
        }
        else
        {
            left=startingX;
            top=startingY;
            right=endX;
            bottom=endY;
        }

        selectTools.right(right);
        selectTools.bottom(bottom);
        selectTools.left(left);
        selectTools.top(top);
    }

    /**
     *  for more Visit --->  <a href="https://stackoverflow.com/a/16792888">...</a>  & ---> <a href="https://stackoverflow.com/a/36146901">...</a>
     * @param downX X position of touche of user Finger in Screen
     * @param downY Y position of touche of user Finger in Screen
     * @param x X position of Circle in the screen
     * @param y X position of Circle in the screen
     * @return distance.
     */
    private boolean isClicked(float downX, float downY, float x, float y){
        int distance = (int) Math.sqrt(((downX - x) * (downX - x)) +
                ((downY - y) * (downY - y)) );
        return distance < 25;
    }

    /**
     * check if rect width and height equals to 0.
     * and set width and height value bigger than 0.
     * "Minimum distance"
     * @param Right Right of Bitmap
     * @param Left Left of Bitmap
     * @param Bottom Bottom of Bitmap
     * @param Top Top of Bitmap
     */
    private void isWidthAndHeightNull(float Right, float Left, float Bottom , float Top){
        RectF rect = new RectF();
        rect.bottom =  Bottom;
        rect.left =  Left;
        rect.top = Top;
        rect.right =  Right;
        if (rect.width()==0&rect.height()==0){
            left = right +3;
            top = bottom +3;
        }
        if (rect.width()==0){
            left = right +3;
        }
        if (rect.height()==0){
            top = bottom +3;
        }
    }
}
