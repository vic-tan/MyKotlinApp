package com.tanlifei.app.profile.ui.activity

import android.view.View
import androidx.viewbinding.ViewBinding
import com.common.base.adapter.BaseRvAdapter
import com.common.base.ui.activity.BaseRvActivity
import com.common.databinding.LayoutRecyclerRefreshBinding
import com.common.widget.component.extension.startActivity
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
    BaseRvActivity<LayoutRecyclerRefreshBinding, ManualBean, ManualViewModel>() {


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
            mBinding.smartRefreshLayout,
            mBinding.refreshRecycler,
            mBinding.refreshLoadingLayout
        )
    }

    override fun setAdapter(): BaseRvAdapter<ManualBean> {
        return ManualAdapter()
    }


    override fun itemClick(holder: ViewBinding, itemBean: ManualBean, v: View, position: Int) {
        holder as ItemManualBinding
        when (v) {
            holder.item -> ManualDetailActivity.actionStart(itemBean.id)
        }
    }


}