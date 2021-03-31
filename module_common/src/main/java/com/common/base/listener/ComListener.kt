package com.common.base.listener

import android.view.View

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/31 9:43
 */
interface ComListener {


    interface ViewClick {
        fun click(v: View)
    }

    interface BackCall {
        fun call(any: Any? = null, any2: Any? = null)
    }
}