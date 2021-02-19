package com.tanlifei.app.persenal.activity

import com.blankj.utilcode.util.ActivityUtils
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.databinding.LayoutRecyclerRefreshBinding
import com.tanlifei.app.persenal.viewmodel.ManualViewModel


/**
 * @desc:操作手册界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class ManualActivity : BaseToolBarActivity<LayoutRecyclerRefreshBinding, ManualViewModel>() {


    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(ManualActivity::class.java)
        }
    }

    override fun createViewModel(): ManualViewModel {
        return ManualViewModel()
    }

    override fun init() {
        initViewModelObserve()
        initListener()
        initData()
    }


    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
    }

    /**
     * 初始化监听
     */
    private fun initListener() {

    }

    private fun initData() {
    }


}