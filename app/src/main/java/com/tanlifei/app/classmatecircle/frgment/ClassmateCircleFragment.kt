package com.tanlifei.app.classmatecircle.frgment

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ConvertUtils
import com.common.core.adapter.ComViewPagerFragAdapter
import com.common.core.magicindicator.ViewPager2Helper
import com.common.core.base.ui.fragment.BaseLazyFragment
import com.common.core.magicindicator.MagicIndicatorUtils
import com.common.utils.ResUtils
import com.common.core.magicindicator.ScalePagerTitleView
import com.tanlifei.app.R
import com.tanlifei.app.databinding.FragmentClassmatecircleBinding
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView
import java.util.*

/**
 * @desc:同学圈
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class ClassmateCircleFragment : BaseLazyFragment<FragmentClassmatecircleBinding>() {
    private val mTitleData = listOf("关注", "推荐")
    private lateinit var fragmentAdapter: ComViewPagerFragAdapter
    private var mFragments: MutableList<Fragment> = ArrayList()

    companion object {
        fun newInstance(): ClassmateCircleFragment {
            val args = Bundle()
            val fragment =
                ClassmateCircleFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onFirstVisibleToUser() {
        bindFragments()
        MagicIndicatorUtils.initMagicIndicator(
            activity,
            binding.tabIndicator,
            binding.viewPager,
            mTitleData
        )
    }

    /**
     * 绑定tab 中各对应的Fragment
     */
    private fun bindFragments() {
        mFragments.add(FollowFragment.newInstance())
        mFragments.add(RecommendFragment.newInstance())
        fragmentAdapter = ComViewPagerFragAdapter(this, mFragments)
        binding.viewPager.adapter = fragmentAdapter
    }


}


