package com.common.core.base.ui.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.common.core.base.viewmodel.BaseListViewModel
import com.common.utils.RecyclerUtils
import com.common.widget.LoadingLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @desc:只有List列表 ViewModel基类
 * @author: tanlifei
 * @date: 2021/2/7 17:14
 */
abstract class BaseRecyclerBVMActivity<T : ViewBinding, VM : BaseListViewModel> :
    BaseToolBarActivity<T, VM>(){
    lateinit var smartRefreshLayout: SmartRefreshLayout
    lateinit var refreshLoadingLayout: LoadingLayout
    lateinit var refreshRecycler: RecyclerView


    /**
     * 初始化列表控件
     */
    protected fun initRefreshView(
        smartRefreshLayout: SmartRefreshLayout,
        refreshLoadingLayout: LoadingLayout,
        refreshRecycler: RecyclerView
    ) {
        this.smartRefreshLayout = smartRefreshLayout
        this.refreshLoadingLayout = refreshLoadingLayout
        this.refreshRecycler = refreshRecycler
        RecyclerUtils.initViewModelObserve(
            smartRefreshLayout,
            refreshLoadingLayout,
            viewModel,
            this,
            setAdapter()
        )
        RecyclerUtils.initListener(smartRefreshLayout,refreshRecycler, viewModel)
        initRecyclerView()
        RecyclerUtils.initData(viewModel)
    }
    /**
     * 初始化Recycler
     */
    private fun initRecyclerView() {
        refreshRecycler.layoutManager = setLinearLayoutManager()
        refreshRecycler.adapter = setAdapter()
        refreshRecycler.itemAnimator = null
    }

    /**
     * 设置 RecyclerView LayoutManager
     */
    protected open fun setLinearLayoutManager(): RecyclerView.LayoutManager {
        return RecyclerUtils.setLinearLayoutManager(this)
    }

    /**
     * 子类设置Adapter
     */
    protected abstract fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>


}