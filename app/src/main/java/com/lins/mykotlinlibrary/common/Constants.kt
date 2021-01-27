package com.lins.mykotlinlibrary.common

object Constants {
    //本地环境
    private const val HTTP_URL_LOCAL = "http://103.231.255.121"

    //线上环境
    private const val HTTP_URL_RELEASE = "https://www.studytour.info"

    const val HTTP_URL = HTTP_URL_LOCAL

    //SDK
    const val SDK_WE_CHAT = 1
    const val SDK_QQ = 2


    //分页数量
    const val PAGE_COUNT = 10

    //性别
    const val SEX_TYPE_MALE = 1
    const val SEX_TYPE_FEMALE = 0


    const val REQUEST_ACTION = 1

    //退出登录
    const val RESULT_LOGOUT = 1
    const val RESULT_OK = 2
    const val RESULT_FAIL = 3

    //Robot type
    const val Robot_Level_USDT = 1
    const val Robot_Level_ETH = 2
    const val Robot_Level_BTC = 3

}