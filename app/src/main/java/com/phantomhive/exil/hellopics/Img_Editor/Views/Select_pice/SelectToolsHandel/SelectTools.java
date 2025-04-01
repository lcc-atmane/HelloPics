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

import android.graphics.Path;
import android.graphics.RectF;


public class SelectTools {


    private static float dt;
    private static float dr;
    private static float dl;
    private static float db;

    static Path DrawSelectAreaPath = new Path();

    private static Path CropPath = new Path();
    private static Path DrawCropPath = new Path();
    private static Path ScaledCropPath = new Path();


    // Set the rectangle's coordinates to the specified values.
    // Note: no range checking is performed,
    // so it is up to the caller to ensure that left <= right and top <= bottom.

    /**
     *
     * @param tp The X coordinate of the left side of the rectangle
     */
    protected void top (float tp){
        dt = tp;
    }

    /**
     *
     * @param rt The Y coordinate of the top of the rectangle
     */
    protected void right (float rt){
        dr = rt;
    }

    /**
     *
     * @param lt The X coordinate of the right side of the rectangle
     */
    protected void left (float lt){
        dl = lt;
    }

    /**
     *
     * @param bm The Y coordinate of the bottom of the rectangle
     */
    protected void bottom (float bm){
        db = bm;
    }


    public static void setAreaEmpty(){
      dr = dt = db = dl = 0;
      DrawSelectAreaPath.reset();
      DrawCropPath.reset();
      ScaledCropPath.reset();
      CropPath.reset();
      DrawPathCropSelectArea().reset();
      getCropPath().reset();
      getDrawCropPath().reset();
      getScaledCropPath().reset();

    }

    // Path Setting ////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param cropPath
     * @param scaledCropPath
     * @param drawCropPath
     */
    protected void setCropPathArea(Path cropPath,Path scaledCropPath,Path drawCropPath){
        CropPath.addPath(cropPath);
        ScaledCropPath.addPath(scaledCropPath);
        DrawCropPath.addPath(drawCropPath);
    }


    protected void setDrawSelectAreaPath(Path path){
        DrawSelectAreaPath=(path);
    }
    // The Global Path <Compose Rect & Path & Oval in Single Area> /////////////////////////////////

    /**
     *
     * @param RealRectF
     * @param scaleRectF
     */
    protected void AddRect(RectF RealRectF, RectF DrawRectF, RectF scaleRectF){
        CropPath.addRect(RealRectF, Path.Direction.CCW);
        DrawCropPath.addRect(DrawRectF,Path.Direction.CCW);
        ScaledCropPath.addRect(scaleRectF,Path.Direction.CCW);
    }

    /**
     *
     * @param RealRectF
     * @param scaleRectF
     */
    protected void AddOval(RectF RealRectF, RectF DrawRectF, RectF scaleRectF){
        CropPath.addOval(RealRectF, Path.Direction.CCW);
        DrawCropPath.addOval(DrawRectF,Path.Direction.CCW);
        ScaledCropPath.addOval(scaleRectF,Path.Direction.CCW);
    }

    /**
     *
     * @param RealPath
     * @param scalePath
     */
    protected void AddPath(Path RealPath, Path DrawPath, Path scalePath){
        CropPath.addPath(RealPath);
        DrawCropPath.addPath(DrawPath);
        ScaledCropPath.addPath(scalePath);
    }


    // Draw Tools <Rect, Oval, Path, GlobalPath ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public static RectF DrawRectOvalCropSelectArea(){
        RectF rectCrop = new RectF();
        rectCrop.right = dr;
        rectCrop.bottom = db;
        rectCrop.left = dl;
        rectCrop.top = dt;
        return rectCrop;
    }

    public static Path DrawPathCropSelectArea(){

        return DrawSelectAreaPath;
    }

    public static Path getCropPath(){
        return CropPath;
    }

    public static Path getDrawCropPath(){
        return DrawCropPath;
    }

    public static Path getScaledCropPath(){
        return ScaledCropPath;
    }

    public static void ClearArea(){
        dr = dt = db = dl = 0;
        DrawSelectAreaPath.reset();
        DrawCropPath.reset();

        getDrawCropPath().reset();
        DrawPathCropSelectArea().reset();
    }
}
