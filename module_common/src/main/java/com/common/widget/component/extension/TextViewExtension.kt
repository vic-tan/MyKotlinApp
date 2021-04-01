package com.common.widget.component.extension

import android.R
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.blankj.utilcode.util.ObjectUtils
import com.common.constant.GlobalEnumConst


/**
 * @desc:TextView扩展类
 * @author: tanlifei
 * @date: 2021/3/10 17:07
 */

/**
 * TextView 加drawable图片
 * resId 图片
 * width 图片宽度 单位（dp)
 * height 图片高度 单位（dp)
 * direction 图片位于文字的方向
 */
fun TextView.drawableText(
    resId: Int,
    width: Float,
    height: Float,
    direction: GlobalEnumConst.DrawableDirection
): Drawable? {
    val drawable: Drawable? = drawable(resId)
    if (ObjectUtils.isNotEmpty(drawable)) {
        drawable!!.setBounds(0, 0, dp2px(width), dp2px(height))
    }
    when (direction) {
        GlobalEnumConst.DrawableDirection.LEFT -> {
            setCompoundDrawables(drawable, null, null, null)
        }
        GlobalEnumConst.DrawableDirection.TOP -> {
            setCompoundDrawables(null, drawable, null, null)
        }
        GlobalEnumConst.DrawableDirection.RIGHT -> {
            setCompoundDrawables(null, null, drawable, null)
        }
        GlobalEnumConst.DrawableDirection.BOTTOM -> {
            setCompoundDrawables(null, null, null, drawable)
        }
    }
    return drawable
}


