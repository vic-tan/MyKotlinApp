package com.tanlifei.app

import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.ui.activity.BaseBVMActivity
import com.common.core.base.ui.activity.BaseWebViewActivity
import com.common.core.environment.utils.EnvironmentUtils
import com.common.utils.GlideUtils
import com.common.utils.extension.clickListener
import com.tanlifei.app.databinding.ActivitySplashBinding
import com.tanlifei.app.home.ui.activity.HomeActivity
import com.tanlifei.app.main.ui.GuideActivity
import com.tanlifei.app.main.ui.LoginAtivity
import com.tanlifei.app.main.viewmodel.SplashViewModel
import com.tanlifei.app.main.viewmodel.SplashViewModel.JumpType.*
import com.taobao.sophix.SophixManager

/**
 * @desc: 启动界面 这个类要放到包名下，因为更换icon时不放在包目录下面无法更换
 * @author: tanlifei
 * @date: 2021/1/22 16:26
 */
class SplashActivity : BaseBVMActivity<ActivitySplashBinding, SplashViewModel>(),
    View.OnClickListener {

    override fun showFullScreen(): Boolean {
        return true
    }

    override fun init() {
        EnvironmentUtils.initBaseApiUrl(BuildConfig.CURRENT_URL)
        initViewModel()
        initViewModelObserve()
        initListener()
        initSophixManager()
    }

    private fun initSophixManager() {
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        //引用了热修复，也有可能会引发这种情况Androidstudio中，修改代码之后运行不生效，需要卸载后重新安装才生效问题的解决
        if (!BuildConfig.DEBUG) {
            SophixManager.getInstance().queryAndLoadNewPatch()
        }
    }

    /**
     * 初始化ViewModel
     */
    private fun initViewModel() {
        viewModel.startInterval()
        viewModel.requestAds()
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        viewModel.jump.observe(this, Observer {
            when (it) {
                GUIDE -> {
                    GuideActivity.actionStart()
                    ActivityUtils.finishActivity(this)
                }
                LOGIN -> {
                    LoginAtivity.actionStart()
                    ActivityUtils.finishActivity(this)
                }
                HOME -> {
                    HomeActivity.actionStart()
                    ActivityUtils.finishActivity(this)
                }
                REQUEST_ADS -> {
                    if (ObjectUtils.isNotEmpty(viewModel.adsBean)) {
                        GlideUtils.load(this, viewModel.adsBean!!.poster, binding.adsImg)
                    }
                }
                ADS -> {
                    viewModel.adsBean?.let {
                        binding.splash.visibility = View.GONE
                        viewModel.startAdsInterval()
                    }
                }
            }
        })

        viewModel.adsInterval.observe(this, Observer {
            when (it) {
                -1L -> viewModel.doAdsJump()
                else -> onIntervalChanged(it)
            }
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(
            this,
            binding.adsImg,
            binding.into,
            binding.splash
        )
    }

    private fun onIntervalChanged(second: Long) {
        binding.into.text = "跳过 ${second}s"
    }

    /**
     * 不允许返回
     */
    override fun onBackPressed() {

    }

    override fun createViewModel(): SplashViewModel {
        return SplashViewModel()
    }

    override fun onClick(v: View) {
        when (v) {
            binding.adsImg -> {
                viewModel.adsBean?.url?.let { it1 ->
                    BaseWebViewActivity.actionStart(
                        viewModel.adsBean!!.name,
                        it1
                    )
                }
            }
            binding.into -> viewModel.doAdsJump()
            binding.splash -> {
            }
        }
    }

}