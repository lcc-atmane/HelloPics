package com.phantomhive.exil.hellopics.Img_Editor.Views.RespectiveCrop;

import static com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards.createCheckerboard;
import static com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards.getBitmapRect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.Zommer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.X_YFixer;

@SuppressLint("AppCompatCustomView")
public class RespectiveCrop extends View {
    // Tag.
    private final static String TAG = "RespectiveCrop";
    float DownX = 0,DownY = 0;

    // circles
    // c1
    float c1left,c1top; boolean c1touched = false;
    // c2
    float c2left,c2bottom; boolean c2touched = false;
    // c3
    float c3right,c3top; boolean c3touched = false;
    //c4
    float c4right,c4bottom; boolean c4touched = false;

    Paint paint =  new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint CirclePaint = new Paint();
    Path path;

    ViewZommer zommer;
    float a1,b1,c1,d1,a2,b2,c2,d2;

    X_YFixer dxAndyFixer;
    X_YFixer rxAndyFixer;
    private Bitmap mBitmap;


    Paint Checkerboardpaint;
    Bitmap createCheckerboard;
    public RespectiveCrop(Context context) {
        super(context);
        init(context,null);
    }

    public RespectiveCrop(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public RespectiveCrop(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public RespectiveCrop(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    public void init(Context context, AttributeSet attributeSet){
        zommer = new ViewZommer(this,context);
        rxAndyFixer = new X_YFixer();
        dxAndyFixer = new X_YFixer();
        path = new Path();

        Checkerboardpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Checkerboardpaint.setAntiAlias(true);
        Checkerboardpaint.setFilterBitmap(true);
        Checkerboardpaint.setDither(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        c1left = getBitmapRect().left + 50; c1top = getBitmapRect().top + 50;
        c2left = getBitmapRect().left + 50; c2bottom = getBitmapRect().bottom - 50;
        c3right = getBitmapRect().right - 50; c3top = getBitmapRect().top + 50;
        c4right = getBitmapRect().right - 50; c4bottom = getBitmapRect().bottom - 50;
        Log.d(TAG, "onLayout: im Working");
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        zommer.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mBitmap.hasAlpha()){
            createCheckerboard= createCheckerboard((int) CheckerBoards.getBitmapRect(zommer.getDefaultMatrix(),mBitmap,
                            getWidth(),getHeight()).width(),

                    (int) CheckerBoards.getBitmapRect(zommer.getDefaultMatrix(),mBitmap,
                            getWidth(),getHeight()).height(),20);

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
         /*float width = getWidth();
        float height = getHeight();

        //Fit to screen.
        Bitmap bitmap = ViewZommer.getScaledBitmap(mBitmap,this);
        float scale;
        float scaleX =  width / bitmap.getWidth();
        float scaleY = height / bitmap.getHeight();
        scale = Math.min(scaleX, scaleY);

        // Center the image
        float redundantYSpace = height - (scale * bitmap.getHeight());
        float redundantXSpace = width - (scale * bitmap.getWidth());
        redundantYSpace /= 2;
        redundantXSpace /= 2;

        c1left = redundantXSpace + 50; c1top = redundantYSpace + 50;
        c2left = redundantXSpace + 50; c2bottom = (redundantYSpace+bitmap.getHeight()) - 50;
        c3right = (redundantXSpace+bitmap.getWidth()) - 50; c3top = redundantYSpace + 50;
        c4right =(redundantXSpace+bitmap.getWidth()) - 50; c4bottom = (redundantYSpace+bitmap.getHeight()) - 50;

        Log.d(TAG, "onLayout: im Working222222");*/
    }

    public void setImageBitmap(Bitmap bm) {
        mBitmap  = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());
        zommer.setBitmap(bm);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setMatrix(zommer.getCanvasMatrix());

        if (createCheckerboard != null){
            canvas.drawBitmap(createCheckerboard,zommer.DefaultXY().x,zommer.DefaultXY().y,null);
        }

        canvas.drawBitmap(mBitmap,zommer.getDefaultMatrix(),Checkerboardpaint);

        paint.setColor(Color.parseColor("#A6A061"));
        paint.setStrokeWidth(5f/zommer.getScaleX());

        CirclePaint.setStrokeWidth(35f/zommer.getScaleX());
        CirclePaint.setColor(Color.parseColor("#A6A061"));
        CirclePaint.setAlpha(150);


        canvas.drawCircle(c1left,c1top,30/zommer.getScaleX(),CirclePaint);

        canvas.drawCircle(c2left,c2bottom,30/zommer.getScaleX(),CirclePaint);

        canvas.drawCircle(c3right,c3top,30/zommer.getScaleX(),CirclePaint);

        canvas.drawCircle(c4right,c4bottom,30/zommer.getScaleX(),CirclePaint);




        a1 = Math.min(c1left,c2left);
        a2 = Math.min(c1top,c3top);

        b1= a1;
        b2 = Math.max(c2bottom,c4bottom);

        c1= Math.max(c3right,c4right);
        c2 = a2;

        d1 = c1;
        d2 = b2;
        /*
        canvas.drawCircle(a1,a2,10,paint);
        canvas.drawCircle(b1,b2,10,paint);
        canvas.drawCircle(c1,c2,10,paint);
        canvas.drawCircle(d1,d2,10,paint);
         */


        canvas.drawLine(c3right,c3top,c1left,c1top,paint);
        canvas.drawLine(c1left,c1top,c2left,c2bottom,paint);
        canvas.drawLine(c2left,c2bottom,c4right,c4bottom,paint);
        canvas.drawLine(c4right,c4bottom,c3right,c3top,paint);

        path = new Path();
        path.moveTo(c3right,c3top);
        path.lineTo(c1left,c1top);

        path.lineTo(c1left,c1top);
        path.lineTo(c2left,c2bottom);

        path.lineTo(c2left,c2bottom);
        path.lineTo(c4right,c4bottom);

        path.lineTo(c4right,c4bottom);
        path.lineTo(c3right,c3top);

//        Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        paint.setAlpha(120);
        //canvas.drawPath(path,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        zommer.OnTouch(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN :

                dxAndyFixer.FixXYUsingMatrix(event.getX(),event.getY(),zommer.getCanvasMatrix());
                DownX = dxAndyFixer.getFixedXYUsingMartix().x;
                DownY = dxAndyFixer.getFixedXYUsingMartix().y;

                if (isClicked(DownX,DownY, c1left,c1top)){
                    c1touched = true;
                }

                if (isClicked(DownX,DownY, c2left,c2bottom)){
                    c2touched = true;
                }

                if (isClicked(DownX,DownY, c3right,c3top)){
                    c3touched = true;
                }

                if (isClicked(DownX,DownY,c4right,c4bottom)){
                    c4touched = true;
                }

                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE :
                dxAndyFixer.FixXYUsingMatrix(event.getX(),event.getY(),zommer.getCanvasMatrix());
                if (c1touched){
                    c1left += dxAndyFixer.getFixedXYUsingMartix().x - DownX;
                    c1top += dxAndyFixer.getFixedXYUsingMartix().y - DownY ;
                }
                if (c2touched){
                    c2left += dxAndyFixer.getFixedXYUsingMartix().x - DownX;
                    c2bottom += dxAndyFixer.getFixedXYUsingMartix().y - DownY ;
                }
                if (c3touched){
                    c3right += dxAndyFixer.getFixedXYUsingMartix().x - DownX;
                    c3top += dxAndyFixer.getFixedXYUsingMartix().y - DownY ;
                }
                if (c4touched){
                    c4right += dxAndyFixer.getFixedXYUsingMartix().x - DownX;
                    c4bottom += dxAndyFixer.getFixedXYUsingMartix().y - DownY ;
                }
                DownX = dxAndyFixer.getFixedXYUsingMartix().x; DownY = dxAndyFixer.getFixedXYUsingMartix().y;

                invalidate();
                return true;
            case MotionEvent.ACTION_UP :
                c1touched = false;
                c2touched = false;
                c3touched = false;
                c4touched = false;
                return true;
        }
        return true;
    }



    /**
     * The final bitmap Of The Cropped Bitmap Area Using Path.
     * For More info Visit : https://stackoverflow.com/a/12089127.
     * @return The Final Free Cropped Image.
     */
    /*
        public Bitmap getImage0(){
        final Drawable drawable = getDrawable();
        if (!(drawable instanceof BitmapDrawable)) {
            return null;
        }


        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        getImageMatrix().getValues(matrixValues);

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        final float scaleX = matrixValues[Matrix.MSCALE_X];
        final float scaleY = matrixValues[Matrix.MSCALE_Y];
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];

        // Ensure that the left and top edges are not outside of the ImageView bounds.
        final float bitmapLeft = (transX < 0) ? Math.abs(transX) : 0;
        final float bitmapTop = (transY < 0) ? Math.abs(transY) : 0;

        RectF rectF =new RectF();
        getImageMatrix().mapRect(rectF);

        // Get the original bitmap object.
        Bitmap output = ((BitmapDrawable) drawable).getBitmap();
        Matrix matrix = new Matrix();

        float [] src = {
                0f, 0f, // top left point
                output.getWidth(), 0f, // top right point
                output.getWidth(), output.getHeight(), // bottom right point
                0f, output.getHeight() // bottom left point
        };
        float [] dst = new float[8];

        float C1left = 0,C1top = 0;
        float C2left = 0,C2bottom = 0;
        float C3right = 0,C3top = 0;
        float C4right = 0,C4bottom = 0;

        if (rectF.left <= 0){
            dst = new float[]{
                    (bitmapLeft + c1left) / scaleX, (bitmapTop + c1top - rectF.top) / scaleY, // top left point
                    (bitmapLeft + c3right) / scaleX, (bitmapTop + c3top- rectF.top) / scaleY, // top right point
                    (bitmapLeft + c4right) / scaleX, (bitmapTop + c4bottom- rectF.top) / scaleY, // bottom right point
                    (bitmapLeft + c2left) / scaleX, (bitmapTop + c2bottom- rectF.top) / scaleY // bottom left point
            };

             C1left = (bitmapLeft+c1left)/scaleX;
             C1top = (bitmapTop+c1top- rectF.top)/scaleY;

            // c2
             C2left =(bitmapLeft+c2left)/scaleX;
             C2bottom = (bitmapTop+c2bottom- rectF.top)/scaleY;


            // c3
             C3right = (bitmapLeft+c3right)/scaleX;
             C3top = (bitmapTop+c3top- rectF.top)/scaleY;

            //c4
             C4right = (bitmapLeft+c4right)/scaleX;
             C4bottom = (bitmapTop+c4bottom- rectF.top)/scaleY;

        }else if (rectF.top <= 0){

            dst = new float[]{
                    (bitmapLeft + c1left - rectF.left) / scaleX, (bitmapTop + c1top) / scaleY, // top left point
                    (bitmapLeft + c3right - rectF.left) / scaleX, (bitmapTop + c3top) / scaleY, // top right point
                    (bitmapLeft + c4right - rectF.left) / scaleX, (bitmapTop + c4bottom) / scaleY, // bottom right point
                    (bitmapLeft + c2left - rectF.left) / scaleX, (bitmapTop + c2bottom) / scaleY // bottom left point
            };

             C1left = (bitmapLeft+c1left- rectF.left)/scaleX;
             C1top = (bitmapTop+c1top)/scaleY;


            // c2
             C2left =(bitmapLeft+c2left- rectF.left)/scaleX ;
             C2bottom = (bitmapTop+c2bottom)/scaleY;

            // c3
             C3right = (bitmapLeft+c3right- rectF.left)/scaleX;
             C3top = (bitmapTop+c3top)/scaleY;

            //c4
             C4right = (bitmapLeft+c4right- rectF.left)/scaleX;
             C4bottom = (bitmapTop+c4bottom)/scaleY;
        }

        float x0 =  Math.max(C3right, C4right);
        float x1 =  Math.min(C1left, C2left);
        float width = x0 - x1;

        float y0 =  Math.max(C2bottom, C4bottom);
        float y1 =  Math.min(C1top,C3top);
        float height =y0-y1;



        int pointCount = 4; // number of points
        if (!OpenCVLoader.initDebug()){

        }
        Mat inputMat = new Mat();
        Mat outputMat = new Mat();

        Utils.bitmapToMat(output, inputMat);
        List<Point> src_pnt = new ArrayList<>();
        Point p0 = new Point( (bitmapLeft + c1left - rectF.left) / scaleX, (bitmapTop + c1top) / scaleY);
        src_pnt.add(p0);
        Point p1 = new Point( (bitmapLeft + c3right - rectF.left) / scaleX, (bitmapTop + c3top) / scaleY);
        src_pnt.add(p1);
        Point p2 = new Point((bitmapLeft + c4right - rectF.left) / scaleX, (bitmapTop + c4bottom) / scaleY);
        src_pnt.add(p2);
        Point p3 = new Point( (bitmapLeft + c2left - rectF.left) / scaleX, (bitmapTop + c2bottom) / scaleY);
        src_pnt.add(p3);
        Mat startM = Converters.vector_Point2f_to_Mat(src_pnt);

        List<Point> dst_pnt = new ArrayList<>();
        Point p4 = new Point(0.0, 0.0);
        dst_pnt.add(p4);
        Point p5 = new Point( output.getWidth(), 0f);
        dst_pnt.add(p5);
        Point p6 = new Point(output.getWidth(), output.getHeight());
        dst_pnt.add(p6);
        Point p7 = new Point(0f, output.getHeight());
        dst_pnt.add(p7);

        Mat endM = Converters.vector_Point2f_to_Mat(dst_pnt);
        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM);
        Size size = new Size(output.getWidth(), output.getHeight());
        Scalar scalar = new Scalar(50.0);
        Imgproc.warpPerspective(inputMat, outputMat, perspectiveTransform, size,
                Imgproc.INTER_LINEAR + Imgproc.CV_WARP_FILL_OUTLIERS,Core.BORDER_DEFAULT);
        Utils.matToBitmap(outputMat, output);
        Toast.makeText(getContext(),
                "Circle1 : "+" c1left : "+(int)C1left+" c1top : "+(int)C1top+
                        " \n "+
                "Circle2 : "+" c2left : "+(int)C2left+" c2bottom : "+(int)C2bottom+
                        " \n "+
                "Circle3 : "+" c3right : "+(int)C3right+" c3top : "+(int)C3top+
                        " \n "+
                "Circle4 : "+" c4right : "+(int)C4right+" c4bottom : "+(int)C4bottom+
                        " \n ", Toast.LENGTH_LONG).show();
        matrix.setPolyToPoly(dst,0,src,0,pointCount);


        //Bitmap bitmap = Bitmap.createBitmap((int) Math.ceil(Width), (int) Math.ceil(Height), output.getConfig());

        RectF rect = new RectF( C1left, C1top,C2left, C2bottom);


        RectF cropRect = new RectF( Math.min(C1left, C2left),
                Math.min(C1top, C3top),
                Math.max(C4right, C3right),
                Math.max(C4bottom, C2bottom));
        Bitmap cut = Bitmap.createBitmap(output,(int) cropRect.left, (int)cropRect.top, (int)cropRect.width(), (int)cropRect.height() );


        Bitmap bitmap = Bitmap.createBitmap (((int) width),  ((int) height), output.getConfig());

        Log.d(TAG, "getImage: "+width+" "+height);
        Canvas canvas = new Canvas(bitmap);
        //canvas.concat(matrix);
        canvas.drawBitmap(output,matrix,null);
        //canvas.drawBitmapMesh(output, WIDTH_BLOCK, HEIGHT_BLOCK, generateVertices(width, height), 0, null, 0, null);
        return output;
    }
 */




    public Bitmap getImage2(){

        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        zommer.getDefaultMatrix().getValues(matrixValues);

        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        final float scaleX = matrixValues[Matrix.MSCALE_X];
        final float scaleY = matrixValues[Matrix.MSCALE_Y];
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];

        // Ensure that the left and top edges are not outside of the ImageView bounds.
        final float bitmapLeft = (transX < 0) ? Math.abs(transX) : 0;
        final float bitmapTop = (transY < 0) ? Math.abs(transY) : 0;

        RectF rectF =new RectF();
        zommer.getDefaultMatrix().mapRect(rectF);

        // Get the original bitmap object.
        Bitmap output = mBitmap;
        Matrix matrix = new Matrix();

        float C1left = 0,C1top = 0;
        float C2left = 0,C2bottom = 0;
        float C3right = 0,C3top = 0;
        float C4right = 0,C4bottom = 0;

        if (rectF.left <= 0){

            // c0
            C1left = (bitmapLeft+c1left)/scaleX;
            C1top = (bitmapTop+c1top- rectF.top)/scaleY;

            // c1
            C2left =(bitmapLeft+c2left)/scaleX;
            C2bottom = (bitmapTop+c2bottom- rectF.top)/scaleY;

            // c2
            C3right = (bitmapLeft+c3right)/scaleX;
            C3top = (bitmapTop+c3top- rectF.top)/scaleY;

            //c3
            C4right = (bitmapLeft+c4right)/scaleX;
            C4bottom = (bitmapTop+c4bottom- rectF.top)/scaleY;

        }else if (rectF.top <= 0){

            // c0
            C1left = (bitmapLeft+c1left- rectF.left)/scaleX;
            C1top = (bitmapTop+c1top)/scaleY;

            // c1
            C2left =(bitmapLeft+c2left- rectF.left)/scaleX ;
            C2bottom = (bitmapTop+c2bottom)/scaleY;

            // c2
            C3right = (bitmapLeft+c3right- rectF.left)/scaleX;
            C3top = (bitmapTop+c3top)/scaleY;

            //c3
            C4right = (bitmapLeft+c4right- rectF.left)/scaleX;
            C4bottom = (bitmapTop+c4bottom)/scaleY;

        }
        Path path = new Path();
        path.moveTo(C3right,C3top);
        path.lineTo(C1left,C1top);

        path.lineTo(C1left,C1top);
        path.lineTo(C2left,C2bottom);

        path.lineTo(C2left,C2bottom);
        path.lineTo(C4right,C4bottom);

        path.lineTo(C4right,C4bottom);
        path.lineTo(C3right,C3top);
        // the X & y of the Scale.
        float projectedX =  ((float) output.getWidth()/(float) getWidth());
        float projectedY =  ((float) output.getHeight()/(float)getHeight());
        matrix.setScale(projectedX,projectedY);
        Bitmap bitmap = Bitmap.createBitmap (output.getWidth(), (output.getHeight()), output.getConfig());

        Canvas canvas = new Canvas(bitmap);
        // Crop out the selected portion of the image...
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, 0, 0, paint);


        // Create a bitmap with just the cropped area.
        Region region = new Region();
        Region clip = new Region(0, 0, bitmap.getWidth(), bitmap.getHeight());
        region.setPath(path, clip);
        Rect bounds = region.getBounds();

        return Bitmap.createBitmap(bitmap, bounds.left, bounds.top, bounds.width(), bounds.height());
    }

