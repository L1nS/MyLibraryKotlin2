package com.imyyq.mvvm.widget.image

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

class SquareImageView : AppCompatImageView {
    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    )


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = widthMeasureSpec
        var height = heightMeasureSpec
        setMeasuredDimension(
            View.getDefaultSize(0, width),
            View.getDefaultSize(0, height)
        )

        // Children are just made to fill our space.
        val childWidthSize: Int = measuredWidth
        val childHeightSize: Int = measuredHeight
        //高度和宽度一样
        width = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY)
        height = width
        super.onMeasure(width, height)
    }
}
