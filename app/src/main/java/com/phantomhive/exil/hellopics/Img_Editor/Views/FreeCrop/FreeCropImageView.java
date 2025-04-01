package com.phantomhive.exil.hellopics.Img_Editor.Views.FreeCrop;

import static com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards.createCheckerboard;
import static com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards.getBitmapRect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.FreeCropActivity;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.X_YFixer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.Zommer;

import java.util.ArrayList;


public class FreeCropImageView extends View {


    private static final String TAG ="FreeCropImageView";
    Context mcontext;

    // the Brush Path
    Path TempraryBrushPath;
    Path RemainingBrushPath;
    Path RealEraseBrushPath;
    Path RealCropBrushPath;

    Paint EraseBrushPaint;

    Paint CropBrushPaint;

    // The Eraser Path.
    Paint TempraryBrushPaint;
    Paint RemainingBrushPaint;
    Paint DrwanBitmapsOnCanvasPaint;
    Paint CorrectionGuidBitmapPaint;

    // The Stroke Width of Lasso
    private static  float ERASER_STROKE_WIDTH =50f;

    // type of The Draw (Path)
    PathType mPathType = PathType.ERASER;

    ViewZommer zommer;
    X_YFixer mxAndyFixer;
    ArrayList<Float> mTouchePoint = new ArrayList<>();

    Canvas FixCanvasForTheErasedBitmap;

    Canvas TraceCanvasInCropModeCanvas;
    Canvas CroppedBitmapCanvas,ErasedbitmapCanvas;

    Bitmap OriginalBitmap, CorrectionGuidBitmap, Erasedbitmap, FixBitmapForTheErasedBitmap;

    Bitmap OriginalCropBitmap, CroppedBitmap;
    private Bitmap TraceCanvasInCropModeBitmap;

    boolean eraser = false;

    float hardness = 5;
    int mOpacity =255;

    Paint Checkerboardpaint;
    Bitmap createCheckerboard;

    public FreeCropImageView(Context context) {
        super(context);
        init(context, null);
    }

    public FreeCropImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FreeCropImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public FreeCropImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    @SuppressLint("ResourceAsColor")
    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        mcontext = context;
        zommer = new ViewZommer(this,context);
        mxAndyFixer = new X_YFixer();

       // mBrushPath
        TempraryBrushPath = new Path();
        RemainingBrushPath = new Path();
        RealEraseBrushPath = new Path();
        RealCropBrushPath = new Path();

        // mBrushPaint
        EraseBrushPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        EraseBrushPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        EraseBrushPaint.setAntiAlias(true);
        EraseBrushPaint.setFilterBitmap(true);
        EraseBrushPaint.setStrokeWidth(ERASER_STROKE_WIDTH);
        EraseBrushPaint.setStyle(Paint.Style.STROKE);
        EraseBrushPaint.setStrokeJoin(Paint.Join.ROUND);
        EraseBrushPaint.setStrokeCap(Paint.Cap.ROUND);


        CropBrushPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        CropBrushPaint.setAntiAlias(true);
        CropBrushPaint.setFilterBitmap(true);
        CropBrushPaint.setAlpha(255);
        CropBrushPaint.setStrokeWidth(ERASER_STROKE_WIDTH);
        CropBrushPaint.setStyle(Paint.Style.STROKE);
        CropBrushPaint.setStrokeJoin(Paint.Join.ROUND);
        CropBrushPaint.setStrokeCap(Paint.Cap.ROUND);

       // mEraserPaint
        TempraryBrushPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TempraryBrushPaint.setColor(Color.parseColor("#A6A061"));
        TempraryBrushPaint.setAlpha(200);
        TempraryBrushPaint.setAntiAlias(true);
        TempraryBrushPaint.setFilterBitmap(true);
        TempraryBrushPaint.setStrokeWidth(ERASER_STROKE_WIDTH);
        TempraryBrushPaint.setStyle(Paint.Style.STROKE);
        TempraryBrushPaint.setStrokeJoin(Paint.Join.ROUND);
        TempraryBrushPaint.setStrokeCap(Paint.Cap.ROUND);

        RemainingBrushPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        RemainingBrushPaint.setColor(Color.parseColor("#A6A061"));
        RemainingBrushPaint.setAntiAlias(true);
        RemainingBrushPaint.setFilterBitmap(true);
        RemainingBrushPaint.setStrokeWidth(ERASER_STROKE_WIDTH);
        RemainingBrushPaint.setStyle(Paint.Style.STROKE);
        RemainingBrushPaint.setStrokeJoin(Paint.Join.ROUND);
        RemainingBrushPaint.setStrokeCap(Paint.Cap.ROUND);

        CorrectionGuidBitmapPaint = new Paint();
        CorrectionGuidBitmapPaint.setAlpha(100);
        CorrectionGuidBitmapPaint.setAntiAlias(true);
        CorrectionGuidBitmapPaint.setFilterBitmap(true);


        DrwanBitmapsOnCanvasPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        DrwanBitmapsOnCanvasPaint.setAntiAlias(true);
        DrwanBitmapsOnCanvasPaint.setFilterBitmap(true);

        Checkerboardpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Checkerboardpaint.setAntiAlias(true);
        Checkerboardpaint.setFilterBitmap(true);
        Checkerboardpaint.setDither(true);

    }


    public void setImageBitmap(Bitmap bm) {
        OriginalBitmap = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());;
        OriginalCropBitmap = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());;
        zommer.setBitmap(bm);
        Bitmap bm1 = Bitmap.createBitmap(bm.getWidth(),bm.getHeight(),bm.getConfig());

    }
    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        zommer.onMeasure(widthMeasureSpec, heightMeasureSpec);
        createCheckerboard= createCheckerboard((int) CheckerBoards.getBitmapRect(zommer.getDefaultMatrix(),OriginalBitmap,
                        getWidth(),getHeight()).width(),

                (int) CheckerBoards.getBitmapRect(zommer.getDefaultMatrix(),OriginalBitmap,
                        getWidth(),getHeight()).height(),20);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        CorrectionGuidBitmap = zommer.getScaledBitmap();


        Erasedbitmap = Bitmap.createBitmap(OriginalBitmap.getWidth(), OriginalBitmap.getHeight(), OriginalBitmap.getConfig());
        ErasedbitmapCanvas = new Canvas(Erasedbitmap);

        TraceCanvasInCropModeBitmap =Bitmap.createBitmap(OriginalBitmap.getWidth(), OriginalBitmap.getHeight(), OriginalBitmap.getConfig());
        TraceCanvasInCropModeCanvas = new Canvas(TraceCanvasInCropModeBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        ErasedbitmapCanvas.drawBitmap(OriginalBitmap,0,0,paint);

        FixBitmapForTheErasedBitmap = Bitmap.createBitmap(OriginalBitmap.getWidth(), OriginalBitmap.getHeight(), OriginalBitmap.getConfig());
        FixCanvasForTheErasedBitmap = new Canvas(FixBitmapForTheErasedBitmap);

        CroppedBitmap = Bitmap.createBitmap(OriginalCropBitmap.getWidth(), OriginalCropBitmap.getHeight(), OriginalCropBitmap.getConfig());
        CroppedBitmapCanvas = new Canvas(CroppedBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setMatrix(zommer.getCanvasMatrix());

        if (createCheckerboard != null){
            canvas.drawBitmap(createCheckerboard,zommer.DefaultXY().x,zommer.DefaultXY().y,null);
        }

        if (mPathType == PathType.ERASER){
            canvas.drawBitmap(Erasedbitmap,zommer.getDefaultMatrix(), DrwanBitmapsOnCanvasPaint);
        }else{
            canvas.drawBitmap(OriginalBitmap,zommer.getDefaultMatrix(), DrwanBitmapsOnCanvasPaint);
            canvas.drawBitmap(TraceCanvasInCropModeBitmap,zommer.getDefaultMatrix(), null);
        }

        CDrawPath(canvas);

        if (eraser){

            canvas.drawBitmap(CorrectionGuidBitmap,zommer.DefaultXY().x,zommer.DefaultXY().y, CorrectionGuidBitmapPaint);
        }


        //https://stackoverflow.com/questions/45574361/erase-drawing-in-canvas
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        zommer.OnTouch(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN -> {
                if (zommer.readyToDraw()) {
                    ((FreeCropActivity) mcontext).EraseSize.setProgress((int) ERASER_STROKE_WIDTH);

                    TempraryBrushPaint.setStrokeWidth(ERASER_STROKE_WIDTH / zommer.getScaleX());
                    RemainingBrushPaint.setStrokeWidth(ERASER_STROKE_WIDTH / zommer.getBmScaleX());

                    EraseBrushPaint.setStrokeWidth(ERASER_STROKE_WIDTH / zommer.getBmScaleX());
                    CropBrushPaint.setStrokeWidth(ERASER_STROKE_WIDTH / zommer.getBmScaleX());

                    mxAndyFixer.FixXYUsingMatrix(event.getX(), event.getY(), zommer.getCanvasMatrix());
                    ActionDown(mxAndyFixer.getFixedXYUsingMartix().x, mxAndyFixer.getFixedXYUsingMartix().y);
                }
                ((FreeCropActivity) mcontext).ShowSetting(false);
                return true;
            }
            case MotionEvent.ACTION_MOVE -> {
                mxAndyFixer.FixXYUsingMatrix(event.getX(), event.getY(), zommer.getCanvasMatrix());
                mTouchePoint.add(event.getX());
                if (mTouchePoint.size() > 2) {
                    ActionMove(mxAndyFixer.getFixedXYUsingMartix().x, mxAndyFixer.getFixedXYUsingMartix().y);
                    invalidate();
                }
                return true;
            }
            case MotionEvent.ACTION_UP -> {
                //ERASER_STROKE_WIDTH = 50f;
                mxAndyFixer.FixXYUsingMatrix(event.getX(), event.getY(), zommer.getCanvasMatrix());
                ActionUp(mxAndyFixer.getFixedXYUsingMartix().x, mxAndyFixer.getFixedXYUsingMartix().y);
                TempraryBrushPath.reset();
                RemainingBrushPath.reset();
                RealEraseBrushPath.reset();
                RealCropBrushPath.reset();
                invalidate();
                return true;
            }
        }
        return false;
    }


    /**
     * Case the user touch screen "draw the Path Fk"
     * @param x For set The X position of the Touche.
     * @param y For set The Y position of the Touche.
     */
    private void ActionDown(float x, float y) {
        mTouchePoint = new ArrayList<>();
        X_YFixer xAndyFixer = new X_YFixer();
        TempraryBrushPath.moveTo(x,y);

        xAndyFixer.FixXYUsingMatrix(x,y,zommer.getDefaultMatrix());

        RemainingBrushPath.moveTo(xAndyFixer.getFixedXYUsingMartix().x, xAndyFixer.getFixedXYUsingMartix().y);
        RealEraseBrushPath.moveTo(xAndyFixer.getFixedXYUsingMartix().x, xAndyFixer.getFixedXYUsingMartix().y);
        RealCropBrushPath.moveTo(xAndyFixer.getFixedXYUsingMartix().x, xAndyFixer.getFixedXYUsingMartix().y);
    }

    /**
     * Case the user Move the Finger in the screen "draw the Path Fk"
     * @param x For set The X position of the Touche.
     * @param y For set The Y position of the Touche.
     */
    private void ActionMove(float x, float y) {
        X_YFixer xAndyFixer = new X_YFixer();
        if (zommer.readyToDraw()){

            if (hardness ==0){
                TempraryBrushPaint.setMaskFilter(null);
                RemainingBrushPaint.setMaskFilter(null);
                EraseBrushPaint.setMaskFilter(null);
                CropBrushPaint.setMaskFilter(null);
            }else {
                TempraryBrushPaint.setMaskFilter(new BlurMaskFilter(hardness/zommer.getScaleX(), BlurMaskFilter.Blur.NORMAL));
                RemainingBrushPaint.setMaskFilter(new BlurMaskFilter(hardness/zommer.getBmScaleX(), BlurMaskFilter.Blur.NORMAL));
                EraseBrushPaint.setMaskFilter(new BlurMaskFilter(hardness/zommer.getBmScaleX(), BlurMaskFilter.Blur.NORMAL));
                CropBrushPaint.setMaskFilter(new BlurMaskFilter(hardness/zommer.getBmScaleX(), BlurMaskFilter.Blur.NORMAL));
            }

            xAndyFixer.FixXYUsingMatrix(x,y,zommer.getDefaultMatrix());
            RealEraseBrushPath.lineTo(xAndyFixer.getFixedXYUsingMartix().x, xAndyFixer.getFixedXYUsingMartix().y);
            RealCropBrushPath.lineTo(xAndyFixer.getFixedXYUsingMartix().x, xAndyFixer.getFixedXYUsingMartix().y);
            TempraryBrushPath.lineTo(x,y);
            RemainingBrushPath.lineTo(xAndyFixer.getFixedXYUsingMartix().x, xAndyFixer.getFixedXYUsingMartix().y);

        }
    }

    /**
     * Case the user Stop Touching the screen "draw the Path Fk"
     * @param x For set The X position of the Touche.
     * @param y For set The y position of the Touche.
     */
    private void ActionUp(float x, float y) {
        X_YFixer xAndyFixer = new X_YFixer();
        if (eraser){
            RemainingBrushPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            RemainingBrushPaint.setAlpha(mOpacity);
            DrawPath(TraceCanvasInCropModeCanvas);
            RemainingBrushPaint.setXfermode(null);
        }else{
            RemainingBrushPaint.setXfermode(null);
            RemainingBrushPaint.setAlpha(mOpacity);
            DrawPath(TraceCanvasInCropModeCanvas);
        }

        if (eraser){

            ////the Erase Case
            //drawing the src path im which where we're going to draw the Bitmap on
            EraseBrushPaint.setXfermode(null);
            EraseBrushPaint.setAlpha(mOpacity);
            FixCanvasForTheErasedBitmap.drawPath(RealEraseBrushPath, EraseBrushPaint);

            //drawing the bitmap(dst) on the the path src which we drawn in the first
            EraseBrushPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            EraseBrushPaint.setAlpha(255);
            FixCanvasForTheErasedBitmap.drawBitmap(OriginalBitmap,0,0, EraseBrushPaint);

            //drawing the fixed bitmap on the final Erased Bitmap
            Paint paint = new Paint();
            paint.setFilterBitmap(true);
            paint.setAntiAlias(true);
            ErasedbitmapCanvas.drawBitmap(FixBitmapForTheErasedBitmap, 0, 0, paint);

            ////the crop Case

            CropBrushPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            CropBrushPaint.setAlpha(mOpacity);
            CroppedBitmapCanvas.drawPath(RealCropBrushPath, CropBrushPaint);
            EraseBrushPaint.setXfermode(null);
        }else {
            //the Erase Case
            EraseBrushPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            EraseBrushPaint.setAlpha(mOpacity);
            ErasedbitmapCanvas.drawPath(RealEraseBrushPath, EraseBrushPaint);
            EraseBrushPaint.setXfermode(null);

            //the crop Case
            //drawing the src path im which where we're going to draw the Bitmap on
            CropBrushPaint.setXfermode(null);
            CropBrushPaint.setAlpha(mOpacity);
            CroppedBitmapCanvas.drawPath(RealCropBrushPath, CropBrushPaint);

            //drawing the bitmap(dst) on the the path src which we drawn in the first
            CropBrushPaint.setAlpha(255);
            CropBrushPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            CroppedBitmapCanvas.drawBitmap(OriginalCropBitmap,0,0, CropBrushPaint);
        }

        //

        TempraryBrushPath.lineTo(x,y);
        RemainingBrushPath.lineTo(xAndyFixer.getFixedXYUsingMartix().x, xAndyFixer.getFixedXYUsingMartix().y);
        RealEraseBrushPath.lineTo(xAndyFixer.getFixedXYUsingMartix().x, xAndyFixer.getFixedXYUsingMartix().y);
        RealCropBrushPath.lineTo(xAndyFixer.getFixedXYUsingMartix().x, xAndyFixer.getFixedXYUsingMartix().y);

        FixBitmapForTheErasedBitmap = Bitmap.createBitmap(OriginalBitmap.getWidth(), OriginalBitmap.getHeight(), OriginalBitmap.getConfig());
        FixCanvasForTheErasedBitmap = new Canvas(FixBitmapForTheErasedBitmap);
        CropBrushPaint.setXfermode(null);
    }


    /**
     * Set Type Of The Path.
     * @param pathType Type of Crop
     */
    public void setPathType(PathType pathType){
        mPathType = pathType;
        invalidate();
    }


    /**
     * Set Type Of The Path.
     * @return mPathType
     */
    public PathType getPathType(){
        return mPathType;
    }
    /**
     * the Size of Eraser strokeWidth
     * @param strokeWidth Stroke size
     */
    public void SetEraserStrokeWidth(float strokeWidth){
        ERASER_STROKE_WIDTH = strokeWidth;
    }

    public void setOpacity(int opacity) {
        mOpacity = opacity;
    }

    public void setHardness(int Hardness) {
            hardness = Hardness;
    }

    public void setEraser(boolean eraser) {
        this.eraser = eraser;
        if (eraser){
            TempraryBrushPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }else {
            TempraryBrushPaint.setXfermode(null);
        }
        invalidate();
    }

    /**
     Draw the Selected path.
     **/
    private void DrawPath(Canvas canvas) {

        canvas.drawPath(RemainingBrushPath, RemainingBrushPaint);

    }

    /**
     Draw the Selected path.
     **/
    private void CDrawPath(Canvas canvas) {
        canvas.drawPath(TempraryBrushPath, TempraryBrushPaint);

    }
    /**Get the original bitmap object.
     *
     * @return
     */
    private Bitmap getErasedBitmap() {

        return this.Erasedbitmap;

    }

    /**
     * The final bitmap Of The Cropped Bitmap Area Using Path.
     * For More info Visit : https://stackoverflow.com/a/12089127.
     * @return The Final Free Cropped Image.
     */
    public Bitmap getCroppedImage(){

        return CroppedBitmap;

    }


    /**
     * Get the Cropped Bitmap Depending on the chose of user (Or DeV)
     * @return The Cropped Image
     */
    public Bitmap getCroppedFreeImage(){
        if (mPathType == PathType.ERASER){
            return getErasedBitmap();
        }else if (mPathType == PathType.BRUSH){
            return getCroppedImage();
        }else {
            return null;
        }
    }

//    /**
//     * Gets the bounding rectangle of the bitmap within the ImageView.
//     */
//    private RectF getBitmapRect() {
//
//        final Drawable drawable = ();
//        if (drawable == null) {
//            return new RectF();
//        }
//
//        // Get image matrix values and place them in an array.
//        final float[] matrixValues = new float[9];
//        getImageMatrix().getValues(matrixValues);
//
//        // Extract the scale and translation values from the matrix.
//        final float scaleX = matrixValues[Matrix.MSCALE_X];
//        final float scaleY = matrixValues[Matrix.MSCALE_Y];
//        final float transX = matrixValues[Matrix.MTRANS_X];
//        final float transY = matrixValues[Matrix.MTRANS_Y];
//
//        // Get the width and height of the original bitmap.
//        final int drawableIntrinsicWidth = drawable.getIntrinsicWidth();
//        final int drawableIntrinsicHeight = drawable.getIntrinsicHeight();
//
//        // Calculate the dimensions as seen on screen.
//        final int drawableDisplayWidth = Math.round(drawableIntrinsicWidth * scaleX);
//        final int drawableDisplayHeight = Math.round(drawableIntrinsicHeight * scaleY);
//
//        // Get the Rect of the displayed image within the ImageView.
//        final float left = Math.max(transX, 0);
//        final float top = Math.max(transY, 0);
//        final float right = Math.min(left + drawableDisplayWidth, getWidth());
//        final float bottom = Math.min(top + drawableDisplayHeight, getHeight());
//        return new RectF(left, top, right, bottom);
//    }


    /**
     * if The Bitmap is Valid for editing and cropping
     * @param H bitmap hieght
     * @param W bitmap width
     */
    private boolean BitmapValidity(int H,int W,Context context){
        if (H<=1&W<=1){
            Log.d(TAG, "getCroppedImage: Bitmap is Small");
            Toast.makeText(context, "OopsThe image height and Width is small", Toast.LENGTH_SHORT).show();
            return true ;
        }else if (H<=1){
            Log.d(TAG, "getCroppedImage: Bitmap is Small");
            Toast.makeText(context, "Oops,The image height is small", Toast.LENGTH_SHORT).show();
            return true;
        }else if (W<=1){
            Log.d(TAG, "getCroppedImage: Bitmap is Small");
            Toast.makeText(context, "Oops,The image Width is small", Toast.LENGTH_SHORT).show();
            return true;
        }else {
            return false;
        }
    }

}
