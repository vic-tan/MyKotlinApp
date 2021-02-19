package com.common.core.environment

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ActivityUtils
import com.common.R
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.core.environment.adapter.EnvironmentAdapter
import com.common.core.environment.bean.ModuleBean
import com.common.core.environment.utils.EnvironmentUtils
import com.common.core.environment.viewmodel.EnvironmentSwitchViewModel
import com.common.databinding.ActivityEnvironmentSwitchBinding
import com.google.gson.Gson

/**
 * @desc:环境切换acitity
 * @author: tanlifei
 * @date: 2021/2/2 15:21
 */
class EnvironmentSwitchActivity :
    BaseToolBarActivity<ActivityEnvironmentSwitchBinding, EnvironmentSwitchViewModel>() {
    internal lateinit var adapter: EnvironmentAdapter

    companion object {
        private const val EXTRAS_DATA = "extras_data"
        private fun actionStart(list: MutableList<ModuleBean>) {
            val mapStr = Gson().toJson(list)
            var intent = Intent(ActivityUtils.getTopActivity(), EnvironmentSwitchActivity::class.java).apply {
                putExtra(EXTRAS_DATA, mapStr)
            }
            ActivityUtils.startActivity(intent)
        }

        fun actionStart() {
            actionStart( EnvironmentUtils.initEnvironmentSwitcher())
        }
    }

    override fun createViewModel(): EnvironmentSwitchViewModel {
        return EnvironmentSwitchViewModel()
    }

    override fun init() {
        val mapJsonStr: String = intent.getStringExtra(EXTRAS_DATA)
        viewModel.initListData(mapJsonStr)
        initRecyclerView()
        viewModel.dataChanged.observe(this, Observer {
            adapter.notifyDataSetChanged()
        })
    }


    private fun initRecyclerView() {
        binding.recycler.layoutManager = LinearLayoutManager(mActivity)
        adapter = EnvironmentAdapter(viewModel.environmentList)
        binding.recycler.adapter = adapter
        adapter.addChildClickViewIds(R.id.title, R.id.url, R.id.radio)
        adapter.setOnItemClickListener { _, _, position ->
            viewModel.itemClickListener(position)
        }

    }


}


