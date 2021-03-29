package com.common.widget.extension


import android.view.View

/**
 * @desc:View扩展类
 * @author: tanlifei
 * @date: 2021/3/10 17:07
 */

// 两次点击按钮之间的点击间隔不能少于500毫秒
private var INTERNAL_TIME: Long = 500

private var lastClickTime: Long = 0

/**
 * view的显示隐藏
 */
val View.isVisible: Boolean
    get() = visibility == View.VISIBLE
val View.isInVisible: Boolean
    get() = visibility == View.INVISIBLE
val View.isGone: Boolean
    get() = visibility == View.GONE

/**
 * view 显示隐藏
 */
fun View.setVisible(show: Boolean) {
    if (show) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

/**
 * view 显示隐藏
 */
fun View.visible() {
    setVisible(true)
}

/**
 * view 显示隐藏
 */
fun View.gone() {
    setVisible(false)
}


/**
 * view点击,带有防重复点击
 */
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {
    if (clickEnable()) {
        block(it as T)
    }
}


/**
 * 长按点击
 */
fun <T : View> T.longClick(block: (T) -> Boolean) = setOnLongClickListener {
    block(it as T)
}

/**
 *两次点击按钮之间的点击间隔不能少于500毫秒
 */
fun <T : View> T.clickEnable(mDelayTime: Long = 500): Boolean {
    INTERNAL_TIME = mDelayTime
    var flag = false
    val curClickTime = System.currentTimeMillis()
    if (curClickTime - lastClickTime >= INTERNAL_TIME) {
        flag = true
    }
    lastClickTime = curClickTime
    return flag
}


