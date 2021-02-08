package com.common.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.blankj.utilcode.util.ObjectUtils
import com.common.R
import com.common.utils.ResUtils
import com.ruffian.library.widget.RTextView
import java.util.*

/**
 * @desc: 用于输入框未输入时按钮显示不可用，
 * @author: tanlifei
 * @date: 2021/1/28 17:40
 */
class TextInputHelper(view: RTextView) : TextWatcher {
    private var mViewSet: MutableList<TextView>? = null //TextView集合，子类也可以（EditText、TextView、Button）
    private var mBtnView: RTextView = view//操作按钮的View
    private var mColorNormal = R.color.theme_color
    private var mEnabledColor = R.color.enabled_color

    constructor(view: RTextView, colorNormal: Int, enabledColor: Int) : this(view) {
        mBtnView = view
        mColorNormal = colorNormal
        mEnabledColor = enabledColor
    }


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
        if (enabled == mBtnView.isEnabled) return
        if (enabled) {
            //启用View的事件
            mBtnView.isEnabled = true
            mBtnView.helper.backgroundColorNormal = ResUtils.getColor(mColorNormal)
        } else {
            //禁用View的事件
            mBtnView.isEnabled = false
            mBtnView.helper.backgroundColorNormal = ResUtils.getColor(mEnabledColor)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}