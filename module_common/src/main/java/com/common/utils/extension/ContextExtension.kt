package com.common.utils.extension

import android.content.Context
import android.content.pm.PackageManager
import android.view.View

/**
 * @desc:context相关的扩展类
 * @author: tanlifei
 * @date: 2021/3/10 17:10
 */


//屏幕宽度
val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

//屏幕高度
val Context.screenHeight
    get() = resources.displayMetrics.heightPixels

fun Context.clickListener(clickListener: View.OnClickListener, vararg views: View) {
    for (i in views.indices) {
        views[i].click {
            clickListener.onClick(it)
        }
    }
}

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
        val manager = appContext.getPackageManager()
        try {
            val info = manager.getPackageInfo(appContext.getPackageName(), 0)

            if (info != null)
                return info.versionCode

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return 0
    }

//dp转px
fun Context.dp2px(value: Int) = (value * resources.displayMetrics.density).toInt()

fun Context.dp2px(value: Float) = (value * resources.displayMetrics.density).toInt()

//sp转px
fun Context.sp2px(value: Int) = (value * resources.displayMetrics.scaledDensity).toInt()

fun Context.sp2px(value: Float) = (value * resources.displayMetrics.scaledDensity).toInt()