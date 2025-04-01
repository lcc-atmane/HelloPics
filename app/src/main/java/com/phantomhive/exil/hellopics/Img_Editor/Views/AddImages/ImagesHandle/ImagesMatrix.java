package com.phantomhive.exil.hellopics.Img_Editor.Views.AddImages.ImagesHandle;



import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;

public class ImagesMatrix {

     static int Index;
    ArrayList<MatrixData> ImagesMatrixData = new ArrayList<>();

    public void AddMatrix(Matrix matrix,int rV,PointF drawXyScale,PointF bmXyScale){
        ImagesMatrixData.add(new MatrixData(matrix,rV,drawXyScale,bmXyScale));
    }


    public void updatePosition(float x, float y, Bitmap copy){

        ImagesMatrixData.get(getIndex()).setPosition_Rotate_Scale(x,y,ImagesMatrixData.get(getIndex()).getRotateV(),
                ImagesMatrixData.get(getIndex()).getScaleV(),
                ImagesMatrixData.get(getIndex()).getRealScaleV(),
                copy);

    }

    public  void setRotation(int degree,float x, float y, Bitmap copy){

        ImagesMatrixData.get(getIndex()).setPosition_Rotate_Scale(x,y,degree,
                ImagesMatrixData.get(getIndex()).getScaleV(),
                ImagesMatrixData.get(getIndex()).getRealScaleV(),
                copy);


    }

    public  void setTRotation(int Index,int degree,float x, float y, Bitmap copy){

        ImagesMatrixData.get(Index).setTRotate(x,y,degree,
                ImagesMatrixData.get(Index).getScaleV(),
                ImagesMatrixData.get(Index).getRealScaleV(),
                copy);
    }

    /*
    public void setTRotation(float degree,float x, float y, Bitmap copy){
        ImagesMatrixData.get(getIndex()).setTRotate(x,y,degree,
                ImagesMatrixData.get(getIndex()).getScaleV(),
                ImagesMatrixData.get(getIndex()).getRealScaleV(),
                copy);
    }
     */


    public void setScale(PointF scale, PointF rscale, float x, float y, Bitmap copy) {

        ImagesMatrixData.get(getIndex()).setPosition_Rotate_Scale(x,y,ImagesMatrixData.get(getIndex()).getRotateV(),
                scale,rscale,copy);
    }

    public  void setRotationAndScaleAndTranslate(int Index, Bitmap copy, float x, float y){

        ImagesMatrixData.get(Index).setPosition_Rotate_Scale(x,y,ImagesMatrixData.get(Index).getRotateV(),
                ImagesMatrixData.get(Index).getRealScaleV(),ImagesMatrixData.get(Index).getRealScaleV(),copy);

    }

    public int getRotationDegree(){
        return ImagesMatrixData.get(getIndex()).getRotateV();
    }

    public  PointF getScaleValue(){
        return ImagesMatrixData.get(getIndex()).getScaleV();
    }

    public  PointF getRScaleValue(){
        return ImagesMatrixData.get(getIndex()).getRealScaleV();
    }

    public  ArrayList<MatrixData> getMatrixs() {
        return ImagesMatrixData;
    }

    public  Matrix getMatrix() {
        return ImagesMatrixData.get(getIndex()).getMatrix();
    }

    public  int Size() {
        return ImagesMatrixData.size();
    }

    public  void removeMatrix(){
        ImagesMatrixData.remove(getIndex());

        Index = ImagesMatrixData.size()-1;
    }

    public  void clearMatrices (){
        ImagesMatrixData.clear();

    }


    public  void setIndex(int index) {
        Index = index;
    }

    public static int getIndex() {
        return Index;
    }

    public  void unloadingIndex(){
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

    public class MatrixData{
        Matrix matrix;
        int RotateV;
        PointF scaleV;
        PointF RealScaleV;

        public MatrixData(Matrix matrix, int rotateV, PointF scaleV, PointF realScaleV) {
            this.matrix = matrix;
            this.RotateV = rotateV;
            this.scaleV = scaleV;
            this.RealScaleV = realScaleV;
        }

        public Matrix getMatrix() {
            return matrix;
        }

        public void setMatrix(Matrix matrix) {
            this.matrix = matrix;
        }

        public void setPosition_Rotate_Scale(float x, float y, int RotateV,PointF scale, PointF rscale,  Bitmap copy){
            float scaleImageCenterX =  (float) (copy.getWidth()) / 2;
            float scaleImageCenterY = (float) (copy.getHeight()) / 2;

            setScaleV(scale);
            setRealScaleV(rscale);
            setRotateV(RotateV);

            float divx = 1f-getScaleV().x;
            float divy = 1f-getScaleV().y;

            float fx = (scaleImageCenterX*divx);
            float fy = (scaleImageCenterY*divy);

            matrix.reset();
            matrix.postRotate(getRotateV(),scaleImageCenterX,scaleImageCenterY);
            matrix.postScale(getScaleV().x,getScaleV().y,scaleImageCenterX,scaleImageCenterY);
            matrix.postTranslate(x,y);
            //-fx  -fy
        }

        public void setTRotate(float x, float y,int RotateV,PointF scale, PointF rscale,  Bitmap copy){
            float scaleImageCenterX =  (copy.getWidth()) / 2f;
            float scaleImageCenterY = (copy.getHeight()) / 2f;


            float divx = 1f-getScaleV().x;
            float divy = 1f-getScaleV().y;

            float fx = (scaleImageCenterX*divx);
            float fy = (scaleImageCenterY*divy);



            matrix.reset();
            matrix.postRotate(RotateV,scaleImageCenterX,scaleImageCenterY);
            matrix.postScale(getScaleV().x,getScaleV().y,scaleImageCenterX,scaleImageCenterY);
            matrix.postTranslate(x,y);


        }
        public int getRotateV() {
            return RotateV;
        }

        public void setRotateV(int rotateV) {
            RotateV = rotateV;
        }

        public PointF getScaleV() {
            return scaleV;
        }

        public void setScaleV(PointF scaleV) {
            this.scaleV = scaleV;
        }

        public PointF getRealScaleV() {
            return RealScaleV;
        }

        public void setRealScaleV(PointF realScaleV) {
            RealScaleV = realScaleV;
        }
    }
}
