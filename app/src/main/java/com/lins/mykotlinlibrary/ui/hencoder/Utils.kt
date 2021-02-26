package com.lins.mykotlinlibrary.ui.hencoder

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.lins.mykotlinlibrary.R

/**
 *@CreateTime 2021/2/26 14:32
 *@Describe
 */

fun getBitmap(res:Resources,width: Int): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, R.drawable.img_avatar, options)
    options.inJustDecodeBounds = false
    options.inDensity = options.outWidth
    options.inTargetDensity = width
    return BitmapFactory.decodeResource(res, R.drawable.img_avatar, options)
}