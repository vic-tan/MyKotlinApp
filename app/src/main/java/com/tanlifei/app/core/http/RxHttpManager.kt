package com.tanlifei.app.core.http

import android.app.Application
import com.common.ComFun
import com.tanlifei.app.common.utils.AppUtils
import okhttp3.OkHttpClient
import rxhttp.RxHttp
import rxhttp.wrapper.param.Param
import java.util.concurrent.TimeUnit;

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/1 10:35
 */
class RxHttpManager {
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
            p!!.add("versionName", "1.0.0") //添加公共参数
                .addHeader("Authorization", "Bearer " + ComFun.getToken())
                .addHeader("channel_code", "" + AppUtils.getDefaultChannel(context))
                .addHeader("deviceType", "android") //添加公共请求头
        }
    }
}