package com.common.core.base.ui.fragment

import com.common.core.base.viewmodel.BaseListViewModel
import com.common.databinding.LayoutRecyclerRefreshBinding

/**
 * @desc:只有一个List列表 没有其它的控件 ViewModel基类
 * @author: tanlifei
 * @date: 2021/2/7 18:17
 */
abstract class BaseSingleListBVMFragment<VM : BaseListViewModel> :
    BaseListBVMFragment<LayoutRecyclerRefreshBinding, BaseListViewModel>() {

    override fun initView() {
        super.initView()
        smartRefreshLayout = binding.smartRefreshLayout
        refreshLoadingLayout = binding.refreshLoadingLayout
        refreshRecycler = binding.refreshRecycler
    }
}