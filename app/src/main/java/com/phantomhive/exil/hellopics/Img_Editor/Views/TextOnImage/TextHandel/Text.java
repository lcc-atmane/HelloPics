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
package com.phantomhive.exil.hellopics.Img_Editor.Views.TextOnImage.TextHandel;

import android.annotation.SuppressLint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.StaticLayout;

import java.util.ArrayList;

public class Text {
    ArrayList<String> TextList = new ArrayList<>();
    ArrayList<Path> TextPath = new ArrayList<>();
    ArrayList<Path> TextEraser = new ArrayList<>();

    ArrayList<Float> TextBendValue = new ArrayList<>();

    ArrayList<TextStatics> TextStatic = new ArrayList<>();

    //ArrayList<StaticLayout> TextBordersStatic = new ArrayList<>();

    static private int Index;

    public void AddText(String text){
        TextList.add(text);
    }

    public void AddTextPath(Path textPath) {
        TextBendValue.add(0f);
        TextPath.add(textPath);
    }

    public void AddTextStaticLayout(StaticLayout s,StaticLayout sb) {
        TextStatics staticLayout = new TextStatics(s,sb);
        staticLayout.setLineSpace(1);
        TextStatic.add(staticLayout);
        //TextBordersStatic.add(sb);
    }

    public void UpdateTextStaticLayout(StaticLayout staticl,StaticLayout sb){
        TextStatics staticLayout = new TextStatics(staticl,sb);
        staticLayout.setLineSpace(1);
        TextStatic.set(getIndex(),staticLayout);
        //TextBordersStatic.set(getIndex(),sb);

    }

    public void UpdateTextStaticLayout(int index,StaticLayout staticl,StaticLayout sb){
        TextStatics staticLayout = new TextStatics(staticl,sb);
        staticLayout.setLineSpace(TextStatic.get(index).getLineSpace());
        TextStatic.set(index,staticLayout);
        //TextBordersStatic.set(index,sb);

    }

    public void setLineSpace(float s){
        TextStatic.get(getIndex()).setLineSpace(s);
    }
    public float getLineSpace(){
        return  TextStatic.get(getIndex()).getLineSpace();
    }
    public void UpdateText(String  NewText){
        TextList.set(getIndex(),NewText);
    }

    public void RemoveText(){
        TextList.remove(getIndex());
        TextPath.remove(getIndex());
        TextEraser.remove(getIndex());
        TextBendValue.remove(getIndex());
        TextStatic.remove(getIndex());
        Index = TextList.size()-1;
    }

    public int TextListSize (){
        return TextList.size();
    }

    @SuppressLint("NotConstructor")
    public String Text(){
        return TextList.get(getIndex());
    }

    public Path TextPath(){
        return TextPath.get(getIndex());
    }

    public TextStatics getStatic(){
        return TextStatic.get(getIndex());
    }

    //public StaticLayout getStaticBorders(){return TextBordersStatic.get(getIndex());}

    public void clearTextList(){
        TextList.clear();
        TextPath.clear();
        TextEraser.clear();
        TextBendValue.clear();
        TextStatic.clear();
        //TextBordersStatic.clear();
    }

    public ArrayList<String> getTextList() {
        return TextList;
    }

    public ArrayList<Path> getTextPath() {
        return TextPath;
    }

    public ArrayList<Float> getTextBend() {
        return TextBendValue;
    }

    public ArrayList<TextStatics> getTextStatic() {
        return TextStatic;
    }

    //public ArrayList<StaticLayout> getTextStaticBorders() {return TextBordersStatic;}

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
     * @param index index of Copy
     */
    public void unloadingIndex(int index)
    {
        Index = index;
    }

