package com.lins.mykotlinlibrary.ui.hencoder.animate

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import com.imyyq.mvvm.app.dp

/**
 * @CreateTime 2021/1/26 15:21
 * @Describe
 */
class PointView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 20.dp
        strokeCap = Paint.Cap.ROUND
    }

    var pointF = PointF(0f, 0f)
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPoint(pointF.x, pointF.y, paint)
//        val animation = ObjectAnimator.ofObject(id_view, "pointF", PointFEvaluator(),PointF(100.dp, 200.dp))
//        animation.startDelay = 1500
//        animation.duration=2000
//        animation.start()
    }
}