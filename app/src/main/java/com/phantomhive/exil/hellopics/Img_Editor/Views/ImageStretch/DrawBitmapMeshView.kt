package com.phantomhive.exil.hellopics.Img_Editor.Views.ImageStretch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.widget.ImageView
import kotlin.math.pow
import kotlin.random.Random


@SuppressLint("AppCompatCustomView")
class DrawBitmapMeshView  constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ImageView(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        const val MESH_WIDTH = 1
        const val MESH_HEIGHT = 1
        const val FULL_COLOR = 256
        const val HALF_COLOR = 128
        private val colorRandom = Random(0)

    }

    private var meshWidth = MESH_WIDTH
    private var mestHeight = MESH_HEIGHT
    private var hasColor = false
    private var isTransparent = false


    private lateinit var coordinates: List<Pair<Float, Float>>
    private var colors: List<Int>? = null

    lateinit var mBitmap : Bitmap
    lateinit var mSBitmap : Bitmap

    fun setup(column: Int, row: Int, randomColor: Boolean, transparent: Boolean) {
        meshWidth = column
        mestHeight = row
        hasColor = randomColor
        isTransparent = transparent
        generateCoordinates()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val division0: Float = mBitmap.height.toFloat() / mBitmap.width.toFloat()
        val division1: Float = mBitmap.width.toFloat() / mBitmap.height.toFloat()
        val FW = (height * division1).toInt()
        val FH = (width * division0).toInt()

        if (mBitmap.width > mBitmap.height) {
            mSBitmap = Bitmap.createScaledBitmap(
                mBitmap,
                width, FH, true
            )
            Log.d("TAG", "onResourceReady: -1")
        } else if (mBitmap.getWidth() < mBitmap.getHeight()) {
            if (FH < height) {
                mSBitmap = Bitmap.createScaledBitmap(
                    mBitmap,
                    width, FH, true
                )
                Log.d("TAG", "onResourceReady: 1")
            } else if ((mBitmap.getWidth() == mBitmap.getHeight())){
                mSBitmap = Bitmap.createScaledBitmap(
                    mBitmap,
                    width, FH, true
                )
                Log.d("TAG", "onResourceReady: -1")
            } else{
                mSBitmap = Bitmap.createScaledBitmap(
                    mBitmap, FW,
                    height, true
                )
                Log.d("TAG", "onResourceReady: 2")
            }
        }
        coordinates = generateCoordinate(
                meshWidth,
            mestHeight,
            mSBitmap.width,
            mSBitmap.height,
            paddingStart,
            paddingEnd,
            paddingTop,
            paddingBottom)
        generateCoordinates()


    }

    private fun generateCoordinates() {


        if (hasColor) {
                colors = (1..(meshWidth + 1) * (mestHeight + 1)).map {
                        Color.argb(if (isTransparent) HALF_COLOR else FULL_COLOR - 1,
                        colorRandom.nextInt(FULL_COLOR),
                        colorRandom.nextInt(FULL_COLOR),
                        colorRandom.nextInt(FULL_COLOR))
                }
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        if (bm != null) {
            mBitmap = bm
        }
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmapMesh(
            mSBitmap, meshWidth, mestHeight,
            coordinates.flatMap { listOf(it.first, it.second) }.toFloatArray(),
            0, colors?.toIntArray(), 0, null)

    }






    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            ACTION_DOWN, ACTION_UP, ACTION_MOVE -> {
                val sorted = coordinates.sortedBy { (it.first - event.x).pow(2) + (it.second - event.y).pow(2) }
                val selectedIndex = coordinates.indexOf(sorted[0])
                coordinates = coordinates.mapIndexed { index, pair -> if (index == selectedIndex) (event.x to event.y) else pair }
                invalidate()
                return true
            }
        }

        return false
    }

    private fun generateCoordinate(
        col: Int, row: Int, width: Int, height: Int,
        paddingStart: Int = 0, paddingEnd: Int = 0,
        paddingTop: Int = 0, paddingBottom: Int = 0 ): List<Pair<Float, Float>> {

        val widthSlice = (width - (paddingStart + paddingEnd)) / (col)
        val heightSlice = (height - (paddingTop + paddingBottom)) / (row)

        val coordinates = mutableListOf<Pair<Float, Float>>()

        for (y in 0..row) {
            for (x in 0..col) {
                coordinates.add(
                    Pair(
                        (x * widthSlice + paddingStart).toFloat(),
                        (y * heightSlice + paddingTop).toFloat()
                    )
                )
            }
        }

        return coordinates
    }
}
