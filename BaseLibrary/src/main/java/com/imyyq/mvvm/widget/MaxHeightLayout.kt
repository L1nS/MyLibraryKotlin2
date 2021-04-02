package com.imyyq.mvvm.widget

import android.content.Context
import android.util.AttributeSet
import android.view.WindowManager
import android.widget.FrameLayout
import com.imyyq.mvvm.R

class MaxHeightLayout : FrameLayout {
    private var mMaxRatio = DEFAULT_MAX_RATIO // 优先级高
    private var mMaxHeight = DEFAULT_MAX_HEIGHT // 优先级低

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initAttrs(context, attrs)
        init()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.MaxHeightLayout
        )
        val count = a.indexCount
        for (i in 0 until count) {
            val attr = a.getIndex(i)
            if (attr == R.styleable.MaxHeightLayout_mhv_HeightRatio) {
                mMaxRatio = a.getFloat(attr, DEFAULT_MAX_RATIO)
            } else if (attr == R.styleable.MaxHeightLayout_mhv_HeightDimen) {
                mMaxHeight = a.getDimension(attr, DEFAULT_MAX_HEIGHT)
            }
        }
        a.recycle()
    }

    private fun init() {
        mMaxHeight = if (mMaxHeight <= 0) {
            mMaxRatio * getScreenHeight(context).toFloat()
        } else {
            Math.min(mMaxHeight, mMaxRatio * getScreenHeight(context).toFloat())
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode == MeasureSpec.EXACTLY) {
            heightSize = if (heightSize <= mMaxHeight) heightSize else mMaxHeight.toInt()
        }
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = if (heightSize <= mMaxHeight) heightSize else mMaxHeight.toInt()
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = if (heightSize <= mMaxHeight) heightSize else mMaxHeight.toInt()
        }
        val maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
            heightSize,
            heightMode
        )
        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec)
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     */
    private fun getScreenHeight(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return wm.defaultDisplay.height
    }

    companion object {
        private const val DEFAULT_MAX_RATIO = 0.6f
        private const val DEFAULT_MAX_HEIGHT = 0f
    }
}