package com.common.widget.component.extension

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ObjectUtils
import com.common.BuildConfig
import com.common.ComFun
import com.hjq.toast.ToastUtils


/**
 * @desc:扩展类
 * @author: tanlifei
 * @date: 2021/3/10 17:07
 */


/**
 * 全局context
 */
fun globalContext() = ComFun.mContext


/**
 * 点击事件
 */
fun clickListener(vararg views: View, clickListener: View.OnClickListener) {
    for (i in views.indices) {
        views[i].setOnClickListener {
            if (it.clickEnable()) {
                clickListener.onClick(it)
            }
        }
    }
}

/**
 * 打印日志
 */
fun log(content: Any) {
    if (BuildConfig.DEBUG) Log.i("tlf_log", "-------->${content}")
}

/**
 * toast
 */
fun toast(content: String?) {
    if (ObjectUtils.isNotEmpty(content)) {
        ToastUtils.show(content)
    }
}

/**
 * toast
 */
fun toast(strId: Int) {
    toast(string(strId))
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
 * 获取dimen值
 */
fun dimen(resId: Int): Float = globalContext().resources.getDimension(resId)

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

