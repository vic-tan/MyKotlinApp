package com.common.base.ui.activity

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.common.base.adapter.BaseRvAdapter
import com.common.base.listener.OnItemClickListener
import com.common.base.viewmodel.BaseListViewModel
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
abstract class BaseRvActivity<V : ViewBinding, T, VM : BaseListViewModel> :
    BaseToolBarActivity<V, VM>() {

    var mAdapter: BaseRvAdapter<T>

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
        initAdapter()
        dataChangeObserve()
        uiObserve(mSmartRefreshLayout, mLoadingLayout)
        RecyclerUtils.initListener(
            mSmartRefreshLayout,
            mRefreshRecycler,
            mLoadingLayout,
            mViewModel
        )
        setSmartRefreshLayoutConfig(mSmartRefreshLayout)
        initRecyclerView(mRefreshRecycler)
        RecyclerUtils.initData(mViewModel)
    }

    /**
     * 初始化initAdapter()
     */
    private fun initAdapter() {
        mAdapter.mData = mViewModel.mData
        mAdapter.setItemClickListener(object : OnItemClickListener<T> {
            override fun click(holder: ViewBinding, itemBean: T, v: View, position: Int) {
                itemClick(holder, itemBean, v, position)
            }
        })
    }

    /**
     * 初始化Recycler
     */
    private fun initRecyclerView(mRefreshRecycler: RecyclerView) {
        mRefreshRecycler.layoutManager = setLinearLayoutManager()
        mRefreshRecycler.adapter = mAdapter
        mRefreshRecycler.itemAnimator = null
    }

    open fun requestData() {
        RecyclerUtils.initData(mViewModel)
    }


    open fun dataChangeObserve(
    ) {
        RecyclerUtils.dataChangeObserve(
            mAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>,
            mViewModel,
            this
        )
    }


    open fun uiObserve(
        smartRefreshLayout: SmartRefreshLayout,
        refreshLoadingLayout: LoadingLayout
    ) {
        RecyclerUtils.uiObserve(smartRefreshLayout, refreshLoadingLayout, mViewModel, this)
    }


    open fun setSmartRefreshLayoutConfig(mSmartRefreshLayout: SmartRefreshLayout) {
        RecyclerUtils.initSmartRefreshLayoutConfig(mSmartRefreshLayout)
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
    protected abstract fun setAdapter(): BaseRvAdapter<T>

    /**
     * item 点击事件
     */
    open fun itemClick(holder: ViewBinding, itemBean: T, v: View, position: Int) {
    }

}