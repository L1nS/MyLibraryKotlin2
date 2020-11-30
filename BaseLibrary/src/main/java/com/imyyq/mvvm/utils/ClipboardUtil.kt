package com.imyyq.mvvm.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.imyyq.mvvm.app.BaseApp

object ClipboardUtil {

    fun copy(text: String, toast: String? = null) {
        val myClipboard: ClipboardManager =
            BaseApp.getInstance().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("text", text)
        myClipboard.setPrimaryClip(myClip)
        if (!toast.isNullOrBlank()) {
            ToastUtil.showShortToast(toast)
        }
    }

}