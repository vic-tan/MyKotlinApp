package com.tanlifei.app.home.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.adapter.BasePagerAdapter
import com.common.core.base.adapter.CommonRvHolder
import com.common.core.base.listener.OnItemClickListener
import com.common.core.base.listener.OnMultiItemListener
import com.common.core.base.ui.fragment.BaseBVMFragment
import com.common.core.base.viewmodel.BaseListViewModel
import com.common.core.magicindicator.MagicIndicatorUtils
import com.common.utils.GlideUtils
import com.common.utils.RecyclerUtils
import com.common.utils.extension.click
import com.hjq.toast.ToastUtils
import com.tanlifei.app.databinding.*
import com.tanlifei.app.home.adapter.HomeAdapter
import com.tanlifei.app.home.adapter.HomeBannerAdapter
import com.tanlifei.app.home.adapter.MenuAdapter
import com.tanlifei.app.home.bean.MenuBean
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
    private lateinit var footer: ViewBinding
    private lateinit var adapter: HomeAdapter
    private lateinit var bannerAdapter: HomeBannerAdapter
    private lateinit var menuAdapter: MenuAdapter

    private lateinit var fragmentAdapter: BasePagerAdapter
    private var mFragments: MutableList<HomeRecommendFragment> = ArrayList()
    private var isFirstLoad = true

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
        initHFooterView()
        viewModel.homeHeaderDataChanged.observe(this, Observer {
            binding.refreshLayout.refreshLoadingLayout.showContent()
            binding.refreshLayout.smartRefreshLayout.finishRefresh()
            addHeaderOrFooter()
            adapter.notifyDataSetChanged()
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
        adapter = HomeAdapter(context)
        adapter.mData = viewModel.mData
        adapter.setOnItemChildClickListener(object : OnMultiItemListener {
            override fun onItemClick(v: View, holder: CommonRvHolder<ViewBinding>, position: Int) {
                when (holder.binding) {
                    is ItemHomeBinding -> {
                        when (v) {
                            (holder.binding as ItemHomeBinding).item -> {
                                ToastUtils.show(viewModel.mData[position].nickName)
                            }
                        }
                    }
                }
            }
        })
        binding.refreshLayout.refreshRecycler.adapter = adapter
        binding.refreshLayout.refreshRecycler.layoutManager =
            RecyclerUtils.setLinearLayoutManager(context)
        binding.refreshLayout.refreshRecycler.itemAnimator = null
        binding.refreshLayout.smartRefreshLayout.setOnRefreshListener {
            isFirstLoad = false
            viewModel.requestRefresh()

        }
        binding.refreshLayout.refreshLoadingLayout.setRetryListener(View.OnClickListener {
            viewModel.requestRefresh()
        })
        binding.refreshLayout.smartRefreshLayout.setEnableLoadMore(false)
        viewModel.requestRefresh()
    }


    private fun initHeaderView() {
        header = HomeHeaderBinding.inflate(layoutInflater)
        header.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        var headerBinding = header as HomeHeaderBinding

        //banner
        bannerAdapter = HomeBannerAdapter(context, viewModel.bannerData)
        headerBinding.banner.addBannerLifecycleObserver(this) //添加生命周期观察者
            .setAdapter(bannerAdapter).indicator = RectangleIndicator(context)


        //Menu
        menuAdapter = MenuAdapter(context)
        menuAdapter.mData = viewModel.menuData
        menuAdapter.setItemClickListener(object :
            OnItemClickListener<ItemHomeMenuBinding, MenuBean> {
            override fun click(
                itemBinding: ItemHomeMenuBinding,
                itemBean: MenuBean,
                v: View,
                position: Int
            ) {
                when (v) {
                    itemBinding.item -> ToastUtils.show(itemBean.name)
                }
            }
        })
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

        headerBinding.more.click {
            ToastUtils.show(it.text.toString())
        }

    }

    private fun initHFooterView() {
        footer = HomeFooterBinding.inflate(layoutInflater)
        footer.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        var footerBinding = footer as HomeFooterBinding
        footerBinding.adjustment.click {
            ToastUtils.show(it.text.toString())
        }
    }

    private fun addHeaderOrFooter() {
        adapter.removeHeaderView(header)
        adapter.addHeaderView(header)
        useBanner()
        menuAdapter.notifyDataSetChanged()
        if (ObjectUtils.isNotEmpty(viewModel.adsnoviceData)) {
            var headerBinding = header as HomeHeaderBinding
            GlideUtils.load(context, viewModel.adsnoviceData[0].image, headerBinding.ads)
        }
        if (!isFirstLoad) {
            for (fragment in mFragments) {
                fragment.refresh()
            }
        }
        adapter.removeFooterView(footer)
        adapter.addFooterView(footer)
    }


    private fun useBanner() {
        bannerAdapter.notifyDataSetChanged()
    }
}