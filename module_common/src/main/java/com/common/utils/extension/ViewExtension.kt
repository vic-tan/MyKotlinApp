package com.common.utils.extension


import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * @desc:View扩展类
 * @author: tanlifei
 * @date: 2021/3/10 17:07
 */

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
 * 是否可以点击
 */
fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

/**
 * 最后点击时间
 */
private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

/**
 * 点击延迟时间，默认500ms
 */
private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else 500
    set(value) {
        setTag(1123461123, value)
    }

