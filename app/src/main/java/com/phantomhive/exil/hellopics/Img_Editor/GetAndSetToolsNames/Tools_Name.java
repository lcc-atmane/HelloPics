package com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames;

import android.graphics.drawable.Drawable;

public class Tools_Name {
    String RToolName;
    Drawable RToolsImg;

    public Tools_Name() {
    }

    public Tools_Name(String RToolName, Drawable RToolsImg) {
        this.RToolName = RToolName;
        this.RToolsImg = RToolsImg;
    }

    public String getRToolName() {
        return RToolName;
    }

    public Drawable getRToolsImg() {
        return RToolsImg;
    }

    public void setRToolName(String RToolName) {
        this.RToolName = RToolName;
    }

    public void setRToolsImg(Drawable RToolsImg) {
        this.RToolsImg = RToolsImg;
    }
}

