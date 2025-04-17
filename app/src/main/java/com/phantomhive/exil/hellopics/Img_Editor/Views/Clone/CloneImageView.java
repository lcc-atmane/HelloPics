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
package com.phantomhive.exil.hellopics.Img_Editor.Views.Clone;

import static com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards.createCheckerboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.X_YFixer;

import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class CloneImageView extends View {

    String TAG= "CloneImageView ";

    private static float CIRCLE_STROKE_WIDTH = 5;
    PointF mCirclePoint = new PointF(-15,-15);
    PointF mCircleRealPoint = new PointF(-15,-15);

    ArrayList<PointF> pointF = new ArrayList<>();

    Path mPath = new Path();
    Path dmPath = new Path();

    Bitmap mBitmap,b;

    Canvas mCanvas;
    Paint mPaint;
    Paint dmPaint;
    Paint mLocPaint;

    X_YFixer xAndyFixer;
    ViewZommer zommer;

    float mBrushSize =30;

    float brushSize =30;
    float hardness = 5;

    private boolean mLocationDetect = true;
    boolean mLocationDraw = false;

    BitmapShader bitmapShader;
    BitmapShader dbitmapShader;

    Matrix matrix = new Matrix();
    Matrix dmatrix = new Matrix();

    boolean eraser = false;


    Bitmap bitmap;
    private Context mctx;

    Paint bmpaint;
    Paint paint;
    PorterDuff.Mode mode;

    Bitmap createCheckerboard;
    public CloneImageView(Context context) {
        super(context);
        init(context,null);
    }

    public CloneImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CloneImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CloneImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        mctx = context;
        zommer = new ViewZommer(this,context);

        mLocPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLocPaint.setColor(Color.parseColor("#A6A061"));
        mLocPaint.setStyle(Paint.Style.STROKE);
        mLocPaint.setStrokeWidth(CIRCLE_STROKE_WIDTH);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaint.setColor(-1);
        mPaint.setStrokeWidth(mBrushSize);

        dmPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dmPaint.setAntiAlias(true);
        dmPaint.setStyle(Paint.Style.STROKE);
        dmPaint.setStrokeJoin(Paint.Join.ROUND);
        dmPaint.setStrokeCap(Paint.Cap.ROUND);
        dmPaint.setFilterBitmap(true);
        dmPaint.setColor(-1);
        dmPaint.setStrokeWidth(mBrushSize);
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

        bmpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bmpaint.setAntiAlias(true);
        bmpaint.setFilterBitmap(true);

        paint = new Paint();
        paint.setAlpha(128);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);


    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setMatrix(zommer.getCanvasMatrix());
        if (createCheckerboard != null){
            canvas.drawBitmap(createCheckerboard,zommer.DefaultXY().x,zommer.DefaultXY().y,null);
        }
        canvas.drawBitmap(mBitmap,zommer.getDefaultMatrix(),bmpaint);
        canvas.drawPath(dmPath,dmPaint);
        if (!eraser){
            canvas.drawCircle(mCirclePoint.x,mCirclePoint.y ,
                    brushSize/zommer.getScaleX(),mLocPaint);
        }

        //canvas.drawPath(mPath,mPaint);
        if (eraser){
            canvas.drawBitmap(bitmap,zommer.getDefaultMatrix(),paint);
        }
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        zommer.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (bitmap.hasAlpha()){
            createCheckerboard= createCheckerboard((int) CheckerBoards.getBitmapRect(zommer.getDefaultMatrix(),bitmap,
                            getWidth(),getHeight()).width(),

                    (int) CheckerBoards.getBitmapRect(zommer.getDefaultMatrix(),bitmap,
                            getWidth(),getHeight()).height(),20);

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        xAndyFixer = new X_YFixer();
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        dbitmapShader = new BitmapShader(ViewZommer.getScaledBitmap(b, (int) getBitmapRect().width(), (int) getBitmapRect().height()), tileMode, tileMode);
    }


    public void setImageBitmap(Bitmap bm) {
        mBitmap = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());
        b = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        bitmapShader = new BitmapShader(b, tileMode, tileMode);
        mCanvas = new Canvas(mBitmap);
        zommer.setBitmap(bm);
    }

    public void setSourceImage(Bitmap bitmap){
     this.bitmap = bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        zommer.OnTouch(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (zommer.readyToDraw()){
                    X_YFixer xAndyFixers = new X_YFixer();
                    xAndyFixers.FixXYUsingMatrix(event.getX(),event.getY()
                            ,zommer.getCanvasMatrix());


                    mLocPaint.setStrokeWidth( 5/zommer.getScaleX());

                    mPaint.setStrokeWidth(brushSize/zommer.getBmScaleX());
                    dmPaint.setStrokeWidth(brushSize/zommer.getScaleX());
                    if (hardness == 0){
                        mPaint.setMaskFilter(null);
                        dmPaint.setMaskFilter(null);
                    }else {
                        mPaint.setMaskFilter(new BlurMaskFilter(hardness/zommer.getBmScaleX(), BlurMaskFilter.Blur.NORMAL));
                        dmPaint.setMaskFilter(new BlurMaskFilter(hardness/zommer.getScaleX(), BlurMaskFilter.Blur.NORMAL));
                    }


                    if(mLocationDetect){
                    mCirclePoint.set(xAndyFixers.getFixedXYUsingMartix().x,xAndyFixers.getFixedXYUsingMartix().y);
                    mCircleRealPoint.set( xAndyFixers.getFixedXYUsingMartix().x,xAndyFixers.getFixedXYUsingMartix().y);
                    mLocationDetect=false;
                }else{
                        mLocationDraw= true;
                    xAndyFixer.FixXYUsingMatrix( xAndyFixers.getFixedXYUsingMartix().x,xAndyFixers.getFixedXYUsingMartix().y
                            ,zommer.getDefaultMatrix());

                    mPath.moveTo(xAndyFixer.getFixedXYUsingMartix().x,xAndyFixer.getFixedXYUsingMartix().y);
                    dmPath.moveTo(xAndyFixers.getFixedXYUsingMartix().x,xAndyFixers.getFixedXYUsingMartix().y);
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (zommer.readyToDraw()){
                    mLocationDetect = false;
                    if (mLocationDraw){
                        X_YFixer xAndyFixers1 = new X_YFixer();
                        xAndyFixers1.FixXYUsingMatrix(event.getX(),event.getY()
                                ,zommer.getCanvasMatrix());
                        xAndyFixer.FixXYUsingMatrix( xAndyFixers1.getFixedXYUsingMartix().x,xAndyFixers1.getFixedXYUsingMartix().y,
                                zommer.getDefaultMatrix());

                        if (pointF.size()<2){
                            pointF.add(new PointF(xAndyFixer.getFixedXYUsingMartix().x,xAndyFixer.getFixedXYUsingMartix().y));
                            pointF.add(new PointF(xAndyFixers1.getFixedXYUsingMartix().x,xAndyFixers1.getFixedXYUsingMartix().y));

                            X_YFixer xAndyFixers = new X_YFixer();
                            xAndyFixers.FixXYUsingMatrix(mCircleRealPoint.x,mCircleRealPoint.y
                                    ,zommer.getDefaultMatrix());

                            /*
                            float sx,sy;
                            sx = (float) zommer.getScaledBitmap().getWidth()/mBitmap.getWidth();
                            sy = (float) zommer.getScaledBitmap().getHeight()/mBitmap.getHeight();
                            matrix.postScale(sx,sy)
                             */
                            if (!eraser){
                                matrix.setTranslate(pointF.get(0).x-xAndyFixers.getFixedXYUsingMartix().x,
                                        pointF.get(0).y-xAndyFixers.getFixedXYUsingMartix().y);
                                bitmapShader.setLocalMatrix(matrix);
                                mPaint.setShader(bitmapShader);


                                float dx = (pointF.get(1).x)-(mCircleRealPoint.x-zommer.DefaultXY().x);
                                float dy = (pointF.get(1).y)-(mCircleRealPoint.y-zommer.DefaultXY().y);
                                dmatrix.setTranslate(dx,dy);

                                dbitmapShader.setLocalMatrix(dmatrix);
                                dmPaint.setShader(dbitmapShader);
                            }
                            //dmatrix.setTranslate((pointF.get(1).x)-(mCircleRealPoint.x), (pointF.get(1).y*dy)-(mCircleRealPoint.y*dy));

                        }

                        float offsetX = pointF.get(1).x-mCircleRealPoint.x;
                        float offsetY = pointF.get(1).y-mCircleRealPoint.y;
                        mCirclePoint.set((xAndyFixers1.getFixedXYUsingMartix().x-offsetX),
                                (xAndyFixers1.getFixedXYUsingMartix().y-offsetY));

                        mPath.lineTo(xAndyFixer.getFixedXYUsingMartix().x,xAndyFixer.getFixedXYUsingMartix().y);
                        dmPath.lineTo(xAndyFixers1.getFixedXYUsingMartix().x,xAndyFixers1.getFixedXYUsingMartix().y);
                        Log.d(TAG, "onTouchEvent: jjjjjjjjjjj");
                    }
                }
                //setImageBitmap(getFinalBitmap());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mCanvas.drawPath(mPath,mPaint);
                if (pointF.size()>0&&!eraser){
                    b = Bitmap.createBitmap(getFinalBitmap(),0,0,getFinalBitmap().getWidth(),getFinalBitmap().getHeight());
                    Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                    dbitmapShader = new BitmapShader(ViewZommer.getScaledBitmap(b, (int) getBitmapRect().width(), (int) getBitmapRect().height()), tileMode, tileMode);
                    float dy = (pointF.get(1).y)-(float)(mCircleRealPoint.y-zommer.DefaultXY().y);
                    float dx = (pointF.get(1).x)-(float)(mCircleRealPoint.x-zommer.DefaultXY().x);
                    dmatrix.setTranslate(dx,dy);
                    dbitmapShader.setLocalMatrix(dmatrix);
                    dmPaint.setShader(dbitmapShader);
                }


                setImageBitmap(getFinalBitmap());

                if (!pointF.isEmpty()){
                   if (!eraser){
                       X_YFixer xAndyFixers = new X_YFixer();xAndyFixers.FixXYUsingMatrix(mCircleRealPoint.x,mCircleRealPoint.y
                               ,zommer.getDefaultMatrix());
                       matrix.setTranslate(pointF.get(0).x-xAndyFixers.getFixedXYUsingMartix().x,
                               pointF.get(0).y-xAndyFixers.getFixedXYUsingMartix().y);
                       bitmapShader.setLocalMatrix(matrix);
                       mPaint.setShader(bitmapShader);
                   }

               }
                mPath.reset();
                dmPath.reset();
                invalidate();
                break;
        }
        return true;
    }



    public void getLocDetect(boolean b) {
        eraser = false;
        if (mode == null){
            mPaint.setXfermode(null);
            dmPaint.setXfermode(null);
        }else {
            mPaint.setXfermode(new PorterDuffXfermode(mode));
            dmPaint.setXfermode(new PorterDuffXfermode(mode));
        }
        setImageBitmap(getFinalBitmap());
        mLocationDetect = b;
        mLocationDraw = false;
        //mCirclePoint = new PointF(-15,-15);
        //mCircleRealPoint = new PointF(-15,-15);
        pointF.clear();
        mPath.reset();
        invalidate();
    }

    public void seCloneMode(PorterDuff.Mode mode) {
        this.mode = mode;
        if (mode == null){
            mPaint.setXfermode(null);
            dmPaint.setXfermode(null);
        }else {
            mPaint.setXfermode(new PorterDuffXfermode(mode));
            dmPaint.setXfermode(new PorterDuffXfermode(mode));
        }

    }
    public void getLocDraw(boolean d) {
        eraser = false;
        if (mode == null){
            mPaint.setXfermode(null);
            dmPaint.setXfermode(null);
        }else {
            mPaint.setXfermode(new PorterDuffXfermode(mode));
            dmPaint.setXfermode(new PorterDuffXfermode(mode));
        }

        if (pointF.size()>0){
            b = Bitmap.createBitmap(getFinalBitmap(),0,0,getFinalBitmap().getWidth(),getFinalBitmap().getHeight());
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            dbitmapShader = new BitmapShader(ViewZommer.getScaledBitmap(b, (int) getBitmapRect().width(), (int) getBitmapRect().height()), tileMode, tileMode);
            float dy = (pointF.get(1).y)- (mCircleRealPoint.y-zommer.DefaultXY().y);
            float dx = (pointF.get(1).x)- (mCircleRealPoint.x-zommer.DefaultXY().x);
            dmatrix.setTranslate(dx,dy);
            dbitmapShader.setLocalMatrix(dmatrix);
            dmPaint.setShader(dbitmapShader);

            X_YFixer xAndyFixers = new X_YFixer();
            xAndyFixers.FixXYUsingMatrix(mCircleRealPoint.x,mCircleRealPoint.y
                    ,zommer.getDefaultMatrix());
            matrix.setTranslate(pointF.get(0).x-xAndyFixers.getFixedXYUsingMartix().x,
                    pointF.get(0).y-xAndyFixers.getFixedXYUsingMartix().y);
            bitmapShader.setLocalMatrix(matrix);
            mPaint.setShader(bitmapShader);

        }
        mLocationDetect = false;
        mLocationDraw = d;
        invalidate();
    }

    public Bitmap getFinalBitmap(){
        return mBitmap;
    }

    public void setEraser(boolean eraser){
        this.eraser =eraser;

        mPaint.setXfermode(null);
        dmPaint.setXfermode(null);

        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        bitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
        dbitmapShader = new BitmapShader(ViewZommer.getScaledBitmap(bitmap, (int) getBitmapRect().width(), (int) getBitmapRect().height()), tileMode, tileMode);

        matrix.setTranslate(0,0);
        bitmapShader.setLocalMatrix(matrix);
        mPaint.setShader(bitmapShader);

        matrix.setTranslate(zommer.DefaultXY().x,zommer.DefaultXY().y);
        dbitmapShader.setLocalMatrix(matrix);
        dmPaint.setShader(dbitmapShader);
        invalidate();
    }

    public void setCloneSize(int Size) {
        brushSize = Size;
        invalidate();
    }

    public void setCloneOpacity(int Opacity) {
        mPaint.setAlpha(Opacity);
        dmPaint.setAlpha(Opacity);
        invalidate();
    }

    public void setCloneHardness(int Hardness) {
        hardness = Hardness;
        invalidate();
    }

    /*
   Returns the bitmap position inside an imageView.
   * @param imageView source ImageView
   * @return Rect position of the bitmap in the ImageView
     */
    public  final RectF getBitmapRect()
    {
        RectF rect = new RectF();

        final Bitmap drawable = mBitmap;
        if ( drawable == null)
        {
            return rect;
        }

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        zommer.getDefaultMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Bitmap d     = mBitmap;
        final int      origW = d.getWidth();
        final int      origH = d.getHeight();

        // Calculate the actual dimensions
        final float actW = origW * scaleX;
        final float actH = origH * scaleY;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = getWidth();
        int imgViewH = getHeight();

        rect.top  =  (imgViewH - actH) / 2;
        rect.left =  (imgViewW - actW) / 2;

        rect.bottom = rect.top + actH;
        rect.right  = rect.left + actW;

        return rect;
    }

}
 /*
                double dis;
                    dis=Math.sqrt((pointF.get(1).x-LocXX)*(pointF.get(1).x-LocXX) + (pointF.get(1).y-LocYY)*(pointF.get(1).y-LocYY));
                 // Define the circular area to be copied
                int centerX = 50;
                int centerY = 50;
                int radius = 20;
                // Loop through each pixel in the circular area
                for (int x = centerX - radius; x <= centerX + radius; x++) {
                    for (int y = centerY - radius; y <= centerY + radius; y++) {
                        if (Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)) <= radius) {
                            // If the pixel is within the circle, copy it to the new bitmap
                            int color = mBitmap.getPixel(x, y);
                            mBitmap.setPixel(x - (centerX - radius), y - (centerY - radius), color);
                        }
                    }
                }

 private Bitmap getCopiedOvalImage() {
     final Drawable drawable = getDrawable();
     if (drawable == null || !(drawable instanceof BitmapDrawable)) {
         return null;
     }

     //Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
     Bitmap output0 = ((BitmapDrawable) drawable).getBitmap();
     //output.setConfig(Bitmap.Config.ARGB_8888);
     Bitmap output = Bitmap.createBitmap(output0.getWidth(), output0.getHeight(), Bitmap.Config.ARGB_8888);
     Canvas canvas = new Canvas(output);

     // Calculate The real height and width of The Selected Area.
     final float cropWidth =(25*2);
     final float cropHeight = (25*2);

     final int color = 0xff424242;
     final Paint paint = new Paint();
     final Rect rect = new Rect(0, 0, output0.getWidth(), output0.getHeight());
     // For more info visit : https://stackoverflow.com/a/12089127.
     paint.setAntiAlias(true);
     canvas.drawARGB(0, 0, 0, 0);
     paint.setColor(color);

     canvas.drawCircle(DownX,DownY, 25, paint);
     paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
     //canvas.drawColor(Color.YELLOW);
     canvas.drawBitmap(output0, rect, rect, paint);
     return Bitmap.createBitmap(output,(int) (DownX)-25,(int) (DownY)-25,(int) (cropWidth),(int) (cropHeight));

 }

    public Bitmap getCopiedOvalImageh() {
        final Drawable drawable = getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);Bitmap.createBitmap(output,(int) (DownX),(int) (DownY),(int) (cropWidth),(int) (cropHeight))
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
                */

               /*
                //Get the color of the pixels within the brush size
                   for (int i = -mBrushSize; i <= mBrushSize; i++) {
                    for (int j = -mBrushSize; j <= mBrushSize; j++) {
                        if (Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2)) <= mBrushSize) {
                        int color = mBitmap.getPixel((int)DownX + i+ mCloneOffsetX,
                                (int)DownY + j+ mCloneOffsetY);
                        paint.setColor(color);
                        mBitmap.setPixel((int)xAndyFixer.getFixedXYUsingMartix().x + i,
                                (int)xAndyFixer.getFixedXYUsingMartix().y + j, color);

                        }
                    }
                }
                  */

   /*
                      canvasff.drawPath(path,paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    canvasff.drawBitmap(mBitmapkk,0,0,paint);
                    //canvasff.drawColor(Color.YELLOW);
                    xAndyFixer xAndyFixers = new xAndyFixer();
                    xAndyFixers.FixXYUsingMatrix(LocXX,LocYY,getImageMatrix());
                    canvasf.drawBitmap(mBitmapm,pointF.get(0).x-xAndyFixers.getFixedXYUsingMartix().x,
                            pointF.get(0).y-xAndyFixers.getFixedXYUsingMartix().y,null);

                     */