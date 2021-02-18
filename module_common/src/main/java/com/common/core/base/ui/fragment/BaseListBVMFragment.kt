package com.common.core.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.common.core.base.holder.BaseVBViewHolder
import com.common.core.base.viewmodel.BaseListViewModel
import com.common.core.base.viewmodel.BaseViewModel
import com.common.widget.LoadingLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @desc:自定义List 列表 ViewModel基类
 * @author: tanlifei
 * @date: 2021/2/7 18:17
 */
abstract class BaseListBVMFragment<T : ViewBinding, VM : BaseListViewModel> :
    BaseLazyFragment<T>() {
    protected lateinit var viewModel: VM
        private set
    protected lateinit var smartRefreshLayout: SmartRefreshLayout
    protected lateinit var refreshLoadingLayout: LoadingLayout
    protected lateinit var refreshRecycler: RecyclerView

    protected abstract fun createViewModel(): VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        injectViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initBefore() {
        injectViewModel()
    }

    private fun injectViewModel() {
        val vm = createViewModel()
        viewModel = ViewModelProvider(this, BaseViewModel.createViewModelFactory(createViewModel()))
            .get(vm::class.java)
        viewModel.application = requireActivity().application
    }

    override fun onFirstVisibleToUser() {
        init()
    }

    protected fun init() {
        initViewModelObserve()
        initListener()
        initRecyclerView()
        initData()
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        viewModel.dataChanged.observe(this, Observer {
            setAdapter().notifyDataSetChanged()
        })
        viewModel.uiBehavior.observe(this, Observer {
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
    private fun initListener() {
        smartRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
            it.finishRefresh()
        }
        smartRefreshLayout.setOnLoadMoreListener {
            viewModel.loadMore()
            it.finishLoadMore()
        }
        smartRefreshLayout.setEnableLoadMore(false)

    }

    private fun initData() {
        viewModel.refresh()
    }

    private fun initRecyclerView() {
        refreshRecycler.layoutManager = setLinearLayoutManager()
        refreshRecycler.adapter = setAdapter()
    }


    protected fun setLinearLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(activity)
    }

    protected abstract fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>

}