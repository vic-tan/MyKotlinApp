package com.tanlifei.mykotlinapp.main.ui

import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SPUtils
import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.common.activity.BaseActivity
import com.tanlifei.mykotlinapp.common.config.Const
import com.tanlifei.mykotlinapp.databinding.ActivitySplashBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * @desc: 启动界面
 * @author: tanlifei
 * @date: 2021/1/22 16:26
 */
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    var count: Int = 3
    lateinit var subscribe: Disposable//保存订阅者

    override fun layoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun bindView() {
        binding = ActivitySplashBinding.bind(containerView)
    }

    override fun showFullScreen(): Boolean {
        return true
    }

    override fun initView() {
        subscribe = Observable.interval(1, TimeUnit.SECONDS)//按时间间隔发送整数的Observable
            .observeOn(AndroidSchedulers.mainThread())//切换到主线程修改UI
            .subscribe {
                val show = count - it
                if (show < 0.toLong()) {//当倒计时小于0,计时结束
                    var guide = SPUtils.getInstance().getBoolean(Const.SPKey.GUIDE, true)
                    if (guide) {
                        ActivityUtils.startActivity(GuideActivity::class.java)
                    } else {
                        ActivityUtils.startActivity(LoginAtivity::class.java)
                    }
                    subscribe.dispose()//取消订阅
                    ActivityUtils.finishActivity(this)
                    return@subscribe//使用标记跳出方法
                }
            }
    }

    /**
     * 不允许返回
     */
    override fun onBackPressed() {

    }

    override fun onDestroy() {
        super.onDestroy()
        if (ObjectUtils.isNotEmpty(subscribe)) {
            subscribe.dispose()//取消订阅
        }
    }
}