package com.common.widget.component.magicindicator

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ConvertUtils
import com.common.R
import com.common.widget.component.extension.color
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

/**
 * @desc: tab 指示器工具类
 * @author: tanlifei
 * @date: 2021/2/7 14:02
 */
object MagicIndicatorUtils {
    private fun initComNavigatorAdapter(
        viewPager: ViewPager,
        mTitleData: List<String>
    ): CommonNavigatorAdapter {
        return object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mTitleData.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView: SimplePagerTitleView =
                    ScalePagerTitleView(
                        context
                    )
                simplePagerTitleView.normalColor = color(R.color.txt_help)
                simplePagerTitleView.selectedColor = color(R.color.txt_basic)
                simplePagerTitleView.text = mTitleData[index]
                simplePagerTitleView.textSize = 19f
                simplePagerTitleView.setOnClickListener {
                    viewPager.currentItem = index
                }
                return simplePagerTitleView
            }


            override fun getIndicator(context: Context): IPagerIndicator {
                //设置线
                val indicator = LinePagerIndicator(context)
                indicator.setColors(color(R.color.theme))
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = ConvertUtils.dp2px(3.5f).toFloat()
                indicator.roundRadius = ConvertUtils.dp2px(3.0f).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.lineWidth = ConvertUtils.dp2px(20f).toFloat()
                return indicator
            }

        }
    }

    fun initComMagicIndicator(
        context: Context?,
        magicIndicator: MagicIndicator,
        viewPager: ViewPager,
        mTitleData: MutableList<String>
    ) {
        var commonNavigator = CommonNavigator(context)
        commonNavigator.adapter = initComNavigatorAdapter(viewPager, mTitleData)
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, viewPager)
        val titleContainer =
            commonNavigator.titleContainer // must after setNavigator
        titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        titleContainer.dividerDrawable = object : ColorDrawable() {
            override fun getIntrinsicWidth(): Int {//tab 之间间距
                return ConvertUtils.dp2px(15.0f)
            }
        }
    }
}