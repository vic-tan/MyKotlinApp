package com.common.core.http

import com.google.gson.annotations.SerializedName

class Response<T> {
    val status = 0
    val msg: String? = null
    @SerializedName(value = "data", alternate = ["dataList"])
    val data: T? = null
    val pageNum = 0 //当前页数
    val pageSize = 0 //当前页数
    val total = 0 //总条数
}