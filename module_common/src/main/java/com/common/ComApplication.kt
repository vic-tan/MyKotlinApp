package com.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.multidex.MultiDex
import com.hjq.toast.ToastUtils
import com.tencent.bugly.crashreport.CrashReport
import org.litepal.LitePal


/**
 * open 表示该类可以被继承，kotlin 中默认类是不可以被继承
 */
open class ComApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        ToastUtils.init(this)
        CrashReport.initCrashReport(this)
        LitePal.initialize(context)
        handler = Handler(Looper.getMainLooper())
    }



    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var handler: Handler
        var token: String? = null
    }
}