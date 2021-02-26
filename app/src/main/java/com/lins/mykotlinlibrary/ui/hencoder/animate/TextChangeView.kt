package com.lins.mykotlinlibrary.ui.hencoder.animate

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.imyyq.mvvm.app.dp
import kotlinx.android.synthetic.main.activity_main.*

/**
 *@CreateTime 2021/1/26 16:18
 *@Describe
 */
class TextChangeView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {


    val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 30.dp
        textAlign = Paint.Align.CENTER
    }

    var province = "北京"
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(province, width / 2f, height / 2f, paint)


      /*  val animator = ObjectAnimator.ofObject(id_view, "province", TextChangeEvaluator(), "澳门特别行政区")
        animator.duration = 10000
        animator.startDelay = 1500
        animator.start()*/

    }
}

class TextChangeEvaluator : TypeEvaluator<String> {
    override fun evaluate(fraction: Float, startValue: String, endValue: String): String {
        val startIndex = provinceList.indexOf(startValue)
        val endIndex = provinceList.indexOf(endValue)
        val currentIndex = startIndex + ((endIndex - startIndex) * fraction).toInt()
        return provinceList[currentIndex]
    }
}

val provinceList = arrayListOf(
    "北京",
    "天津",
    "河北省",
    "山西省",
    "内蒙古自治区",
    "辽宁省",
    "吉林省",
    "黑龙江省",
    "上海",
    "江苏省",
    "浙江省",
    "安徽省",
    "福建省",
    "江西省",
    "山东省",
    "河南省",
    "湖北省",
    "湖南省",
    "广东省",
    "广西壮族自治区",
    "海南省",
    "重庆",
    "四川省",
    "贵州省",
    "云南省",
    "西藏自治区",
    "陕西省",
    "甘肃省",
    "青海省",
    "宁夏回族自治区",
    "新疆维吾尔自治区",
    "台湾省",
    "香港特别行政区",
    "澳门特别行政区"
)