    public void MovePath(float x1,float y1,float x2,float bend) {
        /*
        TextPath.get(getIndex()).moveTo(mTextPosition.TextPosition().x - w,(mTextPosition.TextPosition().y));
        TextPath.get(getIndex()).cubicTo(
                mTextPosition.TextPosition().x - w,(mTextPosition.TextPosition().y),
                mTextPosition.TextPosition().x - w+((rightCopy-mTextPosition.TextPosition().x - w)/2), (mTextPosition.TextPosition().y),
                rightCopy,(mTextPosition.TextPosition().y));
         */

        TextPath.get(getIndex()).reset();
        TextPath.get(getIndex()).moveTo(x1,y1-bend);

        TextPath.get(getIndex()).cubicTo(
                x1,y1-bend,
                (x1+((x2-x1)/2)), (y1)+bend,
                x2,y1-bend);

    }

    public void MovePath(int i,float x1,float y1,float x2,float bend) {

        /*
          TextPath.get(i).reset();
        TextPath.get(i).moveTo(x1,y1-bend);

        TextPath.get(i).cubicTo(
                x1,y1-bend,
                x1+((x2-x1)/2), (y1)+bend,
                x2,y1-bend);
         */


        // Reset the path
        TextPath.get(i).reset();

        // Calculate the control point
        float controlX = (x1 + x2) / 2;
        float controlY = y1 + bend;

        // Calculate vertical offset to keep text centered
        float verticalOffset = bend * 1;

        // Move to the start point, adjusted for vertical centering
        TextPath.get(i).moveTo(x1, y1 - verticalOffset);

        // Create a quadratic curve, adjusted for vertical centering
        TextPath.get(i).quadTo(controlX, controlY, x2, y1 - verticalOffset);
    }

    public void MovePath2(int i, float x1, float y1, RectF x2, float bend) {
        /*
        TextPath.get(getIndex()).moveTo(mTextPosition.TextPosition().x - w,(mTextPosition.TextPosition().y));
        TextPath.get(getIndex()).cubicTo(
                mTextPosition.TextPosition().x - w,(mTextPosition.TextPosition().y),
                mTextPosition.TextPosition().x - w+((rightCopy-mTextPosition.TextPosition().x - w)/2), (mTextPosition.TextPosition().y),
                rightCopy,(mTextPosition.TextPosition().y));
         */

        TextPath.get(i).reset();
        TextPath.get(i).moveTo(x1,y1);

        TextPath.get(i).arcTo(x2,bend,bend,true);


    }

    public void AddTextEraser() {
        Path path = new Path();
        TextEraser.add(path);
    }

    public void MoveEraser(float x, float y){
        TextEraser.get(getIndex()).moveTo(x, y);
    }

    public void LineEraser(float x, float y){
        TextEraser.get(getIndex()).lineTo(x, y);
    }
    public void Eraser(float x, float y){
        //TextEraser.get(getIndex()).reset();
        //TextEraser.get(getIndex()).moveTo(x, y);
        TextEraser.get(getIndex()).offset(x, y);
    }
    public Path TextEraser(){
        return TextEraser.get(getIndex());
    }

    public ArrayList<Path> getTextEraser() {
        return TextEraser;
    }

    public void setBend(float bendX) {
        TextBendValue.set(getIndex(),bendX);
    }

    public float getBend() {
        return TextBendValue.get(getIndex());
    }


    public class TextStatics{
        StaticLayout TextStatic;
        StaticLayout TextBordersStatic ;
        float linespace;

        public TextStatics(StaticLayout textStatic, StaticLayout textBordersStatic) {
            TextStatic = textStatic;
            TextBordersStatic = textBordersStatic;
        }

        public StaticLayout getTextStatic() {
            return TextStatic;
        }

        public void setTextStatic(StaticLayout textStatic) {
            TextStatic = textStatic;
        }

        public StaticLayout getTextBordersStatic() {
            return TextBordersStatic;
        }

        public void setTextBordersStatic(StaticLayout textBordersStatic) {
            TextBordersStatic = textBordersStatic;
        }

        public float getLineSpace() {
            return linespace;
        }

        public void setLineSpace(float lineSpace) {
            this.linespace = lineSpace;
        }
    }
}
