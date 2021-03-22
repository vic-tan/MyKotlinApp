package com.common.core.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.viewmodel.BaseListViewModel
import com.common.core.base.viewmodel.BaseViewModel
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
abstract class BaseRecyclerBVMFragment<V : ViewBinding, T, VM : BaseListViewModel> :
    BaseLazyFragment<V>() {
    protected lateinit var mViewModel: VM

    protected abstract fun createViewModel(): VM

    protected var mAdapter: CommonRvAdapter<T>

    init {
        mAdapter = setAdapter()
    }

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
        mViewModel =
            ViewModelProvider(this, BaseViewModel.createViewModelFactory(createViewModel()))
                .get(vm::class.java)
        mViewModel.mApplication = requireActivity().application
    }

    override fun initView() {
        super.initView()
        mAdapter.mData = mViewModel.mData
    }


    /**
     * 初始化
     */
    fun initRecycler(
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
        requestData()
        mLoadingLayout.setRetryListener(View.OnClickListener {
            mViewModel.refresh()
        })
    }

    open fun requestData() {
        RecyclerUtils.initData(mViewModel)
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
    protected abstract fun setAdapter(): CommonRvAdapter<T>


}