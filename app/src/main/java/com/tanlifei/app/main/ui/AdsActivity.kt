package com.tanlifei.app.main.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SPUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.common.ComApplication
import com.common.base.ui.activity.BaseActivity
import com.tanlifei.app.R
import com.tanlifei.app.common.config.Const
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.databinding.ActivityAdsBinding
import com.tanlifei.app.home.ui.activity.HomeActivity
import com.tanlifei.app.main.bean.AdsBean
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * @desc:广告页
 * @author: tanlifei
 * @date: 2021/2/3 17:58
 */
class AdsActivity : BaseActivity<ActivityAdsBinding>() {

    lateinit var adsBean: AdsBean
    lateinit var subscribe: Disposable//保存订阅者

    companion object {
        private const val EXTRAS_DATA = "extras_data"
        fun actionStart(context: Context, adsBean: AdsBean) {
            var intent = Intent(context, AdsActivity::class.java).apply {
                putExtra(EXTRAS_DATA, adsBean)
            }
            ActivityUtils.startActivity(intent)
        }
    }

    override fun layoutResId(): Int {
        return R.layout.activity_ads
    }

    override fun createBinding(layoutView: View): ActivityAdsBinding {
        return ActivityAdsBinding.bind(layoutView)
    }

    override fun initView() {
        adsBean = intent.getParcelableExtra(EXTRAS_DATA) as AdsBean
        if (ObjectUtils.isNotEmpty(adsBean)) {
            Glide.with(this).applyDefaultRequestOptions(RequestOptions().placeholder(R.mipmap.ads))
                .load(adsBean.poster).into(binding.adsImg)
            subscribe =
                Observable.interval(adsBean.duration * 1L, TimeUnit.SECONDS)//按时间间隔发送整数的Observable
                    .observeOn(AndroidSchedulers.mainThread())//切换到主线程修改UI
                    .subscribe {
                        val show = adsBean.duration - it
                        if (show < 0.toLong()) {//当倒计时小于0,计时结束
                            val token = UserInfoUtils.getToken()
                            //已经登录过了
                            if (ObjectUtils.isNotEmpty(token)) {
                                ComApplication.token = token
                                HomeActivity.actionStart()
                            } else {//未登录
                                LoginAtivity.actionStart()
                            }
                        }
                        subscribe.dispose()//取消订阅
                        ActivityUtils.finishActivity(this)
                        return@subscribe//使用标记跳出方法
                    }
        }

    }

}