package com.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.common.core.base.bean.UserBean
import com.common.core.environment.utils.EnvironmentChangeManager
import com.common.core.http.RxHttpManager
import com.hjq.toast.ToastUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.bugly.crashreport.CrashReport
import org.litepal.LitePal
import update.UpdateAppUtils

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/5 16:41
 */
object ComFun {

    lateinit var context: Context
    lateinit var handler: Handler
    var token: String? = null
    var user: UserBean? = null
    var mFinalCount = 0

    /**
     * 初始化接口。这里会进行应用程序的初始化操作，一定要在代码执行的最开始调用。
     *
     * @param c
     * Context参数，注意这里要传入的是Application的Context，千万不能传入Activity或者Service的Context。
     */
    fun init() {
//设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer { _, layout -> //开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
            //全局设置（优先级最低）
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

    fun initialize(context: Application) {
        this.context = context
        RxHttpManager.init(context)
        ToastUtils.init(context)
        CrashReport.initCrashReport(context)
        UpdateAppUtils.init(context)//app升级
        LitePal.initialize(context)
        handler = Handler(Looper.getMainLooper())
        context.registerActivityLifecycleCallbacks(StatisticActivityLifecycleCallback())
    }

    internal class StatisticActivityLifecycleCallback :
        Application.ActivityLifecycleCallbacks {
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

}