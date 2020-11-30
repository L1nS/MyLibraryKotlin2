package com.lins.mykotlinlibrary2.common

import com.imyyq.mvvm.app.BaseApp
import com.imyyq.mvvm.http.HttpRequest
import com.imyyq.mvvm.app.GlobalConfig
import com.imyyq.mvvm.utils.LogUtil
import com.lins.mykotlinlibrary2.R
import com.lins.mykotlinlibrary2.manager.LoginManager
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter


class MyApp : BaseApp() {

    companion object {

        lateinit var loginManager: LoginManager
    }

    init {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.colorLoading, R.color.colorWhite) //全局设置主题颜色
            MaterialHeader(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }

    override fun onCreate() {
        initGlobal()
        super.onCreate()
        initUserManager()
        LogUtil.init()
        // 网络请求需设置 baseUrl
        HttpRequest.mDefaultBaseUrl = Constants.HTTP_URL

    }

    private fun initGlobal() {
        GlobalConfig.gIsNeedLoadingDialog = true
        GlobalConfig.gIsNeedChangeBaseUrl = true
        GlobalConfig.gIsNeedActivityManager = true
    }

    private fun initUserManager() {
        loginManager = LoginManager.getInstance()
    }

}