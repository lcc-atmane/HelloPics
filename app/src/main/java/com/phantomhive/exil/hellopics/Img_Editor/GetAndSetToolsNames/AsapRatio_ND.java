package com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames;

import android.graphics.drawable.Drawable;

public class AsapRatio_ND {
    String AsapRatioName_H;
    Drawable AsapRatioImg_H;

    public AsapRatio_ND() {
    }

    public AsapRatio_ND(String asapRatioName_H, Drawable asapRatioImg_H) {
        AsapRatioName_H = asapRatioName_H;
        AsapRatioImg_H = asapRatioImg_H;
    }

    public String getAsapRatioName_H() {
        return AsapRatioName_H;
    }

    public Drawable getAsapRatioImg_H() {
        return AsapRatioImg_H;
    }

    public void setAsapRatioName_H(String asapRatioName_H) {
        AsapRatioName_H = asapRatioName_H;
    }

    public void setAsapRatioImg_H(Drawable asapRatioImg_H) {
        AsapRatioImg_H = asapRatioImg_H;
    }
}
