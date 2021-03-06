package com.onlineaginguniversity.home.ui.fragment

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.adapter.BasePagerAdapter
import com.common.base.listener.OnItemClickListener
import com.common.base.ui.fragment.BaseLazyFragment
import com.common.utils.GlideUtils
import com.common.utils.RecyclerUtils
import com.common.widget.component.extension.click
import com.common.widget.component.extension.newInstanceFragment
import com.common.widget.component.extension.toast
import com.common.widget.component.magicindicator.MagicIndicatorUtils
import com.onlineaginguniversity.circle.bean.CircleBean
import com.onlineaginguniversity.databinding.*
import com.onlineaginguniversity.home.adapter.HomeAdapter
import com.onlineaginguniversity.home.adapter.HomeBannerAdapter
import com.onlineaginguniversity.home.adapter.MenuAdapter
import com.onlineaginguniversity.home.bean.MenuBean
import com.onlineaginguniversity.home.viewmodel.HomeViewModel
import com.onlineaginguniversity.main.ui.activity.MainActivity
import com.onlineaginguniversity.main.viewmodel.MainViewModel
import com.youth.banner.indicator.RectangleIndicator

/**
 * @desc:首页
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class HomeFragment : BaseLazyFragment<FragmentHomeBinding, HomeViewModel>() {

    private val mTitleData = mutableListOf("免费直播", "精品课程", "高校直播")
    private lateinit var homeViewModel: MainViewModel
    private lateinit var header: HomeHeaderBinding
    private lateinit var footer: HomeFooterBinding
    private lateinit var adapter: HomeAdapter
    private lateinit var bannerAdapter: HomeBannerAdapter
    private lateinit var menuAdapter: MenuAdapter

    private lateinit var fragmentAdapter: BasePagerAdapter
    private var mFragments: MutableList<HomeRecommendFragment> = ArrayList()
    private var isFirstLoad = true


    override fun createViewModel(): HomeViewModel {
        return HomeViewModel()
    }

    override fun onFirstVisibleToUser() {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.topLayout)
        homeViewModel = (activity as MainActivity).mViewModel
        homeViewModel.getUser()
        initAdapter()
        initHeaderView()
        initFooterView()
        mViewModel.mHomeHeaderDataChanged.observe(this, Observer {
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
        adapter.setItemClickListener(object : OnItemClickListener<CircleBean> {
            override fun click(
                holder: ViewBinding,
                itemBean: CircleBean,
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
            mViewModel.requestList()

        }
        mBinding.refreshLayout.refreshLoadingLayout.setRetryListener(View.OnClickListener {
            mViewModel.requestList()
        })
        mBinding.refreshLayout.smartRefreshLayout.setEnableLoadMore(false)
        mViewModel.requestList()
    }


    private fun initHeaderView() {
        header = HomeHeaderBinding.inflate(layoutInflater)
        header.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        //banner
        bannerAdapter =
            HomeBannerAdapter(header.banner.viewPager2, mViewModel.mBannerData)
        header.banner.addBannerLifecycleObserver(this) //添加生命周期观察者
            .setAdapter(bannerAdapter).indicator = RectangleIndicator(context)

        //Menu
        menuAdapter = MenuAdapter()
        menuAdapter.mData = mViewModel.mMenuData
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
        header.recycler.adapter = menuAdapter
        header.recycler.layoutManager = GridLayoutManager(context, 4)
        header.recycler.itemAnimator = null


        //ViewPager 推荐
        mFragments.add(newInstanceFragment {})
        mFragments.add(newInstanceFragment {})
        mFragments.add(newInstanceFragment {})
        fragmentAdapter = BasePagerAdapter(childFragmentManager, mFragments)
        header.viewPager.adapter = fragmentAdapter
        header.viewPager.offscreenPageLimit = 3
        MagicIndicatorUtils.initComMagicIndicator(
            activity,
            header.tabIndicator,
            header.viewPager,
            mTitleData
        )

        header.more.click {
            toast(it.text.toString())
        }

    }

    private fun initFooterView() {
        footer = HomeFooterBinding.inflate(layoutInflater)
        footer.root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        footer.adjustment.click {
            toast(it.text.toString())
        }
    }

    private fun addHeaderOrFooter() {
        adapter.removeHeaderView(header)
        adapter.addHeaderView(header)
        useBanner()
        menuAdapter.notifyDataSetChanged()
        if (ObjectUtils.isNotEmpty(mViewModel.mAdsnoviceData)) {
            var headerBinding = header as HomeHeaderBinding
            GlideUtils.load(context, mViewModel.mAdsnoviceData[0].image, headerBinding.ads)
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
        header.banner.setDatas(mViewModel.mBannerData)
        header.banner.currentItem = 1
        bannerAdapter.notifyDataSetChanged()
    }
}