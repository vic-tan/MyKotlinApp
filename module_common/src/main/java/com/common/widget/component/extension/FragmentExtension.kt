package com.common.widget.component.extension

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.common.ComFun

/**
 * @desc:context相关的扩展类
 * @author: tanlifei
 * @date: 2021/3/10 17:10
 */

/**
 * 实例化 Fragment
 */
inline fun <reified T : Fragment> newInstanceFragment(block: Bundle.() -> Unit): T {
    val args = Bundle()
    args.block()
    val className = T::class.java.name
    val clazz = FragmentFactory.loadFragmentClass(
        ComFun.mContext.classLoader, className
    )
    val f = clazz.getConstructor().newInstance()
    if (args != null) {
        args.classLoader = f.javaClass.classLoader
        f.arguments = args
    }
    return f as T
}





