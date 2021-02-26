package com.lins.mykotlinlibrary.ui.hencoder.animate

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.imyyq.mvvm.app.dp

/**
 *@CreateTime 2021/1/26 14:54
 *@Describe
 */
class ScaleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#22B14C")
    }

    var radius = 80.dp
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)

//        val animation = ObjectAnimator.ofFloat(id_view, "radius", 180.dp)
//        animation.startDelay = 1000
//        animation.start()
    }
}