    public Bitmap getImage3(){


        // Get the original bitmap object.
        Bitmap output = mBitmap;

        float C1left,C1top;
        float C2left,C2bottom;
        float C3right,C3top;
        float C4right,C4bottom;



        //Bitmap bitmap = Bitmap.createBitmap(output,(int)a1,(int)a2,(int)bwidth,(int)bheight);

       // setImageBitmap(bitmap);

        // c0
        C1left = X_YFixer.getfixedXYUsingMartix(c1left,c1top,zommer.getDefaultMatrix()).x;
        C1top =  X_YFixer.getfixedXYUsingMartix(c1left,c1top,zommer.getDefaultMatrix()).y;

        // c1
        C2left = X_YFixer.getfixedXYUsingMartix(c2left,c2bottom,zommer.getDefaultMatrix()).x;
        C2bottom =  X_YFixer.getfixedXYUsingMartix(c2left,c2bottom,zommer.getDefaultMatrix()).y;

        // c2
        C3right = X_YFixer.getfixedXYUsingMartix(c3right,c3top,zommer.getDefaultMatrix()).x;
        C3top = X_YFixer.getfixedXYUsingMartix(c3right,c3top,zommer.getDefaultMatrix()).y;

        //c3
        C4right = X_YFixer.getfixedXYUsingMartix(c4right,c4bottom,zommer.getDefaultMatrix()).x;
        C4bottom = X_YFixer.getfixedXYUsingMartix(c4right,c4bottom,zommer.getDefaultMatrix()).y;

        float x0 =  Math.max(C3right, C4right);
        float x1 =  Math.min(C1left, C2left);
        float width = x0 - x1;

        float y0 =  Math.max(C2bottom, C4bottom);
        float y1 =  Math.min(C1top,C3top);
        float height =y0-y1;

        int pointCount = 4; // number of points

        Matrix matrix = new Matrix();

        float [] src = {
                0f, 0f, // top left point
                output.getWidth(), 0f, // top right point
                output.getWidth(), output.getHeight(), // bottom right point
                0f, output.getHeight() // bottom left point
        };
        float [] dst;

        dst = new float[]{
                C1left, C1top, // top left point
                C3right, C3top, // top right point
                C4right, C4bottom, // bottom right point
                C2left, C2bottom // bottom left point
        };

        matrix.setPolyToPoly(dst,0,src,0,pointCount);

        Bitmap bitmap2 = Bitmap.createBitmap (output.getWidth(), (output.getHeight()), output.getConfig());

        Canvas canvas = new Canvas(bitmap2);
        //canvas.concat(matrix);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawBitmap(output,matrix,paint);

        return Bitmap.createScaledBitmap(bitmap2,(int)width,(int)height,true);
    }

