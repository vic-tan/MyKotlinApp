package com.common.core.magicindicator

import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback

import net.lucode.hackware.magicindicator.MagicIndicator




/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 13:15
 */
object  ViewPager2Helper {
    fun bind(magicIndicator: MagicIndicator, viewPager: ViewPager2) {
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                magicIndicator.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                magicIndicator.onPageScrollStateChanged(state)
            }
        })
    }
}