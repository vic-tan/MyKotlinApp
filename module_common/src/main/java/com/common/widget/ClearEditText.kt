package com.common.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import com.common.R
import com.ruffian.library.widget.REditText

/**
 * @desc:带删除按钮的输入框
 * @author: tanlifei
 * @date: 2021/1/26 18:23
 */
open class ClearEditText(
    mContext: Context,
    attrs: AttributeSet? = null
) : REditText(mContext, attrs), OnFocusChangeListener,
    TextWatcher {

    init {
        init()
    }

    /*
     * 删除按钮的引用
     */
    private var mClearDrawable: Drawable? = null


    private fun init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = compoundDrawables[2]
        if (mClearDrawable == null) {
            mClearDrawable = resources.getDrawable(R.mipmap.ic_edit_clear)

        }
        mClearDrawable?.setBounds(
            0,
            0,
            mClearDrawable!!.intrinsicWidth,
            mClearDrawable!!.intrinsicHeight
        )
        setClearIconVisible(false)
        onFocusChangeListener = this
        addTextChangedListener(this)
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            if (event.action == MotionEvent.ACTION_UP) {
                val touchable = (event.x > (width
                        - paddingRight - (mClearDrawable
                    ?.intrinsicWidth ?: 0))
                        && event.x < width - paddingRight)
                if (touchable) {
                    this.setText("")
                }
            }
        }
        return super.onTouchEvent(event)
    }


    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        setClearIconVisible(text?.length!! > 0)
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            setClearIconVisible(text.isNotEmpty())
        } else {
            setClearIconVisible(false)
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected open fun setClearIconVisible(visible: Boolean) {
        val right: Drawable? = if (visible) mClearDrawable else null
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1], right, compoundDrawables[3]
        )
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }


}