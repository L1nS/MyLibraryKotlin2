package com.lins.mykotlinlibrary.ui.hencoder.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.imyyq.mvvm.app.dp
import com.lins.mykotlinlibrary.ui.hencoder.getBitmap

/**
 *@CreateTime 2021/2/26 14:25
 *@Describe
 */

private val IMAGE_SIZE = 300.dp.toInt()
private const val EXTRA_SCALE_FRACTION =1.4f

class ScaleImageView(context: Context, attrs: AttributeSet?) : View(context, attrs),
    GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getBitmap(resources, IMAGE_SIZE)
    private var offsetX = 0f
    private var offsetY = 0f
    private var bigScale = 0f
    private var smallScale = 0f
    private val gestureDetector = GestureDetectorCompat(context, this).apply {
        setOnDoubleTapListener(this@ScaleImageView)
    }
    private var scaleFraction = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val scaleAnimator: ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(this, "scaleFraction", 0f, 1f)
    }
    private var big = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        offsetX = (width - IMAGE_SIZE) / 2f
        offsetY = (height - IMAGE_SIZE) / 2f

        if (bitmap.width / bitmap.height.toFloat() > width / height) {
            smallScale = width / bitmap.width.toFloat()
            bigScale = height / bitmap.height.toFloat()* EXTRA_SCALE_FRACTION
        } else {
            bigScale = width / bitmap.width.toFloat()* EXTRA_SCALE_FRACTION
            smallScale = height / bitmap.height.toFloat()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val scale = smallScale + (bigScale - smallScale) * scaleFraction
        canvas.scale(scale, scale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {

    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return true
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        big = !big
        if (big) {
            scaleAnimator.start()
        } else {
            scaleAnimator.reverse()
        }
        invalidate()
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }
}