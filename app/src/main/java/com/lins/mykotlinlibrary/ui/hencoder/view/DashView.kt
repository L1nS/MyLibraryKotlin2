package com.lins.mykotlinlibrary.ui.hencoder.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.imyyq.mvvm.app.dp
import kotlin.math.cos
import kotlin.math.sin


class DashView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val RADIUS = 100f.dp
    private val ANGLE = 120f
    private val DASH_WIDTH = 2f.dp
    private val DASH_LENGTH = 10f.dp
    private val PHASE = 20
    private val POINTER_LENGTH = 80f.dp
    private val POINTER_INDEX = 5

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var path = Path()
    private var dash = Path()
    private lateinit var pathEffect: PathDashPathEffect

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f.dp
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        path.addArc(
            width / 2f - RADIUS,
            width / 2f - RADIUS,
            width / 2f + RADIUS,
            width / 2f + RADIUS,
            90 + ANGLE / 2f,
            360 - ANGLE
        )
        dash.addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CCW)
        val measure = PathMeasure(path, false)
        pathEffect = PathDashPathEffect(
            dash, (measure.length - DASH_WIDTH) / PHASE, 0f, PathDashPathEffect.Style.ROTATE
        )
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)
        paint.pathEffect = pathEffect
        canvas.drawPath(path, paint)
        paint.pathEffect = null
        canvas.drawLine(
            width / 2f,
            width / 2f,
            (width / 2f + POINTER_LENGTH * cos(Math.toRadians((90 + ANGLE / 2f + (360 - ANGLE) / 20 * POINTER_INDEX).toDouble()))).toFloat(),
            (width / 2f + POINTER_LENGTH * sin(Math.toRadians((90 + ANGLE / 2f + (360 - ANGLE) / 20 * POINTER_INDEX).toDouble()))).toFloat(),
            paint
        )
    }
}