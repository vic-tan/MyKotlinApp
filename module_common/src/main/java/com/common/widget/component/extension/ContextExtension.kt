package com.common.widget.component.extension

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

/**
 * @desc:context相关的扩展类
 * @author: tanlifei
 * @date: 2021/3/10 17:10
 */

/**
 * 实例化 Fragment
 */
inline fun <reified T : Fragment> Context.newInstanceFragment(block: Bundle.() -> Unit): T {
    val args = Bundle()
    args.block()
    val className = T::class.java.name
    val clazz = FragmentFactory.loadFragmentClass(
        classLoader, className
    )
    val f = clazz.getConstructor().newInstance()
    if (args != null) {
        args.classLoader = f.javaClass.classLoader
        f.arguments = args
    }
    return f as T
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


