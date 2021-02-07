package com.tanlifei.app.persenal.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ObjectUtils
import com.bumptech.glide.Glide
import com.common.ComApplication
import com.common.core.base.ui.activity.BaseToolBarActivity
import com.common.core.environment.EnvironmentUtils
import com.common.utils.ComDialogUtils
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.tanlifei.app.BuildConfig
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.databinding.ActivitySettingBinding
import com.tanlifei.app.home.ui.activity.HomeActivity
import com.tanlifei.app.main.network.SplashNetwork
import com.tanlifei.app.main.ui.GuideActivity
import com.tanlifei.app.main.ui.LoginAtivity
import com.tanlifei.app.main.viewmodel.SplashViewModel
import com.tanlifei.app.main.viewmodel.factory.SettingModelFactory
import com.tanlifei.app.main.viewmodel.factory.SplashModelFactory
import com.tanlifei.app.persenal.network.SettingNetwork
import com.tanlifei.app.persenal.viewmodel.SettingViewModel
import com.tencent.bugly.crashreport.biz.UserInfoBean
import org.litepal.LitePal


/**
 * @desc:设置界面
 * @author: tanlifei
 * @date: 2021/2/5 10:15
 */
class SettingActivity : BaseToolBarActivity<ActivitySettingBinding>() {

    private lateinit var viewModel: SettingViewModel

    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(SettingActivity::class.java)
        }
    }

    override fun init() {
        initViewModel()
        initViewModelObserve()
        initListener()
        initData()
    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            SettingModelFactory(SettingNetwork.getInstance())
        ).get(
            SettingViewModel::class.java
        )

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
            ComDialogUtils.comConfirm(this, "", "您确定要退出应用吗?", OnConfirmListener {
                viewModel.requestLogin()
            })
        }
    }

    private fun initData() {
        binding.versionName.text = "V${AppUtils.getAppVersionName()}"
    }


}