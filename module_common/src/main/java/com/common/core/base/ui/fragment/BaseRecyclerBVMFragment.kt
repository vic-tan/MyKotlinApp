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
    protected lateinit var mViewModel: VM

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
        mViewModel = ViewModelProvider(this, BaseViewModel.createViewModelFactory(createViewModel()))
            .get(vm::class.java)
        mViewModel.mApplication = requireActivity().application
    }


    /**
     * 初始化
     */
    fun initRecycler() {
        RecyclerUtils.initViewModelObserve(
            smartRefreshLayout(),
            refreshLoadingLayout(),
            mViewModel,
            this,
            setAdapter()
        )
        RecyclerUtils.initListener(
            smartRefreshLayout(),
            refreshRecycler(),
            refreshLoadingLayout(),
            mViewModel
        )
        initRecyclerView()
        requestData()
        refreshLoadingLayout().setRetryListener(View.OnClickListener {
            mViewModel.refresh()
        })
    }

    open fun requestData(){
        RecyclerUtils.initData(mViewModel)
    }

    /**
     * 初始化Recycler
     */
    private fun initRecyclerView() {
        refreshRecycler().layoutManager = setLinearLayoutManager()
        refreshRecycler().adapter = setAdapter()
        refreshRecycler().itemAnimator = null
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

    /**
     * 子类设置SmartRefreshLayout
     */
    protected abstract fun smartRefreshLayout(): SmartRefreshLayout

    /**
     * 子类设置LoadingLayout
     */
    protected abstract fun refreshLoadingLayout(): LoadingLayout

    /**
     * 子类设置RecyclerView
     */
    protected abstract fun refreshRecycler(): RecyclerView


}