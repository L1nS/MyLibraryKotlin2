package com.lins.mykotlinlibrary.ui.hencoder.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.imyyq.mvvm.app.dp
import kotlin.math.cos
import kotlin.math.sin


class PieView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val angles = floatArrayOf(60f, 45f, 125f, 130f)
    private val colors = listOf(
        Color.parseColor("#FFC90E"),
        Color.parseColor("#00A2E8"),
        Color.parseColor("#A349A4"),
        Color.parseColor("#7F7F7F")
    )
    private val RADIUS = 100f.dp
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var offset = 10f.dp

    init {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

    }


    override fun onDraw(canvas: Canvas) {
        var angleStart = 0f
        for ((index, angle) in angles.withIndex()) {
            paint.color = colors[index]
            if (index == 2) {
                canvas.save()
                canvas.translate(
                    (offset * cos(Math.toRadians((angleStart + angle / 2).toDouble()))).toFloat(),
                    (offset * sin(Math.toRadians((angleStart + angle / 2).toDouble()))).toFloat()
                )
            }
            canvas.drawArc(
                width / 2f - RADIUS,
                width / 2f - RADIUS,
                width / 2f + RADIUS,
                width / 2f + RADIUS,
                angleStart,
                angle,
                true,
                paint
            )
            angleStart += angle
            if (index == 2) {
                canvas.restore()
            }
        }
    }
}