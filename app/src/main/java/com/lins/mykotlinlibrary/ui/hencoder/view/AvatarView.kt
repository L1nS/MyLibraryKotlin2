package com.lins.mykotlinlibrary.ui.hencoder.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.imyyq.mvvm.app.dp
import com.lins.mykotlinlibrary.R

class AvatarView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val STROKE_WIDTH = 5f.dp
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bound = RectF()
    private var xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        bound.set(STROKE_WIDTH, STROKE_WIDTH, width - STROKE_WIDTH, width - STROKE_WIDTH)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawOval(0f, 0f, width.toFloat(), width.toFloat(), paint)
        val count = canvas.saveLayer(bound, paint)
        paint.color = Color.parseColor("#ffffff")
        canvas.drawOval(
            STROKE_WIDTH,
            STROKE_WIDTH,
            width-STROKE_WIDTH,
            width-STROKE_WIDTH,
            paint
        )
        paint.xfermode = xfermode
        canvas.drawBitmap(getBitmap(width), STROKE_WIDTH, STROKE_WIDTH, paint)
        paint.xfermode = null
        canvas.restoreToCount(count)
    }

    private fun getBitmap(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true //只解析图片边沿，获取宽高
        BitmapFactory.decodeResource(resources, R.drawable.img_avatar, options)
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.img_avatar, options)

    }
}