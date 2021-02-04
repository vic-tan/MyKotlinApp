package com.tanlifei.app.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SPUtils
import com.common.ComApplication
import com.common.environment.EnvironmentChangeManager
import com.common.utils.MyLogTools
import com.tanlifei.app.common.bean.BaseViewModel
import com.tanlifei.app.common.config.Const
import com.tanlifei.app.common.config.UrlConst
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.home.ui.activity.HomeActivity
import com.tanlifei.app.main.bean.AdsBean
import com.tanlifei.app.main.network.SplashNetwork
import com.tanlifei.app.main.ui.AdsActivity
import com.tanlifei.app.main.ui.GuideActivity
import com.tanlifei.app.main.ui.LoginAtivity
import com.tanlifei.app.main.utils.AdsUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import org.litepal.LitePal
import java.util.concurrent.TimeUnit


/**
 * @desc:启动页ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class SplashViewModel(private val repository: SplashNetwork) : BaseViewModel() {

    /**
     * 3s结束倒计时LveData
     */
    val jump: LiveData<Class<*>> get() = _jump
    private val _jump = MutableLiveData<Class<*>>()

    var adsBean: AdsBean? = null

    /**
     * 请求短信码
     */
    fun requestAds() = launchBySilence {
        val requestAdsBean: AdsBean = repository.requestAds()
        if (ObjectUtils.isNotEmpty(requestAdsBean)) {
            LitePal.deleteAll(AdsBean::class.java)
            requestAdsBean.save()
        }
        MyLogTools.show(requestAdsBean.toString())
    }

    /**
     * 启动页3s倒计时
     */
    fun startInterval(count: Long = 3) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .take((count + 1).toLong())
            .map { aLong -> count - aLong }
            .observeOn(AndroidSchedulers.mainThread()) //ui线程中进行控件更新
            .doOnSubscribe {}.subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(t: Long) {}
                override fun onError(e: Throwable?) {}
                override fun onComplete() {
                    doJump()
                }
            })
    }

    /**
     * 跳转到指定activity
     */
    private fun doJump() {
        var guide = SPUtils.getInstance().getBoolean(Const.SPKey.GUIDE, true)
        if (guide) {
            _jump.value = GuideActivity::class.java
        } else {
            adsBean = AdsUtils.getAds()
            if (ObjectUtils.isNotEmpty(adsBean) && (ObjectUtils.isNotEmpty(adsBean) && adsBean?.status == 1)) {
                _jump.value = AdsActivity::class.java
            } else {
                val token = UserInfoUtils.getToken()
                //已经登录过了
                if (ObjectUtils.isNotEmpty(token)) {
                    ComApplication.token = token
                    _jump.value = HomeActivity::class.java
                } else {//未登录
                    _jump.value = LoginAtivity::class.java
                }
            }
        }
    }

    /**
     * 初始化环境
     */
    fun initBaseApiUrl() {
        val apiUrl = EnvironmentChangeManager.initEnvironment()
        if (ObjectUtils.isNotEmpty(apiUrl)) {
            UrlConst.BASE_URL = apiUrl!!
        }
        MyLogTools.show("UrlConst.BASE_URL = ${UrlConst.BASE_URL}")
    }
}