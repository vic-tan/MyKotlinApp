package com.common.utils

import androidx.core.content.ContextCompat
import com.common.ComFun

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/27 16:06
 */
open class ResUtils {

    companion object {
        /**
         * 获取颜色
         *
         * @param resId
         * @return
         */
        fun getColor(resId: Int): Int {
            return ContextCompat.getColor(ComFun.getContext(), resId)
        }
    }
}