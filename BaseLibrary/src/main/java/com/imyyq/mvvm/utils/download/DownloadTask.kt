package com.imyyq.mvvm.utils.download

import android.os.AsyncTask
import android.os.Environment
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile

/**
 *@CreateTime 2021/2/2 16:06
 *@Describe
 */

private val TYPE_SUCCESS = 0
private val TYPE_FAILED = 1
private val TYPE_PAUSED = 2
private val TYPE_CANCELED = 3

class DownloadTask(val listener: DownloadListener) : AsyncTask<String, Int, Int>() {

    private var isCanceled = false

    private var isPaused = false

    private var lastProgress = 0


    override fun doInBackground(vararg params: String): Int {
        var inputStream: InputStream? = null
        var saveFile: RandomAccessFile? = null
        var file: File? = null
        try {
            var downloadedLength = 0L //记录已下载的文件长度
            val downloadUrl = params[0]
            var filePath = try {
                params[1]
            } catch (e: Exception) {
                val fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"))
                val directory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
                directory + File.separator + fileName
            }
            file = File(filePath)
            if (file.exists()) {
                downloadedLength = file.length()
            }
            val contentLength = getContentLength(downloadUrl)
            if (contentLength == 0L) {
                return TYPE_FAILED
            } else if (contentLength == downloadedLength) {
                //已下载字节和文件总字节相等,说明已经下载完成
                return TYPE_SUCCESS
            }
            val client = OkHttpClient()
            val request = Request.Builder()
                //断点下载,指定从哪个字节开始下载
                .addHeader("RANGE", "bytes=$downloadedLength-")
                .url(downloadUrl)
                .build()
            val response = client.newCall(request).execute()
            if (response.body() != null) {
                inputStream = response.body()!!.byteStream()
                saveFile = RandomAccessFile(file, "rw")
                saveFile.seek(downloadedLength) //跳过已下载的字节
                val buff = ByteArray(1024)
                var total = 0
                var len = inputStream.read(buff)
                while (len != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED
                    } else if (isPaused) {
                        return TYPE_PAUSED
                    } else {
                        total += len
                        saveFile.write(buff, 0, len)
                        //计算已下载的百分比
                        val progress = (total + downloadedLength) * 100 / contentLength
                        publishProgress(progress.toInt())
                        len = inputStream.read(buff)
                    }
                }
                response.body()!!.close()
                return TYPE_SUCCESS
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                saveFile?.close()
                if (isCanceled && file != null) {
                    file.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return TYPE_FAILED
    }

    override fun onProgressUpdate(vararg values: Int?) {
        val progress = values[0]
        if (progress != null) {
            if (progress > lastProgress) {
                listener.onProgress(progress)
                lastProgress = progress
            }
        }
    }

    override fun onPostExecute(result: Int) {
        when (result) {
            TYPE_SUCCESS -> listener.onSuccess()
            TYPE_FAILED -> listener.onFailed()
            TYPE_PAUSED -> listener.onPaused()
            TYPE_CANCELED -> listener.onCanceled()
        }
    }

    fun pauseDownload() {
        isPaused = true
    }

    fun cancelDownload() {
        isCanceled = true
    }

    private fun getContentLength(downloadUrl: String): Long {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(downloadUrl)
            .build()
        val response = client.newCall(request).execute()
        if (response.body() != null && response.isSuccessful) {
            val contentLength = response.body()!!.contentLength()
            response.close()
            return contentLength
        }
        return 0
    }
}