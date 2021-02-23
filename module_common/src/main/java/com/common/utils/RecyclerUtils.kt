package com.common.utils

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.common.core.base.viewmodel.BaseListViewModel
import com.common.widget.LoadingLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/19 15:52
 */
class RecyclerUtils {
    companion object {

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
            viewModel.dataChanged.observe(owner, Observer {
                //请求数据完成
                when (it) {
                    BaseListViewModel.DataChagedType.REFRESH -> {
                        smartRefreshLayout.finishRefresh()
                        adapter.notifyItemRangeChanged(0, viewModel.mData.size - 1)
                    }
                    BaseListViewModel.DataChagedType.LOADMORE -> {
                        smartRefreshLayout.finishLoadMore()
                        adapter.notifyItemRangeInserted(
                            viewModel.loadMoreStartPos,
                            viewModel.mData.size - 1
                        )
                    }
                    else -> {
                        adapter.notifyDataSetChanged()
                    }
                }
            })
            viewModel.uiBehavior.observe(owner, Observer {
                when (it) {
                    BaseListViewModel.UIType.NOTMOREDATA -> {
                        smartRefreshLayout.finishLoadMoreWithNoMoreData() //将不会再次触发加载更多事件
                    }
                    BaseListViewModel.UIType.EMPTYDATA -> refreshLoadingLayout.showEmpty()
                    BaseListViewModel.UIType.ERROR -> refreshLoadingLayout.showError()
                    BaseListViewModel.UIType.CONTENT -> {
                        refreshLoadingLayout.showContent()
                        smartRefreshLayout.setEnableLoadMore(true)
                    }
                }

            })
        }

        /**
         * 初始化监听
         */
        fun initListener(
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