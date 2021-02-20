package com.tanlifei.app.main.viewmodel

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.viewmodel.BaseViewModel
import com.tanlifei.app.common.network.ApiNetwork
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit


/**
 * @desc:登录ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class LoginViewModel() : BaseViewModel() {

    /* 永远暴露不可变LiveData给外部，防止外部可以修改LoginViewModel，保证LoginViewModel独立性 */

    /**
     * 登录成功获取到token 的LveData
     */
    val isToken: LiveData<Boolean> get() = _isToken
    private val _isToken = MutableLiveData<Boolean>()

    /**
     * 连续点击logo 显示切换环境按钮LveData
     */
    val isContinuousClick: LiveData<Boolean> get() = _isContinuousClick
    private val _isContinuousClick = MutableLiveData<Boolean>()

    /**
     * 短信倒计时LveData
     */
    val smsCodeInterval: LiveData<Long> get() = _smsCodeInterval
    private val _smsCodeInterval = MutableLiveData<Long>()


    var token: String? = null


    private val counts = 10 // 点击次数
    private val totalDuration: Long = 10000 // 规定有效时间
    var mHits: LongArray = LongArray(counts)


    /**
     * 短信60s倒计时
     */
    private fun startInterval(count: Long = 60) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .take((count + 1).toLong())
            .map { aLong -> count - aLong }
            .observeOn(AndroidSchedulers.mainThread()) //ui线程中进行控件更新
            .doOnSubscribe {
                _smsCodeInterval.value = -1L//倒计时过程禁止按钮点击
            }.subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(t: Long) {
                    _smsCodeInterval.value = t//倒计时
                }

                override fun onError(e: Throwable?) {}
                override fun onComplete() {
                    _smsCodeInterval.value = -2L//回复原来初始状态
                }
            })
    }


    /**
     * 请求短信码
     */
    fun requestSmsCode(phone: String) = launchByLoading {
        ApiNetwork.requestSmsCode(phone)
        startInterval()
    }

    /**
     * 请求登录
     */
    fun requestLogin(phone: String, code: String) = launchByLoading {
        token = ApiNetwork.requestLogin(phone, code)
        _isToken.value = ObjectUtils.isNotEmpty(token)
    }

    /**
     * 退出登录
     */
    fun requestLogin() = launchByLoading {
        token = ApiNetwork.requestLoginOut()
        _isToken.value = false
    }


    /**
     * 是否连续点击
     */
    fun continuousClick() {
        //每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
        //为数组最后一位赋值
        mHits[mHits.size - 1] = SystemClock.uptimeMillis()
        if (mHits[0] >= SystemClock.uptimeMillis() - totalDuration) {
            mHits = LongArray(counts) //重新初始化数组
            _isContinuousClick.value = true
        }
    }

}