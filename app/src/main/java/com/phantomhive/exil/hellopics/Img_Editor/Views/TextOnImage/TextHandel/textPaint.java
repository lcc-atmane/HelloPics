package com.phantomhive.exil.hellopics.Img_Editor.Views.TextOnImage.TextHandel;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.text.TextPaint;

import java.util.ArrayList;

public class textPaint {
    ArrayList<TextPaint> TextPaintList = new ArrayList<>();

    ArrayList<TextRect> TextRectPaintList = new ArrayList<>();
    ArrayList<Bitmap> TextRectImgList = new ArrayList<>();

    ArrayList<Bitmap> TextImgList = new ArrayList<>();

    ArrayList<TextPaint> BorderTextPaintList = new ArrayList<>();

    ArrayList<Gradients> GradientTextPaintList = new ArrayList<>();
    ArrayList<ShadowLayer> ShadowLayerTextPaintList = new ArrayList<>();
    int Index;


    public void AddTextPaint(float textSize){
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(Color.parseColor("#A6A061"));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextScaleX(1);
        paint.setTextSkewX(0);

        TextPaint BorderPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        BorderPaint.setTextSize(textSize);
        BorderPaint.setColor(Color.BLACK);
        BorderPaint.setStyle(Paint.Style.STROKE);
        BorderPaint.setTextAlign(Paint.Align.CENTER);
        BorderPaint.setTextScaleX(1);
        BorderPaint.setTextSkewX(0);

        Paint RectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        RectPaint.setColor(Color.YELLOW);
        RectPaint.setAlpha(0);

        TextImgList.add(null);
        TextPaintList.add(paint);

        TextRectPaintList.add(new TextRect(RectPaint,10,10,20,20,20,20));
        TextRectImgList.add(null);

        BorderTextPaintList.add(BorderPaint);

        int [] c = {Color.parseColor("#A6A061"),Color.parseColor("#A6A061")};
        GradientTextPaintList.add(new Gradients(c,0));

        ShadowLayer shadowLayer = new ShadowLayer();
        shadowLayer.ShadowLayerPosition(0f,10f,10f);
        shadowLayer.ShadowLayerColor(Color.parseColor("#A6A061"));
        ShadowLayerTextPaintList.add(shadowLayer);
    }

    public void removePaint(){
        TextImgList.remove(getIndex());
        TextRectImgList.remove(getIndex());
        TextPaintList.remove(getIndex());
        BorderTextPaintList.remove(getIndex());
        TextRectPaintList.remove(getIndex());
        GradientTextPaintList.remove(getIndex());
        ShadowLayerTextPaintList.remove(getIndex());
        Index = TextPaintList.size()-1;
    }

    public ArrayList<TextPaint> getTextPaintList() {
        return TextPaintList;
    }

    public ArrayList<Bitmap> getTextimgsList() {
        return TextImgList;
    }

    public ArrayList<TextPaint> getBorderTextPaintList() {
        return BorderTextPaintList;
    }

    public ArrayList<Gradients> getGradientsTextPaintList() {
        return GradientTextPaintList;
    }

    public ArrayList<TextRect> getTextRectPaintList() {
        return TextRectPaintList;
    }
    public ArrayList<ShadowLayer> getShadowLayerTextPaintList() {
        return ShadowLayerTextPaintList;
    }
    public int TextPaintSize(){
        return TextPaintList.size();
    }

    @SuppressLint("NotConstructor")
    public TextPaint TextPaint(){
        return TextPaintList.get(getIndex());
    }

    public Bitmap TextImg(){
        return TextImgList.get(getIndex());
    }

    public TextPaint BorderTextPaint(){
        return BorderTextPaintList.get(getIndex());
    }

    public void clearTextPaintList() {
        TextImgList.clear();
        TextRectImgList.clear();
        TextPaintList.clear();
        GradientTextPaintList.clear();
        ShadowLayerTextPaintList.clear();
        TextRectPaintList.clear();
        BorderTextPaintList.clear();
    }

    //paint Properties

    public void setTextSize(float size){
        TextPaintList.get(getIndex()).setTextSize(size);
        BorderTextPaintList.get(getIndex()).setTextSize(size);
    }

    public float getTextSize(){
        return TextPaintList.get(getIndex()).getTextSize();
    }


    public void setTextFont(Typeface tf){
        TextPaintList.get(getIndex()).setTypeface(tf);
        BorderTextPaintList.get(getIndex()).setTypeface(tf);
    }

