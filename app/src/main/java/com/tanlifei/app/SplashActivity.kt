package com.tanlifei.app

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.bumptech.glide.Glide
import com.common.core.base.ui.activity.BaseActivity
import com.common.core.base.ui.activity.BaseWebViewActivity
import com.common.core.environment.EnvironmentUtils
import com.tanlifei.app.common.bean.BaseViewModel
import com.tanlifei.app.databinding.ActivitySplashBinding
import com.tanlifei.app.home.ui.activity.HomeActivity
import com.tanlifei.app.main.network.SplashNetwork
import com.tanlifei.app.main.ui.GuideActivity
import com.tanlifei.app.main.ui.LoginAtivity
import com.tanlifei.app.main.viewmodel.SplashViewModel

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
        initListener()
    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        splashViewModel = ViewModelProvider(
            this,
            BaseViewModel.createViewModelFactory(SplashViewModel(SplashNetwork.getInstance()))
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
                SplashViewModel.JumpType.GUIDE -> {
                    GuideActivity.actionStart()
                    ActivityUtils.finishActivity(this)
                }
                SplashViewModel.JumpType.LOGIN -> {
                    LoginAtivity.actionStart()
                    ActivityUtils.finishActivity(this)
                }
                SplashViewModel.JumpType.HOME -> {
                    HomeActivity.actionStart()
                    ActivityUtils.finishActivity(this)
                }
                SplashViewModel.JumpType.REQUEST_ADS -> {
                    if (ObjectUtils.isNotEmpty(splashViewModel.adsBean)) {
                        Glide.with(this)
                            .load(splashViewModel.adsBean!!.poster).into(binding.adsImg)
                    }
                }
                SplashViewModel.JumpType.ADS -> {
                    splashViewModel.adsBean?.let {
                        binding.splash.visibility = View.GONE
                        splashViewModel.startAdsInterval()
                    }
                }
            }
        })

        splashViewModel.adsInterval.observe(this, Observer {
            when (it) {
                -1L -> splashViewModel.doAdsJump()
                else -> onIntervalChanged(it)
            }
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        binding.adsImg.setOnClickListener {
            splashViewModel.adsBean?.url?.let { it1 ->
                BaseWebViewActivity.actionStart(
                    this, splashViewModel.adsBean!!.name,
                    it1
                )
            }
        }
        binding.into.setOnClickListener {
            splashViewModel.doAdsJump()
        }
        binding.splash.setOnClickListener {}
    }

    private fun onIntervalChanged(second: Long) {
        binding.into.text = "跳过 ${second}s"
    }

    /**
     * 不允许返回
     */
    override fun onBackPressed() {

    }

}