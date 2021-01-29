package com.tanlifei.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.hjq.toast.ToastUtils
import com.common.ComFun
import com.tanlifei.app.common.utils.AppUtils
import com.tanlifei.app.core.http.TokenInterceptor
import okhttp3.OkHttpClient
import org.litepal.LitePal
import rxhttp.RxHttp


/**
 * open 表示该类可以被继承，kotlin 中默认类是不可以被继承
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //设置debug模式，默认为false，设置为true后，发请求，过滤"RxHttp"能看到请求日志
        RxHttp.init(getDefaultOkHttpClient(), true)
        RxHttp.setOnParamAssembly {
            return@setOnParamAssembly it.addHeader("Authorization", "Bearer " + ComFun.getToken())
                .addHeader(
                    "channel_code",
                    AppUtils.getDefaultChannel(this)
                )//添加公共请求头
        }
        ComFun.initialize(this)
        ToastUtils.init(this)
        LitePal.initialize(this)//初始化数据库
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //分包初始化
        MultiDex.install(this)
    }

    open fun getDefaultOkHttpClient(): OkHttpClient? {
        return OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor())
            .build();

    }
}