package com.imyyq.mvvm.widget.banner

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.imyyq.mvvm.R
import com.imyyq.mvvm.widget.banner.bean.SimpleBannerInfo
import com.zhpan.bannerview.BaseViewHolder

class NetViewHolder<T: SimpleBannerInfo> :BaseViewHolder<T>{

    constructor(itemView: View) : super(itemView){
//        val imageView = findView<ImageView>(R.id.id_banner_image)
    }

    override fun bindData(data: T?, position: Int, pageSize: Int) {
        val imageView = findView<ImageView>(R.id.id_banner_image)
        if (data != null) {
            Glide.with(imageView).load(data.xBannerUrl).into(imageView)
        }
    }
}