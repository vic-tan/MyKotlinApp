package com.tanlifei.app.persenal.activity

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.common.ComApplication
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.utils.ComDialogUtils
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.databinding.ActivitySettingBinding
import com.tanlifei.app.main.ui.LoginAtivity
import com.tanlifei.app.persenal.network.SettingNetwork
import com.tanlifei.app.persenal.viewmodel.SettingViewModel


/**
 * @desc:设置界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class SettingActivity : BaseToolBarActivity<ActivitySettingBinding,SettingViewModel>() {


    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(SettingActivity::class.java)
        }
    }

    override fun createViewModel(): SettingViewModel {
        return SettingViewModel(SettingNetwork.getInstance())
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
        viewModel.isToken.observe(this, Observer {
            ComApplication.token = null
            UserInfoUtils.clear()
            LoginAtivity.actionStart()
            ActivityUtils.finishActivity(this)
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        binding.about.setOnClickListener {
            AboutActivity.actionStart()
        }
        binding.exit.setOnClickListener {
            ComDialogUtils.comConfirm(
                this,
                "您确定要退出应用吗?",
                OnConfirmListener {
                    viewModel.requestLogin()
                })
        }
    }

    private fun initData() {
        binding.versionName.text = "V${AppUtils.getAppVersionName()}"
    }




}