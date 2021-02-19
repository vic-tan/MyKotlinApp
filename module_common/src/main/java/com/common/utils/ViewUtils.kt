package com.common.utils

import android.view.View

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/19 14:30
 */
class ViewUtils {
    companion object {
        fun setOnClickListener(clickListener: View.OnClickListener, vararg views: View) {
            for (i in views.indices) {
                views[i].setOnClickListener(clickListener)
            }
        }
    }
}