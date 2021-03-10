package com.common.utils

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.common.ComApplication
import com.common.ComFun

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/27 16:06
 */
object ResUtils {

    /**
     * 获取颜色
     *
     * @param resId
     * @return
     */
    fun getColor(resId: Int): Int {
        return ContextCompat.getColor(ComFun.context, resId)
    }

    /**
     * 获取颜色
     *
     * @param resId
     * @return
     */
    fun getDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(ComFun.context, resId)
    }
}