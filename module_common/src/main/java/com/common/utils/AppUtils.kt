package com.common.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import com.blankj.utilcode.util.ActivityUtils
import com.common.R
import com.common.environment.EnvironmentChangeManager
import com.hjq.toast.ToastUtils
import java.util.*

/**
 * @desc: object 修饰类：被object修饰的类为单例模式
 * @author: tanlifei
 * @date: 2021/1/28 17:33
 */
object AppUtils {
    private var isExit = false

    /**
     * 退出App
     */
    fun exitApp() {
        var tExit: Timer?
        if (!isExit) {
            isExit = true // 准备退出
            ToastUtils.show(R.string.app_exit)
            tExit = Timer()
            tExit.schedule(object : TimerTask() {
                override fun run() {
                    isExit = false // 取消退出
                }
            }, 2000) // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            //finish所有页面和kill app
            ToastUtils.cancel()
            EnvironmentChangeManager.startEnvironmentSwitchIcon()
            ActivityUtils.finishAllActivities()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }

    /**
     * 获取默认渠道
     */
    fun getDefaultChannel(context: Context): String? {
        var appInfo: ApplicationInfo?
        var channelIdStr: String?
        try {
            appInfo = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val channelId = appInfo.metaData["CHANNEL"]
            channelIdStr =
                if (TextUtils.isEmpty(channelId.toString())) "dev" else channelId.toString()
        } catch (e: Exception) {
            channelIdStr = "qa"
            e.printStackTrace()
        }
        return channelIdStr
    }

}