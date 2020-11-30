package com.lins.mykotlinlibrary2.data

import com.imyyq.mvvm.base.BaseModel
import com.lins.mykotlinlibrary2.data.source.HttpDataSource
import com.lins.mykotlinlibrary2.data.source.http.HttpDataSourceImpl


class Repository : BaseModel(), HttpDataSource {
    private var httpDataSource: HttpDataSource = HttpDataSourceImpl

}