    public Typeface getFont(){
        return TextPaintList.get(getIndex()).getTypeface();
    }
    public void setTextColor(int Color){
        TextPaintList.get(getIndex()).setColor(Color);
    }

    public void setBorderTextColor(int Color){
        BorderTextPaintList.get(getIndex()).setColor(Color);
        //BorderTextPaintList.get(getIndex()).setTextAlign();
    }

    public int getBorderTextColor(int index){
        return BorderTextPaintList.get(index).getColor();
    }

    public void setTextOpacity(int opacity) {
        TextPaintList.get(getIndex()).setAlpha(opacity);
        BorderTextPaintList.get(getIndex()).setAlpha(opacity);
    }

    public int getTextOpacity() {
        return TextPaintList.get(getIndex()).getAlpha();
    }

    public void setTextShadowPosition(float r,float x,float y){
        ShadowLayer shadowLayer = new ShadowLayer();
        shadowLayer.ShadowLayerPosition(r,x,y);
        shadowLayer.ShadowLayerColor(ShadowLayerTextPaintList.get(getIndex()).getColor());
        ShadowLayerTextPaintList.set(getIndex(),shadowLayer);

        TextPaintList.get(getIndex()).setShadowLayer(ShadowLayerTextPaintList.get(getIndex()).getR(),
                ShadowLayerTextPaintList.get(getIndex()).getX()
                ,ShadowLayerTextPaintList.get(getIndex()).getY(),ShadowLayerTextPaintList.get(getIndex()).getColor());
    }

    public void setTextShadowColor(int Color){
        ShadowLayer shadowLayer = new ShadowLayer();
        shadowLayer.ShadowLayerPosition(ShadowLayerTextPaintList.get(getIndex()).getR(),
                ShadowLayerTextPaintList.get(getIndex()).getX()
                ,ShadowLayerTextPaintList.get(getIndex()).getY());

        shadowLayer.ShadowLayerColor(Color);
        ShadowLayerTextPaintList.set(getIndex(),shadowLayer);

        TextPaintList.get(getIndex()).setShadowLayer(ShadowLayerTextPaintList.get(getIndex()).getR(),
                ShadowLayerTextPaintList.get(getIndex()).getX()
                ,ShadowLayerTextPaintList.get(getIndex()).getY(),ShadowLayerTextPaintList.get(getIndex()).getColor());
    }
    public ShadowLayer getTextShadow(float r,float x,float y,int Color){
        return ShadowLayerTextPaintList.get(getIndex());

    }

    public void setTextAlign(TextPaint.Align align){
        TextPaintList.get(getIndex()).setTextAlign(align);
        BorderTextPaintList.get(getIndex()).setTextAlign(align);
    }
    public Paint.Align setTextAlign(){
        return TextPaintList.get(getIndex()).getTextAlign();
    }

    // TODO: 9/3/2024 TEXT IMAGE
    public void setTextImg(Shader shader,Matrix matrix) {
        if (shader!=null){
        shader.setLocalMatrix(matrix);
        }
        TextPaintList.get(getIndex()).setShader(shader);
    }

    public void setTextImg(int index,Shader shader,Matrix matrix) {
        if (shader!=null){
            shader.setLocalMatrix(matrix);
        }

        TextPaintList.get(index).setShader(shader);
    }
    public Shader getTextImg(){
        return TextPaintList.get(getIndex()).getShader();
    }

    public void setTextImg(Bitmap textImg){
        TextImgList.set(getIndex(),textImg);
    }

    public Bitmap getTextimg(){
        return TextImgList.get(getIndex());
    }
    public void setTextGradient(Shader shader) {
        TextPaintList.get(getIndex()).setShader(shader);
    }

    public void setTextGradient(int index, Shader shader, int angle, float x, float y) {
        Matrix matrix = new Matrix();
        matrix.setRotate(angle,x,y);
        shader.setLocalMatrix(matrix);
        TextPaintList.get(index).setShader(shader);
    }
    public void setTextGradient(int [] Color,int angle){
        GradientTextPaintList.set(getIndex(),new Gradients(Color,angle));
    }
    public void setTextGradientAngle(int angle){
        GradientTextPaintList.set(getIndex(),new Gradients(getGradientsTextPaintList().get(getIndex()).getColors(),angle));
    }
    public Gradients getTextGradient(){
        return GradientTextPaintList.get(getIndex());
    }

