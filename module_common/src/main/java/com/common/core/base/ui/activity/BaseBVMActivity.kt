package com.common.core.base.ui.activity

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.common.core.base.viewmodel.BaseViewModel

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 17:14
 */
abstract class BaseBVMActivity<T : ViewBinding, VM : BaseViewModel> : BaseActivity<T>() {
    protected lateinit var viewModel: VM
        private set

    protected abstract fun createViewModel(): VM

    override fun initBefore() {
        injectViewModel()
    }

    private fun injectViewModel() {
        val vm = createViewModel()
        viewModel = ViewModelProvider(this, BaseViewModel.createViewModelFactory(createViewModel()))
            .get(vm::class.java)
        viewModel.application = application
    }


}