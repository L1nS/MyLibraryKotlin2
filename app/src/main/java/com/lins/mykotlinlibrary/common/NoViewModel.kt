package com.lins.mykotlinlibrary.common

import android.app.Application
import com.imyyq.mvvm.base.BaseViewModel
import com.lins.mykotlinlibrary.data.Repository

class NoViewModel(app: Application) : BaseViewModel<Repository>(app) {
}