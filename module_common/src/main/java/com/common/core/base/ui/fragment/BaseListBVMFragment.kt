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
    lateinit var smartRefreshLayout: SmartRefreshLayout
    lateinit var refreshLoadingLayout: LoadingLayout
    lateinit var refreshRecycler: RecyclerView

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

    /**
     * 懒加载第一次显示加载
     */
    override fun onFirstVisibleToUser() {
        init()
    }

    /**
     * 初始化
     */
    protected fun init() {
        initViewModelObserve()
        initListener()
        initRecyclerView()
        initData()
    }

    /**
     * 初始化列表控件
     */
    protected fun initListView(
        smartRefreshLayout: SmartRefreshLayout,
        refreshLoadingLayout: LoadingLayout,
        refreshRecycler: RecyclerView
    ) {
        this.smartRefreshLayout = smartRefreshLayout
        this.refreshLoadingLayout = refreshLoadingLayout
        this.refreshRecycler = refreshRecycler
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        viewModel.dataChanged.observe(this, Observer {
            setAdapter().notifyDataSetChanged()
            //请求数据完成
            when (it) {
                BaseListViewModel.DataChagedType.REFRESH -> smartRefreshLayout.finishRefresh()
                BaseListViewModel.DataChagedType.LOADMORE -> smartRefreshLayout.finishLoadMore()
            }
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

        }
        smartRefreshLayout.setOnLoadMoreListener {
            viewModel.loadMore()
        }
        smartRefreshLayout.setEnableLoadMore(false)
    }

    /**
     * 初化第一次请求数据
     */
    private fun initData() {
        viewModel.refresh()
    }

    /**
     * 初始化Recycler
     */
    private fun initRecyclerView() {
        refreshRecycler.layoutManager = setLinearLayoutManager()
        refreshRecycler.adapter = setAdapter()
    }


    /**
     * 设置 RecyclerView LayoutManager
     */
    protected fun setLinearLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(activity)
    }

    /**
     * 子类设置Adapter
     */
    protected abstract fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>

}