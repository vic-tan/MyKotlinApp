package com.common.core.base.ui.activity

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.common.core.base.viewmodel.BaseViewModel

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 17:14
 */
abstract class BaseBVMActivity<V : ViewBinding, VM : BaseViewModel> : BaseActivity<V>() {
    lateinit var mViewModel: VM
    protected abstract fun createViewModel(): VM

    override fun initBefore() {
        injectViewModel()
    }


    private fun injectViewModel() {
        val vm = createViewModel()
        mViewModel =
            ViewModelProvider(this, BaseViewModel.createViewModelFactory(createViewModel()))
                .get(vm::class.java)
        mViewModel.mApplication = application
    }


}