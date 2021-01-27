package com.lins.mykotlinlibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lins.mykotlinlibrary.ui.view.MyViewActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initListener()
    }

    private fun initListener() {
        id_btn_view.setOnClickListener {
            startActivity(Intent(this, MyViewActivity::class.java))
        }
    }
}