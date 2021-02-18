package com.imyyq.mvvm.app

import androidx.lifecycle.LiveData
import com.imyyq.mvvm.R

object CheckUtil {

    fun checkLoadingDialogEvent(event: LiveData<*>?) {
        if (event == null) {
            throw RuntimeException(BaseApp.getInstance().getString(R.string.loadingDialogTips))
        }
    }
}