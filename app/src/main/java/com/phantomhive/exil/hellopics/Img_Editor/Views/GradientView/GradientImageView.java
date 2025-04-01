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
package com.phantomhive.exil.hellopics.Img_Editor.Views.GradientView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.GradientActivity;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;


public class GradientImageView extends ImageView {

    Bitmap bitmap;

    Bitmap srcBm;

    Canvas canvas;
    Paint paint;
    LinearGradient linearGradient;

    ViewZommer zommer;

    int[] Colors;

    Matrix matrix =new Matrix();
    float angle;
    int opacity =255;
    Context mContext;

    Paint Bp;
    public GradientImageView(Context context) {
        super(context);
        init(context, null);
    }

    public GradientImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GradientImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public GradientImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        zommer.onMeasure(widthMeasureSpec, heightMeasureSpec);

        invalidate();
    }

    public void init(Context context, @Nullable AttributeSet attrs) {
        mContext = context;
        zommer = new ViewZommer(this,context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));

        Bp = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bp.setAntiAlias(true);
        Bp.setFilterBitmap(true);
        Bp.setDither(true);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        zommer.setBitmap(bm);
        bitmap = bm;
        srcBm = bm;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap,zommer.getDefaultMatrix(),Bp);

    }


   public void setGradient(int[] Colors){

        this.Colors =Colors;

        bitmap = Bitmap.createBitmap(srcBm,0,0,srcBm.getWidth(),srcBm.getHeight());

        canvas = new Canvas(bitmap);

       ((GradientActivity) mContext).AngleSeekBar.setProgress(0);
       ((GradientActivity) mContext).OpacitySeekBar.setProgress(255);
       opacity = 255;
       paint.setAlpha(opacity);
       linearGradient= new LinearGradient(0, 0,
               bitmap.getWidth(),
               bitmap.getHeight(),Colors,null, Shader.TileMode.CLAMP);
       matrix.setRotate(0f);
       linearGradient.setLocalMatrix(matrix);
       paint.setShader(linearGradient);
       canvas.drawRect(0, 0,  bitmap.getWidth(), bitmap.getHeight(), paint);

       invalidate();
    }

    public void setMode(PorterDuff.Mode mode) {
        if (Colors!=null){
            if (mode == null){
                paint.setXfermode(null);
            }else {
                paint.setXfermode(new PorterDuffXfermode(mode));
            }
            bitmap = Bitmap.createBitmap(srcBm,0,0,srcBm.getWidth(),srcBm.getHeight());

            canvas = new Canvas(bitmap);




            linearGradient= new LinearGradient(0, 0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),Colors,null, Shader.TileMode.CLAMP);
            linearGradient.setLocalMatrix(matrix);
            paint.setShader(linearGradient);

            canvas.drawRect(0, 0,  bitmap.getWidth(),
                    bitmap.getHeight(), paint);
        }

        invalidate();
    }

    public void setAngle(int angle){
        if (Colors!=null){
            this.angle = angle;
            bitmap = Bitmap.createBitmap(srcBm,0,0,srcBm.getWidth(),srcBm.getHeight());

            canvas = new Canvas(bitmap);

            paint.setAlpha(opacity);
            linearGradient= new LinearGradient(0, 0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),Colors,null, Shader.TileMode.CLAMP);
            matrix.setRotate(angle, bitmap.getWidth()/2f,
                    bitmap.getHeight()/2f);
            linearGradient.setLocalMatrix(matrix);
            paint.setShader(linearGradient);

            canvas.drawRect(0, 0,  bitmap.getWidth(),
                    bitmap.getHeight(), paint);
        }

        invalidate();

    }

    public void setOpacity(int opacity){
        if (Colors!=null){
            this.opacity = opacity;
            bitmap = Bitmap.createBitmap(srcBm,0,0,srcBm.getWidth(),srcBm.getHeight());

            canvas = new Canvas(bitmap);



            paint.setAlpha(opacity);
            linearGradient= new LinearGradient(0, 0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),Colors,null, Shader.TileMode.CLAMP);
            matrix.setRotate(angle,bitmap.getWidth()/2f,
                    bitmap.getHeight()/2f);
            linearGradient.setLocalMatrix(matrix);
            paint.setShader(linearGradient);

            canvas.drawRect(0, 0,  bitmap.getWidth(),
                    bitmap.getHeight(), paint);
        }

        invalidate();

    }
    public Bitmap getFinalBitmap(){
        return bitmap;
    }

    public void setColorLoc(boolean b) {

    }
}
