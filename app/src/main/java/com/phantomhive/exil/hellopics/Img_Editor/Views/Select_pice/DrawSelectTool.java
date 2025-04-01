package com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice;

import android.graphics.Canvas;

import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.SelectToolsHandel.SelectPaintUtil;
import com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.SelectToolsHandel.SelectTools;

public class DrawSelectTool {
    private final SelectPaintUtil mSelectPaintUtil = new SelectPaintUtil();


    // Draw Methods ////////////////////////////////////////////////////////////////////////////////

    /**
     * Draw
     * @param canvas to draw
     */
    private void drawRect(Canvas canvas) {
        canvas.drawRect(SelectTools.DrawRectOvalCropSelectArea(),
                mSelectPaintUtil.RectPaint());
    }

    /**
     * Draw
     * @param canvas to draw
     * @param scaleX
     */
    private void drawRectCorner(Canvas canvas, float scaleX) {
        canvas.drawRect(SelectTools.DrawRectOvalCropSelectArea(),
                mSelectPaintUtil.RectCornerPaint(scaleX));
    }

    /**
     * Draw
     * @param canvas to draw
     * @param scaleX
     */
    private void DrawPath(Canvas canvas, float scaleX) {
        /*
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawCircle(SelectTools.getFx(),SelectTools.getFy(),10,paint);
        Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2.setColor(Color.GREEN);
        canvas.drawCircle(SelectTools.getEx(),SelectTools.getEy(),10,paint2);
         */

        canvas.drawPath(SelectTools.DrawPathCropSelectArea(), mSelectPaintUtil.PathPaint(scaleX));
    }

    /**
     * Draw
     * @param canvas to draw
     */
    private void drawOval(Canvas canvas) {
        canvas.drawOval(SelectTools.DrawRectOvalCropSelectArea(),
                mSelectPaintUtil.RectPaint());
    }

    /**
     * Draw
     * @param canvas to draw
     * @param scaleX
     */
    private void drawOvalCorner(Canvas canvas, float scaleX) {
        canvas.drawOval(SelectTools.DrawRectOvalCropSelectArea(),
                mSelectPaintUtil.RectCornerPaint(scaleX));
    }

    /**
     * Draw
     * @param canvas to draw
     * @param scaleX
     */
    private void drawGlobalArea(Canvas canvas, float scaleX) {
        canvas.drawPath(SelectTools.getDrawCropPath(),
                mSelectPaintUtil.GlobalPathPaint(scaleX));
    }

    /**
     * @param canvas
     * @param cropCutShape
     * @param ActionFinished
     * @param scaleX
     * @param Mode
     */
    public void DrawTheSelectTool(Canvas canvas, CropCutShape cropCutShape, boolean ActionFinished, float scaleX, boolean Mode){
// TODO: 9/6/2024 for the select area
        if (!ActionFinished){
            if (cropCutShape == CropCutShape.RECTANGLE){
                drawRectCorner(canvas,scaleX);
            }else if (cropCutShape == CropCutShape.OVAL){
                drawOvalCorner(canvas,scaleX);

            }else if (cropCutShape == CropCutShape.FREE){
                DrawPath(canvas,scaleX);
            }
            if (Mode){
                drawGlobalArea(canvas,scaleX);
            }

        } else {
            drawGlobalArea(canvas,scaleX);
        }


    }
}
