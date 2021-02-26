package com.lins.mykotlinlibrary.ui.hencoder.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.imyyq.mvvm.app.dp
import com.lins.mykotlinlibrary.R

/**
 *@CreateTime 2021/1/20 9:46
 *@Describe
 */
class MultiTextView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val text =
        "Ut auctor lectus a massa fringilla, vitae imperdiet nisi faucibus. Donec vitae orci pharetra, tristique risus sed, placerat lorem. In vehicula sollicitudin dolor quis placerat. Aenean ut facilisis tellus. Fusce dictum ultrices nunc, vitae lobortis odio luctus quis. Aenean rutrum, sem non iaculis bibendum, ante dolor feugiat dui, aliquam vulputate eros lectus et tellus. Etiam eu neque et eros commodo feugiat. Nullam ut pretium ante. Sed ullamcorper vestibulum convallis. In felis ligula, imperdiet et eros at, interdum facilisis nisi. Ut bibendum ante ac dui convallis molestie. Maecenas tristique tellus sit amet congue sagittis. Fusce orci erat, commodo accumsan massa quis, convallis interdum neque. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec id mi eget massa scelerisque sollicitudin quis sed eros."

    private val IMAGE_SIZE = 150f.dp
    private val IMAGE_TOP = 80f.dp

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 16.dp
    }

    private val fontMetrics = Paint.FontMetrics()
    private val measuredWidth = floatArrayOf()

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(getBitmap(IMAGE_SIZE.toInt()), width - IMAGE_SIZE, IMAGE_TOP, textPaint)
        textPaint.getFontMetrics(fontMetrics)

        var start = 0
        var verticalOffset = -fontMetrics.top
        var maxWidth: Float
        while (start < text.length) {
            maxWidth = if (verticalOffset + fontMetrics.bottom > IMAGE_TOP
                && verticalOffset + fontMetrics.top < IMAGE_TOP + IMAGE_SIZE
            ) {
                width - IMAGE_SIZE
            } else {
                width.toFloat()
            }
            val count = textPaint.breakText(text, start, text.length, true, maxWidth, measuredWidth)
            canvas.drawText(text, start, start + count, 0f, verticalOffset, textPaint)
            start += count
            verticalOffset += textPaint.fontSpacing
        }
    }

    private fun getBitmap(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.img_avatar, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.img_avatar, options)

    }
}