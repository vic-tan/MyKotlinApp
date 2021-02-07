package com.tanlifei.app.classmatecircle.frgment

import android.os.Bundle
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.BarUtils
import com.common.R
import com.common.core.adapter.ComFragPagerAdapter
import com.common.core.base.ui.fragment.BaseLazyFragment
import com.common.core.magicindicator.MagicIndicatorUtils
import com.common.utils.MyLogTools
import com.common.utils.ResUtils
import com.tanlifei.app.databinding.FragmentClassmatecircleBinding
import java.util.*


/**
 * @desc:同学圈
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class ClassmateCircleFragment : BaseLazyFragment<FragmentClassmatecircleBinding>() {
    private val mTitleData = listOf("关注", "推荐")
    private lateinit var fragmentAdapter: ComFragPagerAdapter
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
        MyLogTools.show("${BarUtils.getActionBarHeight()}")
        BarUtils.addMarginTopEqualStatusBarHeight(binding.tabIndicator)
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
        mFragments.add(RecommendFragment.newInstance())
        fragmentAdapter = ComFragPagerAdapter(this, mFragments)
        binding.viewPager.adapter = fragmentAdapter
    }


}


