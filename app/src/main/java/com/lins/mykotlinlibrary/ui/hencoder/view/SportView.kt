package com.lins.mykotlinlibrary.ui.hencoder.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.imyyq.mvvm.app.dp

/**
 *@CreateTime 2021/1/20 9:15
 *@Describe
 */
class SportView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val RADIUS = 150f.dp

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f.dp
    }
    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 100f.dp
        color = Color.parseColor("#EF7AA1")
        textAlign = Paint.Align.CENTER
    }

    private val fontMetrics = Paint.FontMetrics()

    override fun onDraw(canvas: Canvas) {

        //画圆环
        paint.color = Color.parseColor("#7F7F7F")
        canvas.drawCircle(
            width / 2f,
            height / 2f,
            RADIUS,
            paint
        )

        //画进度条
        paint.color = Color.parseColor("#EF7AA1")
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(
            width / 2f - RADIUS,
            height / 2f - RADIUS,
            width / 2f + RADIUS,
            height / 2f + RADIUS,
            200f,
            300f,
            false,
            paint
        )

        //画文字
        textPaint.getFontMetrics(fontMetrics)
        canvas.drawText(
            "abab",
            0,
            "abab".length,
            width / 2f,
            height / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2,
            textPaint
        )
    }
}