package com.tanlifei.app.profile.ui.activity

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.common.core.base.adapter.CommonRvAdapter
import com.common.core.base.adapter.CommonRvHolder
import com.common.core.base.listener.OnItemClickListener
import com.common.core.base.ui.activity.BaseRecyclerBVMActivity
import com.common.databinding.LayoutRecyclerRefreshBinding
import com.common.utils.extension.startActivity
import com.common.widget.LoadingLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tanlifei.app.databinding.ActivityManualBinding
import com.tanlifei.app.databinding.ItemManualBinding
import com.tanlifei.app.profile.adapter.ManualAdapter
import com.tanlifei.app.profile.bean.ManualBean
import com.tanlifei.app.profile.viewmodel.ManualViewModel


/**
 * @desc:操作手册界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class ManualActivity : BaseRecyclerBVMActivity<ActivityManualBinding, ManualViewModel>() {
    private lateinit var mAdapter: ManualAdapter

    companion object {
        fun actionStart() {
            startActivity<ManualActivity> { }
        }
    }

    override fun createViewModel(): ManualViewModel {
        return ManualViewModel()
    }

    override fun init() {
        initData()
    }


    private fun initData() {
        mAdapter = ManualAdapter()
        mAdapter.mData = mViewModel.mData as MutableList<ManualBean>
        initRefreshView()
        mAdapter.setItemClickListener(object :
            OnItemClickListener<ItemManualBinding, ManualBean> {
            override fun click(
                binding: ItemManualBinding,
                bean: ManualBean,
                v: View,
                position: Int
            ) {
                when (v) {
                    binding.item -> ManualDetailActivity.actionStart(bean.id)
                }
            }
        })
    }


    override fun smartRefreshLayout(): SmartRefreshLayout {
        return mBinding.refreshLayout.smartRefreshLayout
    }

    override fun refreshLoadingLayout(): LoadingLayout {
        return mBinding.refreshLayout.refreshLoadingLayout
    }

    override fun refreshRecycler(): RecyclerView {
        return mBinding.refreshLayout.refreshRecycler
    }

    override fun setAdapter(): Any {
        return mAdapter
    }


}