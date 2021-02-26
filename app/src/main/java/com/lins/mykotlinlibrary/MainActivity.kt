package com.lins.mykotlinlibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lins.mykotlinlibrary.ui.hencoder.HenCoderActivity
import com.lins.mykotlinlibrary.ui.sms.SmsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initListener()
    }

    private fun initListener() {
        id_btn_sms.setOnClickListener {
            startActivity(Intent(this, SmsActivity::class.java))
        }
        id_btn_henCoder.setOnClickListener {
            startActivity(Intent(this, HenCoderActivity::class.java))
        }
    }
}