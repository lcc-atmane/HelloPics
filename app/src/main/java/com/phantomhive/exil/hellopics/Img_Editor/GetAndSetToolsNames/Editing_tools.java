package com.phantomhive.exil.hellopics.Img_Editor.GetAndSetToolsNames;

import android.graphics.drawable.Drawable;

public class Editing_tools {
        String ToolsName_H;
        Drawable ToolsImg_H;

    public Editing_tools() {
    }

    public Editing_tools(String toolsName_H, Drawable toolsImg_H) {
        ToolsName_H = toolsName_H;
        ToolsImg_H = toolsImg_H;
    }

    public String getToolsName_H() {
        return ToolsName_H;
    }

    public Drawable getToolsImg_H() {
        return ToolsImg_H;
    }

    public void setToolsName_H(String toolsName_H) {
        ToolsName_H = toolsName_H;
    }

    public void setToolsImg_H(Drawable toolsImg_H) {
        ToolsImg_H = toolsImg_H;
    }
}
