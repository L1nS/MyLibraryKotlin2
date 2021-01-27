package com.imyyq.mvvm.utils.image

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.imyyq.mvvm.app.BaseApp
import java.io.*


object BitmapPathUtils {

    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    fun getRealPathFromUri(uri: Uri): String? {

        // Uri分为三个部分   域名://主机名/路径/id
        // content://media/extenral/images/media/17766
        // content://com.android.providers.media.documents/document/image:2706
        // file://com.xxxx.xxxxx ---- 7.0 有限制
        val sdkVersion = Build.VERSION.SDK_INT
        return if (sdkVersion >= 19) { // api >= 19
            getRealPathFromUriAboveApi19(uri)
        } else { // api < 19
            getRealPathFromUriBelowAPI19(uri)
        }
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private fun getRealPathFromUriAboveApi19(uri: Uri): String? {
        var filePath: String? = null
        if (DocumentsContract.isDocumentUri(BaseApp.getInstance(), uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            val documentId = DocumentsContract.getDocumentId(uri)
            if (isMediaDocument(uri)) { // MediaProvider
                val divide = documentId.split(":".toRegex()).toTypedArray()
                val type = divide[0]
                var mediaUri: Uri? = null
                mediaUri = if ("image" == type) {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                } else {
                    return null
                }
                filePath = getFileFromContentUri(mediaUri)
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                if (documentId.startsWith("raw:")) {
                    filePath = documentId.replaceFirst("raw:".toRegex(), "")
                } else {
                    var contentUri: Uri = uri
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(documentId)
                        )
                    }
                    filePath = getFileFromContentUri(contentUri)
                }
            } else if (isExternalStorageDocument(uri)) {
                val split = documentId.split(":".toRegex()).toTypedArray()
                if (split.size >= 2) {
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        filePath =
                            Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }
                }
            }
        } else if (ContentResolver.SCHEME_CONTENT.equals(uri.scheme, ignoreCase = true)) {
            // 如果是 content 类型的 Uri
            filePath = getFileFromContentUri(uri)
        } else if (ContentResolver.SCHEME_FILE == uri.scheme) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.path
        }
        return filePath
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private fun getRealPathFromUriBelowAPI19(uri: Uri): String? {
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) data = uri.path else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            data = getFileFromContentUri(uri)
        }
        return data
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private fun getFileFromContentUri(contentUri: Uri?): String? {
        val context =BaseApp.getInstance().applicationContext
        if (contentUri == null) {
            return null
        }
        var file: File? = null
        var filePath = ""
        val fileName: String
        val filePathColumn =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME)
        val contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(
            contentUri, filePathColumn, null,
            null, null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            try {
                filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]))
            } catch (e: java.lang.Exception) {
            }
            fileName = cursor.getString(cursor.getColumnIndex(filePathColumn[1]))
            cursor.close()
            if (filePath.isNotEmpty()) {
                file = File(filePath)
            }
            if (file == null || !file.exists() || file.length() <= 0 || filePath.isEmpty()) {
                filePath = getPathFromInputStreamUri(context, contentUri, fileName)
            }
            if (filePath.isNotEmpty()) {
                file = File(filePath)
            }
        }
        return file?.absolutePath
    }

    private fun getPathFromInputStreamUri(context: Context, uri: Uri, fileName: String): String {
        var inputStream: InputStream? = null
        var filePath = ""
        if (uri.authority != null) {
            try {
                inputStream = context.contentResolver.openInputStream(uri)
                val file: File? = createTemporalFileFrom(context, inputStream, fileName)
                filePath = file?.path.toString()
            } catch (e: java.lang.Exception) {
            } finally {
                try {
                    inputStream?.close()
                } catch (e: java.lang.Exception) {
                }
            }
        }
        return filePath
    }

    private fun createTemporalFileFrom(
        context: Context,
        inputStream: InputStream?,
        fileName: String
    ): File? {
        var targetFile: File? = null
        if (inputStream != null) {
            var read: Int
            val buffer = ByteArray(8 * 1024)
            //自己定义拷贝文件路径
            targetFile = File(context.externalCacheDir!!.absolutePath, fileName)
            if (targetFile.exists()) {
                targetFile.delete()
            }
            val outputStream: OutputStream = FileOutputStream(targetFile)
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return targetFile
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }


}