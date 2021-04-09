package com.onlineaginguniversity.main.utils

import android.widget.EditText

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/29 10:45
 */
object LoginUtils {

    /**
     * 格式式化输入手机
     */
    fun phoneFormatTextChanged(phone: EditText, s: CharSequence?, count: Int) {
        val length = s.toString().length
        s?.let {
            //删除数字
            if (count == 0) {
                if (length == 4) {
                    phone.setText(s.subSequence(0, 3))
                }
                if (length == 9) {
                    phone.setText(s.subSequence(0, 8))
                }
            }
            //添加数字
            if (count == 1) {
                if (length == 4) {
                    val part1 = s.subSequence(0, 3).toString()
                    val part2 = s.subSequence(3, length).toString()
                    phone.setText("$part1 $part2")
                }
                if (length == 9) {
                    val part1 = s.subSequence(0, 8).toString()
                    val part2 = s.subSequence(8, length).toString()
                    phone.setText("$part1 $part2")
                }
            }
            //复制粘贴
            if (count == 11) {
                val part1 = s.subSequence(0, 3).toString()
                val part2 = s.subSequence(3, 7).toString()
                val part3 = s.subSequence(7, length).toString()
                phone.setText("$part1 $part2 $part3")
            }
        }

    }

    /**
     * 去手机号344格式空格
     *
     * @param str
     * @return
     */
    fun getPhone(str: String): String {
        return str.replace(12288.toChar(), ' ').replace(" ", "").trim { it <= ' ' }
    }
}