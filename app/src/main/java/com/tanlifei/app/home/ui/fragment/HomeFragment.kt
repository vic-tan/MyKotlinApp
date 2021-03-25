package com.tanlifei.app.home.ui.fragment

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.cofing.enumconst.UiType
import com.common.core.base.adapter.BasePagerAdapter
import com.common.core.base.listener.OnItemClickListener
import com.common.core.base.ui.fragment.BaseBVMFragment
import com.common.core.magicindicator.MagicIndicatorUtils
import com.common.utils.GlideUtils
import com.common.utils.RecyclerUtils
import com.common.utils.extension.click
import com.common.utils.extension.toast
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.databinding.*
import com.tanlifei.app.home.adapter.HomeAdapter
import com.tanlifei.app.home.adapter.HomeBannerAdapter
import com.tanlifei.app.home.adapter.MenuAdapter
import com.tanlifei.app.home.bean.MenuBean
import com.tanlifei.app.home.viewmodel.HomeViewModel
import com.tanlifei.app.main.ui.activity.MainActivity
import com.tanlifei.app.main.viewmodel.MainViewModel
import com.youth.banner.indicator.RectangleIndicator

/**
 * @desc:首页
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class HomeFragment : BaseBVMFragment<FragmentHomeBinding, HomeViewModel>() {

    private val mTitleData = mutableListOf("免费直播", "精品课程", "高校直播")
    private lateinit var homeViewModel: MainViewModel
    private lateinit var header: ViewBinding
    private lateinit var footer: ViewBinding
    private lateinit var adapter: HomeAdapter
    private lateinit var bannerAdapter: HomeBannerAdapter
    private lateinit var menuAdapter: MenuAdapter

    private lateinit var fragmentAdapter: BasePagerAdapter
    private var mFragments: MutableList<HomeRecommendFragment> = ArrayList()
    private var isFirstLoad = true

    companion object {
        fun newInstance() = HomeFragment()
    }


    override fun createViewModel(): HomeViewModel {
        return HomeViewModel()
    }

    override fun onFirstVisibleToUser() {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.topLayout)
        homeViewModel = (activity as MainActivity).mViewModel
        homeViewModel.getUser()
        initAdapter()
        initHeaderView()
        initHFooterView()
        mViewModel.homeHeaderDataChanged.observe(this, Observer {
            mBinding.refreshLayout.refreshLoadingLayout.showContent()
            mBinding.refreshLayout.smartRefreshLayout.finishRefresh()
            addHeaderOrFooter()
            adapter.notifyDataSetChanged()
        })
        RecyclerUtils.uiObserve(
            mBinding.refreshLayout.smartRefreshLayout,
            mBinding.refreshLayout.refreshLoadingLayout,
            mViewModel, this, true
        )
        homeViewModel.mRefreshUserInfo.observe(this, Observer {
            GlideUtils.loadAvatar(this.context, it.avatar, mBinding.userCover)
        })
    }

    private fun initAdapter() {
        adapter = HomeAdapter()
        adapter.mData = mViewModel.mData
        adapter.setItemClickListener(object : OnItemClickListener<ClassmateCircleBean> {
            override fun click(
                holder: ViewBinding,
                itemBean: ClassmateCircleBean,
                v: View,
                position: Int
            ) {
                when (holder) {
                    is ItemHomeBinding -> {
                        when (v) {
                            holder.item -> {
                                toast(itemBean.nickName)
                            }
                        }
                    }
                }
            }


        })
        mBinding.refreshLayout.refreshRecycler.adapter = adapter
        mBinding.refreshLayout.refreshRecycler.layoutManager =
            RecyclerUtils.setLinearLayoutManager(context)
        mBinding.refreshLayout.refreshRecycler.itemAnimator = null
        mBinding.refreshLayout.smartRefreshLayout.setOnRefreshListener {
            isFirstLoad = false
            mViewModel.requestRefresh()

        }
        mBinding.refreshLayout.refreshLoadingLayout.setRetryListener(View.OnClickListener {
            mViewModel.requestRefresh()
        })
        mBinding.refreshLayout.smartRefreshLayout.setEnableLoadMore(false)
        mViewModel.requestRefresh()
    }


    private fun initHeaderView() {
        header = HomeHeaderBinding.inflate(layoutInflater)
        header.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        var headerBinding = header as HomeHeaderBinding

        //banner
        bannerAdapter =
            HomeBannerAdapter(headerBinding.banner.viewPager2, mViewModel.bannerData)
        headerBinding.banner.addBannerLifecycleObserver(this) //添加生命周期观察者
            .setAdapter(bannerAdapter).indicator = RectangleIndicator(context)

        //Menu
        menuAdapter = MenuAdapter()
        menuAdapter.mData = mViewModel.menuData
        menuAdapter.setItemClickListener(object :
            OnItemClickListener<MenuBean> {
            override fun click(
                itemBinding: ViewBinding,
                itemBean: MenuBean,
                v: View,
                position: Int
            ) {
                itemBinding as ItemHomeMenuBinding
                when (v) {
                    itemBinding.item -> toast(itemBean.name)
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
            toast(it.text.toString())
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
            toast(it.text.toString())
        }
    }

    private fun addHeaderOrFooter() {
        adapter.removeHeaderView(header)
        adapter.addHeaderView(header)
        useBanner()
        menuAdapter.notifyDataSetChanged()
        if (ObjectUtils.isNotEmpty(mViewModel.adsnoviceData)) {
            var headerBinding = header as HomeHeaderBinding
            GlideUtils.load(context, mViewModel.adsnoviceData[0].image, headerBinding.ads)
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
        var headerBinding = header as HomeHeaderBinding
        headerBinding.banner.setDatas(mViewModel.bannerData)
        headerBinding.banner.currentItem = 1
        bannerAdapter.notifyDataSetChanged()
    }
}