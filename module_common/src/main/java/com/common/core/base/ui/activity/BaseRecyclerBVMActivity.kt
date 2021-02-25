package com.common.core.base.ui.activity

import android.view.View
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
    BaseToolBarActivity<T, VM>() {

    /**
     * 初始化列表控件
     */
    protected fun initRefreshView(
        smartRefreshLayout: SmartRefreshLayout,
        refreshLoadingLayout: LoadingLayout,
        refreshRecycler: RecyclerView
    ) {
        RecyclerUtils.initViewModelObserve(
            smartRefreshLayout,
            refreshLoadingLayout,
            viewModel,
            this,
            setAdapter()
        )
        RecyclerUtils.initListener(smartRefreshLayout, refreshRecycler, viewModel)
        initRecyclerView(refreshRecycler)
        RecyclerUtils.initData(viewModel)
        refreshLoadingLayout.setRetryListener(View.OnClickListener {
            viewModel.refresh()
        })
    }

    /**
     * 初始化Recycler
     */
    private fun initRecyclerView(refreshRecycler: RecyclerView) {
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