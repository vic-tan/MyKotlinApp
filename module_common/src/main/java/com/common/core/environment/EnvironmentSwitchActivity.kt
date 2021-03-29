package com.common.core.environment

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.listener.OnItemClickListener
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.core.environment.adapter.EnvironmentAdapter
import com.common.core.environment.bean.EnvironmentBean
import com.common.core.environment.bean.ModuleBean
import com.common.core.environment.utils.EnvironmentUtils
import com.common.core.environment.viewmodel.EnvironmentSwitchViewModel
import com.common.databinding.ActivityEnvironmentSwitchBinding
import com.common.databinding.ItemEnvironmentContentBinding
import com.common.widget.extension.startActivity
import com.google.gson.Gson
import com.hjq.bar.OnTitleBarListener

/**
 * @desc:环境切换acitity
 * @author: tanlifei
 * @date: 2021/2/2 15:21
 */
class EnvironmentSwitchActivity :
    BaseToolBarActivity<ActivityEnvironmentSwitchBinding, EnvironmentSwitchViewModel>() {
    internal lateinit var adapter: EnvironmentAdapter

    companion object {
        private fun actionStart(list: MutableList<ModuleBean>) {
            startActivity<EnvironmentSwitchActivity> {
                putExtra(GlobalConst.Extras.JSON, Gson().toJson(list))
            }
        }

        fun actionStart() {
            actionStart(EnvironmentUtils.initEnvironmentSwitcher())
        }
    }

    override fun createViewModel(): EnvironmentSwitchViewModel {
        return EnvironmentSwitchViewModel()
    }

    override fun setTitleBarListener() {
        mTitleBar.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(v: View) {
                mViewModel.saveAllSelect()
                ActivityUtils.finishActivity(mActivity)
            }

            override fun onTitleClick(v: View) {
            }

            override fun onRightClick(v: View) {
            }
        })
    }

    override fun onBackPressed() {
        mViewModel.saveAllSelect()
        super.onBackPressed()
    }

    override fun init() {
        val mapJsonStr: String = intent.getStringExtra(GlobalConst.Extras.JSON)
        mViewModel.initListData(mapJsonStr)
        initRecyclerView()
        mViewModel.mDataChange.observe(this, Observer {
            adapter.notifyDataSetChanged()
        })
    }


    private fun initRecyclerView() {
        mBinding.recycler.layoutManager = LinearLayoutManager(mActivity)
        adapter = EnvironmentAdapter()
        adapter.mData = mViewModel.mData
        mBinding.recycler.adapter = adapter
        adapter.setItemClickListener(object : OnItemClickListener<EnvironmentBean> {
            override fun click(
                holder: ViewBinding,
                itemBean: EnvironmentBean,
                v: View,
                position: Int
            ) {
                when (holder) {
                    is ItemEnvironmentContentBinding -> {
                        when (v) {
                            holder.layout -> {
                                mViewModel.setSelect(position)
                            }
                        }
                    }
                }
            }
        })
    }


}


