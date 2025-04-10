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
package com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;


public class X_YFixer {

    PointF MPointF = new PointF();
    PointF FirstpointF = new PointF();
    RectF rectF = new RectF();

    public float getScale(Matrix matrix){
        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        matrix.getValues(matrixValues);

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        return matrixValues[Matrix.MSCALE_X];
    }

    public PointF getFirstFixedXY(){
        return FirstpointF;
    }

    public void FixRect(float left, float top, float right, float bottom, Matrix matrix){
        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        matrix.getValues(matrixValues);
        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        final float scaleX = matrixValues[Matrix.MSCALE_X];
        final float scaleY = matrixValues[Matrix.MSCALE_Y];
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];
        // Ensure that the left and top edges are not outside of the ImageView bounds.
        final float bitmapLeft = (transX < 0) ? Math.abs(transX) : 0;
        final float bitmapTop = (transY < 0) ? Math.abs(transY) : 0;

        RectF rect = new RectF();
        matrix.mapRect(rect);

        if (rect.top <= 0 && rect.left <= 0) {
            rectF.left = (bitmapLeft + left) / scaleX;
            rectF.top = (bitmapTop + top) / scaleY;
            rectF.right = (bitmapLeft + right) / scaleX;
            rectF.bottom = (bitmapTop + bottom) / scaleY;

        } else if ( rect.left <= 0){
            rectF.left = (bitmapLeft + left) / scaleX;
            rectF.top = (bitmapTop + top - rect.top) / scaleY;
            rectF.right = (bitmapLeft + right) / scaleX;
            rectF.bottom = (bitmapTop + bottom - rect.bottom) / scaleY;
        }else if ( rect.top <=0){
            rectF.left = (bitmapLeft + left - rect.left) / scaleX;
            rectF.top = (bitmapTop + top) / scaleY;
            rectF.right = (bitmapLeft + right - rect.left) / scaleX;
            rectF.bottom = (bitmapTop + bottom) / scaleY;
        } else{
            rectF.left = (bitmapLeft + left - rect.left) / scaleX;
            rectF.top = (bitmapTop + top - rect.top) / scaleY;
            rectF.right = (bitmapLeft + right - rect.right) / scaleX;
            rectF.bottom = (bitmapTop + bottom - rect.bottom) / scaleY;
        }

    }

    public RectF getFixedRectF(){
        return rectF;
    }

    public void FixXYUsingMatrix(float x, float y, Matrix matrix){
        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        matrix.getValues(matrixValues);

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        final float scaleX = matrixValues[Matrix.MSCALE_X];
        final float scaleY = matrixValues[Matrix.MSCALE_Y];
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];

        final float rotationRad = (float) -Math.atan2(matrixValues[Matrix.MSKEW_X], matrixValues[Matrix.MSCALE_X]);

        float cos = (float)Math.cos(rotationRad);
        float sin = (float)Math.sin(rotationRad);

        // Ensure that the left and top edges are not outside of the ImageView bounds.
        final float bitmapLeft = (transX < 0) ? Math.abs(transX) : 0;
        final float bitmapTop = (transY < 0) ? Math.abs(transY) : 0;

        RectF r = new RectF();
        matrix.mapRect(r);
        float xp,yp;
        if (r.top <= 0 && r.left <= 0){
            xp = (bitmapLeft+x)/scaleX;
            yp = (bitmapTop +y)/scaleY;

        } else if ( r.left <= 0){
            xp = (bitmapLeft+x)/scaleX;
            yp = (bitmapTop +y-r.top)/scaleY;

        }else if (r.top <= 0 ){
            xp = (bitmapLeft+x-r.left)/scaleX;
            yp = (bitmapTop +y)/scaleY;

        }else{
            xp = (bitmapLeft+x-r.left)/scaleX;
            yp = (bitmapTop+y-r.top)/scaleY;

        }

        MPointF.set(xp,yp);
        FirstpointF.set(xp,yp);
    }

    public PointF FixXYUsingMatrix(float x, float y, Matrix matrix,float d){
        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        matrix.getValues(matrixValues);

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        final float scaleX = matrixValues[Matrix.MSCALE_X];
        final float scaleY = matrixValues[Matrix.MSCALE_Y];
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];

        final float rotationRad = (float) -Math.atan2(matrixValues[Matrix.MSKEW_X], matrixValues[Matrix.MSCALE_X]);

        float cos = (float)Math.cos(rotationRad);
        float sin = (float)Math.sin(rotationRad);

        // Ensure that the left and top edges are not outside of the ImageView bounds.
        final float bitmapLeft = (transX < 0) ? Math.abs(transX) : 0;
        final float bitmapTop = (transY < 0) ? Math.abs(transY) : 0;

        RectF r = new RectF();
        matrix.mapRect(r);
        float xp,yp;
        if (r.top <= 0 && r.left <= 0){
            xp = (bitmapLeft+x)/scaleX;
            yp = (bitmapTop +y)/scaleY;

        } else if ( r.left <= 0){
            xp = (bitmapLeft+x)/scaleX;
            yp = (bitmapTop +y-r.top)/scaleY;

        }else if (r.top <= 0 ){
            xp = (bitmapLeft+x-r.left)/scaleX;
            yp = (bitmapTop +y)/scaleY;

        }else{
            xp = (bitmapLeft+x-r.left)/scaleX;
            yp = (bitmapTop+y-r.top)/scaleY;

        }


        return new PointF((xp * cos) - (yp * sin),
                (xp * sin) + (yp * cos));

    }

    public PointF getFixedXYUsingMartix(){
        return MPointF;
    }


    public static PointF getfixedXYUsingMartix(float x, float y, Matrix matrix){
        PointF pointF = new PointF();
        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        matrix.getValues(matrixValues);

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        final float scaleX = matrixValues[Matrix.MSCALE_X];
        final float scaleY = matrixValues[Matrix.MSCALE_Y];
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];

        // Ensure that the left and top edges are not outside of the ImageView bounds.
        final float bitmapLeft = (transX < 0) ? Math.abs(transX) : 0;
        final float bitmapTop = (transY < 0) ? Math.abs(transY) : 0;

        RectF r = new RectF();
        matrix.mapRect(r);
        if (r.top <= 0 && r.left <= 0){
            pointF.set((bitmapLeft+x)/scaleX, (bitmapTop +y)/scaleY);
        } else if ( r.left <= 0){
            pointF.set((bitmapLeft+x)/scaleX,(bitmapTop +y-r.top)/scaleY);
        }else if (r.top <= 0 ){
            pointF.set((bitmapLeft+x-r.left)/scaleX,(bitmapTop +y)/scaleY);
        }else{
            pointF.set((bitmapLeft+x-r.left)/scaleX,(bitmapTop+y-r.top)/scaleY);
        }
        return pointF;
    }
}
