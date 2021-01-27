package com.common.utils

import com.blankj.utilcode.util.LogUtils
import com.common.BuildConfig

/**
 * @desc: 统一显示日志
 * @author: tanlifei
 * @date: 2021/1/27 15:26
 */
open class LogTools {
    companion object {
        private const val TAG = "tlf_app_log"
        fun show(content: String) {
            if (BuildConfig.DEBUG) {
                LogUtils.dTag(TAG, content)
            }
        }
    }
}