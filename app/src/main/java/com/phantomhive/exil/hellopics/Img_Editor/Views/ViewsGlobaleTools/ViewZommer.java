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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class ViewZommer {
    //Zoom Variables
    Matrix matrix = new Matrix();
    Matrix Dmatrix = new Matrix();
    Matrix SMatrix = new Matrix();

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    PointF last = new PointF();
    PointF start = new PointF();
    float minScale = 1f;
    float maxScale = 20f;
    float[] m;

    float redundantXSpace, redundantYSpace;
    float width, height;
    float saveScale = 1f;
    float right, bottom, origWidth, origHeight, bmWidth, bmHeight;

    ScaleGestureDetector mScaleDetector;
    PointF curr;
    int PointerNumber;
    int isScaleendreal;
    float scaleHeight;
    float scaleWidth;

    View imageView;
    private Bitmap mBitmap;

    Matrix mCanvasMatrix = new Matrix();


    public ViewZommer(View View, Context context){
        this.imageView = View;
        mScaleDetector = new ScaleGestureDetector(context, new ViewZommer.ScaleListener());
        matrix.setTranslate(1f, 1f);
        m = new float[9];
    }
    public ViewZommer(){
    }

    public void setBitmap(Bitmap bm){
        mBitmap = bm;
        bmWidth = bm.getWidth();
        bmHeight = bm.getHeight();
    }
    public Bitmap getEmptyScaledBitmap(){

        float division0 = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();
        float division1 = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();
        int FW = (int) (imageView.getHeight()*division1);
        int FH = (int) (imageView.getWidth()*division0);
        Bitmap mSBitmap = null;
        if (mBitmap.getWidth()>mBitmap.getHeight()| mBitmap.getWidth()==mBitmap.getHeight()){
            mSBitmap = Bitmap.createBitmap(
                    imageView.getWidth() ,FH ,mBitmap.getConfig());


        }else if (mBitmap.getWidth()<mBitmap.getHeight() ){
            if (FH<imageView.getHeight()) {
                mSBitmap = Bitmap.createBitmap(
                        imageView.getWidth() ,FH ,mBitmap.getConfig());

            }else {
                mSBitmap = Bitmap.createBitmap(FW,
                        imageView.getHeight() ,mBitmap.getConfig());

            }
        }
        return mSBitmap;
    }

    public Bitmap getScaledBitmap(){

        float division0 = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();
        float division1 = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();
        int FW = (int) (imageView.getHeight()*division1);
        int FH = (int) (imageView.getWidth()*division0);
        Bitmap mSBitmap = null;
        if (mBitmap.getWidth()>mBitmap.getHeight()| mBitmap.getWidth()==mBitmap.getHeight()){
            mSBitmap = Bitmap.createScaledBitmap(mBitmap,
                    imageView.getWidth() ,FH ,true);

        }else if (mBitmap.getWidth()<mBitmap.getHeight() ){
            if (FH<imageView.getHeight()) {
                mSBitmap = Bitmap.createScaledBitmap(mBitmap,
                        imageView.getWidth() ,FH ,true);

            }else {
                mSBitmap = Bitmap.createScaledBitmap(mBitmap,FW,
                        imageView.getHeight() ,true);
            }
        }
        return mSBitmap;
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // Convert the input bitmap to a byte array stream
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // Decode the byte array to obtain the image dimensions
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }

    public static Bitmap getScaledBitmapBasedOnView(Bitmap mBitmap, View View){
        float division0 = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();
        float division1 = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();
        int FW = (int) (View.getHeight()*division1);
        int FH = (int) (View.getWidth()*division0);
        Bitmap mSBitmap = null;
        if (mBitmap.getWidth()>mBitmap.getHeight()| mBitmap.getWidth()==mBitmap.getHeight()){
            mSBitmap = Bitmap.createScaledBitmap(mBitmap,
                    View.getWidth() ,FH ,true);


        }else if (mBitmap.getWidth()<mBitmap.getHeight() ){
            if (FH<View.getHeight()) {
                mSBitmap = Bitmap.createScaledBitmap(mBitmap,
                        View.getWidth() ,FH ,true);

            }else {
                mSBitmap = Bitmap.createScaledBitmap(mBitmap,FW,
                        View.getHeight() ,true);

            }
        }
        return Bitmap.createBitmap(mSBitmap,0,0,mSBitmap.getWidth(),
                mSBitmap.getHeight());
    }

    public static Bitmap getScaledBitmapBasedOnScreen(Bitmap mBitmap, int Width,int Height){
        float division0 = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();
        float division1 = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();
        int FW = (int) (Height*division1);
        int FH = (int) (Width*division0);
        Bitmap mSBitmap = null;
        if (mBitmap.getWidth()>mBitmap.getHeight()| mBitmap.getWidth()==mBitmap.getHeight()){
            mSBitmap = Bitmap.createScaledBitmap(mBitmap,
                    Width ,FH ,true);


        }else if (mBitmap.getWidth()<mBitmap.getHeight() ){
            if (FH<Height) {
                mSBitmap = Bitmap.createScaledBitmap(mBitmap,
                        Width ,FH ,true);

            }else {
                mSBitmap = Bitmap.createScaledBitmap(mBitmap,FW,
                        Height ,true);

            }
        }
        return Bitmap.createBitmap(mSBitmap,0,0,mSBitmap.getWidth(),
                mSBitmap.getHeight());
    }
    public static Bitmap getScaledBitmap(Bitmap mBitmap, int scaledWidth,int scaledHeight) {

        // Scale the bitmap
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mBitmap, scaledWidth, scaledHeight, true);

        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());
    }



    public void onDraw(RectF rectF){
        mCanvasMatrix.setTranslate(rectF.left, rectF.top);
    }


    public void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {

        width = View.MeasureSpec.getSize(widthMeasureSpec);
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        //Fit to screen.
        float scale;
        float scaleX =  width / bmWidth;
        float scaleY = height / bmHeight;
        scale = Math.min(scaleX, scaleY);


        matrix.setScale(scale, scale);
        Dmatrix.setScale(scale, scale);
        mCanvasMatrix.setScale(1, 1);
        SMatrix.setScale(scale,scale);

        saveScale = 1f;

        // Center the image
        redundantYSpace = height - (scale * bmHeight) ;
        redundantXSpace = width - (scale * bmWidth);
        redundantYSpace /= 2;
        redundantXSpace /= 2;

        matrix.postTranslate(redundantXSpace, redundantYSpace);
        Dmatrix.postTranslate(redundantXSpace, redundantYSpace);
        mCanvasMatrix.postTranslate(0, 0);
        SMatrix.postTranslate(redundantXSpace, redundantYSpace);

        origWidth = width - 2 * redundantXSpace;
        origHeight = height - 2 * redundantYSpace;
        right = width * saveScale - width - (2 * redundantXSpace * saveScale);
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
    }

    public void onSizeChanged(int w, int h){

    }
    public PointF DefaultXY(){
        return new PointF(redundantXSpace, redundantYSpace);
    }


    public boolean OnTouch(MotionEvent event){
        mScaleDetector.onTouchEvent(event);
        matrix.getValues(m);
        mCanvasMatrix.getValues(m);
        SMatrix.getValues(m);

        float x = m[Matrix.MTRANS_X];
        float y = m[Matrix.MTRANS_Y];
        curr = new PointF(event.getX(), event.getY());

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN -> {
                PointerNumber = event.getPointerCount();
                last.set(event.getX(), event.getY());
                start.set(last);
                mode = ZOOM;
                return true;
            }
            case MotionEvent.ACTION_POINTER_DOWN -> {
                //Log.d(TAG,"POINERDOWN  : "+ event.getActionIndex());
                isScaleendreal = 2;
                return true;
            }
            case MotionEvent.ACTION_MOVE -> {
                PointerNumber = event.getPointerCount();
                if (PointerNumber >= 2) {
                    zomPointerMove(x, y);
                }
                return true;
            }
            case MotionEvent.ACTION_UP -> {
                PointerNumber = event.getPointerCount();
                isScaleendreal = event.getActionIndex();
                return true;
            }
            case MotionEvent.ACTION_POINTER_UP -> {
                PointerNumber = event.getPointerCount();
                isScaleendreal = 1;
                zomPointerUp();
                return true;
            }
        }
        return false;
    }
    private void zomPointerMove(float x, float y){
        //if the mode is ZOOM or
        //if the mode is DRAG and already zoomed
        if (mode == ZOOM || (mode == DRAG && saveScale > minScale))
        {
            float deltaX = curr.x - last.x;// x difference
            float deltaY = curr.y - last.y;// y difference


            scaleWidth = Math.round(origWidth * saveScale);// width after applying current scale
            scaleHeight = Math.round(origHeight * saveScale);// height after applying current scale
            //if scaleWidth is smaller than the views width
            //in other words if the image width fits in the view
            //limit left and right movement

            //todo:for stable translate
            /*
            if (scaleWidth < width)
            {
                deltaX = 0;
                if (y + deltaY > 0)
                    deltaY = -y;
                else if (y + deltaY < -bottom)
                    deltaY = -(y + bottom);
            }
            //if scaleHeight is smaller than the views height
            //in other words if the image height fits in the view
            //limit up and down movement
            else if (scaleHeight < height)
            {
                deltaY = 0;
                if (x + deltaX > 0)
                    deltaX = -x;
                else if (x + deltaX < -right)
                    deltaX = -(x + right);
            }
            //if the image doesnt fit in the width or height
            //limit both up and down and left and right
            else
            {
                if (x + deltaX > 0)
                    deltaX = -x;
                else if (x + deltaX < -right)
                    deltaX = -(x + right);

                if (y + deltaY > 0)
                    deltaY = -y;
                else if (y + deltaY < -bottom)
                    deltaY = -(y + bottom);
            }
             */

            //move the image with the matrix
            matrix.postTranslate(deltaX, deltaY);
            mCanvasMatrix.postTranslate(deltaX, deltaY);
            SMatrix.postTranslate(deltaX,deltaY);
            //set the last touch location to the current
            last.set(curr.x, curr.y);
        }
    }
    /**
     *
     */
    private void zomPointerUp() {
        mode = NONE;
    }

    public Matrix getDrawnBmMatrix() {
        return SMatrix;
    }


    /**
     *
     */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector)
        {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();

            float origScale = saveScale;
            saveScale *= mScaleFactor;

            if (saveScale > maxScale)
            {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            }
            else if (saveScale < minScale)
            {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }
            right = width * saveScale - width - (2 * redundantXSpace * saveScale);
            bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
            if (origWidth * saveScale <= width || origHeight * saveScale <= height)
            {
                matrix.postScale(mScaleFactor, mScaleFactor, width / 2, height / 2);
                mCanvasMatrix.postScale(mScaleFactor, mScaleFactor, width / 2, height / 2);
                SMatrix.postScale(mScaleFactor, mScaleFactor, width / 2, height / 2);
                //todo:for stable translate
               /*
               if (mScaleFactor < 1)
                {
                    matrix.getValues(m);
                    float x = m[Matrix.MTRANS_X];
                    float y = m[Matrix.MTRANS_Y];
                    if (mScaleFactor < 1)
                    {
                        if (Math.round(origWidth * saveScale) < width)
                        {
                            if (y < -bottom)
                                matrix.postTranslate(0, -(y + bottom));
                            else if (y > 0)
                                matrix.postTranslate(0, -y);
                        }
                        else
                        {
                            if (x < -right)
                                matrix.postTranslate(-(x + right), 0);
                            else if (x > 0)
                                matrix.postTranslate(-x, 0);
                        }
                    }
                }
                */

            }
            else
            {
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
                matrix.getValues(m);

                mCanvasMatrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
                mCanvasMatrix.getValues(m);

                SMatrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
                SMatrix.getValues(m);

                //todo:for stable translate
                /*
                float x = m[Matrix.MTRANS_X];
                float y = m[Matrix.MTRANS_Y];
                if (mScaleFactor < 1) {
                    if (x < -right)
                        matrix.postTranslate(-(x + right), 0);
                    else if (x > 0)
                        matrix.postTranslate(-x, 0);
                    if (y < -bottom)
                        matrix.postTranslate(0, -(y + bottom));
                    else if (y > 0)
                        matrix.postTranslate(0, -y);
                }
                 */

            }
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }
    }

    /*
     public void ReturnBitmapToPosition(){
        float x = m[Matrix.MTRANS_X];
        float y = m[Matrix.MTRANS_Y];
        if (scaleWidth < width)
        {
            deltaX1 = 0;
            if (y + deltaY1 > 0)
                deltaY1 = -y;
            else if (y + deltaY1 < -bottom)
                deltaY1 = -(y + bottom);
        }
        //if scaleHeight is smaller than the views height
        //in other words if the image height fits in the view
        //limit up and down movement
        else if (scaleHeight < height)
        {
            deltaY1 = 0;
            if (x + deltaX1 > 0)
                deltaX1 = -x;
            else if (x + deltaX1 < -right)
                deltaX1 = -(x + right);
        }
        //if the image doesnt fit in the width or height
        //limit both up and down and left and right
        else
        {
            if (x + deltaX1 > 0)
                deltaX1 = -x;
            else if (x + deltaX1 < -right)
                deltaX1 = -(x + right);

            if (y + deltaY1 > 0)
                deltaY1 = -y;
            else if (y + deltaY1 < -bottom)
                deltaY1 = -(y + bottom);
        }

        matrix.postTranslate(deltaX1, deltaY1);
    }
     */

    public Matrix getCanvasMatrix() {
        return mCanvasMatrix;
    }
    public float getSaveScale() {
        return saveScale;
    }

    public float getScaleX(){
        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        mCanvasMatrix.getValues(matrixValues);

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        return matrixValues[Matrix.MSCALE_X];
    }

    public float getScaleY(){
        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        mCanvasMatrix.getValues(matrixValues);

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        return matrixValues[Matrix.MSCALE_Y];
    }

    public float getBmScaleX(){
        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        matrix.getValues(matrixValues);

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        return matrixValues[Matrix.MSCALE_X];
    }

    public float getBmScaleY(){
        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        matrix.getValues(matrixValues);

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        return matrixValues[Matrix.MSCALE_Y];
    }

    public Matrix getDefaultMatrix() {
        return Dmatrix;
    }

    public boolean readyToDraw(){
        return isScaleendreal == 0;
    }
    public boolean Draw(){
        return false;
    }


    public float getDBmScaleX(){
        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        Dmatrix.getValues(matrixValues);

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        return matrixValues[Matrix.MSCALE_X];
    }

    public float getDBmScaleY(){
        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        Dmatrix.getValues(matrixValues);

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        return matrixValues[Matrix.MSCALE_Y];
    }



    private int calculateCompressionRatio(Bitmap originalBitmap, long desiredMaxSizeInBytes) {

        Bitmap RealBitmap = Bitmap.createBitmap((int) bmWidth, (int) bmHeight, Bitmap.Config.ALPHA_8);
        int compressionRatio2 = calculateCompressionRatio(RealBitmap, 1000000L);

        // Create a ByteArrayOutputStream to compress the Bitmap
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        RealBitmap.compress(Bitmap.CompressFormat.JPEG, compressionRatio2, byteArrayOutputStream2);
        // Convert the compressed data back to a Bitmap
        byte[] compressedData = byteArrayOutputStream2.toByteArray();
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(compressedData, 0, compressedData.length);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int compressionRatio = 100; // Start with the highest quality

        do {
            byteArrayOutputStream.reset(); // Reset the stream
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, compressionRatio, byteArrayOutputStream);
            compressionRatio -= 10; // Reduce the quality by 10 each time
        } while (
                byteArrayOutputStream.size() > desiredMaxSizeInBytes
                && compressionRatio > 0);

        return compressionRatio;
    }
}