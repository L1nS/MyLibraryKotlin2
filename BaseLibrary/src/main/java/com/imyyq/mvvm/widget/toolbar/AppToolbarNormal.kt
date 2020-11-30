package com.imyyq.mvvm.widget.toolbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.imyyq.mvvm.R
import com.imyyq.mvvm.utils.DensityUtil
import com.imyyq.mvvm.utils.SystemUIUtil

class AppToolbarNormal : RelativeLayout {

    private val RIGHT_SHOW_BUTTON = 1
    private val RIGHT_SHOW_TEXT = 2
    private val RIGHT_SHOW_IMAGE = 3
    private var background = 0

    private var backShow = true
    private var backDrawable: Drawable? = null

    //标题
    private var titleText = ""
    private var titleTextColor = 0

    //右边按钮
    private var rightShow = 0
    private var rightButtonText = ""
    private var rightButtonDrawable: Drawable? = null

    //右边按钮
    private var rightTextText = ""
    private var rightTextColor = 0

    private var rightImageDrawable: Drawable? = null

    //底部线条
    private var bottomLineShow = false
    private var bottomLineColor = 0

    private var fitsSystemWindow = false
    private var marginStatusHeight = 0f

    private lateinit var idRoot: FrameLayout
    private lateinit var idRlToolbar: RelativeLayout
    private lateinit var idImgBack: ImageView
    private lateinit var idTvTitle: TextView
    private lateinit var idBtnRight: TextView
    private lateinit var idTvRight: TextView
    private lateinit var idImgRight: ImageView
    private lateinit var idVLine: View

    constructor(context: Context?) : super(context)
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {

        //加载视图的布局
        LayoutInflater.from(context).inflate(R.layout.app_toolbar_normal, this, true)

        //加载自定义的属性
        val a = context.obtainStyledAttributes(attrs, R.styleable.AppToolbarNormal)
        backShow = a.getBoolean(R.styleable.AppToolbarNormal_backShow, true)
        backDrawable = a.getDrawable(R.styleable.AppToolbarNormal_backDrawable)
        background = a.getColor(
            R.styleable.AppToolbarNormal_toolbarBackground,
            ContextCompat.getColor(context, R.color.colorTransparent)
        )
        var text = a.getString(R.styleable.AppToolbarNormal_titleText)
        if (text == null)
            text = ""
        titleText = text
        titleTextColor = a.getColor(
            R.styleable.AppToolbarNormal_titleTextColor,
            ContextCompat.getColor(context, R.color.colorTextBlack)
        )
        rightShow = a.getInt(R.styleable.AppToolbarNormal_rightShow, 0)
        rightButtonText = a.getString(R.styleable.AppToolbarNormal_rightButtonText).toString()
        rightButtonDrawable = a.getDrawable(R.styleable.AppToolbarNormal_rightButtonDrawable)
        rightTextText = a.getString(R.styleable.AppToolbarNormal_rightTextText).toString()
        rightTextColor = a.getColor(
            R.styleable.AppToolbarNormal_rightTextColor,
            ContextCompat.getColor(context, R.color.colorTheme)
        )
        rightImageDrawable = a.getDrawable(R.styleable.AppToolbarNormal_rightImageDrawable)
        bottomLineShow = a.getBoolean(R.styleable.AppToolbarNormal_bottomLineShow, false)
        bottomLineColor = a.getColor(
            R.styleable.AppToolbarNormal_bottomLineColor,
            ContextCompat.getColor(context, R.color.colorLineGray)
        )
        fitsSystemWindow = a.getBoolean(R.styleable.AppToolbarNormal_fitsSystemWindow, false)
        marginStatusHeight = a.getDimension(R.styleable.AppToolbarNormal_marginStatusHeight, 0f)
        //回收资源，这一句必须调用
        a.recycle()
    }

    /**
     * 此方法会在所有的控件都从xml文件中加载完成后调用
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        idRoot = findViewById(R.id.id_root)
        idRlToolbar = findViewById(R.id.id_rl_toolbar)
        idImgBack = findViewById(R.id.id_img_back)
        idTvTitle = findViewById(R.id.id_tv_title)
        idBtnRight = findViewById(R.id.id_tv_right_btn)
        idTvRight = findViewById(R.id.id_tv_right)
        idImgRight = findViewById(R.id.id_img_right)
        idVLine = findViewById(R.id.id_v_line)

//        idRoot.fitsSystemWindows = fitsSystemWindow
        idRoot.setBackgroundColor(background)
        if (fitsSystemWindow) {
            val lp = idRlToolbar.layoutParams as FrameLayout.LayoutParams
            lp.width = FrameLayout.LayoutParams.MATCH_PARENT
            lp.height =
                SystemUIUtil.getStatusBarHeight() + DensityUtil.dp2px(48f)
            idRlToolbar.layoutParams = lp
        }
        idTvTitle.text = titleText
        idTvTitle.setTextColor(titleTextColor)
        if (backShow)
            idImgBack.visibility = VISIBLE
        else
            idImgBack.visibility = GONE
        if (backDrawable != null)
            idImgBack.setImageDrawable(backDrawable)
        when (rightShow) {
            RIGHT_SHOW_BUTTON -> {
                idBtnRight.visibility = View.VISIBLE
                idBtnRight.text = rightButtonText
                idBtnRight.background = rightButtonDrawable
            }
            RIGHT_SHOW_TEXT -> {
                idTvRight.visibility = View.VISIBLE
                idTvRight.text = rightTextText
                idTvRight.setTextColor(rightTextColor)
            }
            RIGHT_SHOW_IMAGE -> {
                idImgRight.visibility = View.VISIBLE
                idImgRight.setImageDrawable(rightImageDrawable)
            }
            else -> {
                idBtnRight.visibility = View.GONE
                idTvRight.visibility = View.GONE
                idImgRight.visibility = View.GONE
            }
        }

        if (bottomLineShow) {
            idVLine.visibility = View.VISIBLE
            idVLine.setBackgroundColor(bottomLineColor)
        } else
            idVLine.visibility = View.GONE

        idImgBack.setOnClickListener { thisBack?.let { it1 -> it1() } }
        idBtnRight.setOnClickListener { thisRightBtn?.let { it1 -> it1() } }
        idTvRight.setOnClickListener { thisRightBtn?.let { it1 -> it1() } }
        idImgRight.setOnClickListener { thisRightBtn?.let { it1 -> it1() } }
    }

    /**
     * 外部方法
     */
    fun setTitle(text: String) {
        idTvTitle.text = text
    }

    fun setRightText(text: String) {
        idTvRight.visibility = View.VISIBLE
        idTvRight.text = text
    }

    fun setRightText(text: String, color: Int) {
        idTvRight.visibility = View.VISIBLE
        idTvRight.text = text
        idTvRight.setTextColor(color)
    }

    private var thisBack: (() -> Unit)? = null
    private var thisRightBtn: (() -> Unit)? = null

    fun setAppToolbarClickListener(back: (() -> Unit)? = null, rightBtn: (() -> Unit)? = null) {
        thisBack = back
        thisRightBtn = rightBtn
    }
}