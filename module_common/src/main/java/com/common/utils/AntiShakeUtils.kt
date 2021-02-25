package com.common.utils

import android.view.View
import androidx.annotation.IntRange
import com.common.R

/**
 * @desc:防重复点击工具类
 * @author: tanlifei
 * @date: 2021/2/23 15:59
 */
class AntiShakeUtils {
    companion object {
        // 两次点击按钮之间的点击间隔不能少于1000毫秒
        private const val INTERNAL_TIME: Long = 500


        /**
         * Whether this click event is invalid.
         *
         * @param target target view
         * @return true, invalid click event.
         * @see .isInvalidClick
         */
        fun isInvalidClick(target: View): Boolean {
            return isInvalidClick(
                target,
                INTERNAL_TIME
            )
        }

        /**
         * Whether this click event is invalid.
         *
         * @param target       target view
         * @param internalTime the internal time. The unit is millisecond.
         * @return true, invalid click event.
         */
        fun isInvalidClick(
            target: View,
            @IntRange(from = 0) internalTime: Long
        ): Boolean {
            val curTimeStamp = System.currentTimeMillis()
            var lastClickTimeStamp: Long = 0
            val o = target.getTag(R.id.last_click_time)
            if (o == null) {
                target.setTag(R.id.last_click_time, curTimeStamp)
                return false
            }
            lastClickTimeStamp = o as Long
            val isInvalid = curTimeStamp - lastClickTimeStamp < internalTime
            if (!isInvalid) target.setTag(R.id.last_click_time, curTimeStamp)
            return isInvalid
        }

    }
}