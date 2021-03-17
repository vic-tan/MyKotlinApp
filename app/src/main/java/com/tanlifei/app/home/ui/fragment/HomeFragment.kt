package com.tanlifei.app.home.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.adapter.BasePagerAdapter
import com.common.core.base.ui.fragment.BaseBVMFragment
import com.common.core.base.viewmodel.BaseListViewModel
import com.common.core.magicindicator.MagicIndicatorUtils
import com.common.utils.GlideUtils
import com.common.utils.RecyclerUtils
import com.tanlifei.app.classmatecircle.adapter.CommentAdapter
import com.tanlifei.app.classmatecircle.bean.CommentBean
import com.tanlifei.app.databinding.FragmentHomeBinding
import com.tanlifei.app.databinding.HomeHeaderBannerBinding
import com.tanlifei.app.home.adapter.HomeBannerAdapter
import com.tanlifei.app.home.adapter.MenuAdapter
import com.tanlifei.app.home.viewmodel.HomeViewModel
import com.youth.banner.indicator.RectangleIndicator

/**
 * @desc:首页
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class HomeFragment : BaseBVMFragment<FragmentHomeBinding, HomeViewModel>() {

    private val mTitleData = mutableListOf("免费直播", "精品课程", "高校直播")
    private lateinit var header: ViewBinding
    private lateinit var adapter: CommentAdapter
    private lateinit var bannerAdapter: HomeBannerAdapter
    private lateinit var menuAdapter: MenuAdapter

    private lateinit var fragmentAdapter: BasePagerAdapter
    private var mFragments: MutableList<Fragment> = ArrayList()

    companion object {
        fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun createViewModel(): HomeViewModel {
        return HomeViewModel()
    }

    override fun onFirstVisibleToUser() {
        BarUtils.addMarginTopEqualStatusBarHeight(binding.topLayout)
        initAdapter()
        initHeaderView()
        viewModel.homeHeaderDataChanged.observe(this, Observer {
            binding.refreshLayout.refreshLoadingLayout.showContent()
            binding.refreshLayout.smartRefreshLayout.finishRefresh()
            addHeader()
        })
        viewModel.loadingState.observe(this, Observer {
            when (it) {
                BaseListViewModel.UIType.NOTMOREDATA -> {
                    binding.refreshLayout.smartRefreshLayout.finishLoadMoreWithNoMoreData() //将不会再次触发加载更多事件
                }
                BaseListViewModel.UIType.EMPTYDATA -> binding.refreshLayout.refreshLoadingLayout.showContent()
                BaseListViewModel.UIType.ERROR -> binding.refreshLayout.refreshLoadingLayout.showError()
                BaseListViewModel.UIType.CONTENT -> binding.refreshLayout.refreshLoadingLayout.showContent()
            }
        })
    }

    private fun initAdapter() {
        adapter = CommentAdapter()
        adapter.mData = viewModel.mData as MutableList<CommentBean>
        binding.refreshLayout.refreshRecycler.adapter = adapter
        binding.refreshLayout.refreshRecycler.layoutManager =
            RecyclerUtils.setLinearLayoutManager(context)
        binding.refreshLayout.refreshRecycler.itemAnimator = null
        binding.refreshLayout.smartRefreshLayout.setOnRefreshListener {
            viewModel.requestRefresh()

        }
        binding.refreshLayout.refreshLoadingLayout.setRetryListener(View.OnClickListener {
            viewModel.requestRefresh()
        })
        binding.refreshLayout.smartRefreshLayout.setEnableLoadMore(false)
        viewModel.requestRefresh()
    }

    private fun initHeaderView() {
        header = HomeHeaderBannerBinding.inflate(layoutInflater)
        header.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        var headerBinding = header as HomeHeaderBannerBinding

        //banner
        bannerAdapter = HomeBannerAdapter(context, viewModel.bannerData)
        headerBinding.banner.addBannerLifecycleObserver(this) //添加生命周期观察者
            .setAdapter(bannerAdapter).indicator = RectangleIndicator(context)


        //Menu
        menuAdapter = MenuAdapter(context)
        menuAdapter.mData = viewModel.menuData
        headerBinding.recycler.adapter = menuAdapter
        headerBinding.recycler.layoutManager = GridLayoutManager(context, 4)
        headerBinding.recycler.itemAnimator = null


        //ViewPager 推荐
        mFragments.add(HomeRecommendFragment.newInstance())
        mFragments.add(HomeRecommendFragment.newInstance())
        mFragments.add(HomeRecommendFragment.newInstance())
        fragmentAdapter = BasePagerAdapter(childFragmentManager, mFragments)
        headerBinding.viewPager.adapter = fragmentAdapter
        headerBinding.viewPager.offscreenPageLimit = 3
        MagicIndicatorUtils.initComMagicIndicator(
            activity,
            headerBinding.tabIndicator,
            headerBinding.viewPager,
            mTitleData
        )

    }

    private fun addHeader() {
        adapter.removeHeaderView(header)
        adapter.addHeaderView(header)
        useBanner()
        menuAdapter.notifyDataSetChanged()
        if (ObjectUtils.isNotEmpty(viewModel.adsnoviceData)) {
            var headerBinding = header as HomeHeaderBannerBinding
            GlideUtils.load(context, viewModel.adsnoviceData[0].image, headerBinding.ads)
        }
    }


    private fun useBanner() {
        bannerAdapter.notifyDataSetChanged()
    }
}