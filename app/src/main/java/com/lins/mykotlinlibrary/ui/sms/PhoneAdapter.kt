package com.lins.mykotlinlibrary.ui.sms

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.imyyq.mvvm.base.BaseViewHolder
import com.lins.mykotlinlibrary.R
import com.lins.mykotlinlibrary.databinding.ItemSmsPhoneBinding

/**
 *@CreateTime 2021/2/18 10:30
 *@Describe
 */
class PhoneAdapter(phoneList: MutableList<String>) : RecyclerView.Adapter<BaseViewHolder>() {

    private lateinit var mContext: Context
    private var mData: MutableList<String> = phoneList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mContext = parent.context
        val binding = DataBindingUtil.inflate<ItemSmsPhoneBinding>(
            LayoutInflater.from(mContext),
            R.layout.item_sms_phone,
            parent,
            false
        )
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val mBinding = holder.binding as ItemSmsPhoneBinding
        val phone = mData[position]
        println(phone)
        mBinding.idTvPhone.text = phone
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}