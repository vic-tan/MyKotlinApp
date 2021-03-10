package com.common.utils

import android.view.View
import android.widget.TextView
import com.common.utils.extension.click

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/19 14:30
 */
object ViewUtils {
    fun setOnClickListener(clickListener: View.OnClickListener, vararg views: View) {
        for (i in views.indices) {
            views[i].click { clickListener.onClick(views[i]) }
        }
    }

    fun setOnClickListener(v: View, clickListener: View.OnClickListener, vararg views: Int) {
        for (i in views.indices) {
            v.findViewById<TextView>(views[i]).setOnClickListener(clickListener)
        }
    }

}