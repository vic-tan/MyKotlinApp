package com.tanlifei.app.profile.ui.activity

import android.view.View
import androidx.viewbinding.ViewBinding
import com.common.core.base.listener.OnMultiItemListener
import com.common.core.base.ui.activity.BaseRecyclerBVMActivity
import com.common.utils.extension.startActivity
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
        mAdapter.mData = mViewModel.mData
        initRefreshView(
            mBinding.refreshLayout.smartRefreshLayout,
            mBinding.refreshLayout.refreshRecycler,
            mBinding.refreshLayout.refreshLoadingLayout
        )
        mAdapter.setItemClickListener(object : OnMultiItemListener<ManualBean> {
            override fun click(holder: ViewBinding, itemBean: ManualBean, v: View, position: Int) {
                holder as ItemManualBinding
                when (v) {
                    holder.item -> ManualDetailActivity.actionStart(itemBean.id)
                }
            }

        })
    }

    override fun setAdapter(): Any {
        return mAdapter
    }


}