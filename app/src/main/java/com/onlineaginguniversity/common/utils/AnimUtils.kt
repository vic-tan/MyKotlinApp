package com.onlineaginguniversity.common.utils

import android.content.Context
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import com.onlineaginguniversity.R

/**
 * @desc: 动画工具类
 * @author: tanlifei
 * @date: 2021/3/30 15:00
 */
object AnimUtils {

    /**
     * 分享心跳动画
     */
    fun shareAnim(mContext: Context): ScaleAnimation {
        return AnimationUtils.loadAnimation(
            mContext,
            R.anim.share_scale
        ) as ScaleAnimation
    }
}