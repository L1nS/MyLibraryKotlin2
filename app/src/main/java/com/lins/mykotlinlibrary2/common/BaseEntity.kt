package com.lins.mykotlinlibrary2.common

import com.imyyq.mvvm.base.IBaseResponse

data class BaseEntity<T>(
    var data: T?,
    var status: Int?,
    var msg: String?
) : IBaseResponse<T> {
    override fun code() = status

    override fun msg() = msg

    override fun data() = data

    override fun isSuccess() = status == 1
}