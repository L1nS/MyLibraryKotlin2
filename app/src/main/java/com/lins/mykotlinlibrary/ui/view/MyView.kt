package com.lins.mykotlinlibrary.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.lins.mykotlinlibrary.common.px

class MyView : View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var path=Path()
    private val RADIUS =50f.px

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(path,paint)
//        canvas?.drawLine(100f, 100f, 200f, 200f,paint)
//        canvas?.drawCircle(width/2f,200f,RADIUS,paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        //画圆
        path.addCircle(width/2f,200f,RADIUS,Path.Direction.CW)
        //画方
        path.addRect(width/2f-RADIUS,200f,width/2f+RADIUS,200f+2*RADIUS,Path.Direction.CCW)
        //画圆
        path.addCircle(width/2f,200f,RADIUS*1.5f,Path.Direction.CW)
        path.fillType=Path.FillType.EVEN_ODD
    }
}