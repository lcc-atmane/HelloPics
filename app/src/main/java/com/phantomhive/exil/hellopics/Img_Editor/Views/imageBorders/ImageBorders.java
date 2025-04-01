package com.phantomhive.exil.hellopics.Img_Editor.Views.imageBorders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.ImageBorderActivity;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ColorDetector;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.X_YFixer;


@SuppressLint("AppCompatCustomView")
public class ImageBorders extends ImageView {

    private static final String TAG = "ImageBorders";

    Matrix drawM = new Matrix();
    Matrix RealM = new Matrix();

    private float DownX,DownY,X,Y,
            DownRealTimeX, DownRealTimeY, RealTimeX, RealTimeY;


    float redundantYSpace ;
    float redundantXSpace ;
    float RRedundantYSpace ;
    float RRedundantXSpace ;


    float RredundantYSpace ;
    float RredundantXSpace;
    float RRRedundantYSpace ;
    float RRRedundantXSpace ;


    private Paint mBorderPaint;
    private Paint mRBorderPaint;
    private Bitmap mBitmap;

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    PointF last = new PointF();
    PointF start = new PointF();

    X_YFixer mxAndyFixer = new X_YFixer();

    float saveScaleX = 1f;
    float saveScaleY = 1f;

    float dsaveScaleX = 1f;
    float dsaveScaleY = 1f;
    float mScaleFactor=1;

    ScaleGestureDetector mScaleDetector;

    float targetWidth= 1;
    float targetHeight = 1;

    float RtargetWidth= 1;
    float RtargetHeight = 1;

    Bitmap RImgBackground,DImgBackground;

    ViewZommer zommer;
    private Context context;
    private Bitmap BackgroundImage;
    private int mRotate;
    int mColor = Color.parseColor("#A6A061");
    boolean ColorLoc;
    Paint CircleLPaint;
    float CirclCx,CirclCy;
    Paint bmpaint;

    public ImageBorders(Context context) {
        super(context);
        init(context, null);
    }

