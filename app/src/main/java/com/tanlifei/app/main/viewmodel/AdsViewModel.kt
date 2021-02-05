package com.tanlifei.app.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.ComApplication
import com.tanlifei.app.common.bean.BaseViewModel
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.home.ui.activity.HomeActivity
import com.tanlifei.app.main.bean.AdsBean
import com.tanlifei.app.main.ui.LoginAtivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 * @desc:启动页ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class AdsViewModel(adsBean: AdsBean) : BaseViewModel() {

    /**
     * 倒计时结束LveData
     */
    val jump: LiveData<Class<*>> get() = _jump
    private val _jump = MutableLiveData<Class<*>>()

    /**
     * 短信倒计时LveData
     */
    val interval: LiveData<Long> get() = _interval
    private val _interval = MutableLiveData<Long>()

    var bean: AdsBean = adsBean


    /**
     * 启动页3s倒计时
     */
    fun startInterval() {
        var count = 3
        if (ObjectUtils.isNotEmpty(bean)) {
            count = bean.duration
        }
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .take((count + 1).toLong())
            .map { aLong -> count - aLong }
            .observeOn(AndroidSchedulers.mainThread()) //ui线程中进行控件更新
            .doOnSubscribe{}.subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(t: Long) {
                    _interval.value = t//倒计时
                }

                override fun onError(e: Throwable?) {}
                override fun onComplete() {
                    _interval.value = -1L//回复原来初始状态
                }
            })
    }

    /**
     * 跳转到指定activity
     */
     fun doJump() {
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