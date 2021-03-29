package com.common.core.navigator

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.common.R
import com.ruffian.library.widget.RTextView

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 16:23
 */
abstract class NavigatorView<T : ViewBinding> @JvmOverloads constructor(
    mContext: Context,
    mAttributeSet: AttributeSet? = null,
    mDefStyleAttr: Int =  0
) : LinearLayout(mContext, mAttributeSet, mDefStyleAttr) {

    protected lateinit var mBinding: T
    lateinit var mListener: NavigatorListener

    init {
        init()
    }


    /**
     * 初始化数据
     */
    private fun init() {
        var view = View.inflate(context, navigatorLayoutResId(), this)
        mBinding = createBinding(view)
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.setOnClickListener { v: View? ->
                mListener.onNavigatorItemClick(i, v)
            }
        }
    }

    open fun select(position: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (i == position) {
                selectChild(child, i, true, position)
            } else {
                selectChild(child, i, false, position)
            }
        }
    }

    open fun selectChild(
        child: View,
        forI: Int,
        select: Boolean,
        tabPos: Int
    ) {
        if (child is ViewGroup) {
            child.isSelected = select
            for (i in 0 until child.childCount) {
                selectChild(child.getChildAt(i), i, select, tabPos)
            }
        } else {
            child.isSelected = select
            if (child is ImageView) {
                child.isDrawingCacheEnabled = true
                if (select) {
                    pressImageArray()?.get(
                        (child.tag.toString() + "").toInt()
                    )?.let {
                        child.setImageResource(it)
                    }
                } else {
                    normalImageArray()?.get(
                        (child.tag.toString() + "").toInt()
                    )?.let {
                        child.setImageResource(it)
                    }
                }
            }
            if (child is TextView) {
                if (child !is RTextView) {
                    if (select) {
                        child.setTextColor(ContextCompat.getColor(context, R.color.txt_navigator_press))
                    } else {
                        child.setTextColor(ContextCompat.getColor(context, R.color.txt_navigator_normal))
                    }
                }
            }
        }
    }

    /**
     * tab 布局
     *
     * @return
     */
    abstract fun navigatorLayoutResId(): Int

    /**
     * 绑定binding
     */
    abstract fun createBinding(layoutView: View): T

    /**
     * 平常显示的图片
     *
     * @return
     */
    abstract fun normalImageArray(): IntArray?

    /**
     * 选中显示的图片
     *
     * @return
     */
    abstract fun pressImageArray(): IntArray?


    interface NavigatorListener {
        fun onNavigatorItemClick(position: Int, view: View?)
    }

    open fun setNavigatorListener(listener: NavigatorListener) {
        this.mListener = listener

    }
}