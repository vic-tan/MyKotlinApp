package com.common.core.base.ui.activity

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.viewmodel.BaseListViewModel
import com.common.utils.RecyclerUtils
import com.common.widget.LoadingLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @desc:只有List列表 ViewModel基类
 * @V :acticvity Recycler 列表布局文件
 * @T :  Recycler adapter 适配器实体对象
 * @VM :  BaseListViewModel  viewMode
 * @author: tanlifei
 * @date: 2021/2/7 17:14
 */
abstract class BaseRecyclerBVMActivity<V : ViewBinding, T, VM : BaseListViewModel> :
    BaseToolBarActivity<V, VM>() {

    protected var mAdapter: CommonRvAdapter<T>

    init {
        mAdapter = setAdapter()
    }


    /**
     * 初始化列表控件
     */
    protected fun initRefreshView(
        mSmartRefreshLayout: SmartRefreshLayout,
        mRefreshRecycler: RecyclerView,
        mLoadingLayout: LoadingLayout
    ) {
        RecyclerUtils.initViewModelObserve(
            mSmartRefreshLayout,
            mLoadingLayout,
            mViewModel,
            this,
            mAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
        )
        RecyclerUtils.initListener(
            mSmartRefreshLayout,
            mRefreshRecycler,
            mLoadingLayout,
            mViewModel
        )
        initRecyclerView(mRefreshRecycler)
        RecyclerUtils.initData(mViewModel)

    }

    /**
     * 初始化Recycler
     */
    private fun initRecyclerView(mRefreshRecycler: RecyclerView) {
        mRefreshRecycler.layoutManager = setLinearLayoutManager()
        mRefreshRecycler.adapter = mAdapter
        mRefreshRecycler.itemAnimator = null
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
    protected abstract fun setAdapter(): CommonRvAdapter<T>

}