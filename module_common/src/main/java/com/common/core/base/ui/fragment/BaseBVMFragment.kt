package com.common.core.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.common.core.base.viewmodel.BaseViewModel

/**
 * @desc:自动ViewBinding 和ViewModel基类
 * @author: tanlifei
 * @date: 2021/2/7 18:17
 */
abstract class BaseBVMFragment<T : ViewBinding, VM : BaseViewModel> : BaseLazyFragment<T>() {
    protected lateinit var mViewModel: VM
        private set

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


}