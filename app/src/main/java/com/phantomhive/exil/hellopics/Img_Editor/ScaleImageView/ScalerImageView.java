package com.phantomhive.exil.hellopics.Img_Editor.ScaleImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import static android.content.Context.WINDOW_SERVICE;

public class ScalerImageView {
    public void ScaleImageView(@NonNull ImageView imageView, @NonNull Context context, @NonNull Bitmap resource, @NonNull ConstraintLayout constraintLayout){

        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        double division = (double) resource.getHeight()/(double) resource.getWidth();
        double newwidth = constraintLayout.getHeight()/division;
        if (resource.getHeight()>resource.getWidth()){
            if (newwidth>constraintLayout.getWidth()){
                imageView.requestLayout();
                double division2 = (double)resource.getHeight()/(double)resource.getWidth();
                double newheght2 = constraintLayout.getWidth()*division2;
                imageView.getLayoutParams().width= constraintLayout.getWidth();
                double rnewH = Math.round(newheght2);
                imageView.getLayoutParams().height = (int)rnewH;
                imageView.setImageBitmap(resource);
            }else {
                imageView.requestLayout();
                double rnewW = Math.ceil(newwidth);
                imageView.getLayoutParams().width= (int)rnewW;
                imageView.getLayoutParams().height = constraintLayout.getHeight();
               imageView.setImageBitmap(resource);
            }
        }else {
            double far  = (double) resource.getWidth()/(double)resource.getHeight();
            double newhi = (double) constraintLayout.getWidth()/far;
            if (resource.getWidth()>resource.getHeight()){
                imageView.requestLayout();
                double rnewH2= Math.round(newhi);
                imageView.getLayoutParams().width= constraintLayout.getWidth();
                imageView.getLayoutParams().height =(int) rnewH2;
                imageView.setImageBitmap(resource);
            }

        }  if (resource.getWidth()==resource.getHeight()){
            imageView.requestLayout();
            imageView.getLayoutParams().width= constraintLayout.getWidth();
            imageView.getLayoutParams().height =constraintLayout.getWidth();
            imageView.setImageBitmap(resource);
        }
    }


}
