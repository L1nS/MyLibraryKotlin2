package com.lins.mykotlinlibrary2.data.source.http

import com.imyyq.mvvm.http.HttpRequest
import com.lins.mykotlinlibrary2.data.source.HttpDataSource
import com.lins.mykotlinlibrary2.data.source.http.service.ApiService


object HttpDataSourceImpl : HttpDataSource {
    private var apiService = HttpRequest.getService(ApiService::class.java)
}