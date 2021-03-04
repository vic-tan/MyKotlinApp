package com.common.core.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.common.core.base.viewmodel.BaseListViewModel
import com.common.core.base.viewmodel.BaseViewModel
import com.common.utils.RecyclerUtils
import com.common.widget.LoadingLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @desc:只有List 列表 ViewModel基类
 * @author: tanlifei
 * @date: 2021/2/7 18:17
 */
abstract class BaseRecyclerBVMFragment<T : ViewBinding, VM : BaseListViewModel> :
    BaseLazyFragment<T>() {
    protected lateinit var viewModel: VM


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
     * 初始化
     */
    fun initRecycler(
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
        RecyclerUtils.initListener(smartRefreshLayout, refreshRecycler, refreshLoadingLayout,viewModel)
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
        return RecyclerUtils.setLinearLayoutManager(context)
    }


    /**
     * 子类设置Adapter
     */
    protected abstract fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>

}