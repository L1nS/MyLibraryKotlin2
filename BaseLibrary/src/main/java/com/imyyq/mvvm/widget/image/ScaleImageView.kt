package com.imyyq.mvvm.widget.image

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.imyyq.mvvm.R

class ScaleImageView : RelativeLayout {
    lateinit var imageView: ImageView
        private set
    private lateinit var idCardView: CardView
    private var idConstraintLayout: ConstraintLayout? = null

    //设定宽高比例
    private var dimensionRatioWidth = 0f
    private var dimensionRatioHeight = 0f
    private val default_dimension_ratio = 1.0f

    //CardView的角度
    private var cardCornerRadius = 0f

    constructor(context: Context?) : super(context)
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {

        //加载视图的布局
        LayoutInflater.from(context).inflate(R.layout.widget_scale_imageview, this, true)

        //加载自定义的属性
        val a = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView)
        dimensionRatioWidth =
            a.getFloat(R.styleable.ScaleImageView_dimensionRatioWidth, default_dimension_ratio)
        dimensionRatioHeight =
            a.getFloat(R.styleable.ScaleImageView_dimensionRatioHeight, default_dimension_ratio)
        cardCornerRadius = a.getDimension(R.styleable.ScaleImageView_cornerRadius, 0f)
        //回收资源，这一句必须调用
        a.recycle()
    }

    /**
     * 此方法会在所有的控件都从xml文件中加载完成后调用
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        imageView =
            findViewById<View>(R.id.widget_scaleimageview_img) as ImageView
        idCardView = findViewById(R.id.widget_scaleimageview_cardView)
        idConstraintLayout = findViewById(R.id.id_constraint_layout)
        if (dimensionRatioHeight != default_dimension_ratio || dimensionRatioWidth != default_dimension_ratio) setImageViewLayoutParams()
        idCardView.radius=cardCornerRadius
    }

    /**
     * 初始化宽高比例
     */
    private fun setImageViewLayoutParams() {
        val imageViewParams = ConstraintLayout.LayoutParams(0, 0)
        imageViewParams.dimensionRatio = "h,$dimensionRatioWidth:$dimensionRatioHeight"
        imageViewParams.leftToLeft = R.id.id_constraint_layout
        imageViewParams.rightToRight = R.id.id_constraint_layout
        imageViewParams.topToTop = R.id.id_constraint_layout
        idCardView.layoutParams = imageViewParams
    }

    /**
     * 自定义宽高比例
     *
     * @param ratioWidth
     * @param ratioHeight
     */
    fun initDimensionRatio(ratioWidth: Float, ratioHeight: Float) {
        dimensionRatioHeight = ratioHeight
        dimensionRatioWidth = ratioWidth
        setImageViewLayoutParams()
    }

    fun initDimensionRatio(ratioHeight: Float) {
        initDimensionRatio(1f, ratioHeight)
    }

    /**
     * 设置按钮点击事件监听器
     *
     * @param listener
     */
    fun setOnItemClickListener(listener: OnClickListener?) {
        imageView.setOnClickListener(listener)
    }
}