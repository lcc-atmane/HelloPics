package com.phantomhive.exil.hellopics.Img_Editor.Views.ImageStretch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.InputStream;

@SuppressLint("AppCompatCustomView")
public class ieStretch extends ImageView {
    private static final String TAG = "MyView";
    private Matrix initMatrix = new Matrix();
    private int hd,wd;
    //Related variables built for drawBitmapMesh, start
    private final static int WIDTH =20;
    private final static int HEIGHT =20;
    private final static int COUNT = (WIDTH+1)*(HEIGHT+1);
    private float[] verts = new float[COUNT*2];
    private float[] origs = new float[COUNT*2];
    //Related variables built for drawBitmapMesh, end
    public Bitmap mBitmap,mSBitmap = null;
    float offsetStartX=0.0f,offsetStartY=0.0f;//Centered display adjustment

    public ieStretch(Context context) {
        super(context);
        init(context);
    }

    public ieStretch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ieStretch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ieStretch(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onMeasure: "+getWidth()+" h "+getHeight());
        float division0 = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();
        float division1 = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();
        int FW = (int) (getHeight()*division1);
        int FH = (int) (getWidth()*division0);

        if (mBitmap.getWidth()>mBitmap.getHeight()| mBitmap.getWidth()==mBitmap.getHeight()){
            mSBitmap = Bitmap.createScaledBitmap(mBitmap,
                    getWidth() ,FH ,true);
            Log.d("TAG", "onResourceReady: -1");

        }else if (mBitmap.getWidth()<mBitmap.getHeight() ){
            if (FH<getHeight()) {
                mSBitmap = Bitmap.createScaledBitmap(mBitmap,
                        getWidth() ,FH ,true);
                Log.d("TAG", "onResourceReady: 1");
            }else {
                mSBitmap = Bitmap.createScaledBitmap(mBitmap,FW,
                        getHeight() ,true);
                Log.d("TAG", "onResourceReady: 2");
            }
        }
        //mcanvas= new Canvas(mSBitmap);
        //2. Build origs array (processing data) and verts array (saving original data) according to the specified method of drawBitmapMesh
        wd = mSBitmap.getWidth();
        hd = mSBitmap.getHeight();
        int index=0;
        for(int i=0;i<HEIGHT+1;i++){
            float fy=(hd*i)/HEIGHT;
            for(int j=0;j<WIDTH+1;j++){
                float fx=(wd*j)/WIDTH;
                //The record format is: (x1,y1), (x2,y2), (x3,y3), and so on
                origs[index*2+0]=verts[index*2+0]=fx;
                origs[index*2+1]=verts[index*2+1]=fy;
                index++;
            }
        }
        Log.d(TAG, "onMeasure: "+WIDTH+" h "+HEIGHT);
    }

    private void init(Context context){
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Center display
        offsetStartX = (canvas.getWidth()-mSBitmap.getWidth())*1.0f/2;
        offsetStartY = (canvas.getHeight()-mSBitmap.getHeight())*1.0f/2;
        canvas.translate(offsetStartX,offsetStartY);
        //5 bitmap drawing. When the vert array changes, the local part of the picture will be adjusted accordingly.
        canvas.drawBitmapMesh(mSBitmap,WIDTH,HEIGHT,verts,0,null,0,null);
        //canvas.drawBitmap(mSBitmap,0,0,null);
    }

    private void wrap(float x,float y){
        //4 key algorithm, distortion effect design, and construct distortion effect according to the input event coordinates.
        for(int i=0;i<COUNT*2;i+=2){
            float dx = x-origs[i];
            float dy = y-origs[i+1];
            float d = (float)Math.sqrt(dx*dx+dy*dy);
            float pull = 200000.f/(d*d*d);
            if(pull>=1){
                verts[i] = x;
                verts[i+1] = y;
            }/*else{
                verts[i] = origs[i]+dx*pull;
                verts[i+1] = origs[i+1]+dy*pull;
            }*/
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //3 coordinate correction is made here to match the central display.
        wrap(event.getX()-offsetStartX,event.getY()-offsetStartY);
        return true;
    }
}
