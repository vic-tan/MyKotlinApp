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
import com.common.core.base.viewmodel.BaseListViewModel
import com.common.widget.LoadingLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/19 15:52
 */
class RecyclerUtils {
    companion object {
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
            dataChangedObserve(smartRefreshLayout, adapter, viewModel, owner)
            uiBehaviorObserve(smartRefreshLayout, refreshLoadingLayout, viewModel, owner)
        }


        /**
         * 处理数据回调显示逻辑
         */
        fun dataChangedObserve(
            smartRefreshLayout: SmartRefreshLayout,
            adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
            viewModel: BaseListViewModel,
            owner: LifecycleOwner
        ) {
            viewModel.dataChanged.observe(owner, Observer {
                when (it) {
                    BaseListViewModel.DataChagedType.REFRESH -> {
                        smartRefreshLayout.finishRefresh()
                        smartRefreshLayout.setEnableLoadMore(true)
                        if (viewModel.mData.isEmpty()) {
                            smartRefreshLayout.finishLoadMoreWithNoMoreData() //将不会再次触发加载更多事件
                            adapter.notifyDataSetChanged()
                        } else {
                            smartRefreshLayout.setEnableLoadMore(true)
                            adapter.notifyItemRangeChanged(0, viewModel.mData.size - 1)
                        }
                    }
                    BaseListViewModel.DataChagedType.LOADMORE -> {
                        smartRefreshLayout.finishLoadMore()
                        adapter.notifyItemRangeInserted(
                            viewModel.loadMoreStartPos,
                            viewModel.mData.size - 1
                        )

                    }
                    BaseListViewModel.DataChagedType.ERROE -> {
                        smartRefreshLayout.finishRefresh()
                        smartRefreshLayout.finishLoadMore()
                        adapter.notifyDataSetChanged()
                    }
                    else -> {
                        smartRefreshLayout.finishRefresh()
                        smartRefreshLayout.finishLoadMore()
                        adapter.notifyDataSetChanged()
                    }
                }
            })
        }

        /**
         * 处理请求情况显示布局
         */
        fun uiBehaviorObserve(
            smartRefreshLayout: SmartRefreshLayout,
            refreshLoadingLayout: LoadingLayout,
            viewModel: BaseListViewModel,
            owner: LifecycleOwner,
            isHeaderOrFooter: Boolean = false
        ) {
            viewModel.uiBehavior.observe(owner, Observer {
                when (it) {
                    BaseListViewModel.UIType.NOTMOREDATA -> {
                        smartRefreshLayout.finishLoadMoreWithNoMoreData() //将不会再次触发加载更多事件
                    }
                    BaseListViewModel.UIType.EMPTYDATA -> if (isHeaderOrFooter) refreshLoadingLayout.showContent() else refreshLoadingLayout.showEmpty()
                    BaseListViewModel.UIType.ERROR -> refreshLoadingLayout.showError()
                    BaseListViewModel.UIType.CONTENT -> refreshLoadingLayout.showContent()
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
            refreshRecycler: RecyclerView,
            refreshLoadingLayout: LoadingLayout,
            viewModel: BaseListViewModel
        ) {
            initRefreshLayoutListener(smartRefreshLayout, viewModel)
            initLoadingLayoutListener(refreshLoadingLayout, viewModel)
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
                            && viewModel.state == RefreshState.None
                        ) {
                            viewModel.state = RefreshState.Loading
                            viewModel.loadMore()
                        }
                    }
                }
            })
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
}