package com.common.widget.component.extension

import android.content.Context
import android.content.pm.PackageManager

/**
 * @desc:context相关的扩展类
 * @author: tanlifei
 * @date: 2021/3/10 17:10
 */


//app versionName
val Context.appVersionName: String
    get() {
        val appContext = applicationContext
        val manager = appContext.packageManager
        try {
            val info = manager.getPackageInfo(appContext.packageName, 0)

            if (info != null)
                return info.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

//app versionCode
val Context.appVersionCode: Int
    get() {
        val appContext = applicationContext
        val manager = appContext.packageManager
        try {
            val info = manager.getPackageInfo(appContext.packageName, 0)

            if (info != null)
                return info.versionCode

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return 0
    }


