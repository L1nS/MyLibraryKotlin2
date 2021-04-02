package com.imyyq.mvvm.utils

import android.text.TextUtils
import java.util.regex.Pattern

object StringUtils {

    /**
     * 手机号
     *
     * @param str
     * @return
     */
    fun isPhone(str: String?): Boolean {
        if (TextUtils.isEmpty(str))
            return false
        val pattern = Pattern.compile("[1][3456789]\\d{9}")
        val matcher = pattern.matcher(str)
        return matcher.matches()
    }

    fun isPassword(str: String?): Boolean {
        if (TextUtils.isEmpty(str))
            return false
        val pattern = Pattern.compile("^([A-Z]|[a-z]|[0-9]){6,20}\$")
        val matcher = pattern.matcher(str)
        return matcher.matches()
    }

    /**
     * 6-20位密码,包含数字和字母
     */
    fun isPasswordContainLetterAndDigital(str: String?): Boolean {
        if (str.isNullOrEmpty())
            return false
        var isDigit = false
        var isLetter = false
        str.forEach {
            if (Character.isDigit(it))
                isDigit = true
            else if (Character.isLetter(it))
                isLetter = true
        }
        val pattern = Pattern.compile("^([A-Za-z0-9]){6,20}\$")
        val matcher = pattern.matcher(str)
        return isDigit && isLetter && matcher.matches()
    }

    /**
     * 身份证号
     *
     * @param str
     * @return
     */
    fun isIDCard(str: String?): Boolean {
        val pattern = Pattern.compile("[\\d]{17}[0-9Xx]")
        val matcher = pattern.matcher(str)
        return matcher.matches()
    }

    fun subString(str: String, beginIndex: Int, endIndex: Int): String {
        var begin = beginIndex
        var end = endIndex
        if (TextUtils.isEmpty(str))
            return ""
        if (begin > end)
            return str
        if (begin < 0)
            begin = 0
        if (end > str.length)
            end = str.length
        return str.substring(begin, end)
    }

    fun covertNull2String(str: Any?): String {
        var result = ""
        if (str != null)
            result = str.toString()
        return result
    }

    fun append(vararg values: String): String {
        var str = ""
        values.forEach {
            if (!TextUtils.isEmpty(it)) {
                str += "$it/"
            }
        }
        if (str.endsWith("/")) {
            str = str.substring(0, str.length - 1)
        }
        return str
    }
}