package com.tanlifei.app.persenal.activity

import android.Manifest
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.common.core.base.ui.activity.BaseRecyclerBVMActivity
import com.common.databinding.LayoutRecyclerRefreshBinding
import com.common.utils.PermissionUtils
import com.tanlifei.app.R
import com.tanlifei.app.persenal.adapter.ManualAdapter
import com.tanlifei.app.persenal.bean.ManualBean
import com.tanlifei.app.persenal.viewmodel.ManualViewModel


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
        adapter = ManualAdapter(viewModel.mData as MutableList<ManualBean>)
        initRefreshView(
            binding.smartRefreshLayout,
            binding.refreshLoadingLayout,
            binding.refreshRecycler
        )
        binding.refreshLoadingLayout.setOnClickListener {

        }
        adapter.addChildClickViewIds(R.id.item)
        adapter.setOnItemClickListener { _, _, position ->
            ManualDetailActivity.actionStart((viewModel.mData[position] as ManualBean).id)
        }
    }

    override fun setAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        return adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }

}