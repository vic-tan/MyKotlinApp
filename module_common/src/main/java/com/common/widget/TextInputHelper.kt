package com.common.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.blankj.utilcode.util.ObjectUtils
import com.common.R
import com.common.widget.extension.color
import com.ruffian.library.widget.RTextView
import java.util.*

/**
 * @desc: 用于输入框未输入时按钮显示不可用，
 * @author: tanlifei
 * @date: 2021/1/28 17:40
 */
class TextInputHelper(
    var mContext: Context,
    var mTargetView: RTextView,
    private var mColorNormal: Int = R.color.theme,
    private var mEnabledColor: Int = R.color.com_btn_enabled
) : TextWatcher {
    private var mViewSet: MutableList<TextView>? = null //TextView集合，子类也可以（EditText、TextView、Button）


    /**
     * 添加EditText或者TextView监听
     *
     * @param views 传入单个或者多个EditText或者TextView对象
     */
    fun addViews(vararg views: TextView) {
        views?.let {
            if (ObjectUtils.isEmpty(mViewSet)) mViewSet = ArrayList(it.size - 1)
            for (view in it) {
                view.addTextChangedListener(this)
                mViewSet!!.add(view)
            }
            afterTextChanged(null)
        }
    }


    /**
     * 移除EditText监听，避免内存泄露
     */
    fun removeViews() {
        mViewSet?.let {
            for (view in it) view.removeTextChangedListener(this)
            it.clear()
        }
    }

    override fun afterTextChanged(s: Editable?) {
        mViewSet?.let {
            for (view in it)
                if ("" == view.text.toString()) {
                    setEnabled(false)
                    return
                }
            setEnabled(true)
        }
    }


    /**
     * 设置View的事件
     *
     * @param enabled 启用或者禁用View的事件
     */
    private fun setEnabled(enabled: Boolean) {
        if (enabled == mTargetView.isEnabled) return
        if (enabled) {
            //启用View的事件
            mTargetView.isEnabled = true
            mTargetView.helper.backgroundColorNormal = color(mColorNormal)
        } else {
            //禁用View的事件
            mTargetView.isEnabled = false
            mTargetView.helper.backgroundColorNormal = color(mEnabledColor)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}