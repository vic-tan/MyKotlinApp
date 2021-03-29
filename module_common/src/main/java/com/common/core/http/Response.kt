package com.common.core.http

import com.google.gson.annotations.SerializedName

class Response<T> {
    val status = 0
    val msg: String? = null
    @SerializedName(value = "data", alternate = ["dataList"])
    val data: T? = null
}