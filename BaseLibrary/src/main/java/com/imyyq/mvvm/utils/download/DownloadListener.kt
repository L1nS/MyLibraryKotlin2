package com.imyyq.mvvm.utils.download

/**
 *@CreateTime 2021/2/2 16:04
 *@Describe
 */
interface DownloadListener {
    fun onProgress(progress: Int)

    fun onSuccess()

    fun onFailed()

    fun onPaused()

    fun onCanceled()
}