package com.tanlifei.app.circle.ui.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.BarUtils
import com.common.base.adapter.BasePagerAdapter
import com.common.base.ui.fragment.BaseLazyFragment
import com.common.base.viewmodel.EmptyViewModel
import com.common.widget.component.magicindicator.MagicIndicatorUtils
import com.tanlifei.app.common.constant.EnumConst
import com.tanlifei.app.databinding.FragmentClassmatecircleBinding
import com.tanlifei.app.main.ui.activity.MainActivity
import com.tanlifei.app.main.viewmodel.MainViewModel


/**
 * @desc:同学圈
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class CircleFragment : BaseLazyFragment<FragmentClassmatecircleBinding, EmptyViewModel>() {
    private val mTitleData =
        mutableListOf(EnumConst.CircleTabTag.CIRCLE.title, EnumConst.CircleTabTag.RECOMMEND.title)
    private lateinit var mFragmentAdapter: BasePagerAdapter
    private var mFragments: MutableList<Fragment> = ArrayList()
    private lateinit var mHomeViewModel: MainViewModel

    companion object {
        fun newInstance() = CircleFragment()
    }

    override fun createViewModel(): EmptyViewModel {
        return EmptyViewModel()
    }

    override fun onFirstVisibleToUser() {
        mHomeViewModel = (activity as MainActivity).mViewModel
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.tabIndicator)//为 view 增加 MarginTop 为状态栏高度
        bindFragments()
        MagicIndicatorUtils.initComMagicIndicator(
            activity,
            mBinding.tabIndicator,
            mBinding.viewPager,
            mTitleData
        )
        mBinding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mHomeViewModel.showRecommendPageFragment(position)
            }

        })
    }

    /**
     * 绑定tab 中各对应的Fragment
     */
    private fun bindFragments() {
        mFragments.add(FollowFragment.newInstance())
        mFragments.add(RecommendTabFragment.newInstance())
        mFragmentAdapter = BasePagerAdapter(childFragmentManager, mFragments)
        mBinding.viewPager.adapter = mFragmentAdapter
        mBinding.viewPager.currentItem = EnumConst.CircleTabTag.RECOMMEND.value
    }
}


