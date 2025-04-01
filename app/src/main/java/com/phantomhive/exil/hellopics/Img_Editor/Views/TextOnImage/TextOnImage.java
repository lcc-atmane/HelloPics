package com.phantomhive.exil.hellopics.Img_Editor.Views.TextOnImage;

import static com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards.createCheckerboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.phantomhive.exil.hellopics.Img_Editor.EditorActivities.ViewsActivities.TextOnImageActivity;
import com.phantomhive.exil.hellopics.Img_Editor.Views.TextOnImage.TextHandel.Text;
import com.phantomhive.exil.hellopics.Img_Editor.Views.TextOnImage.TextHandel.TextMatrix;
import com.phantomhive.exil.hellopics.Img_Editor.Views.TextOnImage.TextHandel.textPaint;
import com.phantomhive.exil.hellopics.Img_Editor.Views.TextOnImage.TextHandel.TextPosition;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.CheckerBoards;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ColorDetector;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.ViewZommer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.X_YFixer;
import com.phantomhive.exil.hellopics.Img_Editor.Views.ViewsGlobaleTools.Zommer;
import com.phantomhive.exil.hellopics.R;


public class TextOnImage extends View {

    private static final float ERASER_STROKE_WIDTH = 30f;
    String TAG = "TextOnImage";
    Text mText;
    TextPosition mTextPosition;
    textPaint mTextPaint;
    TextMatrix mTextMatrix;

    private float DownX;
    private float DownY;

    float rightCopy, bottomCopy;
    RectF RectDst;

    private float X;
    private float Y;
    private boolean ShowCadre = true;

    Bitmap Rt,Rz,Cs,Cp;

    //Text Properties
    float TextSize = 150;
    private Context mContext;
    ClickCircles mClickCircles;

    boolean mRotate;
    boolean mScale;
    float mScaleSource;
    float scale = 1;

    Bitmap mBitmap;

    ViewZommer zommer;
    X_YFixer mxAndyFixer;
    boolean eraser = false;
    Paint mErasePaint;
    Path mErasePath,mRealErasePath;

    Matrix TextImgBackgroudMatrix;
    Bitmap TextImgBackground;

    Matrix RectImgBackgroundMatrix;
    Bitmap RectImgBackground;

    boolean mtextStaticwidth;
    Integer Color1 = Color.parseColor("#A6A061"),Color2 = Color.parseColor("#A6A061");

    Integer mColor;
    boolean ColorLoc;
    String ColorLocType;
    Integer GradRainbowPosition;
    Paint CircleLPaint;

    Paint Checkerboardpaint;
    Bitmap createCheckerboard;


    private ToDoWhenItIsOnTouchActionListener mListener;
    public TextOnImage(Context context) {
        super(context);
        init(context,null);
    }

