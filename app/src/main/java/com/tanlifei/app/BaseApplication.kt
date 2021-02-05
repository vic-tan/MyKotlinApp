package com.tanlifei.app

import android.content.Context
import androidx.multidex.MultiDex
import com.common.ComApplication
import com.common.http.RxHttpManager


/**
 * open 表示该类可以被继承，kotlin 中默认类是不可以被继承
 */
open class BaseApplication : ComApplication() {

    override fun onCreate() {
        super.onCreate()
        RxHttpManager().init(this)
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //分包初始化
        MultiDex.install(this)
    }


}