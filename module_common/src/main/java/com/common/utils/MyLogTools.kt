package com.common.utils

import com.blankj.utilcode.util.LogUtils
import com.common.BuildConfig

/**
 * @desc: 统一显示日志
 * @author: tanlifei
 * @date: 2021/1/27 15:26
 */
object MyLogTools {
    private const val TAG = "tlf_log"
    fun log(content: String) {
        if (BuildConfig.DEBUG) {
            LogUtils.dTag(TAG, content)
        }
    }
}