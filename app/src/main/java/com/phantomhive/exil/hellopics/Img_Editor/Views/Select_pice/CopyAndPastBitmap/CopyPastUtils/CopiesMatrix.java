package com.phantomhive.exil.hellopics.Img_Editor.Views.Select_pice.CopyAndPastBitmap.CopyPastUtils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;

import java.util.ArrayList;

public class CopiesMatrix {

    ArrayList<MatrixData> ImagesMatrixData = new ArrayList<>();

    static private int Index;


    public  void setIndex(int index) {
        Index = index;
    }

    public static int getIndex() {
        return Index;
    }

    public void unloadingIndex(){
        Index = 0;
    }

    /**
     * remove the previous Copies
     * @param index index of Paints
     */
    public void unloadingIndex(int index){
        Index = index;
    }

    public void AddMatrix(Matrix matrix,int v,PointF drawXyScale,PointF bmXyScale){
        ImagesMatrixData.add(new MatrixData(matrix,v,drawXyScale,bmXyScale));
    }

    public ArrayList<MatrixData> getCopiesMatrixList() {
        return ImagesMatrixData;
    }



    public MatrixData CopyMatrix(){
        return ImagesMatrixData.get(getIndex());
    }

    public void removeMatrix(){
        ImagesMatrixData.remove(getIndex());
        Index = ImagesMatrixData.size()-1;
    }

    public void updatePosition(float x, float y, Bitmap copy){
        ImagesMatrixData.get(getIndex()).setPosition_Rotate_Scale(x,y,ImagesMatrixData.get(getIndex()).getRotateV(),
                ImagesMatrixData.get(getIndex()).getScaleV(),
                ImagesMatrixData.get(getIndex()).getRealScaleV(),
                copy);
    }

    public void setRotation(int degree, Bitmap copy,float x, float y){
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
      public void setTRotation(float degree, Bitmap copy,float x, float y){
        ImagesMatrixData.get(getIndex()).setTRotate(x,y,degree,
                ImagesMatrixData.get(getIndex()).getScaleV(),
                ImagesMatrixData.get(getIndex()).getRealScaleV(),
                copy);
    }
     */

    public void setScale(PointF scale,PointF rscale, float x, float y, Bitmap copy) {
        ImagesMatrixData.get(getIndex()).setPosition_Rotate_Scale(x,y,ImagesMatrixData.get(getIndex()).getRotateV(),
                scale,rscale,copy);
    }

    public void setRotationAndScaleAndTranslate(int Index, Bitmap copy, float x, float y){
        ImagesMatrixData.get(Index).setPosition_Rotate_Scale(x,y,ImagesMatrixData.get(Index).getRotateV(),
                ImagesMatrixData.get(Index).getRealScaleV(),ImagesMatrixData.get(Index).getRealScaleV(),copy);
    }


    public int getRotationDegree(){
        return ImagesMatrixData.get(getIndex()).getRotateV();
    }

    public PointF getScaleValue(){
        return ImagesMatrixData.get(getIndex()).getScaleV();
    }
    public PointF getRScaleValue(){
        return ImagesMatrixData.get(getIndex()).getRealScaleV();
    }

    public int size (){
        return ImagesMatrixData.size();
    }
    public void clearMatrices(){
        ImagesMatrixData.clear();
    }


    public class MatrixData{
        Matrix matrix;
        int RotateV;
        PointF scaleV;
        PointF RealScaleV;

        public MatrixData(Matrix matrix, int rotateV, PointF scaleV, PointF realScaleV) {
            this.matrix = matrix;
            RotateV = rotateV;
            this.scaleV = scaleV;
            RealScaleV = realScaleV;
        }

        public Matrix getMatrix() {
            return matrix;
        }

        public void setMatrix(Matrix matrix) {
            this.matrix = matrix;
        }

        public void setPosition_Rotate_Scale(float x, float y,int RotateV,PointF scale, PointF rscale,  Bitmap copy){
            setScaleV(scale);
            setRealScaleV(rscale);
            setRotateV(RotateV);


            float scaleImageCenterX =  (copy.getWidth()) / 2f;
            float scaleImageCenterY = (copy.getHeight()) / 2f;


            float divx = 1f-getScaleV().x;
            float divy = 1f-getScaleV().y;

            float fx = (scaleImageCenterX*divx);
            float fy = (scaleImageCenterY*divy);



            matrix.reset();
            matrix.postRotate(getRotateV(),scaleImageCenterX,scaleImageCenterY);
            matrix.postScale(getScaleV().x,getScaleV().y,scaleImageCenterX,scaleImageCenterY);
            matrix.postTranslate(x,y);



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
