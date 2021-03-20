package com.tanlifei.app.classmatecircle.ui.fragment

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
    private lateinit var mFragmentAdapter: BasePagerAdapter
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
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.tabIndicator)//为 view 增加 MarginTop 为状态栏高度
        bindFragments()
        MagicIndicatorUtils.initComMagicIndicator(
            activity,
            mBinding.tabIndicator,
            mBinding.viewPager,
            mTitleData
        )
    }

    /**
     * 绑定tab 中各对应的Fragment
     */
    private fun bindFragments() {
        mFragments.add(FollowFragment.newInstance())
        mFragments.add(RecommendTabFragment.newInstance())
        mFragmentAdapter = BasePagerAdapter(childFragmentManager, mFragments)
        mBinding.viewPager.adapter = mFragmentAdapter
    }


}


