package com.onlineaginguniversity

import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.ui.activity.BaseActivity
import com.common.base.ui.activity.BaseWebViewActivity
import com.common.core.environment.utils.EnvironmentUtils
import com.common.utils.GlideUtils
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.gone
import com.onlineaginguniversity.BuildConfig
import com.onlineaginguniversity.databinding.ActivitySplashBinding
import com.onlineaginguniversity.main.ui.activity.GuideActivity
import com.onlineaginguniversity.main.ui.activity.LoginAtivity
import com.onlineaginguniversity.main.ui.activity.MainActivity
import com.onlineaginguniversity.main.viewmodel.SplashViewModel
import com.onlineaginguniversity.main.viewmodel.SplashViewModel.JumpType.*
import com.taobao.sophix.SophixManager

/**
 * @desc: 启动界面 这个类要放到包名下，因为更换icon时不放在包目录下面无法更换
 * @author: tanlifei
 * @date: 2021/1/22 16:26
 */
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override fun createViewModel(): SplashViewModel {
        return SplashViewModel()
    }

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
        mViewModel.startInterval()
        mViewModel.requestAds()
    }

    /**
     * 设置ViewModel的observe
     */
    private fun initViewModelObserve() {
        mViewModel.mJump.observe(this, Observer {
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
                    MainActivity.actionStart()
                    ActivityUtils.finishActivity(this)
                }
                REQUEST_ADS -> {
                    if (ObjectUtils.isNotEmpty(mViewModel.mAdsBean)) {
                        GlideUtils.load(this, mViewModel.mAdsBean!!.poster, mBinding.adsImg)
                    }
                }
                ADS -> {
                    mViewModel.mAdsBean?.let {
                        mBinding.splash.gone()
                        mViewModel.startAdsInterval()
                    }
                }
            }
        })

        mViewModel.mAdsInterval.observe(this, Observer {
            when (it) {
                -1L -> mViewModel.doAdsJump()
                else -> onIntervalChanged(it)
            }
        })
    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        clickListener(
            mBinding.adsImg,
            mBinding.into,
            mBinding.splash,
            clickListener = View.OnClickListener {
                when (it) {
                    mBinding.adsImg -> {
                        mViewModel.mAdsBean?.url?.let { it1 ->
                            BaseWebViewActivity.actionStart(
                                mViewModel.mAdsBean!!.name,
                                it1
                            )
                        }
                    }
                    mBinding.into -> mViewModel.doAdsJump()
                    mBinding.splash -> {
                    }
                }
            }
        )
    }

    private fun onIntervalChanged(second: Long) {
        mBinding.into.text = "跳过 ${second}s"
    }

    /**
     * 不允许返回
     */
    override fun onBackPressed() {

    }


}