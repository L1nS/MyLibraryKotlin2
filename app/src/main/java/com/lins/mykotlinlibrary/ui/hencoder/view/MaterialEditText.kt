package com.lins.mykotlinlibrary.ui.hencoder.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.imyyq.mvvm.app.dp
import com.lins.mykotlinlibrary.R

/**
 *@CreateTime 2021/2/2 9:04
 *@Describe
 */

private val TEXT_SIZE = 12.dp
private val TEXT_PADDING = 10.dp
private val HORIZONTAL_OFFSET = 5.dp
private val VERTICAL_OFFSET = 23.dp
private val EXTRA_VERTICAL_OFFSET = 16.dp

class MaterialEditText(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    private var floatingLabelFraction = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var floatingLabelShown = false
    private var useFloatingLabel = false
        set(value) {
            if (field != value) {
                field = value
                if (field) {
                    setPadding(paddingStart, (paddingTop + TEXT_PADDING + TEXT_SIZE).toInt(), paddingEnd, paddingBottom)
                } else {
                    setPadding(paddingStart, (paddingTop - TEXT_PADDING - TEXT_SIZE).toInt(), paddingEnd, paddingBottom)
                }
            }
        }
    private val animator by lazy {
        ObjectAnimator.ofFloat(this, "floatingLabelFraction", 1f)
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE
        color = Color.parseColor("#ff0000")
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText)
        useFloatingLabel = typedArray.getBoolean(R.styleable.MaterialEditText_useFloatingLabel, true)
        typedArray.recycle()
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        if (floatingLabelShown && text.isNullOrEmpty()) {
            animator.reverse()
            floatingLabelShown = false
        } else if (!floatingLabelShown && !text.isNullOrEmpty()) {
            floatingLabelShown = true
            animator.start()
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (useFloatingLabel) {
            paint.alpha = (floatingLabelFraction * 0xff).toInt()
            val currentVerticalOffset = VERTICAL_OFFSET + EXTRA_VERTICAL_OFFSET * (1 - floatingLabelFraction)
            canvas.drawText(hint as String, HORIZONTAL_OFFSET, currentVerticalOffset, paint)
        }
    }
}