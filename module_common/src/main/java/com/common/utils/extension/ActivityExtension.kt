package com.common.utils.extension

import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils

/**
 * @desc:actvity 跳转类
 * @author: tanlifei
 * @date: 2021/3/22 9:34
 */
inline fun <reified T> startActivity(block: Intent.() -> Unit) {
    val intent = Intent(ActivityUtils.getTopActivity(), T::class.java)
    intent.block()
    ActivityUtils.startActivity(intent)
}

inline fun <reified T> startActivityForResult(requestCode: Int, block: Intent.() -> Unit) {
    val intent = Intent(ActivityUtils.getTopActivity(), T::class.java)
    intent.block()
    ActivityUtils.startActivityForResult(
        ActivityUtils.getTopActivity(),
        intent,
        requestCode
    )
}

