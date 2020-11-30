package com.imyyq.mvvm.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.imyyq.mvvm.app.BaseApp
import java.io.UnsupportedEncodingException
import java.util.*


object DeviceUtils {

    /**
     * 获取手机号码
     * @param mContext
     * @return
     */
    fun getDevicePhoneNum(): String {
        val tm =
            BaseApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ActivityCompat.checkSelfPermission(
                BaseApp.getInstance(),
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                BaseApp.getInstance(),
                Manifest.permission.READ_PHONE_NUMBERS
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                BaseApp.getInstance(),
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return ""
        }
        return if (tm.line1Number != null) tm.line1Number else ""
    }


    const val PREFS_FILE = "device_id.xml"
    const val PREFS_DEVICE_ID = "device_id"
    var uuid: UUID? = null

    /**
     * 获取设备唯一ID
     *
     * @return
     */
    @SuppressLint("HardwareIds")
    fun getDeviceUuid(): String {
        val context: Context = BaseApp.getInstance()
        if (uuid == null) {
            val prefs: SharedPreferences =
                context.getSharedPreferences(PREFS_FILE, 0)
            val id =
                prefs.getString(PREFS_DEVICE_ID, null)
            if (id != null) {
                uuid = UUID.fromString(id)
            } else {
                val androidId = Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
                uuid = try {
                    if ("9774d56d682e549c" != androidId) {
                        UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8")))
                    } else {
                        val deviceId =
                            (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
                        if (deviceId != null) UUID.nameUUIDFromBytes(
                            deviceId.toByteArray(charset("utf8"))
                        ) else UUID.randomUUID()
                    }
                } catch (e: UnsupportedEncodingException) {
                    throw RuntimeException(e)
                }
                prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit()
            }
        }
        return uuid.toString()
    }
}