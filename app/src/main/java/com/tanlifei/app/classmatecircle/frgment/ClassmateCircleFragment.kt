package com.tanlifei.app.classmatecircle.frgment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.BarUtils
import com.common.core.base.adapter.BasePagerAdapter
import com.common.core.base.ui.fragment.BaseLazyFragment
import com.common.core.magicindicator.MagicIndicatorUtils
import com.tanlifei.app.databinding.FragmentClassmatecircleBinding


/**
 * @desc:同学圈
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class ClassmateCircleFragment : BaseLazyFragment<FragmentClassmatecircleBinding>() {
    private val mTitleData = mutableListOf("关注", "推荐")
    private lateinit var fragmentAdapter: BasePagerAdapter
    private var mFragments: MutableList<Fragment> = ArrayList()

    companion object {
        fun newInstance(): ClassmateCircleFragment {
            val fragment = ClassmateCircleFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onFirstVisibleToUser() {
        BarUtils.addMarginTopEqualStatusBarHeight(binding.tabIndicator)//为 view 增加 MarginTop 为状态栏高度
        bindFragments()
        MagicIndicatorUtils.initComMagicIndicator(
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
        mFragments.add(RecommendTabFragment.newInstance())
        fragmentAdapter = BasePagerAdapter(childFragmentManager, mFragments)
        binding.viewPager.adapter = fragmentAdapter
    }


}


