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
