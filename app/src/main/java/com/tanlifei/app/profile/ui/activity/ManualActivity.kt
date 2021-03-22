package com.tanlifei.app.profile.ui.activity

import android.view.View
import androidx.viewbinding.ViewBinding
import com.common.core.base.adapter.CommonRvAdapter
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
class ManualActivity :
    BaseRecyclerBVMActivity<ActivityManualBinding, ManualBean, ManualViewModel>() {


    companion object {
        fun actionStart() {
            startActivity<ManualActivity> { }
        }
    }

    override fun createViewModel(): ManualViewModel {
        return ManualViewModel()
    }

    override fun init() {
        initRefreshView(
            mBinding.refreshLayout.smartRefreshLayout,
            mBinding.refreshLayout.refreshRecycler,
            mBinding.refreshLayout.refreshLoadingLayout
        )
    }

    override fun setAdapter(): CommonRvAdapter<ManualBean> {
        return ManualAdapter()
    }


    override fun itemClick(holder: ViewBinding, itemBean: ManualBean, v: View, position: Int) {
        holder as ItemManualBinding
        when (v) {
            holder.item -> ManualDetailActivity.actionStart(itemBean.id)
        }
    }


}