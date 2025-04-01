package com.phantomhive.exil.hellopics.Img_Editor.Views.ImageStretch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressLint("AppCompatCustomView")
public class Mesh extends ImageView {

    private static final int MESH_WIDTH = 1;
    private static final int MESH_HEIGHT = 1;
    private static final int FULL_COLOR = 256;
    private static final int HALF_COLOR = 128;
    private static final Random colorRandom = new Random(0);

    private int meshWidth = MESH_WIDTH;
    private int meshHeight = MESH_HEIGHT;
    private boolean hasColor = false;
    private boolean isTransparent = false;

    private List<Pair<Float, Float>> coordinates;
    private List<Integer> colors;

    int index;
    boolean inMove = false;

    private Bitmap mBitmap;
    private Bitmap mSBitmap;


    public Mesh(Context context) {
        super(context);
        init();
    }

    public Mesh(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Mesh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Mesh(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        coordinates = new ArrayList<>();
    }

    public void setup(int column, int row, boolean randomColor, boolean transparent) {
        meshWidth = column;
        meshHeight = row;
        hasColor = randomColor;
        isTransparent = transparent;
        generateCoordinates();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float division0 = (float) mBitmap.getHeight() / (float) mBitmap.getWidth();
        float division1 = (float) mBitmap.getWidth() / (float) mBitmap.getHeight();
        int FW = (int) (getHeight() * division1);
        int FH = (int) (getWidth() * division0);

        if (mBitmap.getWidth() > mBitmap.getHeight()) {
            mSBitmap = Bitmap.createScaledBitmap(mBitmap, getWidth(), FH, true);
            Log.d("TAG", "onResourceReady: -1");
        } else if (mBitmap.getWidth() < mBitmap.getHeight()) {
            if (FH < getHeight()) {
                mSBitmap = Bitmap.createScaledBitmap(mBitmap, getWidth(), FH, true);
                Log.d("TAG", "onResourceReady: 1");
            } else if (mBitmap.getWidth() == mBitmap.getHeight()) {
                mSBitmap = Bitmap.createScaledBitmap(mBitmap, getWidth(), FH, true);
                Log.d("TAG", "onResourceReady: -1");
            } else {
                mSBitmap = Bitmap.createScaledBitmap(mBitmap, FW, getHeight(), true);
                Log.d("TAG", "onResourceReady: 2");
            }
        }

        coordinates = generateCoordinate(
                meshWidth,
                meshHeight,
                mSBitmap.getWidth(),
                mSBitmap.getHeight(),
                getPaddingStart(),
                getPaddingEnd(),
                getPaddingTop(),
                getPaddingBottom());
        generateCoordinates();
    }

    private void generateCoordinates() {
        if (hasColor) {
            colors = new ArrayList<>();
            for (int i = 1; i <= (meshWidth + 1) * (meshHeight + 1); i++) {
                int alpha = isTransparent ? HALF_COLOR : FULL_COLOR - 1;
                int red = colorRandom.nextInt(FULL_COLOR);
                int green = colorRandom.nextInt(FULL_COLOR);
                int blue = colorRandom.nextInt(FULL_COLOR);
                int color = Color.argb(alpha, red, green, blue);
                colors.add(color);
            }
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (bm != null) {
            mBitmap = bm;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmapMesh(
                mSBitmap,
                meshWidth,
                meshHeight,
                getVerticesArray(),
                0,
                null,
                0,
                null
        );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < coordinates.size(); i++) {
                    if (isClicked(event.getX(),event.getY(),coordinates.get(i).first,coordinates.get(i).second)){
                        inMove = true;
                        index = i;
                    }
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if (inMove){
                    coordinates.set(index,new Pair<>(event.getX(),event.getY()));
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                inMove = false;
                return true;
        }

        return true;

    }

    private float[] getVerticesArray() {
        List<Float> vertices = new ArrayList<>();
        for (Pair<Float, Float> coordinate : coordinates) {
            vertices.add(coordinate.first);
            vertices.add(coordinate.second);
        }
        float[] verticesArray = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            verticesArray[i] = vertices.get(i);
        }
        return verticesArray;
    }


    private List<Pair<Float, Float>> generateCoordinate(
            int col, int row, int width, int height,
            int paddingStart, int paddingEnd, int paddingTop, int paddingBottom) {

        int widthSlice = (width - (paddingStart + paddingEnd)) / col;
        int heightSlice = (height - (paddingTop + paddingBottom)) / row;

        List<Pair<Float, Float>> coordinates = new ArrayList<>();

        for (int y = 0; y <= row; y++) {
            for (int x = 0; x <= col; x++) {
                float xCoordinate = x * widthSlice + paddingStart;
                float yCoordinate = y * heightSlice + paddingTop;
                coordinates.add(new Pair<>(xCoordinate, yCoordinate));
            }
        }

        return coordinates;
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
        return distance < 25;
    }
}
