package com.common.core.http

import com.common.widget.component.extension.toast
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.TimeoutCancellationException
import rxhttp.wrapper.exception.HttpStatusCodeException
import rxhttp.wrapper.exception.ParseException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * User: tanlifei
 * Date: 2020-02-07
 * Time: 21:04
 */
fun Throwable.show(errorCode: Int, errorMsg: String) {
    errorMsg.show(errorCode, errorMsg)
}

fun String.show(errorCode: Int, errorMsg: String) {
    toast(errorMsg)
}

val Throwable.code: Int
    get() {
        val errorCode = when (this) {
            is HttpStatusCodeException -> this.statusCode//Http状态码异常
            is ParseException -> this.errorCode  //业务code异常 ParseException异常表明请求成功，但是数据不正确
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


val Throwable.msg: String
    get() {
        return when (this) {
            is UnknownHostException -> "当前无网络，请检查你的网络设置"//网络异常
            is SocketTimeoutException, is TimeoutException, is TimeoutCancellationException -> "连接超时,请稍后再试"//okhttp全局设置超时 、rxjava中的timeout方法超时、协程超时
            is ConnectException -> "网络不给力，请稍候重试！"
            is HttpStatusCodeException -> {//请求失败异常
                return when (this.statusCode) {
                    "416" -> {
                        "请求范围不符合要求"
                    }
                    "500" -> {
                        "服务器频忙"
                    }
                    else -> "请求失败，请稍后再试"
                }
            }
            is JsonSyntaxException -> "数据解析失败,请检查数据是否正确"//请求成功，但Json语法异常,导致解析失败
            is ParseException -> this.message// ParseException异常表明请求成功，但是数据不正确
                ?: errorCode       //msg为空，显示code
            else -> "请求失败，请稍后再试"
        }
    }


