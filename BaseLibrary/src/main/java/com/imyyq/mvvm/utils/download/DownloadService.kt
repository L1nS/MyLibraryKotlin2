package com.imyyq.mvvm.utils.download

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Environment
import android.os.IBinder
import android.widget.Toast
import java.io.File


/**
 *@CreateTime 2021/2/2 16:43
 *@Describe
 */
class DownloadService : Service() {

    private var downloadTask: DownloadTask? = null
    private var downloadUrl: String? = null
    private var mBinder = DownloadBinder()
    private var outListener: DownloadListener? = null

    val listener = object : DownloadListener {
        override fun onProgress(progress: Int) {
            outListener?.onProgress(progress)
            //            getNotificationManager().notify(1, getNotification("Downloading...", progress))
        }

        override fun onSuccess() {
            outListener?.onSuccess()
            downloadTask = null
            // 下载成功时将前台服务通知关闭，并创建一个下载成功的通知
            //            stopForeground(true)
            //            getNotificationManager().notify(1, getNotification("Download Success", -1))
        }

        override fun onFailed() {
            outListener?.onFailed()
            downloadTask = null
            // 下载失败时将前台服务通知关闭，并创建一个下载失败的通知
            //            stopForeground(true)
            //            getNotificationManager().notify(1, getNotification("Download Failed", -1))
        }

        override fun onPaused() {
            outListener?.onPaused()
            downloadTask = null
        }

        override fun onCanceled() {
            outListener?.onCanceled()
            downloadTask = null
            //            stopForeground(true)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    inner class DownloadBinder : Binder() {
        fun startDownload(url: String,filePath:String) {
            if (downloadTask == null) {
                downloadUrl = url
                downloadTask = DownloadTask(listener)
                downloadTask?.execute(downloadUrl,filePath)
                //                startForeground(1, getNotification("Downloading...", 0))
                //                Toast.makeText(this@DownloadService, "Downloading...", Toast.LENGTH_SHORT).show()
            }
        }

        fun pauseDownload() {
            if (downloadTask != null) {
                downloadTask?.pauseDownload()
            }
        }

        fun cancelDownload() {
            if (downloadTask != null) {
                downloadTask?.cancelDownload()
            } else {
                if (downloadUrl != null) {
                    // 取消下载时需将文件删除，并将通知关闭
                    val fileName = downloadUrl!!.substring(downloadUrl!!.lastIndexOf("/"))
                    val directory: String =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
                    val file = File(directory + fileName)
                    if (file.exists()) {
                        file.delete()
                    }
                    //                    getNotificationManager().cancel(1)
                    //                    stopForeground(true)
                    Toast.makeText(this@DownloadService, "Canceled", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun setListener(listener: DownloadListener) {
            outListener = listener
        }
    }

    /*private fun getNotificationManager(): NotificationManager {
        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }*/

    /*private fun getNotification(title: String, progress: Int): Notification {
        //        val intent = Intent(this, MainActivity::class.java)
        //        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this, 1.toString())
        builder.setSmallIcon(R.drawable.ic_launcher)
        builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher))
        //        builder.setContentIntent(pi)
        builder.setContentTitle(title)
        if (progress >= 0) {
            // 当progress大于或等于0时才需显示下载进度
            builder.setContentText("$progress%")
            builder.setProgress(100, progress, false)
        }
        return builder.build()
    }*/
}