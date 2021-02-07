package com.common.core.magicindicator

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ConvertUtils
import com.common.R
import com.common.utils.ResUtils
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 14:02
 */
object MagicIndicatorUtils {
    private fun initComNavigatorAdapter(
        viewPager2: ViewPager2,
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
                simplePagerTitleView.normalColor = ResUtils.getColor(R.color.color_999999)
                simplePagerTitleView.selectedColor = ResUtils.getColor(R.color.color_333333)
                simplePagerTitleView.text = mTitleData[index]
                simplePagerTitleView.textSize = 19f
                simplePagerTitleView.setOnClickListener {
                    viewPager2.currentItem = index
                }
                return simplePagerTitleView
            }


            override fun getIndicator(context: Context): IPagerIndicator {
                //设置线
                val indicator = LinePagerIndicator(context)
                indicator.setColors(ResUtils.getColor(R.color.theme_color))
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
        viewPager2: ViewPager2,
        mTitleData: List<String>
    ) {
        var commonNavigator = CommonNavigator(context)
        commonNavigator.adapter = initComNavigatorAdapter(viewPager2, mTitleData)
        magicIndicator.navigator = commonNavigator
        ViewPager2Helper.bind(magicIndicator, viewPager2)
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