    public TextOnImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public TextOnImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public TextOnImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attributeSet){
        mContext = context;
        Rt = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rotate_ic);
        Rz = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.resize_ic);
        Cs = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.closeimg_ic);
        Cp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.copycopy_ic);

        mText = new Text();
        mTextPosition = new TextPosition();
        mTextPaint = new textPaint();
        mTextMatrix = new TextMatrix();

        zommer = new ViewZommer(this,context);
        mxAndyFixer = new X_YFixer();

        mErasePath = new Path();
        mRealErasePath  = new Path();

        mErasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //mErasePaint.setColor(Color.TRANSPARENT);
        mErasePaint.setAntiAlias(true);
        mErasePaint.setStrokeWidth(ERASER_STROKE_WIDTH);
        mErasePaint.setStyle(Paint.Style.STROKE);
        mErasePaint.setStrokeJoin(Paint.Join.ROUND);
        mErasePaint.setStrokeCap(Paint.Cap.ROUND);

        CircleLPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TextImgBackgroudMatrix = new Matrix();
        RectImgBackgroundMatrix = new Matrix();

        Checkerboardpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Checkerboardpaint.setAntiAlias(true);
        Checkerboardpaint.setFilterBitmap(true);
        Checkerboardpaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setMatrix(zommer.getCanvasMatrix());
        if (createCheckerboard != null){
            canvas.drawBitmap(createCheckerboard,zommer.DefaultXY().x,zommer.DefaultXY().y,null);
        }

        canvas.drawBitmap(mBitmap,zommer.getDefaultMatrix(),Checkerboardpaint);
        if (!mText.getTextList().isEmpty()){
            for (int i= 0; i < mText.TextListSize(); i++){
                canvas.save();
                if (mText.getTextBend().get(i)==0){
                    DrawTextsWithoutBend(canvas,i);
                }else {
                    DrawTextsWithBend(canvas,i);
                }
                canvas.restore();
            }
        }
        DrawBorders(canvas);
        if (ColorLoc){
            canvas.drawCircle(mxAndyFixer.getFixedXYUsingMartix().x, mxAndyFixer.getFixedXYUsingMartix().y-(200/zommer.getScaleX()),
                    50/zommer.getScaleX(),CircleLPaint);
        }
    }

    private void DrawTextsWithBend(Canvas canvas,int i) {
        int centerX;
        int centerY;

        float TextWidth_bend = mTextPaint.getTextPaintList().get(i).measureText(mText.getTextList().get(i));

        Paint.FontMetrics F = mTextPaint.getTextPaintList().get(i).getFontMetrics();
        float BaseLine = F.bottom;
        float Ascent = F.top;
        Rect bounds = new Rect();
        mTextPaint.getTextPaintList().get(i).getTextBounds(mText.getTextList().get(i),
                0, mText.getTextList().get(i).length(), bounds);

        RectF RectDt;
        float right = mTextPosition.TextPositionList().get(i).x +
                mTextPaint.getTextPaintList().get(i).measureText(mText.getTextList().get(i))/2;

        float bottom = mTextPosition.TextPositionList().get(i).y
                + BaseLine ;

        RectDt = new RectF(mTextPosition.TextPositionList().get(i).x
                - mTextPaint.getTextPaintList().get(i).measureText(mText.getTextList().get(i))/2,
                (mTextPosition.TextPositionList().get(i).y + Ascent),
                (right),bottom);

        canvas.translate(mTextPosition.TextPositionList().get(i).x,mTextPosition.TextPositionList().get(i).y);
        canvas.rotate(mTextMatrix.getRotateValueList().get(i),(RectDt.centerX())-(mTextPosition.TextPositionList().get(i).x),
                (RectDt.centerY())-(mTextPosition.TextPositionList().get(i).y));


        mText.MovePath(i,0 - TextWidth_bend,0, 0+TextWidth_bend,mText.getTextBend().get(i));

                    /*
                    float imgRight2 = 0 + TextWidth_bend/2;
                    float imgBottom2 = 0f + BaseLine;

                    RectF imgRec2 = new RectF((0f-TextWidth_bend/2f)-20,
                            (0f + Ascent)-20,
                            imgRight2+20,imgBottom2+20);

                    mText.MovePath2(i,0 - TextWidth_bend,0,imgRec2 ,mText.getTextBend().get(i));
                     */


        if (mTextPaint.getTextimgsList().get(i)!=null){

            TextImgBackground = getScaledBitmap(mTextPaint.getTextimgsList().get(i),RectDt.width(),RectDt.height());
            centerX = (TextImgBackground.getWidth()/2);
            centerY =(TextImgBackground.getHeight()/2);


            TextImgBackgroudMatrix.reset();
            TextImgBackgroudMatrix.setTranslate(((0f)-centerX),(((BaseLine+Ascent)*0.5f)-centerY));

            BitmapShader shader = new BitmapShader(TextImgBackground, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            mTextPaint.setTextImg(i,shader, TextImgBackgroudMatrix);

        }

        if (mTextPaint.getTextPaintList().get(i).getShader()!=null&&
                mTextPaint.getTextimgsList().get(i)==null){

            float imgRight = 0f + TextWidth_bend/2;
            float imgBottom = 0f + bounds.bottom;

            RectF imgRect = new RectF(0f-TextWidth_bend/2,
                    0f - Ascent,
                    imgRight,imgBottom);

            LinearGradient  linearGradient = new LinearGradient( imgRect.left, imgRect.top,
                    imgRect.right, imgRect.bottom
                    ,mTextPaint.getGradientsTextPaintList().get(i).getColors(),
                    null,
                    Shader.TileMode.CLAMP);

            mTextPaint.setTextGradient(i,linearGradient,mTextPaint.getGradientsTextPaintList().get(i).getAngle(),
                    imgRect.width()/2f, imgRect.height()/2f);
        }

        float imgRight = 0 + TextWidth_bend/2;
        float imgBottom = 0f + BaseLine;

        RectF imgRect = new RectF((0f-TextWidth_bend/2f)-mTextPaint.getTextRectPaintList().get(i).getLeft(),
                (0f + Ascent)-mTextPaint.getTextRectPaintList().get(i).getTop(),
                imgRight+mTextPaint.getTextRectPaintList().get(i).getRight(),imgBottom+mTextPaint.getTextRectPaintList().get(i).getBottom());


        if (mTextPaint.getTextRectImgs().get(i)!=null){
            float imgRightI = mTextPosition.TextPositionList().get(i).x  + TextWidth_bend/2;
            float imgBottomI = mTextPosition.TextPositionList().get(i).y+ BaseLine;

            RectF imgRectI = new RectF((mTextPosition.TextPositionList().get(i).x-TextWidth_bend/2f)-mTextPaint.getTextRectPaintList().get(i).getLeft(),
                    (mTextPosition.TextPositionList().get(i).y + Ascent)-mTextPaint.getTextRectPaintList().get(i).getTop(),
                    imgRightI+mTextPaint.getTextRectPaintList().get(i).getRight(),
                    imgBottomI+mTextPaint.getTextRectPaintList().get(i).getBottom());


            RectImgBackground = getScaledBitmap(mTextPaint.getTextRectImgs().get(i), imgRectI.width(),imgRectI.height());

            int centerXI = (RectImgBackground.getWidth());
            int centerYI =( RectImgBackground.getHeight());

            RectImgBackgroundMatrix.reset();
            RectImgBackgroundMatrix.setTranslate(((0f)-centerXI)*0.5f,(imgRectI.height()-centerYI)*0.5f);

            BitmapShader shader = new BitmapShader(RectImgBackground, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            mTextPaint.setTextRectShader(i,shader,RectImgBackgroundMatrix);
        }
        canvas.drawRoundRect(imgRect,mTextPaint.getTextRectPaintList().get(i).getX(),
                mTextPaint.getTextRectPaintList().get(i).getY(),mTextPaint.getTextRectPaintList().get(i).getRectPaint());

        if (mTextPaint.getBorderTextPaintList().get(i).getStrokeWidth()>0){
            canvas.drawTextOnPath(mText.getTextList().get(i),
                    mText.getTextPath().get(i), 0, 0, mTextPaint.getBorderTextPaintList().get(i));
        }


        canvas.drawTextOnPath(mText.getTextList().get(i), mText.getTextPath().get(i),
                0, 0, mTextPaint.getTextPaintList().get(i));



        /*
        Paint paint = new Paint (Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#A6A061"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap( Paint.Cap.ROUND);
        paint.setStrokeWidth(5f/zommer.getScaleX());
        canvas.drawPath(mText.getTextPath().get(i),paint);

// Increase height based on bend amount float extraHeight = Math.abs(bend) * 0.5f; // Adjust factor as needed

         Paint PaintOfRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        PaintOfRect.setColor(Color.parseColor("#A6A061"));
        PaintOfRect.setStyle(Paint.Style.STROKE);
        PaintOfRect.setStrokeWidth(2/zommer.getScaleX());
        // After moving the path, get its bounds
        RectF pathBounds = new RectF();
        mText.getTextPath().get(i).computeBounds(pathBounds, true);
        // Adjust the bounds to account for the text's baseline
        pathBounds.bottom += BaseLine;
        // Add some padding to the bounds if needed
        float padding = 10; // Adjust this value as needed
        pathBounds.left -= padding;
        pathBounds.top -= padding;
        pathBounds.right += padding;
        pathBounds.bottom += padding;

        RectF Rdect = new RectF(pathBounds);
        canvas.drawRect(Rdect,PaintOfRect);
         */

        rightCopy = 0f+ (TextWidth_bend/2);
        bottomCopy =0f + BaseLine;
        RectDst = new RectF(0 - (TextWidth_bend/2),
                (0f + Ascent),
                (rightCopy),bottomCopy);
    }

    private void DrawTextsWithoutBend(Canvas canvas,int i) {

        int centerX = 0;
        int centerY = 0;

        Rect bounds = new Rect();
        mTextPaint.getTextPaintList().get(i).getTextBounds(mText.getTextList().get(i),
                    0, mText.getTextList().get(i).length(), bounds);



        // Split the text into lines using the newline character "\n"
        String[] lines = mText.getTextList().get(i).split("\n");

        float TextWidth_static = mTextPaint.getTextPaintList().get(i).measureText(lines[0]);
        if (TextWidth_static<0){
            for (String number : lines) {
                if ( mTextPaint.getTextPaintList().get(i).measureText(number) < TextWidth_static) {
                    TextWidth_static =  mTextPaint.getTextPaintList().get(i).measureText(number);
                }
            }
        }else {
            for (String number : lines) {
                if ( mTextPaint.getTextPaintList().get(i).measureText(number) > TextWidth_static) {
                    TextWidth_static =  mTextPaint.getTextPaintList().get(i).measureText(number);
                }
            }
        }


        StaticLayout staticLayout = new StaticLayout(mText.getTextList().get(i),mTextPaint.getTextPaintList().get(i),
                (int)Math. abs(TextWidth_static),
                Layout.Alignment.ALIGN_NORMAL,mText.getTextStatic().get(i).getLineSpace(), 0.0f, false);

        StaticLayout staticLayoutBorder = new StaticLayout(mText.getTextList().get(i),
                mTextPaint.getBorderTextPaintList().get(i),(int)Math. abs(TextWidth_static),
                Layout.Alignment.ALIGN_NORMAL,mText.getTextStatic().get(i).getLineSpace(), 0.0f, false);

        float right = mTextPosition.TextPositionList().get(i).x +
                mTextPaint.getTextPaintList().get(i).measureText(mText.getTextList().get(i))/2;

        float bottom = mTextPosition.TextPositionList().get(i).y
                + mText.getTextStatic().get(i).getTextStatic().getHeight() ;

        RectF  RectDt = new RectF(mTextPosition.TextPositionList().get(i).x
                - mTextPaint.getTextPaintList().get(i).measureText(mText.getTextList().get(i))/2,
                (mTextPosition.TextPositionList().get(i).y + mText.getTextStatic().get(i).getTextStatic().getTopPadding() ),
                (right),bottom);

        canvas.translate(mTextPosition.TextPositionList().get(i).x,mTextPosition.TextPositionList().get(i).y);
        canvas.rotate(mTextMatrix.getRotateValueList().get(i),(RectDt.centerX())-(mTextPosition.TextPositionList().get(i).x),
                (RectDt.centerY())-(mTextPosition.TextPositionList().get(i).y));

        if (mTextPaint.getTextimgsList().get(i)!=null){

            TextImgBackground = getScaledBitmap(mTextPaint.getTextimgsList().get(i),RectDt.width(),RectDt.height());
            centerX = (TextImgBackground.getWidth());
            centerY =(TextImgBackground.getHeight());

            TextImgBackgroudMatrix.reset();
            TextImgBackgroudMatrix.setTranslate(((0f)-centerX)*0.5f,(RectDt.height()-centerY)*0.5f);

            BitmapShader shader = new BitmapShader(TextImgBackground, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            mTextPaint.setTextImg(i,shader, TextImgBackgroudMatrix);

        }

        if (mTextPaint.getTextPaintList().get(i).getShader()!=null&&mTextPaint.getTextimgsList().get(i)==null){

            float imgRight = mTextPosition.TextPositionList().get(i).x + TextWidth_static/2;
            float imgBottom = mTextPosition.TextPositionList().get(i).y + bounds.bottom;

            RectF imgRect = new RectF(mTextPosition.TextPositionList().get(i).x-TextWidth_static/2,
                    mTextPosition.TextPositionList().get(i).y - bounds.height()+ bounds.bottom,
                    imgRight,imgBottom);

            LinearGradient linearGradient = new LinearGradient(((0f)-imgRect.width())*0.5f,
                    (RectDt.height()-imgRect.height())*0.5f,TextWidth_static/2, 0
                    ,mTextPaint.getGradientsTextPaintList().get(i).getColors(),
                    null,
                    Shader.TileMode.CLAMP);

            mTextPaint.setTextGradient(i,linearGradient,mTextPaint.getGradientsTextPaintList().get(i).getAngle(), imgRect.width()/2f, imgRect.height()/2f);
        }


        mText.UpdateTextStaticLayout(i,staticLayout,staticLayoutBorder);

        rightCopy = 0f + TextWidth_static/2;
        bottomCopy = 0f + staticLayout.getHeight() ;

        RectDst = new RectF(0f - TextWidth_static/2,
                (0f + staticLayout.getTopPadding() ),
                (rightCopy),bottomCopy);


        float imgRight = 0 + TextWidth_static/2;
        float imgBottom = 0f + mText.getTextStatic().get(i).getTextStatic().getHeight();

        RectF imgRect = new RectF((0f-TextWidth_static/2f)-mTextPaint.getTextRectPaintList().get(i).getLeft(),
                0f-mTextPaint.getTextRectPaintList().get(i).getTop(),
                imgRight+mTextPaint.getTextRectPaintList().get(i).getRight(),
                imgBottom+mTextPaint.getTextRectPaintList().get(i).getBottom());

        if (mTextPaint.getTextRectImgs().get(i)!=null){
            float imgRightI = mTextPosition.TextPositionList().get(i).x + TextWidth_static/2;
            float imgBottomI = mTextPosition.TextPositionList().get(i).y + mText.getTextStatic().get(i).getTextStatic().getHeight();

            RectF imgRectI = new RectF((mTextPosition.TextPositionList().get(i).x-TextWidth_static/2f)-mTextPaint.getTextRectPaintList().get(i).getLeft(),
                    mTextPosition.TextPositionList().get(i).y-mTextPaint.getTextRectPaintList().get(i).getTop(),
                    imgRightI+mTextPaint.getTextRectPaintList().get(i).getRight(),
                    imgBottomI+mTextPaint.getTextRectPaintList().get(i).getBottom());

            RectImgBackground = getScaledBitmap(mTextPaint.getTextRectImgs().get(i), imgRectI.width(),imgRectI.height());

            int centerXI = (RectImgBackground.getWidth());
            int centerYI =( RectImgBackground.getHeight());

            RectImgBackgroundMatrix.reset();
            RectImgBackgroundMatrix.setTranslate(((0f)-centerXI)*0.5f,(imgRectI.height()-centerYI)*0.5f);

            BitmapShader shader = new BitmapShader(RectImgBackground, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            mTextPaint.setTextRectShader(i,shader,RectImgBackgroundMatrix);
        }




        canvas.drawRoundRect(imgRect,mTextPaint.getTextRectPaintList().get(i).getX(),
                mTextPaint.getTextRectPaintList().get(i).getY(),
                mTextPaint.getTextRectPaintList().get(i).getRectPaint());

        /*
        if (RectImgBackground!=null){
            canvas.drawBitmap(RectImgBackground,RectImgBackgroundMatrix,null);

        }
         */

        if (mTextPaint.getBorderTextPaintList().get(i).getStrokeWidth()>0){
            mText.getTextStatic().get(i).getTextBordersStatic().draw(canvas);
        }
        mText.getTextStatic().get(i).getTextStatic().draw(canvas);
    }

    private void DrawBorders(Canvas canvas) {
        if (mText.getTextList().size() != 0 ){
            Paint PaintOfRect = new Paint(Paint.ANTI_ALIAS_FLAG);
            PaintOfRect.setColor(Color.parseColor("#A6A061"));
            PaintOfRect.setStyle(Paint.Style.STROKE);
            PaintOfRect.setStrokeWidth(2/zommer.getScaleX());

            Paint PaintOfCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
            PaintOfCircle.setColor(Color.parseColor("#A6A061"));


            Paint paint = new Paint (Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.parseColor("#A6A061"));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap( Paint.Cap.ROUND);
            paint.setStrokeWidth(5f/zommer.getScaleX());

            float w = mTextPaint.TextPaint().measureText(mText.Text())/2;


            Paint.FontMetrics FF = mTextPaint.TextPaint().getFontMetrics();
            float BaseLine2 = FF.bottom;
            float Ascent2 = FF.top;

            // Split the text into lines using the newline character "\n"
            String[] lines = mText.Text().split("\n");

            float maxWidth = mTextPaint.TextPaint().measureText(lines[0]);
            if(maxWidth<0){
                for (String number : lines) {
                    if (mTextPaint.TextPaint().measureText(number) < maxWidth) {
                        maxWidth = mTextPaint.TextPaint().measureText(number);
                    }
                }
            }else{
                for (String number : lines) {
                    if (mTextPaint.TextPaint().measureText(number) > maxWidth) {
                        maxWidth = mTextPaint.TextPaint().measureText(number);
                    }
                }
            }




            if(getBend()==0){

                rightCopy = mTextPosition.TextPosition().x + maxWidth/2;
                bottomCopy = mTextPosition.TextPosition().y + mText.getStatic().getTextStatic().getHeight() ;

                RectDst = new RectF(mTextPosition.TextPosition().x - maxWidth/2,
                        (mTextPosition.TextPosition().y + mText.getStatic().getTextStatic().getTopPadding() ),
                        (rightCopy),bottomCopy);

            }else {
                rightCopy = mTextPosition.TextPosition().x + w;
                bottomCopy = mTextPosition.TextPosition().y + BaseLine2 ;
                RectDst = new RectF(mTextPosition.TextPosition().x - w,
                        (mTextPosition.TextPosition().y + Ascent2),
                        (rightCopy),bottomCopy);
            }



            if (ShowCadre){
                //canvas.drawPath(mText.TextPath(), paint);

                // cancel Past.
                Bitmap sc = Bitmap.createScaledBitmap(Cs,(int) ((25/zommer.getScaleX())*2),(int) ((25/zommer.getScaleX())*2),true);
                canvas.drawCircle(RectDst.left-(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX()),25/zommer.getScaleX(),PaintOfCircle);
                canvas.drawBitmap(sc,(RectDst.left-(25/zommer.getScaleX()))-(float)sc.getWidth()/2,
                        (RectDst.top-(25/zommer.getScaleX()))-(float)sc.getHeight()/2,null);

                // Rotate Past.
                Bitmap tr = Bitmap.createScaledBitmap(Rt, (int) ((25/zommer.getScaleX())*2),(int) ((25/zommer.getScaleX())*2),true);
                canvas.drawCircle(RectDst.right+(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX()),25/zommer.getScaleX(),PaintOfCircle);
                canvas.drawBitmap(tr,(RectDst.right+(25/zommer.getScaleX()))-(float)tr.getWidth()/2,
                        (RectDst.top-(25/zommer.getScaleX()))-(float)tr.getHeight()/2,null);

                // Scale Past.
                Bitmap zr = Bitmap.createScaledBitmap(Rz,(int) ((25/zommer.getScaleX())*2),(int) ((25/zommer.getScaleX())*2),true);
                canvas.drawCircle(RectDst.right+(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX()),25/zommer.getScaleX(),PaintOfCircle);
                canvas.drawBitmap(zr,(RectDst.right+(25/zommer.getScaleX()))-(float)zr.getWidth()/2,
                        (RectDst.bottom+(25/zommer.getScaleX()))-(float)zr.getHeight()/2,null);

                // Copy the Copy.
                Bitmap pC = Bitmap.createScaledBitmap(Cp,(int) ((25/zommer.getScaleX())*2),(int) ((25/zommer.getScaleX())*2),true);
                canvas.drawCircle(RectDst.left-(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX()),25/zommer.getScaleX(),PaintOfCircle);
                canvas.drawBitmap(pC,(RectDst.left-(25/zommer.getScaleX()))-(float)pC.getWidth()/2,
                        (RectDst.bottom+(25/zommer.getScaleX()))-(float)pC.getHeight()/2,null);

                if(getBend()==0){
                    canvas.drawCircle((RectDst.right + RectDst.right) / 2,(RectDst.top + RectDst.bottom) / 2,15/zommer.getScaleX(),PaintOfCircle);
                }
                canvas.drawRect(RectDst,PaintOfRect);
            }
        }

    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        zommer.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if (mBitmap.hasAlpha()){
            createCheckerboard= createCheckerboard((int) CheckerBoards.getBitmapRect(zommer.getDefaultMatrix(),mBitmap,
                            getWidth(),getHeight()).width(),

                    (int) CheckerBoards.getBitmapRect(zommer.getDefaultMatrix(),mBitmap,
                            getWidth(),getHeight()).height(),20);

        }
    }


    public void setImageBitmap(Bitmap bm) {

        mBitmap  = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight());
        zommer.setBitmap(bm);
    }

    public Bitmap getImageBitmap() {
        return Bitmap.createScaledBitmap(mBitmap,getWidth(),getHeight(),true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        zommer.OnTouch(event);
        gd.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mxAndyFixer.FixXYUsingMatrix(event.getX(),event.getY(),zommer.getCanvasMatrix());
                DownX = mxAndyFixer.getFixedXYUsingMartix().x;
                DownY = mxAndyFixer.getFixedXYUsingMartix().y;
                if (ColorLoc) {
                    mColor = ColorDetector.getColor((int) X_YFixer.getfixedXYUsingMartix(mxAndyFixer.getFixedXYUsingMartix().x,
                                    mxAndyFixer.getFixedXYUsingMartix().y,zommer.getDefaultMatrix()).x,

                            (int)  X_YFixer.getfixedXYUsingMartix(mxAndyFixer.getFixedXYUsingMartix().x,
                                    mxAndyFixer.getFixedXYUsingMartix().y,zommer.getDefaultMatrix()).y,mBitmap);
                    CircleLPaint.setColor(mColor);
                }else {
                    ActionDown(mxAndyFixer.getFixedXYUsingMartix().x, mxAndyFixer.getFixedXYUsingMartix().y);
                }

                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                mxAndyFixer.FixXYUsingMatrix(event.getX(),event.getY(),zommer.getCanvasMatrix());
                if (ColorLoc) {
                    mColor = ColorDetector.getColor((int) X_YFixer.getfixedXYUsingMartix(mxAndyFixer.getFixedXYUsingMartix().x,
                                    mxAndyFixer.getFixedXYUsingMartix().y,zommer.getDefaultMatrix()).x,

                            (int)  X_YFixer.getfixedXYUsingMartix(mxAndyFixer.getFixedXYUsingMartix().x,
                                    mxAndyFixer.getFixedXYUsingMartix().y,zommer.getDefaultMatrix()).y,mBitmap);
                    CircleLPaint.setColor(mColor);
                }else {
                    ActionMove(mxAndyFixer.getFixedXYUsingMartix().x, mxAndyFixer.getFixedXYUsingMartix().y);
                }

                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                mxAndyFixer.FixXYUsingMatrix(event.getX(),event.getY(),zommer.getCanvasMatrix());
                ActionUp(mxAndyFixer.getFixedXYUsingMartix().x, mxAndyFixer.getFixedXYUsingMartix().y);
                return true;
        }
        return false;
    }

    public void ActionDown(float x,float y){
        for (int i = 0; i < mText.TextListSize(); i++) {
            RectF rect;
            RectF rect1;

            float rightCopy ;
            float bottomCopy ;
            float leftCopy;
            float topCopy ;

            float w = mTextPaint.getTextPaintList().get(i).measureText(mText.getTextList().get(i))/2;

            Paint.FontMetrics F = mTextPaint.getTextPaintList().get(i).getFontMetrics();
            float BaseLine = F.bottom;
            float Ascent = F.top;

            if (mText.getTextBend().get(i)==0){

                // Split the text into lines using the newline character "\n"
                String[] lines2 = mText.getTextList().get(i).split("\n");

                float wAll2 = mTextPaint.getTextPaintList().get(i).measureText(lines2[0]);
                if (wAll2<0){
                    for (String number : lines2) {
                        if (mTextPaint.getTextPaintList().get(i).measureText(number) < wAll2) {
                            wAll2 = mTextPaint.getTextPaintList().get(i).measureText(number);
                        }
                    }
                }else {
                    for (String number : lines2) {
                        if (mTextPaint.getTextPaintList().get(i).measureText(number) > wAll2) {
                            wAll2 = mTextPaint.getTextPaintList().get(i).measureText(number);
                        }
                    }
                }


                rightCopy = mTextPosition.TextPositionList().get(i).x + wAll2/2;
                bottomCopy = mTextPosition.TextPositionList().get(i).y + mText.getTextStatic().get(i).getTextStatic().getHeight() ;
                leftCopy =mTextPosition.TextPositionList().get(i).x-wAll2/2;
                topCopy = mTextPosition.TextPositionList().get(i).y + mText.getTextStatic().get(i).getTextStatic().getTopPadding();
            }else {
                rightCopy = mTextPosition.TextPositionList().get(i).x +w;
                bottomCopy = mTextPosition.TextPositionList().get(i).y +BaseLine;
                leftCopy =mTextPosition.TextPositionList().get(i).x-w;
                topCopy = mTextPosition.TextPositionList().get(i).y+Ascent;

            }

            rect = new RectF(rightCopy,
                    topCopy,
                    leftCopy
                    , bottomCopy);

            rect1= new RectF(leftCopy,
                    topCopy,
                    rightCopy, bottomCopy);


            if(rect.contains(x,y) | rect1.contains(x,y)
                    &&!isClicked(x, y, RectDst.left-(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX()))
                    &&!isClicked(x, y, RectDst.right+(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX()))
                    &&!isClicked(x, y, RectDst.right+(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX()))
                    &&!isClicked(x, y, RectDst.left-(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX()))){

                mText.setIndex(i);
                mTextPosition.setIndex(i);
                mTextPaint.setIndex(i);
                mTextMatrix.setIndex(i);
                ((TextOnImageActivity)mContext).MargeAddOptions(VISIBLE);
                X = mTextPosition.TextPosition().x;
                Y =mTextPosition.TextPosition().y;

                if (eraser){
                    X_YFixer xAndyFixer = new X_YFixer();
                    xAndyFixer.FixXYUsingMatrix(x,y,zommer.getDefaultMatrix());
                    mErasePath.moveTo(x,y);
                    mText.MoveEraser(x, y);
                    mRealErasePath.moveTo(xAndyFixer.getFixedXYUsingMartix().x,xAndyFixer.getFixedXYUsingMartix().y);
                }else{
                    ShowCadre = true;
                }

                ((TextOnImageActivity) mContext).OpacitySeekBar.setProgress(getTextOpacity());
                ((TextOnImageActivity) mContext).TextSizeSeekBar.setProgress((int) getTextSize());
                if (getTextStoke()>0){
                    ((TextOnImageActivity) mContext).TextStrokeSeekBar.setProgress((int) getTextStoke());
                }

                if (getBorderTextStoke()>0){
                    ((TextOnImageActivity) mContext).TextBorderSeekBar.setProgress((int) getBorderTextStoke());
                }

                ((TextOnImageActivity) mContext).RectOnOF.setChecked(getHighlighter() > 0);

                ((TextOnImageActivity) mContext).opacitySeekb.setProgress(getHighlighter());
                ((TextOnImageActivity) mContext).cornerSeekb.setProgress((int) getHighlighterCorners());
                ((TextOnImageActivity) mContext).TextRotateSeekBar.setProgress((int) mTextMatrix.getRotationDegree());
                ((TextOnImageActivity) mContext).TextBendSeekBar.setProgress((Math.round(getBend()))*-1);
                ((TextOnImageActivity) mContext).TextScaleXSeekBar.setProgress( Math.round(getTextScaleX()*100));
                ((TextOnImageActivity) mContext).TextSkewXSeekBar.setProgress((Math.round(getTextSkewX()*100))*-1);
                ((TextOnImageActivity) mContext).TextLetterSpacingSeekBar.setProgress((Math.round(getLatterSpacing()*100)));
                ((TextOnImageActivity) mContext).TextLineSpacingSeekBar.setProgress((Math.round(getLineSpacing()*100)));


            }else {
                ShowCadre = false;
            }

        }
        if (mText.TextListSize()>0){
            mClickCircles.ClickTopLeft(isClicked(x, y, RectDst.left-(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX())),
                    this);
            // Case On touche For Rotate.
            mClickCircles.ClickTopRight(isClicked(x, y, RectDst.right+(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX())),
                    this);
            // Case On touche Copy Button To Copy The Copy
            mClickCircles.ClickBottomRight(isClicked(x, y, RectDst.right+(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX())),
                    this);

            mClickCircles.ClickBottomLeft(isClicked(x, y,RectDst.left-(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX())),
                    this);

            if (isClicked(x,y, RectDst.right+(25/zommer.getScaleX()), RectDst.top-(25/zommer.getScaleX()))){
                mRotate = true;
            }

            if (isClicked(x,y,RectDst.right+(25/zommer.getScaleX()), RectDst.bottom+(25/zommer.getScaleX()))){
                mScale = true;
                mScaleSource = x;
            }
           if(isClicked(x,y,(RectDst.right + RectDst.right) / 2,(RectDst.top + RectDst.bottom) / 2)){
               mtextStaticwidth = true;
            }

        }

    }

    public void ActionMove(float x,float y){
        if (eraser){
            X_YFixer xAndyFixer = new X_YFixer();
            xAndyFixer.FixXYUsingMatrix(x,y,zommer.getDefaultMatrix());
            mErasePath.lineTo(x,y);
            mText.LineEraser(x, y);
            mRealErasePath.lineTo(xAndyFixer.getFixedXYUsingMartix().x,xAndyFixer.getFixedXYUsingMartix().y);
        }else
        if (mText.TextListSize()>0){

            float w = mTextPaint.TextPaint().measureText(mText.Text())/2;

            Paint.FontMetrics F = mTextPaint.TextPaint().getFontMetrics();
            float BaseLine = F.bottom;
            float Ascent = F.top;

            RectF rect;
            RectF rect1;

            float rightCopy ;
            float bottomCopy ;
            float leftCopy;
            float topCopy ;

            if (mText.getBend()==0){
                // Split the text into lines using the newline character "\n"
                String[] lines = mText.Text().split("\n");

                float maxWidth = mTextPaint.TextPaint().measureText(lines[0]);
                if (maxWidth<0){
                    for (String number : lines) {
                        if (mTextPaint.TextPaint().measureText(number) < maxWidth) {
                            maxWidth = mTextPaint.TextPaint().measureText(number);
                        }
                    }
                }else {
                    for (String number : lines) {
                        if (mTextPaint.TextPaint().measureText(number) > maxWidth) {
                            maxWidth = mTextPaint.TextPaint().measureText(number);
                        }
                    }
                }

                rightCopy = mTextPosition.TextPosition().x + maxWidth/2;
                bottomCopy = mTextPosition.TextPosition().y + mText.getStatic().getTextStatic().getHeight() ;
                leftCopy =mTextPosition.TextPosition().x-maxWidth/2;
                topCopy = mTextPosition.TextPosition().y + mText.getStatic().getTextStatic().getTopPadding();
            }else {
                rightCopy = mTextPosition.TextPosition().x +w;
                bottomCopy = mTextPosition.TextPosition().y +BaseLine;
                leftCopy =mTextPosition.TextPosition().x-w;
                topCopy =mTextPosition.TextPosition().y+Ascent;
            }

            rect = new RectF(rightCopy,
                    topCopy,
                    leftCopy
                    , bottomCopy);

            rect1= new RectF(leftCopy,
                    topCopy,
                    rightCopy, bottomCopy);

            if (mScale){
                scale = getTextSize();
                if (x>mScaleSource){
                    scale += 1.5f;
                    setTextSize(scale);
                }else if (mScaleSource>x){
                    scale -= 1.5f;
                    setTextSize(scale);
                }
            }else if (mRotate){
                int rotationAngleDegrees = 0 ;
                double rotationAngleRadians = Math.atan2(x - (float) getWidth()/2, (float)getHeight()/2 - y);
                rotationAngleDegrees += (int) Math.toDegrees(rotationAngleRadians);
                mTextMatrix.setRotation(rotationAngleDegrees);
            }else {
                if(rect.contains(x,y) | rect1.contains(x,y)){
                    ShowCadre =true;
                    X += x - DownX;
                    Y += y - DownY;
                    mTextPosition.updatePosition(X,Y);
                    //mText.Eraser(0,0);

                    DownX = x;
                    DownY = y;
                }else{
                    ShowCadre = false;
                }

                if (mtextStaticwidth){
                    X += x - DownX;
                    mTextPosition.updatePosition(X,Y);
                    //mText.Eraser(0,0);
                    DownX = x;
                }

            }
        }//https://github.com/AnadolStudio/AndroidCurvesToolView
    }

    public void ActionUp(float x,float y){
        mRotate = false;
        mScale = false;
        ColorLoc = false;
        mtextStaticwidth = false;
        if (mColor!=null){
            switch (ColorLocType){
                case colorLocType.TEXT:
                    setTextColor(mColor);
                    break;
                case colorLocType.GRAD:
                    if (mListener!=null){
                        mListener.SetGradColorInLocMode(GradRainbowPosition,mColor);
                    }
                    break;
                case colorLocType.BORD:
                    setBorderTextColor(mColor);
                    break;
                case colorLocType.HIG:
                    setHighlighterColor(mColor);
                    break;
                case colorLocType.SHAD:
                    setTextShadowColor(mColor);
                    break;
            }
            mColor = null;
            //setTextColor(mColor);
        }

        mScaleSource = x;
    }

    public static class colorLocType {
        public final static String GRAD = "GRAD";
        public final static String BORD= "BORD";
        public final static String SHAD= "SHAD";
        public final static String HIG = "HIG";
        public final static String TEXT= "TEXT";
    }


    public void SetText(String text){

        mText.AddText(text);
        float x =(float) getWidth()/2;
        float y =(float) getHeight()/2;

        mTextPosition.AddPosition(x,y);
        mTextPaint.AddTextPaint(TextSize);

        float w = mTextPaint.TextPaint().measureText(text);

        float rightCopy = x + w;

        Path path = new Path();
        path.moveTo(x - w,(y));
        path.cubicTo(x- w,y,
                (x - w+((rightCopy-x - w)/2)),
               y,rightCopy,y);
        StaticLayout staticLayout = new StaticLayout(text, mTextPaint.TextPaint(), (int)Math.abs(w),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        StaticLayout staticLayoutB = new StaticLayout(text, mTextPaint.BorderTextPaint(), (int)Math.abs(w),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        mText.AddTextPath(path);
        mText.AddTextStaticLayout(staticLayout,staticLayoutB);
        mText.AddTextEraser();
        Matrix matrix = new Matrix();
        mTextMatrix.AddMatrix(matrix,0f,1f);
        invalidate();
    }

    public void AddText() {
        mText.AddText(mText.Text());
        mTextPosition.AddPosition( (mTextPosition.TextPosition().x+10),(mTextPosition.TextPosition().y+10));
        mTextPaint.AddTextPaint(TextSize);
        float w = mTextPaint.TextPaint().measureText(mText.Text());

        float rightCopy = (mTextPosition.TextPosition().x+10) + w;
        Path path = new Path();
        path.moveTo((mTextPosition.TextPosition().x+10) - w,(mTextPosition.TextPosition().y+10));
        path.cubicTo((mTextPosition.TextPosition().x+10)- w,(mTextPosition.TextPosition().y+10),
                ((mTextPosition.TextPosition().x+10) - w+((rightCopy-(mTextPosition.TextPosition().x+10) - w)/2)),
                (mTextPosition.TextPosition().y+10),rightCopy,(mTextPosition.TextPosition().y+10));

        StaticLayout staticLayout = new StaticLayout(mText.Text(), mTextPaint.TextPaint(), (int)Math.abs(w),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        StaticLayout staticLayoutB = new StaticLayout(mText.Text(), mTextPaint.BorderTextPaint(), (int)Math.abs(w),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        mText.AddTextPath(path);
        mText.AddTextStaticLayout(staticLayout,staticLayoutB);
        mText.AddTextEraser();
        Matrix matrix = new Matrix();
        mTextMatrix.AddMatrix(matrix,0f,1f);
        invalidate();
    }

    public void ChangeText(String text){
        float wAll = mTextPaint.TextPaint().measureText(mText.Text());
       // mText.setBend(0,mTextPosition.TextPosition().x - wAll,mTextPosition.TextPosition().y, mTextPosition.TextPosition().x+wAll);
        StaticLayout staticLayout = new StaticLayout(text, mTextPaint.TextPaint(), (int)Math.abs(wAll),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        StaticLayout staticLayoutB = new StaticLayout(text, mTextPaint.BorderTextPaint(), (int)Math.abs(wAll),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        mText.UpdateText(text);
        mText.UpdateTextStaticLayout(staticLayout,staticLayoutB);
        invalidate();
    }

    public void RemoveText() {
        if (mText.TextListSize() != 1){
            mText.RemoveText();
            mTextPosition.removePosition();
            mTextPaint.removePaint();

        }else {
            ((TextOnImageActivity)mContext).MargeAddOptions(INVISIBLE);
            ((TextOnImageActivity)mContext).FlipperSetVisibility();
            mText.RemoveText();
            mTextPosition.removePosition();
            mTextPaint.removePaint();

            mText.unloadingIndex();
            mTextPosition.unloadingIndex();
            mTextPaint.unloadingIndex();
            mTextMatrix.unloadingIndex();
        }
        invalidate();
    }

    public void setEraser(boolean eraser) {
        this.eraser = eraser;
    }
    public boolean getEraser() {
        return this.eraser;
    }

    public void setColorLoc(boolean b,String colortp,Integer position) {
        ColorLoc = b;
        ColorLocType = colortp;
        if (colortp.equals(colorLocType.GRAD)){
            GradRainbowPosition = position;
        }

    }

    public void setTextColor(int color) {
      if (mTextPaint.TextPaintSize()!=0){
          mTextPaint.setTextImg(null);
          mTextPaint.setTextImg(null,null);
          if (getBorderTextStoke()==0){
              mTextPaint.setTextColor(color);
              mTextPaint.setBorderTextColor(color);
          }else {
              mTextPaint.setTextColor(color);

          }

          invalidate();
      }
    }

    public void AddGradientColors(int [] color) {
        mTextPaint.TextPaint().setShader(null);
        Color1 = color[0];
        Color2 = color[1];

        mTextPaint.setTextImg(null);
        mTextPaint.setTextImg(null,null);

        Rect bounds = new Rect();
        mTextPaint.TextPaint().getTextBounds(mText.Text(), 0, mText.Text().length(), bounds);

        float w = mTextPaint.TextPaint().measureText(mText.Text())/2;
        float wAll = mTextPaint.TextPaint().measureText(mText.Text());
        float imgRight = mTextPosition.TextPosition().x + w;
        float imgBottom = mTextPosition.TextPosition().y + bounds.bottom;

        RectF imgRect = new RectF(mTextPosition.TextPosition().x-w,
                mTextPosition.TextPosition().y - bounds.height()+ bounds.bottom,
                imgRight,imgBottom);

        float right = mTextPosition.TextPosition().x +
                mTextPaint.TextPaint().measureText(mText.Text())/2;

        float bottom = mTextPosition.TextPosition().y
                + mText.getStatic().getTextStatic().getHeight() ;

        RectF RectDt = new RectF(mTextPosition.TextPosition().x
                - mTextPaint.TextPaint().measureText(mText.Text())/2,
                (mTextPosition.TextPosition().y + mText.getStatic().getTextStatic().getTopPadding() ),
                (right),bottom);
        LinearGradient linearGradient;
        if (getBend()==0){
            linearGradient = new LinearGradient(((0f)-imgRect.width())*0.5f,
                    (RectDt.height()-imgRect.height())*0.5f,w, 0
                    ,color,null ,Shader.TileMode.CLAMP);
        }else {
            linearGradient = new LinearGradient( imgRect.left, imgRect.top,
                    imgRect.right, imgRect.bottom
                    ,color,null, Shader.TileMode.CLAMP);
        }
        mTextPaint.setTextGradient(color,0);
        //matrix.reset();
        //matrix.setTranslate((imgRect.left)-imgRect.centerX(), imgRect.top+imgRect.centerY());
        mTextPaint.setTextGradient(linearGradient);
        invalidate();

    }

    public void SetGradientangle(int angle) {
        mTextPaint.setTextGradientAngle(angle);
        invalidate();
    }
    public void setTextOpacity(int opacity) {
        if (mTextPaint.TextPaintSize()!=0){
            mTextPaint.setTextOpacity(opacity);
            invalidate();
        }
    }
    public int getTextOpacity() {
        return mTextPaint.getTextOpacity();
    }

    public void setTextShadowPosition(float r,float x,float y) {
        if (mTextPaint.TextPaintSize()!=0){
            mTextPaint.setTextShadowPosition(r,x,y);
            invalidate();
        }
    }
    public void setTextShadowColor(int Color) {
        if (mTextPaint.TextPaintSize()!=0){
            mTextPaint.setTextShadowColor(Color);
            invalidate();
        }
    }
    public void setTextSize(float size) {
        if (mTextPaint.TextPaintSize()!=0){
            mTextPaint.setTextSize(size);
            invalidate();
        }
    }

    public float getTextSize() {
        return mTextPaint.getTextSize();
    }

    public void setTextFont(Typeface tf){
        mTextPaint.setTextFont(tf);
        invalidate();
    }

    public void setTextImg(Bitmap resource, String backgroundType) {
        if (backgroundType.equals("TEXT")){
            Rect bounds = new Rect();
            mTextPaint.TextPaint().getTextBounds(mText.Text(), 0, mText.Text().length(), bounds);

            float w = mTextPaint.TextPaint().measureText(mText.Text())/2;

            float imgRight = mTextPosition.TextPosition().x + w;
            float imgBottom = mTextPosition.TextPosition().y + bounds.bottom;

            RectF imgRect = new RectF(mTextPosition.TextPosition().x-w,
                    mTextPosition.TextPosition().y - bounds.height()+ bounds.bottom,
                    imgRight,imgBottom);

            TextImgBackground = getScaledBitmap(resource,imgRect.width(),imgRect.height());


            int centerX = (TextImgBackground.getWidth()) / 2;
            int centerY =( TextImgBackground.getHeight())/ 2;


            TextImgBackgroudMatrix.reset();
            TextImgBackgroudMatrix.setTranslate((imgRect.left)- centerX, imgRect.top- centerY);

            BitmapShader shader = new BitmapShader(TextImgBackground, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            mTextPaint.setTextImg(resource);
            mTextPaint.setTextImg(shader, TextImgBackgroudMatrix);

        } else if (backgroundType.equals("RECT")) {
            RectF imgRect;
            if (getBend()==0){
                // Split the text into lines using the newline character "\n"
                String[] lines = mText.Text().split("\n");

                float TextWidth_static = mTextPaint.TextPaint().measureText(lines[0]);
                if (TextWidth_static<0){
                    for (String number : lines) {
                        if ( mTextPaint.TextPaint().measureText(number) < TextWidth_static) {
                            TextWidth_static =  mTextPaint.TextPaint().measureText(number);
                        }
                    }
                }else {
                    for (String number : lines) {
                        if ( mTextPaint.TextPaint().measureText(number) > TextWidth_static) {
                            TextWidth_static =  mTextPaint.TextPaint().measureText(number);
                        }
                    }
                }

                float imgRight = mTextPosition.TextPosition().x + TextWidth_static/2;
                float imgBottom = mTextPosition.TextPosition().y + mText.getStatic().getTextStatic().getHeight();

                imgRect = new RectF((mTextPosition.TextPosition().x-TextWidth_static/2f)-getTextRectLeft(),
                        mTextPosition.TextPosition().y-getTextRectTop(),
                        imgRight+getTextRectRight(),
                        imgBottom+getTextRectBottom());

            }else {
                float TextWidth_bend = mTextPaint.TextPaint().measureText(mText.Text());

                Paint.FontMetrics F = mTextPaint.TextPaint().getFontMetrics();
                float BaseLine = F.bottom;
                float Ascent = F.top;

                float imgRight = mTextPosition.TextPosition().x + TextWidth_bend/2;
                float imgBottom = mTextPosition.TextPosition().y + BaseLine;

                imgRect = new RectF((mTextPosition.TextPosition().x-TextWidth_bend/2f)-getTextRectLeft(),
                        (mTextPosition.TextPosition().y + Ascent)-getTextRectTop(),
                        imgRight+getTextRectRight(),imgBottom+getTextRectBottom());
            }

            RectImgBackground = getScaledBitmap(resource,imgRect.width(),imgRect.height());

            int centerX = (RectImgBackground.getWidth());
            int centerY =( RectImgBackground.getHeight());

            RectImgBackgroundMatrix.reset();
            RectImgBackgroundMatrix.setTranslate(((0f)-centerX)*0.5f,(imgRect.height()-centerY)*0.5f);

            BitmapShader shader = new BitmapShader(RectImgBackground, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mTextPaint.setTextRectImg(resource);
            mTextPaint.setTextRectShader(shader,RectImgBackgroundMatrix);
        }


        invalidate();
    }

    public String getTextFont(){
        return "defog";
    }

    public void setTextStroke(float Stroke) {
        if (mTextPaint.TextPaintSize()!=0){
            mTextPaint.setTextStroke(Stroke);
            invalidate();
        }
    }

    public float getTextStoke() {
        return mTextPaint.getTextStroke();
    }

    public void setBorderTextStroke(float Stroke) {
        if (mTextPaint.TextPaintSize()!=0){
            mTextPaint.setBorderTextStroke(Stroke);
            invalidate();
        }
    }

    public float getBorderTextStoke() {
        return mTextPaint.getBorderTextStroke();
    }


    public void setTextFill(){
        if (mTextPaint.TextPaintSize()!=0){
            mTextPaint.setTextFill();
            invalidate();
        }
    }

    public void setBorderTextColor(int color) {
        if (mTextPaint.TextPaintSize()!=0){
            mTextPaint.setBorderTextColor(color);
            invalidate();
        }
    }

    public void setTextScaleX(float ScaleX) {
        if (mTextPaint.TextPaintSize()!=0){
            mTextPaint.setTextScaleX(ScaleX);
            invalidate();
        }

    }

    public float getTextScaleX() {
        return mTextPaint.getTextScaleX();
    }

    public void setTextSkewX(float SkewX) {
        mTextPaint.setTextSkewX(SkewX);
        invalidate();
    }

    public float getTextSkewX() {
       return mTextPaint.getTextSkewX();
    }


    public void setBend(float BendX) {
        mText.setBend(BendX);
        invalidate();
    }

    public float getBend() {
        return mText.getBend();
    }


    public void setLetterSpacing(float value){
        mTextPaint.setLatterSpacing(value);
        invalidate();
    }

    public float getLatterSpacing(){
        return mTextPaint.getLatterSpacing();

    }

    public void setLineSpacing(float value){
        mText.setLineSpace(value);
        invalidate();
    }

    public float getLineSpacing(){
        return mText.getLineSpace();

    }

    public void setHighlighter(){
        mTextPaint.setTextRect();
        invalidate();
    }

    public int getHighlighter(){
        return mTextPaint.getTextRect();

    }

    public void removeHighlighter(){
        mTextPaint.removeTextRect();
        invalidate();
    }

    public void setHighlighterCorners(float v){
        mTextPaint.setTextRectXY(v,v);
        invalidate();
    }

    public void setTextRectLeft(float left){
        mTextPaint.setTextRectLeft(left);
        invalidate();
    }

    public void setTextRectTop(float top){
        mTextPaint.setTextRectTop(top);
        invalidate();
    }

    public void setTextRectRight(float right){
        mTextPaint.setTextRectRight(right);
        invalidate();
    }

    public void setTextRectBottom(float bottom){
        mTextPaint.setTextRectBottom(bottom);
        invalidate();
    }

    public float getTextRectLeft(){
        return mTextPaint.getTextRectLeft();
    }

    public float getTextRectTop(){
        return mTextPaint.getTextRectTop();
    }

    public float getTextRectRight(){
        return   mTextPaint.getTextRectRight();
    }

    public float getTextRectBottom(){
        return mTextPaint.getTextRectBottom();
    }

    public float getHighlighterCorners(){
        return mTextPaint.getTextRectXY();

    }


    public void setHighlighterColor(int color){
        mTextPaint.setTextRectImg(null);
        mTextPaint.setTextRectShader(null,null);
        mTextPaint.setTextRectColor(color);
        invalidate();
    }

    public void setHighlighterOpacity(int opacity){
        mTextPaint.setTextRectOpacity(opacity);
        invalidate();
    }


    public void setTextMode(PorterDuff.Mode mode) {
        mTextPaint.setTextMode(mode);
        invalidate();
    }
    public void setTextRotation(int v){
        mTextMatrix.setRotation(v);
        invalidate();
    }


    private void setRotateDegree(float rotationAngleDegrees,float x,float y) {
        mTextMatrix.setRotation(rotationAngleDegrees,x,y);
    }

    private void setRotateDegree(int i,float rotationAngleDegrees,float x,float y) {
        mTextMatrix.setRotation(i,rotationAngleDegrees,x,y);
    }


    public Bitmap getFinalImage(){

        // Get the original bitmap object.
        final Bitmap output = mBitmap;

        Bitmap bitmap = Bitmap.createBitmap(output);
        Canvas canvas = new Canvas(bitmap);

        Bitmap EraseBitmap = Bitmap.createBitmap(output.getWidth(),output.getHeight(),output.getConfig());
        Canvas EraseCanvas = new Canvas(EraseBitmap);

        float ScaledTextSize,ScaledTextStroke,ScaledTextBorders;

        X_YFixer xAndyFixer = new X_YFixer();

        for (int i = 0; i < mText.TextListSize(); i++) {
            canvas.drawBitmap(bitmap,0,0,null);
            xAndyFixer.FixXYUsingMatrix(mTextPosition.TextPositionList().get(i).x,
                    mTextPosition.TextPositionList().get(i).y,zommer.getDefaultMatrix());

            ScaledTextSize =( mTextPaint.getTextPaintList().get(i).getTextSize())/xAndyFixer.getScale(zommer.getDefaultMatrix());

            ScaledTextStroke =(mTextPaint.getTextPaintList().get(i).getStrokeWidth())/xAndyFixer.getScale(zommer.getDefaultMatrix());

            ScaledTextBorders =(mTextPaint.getBorderTextPaintList().get(i).getStrokeWidth())/xAndyFixer.getScale(zommer.getDefaultMatrix());

            mTextPaint.setScaledTextSize_Stroke_Borders(i,ScaledTextSize,ScaledTextStroke,ScaledTextBorders);

            mTextPaint.getTextPaintList().get(i).setShadowLayer(
                    mTextPaint.getShadowLayerTextPaintList().get(i).getR()/xAndyFixer.getScale(zommer.getDefaultMatrix()),
                    mTextPaint.getShadowLayerTextPaintList().get(i).getX()/xAndyFixer.getScale(zommer.getDefaultMatrix()),
                    mTextPaint.getShadowLayerTextPaintList().get(i).getY()/xAndyFixer.getScale(zommer.getDefaultMatrix()),
                    mTextPaint.getShadowLayerTextPaintList().get(i).getColor());


            float w = mTextPaint.getTextPaintList().get(i).measureText(mText.getTextList().get(i));
            Paint.FontMetrics F = mTextPaint.getTextPaintList().get(i).getFontMetrics();
            float BaseLine= F.bottom;
            float Ascent = F.top;


            float centrex = ((xAndyFixer.getFixedXYUsingMartix().x - w) + (xAndyFixer.getFixedXYUsingMartix().x+ w)) * 0.5f;

            float centrey = ((xAndyFixer.getFixedXYUsingMartix().y+ Ascent) +(xAndyFixer.getFixedXYUsingMartix().y + BaseLine)) * 0.5f;


            String[] lines = mText.getTextList().get(i).split("\n");

            float wAll = mTextPaint.getTextPaintList().get(i).measureText(lines[0]);
            if (wAll<0){
                for (String number : lines) {
                    if ( mTextPaint.getTextPaintList().get(i).measureText(number) < wAll) {
                        wAll =  mTextPaint.getTextPaintList().get(i).measureText(number);
                    }
                }
            }else {
                for (String number : lines) {
                    if ( mTextPaint.getTextPaintList().get(i).measureText(number) > wAll) {
                        wAll =  mTextPaint.getTextPaintList().get(i).measureText(number);
                    }
                }
            }


            StaticLayout staticLayout = new StaticLayout(mText.getTextList().get(i),mTextPaint.getTextPaintList().get(i),
                    (int)Math. abs(wAll),
                    Layout.Alignment.ALIGN_NORMAL,mText.getTextStatic().get(i).getLineSpace(), 0.0f, false);

            StaticLayout staticLayoutBorder = new StaticLayout(mText.getTextList().get(i),
                    mTextPaint.getBorderTextPaintList().get(i),(int)Math. abs(wAll),
                    Layout.Alignment.ALIGN_NORMAL,mText.getTextStatic().get(i).getLineSpace(), 0.0f, false);

            mText.UpdateTextStaticLayout(i,staticLayout,staticLayoutBorder);

            RectF RectDt;
            canvas.save();

            if (mText.getTextBend().get(i)==0)

            {
                float right = xAndyFixer.getFixedXYUsingMartix().x +
                        mTextPaint.getTextPaintList().get(i).measureText(mText.getTextList().get(i))/2;

                float bottom = xAndyFixer.getFixedXYUsingMartix().y
                        + mText.getTextStatic().get(i).getTextStatic().getHeight() ;

                 RectDt = new RectF(xAndyFixer.getFixedXYUsingMartix().x
                        - mTextPaint.getTextPaintList().get(i).measureText(mText.getTextList().get(i))/2,
                        (xAndyFixer.getFixedXYUsingMartix().y + mText.getTextStatic().get(i).getTextStatic().getTopPadding() ),
                        (right),bottom);

                canvas.translate(xAndyFixer.getFixedXYUsingMartix().x,xAndyFixer.getFixedXYUsingMartix().y);
                canvas.rotate(mTextMatrix.getRotateValueList().get(i),(RectDt.centerX())-(xAndyFixer.getFixedXYUsingMartix().x),
                        (RectDt.centerY())-(xAndyFixer.getFixedXYUsingMartix().y));

                if (mTextPaint.getTextimgsList().get(i)!=null){


                    TextImgBackground = getScaledBitmap(mTextPaint.getTextimgsList().get(i),RectDt.width(),RectDt.height());

                    TextImgBackgroudMatrix.reset();
                    TextImgBackgroudMatrix.setTranslate(((0f)- TextImgBackground.getWidth())*0.5f,
                            (RectDt.height()- TextImgBackground.getHeight())*0.5f);

                    BitmapShader shader = new BitmapShader(TextImgBackground, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                    mTextPaint.setTextImg(i,shader, TextImgBackgroudMatrix);
                }

                if (mTextPaint.getTextPaintList().get(i).getShader()!=null&&mTextPaint.getTextimgsList().get(i)==null){

                    Rect bounds = new Rect();
                    mTextPaint.getTextPaintList().get(i).getTextBounds(mText.getTextList().get(i), 0,
                            mText.getTextList().get(i).length(), bounds);

                    float imgRight = xAndyFixer.getFixedXYUsingMartix().x + wAll/2;
                    float imgBottom = xAndyFixer.getFixedXYUsingMartix().y + bounds.bottom;

                    RectF imgRect = new RectF(xAndyFixer.getFixedXYUsingMartix().x-wAll/2,
                            xAndyFixer.getFixedXYUsingMartix().y - bounds.height()+ bounds.bottom,
                            imgRight,imgBottom);

                    LinearGradient linearGradient = new LinearGradient(((0f)-imgRect.width())*0.5f,
                            (RectDt.height()-imgRect.height())*0.5f,wAll/2, 0
                            ,mTextPaint.getGradientsTextPaintList().get(i).getColors(),
                           null,
                            Shader.TileMode.CLAMP);

                    mTextPaint.setTextGradient(i,linearGradient,mTextPaint.getGradientsTextPaintList().get(i).getAngle(),
                            imgRect.width()/2f, imgRect.height()/2f);
                }


                Rect bounds = new Rect();
                mTextPaint.getTextPaintList().get(i).getTextBounds(mText.getTextList().get(i), 0,
                        mText.getTextList().get(i).length(), bounds);


                float imgRight = 0+ wAll/2;
                float imgBottom =0f + mText.getTextStatic().get(i).getTextStatic().getHeight();
                RectF imgRect = new RectF(
                        (0f-wAll/2f)-(mTextPaint.getTextRectPaintList().get(i).getLeft()/xAndyFixer.getScale(zommer.getDefaultMatrix())),
                        0f-(mTextPaint.getTextRectPaintList().get(i).getTop()/xAndyFixer.getScale(zommer.getDefaultMatrix())),
                        imgRight+(mTextPaint.getTextRectPaintList().get(i).getRight()/xAndyFixer.getScale(zommer.getDefaultMatrix())),
                        imgBottom+(mTextPaint.getTextRectPaintList().get(i).getBottom()/xAndyFixer.getScale(zommer.getDefaultMatrix())));

                if (mTextPaint.getTextRectImgs().get(i)!=null){
                    float imgRightI = xAndyFixer.getFixedXYUsingMartix().x + wAll/2;
                    float imgBottomI = xAndyFixer.getFixedXYUsingMartix().y + mText.getTextStatic().get(i).getTextStatic().getHeight();

                    RectF imgRectI = new RectF((xAndyFixer.getFixedXYUsingMartix().x-wAll/2f)-(mTextPaint.getTextRectPaintList().get(i).getLeft()/xAndyFixer.getScale(zommer.getDefaultMatrix())),
                            xAndyFixer.getFixedXYUsingMartix().y-(mTextPaint.getTextRectPaintList().get(i).getTop()/xAndyFixer.getScale(zommer.getDefaultMatrix())),
                            imgRightI+(mTextPaint.getTextRectPaintList().get(i).getRight()/xAndyFixer.getScale(zommer.getDefaultMatrix())),
                            imgBottomI+(mTextPaint.getTextRectPaintList().get(i).getBottom()/xAndyFixer.getScale(zommer.getDefaultMatrix())));

                    RectImgBackground = getScaledBitmap(mTextPaint.getTextRectImgs().get(i), imgRectI.width(),imgRectI.height());

                    int centerXI = (RectImgBackground.getWidth());
                    int centerYI =( RectImgBackground.getHeight());

                    RectImgBackgroundMatrix.reset();
                    RectImgBackgroundMatrix.setTranslate(((0f)-centerXI)*0.5f,(imgRectI.height()-centerYI)*0.5f);

                    BitmapShader shader = new BitmapShader(RectImgBackground, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                    mTextPaint.setTextRectShader(i,shader,RectImgBackgroundMatrix);
                }


                canvas.drawRoundRect(imgRect,mTextPaint.getTextRectPaintList().get(i).getX()/xAndyFixer.getScale(zommer.getDefaultMatrix()),
                        mTextPaint.getTextRectPaintList().get(i).getY()/xAndyFixer.getScale(zommer.getDefaultMatrix()),
                        mTextPaint.getTextRectPaintList().get(i).getRectPaint());

                if (mTextPaint.getBorderTextPaintList().get(i).getStrokeWidth()>0){
                    mText.getTextStatic().get(i).getTextBordersStatic().draw(canvas);
                }
                mText.getTextStatic().get(i).getTextStatic().draw(canvas);
            }

            else

            {

                float right = xAndyFixer.getFixedXYUsingMartix().x +
                        mTextPaint.getTextPaintList().get(i).measureText(mText.getTextList().get(i))/2;

                float bottom = xAndyFixer.getFixedXYUsingMartix().y
                        + BaseLine ;

                RectDt = new RectF(xAndyFixer.getFixedXYUsingMartix().x
                        - mTextPaint.getTextPaintList().get(i).measureText(mText.getTextList().get(i))/2,
                        (xAndyFixer.getFixedXYUsingMartix().y + Ascent),
                        (right),bottom);

                canvas.translate(xAndyFixer.getFixedXYUsingMartix().x,xAndyFixer.getFixedXYUsingMartix().y);
                canvas.rotate(mTextMatrix.getRotateValueList().get(i),(RectDt.centerX())-(xAndyFixer.getFixedXYUsingMartix().x),
                        (RectDt.centerY())-(xAndyFixer.getFixedXYUsingMartix().y));

                mText.MovePath(i,(0f- w),
                       0f,
                        (0f+ w),
                        mText.getTextBend().get(i)/xAndyFixer.getScale(zommer.getDefaultMatrix()));
                mText.getTextPath().get(i).transform(mTextMatrix.getTextMatrixList().get(i));

                if (mTextPaint.getTextimgsList().get(i)!=null){

                    TextImgBackground = getScaledBitmap(mTextPaint.getTextimgsList().get(i),RectDt.width(),RectDt.height());
                    float centerX = (TextImgBackground.getWidth()/2f);
                    float centerY =(TextImgBackground.getHeight()/2f);

                    TextImgBackgroudMatrix.reset();
                    TextImgBackgroudMatrix.setTranslate(((0f)-centerX),(((BaseLine+Ascent)*0.5f)-centerY));

                    BitmapShader shader = new BitmapShader(TextImgBackground, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                    mTextPaint.setTextImg(i,shader, TextImgBackgroudMatrix);
                }

                if (mTextPaint.getTextPaintList().get(i).getShader()!=null
                        &&mTextPaint.getTextimgsList().get(i)==null){
                    Rect bounds = new Rect();
                    mTextPaint.TextPaint().getTextBounds(mText.getTextList().get(i), 0, mText.getTextList().get(i).length(), bounds);

                    float w2 = mTextPaint.TextPaint().measureText(mText.getTextList().get(i))/2;

                    float imgRight = 0f + w2;
                    float imgBottom = 0f + bounds.bottom;

                    RectF imgRect = new RectF(0f-w2/2,
                            0f - Ascent,
                            imgRight,imgBottom);

                    LinearGradient  linearGradient = new LinearGradient( imgRect.left, imgRect.top,
                            imgRect.right, imgRect.bottom
                            ,mTextPaint.getGradientsTextPaintList().get(i).getColors(),null,
                            Shader.TileMode.CLAMP);

                    mTextPaint.setTextGradient(i,linearGradient,mTextPaint.getGradientsTextPaintList().get(i).getAngle(),
                            imgRect.width()/2f, imgRect.height()/2f);
                }


                float imgRight = 0 + w/2;
                float imgBottom = 0f + BaseLine;

                RectF imgRect = new RectF((0f-w/2f)-(mTextPaint.getTextRectPaintList().get(i).getLeft()/xAndyFixer.getScale(zommer.getDefaultMatrix())),
                        (0f + Ascent)-(mTextPaint.getTextRectPaintList().get(i).getTop()/xAndyFixer.getScale(zommer.getDefaultMatrix())),
                        imgRight+(mTextPaint.getTextRectPaintList().get(i).getRight()/xAndyFixer.getScale(zommer.getDefaultMatrix())),
                        imgBottom+(mTextPaint.getTextRectPaintList().get(i).getBottom()/xAndyFixer.getScale(zommer.getDefaultMatrix())));


                if (mTextPaint.getTextRectImgs().get(i)!=null){
                    float imgRightI = xAndyFixer.getFixedXYUsingMartix().x  + w/2;
                    float imgBottomI = xAndyFixer.getFixedXYUsingMartix().y+ BaseLine;

                    RectF imgRectI = new RectF((xAndyFixer.getFixedXYUsingMartix().x-w/2f)-mTextPaint.getTextRectPaintList().get(i).getLeft(),
                            (xAndyFixer.getFixedXYUsingMartix().y + Ascent)-mTextPaint.getTextRectPaintList().get(i).getTop(),
                            imgRightI+mTextPaint.getTextRectPaintList().get(i).getRight(),
                            imgBottomI+mTextPaint.getTextRectPaintList().get(i).getBottom());


                    RectImgBackground = getScaledBitmap(mTextPaint.getTextRectImgs().get(i), imgRectI.width(),imgRectI.height());

                    int centerXI = (RectImgBackground.getWidth());
                    int centerYI =( RectImgBackground.getHeight());

                    RectImgBackgroundMatrix.reset();
                    RectImgBackgroundMatrix.setTranslate(((0f)-centerXI)*0.5f,(imgRectI.height()-centerYI)*0.5f);

                    BitmapShader shader = new BitmapShader(RectImgBackground, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                    mTextPaint.setTextRectShader(i,shader,RectImgBackgroundMatrix);
                }


                canvas.drawRoundRect(imgRect,mTextPaint.getTextRectPaintList().get(i).getX()/xAndyFixer.getScale(zommer.getDefaultMatrix()),
                        mTextPaint.getTextRectPaintList().get(i).getY()/xAndyFixer.getScale(zommer.getDefaultMatrix()),mTextPaint.getTextRectPaintList().get(i).getRectPaint());

                if (mTextPaint.getBorderTextPaintList().get(i).getStrokeWidth()>0){
                    canvas.drawTextOnPath(mText.getTextList().get(i), mText.getTextPath().get(i),
                            0, 0,mTextPaint.getBorderTextPaintList().get(i));
                }
                canvas.drawTextOnPath(mText.getTextList().get(i), mText.getTextPath().get(i),
                        0, 0, mTextPaint.getTextPaintList().get(i));

            }

            canvas.restore();
            EraseCanvas.drawPath(mRealErasePath,mErasePaint);
            canvas.drawBitmap(EraseBitmap,0,0,null);
        }
        return bitmap;
    }

    public Bitmap getScaledBitmap(Bitmap mBitmap, float targetWidth, float targetHeight){

        float division0 = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();
        float division1 = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();

        float RWidth,RHeight;

        if (targetHeight>
               targetWidth){

            if (targetHeight/division0
                    <targetWidth){


                float d = (float) mBitmap.getWidth()/(float) mBitmap.getHeight();

                RWidth = targetWidth;
                RHeight =  (RWidth/d);

            }else {
                float d = (float) mBitmap.getHeight()/(float) mBitmap.getWidth();

                RHeight = targetHeight;
                RWidth = (RHeight/d);

            }


        }else {
            if(targetWidth/division1
                    <targetHeight){
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

    /*Returns the bitmap position inside an imageView.
     * @param imageView source ImageView
     * @return Rect position of the bitmap in the ImageView
     */

    public RectF getBitmapRect()
    {
        RectF rect = new RectF();

        final Bitmap drawable = mBitmap;
        if ( drawable == null)
        {
            return rect;
        }

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        zommer.getDefaultMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Bitmap d = mBitmap;
        final int origW = d.getWidth();
        final int origH = d.getHeight();

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

    /**
     * Done From Drawn Texts
     */
    public void DoneDrawn(){
        ShowCadre = true;
        mText.clearTextList();
        mTextPaint.clearTextPaintList();
        mTextPosition.clearPositions();
        mTextMatrix.clearMatrices();

        mText.unloadingIndex();
        mTextPosition.unloadingIndex();
        mTextPaint.unloadingIndex();
        mTextMatrix.unloadingIndex();
        invalidate();
    }

    /**
     * Cancel the Draw Text Process
     */
    public void CancelDrawn(){
        ShowCadre = true;
        mText.clearTextList();
        mTextPaint.clearTextPaintList();
        mTextPosition.clearPositions();
        mTextMatrix.clearMatrices();

        mText.unloadingIndex();
        mTextPosition.unloadingIndex();
        mTextPaint.unloadingIndex();
        mTextMatrix.unloadingIndex();
        invalidate();
    }

    /**
     *  for more Visit --->  https://stackoverflow.com/a/16792888  & ---> https://stackoverflow.com/a/36146901
     * @param downX X position of touche of user Finger in Screen
     * @param downY Y position of touche of user Finger in Screen
     * @param x X position of Circle in the screen
     * @param y X position of Circle in the screen
     * @return distance.
     */
    private boolean isClicked(float downX, float downY, float x, float y){
        int distance = (int) Math.sqrt(((downX - x) * (downX - x)) +
                ((downY - y) * (downY - y)) );
        ShowCadre = true;
        return distance < 25/zommer.getScaleX();
    }

    /**
     *
     * @param clickCircles The callback that will run
     */
    public void ClickCircles(ClickCircles clickCircles){
        this.mClickCircles = clickCircles;
    }

    public interface ClickCircles {

        /**
         *
         * @param b
         * @param textOnImage
         */
        void ClickTopLeft(boolean b, TextOnImage textOnImage);

        /**
         *
         * @param b
         * @param textOnImage
         */
        void ClickTopRight(boolean b, TextOnImage textOnImage);

        /**
         *
         * @param b
         * @param textOnImage
         */
        void ClickBottomRight(boolean b, TextOnImage textOnImage);

        /**
         *
         * @param b
         * @param textOnImage
         */
        void ClickBottomLeft(boolean b, TextOnImage textOnImage);

    }

    final GestureDetector gd = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){

        //here is the method for double tap


        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mText.getTextList().size()>0){
                mxAndyFixer.FixXYUsingMatrix(e.getX(),e.getY(),zommer.getCanvasMatrix());
                float w = mTextPaint.TextPaint().measureText(mText.Text())/2;

                Paint.FontMetrics F = mTextPaint.TextPaint().getFontMetrics();
                float BaseLine = F.bottom;
                float Ascent = F.top;

                RectF rect;
                RectF rect1;

                float rightCopy ;
                float bottomCopy ;
                float leftCopy;
                float topCopy ;

                if (mText.getBend()==0){

                    // Split the text into lines using the newline character "\n"
                    String[] lines = mText.Text().split("\n");

                    float maxWidth = mTextPaint.TextPaint().measureText(lines[0]);
                    if (maxWidth<0){
                        for (String number : lines) {
                            if (mTextPaint.TextPaint().measureText(number) < maxWidth) {
                                maxWidth = mTextPaint.TextPaint().measureText(number);
                            }
                        }
                    }else {
                        for (String number : lines) {
                            if (mTextPaint.TextPaint().measureText(number) > maxWidth) {
                                maxWidth = mTextPaint.TextPaint().measureText(number);
                            }
                        }
                    }


                    rightCopy = mTextPosition.TextPosition().x + maxWidth/2;
                    bottomCopy = mTextPosition.TextPosition().y + mText.getStatic().getTextStatic().getHeight() ;
                    leftCopy =mTextPosition.TextPosition().x-maxWidth/2;
                    topCopy = mTextPosition.TextPosition().y + mText.getStatic().getTextStatic().getTopPadding();
                }else {
                    rightCopy = mTextPosition.TextPosition().x +w;
                    bottomCopy = mTextPosition.TextPosition().y +BaseLine;
                    leftCopy =mTextPosition.TextPosition().x-w;
                    topCopy =mTextPosition.TextPosition().y+Ascent;
                }

                rect = new RectF(rightCopy,
                        topCopy,
                        leftCopy
                        , bottomCopy);

                rect1= new RectF(leftCopy,
                        topCopy,
                        rightCopy, bottomCopy);


                if(rect.contains(mxAndyFixer.getFixedXYUsingMartix().x, mxAndyFixer.getFixedXYUsingMartix().y) |
                        rect1.contains(mxAndyFixer.getFixedXYUsingMartix().x, mxAndyFixer.getFixedXYUsingMartix().y)){
                    ((TextOnImageActivity)mContext).ShowTextRitter(1,mText.Text());
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    });


    public interface ToDoWhenItIsOnTouchActionListener {
        //void onTouchAction(int highlighterProgress, int cornerProgress, int rotationProgress);
        void SetGradColorInLocMode(Integer Position,int Color);
    }
    public void setToDoWhenItIsOnTouchActionListener(ToDoWhenItIsOnTouchActionListener listener) {
        mListener = listener;
    }
}