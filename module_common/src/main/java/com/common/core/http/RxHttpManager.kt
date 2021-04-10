package com.common.core.http

import android.app.Application
import com.blankj.utilcode.util.AppUtils
import com.common.ComFun
import com.common.utils.ComUtils
import okhttp3.OkHttpClient
import rxhttp.RxHttp
import rxhttp.wrapper.param.Param
import java.util.concurrent.TimeUnit

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/1 10:35
 */
object RxHttpManager {
    fun init(context: Application) {
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
        //RxHttp初始化，自定义OkHttpClient对象，非必须
        RxHttp.init(client)
        RxHttp.setDebug(true)
        //设置公共参数，非必须
        RxHttp.setOnParamAssembly { p: Param<*>? ->
            p!!.addHeader("Authorization", "Bearer " + ComFun.mToken)
                .addHeader("channel_code", "" + ComUtils.getDefaultChannel(context))
                .addHeader("app-version", "" + AppUtils.getAppVersionName())
                .addHeader("source-type", "android") //添加公共请求头
        }
    }

    fun addToken() {
        RxHttp.setOnParamAssembly { p: Param<*>? ->
            p!!.addHeader("Authorization", "Bearer " + ComFun.mToken)
        }
    }
}