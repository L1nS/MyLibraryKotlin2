package com.lins.mykotlinlibrary2.manager

import android.text.TextUtils
import com.imyyq.mvvm.app.GlobalConfig
import com.imyyq.mvvm.http.HttpRequest
import com.imyyq.mvvm.utils.SPUtils

class LoginManager {

    private val SP_TOKEN = "token"
    private val SP_UID = "uid"
    private val SP_USERNAME = "username"
    private val SP_SMS_LISTENER_CHECKED = "sms_listener_checked"

    companion object {
        fun getInstance() = Helper.instance
    }

    private object Helper {
        val instance = LoginManager()
    }

    /**
     * 判断用户是否已经登录
     *
     * @return
     */
    fun isLogin(): Boolean {
        val sPUserInfo = SPUtils.getInstance(GlobalConfig.spUserInfo)
        val token = sPUserInfo.getString(SP_TOKEN, "")
        val uid = sPUserInfo.getString(SP_UID, "")
        return !(TextUtils.isEmpty(token) || TextUtils.isEmpty(uid))
    }

    /**
     * 初始化用户登录信息
     */
    fun setLoginData(uid: String, token: String, username: String) {
        val sPUserInfo = SPUtils.getInstance(GlobalConfig.spUserInfo)
        sPUserInfo.put(SP_TOKEN, token)
        sPUserInfo.put(SP_UID, uid)
        sPUserInfo.put(SP_USERNAME, username)
    }

    /**
     * 清空用户信息，重置token
     */
    fun clearUserInfo() {
        SPUtils.getInstance(GlobalConfig.spUserInfo).clear()
        HttpRequest.addDefaultHeader("uid", "")
        HttpRequest.addDefaultHeader("token", "")
    }

    fun getToken(): String {
        val sPUserInfo = SPUtils.getInstance(GlobalConfig.spUserInfo)
        val token = sPUserInfo.getString(SP_TOKEN, "")
        return token
    }

    fun getUId(): String {
        return SPUtils.getInstance(GlobalConfig.spUserInfo).getString(SP_UID, "")
    }

    fun getUsername(): String {
        return SPUtils.getInstance(GlobalConfig.spUserInfo).getString(SP_USERNAME, "")
    }

    fun isSMSListenerChecked(): Boolean {
        return SPUtils.getInstance(GlobalConfig.spAppInfo)
            .getBoolean(SP_SMS_LISTENER_CHECKED, false)
    }

    fun setSMSListenerChecked() {
        SPUtils.getInstance(GlobalConfig.spAppInfo).put(SP_SMS_LISTENER_CHECKED, true)
    }

}