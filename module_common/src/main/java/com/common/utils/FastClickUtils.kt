package com.common.utils

/**
 * @desc:防重复点击工具类
 * @author: tanlifei
 * @date: 2021/2/23 15:59
 */
class FastClickUtils {
    companion object {
        // 两次点击按钮之间的点击间隔不能少于1000毫秒
        private var MIN_CLICK_DELAY_TIME = 1000
        private var lastClickTime: Long = 0
        fun isFastClick(): Boolean {
            return isFastClick(1000)
        }

        fun isPraiseFastClick(): Boolean {
            return isFastClick(600)
        }


        fun isFastClick(delay_time: Int): Boolean {
            MIN_CLICK_DELAY_TIME = delay_time
            var flag = false
            val curClickTime = System.currentTimeMillis()
            if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
                flag = true
            }
            lastClickTime = curClickTime
            return flag
        }

    }
}