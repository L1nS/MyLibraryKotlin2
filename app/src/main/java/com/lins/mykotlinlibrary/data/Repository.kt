package com.lins.mykotlinlibrary.data

import com.imyyq.mvvm.base.BaseModel
import com.lins.mykotlinlibrary.data.source.HttpDataSource
import com.lins.mykotlinlibrary.data.source.http.HttpDataSourceImpl


class Repository : BaseModel(), HttpDataSource {
    private var httpDataSource: HttpDataSource = HttpDataSourceImpl

}