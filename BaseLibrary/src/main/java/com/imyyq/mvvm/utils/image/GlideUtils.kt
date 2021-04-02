package com.imyyq.mvvm.utils.image

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.imyyq.mvvm.R

object GlideUtils {

    private const val TAG = "Glide"

    /**
     * 加载网络图片时候进行缩放
     *
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    fun glideLoadWithScale(
        context: Context,
        url: String,
        imageView: ImageView,
        width: Int,
        height: Int
    ) {
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(width, height)
        Glide.with(context).load(url).apply(options).into(imageView)
    }

    /**
     * 加载网络头像
     */
    fun glideLoadAvatar(
        context: Context?,
        url: String?,
        imageView: ImageView
    ) {
        if (context != null) {
            val options: RequestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(240, 240)
                .placeholder(R.drawable.img_avatar_default)
                .error(R.drawable.img_avatar_default)
            Glide.with(context).load(url).apply(options).into(imageView)
        }
    }
}