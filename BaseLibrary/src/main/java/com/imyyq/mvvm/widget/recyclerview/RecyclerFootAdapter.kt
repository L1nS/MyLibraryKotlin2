package com.imyyq.mvvm.widget.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.imyyq.mvvm.R
import com.imyyq.mvvm.base.BaseViewHolder
import com.imyyq.mvvm.databinding.ItemRecyclerviewFootViewBinding

abstract class RecyclerFootAdapter() :
    RecyclerView.Adapter<BaseViewHolder>() {

    /**
     * footView 开关
     */
    var isFootVisible = false
    val FOOT_VIEW = 1
    lateinit var mContext: Context

    /**
     * 数组数量
     */
    abstract fun getDataCount(): Int

    /**
     * item布局
     */
    abstract fun getCreateViewHolder(parent: ViewGroup, viewType: Int): ViewDataBinding

    /**
     * 数据渲染
     */
    abstract fun setBindViewHolder(holder: BaseViewHolder, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mContext = parent.context
        return if (viewType == FOOT_VIEW) {
            val binding = DataBindingUtil.inflate<ItemRecyclerviewFootViewBinding>(
                LayoutInflater.from(mContext),
                R.layout.item_recyclerview_foot_view, parent, false
            )
            BaseViewHolder(binding)
        } else {
            val binding = getCreateViewHolder(parent, viewType)
            BaseViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return if (isFootVisible)
            getDataCount() + 1
        else
            getDataCount()
    }


    override fun getItemViewType(position: Int): Int {
        if (isFootVisible && position + 1 == itemCount)
            return FOOT_VIEW
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (getItemViewType(position) != FOOT_VIEW) {
            setBindViewHolder(holder, position)
        }
    }

}