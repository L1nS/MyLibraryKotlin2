package com.imyyq.mvvm.widget.banner

import android.view.View
import com.imyyq.mvvm.R
import com.imyyq.mvvm.widget.banner.bean.SimpleBannerInfo
import com.zhpan.bannerview.BaseBannerAdapter

class BannerNormalAdapter<T : SimpleBannerInfo>: BaseBannerAdapter<T, NetViewHolder<T>>() {
    override fun createViewHolder(itemView: View?, viewType: Int): NetViewHolder<T> {
        return NetViewHolder(itemView!!)
    }

    override fun onBind(holder: NetViewHolder<T>?, data: T?, position: Int, pageSize: Int) {
        holder!!.bindData(data, position, pageSize)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_banner_image
    }

}