package com.tanlifei.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.multidex.MultiDex
import com.common.ComApplication
import com.hjq.toast.ToastUtils
import com.tanlifei.app.core.http.RxHttpManager
import org.litepal.LitePal


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