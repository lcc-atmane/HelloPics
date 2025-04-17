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
package com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.SelectToolsHandel;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

public class SelectPaintUtil {

    public Paint RectPaint (){
        Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setColor(Color.parseColor("#635b05"));
        mRectPaint.setAlpha(100);
        return mRectPaint;
    }

    public Paint RectCornerPaint(float scaleX){
        float CORNER_STROKE_WIDTH = 3f;
        Paint mRectCornerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectCornerPaint.setColor(Color.RED);
        mRectCornerPaint.setStyle(Paint.Style.STROKE);
        mRectCornerPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        mRectCornerPaint.setStrokeWidth(CORNER_STROKE_WIDTH/scaleX);
        return mRectCornerPaint;
    }

    public Paint PathPaint(float scaleX){
        float STROKE_WIDTH = 2f;
        Paint mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setColor(Color.RED);
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStrokeWidth(STROKE_WIDTH/scaleX);
        mPathPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);
        return mPathPaint;
    }

    public Paint GlobalPathPaint(float scaleX){
        Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setColor(Color.parseColor("#635b05"));
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStrokeJoin(Paint.Join.ROUND);
        mRectPaint.setStrokeCap(Paint.Cap.ROUND);
        mRectPaint.setAlpha(100);
        return mRectPaint;
    }
}
