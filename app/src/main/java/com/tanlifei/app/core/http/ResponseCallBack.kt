package com.tanlifei.app.core.http

import com.example.httpsender.kt.errorCode
import com.example.httpsender.kt.errorMsg
import com.example.httpsender.kt.show

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/1 14:30
 */
interface ResponseCallBack {
    fun <T> onSuccess(result: T)
    fun onFailed(errorCode: Int, errorMsg: String) {
    }
}