package com.imyyq.mvvm.utils.image

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.imyyq.mvvm.utils.LogUtil
import com.imyyq.mvvm.utils.MathUtils
import com.imyyq.mvvm.utils.image.BitmapOptionsUtils.calculateInSampleSize
import com.imyyq.mvvm.utils.image.BitmapOptionsUtils.createScaleBitmap
import java.io.*

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

    fun decodeBitmapFromFile(path: String, reqWidth: Int, reqHeight: Int): Bitmap? {

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

    fun decodeBitmapFromFile(path: String): Bitmap? {

        // 取得图片旋转角度
        val angle: Int = BitmapOptionsUtils.readPictureDegree(path)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        val resultBmp = BitmapFactory.decodeFile(path)
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

    fun bitmapToBase64(bitmap: Bitmap?): String? {
        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos) //这里50表示压缩50%
                baos.flush()
                baos.close()
                val bitmapBytes = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return result
    }
    fun fileToBase64(file: File?): String? {
        var base64: String? = null
        var input: InputStream? = null
        try {
            input = FileInputStream(file)
            val bytes = ByteArray(input.available())
            val length: Int = input.read(bytes)
            base64 = Base64.encodeToString(bytes, 0, length, Base64.NO_WRAP)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                input?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return base64
    }
}