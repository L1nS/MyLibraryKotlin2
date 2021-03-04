package com.lins.mykotlinlibrary.ui.layout.slide

import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import com.imyyq.mvvm.base.DataBindingBaseActivity
import com.lins.mykotlinlibrary.R
import com.lins.mykotlinlibrary.common.NoViewModel
import com.lins.mykotlinlibrary.databinding.ActivityLayoutSlidingVerticalBinding
import kotlinx.android.synthetic.main.activity_layout_sliding_vertical.*


/**
 *@CreateTime 2021/3/4 11:10
 *@Describe
 */
class SlidingVerticalLayoutActivity :
    DataBindingBaseActivity<ActivityLayoutSlidingVerticalBinding, NoViewModel>(R.layout.activity_layout_sliding_vertical) {

    private val applyConstraintSet = ConstraintSet()
    private var rlBottomHeight = 0
    private var rlBottomWidth = 0

    private var touchY = 0f

    private var openOffsetY = 0
    private var closeOffsetY = 0
    private var handlerY = 0
    private var handlerHeight = 0
    private var clickHandler = false

    override fun initView() {

        root!!.post {
            println("constraintLayout==width:${root!!.width},height:${root!!.height}")
            closeOffsetY = (root!!.height * 0.8).toInt()
        }
        rlBottom.post {
            val location = IntArray(2)
            rlBottom.getLocationOnScreen(location)
            openOffsetY = rlBottom.top
            handlerY = openOffsetY
            rlBottomHeight = rlBottom.height
            rlBottomWidth = rlBottom.width
            println("rlBottom==x:${location[0]},y:${location[1]}")
            println("rlBottom==width:${rlBottom!!.width},height:${rlBottom!!.height}")
            println("rlBottom==left:${rlBottom.left},top:${rlBottom.top},right:${rlBottom.right},bottom:${rlBottom.bottom}")
        }
        tvTitleBottom.post {
            val location = IntArray(2)
            tvTitleBottom.getLocationOnScreen(location)
            handlerHeight = tvTitleBottom!!.height
            println("tvTitleBottom==x:${location[0]},y:${location[1]}")
            println("tvTitleBottom==width:${tvTitleBottom!!.width},height:${tvTitleBottom!!.height}")
        }
    }

    override fun initData() {


    }

    //上滑覆盖
    private fun open() {
        clickHandler = false
        handlerY = openOffsetY
        TransitionManager.beginDelayedTransition(root)

        applyConstraintSet.constrainWidth(R.id.rlBottom, rlBottomWidth) //设置宽度不变
        applyConstraintSet.constrainHeight(R.id.rlBottom, rlBottomHeight) //设置高度不变
        applyConstraintSet.connect(
            R.id.rlBottom, ConstraintSet.TOP, R.id.root, ConstraintSet.TOP,
            openOffsetY
        ) //设置marginTop
        applyConstraintSet.centerHorizontally(R.id.rlBottom, R.id.root)
        applyConstraintSet.applyTo(root)
    }

    //下滑隐藏
    private fun close() {
        clickHandler = false
        handlerY = closeOffsetY
        TransitionManager.beginDelayedTransition(root)

        applyConstraintSet.constrainWidth(R.id.rlBottom, rlBottomWidth)
        applyConstraintSet.constrainHeight(R.id.rlBottom, rlBottomHeight) //设置高度不变
        applyConstraintSet.connect(
            R.id.rlBottom,
            ConstraintSet.TOP,
            R.id.root,
            ConstraintSet.TOP,
            closeOffsetY
        ) //设置marginBottom
        applyConstraintSet.centerHorizontally(R.id.rlBottom, R.id.root)
        applyConstraintSet.applyTo(root)
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            touchY = event.y
            clickHandler = touchY > handlerY && touchY < handlerY + handlerHeight * 1.5
        }
        if (event.action == MotionEvent.ACTION_UP) {
            if (clickHandler) {
                //当手指离开的时候
                val y2 = event.y
                if (touchY - y2 > 10) { //向上滑动
                    open()
                } else if (y2 - touchY > 10) { //向下滑动
                    close()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}