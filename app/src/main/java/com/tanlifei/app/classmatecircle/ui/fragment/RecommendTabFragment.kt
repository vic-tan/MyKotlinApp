package com.tanlifei.app.classmatecircle.ui.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.common.base.ui.fragment.BaseLazyFragment
import com.common.constant.EnumConst
import com.common.utils.GlideUtils
import com.common.widget.component.extension.color
import com.common.widget.component.extension.setVisible
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
    BaseLazyFragment<FragmentClassmatecircleRecommendBinding, RecommendTabViewModel>() {

    companion object {
        fun newInstance() = RecommendTabFragment()
    }

    override fun createViewModel(): RecommendTabViewModel {
        return RecommendTabViewModel(childFragmentManager)
    }

    override fun onFirstVisibleToUser() {
        mViewModel.requestCategoryList()
        initViewModelObserve()
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        mViewModel.mDataChanged.observe(this, Observer {
            mBinding.viewPager.adapter = mViewModel.mTabAdapter
            customLayoutTab()
        })
        mViewModel.mUiChange.observe(this, Observer {
            when (it) {
                EnumConst.UiType.LOADING -> mBinding.loadingLayout.showLoading()
                EnumConst.UiType.COMPLETE -> mBinding.loadingLayout.showContent()
                EnumConst.UiType.ERROR -> mBinding.loadingLayout.showError()
            }
        })
    }

    private fun customLayoutTab() {
        val commonNavigator = CommonNavigator(context)
        commonNavigator.scrollPivotX = 0.25f
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mViewModel.mData.size
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
                lineRight.setVisible(index == mViewModel.mData.size - 1)
                titleText.text = mViewModel.mData[index].name
                commonPagerTitleView.setContentView(customLayout)
                commonPagerTitleView.onPagerTitleChangeListener = object :
                    OnPagerTitleChangeListener {
                    override fun onSelected(index: Int, totalCount: Int) {
                        titleText.setTextColor(color(R.color.theme))
                        GlideUtils.load(context, mViewModel.mData[index].iconSelect, titleImg)
                    }

                    override fun onDeselected(index: Int, totalCount: Int) {
                        titleText.setTextColor(color(R.color.color_96A8BB))
                        GlideUtils.load(context, mViewModel.mData[index].iconDefault, titleImg)
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
                commonPagerTitleView.setOnClickListener { mBinding.viewPager.currentItem = index }
                return commonPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                return null
            }
        }
        mBinding.tabIndicator.navigator = commonNavigator
        mBinding.viewPager.offscreenPageLimit = 10//有内存泄漏暂时先用这句处理内存泄漏
        ViewPagerHelper.bind(mBinding.tabIndicator, mBinding.viewPager)
    }
}