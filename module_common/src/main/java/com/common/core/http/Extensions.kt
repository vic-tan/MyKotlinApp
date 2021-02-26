package com.example.httpsender.kt

import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.Utils
import com.common.R
import com.google.gson.JsonSyntaxException
import com.hjq.toast.ToastUtils
import rxhttp.wrapper.exception.HttpStatusCodeException
import rxhttp.wrapper.exception.ParseException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * User: ljx
 * Date: 2020-02-07
 * Time: 21:04
 */
fun Throwable.show(errorCode: Int, errorMsg: String) {
    errorMsg.show(errorCode, errorMsg)
}

fun String.show(errorCode: Int, errorMsg: String) {
    ToastUtils.show(errorMsg)
}

val Throwable.errorCode: Int
    get() {
        val errorCode = when (this) {
            is HttpStatusCodeException -> {//请求失败异常
                this.statusCode
            }
            is ParseException -> {  // ParseException异常表明请求成功，但是数据不正确
                this.errorCode
            }
            else -> {
                "-1"
            }
        }
        return try {
            errorCode.toInt()
        } catch (e: Exception) {
            -1
        }
    }


val Throwable.errorMsg: String
    get() {
        var errorMsg = handleNetworkException(this)  //网络异常
        if (this is HttpStatusCodeException) {               //请求失败异常
            val code = this.statusCode
            if ("416" == code) {
                errorMsg = "请求范围不符合要求"
            }
        } else if (this is JsonSyntaxException) {  //请求成功，但Json语法异常,导致解析失败
            errorMsg = "数据解析失败,请稍后再试"
        } else if (this is ParseException) {       // ParseException异常表明请求成功，但是数据不正确
            errorMsg = this.message ?: errorCode   //errorMsg为空，显示errorCode
        } else {
            printStackTrace()
        }
        return errorMsg ?: message ?: this.toString()
    }

//处理网络异常
private fun <T> handleNetworkException(throwable: T): String? {
    val stringId =
        if (throwable is UnknownHostException) { //网络异常
            if (!NetworkUtils.isConnected()) R.string.network_error else R.string.notify_no_network
        } else if (throwable is SocketTimeoutException || throwable is TimeoutException) {
            R.string.time_out_please_try_again_later  //前者是通过OkHttpClient设置的超时引发的异常，后者是对单个请求调用timeout方法引发的超时异常
        } else if (throwable is ConnectException) {
            R.string.esky_service_exception  //连接异常
        } else {
            -1
        }
    return if (stringId == -1) null else Utils.getApp().resources.getString(stringId)
}


