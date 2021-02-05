package com.tanlifei.app

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.common.core.base.ui.activity.BaseActivity
import com.common.core.environment.EnvironmentUtils
import com.tanlifei.app.databinding.ActivitySplashBinding
import com.tanlifei.app.home.ui.activity.HomeActivity
import com.tanlifei.app.main.viewmodel.SplashViewModel
import com.tanlifei.app.main.viewmodel.factory.SplashModelFactory
import com.tanlifei.app.main.network.SplashNetwork
import com.tanlifei.app.main.ui.AdsActivity
import com.tanlifei.app.main.ui.GuideActivity
import com.tanlifei.app.main.ui.LoginAtivity

/**
 * @desc: 启动界面 这个类要放到包名下，因为更换icon时不放在包目录下面无法更换
 * @author: tanlifei
 * @date: 2021/1/22 16:26
 */
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private lateinit var splashViewModel: SplashViewModel


    override fun showFullScreen(): Boolean {
        return true
    }

    override fun init() {
        EnvironmentUtils.initBaseApiUrl(BuildConfig.CURRENT_URL)
        initViewModel()
        initViewModelObserve()
    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        splashViewModel = ViewModelProvider(
            this,
            SplashModelFactory(SplashNetwork.getInstance())
        ).get(
            SplashViewModel::class.java
        )
        splashViewModel.startInterval()
        splashViewModel.requestAds()
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        splashViewModel.jump.observe(this, Observer {
            when (it) {
                GuideActivity::class.java -> GuideActivity.actionStart()
                HomeActivity::class.java -> HomeActivity.actionStart()
                LoginAtivity::class.java -> LoginAtivity.actionStart()
                AdsActivity::class.java -> {
                    splashViewModel.adsBean?.let { it1 -> AdsActivity.actionStart(this, it1) }
                }
            }
            ActivityUtils.finishActivity(this)
        })
    }


    /**
     * 不允许返回
     */
    override fun onBackPressed() {

    }

}