    public Bitmap getImage() {

        Bitmap output = mBitmap;

        // Get the corner coordinates
        PointF c1 = X_YFixer.getfixedXYUsingMartix(c1left, c1top, zommer.getDefaultMatrix());
        PointF c2 = X_YFixer.getfixedXYUsingMartix(c2left, c2bottom, zommer.getDefaultMatrix());
        PointF c3 = X_YFixer.getfixedXYUsingMartix(c3right, c3top, zommer.getDefaultMatrix());
        PointF c4 = X_YFixer.getfixedXYUsingMartix(c4right, c4bottom, zommer.getDefaultMatrix());

        // Calculate width and height based on the average of two sides
        float width = (distance(c1, c3) + distance(c2, c4)) / 2f;
        float height = (distance(c1, c2) + distance(c3, c4)) / 2f;

        // Create the transformation matrix
        float[] src = {
                0, 0,
                output.getWidth(), 0,
                output.getWidth(), output.getHeight(),
                0, output.getHeight()
        };

        float[] dst = {
                c1.x, c1.y,
                c3.x, c3.y,
                c4.x, c4.y,
                c2.x, c2.y
        };

        Matrix matrix = new Matrix();
        matrix.setPolyToPoly(dst, 0, src, 0, 4);

        // Create a new bitmap with the original dimensions
        Bitmap transformedBitmap = Bitmap.createBitmap(output.getWidth(), output.getHeight(), output.getConfig());
        Canvas canvas = new Canvas(transformedBitmap);

        // Draw the transformed bitmap
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawBitmap(output, matrix, paint);

        // Create the final cropped and scaled bitmap
        return Bitmap.createScaledBitmap(transformedBitmap, (int)width, (int)height, true);
    }

