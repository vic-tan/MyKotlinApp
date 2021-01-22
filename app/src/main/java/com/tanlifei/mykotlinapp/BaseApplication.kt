package com.tanlifei.mykotlinapp

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.hjq.toast.ToastUtils
import com.tanlifei.mykotlinapp.common.config.ComFun
import com.tanlifei.mykotlinapp.core.http.TokenInterceptor
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
           return@setOnParamAssembly it.addHeader("deviceType", "android") //添加公共请求头
        }
        ComFun.initialize(this)
        ToastUtils.init(this);
        LitePal.initialize(this)//初始化数据库
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //分包初始化
        MultiDex.install(this)
    }

     open fun getDefaultOkHttpClient(): OkHttpClient? {
        val  client =OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor())
            .build()
        return client;

    }
}