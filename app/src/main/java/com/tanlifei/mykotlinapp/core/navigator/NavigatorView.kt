package com.tanlifei.mykotlinapp.core.navigator

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ruffian.library.widget.RTextView
import com.tanlifei.mykotlinapp.R

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 16:23
 */
abstract class NavigatorView : LinearLayout {
    var mContext: Context
    private lateinit var view: View
    var listener: NavigatorListener? = null

    constructor(context: Context) : super(context) {
        mContext = context
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        mContext = context
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        mContext = context
        init()
    }

    /**
     * 初始化数据
     */
    private fun init() {
        view = View.inflate(context, navigatorLayoutResId(), this)
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.setOnClickListener { v: View? ->
                listener?.onNavigatorItemClick(i, v)
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
                        child.setTextColor(context.resources.getColor(R.color.tab_txt_press))
                    } else {
                        child.setTextColor(context.resources.getColor(R.color.tab_txt_normal))
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

    open fun setNavigatorListener(listener: NavigatorListener?) {
        this.listener = listener
    }
}