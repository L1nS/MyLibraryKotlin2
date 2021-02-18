package com.lins.mykotlinlibrary.ui.sms

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.imyyq.mvvm.base.DataBindingBaseActivity
import com.imyyq.mvvm.utils.ToastUtil
import com.lins.mykotlinlibrary.R
import com.lins.mykotlinlibrary.common.NoViewModel
import com.lins.mykotlinlibrary.databinding.ActivitySmsBinding
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.activity_sms.*

/**
 *@CreateTime 2021/2/18 9:38
 *@Describe
 */

private const val SMS_SEND_ACTION = "SMS_SEND_ACTION"
private const val SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION"

class SmsActivity :
    DataBindingBaseActivity<ActivitySmsBinding, NoViewModel>(R.layout.activity_sms) {

    private lateinit var phoneList: MutableList<String>

    private lateinit var adapter: PhoneAdapter

    private lateinit var smsManager: SmsManager
    private lateinit var receiver: SmsBroadReceiver
    private lateinit var piSent: PendingIntent
    private lateinit var piDelivery: PendingIntent

    override fun initView() {
        mBinding.idRvPhone.layoutManager = LinearLayoutManager(this)
        phoneList = arrayListOf()
        adapter = PhoneAdapter(phoneList)
        mBinding.idRvPhone.adapter = adapter

    }

    override fun initListener() {
        mBinding.idBtnPhoneAdd.setOnClickListener {
            val phone = mBinding.idEtPhone.text.toString()
            if (phone.length == 11) {
                if (!phoneList.contains(phone)) {
                    phoneList.add(phone)
                    mBinding.idEtPhone.setText("")
                    adapter.notifyDataSetChanged()
                }
            }
        }

        mBinding.idBtnSend.setOnClickListener {
            val content = mBinding.idEtSmsContent.text.toString()
            if (content.isEmpty())
                return@setOnClickListener
            PermissionX.init(this)
                .permissions(Manifest.permission.SEND_SMS)
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        phoneList.forEach {
                            smsManager.sendTextMessage(it, null, content, piSent, piDelivery)
                        }
                    }
                }

        }
    }

    override fun initData() {
        smsManager = SmsManager.getDefault()
        piSent = PendingIntent.getBroadcast(this, 0, Intent(SMS_SEND_ACTION), PendingIntent.FLAG_UPDATE_CURRENT)
        piDelivery = PendingIntent.getBroadcast(this, 1, Intent(SMS_DELIVERED_ACTION), PendingIntent.FLAG_UPDATE_CURRENT)

        receiver = SmsBroadReceiver()
        val filter = IntentFilter()
        filter.addAction(SMS_SEND_ACTION)
        filter.addAction(SMS_DELIVERED_ACTION)
        registerReceiver(receiver, filter)
    }

    inner class SmsBroadReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    println("${intent.action} 成功")
                    ToastUtil.showShortToast("${intent.action} 成功")
                }
                else -> {
                    println("${intent.action} 失败")
                    ToastUtil.showShortToast("${intent.action} 失败")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

}