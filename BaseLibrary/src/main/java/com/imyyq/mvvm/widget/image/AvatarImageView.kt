package com.imyyq.mvvm.widget.image

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class AvatarImageView : AppCompatImageView {
    private var mPaint: Paint? = null

    constructor(context: Context?) : super(context!!) {
        initViews()
    }

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        initViews()
    }

    private fun initViews() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        /**
         * 强制让宽高一致
         */
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val result = Math.min(measuredHeight, measuredWidth)
        setMeasuredDimension(result, result)
    }

    override fun onDraw(canvas: Canvas) {
        val mDrawable = drawable
        val mDrawMatrix = imageMatrix
        if (mDrawable == null) {
            return  // couldn't resolve the URI
        }
        if (mDrawable.intrinsicWidth == 0 || mDrawable.intrinsicHeight == 0) {
            return  // nothing to draw (empty bounds)
        }
        if (mDrawMatrix == null && paddingTop == 0 && paddingLeft == 0) {
            mDrawable.draw(canvas)
        } else {
            val saveCount = canvas.saveCount
            canvas.save()
            if (cropToPadding) {
                val scrollX = scrollX
                val scrollY = scrollY
                canvas.clipRect(
                    scrollX + paddingLeft, scrollY + paddingTop,
                    scrollX + right - left - paddingRight,
                    scrollY + bottom - top - paddingBottom
                )
            }
            canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
            val bitmap = drawable2Bitmap(mDrawable)
            mPaint!!.shader =
                BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            canvas.drawCircle(
                width / 2.toFloat(),
                height / 2.toFloat(),
                width / 2.toFloat(),
                mPaint!!
            )
            canvas.restoreToCount(saveCount)
        }
    }

    /**
     * drawable转换成bitmap
     */
    private fun drawable2Bitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        //根据传递的scaletype获取matrix对象，设置给bitmap
        val matrix = imageMatrix
        if (matrix != null) {
            canvas.concat(matrix)
        }
        drawable.draw(canvas)
        return bitmap
    }
}