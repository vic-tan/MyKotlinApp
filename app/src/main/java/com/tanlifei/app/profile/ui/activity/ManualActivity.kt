package com.tanlifei.app.profile.ui.activity

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.common.core.base.listener.OnItemListener
import com.common.core.base.ui.activity.BaseRecyclerBVMActivity
import com.common.databinding.LayoutRecyclerRefreshBinding
import com.common.widget.LoadingLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tanlifei.app.databinding.ItemManualBinding
import com.tanlifei.app.profile.adapter.ManualAdapter
import com.tanlifei.app.profile.bean.ManualBean
import com.tanlifei.app.profile.viewmodel.ManualViewModel


/**
 * @desc:操作手册界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class ManualActivity : BaseRecyclerBVMActivity<LayoutRecyclerRefreshBinding, ManualViewModel>() {
    private lateinit var adapter: ManualAdapter

    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(ManualActivity::class.java)
        }
    }

    override fun createViewModel(): ManualViewModel {
        return ManualViewModel()
    }

    override fun init() {
        initData()
    }


    private fun initData() {
        adapter = ManualAdapter()
        adapter.mData = viewModel.mData as MutableList<ManualBean>
        initRefreshView()
        adapter.setOnItemChildClickListener(object :
            OnItemListener<ItemManualBinding> {
            override fun onItemClick(v: View, itemBinding: ItemManualBinding, position: Int) {
                when (v) {
                    itemBinding.item -> ManualDetailActivity.actionStart((viewModel.mData[position] as ManualBean).id)
                }
            }
        })
    }

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }

    override fun smartRefreshLayout(): SmartRefreshLayout {
        return binding.smartRefreshLayout
    }

    override fun refreshLoadingLayout(): LoadingLayout {
        return binding.refreshLoadingLayout
    }

    override fun refreshRecycler(): RecyclerView {
        return binding.refreshRecycler
    }

}