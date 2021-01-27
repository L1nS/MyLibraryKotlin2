package com.lins.mykotlinlibrary.data.source.http

import com.imyyq.mvvm.http.HttpRequest
import com.lins.mykotlinlibrary.data.source.HttpDataSource
import com.lins.mykotlinlibrary.data.source.http.service.ApiService


object HttpDataSourceImpl : HttpDataSource {
    private var apiService = HttpRequest.getService(ApiService::class.java)
}