package com.lins.mykotlinlibrary.ui.hencoder.touch

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.animation.doOnEnd
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import com.imyyq.mvvm.app.dp
import com.lins.mykotlinlibrary.ui.hencoder.getBitmap
import kotlin.math.max
import kotlin.math.min

/**
 *@CreateTime 2021/2/26 14:25
 *@Describe
 */

private val IMAGE_SIZE = 300.dp.toInt()
private const val EXTRA_SCALE_FACTOR = 1.5f

class ScaleImageView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getBitmap(resources, IMAGE_SIZE)
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f
    private var bigScale = 0f
    private var smallScale = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private val gestureListener = GestureListener()
    private val scaleFlingRunner = ScaleFlingRunner()
    private val scaleGestureListener = ScaleGestureListener()

    private val gestureDetector = GestureDetectorCompat(context, gestureListener)
    private val scaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener)
    private var currentScale = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val scaleAnimator = ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale)
    private var big = false
    private val scroller = OverScroller(context)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        originalOffsetX = (width - IMAGE_SIZE) / 2f
        originalOffsetY = (height - IMAGE_SIZE) / 2f

        if (bitmap.width / bitmap.height.toFloat() > width / height.toFloat()) {
            smallScale = width / bitmap.width.toFloat()
            bigScale = height / bitmap.height.toFloat() * EXTRA_SCALE_FACTOR
        } else {
            bigScale = width / bitmap.width.toFloat() * EXTRA_SCALE_FACTOR
            smallScale = height / bitmap.height.toFloat()
        }
        currentScale = smallScale
        scaleAnimator.setFloatValues(smallScale, bigScale)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress)
            gestureDetector.onTouchEvent(event)
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val scaleFraction = (currentScale - smallScale) / (bigScale - smallScale)
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction)
        canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
    }


    private fun fixOffset() {
        offsetX = min(offsetX, (bitmap.width * bigScale - width) / 2)
        offsetX = max(offsetX, -(bitmap.width * bigScale - width) / 2)
        offsetY = min(offsetY, (bitmap.height * bigScale - height) / 2)
        offsetY = max(offsetY, -(bitmap.height * bigScale - height) / 2)
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            //每次 ACTION_DOWN 事件出现的时候都会被调用,在这里返回 true 可以保证必然消费掉事件
            return true
        }

        override fun onShowPress(e: MotionEvent?) {
            //用户按下 100ms 不松手后会被调用,用于标记 [可以显示按下状态]
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            //用户单击时被调用(支持长按时 长按后松手不会调用、双击的第二下时不会被调用
            return super.onSingleTapUp(e)
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            //用户滑动时被调用
            //第一个事件是用户按下时的 ACTION_DOWN 事件，第二个事件是当前事件
            //偏移时按下时的位置 - 当前事件的位置,偏移量为初始位置-当前位置
            if (big) {
                offsetX -= distanceX
                offsetY -= distanceY
                fixOffset()
                invalidate()
            }
            return false
        }

        override fun onLongPress(e: MotionEvent?) {
            //用户长按（按下 500ms 不松手） 后会被调用
            //这个 500ms 在 GestureDetectorCompat 中变成了 600ms （？？？）
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            //用户滑动时迅速抬起时被调用，用于用户希望控件进行惯性滑动的场景
            if (big) {
                scroller.fling(
                    offsetX.toInt(),
                    offsetY.toInt(),
                    velocityX.toInt(),
                    velocityY.toInt(),
                    (-(bitmap.width * bigScale - width) / 2).toInt(),
                    ((bitmap.width * bigScale - width) / 2).toInt(),
                    (-(bitmap.height * bigScale - height) / 2).toInt(),
                    ((bitmap.height * bigScale - height) / 2).toInt()
                )
                ViewCompat.postOnAnimation(this@ScaleImageView, scaleFlingRunner)
            }
            return false
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            //用户单击时被调用
            //和 onSingleTapUp() 的区别在于，用户的一次点击不会立即调用这个方法，而是在一定时间后（300ms），
            //确认用户没有进行双击，这个方法才会被调用
            return super.onSingleTapConfirmed(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            //用户双击时被调用
            //注意：第二次触摸到屏幕时就调用，而不是抬起时
            big = !big
            if (big) {
                offsetX = (e.x - width / 2f) * (1 - bigScale / smallScale)
                offsetY = (e.y - height / 2f) * (1 - bigScale / smallScale)
                fixOffset()
                scaleAnimator.start()
            } else {
                scaleAnimator.reverse()
            }
            return false
        }

        override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
            //用户双击第二次按下时，第二次按下后移动时、第二次按下后抬起时都会被调用
            //常用于[双击拖拽]的场景
            return super.onDoubleTapEvent(e)
        }
    }


    inner class ScaleGestureListener : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val tempCurrentScale = currentScale * detector.scaleFactor
            return if (tempCurrentScale < smallScale || tempCurrentScale > bigScale) {
                false
            } else {
                currentScale *= detector.scaleFactor
                true
            }

        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            offsetX = (detector.focusX - width / 2f) * (1 - bigScale / smallScale)
            offsetY = (detector.focusY - height / 2f) * (1 - bigScale / smallScale)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {

        }

    }

    inner class ScaleFlingRunner : Runnable {
        override fun run() {
            if (scroller.computeScrollOffset()) {
                offsetX = scroller.currX.toFloat()
                offsetY = scroller.currY.toFloat()
                invalidate()
                ViewCompat.postOnAnimation(this@ScaleImageView, this)
            }
        }

    }
}