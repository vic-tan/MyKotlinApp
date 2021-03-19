package com.common.core.base.ui.activity

import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.KeyboardUtils
import com.common.core.base.viewmodel.BaseViewModel
import java.util.*

/**
 * @desc: 有表单输入基类 主要是点输入显示键盘，点其它的地方收起键盘
 * @author: tanlifei
 * @date: 2021/1/28 13:51
 */
open abstract class BaseFormActivity<T : ViewBinding, VM : BaseViewModel> :
    BaseToolBarActivity<T, VM>() {

    //region软键盘的处理
    /**
     * 清除editText的焦点
     *
     * @param v   焦点所在View
     * @param ids 输入框
     */
    open fun clearViewFocus(v: View?, views: MutableList<View>) {
        if (null != v && null != views && views.isNotEmpty()) {
            for (view in views) {
                if (v == view) {
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
    open fun isFocusEditText(v: View?, views: MutableList<View>): Boolean {
        if (v is EditText) {
            for (view in views) {
                if (v == view) {
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
    open fun isTouchView(views: MutableList<View>?, ev: MotionEvent): Boolean {
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
                if (showSoftByEditView() == null || showSoftByEditView().isEmpty()) {
                    return super.dispatchTouchEvent(ev)
                }
                val v = currentFocus
                if (isFocusEditText(v, showSoftByEditView())) {
                    if (isTouchView(showSoftByEditView(), ev)) {
                        return super.dispatchTouchEvent(ev)
                    }
                    //隐藏键盘
                    KeyboardUtils.hideSoftInput(this)
                    clearViewFocus(v, showSoftByEditView())
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
     * @return view 数组
     */
    open fun filterViewByIds(): MutableList<View> {
        return mutableListOf()
    }

    /**
     * 传入EditText的Id
     * 没有传入的EditText不做处理
     *
     * @return view 数组
     */
    abstract fun showSoftByEditView(): MutableList<View>
}