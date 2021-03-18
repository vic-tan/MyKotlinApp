package com.common.utils.extension

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.common.BuildConfig
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
 * 点击事件
 */
fun clickListener( vararg views: View,clickListener: View.OnClickListener) {
    for (i in views.indices) {
        views[i].click {
            clickListener.onClick(it)
        }
    }
}

/**
 * 打印日志
 */
fun log(content: String?) {
    if (BuildConfig.DEBUG) Log.i("[tlf_log]", content ?: "")
}


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

/**
 * 屏幕宽度
 */
val screenWidth
    get() = globalContext().resources.displayMetrics.widthPixels

/**
 * 屏幕高度
 */
val screenHeight
    get() = globalContext().resources.displayMetrics.heightPixels

/**
 * dp转px
 */
fun dp2px(value: Float) = (value * globalContext().resources.displayMetrics.density).toInt()

/**
 * sp转px
 */
fun sp2px(value: Float) = (value * globalContext().resources.displayMetrics.scaledDensity).toInt()

