package com.imyyq.mvvm.widget.loading

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.imyyq.mvvm.R

/**
 * Created by helin on 8/19/15.
 */
class LoadingLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        val EMPTY = 0
        val ERROR = 1
        val SUCCESS = 2
        val LOADING = 3
    }

    /**
     * 空数据View
     */
    private var mEmptyView = 0

    /**
     * 状态View
     */
    private var mErrorView = 0

    /**
     * 加载View
     */
    private var mLoadingView = 0

    /**
     * 网络错误View
     */
    private var mNetWorkErrorView = 0

    /**
     * 布局加载完成后隐藏所有View
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        for (i in 0 until childCount - 1) {
            getChildAt(i).visibility = View.GONE
        }
    }

    fun setRetryListener(listener: (View) -> Unit) {
        findViewById<View>(R.id.load_empty_retry).setOnClickListener(listener)
        findViewById<View>(R.id.load_error_retry).setOnClickListener(listener)
        findViewById<View>(R.id.load_network_error_retry).setOnClickListener(listener)
        findViewById<View>(R.id.load_empty_retry).setOnClickListener(listener)
    }

    /**
     * 设置自定义布局的点击事件
     *
     * @param resourceId
     * @param listener
     */
    fun setViewOnClickListener(
        resourceId: Int,
        listener: OnClickListener?
    ) {
        findViewById<View>(resourceId).setOnClickListener(listener)
    }

    /**
     * 设置自定义布局的view文本
     *
     * @param resoureId
     * @param text
     */
    fun setViewText(resoureId: Int, text: String?) {
        (findViewById<View>(resoureId) as TextView).text = text
    }

    /**
     * 设置自定义布局的image
     *
     * @param resoureId
     * @param img
     */
    fun setViewImage(resoureId: Int, img: Int) {
        (findViewById<View>(resoureId) as ImageView).setImageResource(
            img
        )
    }

    /**
     * Empty view
     */
    fun showEmpty() {
        showEmpty("")
    }


    /**
     * Empty view
     *
     * @param text
     */
    fun showEmpty(text: String, imgRes: Int? = null) {
        for (i in 0 until this.childCount) {
            val child = getChildAt(i)
            if (i == 2) {
                child.visibility = View.VISIBLE
                if (!TextUtils.isEmpty(text))
                    (child.findViewById<View>(R.id.load_empty_text) as TextView).text = text
                if (imgRes != null) {
                    (child.findViewById<View>(R.id.load_empty_img) as ImageView).setImageResource(
                        imgRes
                    )
                }
            } else {
                child.visibility = View.GONE
            }
        }
    }

    /**
     * Loading view
     */
    fun showLoading() {
        showLoading("")
    }

    /**
     * @param text
     */
    fun showLoading(text: String) {
        for (i in 0 until this.childCount) {
            val child = getChildAt(i)
            if (i == 1) {
                child.visibility = View.VISIBLE
                if (!TextUtils.isEmpty(text))
                    (child.findViewById<View>(R.id.loading_text) as TextView).text = text
            } else {
                child.visibility = View.GONE
            }
        }
    }


    /**
     * State View
     */
    fun showError() {
        showError(0, "")
    }

    /**
     * @param tips
     */
    fun showError(text: String) {
        showError(0, text)
    }

    fun showError(imgId: Int, text: String) {
        for (i in 0 until this.childCount) {
            val child = getChildAt(i)
            if (i == 0) {
                child.visibility = View.VISIBLE
                if (imgId != 0)
                    (child.findViewById<View>(R.id.load_error_img) as ImageView).setImageResource(
                        imgId
                    )
                if (!TextUtils.isEmpty(text))
                    (child.findViewById<View>(R.id.load_error_tv) as TextView).text = text
            } else {
                child.visibility = View.GONE
            }
        }
    }

    /**
     * 展示内容
     */
    fun showContent() {
        for (i in 0 until this.childCount) {
            val child = getChildAt(i)
            if (i > 3) {
                child.visibility = View.VISIBLE
            } else {
                child.visibility = View.GONE
            }
        }
    }

    /**
     * 展示无网络
     */
    fun showNetworkError() {
        for (i in 0 until this.childCount) {
            val child = getChildAt(i)
            if (i == 3) {
                child.visibility = View.VISIBLE
            } else {
                child.visibility = View.GONE
            }
        }
    }

    /**
     * 判断content是否展示。
     *
     * @return
     */
    val isShowContent: Boolean
        get() {
            var flag = false
            for (i in 0 until this.childCount) {
                val child = getChildAt(i)
                if (i > 3) {
                    if (child.visibility == View.VISIBLE) flag = true
                    break
                }
            }
            return flag
        }

    init {
        val a =
            context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingLayout, 0, 0)
        try {
            mErrorView = a.getResourceId(
                R.styleable.LoadingLayout_errorView,
                R.layout.widget_loadlayout_error
            )
            mLoadingView = a.getResourceId(
                R.styleable.LoadingLayout_loadingView,
                R.layout.widget_loadlayout_loading
            )
            mEmptyView = a.getResourceId(
                R.styleable.LoadingLayout_emptyView,
                R.layout.widget_loadlayout_empty
            )
            mNetWorkErrorView = a.getResourceId(
                R.styleable.LoadingLayout_emptyView,
                R.layout.widget_loadlayout_network_error
            )
            val inflater = LayoutInflater.from(getContext())
            inflater.inflate(mErrorView, this, true)
            inflater.inflate(mLoadingView, this, true)
            inflater.inflate(mEmptyView, this, true)
            inflater.inflate(mNetWorkErrorView, this, true)
        } finally {
            a.recycle()
        }
    }
}