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

//dp转px
fun View.dp2px(value: Int) = (value * resources.displayMetrics.density).toInt()

fun View.dp2px(value: Float) = (value * resources.displayMetrics.density).toInt()

//sp转px
fun View.sp2px(value: Int) = (value * resources.displayMetrics.scaledDensity).toInt()

fun View.sp2px(value: Float) = (value * resources.displayMetrics.scaledDensity).toInt()

/**
 * 隐藏键盘
 */
fun View.hideKeyboard(): Boolean {
    clearFocus()
    return (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        windowToken,
        0
    )
}

/**
 * 显示键盘
 */
fun View.showKeyboard(): Boolean {
    requestFocus()
    return (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
        this,
        InputMethodManager.SHOW_IMPLICIT
    )
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

//是否可以点击
private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

//最后点击时间
private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

//点击延迟时间，默认300ms
private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else 300
    set(value) {
        setTag(1123461123, value)
    }

