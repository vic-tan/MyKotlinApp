package com.tanlifei.app

import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SPUtils
import com.common.base.ui.activity.BaseActivity
import com.tanlifei.app.common.config.Const
import com.tanlifei.app.databinding.ActivitySplashBinding
import com.tanlifei.app.main.ui.GuideActivity
import com.tanlifei.app.main.ui.LoginAtivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * @desc: 启动界面 这个类要放到包名下，因为更换icon时不放在包目录下面无法更换
 * @author: tanlifei
 * @date: 2021/1/22 16:26
 */
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    var count: Int = 3
    lateinit var subscribe: Disposable//保存订阅者

    override fun layoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun createBinding(layoutView: View): ActivitySplashBinding {
        return ActivitySplashBinding.bind(layoutView)
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
                        GuideActivity.actionStart()
                    } else {
                        LoginAtivity.actionStart()
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