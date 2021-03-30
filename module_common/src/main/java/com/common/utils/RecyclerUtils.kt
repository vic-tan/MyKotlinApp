package com.common.utils

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.bean.ListDataChangePrams
import com.common.base.viewmodel.BaseListViewModel
import com.common.constant.GlobalEnumConst
import com.common.widget.component.extension.toast
import com.common.widget.LoadingLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/19 15:52
 */
object RecyclerUtils {
    private const val PRELOAD_COUNT = 5 //预加载阈值

    /**
     * 设置ViewModel的observe
     */
    fun initViewModelObserve(
        smartRefreshLayout: SmartRefreshLayout,
        refreshLoadingLayout: LoadingLayout,
        viewModel: BaseListViewModel,
        owner: LifecycleOwner,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    ) {
        dataChangeObserve(adapter, viewModel, owner)
        uiObserve(smartRefreshLayout, refreshLoadingLayout, viewModel, owner)
    }


    /**
     * 处理数据回调显示逻辑
     */
    fun dataChangeObserve(
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        viewModel: BaseListViewModel,
        owner: LifecycleOwner,
        block: (listDataChangePrams: ListDataChangePrams) -> Unit = { null }
    ) {
        viewModel.mDataChange.observe(owner, Observer {
            block(it)
            when (it.uiType) {
                /**上拉刷新**/
                GlobalEnumConst.UiType.REFRESH -> {
                    if (it.size == -1) {
                        adapter.notifyDataSetChanged()
                    } else {
                        adapter.notifyItemRangeChanged(0, viewModel.mData.size - 1)
                    }
                }
                /**下接刷新**/
                GlobalEnumConst.UiType.LOADMORE -> {
                    adapter.notifyItemRangeInserted(
                        viewModel.mData.size - it.size - 1,
                        viewModel.mData.size - 1
                    )

                }
                /**更新刷新**/
                GlobalEnumConst.UiType.NOTIFY -> {
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }


    /**
     * 处理请求情况显示布局
     */
    fun uiObserve(
        smartRefreshLayout: SmartRefreshLayout,
        refreshLoadingLayout: LoadingLayout,
        viewModel: BaseListViewModel,
        owner: LifecycleOwner,
        isHeaderOrFooter: Boolean = false,
        isMoreWithNoMoreData: Boolean = true
    ) {
        viewModel.mUiChange.observe(owner, Observer {
            when (it) {
                /**请求完成**/
                GlobalEnumConst.UiType.COMPLETE -> {
                    smartRefreshLayout.finishRefresh()
                    smartRefreshLayout.finishLoadMore()
                }
                /**有数据**/
                GlobalEnumConst.UiType.CONTENT -> {
                    refreshLoadingLayout.showContent()
                    smartRefreshLayout.setEnableLoadMore(true)
                }
                /**关闭上拉刷新但只有下拉时显示数据**/
                GlobalEnumConst.UiType.REFRESH_CONTENT -> {
                    refreshLoadingLayout.showContent()
                    smartRefreshLayout.setEnableLoadMore(false)
                }
                /**无数据**/
                GlobalEnumConst.UiType.EMPTY -> {
                    //是否存在头部或者尾部，存在不用显示布局，要显示头部或者尾部
                    if (isHeaderOrFooter) {
                        refreshLoadingLayout.showContent()
                        smartRefreshLayout.setEnableLoadMore(false)
                        smartRefreshLayout.setNoMoreData(false)
                    } else {
                        refreshLoadingLayout.showEmpty()
                    }
                }
                /**没有一下页数据**/
                GlobalEnumConst.UiType.NO_NEXT -> {
                    if (isMoreWithNoMoreData) {
                        smartRefreshLayout.finishLoadMoreWithNoMoreData() //将不会再次触发加载更多事件
                    } else {
                        smartRefreshLayout.setEnableLoadMore(false)
                        toast("没有更多数据了哦~")
                    }
                }
                /**报错界面**/
                GlobalEnumConst.UiType.ERROR -> {
                    if (viewModel.mData.isEmpty()) {
                        refreshLoadingLayout.showError()
                    }
                    smartRefreshLayout.setEnableLoadMore(false)
                }
            }
        })
    }

    /**
     * 初始化加载中控件监听
     */
    fun initAddOnScrollListenerListener(
        refreshRecycler: RecyclerView,
        viewModel: BaseListViewModel
    ) {
        refreshRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!NetworkUtils.isConnected())//没有网不加载，让用户手动加载
                    return
                // 获取 LayoutManger
                val layoutManager = recyclerView.layoutManager
                if (ObjectUtils.isNotEmpty(layoutManager)) {
                    // 如果列表正在往上滚动，并且表项最后可见表项索引值 等于 预加载阈值
                    if (dy > 0 && ObjectUtils.isNotEmpty(layoutManager!!.itemCount)
                        && getOutLast(layoutManager) >= getLoadCount(layoutManager)
                        && viewModel.mRefreshState == RefreshState.None
                    ) {
                        viewModel.loadMore()
                    }
                }
            }
        })
    }

