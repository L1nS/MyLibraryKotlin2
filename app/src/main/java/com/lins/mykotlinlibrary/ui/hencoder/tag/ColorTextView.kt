package com.lins.mykotlinlibrary.ui.hencoder.tag

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.imyyq.mvvm.app.dp
import java.util.*

private val COLORS = intArrayOf(
    Color.parseColor("#ED1C24"),
    Color.parseColor("#FF7F27"),
    Color.parseColor("#22B14C"),
    Color.parseColor("#3F48CC"),
    Color.parseColor("#B97A57"),
    Color.parseColor("#B5E61D"),
    Color.parseColor("#99D9EA"),
    Color.parseColor("#C8BFE7")
)

private val TEXT_SIZES = intArrayOf(12, 16, 20)
private val PADDING_W = 12.dp.toInt()
private val PADDING_H = 6.dp.toInt()
private val CORNER_RADIUS = 4.dp


/**
 *@CreateTime 2021/2/19 10:29
 *@Describe
 */
class ColorTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val random = Random()

    init {
        setTextColor(Color.WHITE)
        textSize = TEXT_SIZES[random.nextInt(3)].toFloat()
        paint.color = COLORS[random.nextInt(COLORS.size)]
        setPadding(PADDING_W, PADDING_H, PADDING_W, PADDING_H)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), CORNER_RADIUS, CORNER_RADIUS, paint)
        super.onDraw(canvas)
    }
}