    public void setTextRect(){
        TextRectPaintList.get(getIndex()).getRectPaint().setAlpha(255);
    }

    public int getTextRect(){
        return TextRectPaintList.get(getIndex()).getRectPaint().getAlpha();
    }
    public void removeTextRect(){
        TextRectPaintList.get(getIndex()).getRectPaint().setAlpha(0);
    }
    public void setTextRectOpacity(int opacity){
        TextRectPaintList.get(getIndex()).getRectPaint().setAlpha(opacity);
    }

    public void setTextRectColor(int color){
        TextRectPaintList.get(getIndex()).getRectPaint().setColor(color);
    }

    // TODO: 9/3/2024 TEXT RECT
    public void setTextRectShader(Shader shader,Matrix matrix) {
        if (shader!=null){
            shader.setLocalMatrix(matrix);
        }
        TextRectPaintList.get(getIndex()).getRectPaint().setShader(shader);
    }
    public void setTextRectShader(int index,Shader shader,Matrix matrix) {
        if (shader!=null){
            shader.setLocalMatrix(matrix);
        }
        TextRectPaintList.get(index).getRectPaint().setShader(shader);
    }
    public void setTextRectImg(Bitmap bitmap) {
        TextRectImgList.set(getIndex(),bitmap);
    }

    public ArrayList<Bitmap> getTextRectImgs() {
        return TextRectImgList;
    }

    public Bitmap getTextRectImg() {
        return TextRectImgList.get(getIndex());
    }



    public void setTextRectXY(float x,float y) {
        TextRectPaintList.get(getIndex()).setX(x);
        TextRectPaintList.get(getIndex()).setY(y);
    }

    public void setTextRectLeft(float left){
        TextRectPaintList.get(getIndex()).setLeft(left);
    }

    public void setTextRectTop(float top){
        TextRectPaintList.get(getIndex()).setTop(top);
    }

    public void setTextRectRight(float right){
        TextRectPaintList.get(getIndex()).setRight(right);
    }

    public void setTextRectBottom(float bottom){
        TextRectPaintList.get(getIndex()).setBottom(bottom);
    }

    public float getTextRectLeft(){
        return TextRectPaintList.get(getIndex()).getLeft();
    }

    public float getTextRectTop(){
        return TextRectPaintList.get(getIndex()).getTop();
    }

    public float getTextRectRight(){
        return   TextRectPaintList.get(getIndex()).getRight();
    }

    public float getTextRectBottom(){
        return TextRectPaintList.get(getIndex()).getBottom();
    }
    public float getTextRectXY(){
        return TextRectPaintList.get(getIndex()).getX();

    }

    /*
      public Paint getTextRect(){
        return TextRectPaintList.get(getIndex()).getRectPaint();
    }
     */

    public void setTextStroke(float stroke) {
        TextPaintList.get(getIndex()).setStyle(Paint.Style.STROKE);
        TextPaintList.get(getIndex()).setStrokeWidth(stroke);
    }

    public void setScaledTextSize_Stroke_Borders(int index,float Size, float Stroke, float Borders){
        TextPaintList.get(index).setTextSize(Size);
        BorderTextPaintList.get(index).setTextSize(Size);

        TextPaintList.get(index).setStrokeWidth(Stroke);
        BorderTextPaintList.get(index).setStrokeWidth(Borders);
    }
    public void setTextFill() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(getTextSize());
        paint.setColor(TextPaintList.get(getIndex()).getColor());
        paint.setShader(TextPaintList.get(getIndex()).getShader());
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextScaleX(getTextScaleX());
        paint.setTextSkewX(getTextSkewX());
        //paint.setShadowLayer(TextPaintList.get(getIndex()).getShadowLayerRadius(),TextPaintList.get(getIndex()).getShadowLayerDx(), TextPaintList.get(getIndex()).getShadowLayerDy(),TextPaintList.get(getIndex()).getShadowLayerColor());
        paint.setLetterSpacing(getLatterSpacing());
        paint.setXfermode(getTextMode());
        paint.setTypeface(getFont());


        Paint BorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        BorderPaint.setTextSize(getTextSize());
        BorderPaint.setColor(TextPaintList.get(getIndex()).getColor());
        BorderPaint.setStyle(Paint.Style.STROKE);
        BorderPaint.setTextAlign(Paint.Align.CENTER);
        BorderPaint.setTextScaleX(getTextScaleX());
        BorderPaint.setTextSkewX(getTextSkewX());
        BorderPaint.setLetterSpacing(getLatterSpacing());
        BorderPaint.setXfermode(getTextMode());
        BorderPaint.setTypeface(getFont());

