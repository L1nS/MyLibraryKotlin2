package com.imyyq.mvvm.utils.image

import android.content.res.Resources
import android.graphics.*
import android.view.View
import android.webkit.WebView
import androidx.core.widget.NestedScrollView
import com.imyyq.mvvm.utils.LogUtil
import com.imyyq.mvvm.utils.MathUtils
import com.imyyq.mvvm.utils.image.BitmapOptionsUtils.calculateInSampleSize
import com.imyyq.mvvm.utils.image.BitmapOptionsUtils.createScaleBitmap
import java.io.ByteArrayOutputStream


object BitmapDecodeUtils {
    fun decodeBitmapFromRes(res: Resources?, path: Int, reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, path, options)
        //计算原图片的宽高比
        val widthScale = widthScaleWithHeight(options)
        options.inSampleSize = calculateInSampleSize(
            options, reqWidth,
            reqHeight
        )
        options.inJustDecodeBounds = false
        //获取缩略图
        val bmp = BitmapFactory.decodeResource(res, path, options)
        //按照控件比例进行缩放
        val resultBmp = createScaleBitmap(bmp, reqHeight, widthScale)
        bmp.recycle()
        return resultBmp
    }

    fun decodeBitmapFromFile(path: String?, reqWidth: Int, reqHeight: Int): Bitmap? {

        // 取得图片旋转角度
        val angle: Int = BitmapOptionsUtils.readPictureDegree(path)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        //计算原图片的宽高比
        val widthScale = widthScaleWithHeight(options)
        options.inSampleSize = calculateInSampleSize(
            options, reqWidth,
            reqHeight
        )
        options.inJustDecodeBounds = false
        //获取缩略图
        val bmp = BitmapFactory.decodeFile(path, options)
        //按照控件比例进行缩放
        val resultBmp = createScaleBitmap(bmp, reqHeight, widthScale)
        bmp.recycle()
        return if (angle == 0) {
            resultBmp
        } else {
            // 修复图片被旋转的角度
            BitmapOptionsUtils.rotatingImageView(angle, resultBmp!!)
        }
    }

    fun widthScaleWithHeight(options: BitmapFactory.Options): Double {
        val width = options.outWidth.toDouble()
        val height = options.outHeight.toDouble()
        return MathUtils.div(width, height, 2)
    }

    fun calculateBitmapSize(bmp: Bitmap) {
        var bmSize = 0
        bmSize += bmp.byteCount // 得到bitmap的大小
        val kb = bmSize / 1024
        val mb = kb / 1024
        LogUtil.d("bitmap size = " + mb + "MB" + "  ," + kb + "KB")
    }

    fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray? {
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output)
        if (needRecycle) {
            bmp.recycle()
        }
        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    //控件生成图片
    fun createViewBitmap(v: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            v.width, v.height,
            Bitmap.Config.ARGB_8888
        ) //创建一个和View大小一样的Bitmap
        val canvas = Canvas(bitmap) //使用上面的Bitmap创建canvas
        v.draw(canvas) //把View画到Bitmap上
        return bitmap
    }

    //ScrollView 生成图片
    fun getBitmapByView(scrollView: NestedScrollView): Bitmap? {
        var h = 0
        var bitmap: Bitmap? = null
        for (i in 0 until scrollView.childCount) {
            h += scrollView.getChildAt(i).height
        }
        bitmap = Bitmap.createBitmap(scrollView.width, h, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        scrollView.draw(canvas)
        return bitmap
    }


    /**
     *
    override fun onCreate(savedInstanceState: Bundle?) {
    /*在android5.0及以上版本使用webView进行截长图时,默认是截取可是区域内的内容.因此需要在支撑窗体内容之前加上*/
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    WebView.enableSlowWholeDocumentDraw()
    }
    super.onCreate(savedInstanceState)
    }

    if (FileStoreUtils.saveImageToGallery(BitmapDecodeUtils.getBitmapFromWebView(mBinding.idWebview))) {
    /*重新加载页面*/
    finish()
    startActivity(intent)
    }
     */
    fun getBitmapFromWebView(webView: WebView): Bitmap? {
        return try {
            val widthMeasureSpec: Int = View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )
            val heightMeasureSpec: Int =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

            //Measure WebView's content
            webView.measure(widthMeasureSpec, heightMeasureSpec)
            webView.layout(0, 0, webView.measuredWidth, webView.measuredHeight)

            //Build drawing cache and store its size
            webView.buildDrawingCache(true)
            val measuredWidth = webView.measuredWidth
            val measuredHeight = webView.measuredHeight

            //Creates the bitmap and draw WebView's content on in
            val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
            val paint = Paint()
            val canvas = Canvas(bitmap)
            canvas.drawBitmap(bitmap, 0f, bitmap.height.toFloat(), paint)
            webView.draw(canvas)
            webView.destroyDrawingCache()
            bitmap
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            null
        }
    }
}