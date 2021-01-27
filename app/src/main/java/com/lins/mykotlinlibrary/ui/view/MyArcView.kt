package com.lins.mykotlinlibrary.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.lins.mykotlinlibrary.common.px
import kotlin.math.cos
import kotlin.math.sin

class MyArcView: View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dash = Path()
    private val path = Path()
    private lateinit var pathEffect: PathDashPathEffect

    val OPEN_ANGLE = 120F
    //扇形半径
    val CIRCLE_RADIUS = 150f.px
    val LENGTH = 120f.px
    //3.1 虚线宽高
    val DASH_WIDTH = 2f.px
    val DASH_LENGTH = 10f.px

    init {
        //2.画笔风格设置为空心有边框
        paint.strokeWidth = 3f.px
        paint.style = Paint.Style.STROKE
        //3.2 添加刻度效果 矩形刻度条
        dash.addRect(0f,0f, DASH_WIDTH, DASH_LENGTH,Path.Direction.CCW)

    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        path.addArc(width/2f-CIRCLE_RADIUS,height/2-CIRCLE_RADIUS,width/2f+CIRCLE_RADIUS,height/2f+CIRCLE_RADIUS,
            90 + OPEN_ANGLE/2,360- OPEN_ANGLE)
        val pathMeasure = PathMeasure(path,false)
        pathEffect = PathDashPathEffect(dash,(pathMeasure.length - DASH_WIDTH)/20,0f, PathDashPathEffect.Style.ROTATE)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path,paint)

        paint.pathEffect = pathEffect
        canvas.drawPath(path,paint)
        paint.pathEffect = null

        canvas.drawLine(width / 2f, height / 2f,
            width / 2f + LENGTH * cos(markToRadians(8)).toFloat(),
            height / 2f + LENGTH * sin(markToRadians(8)).toFloat(), paint)
    }

    private fun markToRadians(mark: Int) =
        Math.toRadians((90 + OPEN_ANGLE / 2f + (360 - OPEN_ANGLE) / 20f * mark).toDouble())
}