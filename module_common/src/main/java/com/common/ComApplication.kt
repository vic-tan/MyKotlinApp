package com.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.common.environment.EnvironmentChangeManager
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
        registerActivityLifecycleCallbacks(StatisticActivityLifecycleCallback())
    }

    internal class StatisticActivityLifecycleCallback :
        ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStarted(activity: Activity) {
            mFinalCount++
        }

        override fun onActivityDestroyed(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityStopped(activity: Activity) {
            /**
             * 设置是否到应用后台，用于更新应用icon处理
             */
            mFinalCount--
            if (mFinalCount == 0) { //如果mFinalCount ==0，说明是前台到后台
                EnvironmentChangeManager.startEnvironmentSwitchIcon()

            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        }

        override fun onActivityResumed(activity: Activity) {
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var handler: Handler
        var token: String? = null
        var mFinalCount = 0
    }
}


