package com.imyyq.mvvm.widget.banner

import com.imyyq.mvvm.widget.banner.bean.SimpleBannerInfo

data class BannerEntity(val imagePath: String) : SimpleBannerInfo() {
    override val xBannerUrl: Any?
        get() = imagePath
}