    // Helper method to calculate distance between two points
    private float distance(PointF p1, PointF p2) {
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
    /**
     * Gets the bounding rectangle of the bitmap within the ImageView.
     */
    private RectF getBitmapRect() {

        final Bitmap drawable =mBitmap;
        if (drawable == null) {
            return new RectF();
        }

        // Get image matrix values and place them in an array.
        final float[] matrixValues = new float[9];
        zommer.getDefaultMatrix().getValues(matrixValues);

        // Extract the scale and translation values from the matrix.
        final float scaleX = matrixValues[Matrix.MSCALE_X];
        final float scaleY = matrixValues[Matrix.MSCALE_Y];
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];

        // Get the width and height of the original bitmap.
        final int drawableIntrinsicWidth = drawable.getWidth();
        final int drawableIntrinsicHeight = drawable.getHeight();

        // Calculate the dimensions as seen on screen.
        final int drawableDisplayWidth = Math.round(drawableIntrinsicWidth * scaleX);
        final int drawableDisplayHeight = Math.round(drawableIntrinsicHeight * scaleY);

        // Get the Rect of the displayed image within the ImageView.
        final float left = Math.max(transX, 0);
        final float top = Math.max(transY, 0);
        final float right = Math.min(left + drawableDisplayWidth, getWidth());
        final float bottom = Math.min(top + drawableDisplayHeight, getHeight());

        return new RectF(left, top, right, bottom);
    }

    /**
     *  for more Visit -->  https://stackoverflow.com/a/16792888
     *  & ---> https://stackoverflow.com/a/36146901
     * @param downX X position of touche of user Finger in Screen
     * @param downY Y position of touche of user Finger in Screen
     * @param x X position of Circle in the screen
     * @param y X position of Circle in the screen
     * @return distance.
     */
    private boolean isClicked(float downX, float downY, float x, float y){
        int distance = (int) Math.sqrt(((downX - x) * (downX - x)) +
                ((downY - y) * (downY - y)) );
        return distance < 50/zommer.getScaleX();
    }
}
