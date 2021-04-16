package com.onlineaginguniversity

import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SPUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.common.base.ui.activity.BaseActivity
import com.common.base.ui.activity.BaseWebViewActivity
import com.common.core.environment.utils.EnvironmentUtils
import com.common.widget.component.extension.clickListener
import com.common.widget.component.extension.gone
import com.common.widget.component.extension.log
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.onlineaginguniversity.common.constant.ComConst
import com.onlineaginguniversity.databinding.ActivitySplashBinding
import com.onlineaginguniversity.login.utils.LoginUtils
import com.onlineaginguniversity.main.ui.activity.GuideActivity
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
        //是否已经显示过隐私协议弹出框过
        if (SPUtils.getInstance().getBoolean(ComConst.SPKey.PRIVACY, true)) {
            LoginUtils.privacyDialog(this@SplashActivity,
                OnConfirmListener {
                    SPUtils.getInstance().put(ComConst.SPKey.PRIVACY, false)
                    startInIt()
                })
        } else {
            startInIt()
        }

    }

    /**
     * 点击隐私弹框同意后在初始化
     */
    private fun startInIt() {
        mViewModel.splashTimer()
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
                    ActivityUtils.finishActivity(this@SplashActivity)
                }
                LOGIN -> {
                    LoginUtils.gotoLoginActivity()
                }
                HOME -> {
                    MainActivity.actionStart()
                    ActivityUtils.finishActivity(this@SplashActivity)
                }
                REQUEST_ADS -> {
                    if (ObjectUtils.isNotEmpty(mViewModel.mAdsBean)) {
                        Glide.with(this@SplashActivity)
                            .load(mViewModel.mAdsBean!!.poster)
                            .dontAnimate()
                            .into(object : DrawableImageViewTarget(mBinding.adsImg) {
                                override fun onResourceReady(
                                    drawable: Drawable,
                                    transition: Transition<in Drawable>?
                                ) {
                                    mBinding.adsImg.setImageDrawable(drawable)
                                    mViewModel.stopSplashTimerAdsDrawable(drawable)
                                }
                            })
                    }
                }
                ADS -> {
                    mBinding.splash.gone()
                    mViewModel.startAdsTimer()
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

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.adsDrawable = null
        mViewModel.splashTimer?.cancel()
    }

    /**
     * 不允许返回
     */
    override fun onBackPressed() {

    }


}