    public ImageBorders(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ImageBorders(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    public ImageBorders(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);

    }


    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        this.context = context;
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setColor(Color.parseColor("#A6A061"));

        mRBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRBorderPaint.setColor(Color.parseColor("#A6A061"));
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        CircleLPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        bmpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bmpaint.setAntiAlias(true);
        bmpaint.setFilterBitmap(true);
        zommer = new ViewZommer(this,context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        zommer.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(RRedundantXSpace,RRedundantYSpace,
                RRedundantXSpace+targetWidth
                    ,RRedundantYSpace+targetHeight, mBorderPaint);
        canvas.drawBitmap(mBitmap, drawM,bmpaint);
        if (ColorLoc) {
            canvas.drawCircle(CirclCx,CirclCy - (200 / zommer.getScaleX()),
                    50 / zommer.getScaleX(), CircleLPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float width = getWidth();
        float height = getHeight();

        //Fit to screen.
        float scale;
        float scaleX =  width /mBitmap.getWidth();
        float scaleY = height / mBitmap.getHeight();
        scale = Math.min(scaleX, scaleY);

        // Center the image
        redundantYSpace = height - (scale *mBitmap.getHeight()) ;
        redundantXSpace = width - (scale *mBitmap.getWidth());
        redundantYSpace /= 2;
        redundantXSpace /= 2;

        RRedundantXSpace = redundantXSpace;
        RRedundantYSpace = redundantYSpace;

        Log.d(TAG, "onSizeChanged: "+redundantXSpace+" "+redundantYSpace);
        drawM.postScale(scale,scale,(float)mBitmap.getWidth()/2,
                (float) mBitmap.getHeight()/2);
        drawM.postTranslate(redundantXSpace, redundantYSpace);

        X = redundantXSpace;
        Y = redundantYSpace;

        targetWidth = mBitmap.getWidth();
        targetHeight = mBitmap.getHeight();

        RtargetWidth = mBitmap.getWidth();
        RtargetHeight = mBitmap.getHeight();

        RredundantYSpace = RtargetHeight - (mBitmap.getHeight()) ;
        RredundantXSpace = RtargetWidth - (mBitmap.getWidth());
        RredundantYSpace /= 2;
        RredundantXSpace /= 2;

        RRRedundantXSpace = RredundantXSpace;
        RRRedundantYSpace = RredundantYSpace;

        RealM.setScale(1f,1f,(float)mBitmap.getWidth()/2,
                (float) mBitmap.getHeight()/2);
        RealM.postTranslate(RredundantXSpace, RredundantYSpace);
        Log.d(TAG, "onSizeChanged: "+RredundantXSpace+"  "+RredundantYSpace);
        RealTimeX =RredundantXSpace;
        RealTimeY =RredundantYSpace;
    }


    @Override
    public void setImageBitmap(Bitmap bm) {
        Bitmap bitmap = Bitmap.createBitmap(bm.getWidth(),bm.getHeight(),bm.getConfig());
        super.setImageBitmap(bitmap);
        zommer.setBitmap(bm);
    }

    public void setImageSource(Bitmap bitmap){
        mBitmap = bitmap;
    }

    public Bitmap getImageBitmap() {
        return mBitmap;
    }


    public void setBorderColor(int Color){
        mBorderPaint.setShader(null);
        mRBorderPaint.setShader(null);
        mBorderPaint.setColor(Color);
        mRBorderPaint.setColor(Color);
        invalidate();
    }
    public void setColorLoc(boolean b) {
        ColorLoc = b;
        setImageTRotate(0);
        invalidate();
    }
    public void setImageBlur(int blurRadius) {
        invalidate();
    }

    public void setBackgroundImage(Bitmap resource) {
        BackgroundImage = resource;

        RImgBackground = getRScaledBitmap(resource,RtargetWidth,RtargetHeight);
        DImgBackground = getScaledBitmap(resource,targetWidth,targetHeight);

        BitmapShader shader = new BitmapShader(DImgBackground, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);


        BitmapShader Rshader = new BitmapShader(RImgBackground, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);


        Matrix matrix = new Matrix();


        float dx, dy;

        final float dwidth = DImgBackground.getWidth();
        final float dheight = DImgBackground.getHeight();


        dx = (dwidth) * 0.5f;

        dy = (dheight) * 0.5f;

        RectF rectF = new RectF(RRedundantXSpace,RRedundantYSpace,
                RRedundantXSpace+targetWidth
                ,RRedundantYSpace+targetHeight);
        matrix.setTranslate(rectF.centerX()-Math.round(dx), rectF.centerY()-Math.round(dy));


        Matrix Rmatrix = new Matrix();

        float Rdx , Rdy ;
        final float Rdwidth = RImgBackground.getWidth();
        final float Rdheight = RImgBackground.getHeight();

        final float Rvwidth = RtargetWidth;
        final float Rvheight = RtargetHeight;


        Rdx = (Rvwidth - Rdwidth ) * 0.5f;
        Rdy = (Rvheight - Rdheight ) * 0.5f;
        Rmatrix.setTranslate(Math.round(Rdx), Math.round(Rdy));

        shader.setLocalMatrix(matrix);
        Rshader.setLocalMatrix(Rmatrix);

        mBorderPaint.setShader(shader);
        mRBorderPaint.setShader(Rshader);

        invalidate();
    }


    public Bitmap getScaledBitmap(Bitmap mBitmap, float targetWidth, float targetHeight){

        float division0 = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();
        float division1 = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();

        float RWidth=0,RHeight=0;

        if (getImageBitmap().getHeight()>
                getImageBitmap().getWidth()){


            if (getBitmapRect().height()/division0
                    <getBitmapRect().width()){

                float d = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();

                RWidth = targetWidth;
                RHeight =  (RWidth/d);

            }else {
                float d = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();

                RHeight = targetHeight;
                RWidth = (RHeight/d);

            }


        }else {
            if(getBitmapRect().width()/division1
                    <getBitmapRect().height()){
                float d = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();


                RHeight =targetHeight;
                RWidth = (RHeight/d);
            }else{
                float d = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();


                RWidth = targetWidth;
                RHeight =  (RWidth/d);
            }

        }

        return Bitmap.createScaledBitmap(mBitmap, (int) RWidth, (int) RHeight,true);
    }

    public Bitmap getRScaledBitmap(Bitmap mBitmap, float RtargetWidth, float RtargetHeight){

        float division0 = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();
        float division1 = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();

        float RWidth=0,RHeight=0;

        if (getImageBitmap().getHeight()>
                getImageBitmap().getWidth()){


            if (getBitmapRect().height()/division0
                    <getBitmapRect().width()){

                float d = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();

                RWidth =RtargetWidth;
                RHeight =  (RWidth/d);

            }else {
                float d = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();

                RHeight = RtargetHeight;
                RWidth = (RHeight/d);

            }


        }else {
            if(getBitmapRect().width()/division1
                    <getBitmapRect().height()){
                float d = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();


                RHeight = RtargetHeight;
                RWidth = (RHeight/d);
            }else{
                float d = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();


                RWidth =RtargetWidth;
                RHeight =  (RWidth/d);
            }

        }

        return Bitmap.createScaledBitmap(mBitmap, (int) RWidth, (int) RHeight,true);
    }



    /*Returns the bitmap position inside an imageView.
     * @param imageView source ImageView
     * @return Rect position of the bitmap in the ImageView
     */

    public RectF getBitmapRect()
    {
        RectF rect = new RectF();

        final Drawable drawable = getDrawable();
        if ( drawable == null)
        {
            return rect;
        }

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final float actW = origW * scaleX;
        final float actH = origH * scaleY;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = getWidth();
        int imgViewH = getHeight();

        rect.top  =  (imgViewH - actH) / 2;
        rect.left =  (imgViewW - actW) / 2;

        rect.bottom = rect.top + actH;
        rect.right  = rect.left + actW;

        return rect;
    }

    // Helper method to find the greatest common divisor (GCD) using Euclid's algorithm
    public int gcd(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }

    public void Fit(int x, int y){

        float originalAspectRatio = (float) mBitmap.getWidth() /
                (float)mBitmap.getHeight();
        float desiredAspectRatio = (float)x/(float)y;

        float proximityHeightWidth = 2500;

        float div ;

        if (x > y){

            float div1 =(float) (mBitmap.getHeight()
                    /(float)mBitmap.getWidth());
            Matrix matrix = new Matrix();
            matrix.setScale(((float)getHeight()/div1) / (float) mBitmap.getWidth()
                    ,(float)getHeight()/(float)mBitmap.getHeight());

            Bitmap bitmap = Bitmap.createBitmap(getWidth(),
                    (int) (getHeight()+ (( getWidth() / desiredAspectRatio) -getHeight())), Bitmap.Config.RGB_565);

            if (bitmap.getHeight()>getHeight()){
                targetWidth = getWidth()+
                        (((float)getHeight() * desiredAspectRatio) -
                                (float)getWidth());
                targetHeight = (float)getHeight();


            }else {
                targetWidth = getWidth();
                targetHeight =  ((float)getHeight()+
                        (((float) getWidth() / desiredAspectRatio) -
                                (float)getHeight()));
            }


//            targetWidth = getWidth();
//            targetHeight =  ((float)getHeight()+ (((float) getWidth() / desiredAspectRatio) -(float)getHeight()));

            RtargetWidth = proximityHeightWidth;
            RtargetHeight =  ((float)mBitmap.getHeight()+
                    (((float) proximityHeightWidth / desiredAspectRatio) -
                            (float)mBitmap.getHeight()));
            Log.d(TAG, "4: ");
        }else if (x < y){

            float div1 =(float) (mBitmap.getHeight()
                    /(float)mBitmap.getWidth());
            Matrix matrix = new Matrix();
            matrix.setScale(((float)getHeight()/div1) / (float) mBitmap.getWidth()
                    ,(float)getHeight()/(float)mBitmap.getHeight());

            Bitmap bitmap = Bitmap.createBitmap((int) (getWidth()+ ((getHeight() * desiredAspectRatio) - getWidth())),
            getHeight(), Bitmap.Config.RGB_565);

            if (bitmap.getWidth()>getWidth()){
                targetWidth = getWidth();
                targetHeight =  ((float)getHeight()+
                        (((float) getWidth() / desiredAspectRatio) -
                                (float)getHeight()));

            }else {
                targetWidth = getWidth()+
                        (((float)getHeight() * desiredAspectRatio) -
                                (float)getWidth());
                targetHeight = (float)getHeight();
            }


            RtargetWidth = mBitmap.getWidth()+
                    ((proximityHeightWidth * desiredAspectRatio) -
                            (float)mBitmap.getWidth());

            RtargetHeight = proximityHeightWidth;
            Log.d(TAG, "1: ");

        }else {
            float div1 =(float) (mBitmap.getHeight()
                    /(float)mBitmap.getWidth());
            Matrix matrix = new Matrix();
            matrix.setScale(((float)getHeight()/div1) / (float) mBitmap.getWidth()
                    ,(float)getHeight()/(float)mBitmap.getHeight());

            Bitmap bitmap = Bitmap.createBitmap(getWidth(),
                    (int) (getHeight()+ (( getWidth() / desiredAspectRatio) -getHeight())), Bitmap.Config.RGB_565);

            if (bitmap.getHeight()>getHeight()){
                targetWidth = getWidth()+
                        (((float)getHeight() * desiredAspectRatio) -
                                (float)getWidth());
                targetHeight = (float)getHeight();


            }else {
                targetWidth = getWidth();
                targetHeight =  ((float)getHeight()+
                        (((float) getWidth() / desiredAspectRatio) -
                                (float)getHeight()));
            }

            RtargetWidth = proximityHeightWidth;
            RtargetHeight =  ((float)mBitmap.getHeight()+
                    (((float) proximityHeightWidth / desiredAspectRatio) -
                            (float)mBitmap.getHeight()));
            Log.d(TAG, "0: ");
        }

        if (originalAspectRatio > desiredAspectRatio){
            div = mBitmap.getWidth()
                    /(float)mBitmap.getHeight();
            dsaveScaleX =targetWidth/(float)mBitmap.getWidth();
            dsaveScaleY = (targetWidth/div)/(float)mBitmap.getHeight();

            saveScaleX =RtargetWidth/(float)mBitmap.getWidth();
            saveScaleY = (RtargetWidth/div)/(float)mBitmap.getHeight();
            Log.d(TAG, "Fit: 2");
        }else if (originalAspectRatio < desiredAspectRatio){
            div =(float) (mBitmap.getHeight()
                    /(float)mBitmap.getWidth());

            dsaveScaleX =(targetHeight/div) / (float) mBitmap.getWidth();
            dsaveScaleY = targetHeight/(float)mBitmap.getHeight();

            saveScaleX =(RtargetHeight/div) / (float) mBitmap.getWidth();
            saveScaleY = RtargetHeight/(float)mBitmap.getHeight();
            Log.d(TAG, "Fit: 3");
        }else {
            div = mBitmap.getWidth()
                    /(float)mBitmap.getHeight();
            dsaveScaleX =targetWidth/(float)mBitmap.getWidth();
            dsaveScaleY = (targetWidth/div)/(float)mBitmap.getHeight();

            saveScaleX =RtargetWidth/(float)mBitmap.getWidth();
            saveScaleY = (RtargetWidth/div)/(float)mBitmap.getHeight();
            Log.d(TAG, "Fit: 4");
        }

        float width = getWidth();
        float height = getHeight();


        // Center the image
        RRedundantYSpace = height - (targetHeight) ;
        RRedundantXSpace = width - (targetWidth);
        RRedundantYSpace /= 2f;
        RRedundantXSpace /= 2f;

        //Fit to screen.
        float scale;
        float scaleX =  width /mBitmap.getWidth();
        float scaleY = height /mBitmap.getHeight();
        scale = Math.min(scaleX, scaleY);

        // Center the image
        float YY = height - (mBitmap.getHeight()) ;
        float XX = width - (mBitmap.getWidth());

        YY /= 2f;
        XX /= 2f;

        X = XX;
        Y = YY;




        drawM.reset();
        drawM.postRotate(mRotate,(float)mBitmap.getWidth()/2,
                (float)mBitmap.getHeight()/2);
        drawM.postScale(dsaveScaleX, dsaveScaleY,(float)mBitmap.getWidth()/2,
                (float)mBitmap.getHeight()/2);
        drawM.postTranslate(X,Y);


        RredundantYSpace = RtargetHeight - (mBitmap.getHeight()) ;
        RredundantXSpace = RtargetWidth - (mBitmap.getWidth());
        RredundantYSpace /= 2;
        RredundantXSpace /= 2;

        RealTimeX = RredundantXSpace;
        RealTimeY = RredundantYSpace;

        RealM.reset();
        RealM.postRotate(mRotate,((mBitmap.getWidth())/2f),
                ((mBitmap.getHeight())/2f));
        RealM.postScale(saveScaleX, saveScaleY,(float) mBitmap.getWidth()/2,
                (float)mBitmap.getHeight()/2);
        RealM.postTranslate(RealTimeX, RealTimeY);

        Log.d(TAG, "Fit: Crash"+saveScaleX+" "+saveScaleY+" ( "+dsaveScaleX+" "+dsaveScaleY);
        //((ImageBorderActivity)(context)).firstBorder.setProgress((int) (saveScaleX*100));
        setImageBitmap(Bitmap.createBitmap( (int) RtargetWidth, (int) RtargetHeight, Bitmap.Config.ARGB_8888));

        if (BackgroundImage!=null){
            setBackgroundImage(BackgroundImage);
        }

        invalidate();
    }

    public void setImageRotate(int progress) {
        mRotate = progress;

        drawM.reset();
        drawM.postRotate(mRotate,(float) mBitmap.getWidth()/2,
                (float)mBitmap.getHeight()/2);
        drawM.postScale(dsaveScaleX, dsaveScaleY,(float)mBitmap.getWidth()/2,
                (float)mBitmap.getHeight()/2);
        drawM.postTranslate(X,Y);


        RealM.reset();
        RealM.postRotate(mRotate,((mBitmap.getWidth())/2f),
                ((mBitmap.getHeight())/2f));
        RealM.postScale(saveScaleX, saveScaleY,  ((mBitmap.getWidth())/2f),
                ((mBitmap.getHeight())/2f));
        RealM.postTranslate(RealTimeX, RealTimeY);
        invalidate();

    }

    public void setBorderMode(PorterDuff.Mode mode){
        if (mode == null){
            bmpaint.setXfermode(null);
        }else {
            bmpaint.setXfermode(new PorterDuffXfermode(mode));
        }
        invalidate();
    }
    public void setImageTRotate(int progress) {

        drawM.reset();
        drawM.postRotate(progress,(float) mBitmap.getWidth()/2,
                (float)mBitmap.getHeight()/2);
        drawM.postScale(dsaveScaleX, dsaveScaleY,(float)mBitmap.getWidth()/2,
                (float)mBitmap.getHeight()/2);
        drawM.postTranslate(X,Y);


        RealM.reset();
        RealM.postRotate(progress,((mBitmap.getWidth())/2f),
                ((mBitmap.getHeight())/2f));
        RealM.postScale(saveScaleX, saveScaleY,  ((mBitmap.getWidth())/2f),
                ((mBitmap.getHeight())/2f));
        RealM.postTranslate(RealTimeX, RealTimeY);
        invalidate();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN -> {
                DownX = event.getX();
                DownY = event.getY();
                CirclCx = event.getX();
                CirclCy += event.getY();
                mxAndyFixer.FixXYUsingMatrix(DownX, DownY, getImageMatrix());
                if (ColorLoc) {
                    mColor = ColorDetector.getColor((int) X_YFixer.getfixedXYUsingMartix(event.getX(), event.getY(),drawM).x,
                            (int) X_YFixer.getfixedXYUsingMartix(event.getX(), event.getY(),drawM).y,mBitmap);
                    CircleLPaint.setColor(mColor);
                } else {

                DownRealTimeX = mxAndyFixer.getFixedXYUsingMartix().x;
                DownRealTimeY = mxAndyFixer.getFixedXYUsingMartix().y;

                last.set(event.getX(), event.getY());
                start.set(last);
                mode = DRAG;
                }
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_POINTER_DOWN -> {
                mode = ZOOM;
                return true;
            }
            case MotionEvent.ACTION_MOVE -> {
                mxAndyFixer.FixXYUsingMatrix(event.getX(), event.getY(), getImageMatrix());
                CirclCx = event.getX();
                CirclCy = event.getY() ;
                if (ColorLoc) {
                    mColor = ColorDetector.getColor((int) X_YFixer.getfixedXYUsingMartix(event.getX(), event.getY(),drawM).x,
                            (int) X_YFixer.getfixedXYUsingMartix(event.getX(), event.getY(),drawM).y,mBitmap);
                    CircleLPaint.setColor(mColor);
                } else {
                    if (mode == DRAG) {
                        if (event.getX() >= X && event.getX() <=(mBitmap.getWidth()) +
                                X &&
                                event.getY() >= Y &&
                                event.getY() <= (mBitmap.getHeight()) +
                                Y) {
                            X += event.getX() - DownX;
                            Y += event.getY() - DownY;


                            RealTimeX += mxAndyFixer.getFixedXYUsingMartix().x - DownRealTimeX;
                            RealTimeY += mxAndyFixer.getFixedXYUsingMartix().y - DownRealTimeY;

                            drawM.reset();
                            drawM.postRotate(mRotate, (float) mBitmap.getWidth() / 2,
                                    (float) mBitmap.getHeight() / 2);
                            drawM.postScale(dsaveScaleX, dsaveScaleY, (float)mBitmap.getWidth() / 2,
                                    (float) mBitmap.getHeight() / 2);
                            drawM.postTranslate(X, Y);


                            RealM.reset();
                            RealM.postRotate(mRotate, ((mBitmap.getWidth()) / 2f),
                                    ((mBitmap.getHeight()) / 2f));
                            RealM.postScale(saveScaleX, saveScaleY, ((mBitmap.getWidth()) / 2f),
                                    ((mBitmap.getHeight()) / 2f));
                            RealM.postTranslate(RealTimeX, RealTimeY);

                        }
                    }
                    DownX = event.getX();
                    DownY = event.getY();
                    mxAndyFixer.FixXYUsingMatrix(event.getX(), event.getY(), getImageMatrix());
                    DownRealTimeX = mxAndyFixer.getFixedXYUsingMartix().x;
                    DownRealTimeY = mxAndyFixer.getFixedXYUsingMartix().y;
                }

                invalidate();
                return true;
            }
            case MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                if (ColorLoc){
                    setImageRotate(mRotate);
                    setBorderColor(mColor);
                    ((ImageBorderActivity) context).Border1Color.setImageBitmap(
                            ColorDetector.getCroppedBitmap(((ImageBorderActivity) context).Border1Color,
                                    mColor));

                }
                ColorLoc = false;

                mode = NONE;
                return true;
            }
        }
        return false;
    }

    public Bitmap getFinalBitmap(){
        Bitmap bitmap = Bitmap.createBitmap((int)RtargetWidth, (int) RtargetHeight,mBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0,0,RtargetWidth,RtargetHeight, mRBorderPaint);

        canvas.drawBitmap(mBitmap,RealM,bmpaint);
        return bitmap;
    }

    /**
     *
     */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector)
        {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor = detector.getScaleFactor();

            saveScaleX *= mScaleFactor;
            saveScaleY *= mScaleFactor;

            dsaveScaleX *= mScaleFactor;
            dsaveScaleY *= mScaleFactor;



            drawM.reset();
            drawM.postRotate(mRotate,(float)mBitmap.getWidth()/2,
                    (float)mBitmap.getHeight()/2);
            drawM.postScale(dsaveScaleX, dsaveScaleY, (float) mBitmap.getWidth()/2,
                    (float)mBitmap.getHeight()/2);
            drawM.postTranslate(X,Y);

            X_YFixer xAndyFixer = new X_YFixer();
            xAndyFixer.FixXYUsingMatrix(X,Y,
                    getImageMatrix());

            RealM.reset();
            RealM.postRotate(mRotate,((mBitmap.getWidth())/2f),
                    ((mBitmap.getHeight())/2f));
            RealM.postScale(saveScaleX, saveScaleY,(float) mBitmap.getWidth()/2,
                    (float)mBitmap.getHeight()/2);
            RealM.postTranslate(RealTimeX, RealTimeY);
            invalidate();
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            //((ImageBorderActivity)(context)).firstBorder.setProgress((int) (saveScaleX*100));
        }
    }
}