    /**
     * 初始化加载中控件监听
     */
    fun initLoadingLayoutListener(
        refreshLoadingLayout: LoadingLayout,
        viewModel: BaseListViewModel
    ) {
        refreshLoadingLayout.setRetryListener(View.OnClickListener {
            viewModel.refresh()
        })
    }

    /**
     * 初始化刷新控件监听
     */
    fun initRefreshLayoutListener(
        smartRefreshLayout: SmartRefreshLayout,
        viewModel: BaseListViewModel
    ) {
        smartRefreshLayout.setOnRefreshListener {
            viewModel.refresh()

        }
        smartRefreshLayout.setOnLoadMoreListener {
            viewModel.loadMore()
        }
        smartRefreshLayout.setEnableLoadMore(false)
    }

    /**
     * 初始化监听
     */
    fun initListener(
        smartRefreshLayout: SmartRefreshLayout,
        refreshLoadingLayout: LoadingLayout,
        viewModel: BaseListViewModel
    ) {
        initRefreshLayoutListener(smartRefreshLayout, viewModel)
        initLoadingLayoutListener(refreshLoadingLayout, viewModel)
    }

    fun initSmartRefreshLayoutConfig(smartRefreshLayout: SmartRefreshLayout) {
        smartRefreshLayout.setEnableLoadMoreWhenContentNotFull(false)//是否在列表不满一页时候开启上拉加载功能
        smartRefreshLayout.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
    }

    fun getLoadCount(layoutManager: RecyclerView.LayoutManager): Int {
        var count = layoutManager.itemCount - 1 - PRELOAD_COUNT
        if (layoutManager.itemCount - 1 - PRELOAD_COUNT > 0) {
            return count
        }
        return Int.MAX_VALUE
    }

    fun getOutLast(layoutManager: RecyclerView.LayoutManager): Int {
        return when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val last = IntArray(layoutManager.spanCount)
                layoutManager.findLastVisibleItemPositions(last)
                Arrays.sort(last)
                last[last.size - 1]
            }
            is LinearLayoutManager -> {
                layoutManager.findLastVisibleItemPosition()
            }
            is GridLayoutManager -> {
                layoutManager.findLastVisibleItemPosition()
            }
            else -> layoutManager.itemCount - 1
        }
    }

    /**
     * 初始化Recycler
     */
    fun initRecyclerView(context: Context?, refreshRecycler: RecyclerView) {
        refreshRecycler.layoutManager = setLinearLayoutManager(context)
        refreshRecycler.itemAnimator = null
    }

    /**
     * 初化第一次请求数据
     */
    fun initData(viewModel: BaseListViewModel) {
        viewModel.refresh()
    }


    /**
     * 设置 RecyclerView LayoutManager
     */
    fun setLinearLayoutManager(context: Context?): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }
}