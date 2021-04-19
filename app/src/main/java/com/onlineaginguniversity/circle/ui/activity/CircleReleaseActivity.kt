package com.onlineaginguniversity.circle.ui.activity

import com.common.base.ui.activity.BaseToolBarActivity
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.startActivity
import com.onlineaginguniversity.databinding.ActivityCircleReleaseBinding
import com.onlineaginguniversity.profile.viewmodel.SettingViewModel


/**
 * @desc:同学圈发布界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class CircleReleaseActivity :
    BaseToolBarActivity<ActivityCircleReleaseBinding, SettingViewModel>() {


    companion object {
        fun actionStart() {
            startActivity<CircleReleaseActivity> { }
        }
    }

    override fun createViewModel(): SettingViewModel {
        return SettingViewModel()
    }

    override fun init() {
        mTitleBar.titleView.gone()
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