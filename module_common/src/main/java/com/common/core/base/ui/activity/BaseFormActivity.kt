package com.common.core.base.ui.activity

import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.KeyboardUtils
import com.common.core.base.viewmodel.BaseViewModel

/**
 * @desc: 有表单输入基类 主要是点输入显示键盘，点其它的地方收起键盘
 * @author: tanlifei
 * @date: 2021/1/28 13:51
 */
open abstract class BaseFormActivity<T : ViewBinding, VM : BaseViewModel> : BaseToolBarActivity<T,VM>() {

    //region软键盘的处理
    /**
     * 清除editText的焦点
     *
     * @param v   焦点所在View
     * @param ids 输入框
     */
    open fun clearViewFocus(v: View?, vararg ids: Int) {
        if (null != v && null != ids && ids.isNotEmpty()) {
            for (id in ids) {
                if (v.id == id) {
                    v.clearFocus()
                    break
                }
            }
        }
    }

    /**
     * 隐藏键盘
     *
     * @param v   焦点所在View
     * @param ids 输入框
     * @return true代表焦点在edit上
     */
    open fun isFocusEditText(v: View?, vararg ids: Int): Boolean {
        if (v is EditText) {
            for (id in ids) {
                if (v.id == id) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 是否触摸在指定view上面,对某个控件过滤
     *
     * @param views
     * @param ev
     * @return
     */
    open fun isTouchView(views: Array<View>?, ev: MotionEvent): Boolean {
        if (views == null || views.isEmpty()) {
            return false
        }
        val location = IntArray(2)
        for (view in views) {
            view.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]
            if (ev.x > x && ev.x < x + view.width && ev.y > y && ev.y < y + view.height
            ) {
                return true
            }
        }
        return false
    }

    /**
     * 是否触摸在指定view上面,对某个控件过滤
     *
     * @param ids
     * @param ev
     * @return
     */
    open fun isTouchView(ids: IntArray, ev: MotionEvent): Boolean {
        val location = IntArray(2)
        for (id in ids) {
            val view: View = findViewById(id) ?: continue
            view.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]
            if (ev.x > x && ev.x < x + view.width && ev.y > y && ev.y < y + view.height
            ) {
                return true
            }
        }
        return false
    }

    /**
     *
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        try {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                if (isTouchView(filterViewByIds(), ev)) {
                    return super.dispatchTouchEvent(ev)
                }
                if (showSoftByEditViewIds() == null || showSoftByEditViewIds().isEmpty()) {
                    return super.dispatchTouchEvent(ev)
                }
                val v = currentFocus
                if (isFocusEditText(v, *showSoftByEditViewIds())) {
                    if (isTouchView(showSoftByEditViewIds(), ev)) {
                        return super.dispatchTouchEvent(ev)
                    }
                    //隐藏键盘
                    KeyboardUtils.hideSoftInput(this)
                    clearViewFocus(v, *showSoftByEditViewIds())
                }
            }
        } finally {
            return super.dispatchTouchEvent(ev)
        }
    }


    /**
     * 传入要过滤的View
     * 过滤之后点击将不会有隐藏软键盘的操作
     *
     * @return id 数组
     */
    open fun filterViewByIds(): Array<View> {
        return arrayOf()
    }

    /**
     * 传入EditText的Id
     * 没有传入的EditText不做处理
     *
     * @return id 数组
     */
    abstract fun showSoftByEditViewIds(): IntArray
}