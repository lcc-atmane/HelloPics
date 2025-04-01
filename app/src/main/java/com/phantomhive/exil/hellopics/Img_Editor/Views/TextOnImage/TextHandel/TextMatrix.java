package com.phantomhive.exil.hellopics.Img_Editor.Views.TextOnImage.TextHandel;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;

import java.util.ArrayList;

public class TextMatrix {
    ArrayList<Matrix> TextMatrixList = new ArrayList<>();
    ArrayList<Float> RotateValueList = new ArrayList<>();
    ArrayList<Float> ScaleValueList = new ArrayList<>();
    private static  int Index;


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

    public void AddMatrix(Matrix matrix, float v, float s){
        TextMatrixList.add(matrix);
        RotateValueList.add(v);
        ScaleValueList.add(s);
    }

    public ArrayList<Matrix> getTextMatrixList() {
        return TextMatrixList;
    }

    public ArrayList<Float> getRotateValueList() {
        return RotateValueList;
    }

    public ArrayList<Float> getScaleValueList() {
        return ScaleValueList;
    }

    @SuppressLint("NotConstructor")
    public Matrix TextMatrix(){
        return TextMatrixList.get(getIndex());
    }

    public void removeMatrix(){
        TextMatrixList.remove(getIndex());
        RotateValueList.remove(getIndex());
        ScaleValueList.remove(getIndex());
        Index = TextMatrixList.size()-1;
    }

    public void setRotation(float degree,float x,float y){
        //RotateValueList.set(getIndex(),degree);
        TextMatrixList.get(getIndex()).reset();
        TextMatrixList.get(getIndex()).postRotate(degree, x, y);
        //TextMatrixList.get(getIndex()).postScale(getScaleValue(),getScaleValue(),x,y);
        //TextMatrixList.get(getIndex()).postTranslate(x, y);
    }

    public void setRotation(int index, float degree,float x,float y){
        TextMatrixList.get(index).setRotate(degree, x, y);
    }

    public void translate(float x,float y){
        TextMatrixList.get(getIndex()).postTranslate(x, y);
    }
    public void setRotation(float degree){
        RotateValueList.set(getIndex(),degree);
    }


     public void setScale(float scale,float x, float y,float xd, float yd) {
        ScaleValueList.set(getIndex(),scale);
        TextMatrixList.get(getIndex()).reset();
        TextMatrixList.get(getIndex()).postRotate(getRotationDegree(), x,y);
        TextMatrixList.get(getIndex()).postScale(scale,scale,x,y);
    }



    public float getRotationDegree(){
        return RotateValueList.get(getIndex());
    }
    public float getScaleValue(){
        return ScaleValueList.get(getIndex());
    }

    public int size (){
        return TextMatrixList.size();
    }
    public void clearMatrices(){
        TextMatrixList.clear();
        RotateValueList.clear();
        ScaleValueList.clear();
    }

}
