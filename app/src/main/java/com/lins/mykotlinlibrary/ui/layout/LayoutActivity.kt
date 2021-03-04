package com.lins.mykotlinlibrary.ui.layout

import android.content.Intent
import com.imyyq.mvvm.base.DataBindingBaseActivity
import com.lins.mykotlinlibrary.R
import com.lins.mykotlinlibrary.common.NoViewModel
import com.lins.mykotlinlibrary.databinding.ActivityLayoutBinding
import com.lins.mykotlinlibrary.ui.layout.slide.SlidingVerticalLayoutActivity
import kotlinx.android.synthetic.main.activity_layout.*

/**
 *@CreateTime 2021/3/4 11:06
 *@Describe
 */
class LayoutActivity :
    DataBindingBaseActivity<ActivityLayoutBinding, NoViewModel>(R.layout.activity_layout) {

    override fun initListener() {
        id_btn_drawer_layout.setOnClickListener {
            startActivity(Intent(this, SlidingVerticalLayoutActivity::class.java))
        }
    }
}