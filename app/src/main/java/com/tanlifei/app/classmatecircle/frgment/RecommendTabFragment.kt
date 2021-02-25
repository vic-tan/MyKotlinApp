package com.tanlifei.app.classmatecircle.frgment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.common.core.base.ui.fragment.BaseBVMFragment
import com.common.core.base.viewmodel.BaseViewModel
import com.common.utils.GlideUtils
import com.common.utils.ResUtils
import com.tanlifei.app.R
import com.tanlifei.app.classmatecircle.viewmodel.RecommendTabViewModel
import com.tanlifei.app.databinding.FragmentClassmatecircleRecommendBinding
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView.OnPagerTitleChangeListener

/**
 * @desc:同学圈推荐
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class RecommendTabFragment :
    BaseBVMFragment<FragmentClassmatecircleRecommendBinding, RecommendTabViewModel>() {

    companion object {
        fun newInstance(): RecommendTabFragment {
            val fragment = RecommendTabFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createViewModel(): RecommendTabViewModel {
        return RecommendTabViewModel(childFragmentManager)
    }

    override fun onFirstVisibleToUser() {
        viewModel.requestCategoryList()
        initViewModelObserve()
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        viewModel.dataChanged.observe(this, Observer {
            binding.viewPager.adapter = viewModel.fragmentAdapter
            customLayoutTab()
        })
        viewModel.loadingState.observe(this, Observer {
            when (it) {
                BaseViewModel.LoadType.LOADING -> binding.loadingLayout.showLoading()
                BaseViewModel.LoadType.DISMISS -> binding.loadingLayout.showContent()
                BaseViewModel.LoadType.ERROR -> binding.loadingLayout.showError()
            }
        })
    }

    private fun customLayoutTab() {
        val commonNavigator = CommonNavigator(context)
        commonNavigator.scrollPivotX = 0.25f
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return viewModel.mData.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                var commonPagerTitleView = CommonPagerTitleView(context)

                // load custom layout
                val customLayout: View =
                    LayoutInflater.from(context).inflate(R.layout.item_recommend_tab, null)
                val titleImg =
                    customLayout.findViewById<View>(R.id.icon) as ImageView
                val titleText =
                    customLayout.findViewById<View>(R.id.name) as TextView
                val lineRight =
                    customLayout.findViewById<View>(R.id.line_right) as View
                when (index) {
                    viewModel.mData.size - 1 -> lineRight.visibility = View.VISIBLE
                    else -> lineRight.visibility = View.GONE
                }
                titleText.text = viewModel.mData[index].name
                commonPagerTitleView.setContentView(customLayout)
                commonPagerTitleView.onPagerTitleChangeListener = object :
                    OnPagerTitleChangeListener {
                    override fun onSelected(index: Int, totalCount: Int) {
                        titleText.setTextColor(ResUtils.getColor(R.color.theme_color))
                        GlideUtils.load(context, viewModel.mData[index].iconSelect, titleImg)
                    }

                    override fun onDeselected(index: Int, totalCount: Int) {
                        titleText.setTextColor(ResUtils.getColor(R.color.color_96A8BB))
                        GlideUtils.load(context, viewModel.mData[index].iconDefault, titleImg)
                    }

                    override fun onLeave(
                        index: Int,
                        totalCount: Int,
                        leavePercent: Float,
                        leftToRight: Boolean
                    ) {
                    }

                    override fun onEnter(
                        index: Int,
                        totalCount: Int,
                        enterPercent: Float,
                        leftToRight: Boolean
                    ) {
                    }
                }
                commonPagerTitleView.setOnClickListener { binding.viewPager.currentItem = index }
                return commonPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                return null
            }
        }
        binding.tabIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(binding.tabIndicator, binding.viewPager)
    }
}