        BorderTextPaintList.get(getIndex()).set(BorderPaint);

        TextPaintList.get(getIndex()).set(paint);
    }

    public float getTextStroke() {
        return TextPaintList.get(getIndex()).getStrokeWidth();
    }

    public void setBorderTextStroke(float stroke) {
        if(TextPaintList.get(getIndex()).getColor()==BorderTextPaintList.get(getIndex()).getColor()){
            BorderTextPaintList.get(getIndex()).setColor(Color.WHITE);
        }
        BorderTextPaintList.get(getIndex()).setStrokeWidth(stroke);

    }
    public float getBorderTextStroke(){
        return BorderTextPaintList.get(getIndex()).getStrokeWidth();
    }
    public void setTextScaleX(float scaleX){
        TextPaintList.get(getIndex()).setTextScaleX(scaleX);
        BorderTextPaintList.get(getIndex()).setTextScaleX(scaleX);
    }
    public float getTextScaleX(){
        return TextPaintList.get(getIndex()).getTextScaleX();
    }

    public void setTextSkewX(float SkewX){
        TextPaintList.get(getIndex()).setTextSkewX(SkewX);
        BorderTextPaintList.get(getIndex()).setTextSkewX(SkewX);
    }

    public float getTextSkewX(){
        return TextPaintList.get(getIndex()).getTextSkewX();
    }

    public void setLatterSpacing(float value) {
        TextPaintList.get(getIndex()).setLetterSpacing(value);
        BorderTextPaintList.get(getIndex()).setLetterSpacing(value);
    }
    public float getLatterSpacing(){
        return TextPaintList.get(getIndex()).getLetterSpacing();

    }
    public void setTextMode(PorterDuff.Mode mode) {
        if (mode == null){
            TextPaintList.get(getIndex()).setXfermode(null);
            BorderTextPaintList.get(getIndex()).setXfermode(null);
        }else {
            TextPaintList.get(getIndex()).setXfermode(new PorterDuffXfermode(mode));
            BorderTextPaintList.get(getIndex()).setXfermode(new PorterDuffXfermode(mode));
        }
    }

    public Xfermode getTextMode(){
       return  TextPaintList.get(getIndex()).getXfermode();
    }


    public void unloadingIndex(){
        Index = 0;
    }



    public void setIndex(int index) {
        Index = index;
    }

    public int getIndex() {
        return Index;
    }

    public int PaintTextListSize() {
        return TextPaintList.size();
    }


    public class Gradients {
        int[] Colors;
        int angle;

        public Gradients(int[] colors, int angle) {
            Colors = colors;
            this.angle = angle;
        }

        public int[] getColors() {
            return Colors;
        }

        public void setColors(int[] colors) {
            Colors = colors;
        }

        public int getAngle() {
            return angle;
        }

        public void setAngle(int angle) {
            this.angle = angle;
        }
    }

    public class ShadowLayer{
        float r;
        float x;
        float y;
        int color;

        public void ShadowLayerPosition(float r, float x, float y) {
            this.r = r;
            this.x = x;
            this.y = y;
        }

        public void ShadowLayerColor( int color) {

            this.color = color;
        }
        public float getR() {
            return r;
        }

        public void setR(float r) {
            this.r = r;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    public class TextRect{
        Paint RectPaint;
        float x,y;

        float left,top,bottom,right;

        public TextRect(Paint rectPaint, float x, float y, float left, float top, float bottom, float right) {
            RectPaint = rectPaint;
            this.x = x;
            this.y = y;
            this.left = left;
            this.top = top;
            this.bottom = bottom;
            this.right = right;
        }

        public Paint getRectPaint() {
            return RectPaint;
        }

        public void setRectPaint(Paint rectPaint) {
            RectPaint = rectPaint;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getLeft() {
            return left;
        }

        public void setLeft(float left) {
            this.left = left;
        }

        public float getTop() {
            return top;
        }

        public void setTop(float top) {
            this.top = top;
        }

        public float getBottom() {
            return bottom;
        }

        public void setBottom(float bottom) {
            this.bottom = bottom;
        }

        public float getRight() {
            return right;
        }

        public void setRight(float right) {
            this.right = right;
        }


    }
}
