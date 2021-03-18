package com.common.utils.extension

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.common.ComFun


/**
 * @desc:扩展类
 * @author: tanlifei
 * @date: 2021/3/10 17:07
 */

/**
 * 全局context
 */
fun globalContext() = ComFun.context


/**
 * 打印日志
 */
//fun log(content: String?) = BuildConfig.isDebug.yes {
//    Log.e("[UpdateAppUtils]", content ?: "")
//}

/**
 * 获取color
 */
fun color(color: Int) =
    if (globalContext() == null) 0 else ContextCompat.getColor(globalContext(), color)

/**
 * 获取 String
 */
fun string(string: Int) = globalContext().getString(string) ?: ""


/**
 * 获取颜色
 *
 * @param resId
 * @return
 */
fun drawable(resId: Int): Drawable? = ContextCompat.getDrawable(globalContext(), resId)


//dp转px
fun dp2px(value: Int) = (value * globalContext().resources.displayMetrics.density).toInt()

fun dp2px(value: Float) = (value * globalContext().resources.displayMetrics.density).toInt()

//sp转px
fun sp2px(value: Int) = (value * globalContext().resources.displayMetrics.scaledDensity).toInt()

fun sp2px(value: Float) = (value * globalContext().resources.displayMetrics.scaledDensity).toInt()

