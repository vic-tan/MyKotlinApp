package com.tanlifei.app.classmatecircle.frgment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ObjectUtils

import com.common.core.base.ui.fragment.BaseBVMFragment
import com.common.core.base.viewmodel.BaseViewModel
import com.common.core.magicindicator.MagicIndicatorUtils
import com.tanlifei.app.classmatecircle.viewmodel.RecommendTabViewModel
import com.tanlifei.app.databinding.FragmentClassmatecircleRecommendBinding

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
            MagicIndicatorUtils.initComMagicIndicator(
                activity,
                binding.tabIndicator,
                binding.viewPager,
                viewModel.mTabTitleData
            )
        })
        viewModel.loadingState.observe(this, Observer {
            when (it) {
                BaseViewModel.LoadType.LOADING -> binding.loadingLayout.showLoading()
                BaseViewModel.LoadType.DISMISS -> binding.loadingLayout.showContent()
                BaseViewModel.LoadType.ERROR -> binding.loadingLayout.showError()
            }
        })
    }
}