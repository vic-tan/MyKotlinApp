package com.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.common.core.environment.utils.EnvironmentChangeManager
import com.hjq.toast.ToastUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.bugly.crashreport.CrashReport
import org.litepal.LitePal


/**
 * open 表示该类可以被继承，kotlin 中默认类是不可以被继承
 */
open class ComApplication : Application() {

    //static 代码段可以防止内存泄露
    init {
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer { _, layout -> //开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
            //全局设置（优先级最低）
            layout.setEnableAutoLoadMore(true)
            layout.setEnableOverScrollDrag(false)
            layout.setEnableOverScrollBounce(true)
            layout.setEnableLoadMoreWhenContentNotFull(true)
            layout.setEnableScrollContentWhenRefreshed(true)
            layout.setEnableScrollContentWhenLoaded(false)
            layout.setFooterMaxDragRate(4.0f)
            layout.setFooterHeight(45f)
            layout.setPrimaryColorsId(R.color.transparent, R.color.color_666666)
        }

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            //全局设置主题颜色（优先级第二低，可以覆盖 DefaultRefreshInitializer 的配置，与下面的ClassicsHeader绑定）
            layout.setEnableHeaderTranslationContent(true)
            ClassicsHeader(context)
        }

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            //全局设置主题颜色
            ClassicsFooter(context)
        }

    }

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


