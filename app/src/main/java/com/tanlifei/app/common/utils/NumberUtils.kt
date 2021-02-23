package com.tanlifei.app.common.utils

import java.text.DecimalFormat

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/23 12:04
 */
class NumberUtils {
    companion object {
        private const val MAX_SHOW_PRAISE_COUNT = 10000.0

        fun setPraiseCount(count: Int): String? {
            return showCount("点赞", count)
        }

        fun setCommentCount(defaultStr: String, count: Int): String? {
            return showCount(defaultStr, count)
        }

        fun setCommentCount(count: Int): String? {
            return showCount("评论", count)
        }

        private fun showCount(defaultStr: String, count: Int): String {
            return if (count <= 0) {
                defaultStr
            } else if (count > 0 && count < MAX_SHOW_PRAISE_COUNT) {
                count.toString() + ""
            } else {
                val df = DecimalFormat("#.0")
                val str = df.format((count / MAX_SHOW_PRAISE_COUNT))
                str + "w"
            }
        }
    }
}