package com.common.widget.component.magicindicator

import android.content.Context
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * @desc:带颜色渐变和缩放的指示器标题
 * @author: tanlifei
 * @date: 2021/3/29 14:47
 */
class ScalePagerTitleView(context: Context) : ColorTransitionPagerTitleView(context) {
    private val mMinScale = 0.8f
    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
        super.onEnter(index, totalCount, enterPercent, leftToRight)
        scaleX = mMinScale + (1.0f - mMinScale) * enterPercent
        scaleY = mMinScale + (1.0f - mMinScale) * enterPercent
    }

    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
        super.onLeave(index, totalCount, leavePercent, leftToRight)
        scaleX = 1.0f + (mMinScale - 1.0f) * leavePercent
        scaleY = 1.0f + (mMinScale - 1.0f) * leavePercent
    }
}