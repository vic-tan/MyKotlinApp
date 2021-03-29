package com.common.base.ui.fragment

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.common.base.adapter.BaseRvAdapter
import com.common.base.listener.OnItemClickListener
import com.common.base.viewmodel.BaseListViewModel
import com.common.base.viewmodel.BaseViewModel
import com.common.utils.RecyclerUtils
import com.common.widget.LoadingLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @desc:只有List 列表 ViewModel基类
 * @V :acticvity Recycler 列表布局文件
 * @T :  Recycler adapter 适配器实体对象
 * @VM :  BaseListViewModel  viewMode
 * @author: tanlifei
 * @date: 2021/2/7 18:17
 */
abstract class BaseRvFragment<V : ViewBinding, VM : BaseListViewModel, T> :
    BaseLazyFragment<V, VM>() {
    var mAdapter: BaseRvAdapter<T>

    init {
        mAdapter = setAdapter()
    }

    override fun initBefore() {
        injectViewModel()
    }

    private fun injectViewModel() {
        val vm = createViewModel()
        mViewModel =
            ViewModelProvider(this, BaseViewModel.createViewModelFactory(createViewModel()))
                .get(vm::class.java)
        mViewModel.mApplication = requireActivity().application
    }

    override fun initView() {
        super.initView()
        initAdapter()
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
     * 初始化
     */
    fun initRecycler(
        mSmartRefreshLayout: SmartRefreshLayout,
        mRefreshRecycler: RecyclerView,
        mLoadingLayout: LoadingLayout
    ) {
        dataChangeObserve()
        uiObserve(mSmartRefreshLayout, mLoadingLayout)
        RecyclerUtils.initListener(
            mSmartRefreshLayout,
            mLoadingLayout,
            mViewModel
        )
        addOnScrollListener(mRefreshRecycler)
        setSmartRefreshLayoutConfig(mSmartRefreshLayout)
        initRecyclerView(mRefreshRecycler)
        requestData()
        mLoadingLayout.setRetryListener(View.OnClickListener {
            mViewModel.refresh()
        })
    }

    open fun requestData() {
        RecyclerUtils.initData(mViewModel)
    }

    open fun addOnScrollListener(mRefreshRecycler: RecyclerView) {
        RecyclerUtils.initAddOnScrollListenerListener(mRefreshRecycler, mViewModel)
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
     * 初始化Recycler
     */
    private fun initRecyclerView(mRefreshRecycler: RecyclerView) {
        mRefreshRecycler.layoutManager = setLinearLayoutManager()
        mRefreshRecycler.adapter = mAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRefreshRecycler.itemAnimator = null
    }


    /**
     * 设置 RecyclerView LayoutManager
     */
    protected open fun setLinearLayoutManager(): RecyclerView.LayoutManager {
        return RecyclerUtils.setLinearLayoutManager(context)
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