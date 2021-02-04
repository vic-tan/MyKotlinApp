package com.tanlifei.app.main.model

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.rxLifeScope
import com.blankj.utilcode.util.ObjectUtils
import com.common.environment.EnvironmentBean
import com.common.environment.ModuleBean
import com.example.httpsender.kt.errorCode
import com.example.httpsender.kt.errorMsg
import com.example.httpsender.kt.show
import com.hjq.toast.ToastUtils
import com.tanlifei.app.BuildConfig
import com.tanlifei.app.common.bean.BaseViewModel
import com.tanlifei.app.common.bean.UserBean
import com.tanlifei.app.common.config.UrlConst
import com.tanlifei.app.main.network.LoginNetwork
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
class LoginViewModel(private val repository: LoginNetwork) : BaseViewModel() {

    /* 永远暴露不可变LiveData给外部，防止外部可以修改LoginViewModel，保证LoginViewModel独立性 */

    /**
     * 请求网络是否正在加载的LveData
     */
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLoading = MutableLiveData<Boolean>()

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
    lateinit var environmentList: MutableList<ModuleBean>


    private val counts = 10 // 点击次数
    private val totalDuration: Long = 10000 // 规定有效时间
    var mHits: LongArray = LongArray(counts)


    /**
     * 短信60s倒计时
     */
    private fun startInterval() {
        var count = 60
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
    fun requestSmsCode(phone: String) = launch {
        repository.requestSmsCode(phone)
        startInterval()
    }

    /**
     * 请求登录
     */
    fun requestLogin(phone: String, code: String) = launch {
        token = repository.requestLogin(phone, code)
        _isToken.value = ObjectUtils.isNotEmpty(token)
    }

    private fun launch(block: suspend () -> Unit) = rxLifeScope.launch({
        _isLoading.value = true
        block()
        _isLoading.value = false
    }, {
        _isLoading.value = false
        it.show(it.errorCode, it.errorMsg)
    }, {
        _isLoading.value = true
    }, {
        _isLoading.value = false
    })


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

    /**
     * 初始化环境切换器
     */
    fun initEnvironmentSwitcher() {
        environmentList = ArrayList()
        val apiList: MutableList<EnvironmentBean> = ArrayList()
        apiList.add(EnvironmentBean("开发环境", BuildConfig.BASE_URL_DEV, "environment_dev", true))
        apiList.add(EnvironmentBean("测试环境", BuildConfig.BASE_URL_TEST, "environment_test"))
        apiList.add(EnvironmentBean("正式环境", BuildConfig.BASE_URL_PRO, "environment_pro"))
        environmentList.add(ModuleBean("接口", EnvironmentBean.GROUP_API, apiList))

        val shareList: MutableList<EnvironmentBean> = ArrayList()
        shareList.add(
            EnvironmentBean(
                "开发分享",
                BuildConfig.BASE_URL_DEV + "devShare/",
                defaultCheck = true
            )
        )
        shareList.add(EnvironmentBean("测试分享", BuildConfig.BASE_URL_TEST + "testShare/"))
        shareList.add(EnvironmentBean("正式分享", BuildConfig.BASE_URL_PRO + "proShare/"))
        environmentList.add(ModuleBean("分享", EnvironmentBean.GROUP_SHARE, shareList))
    }

    /**
     * 环境切换修改请求接口路径
     */
    fun onEnvironmentChanged(environment: EnvironmentBean) {
        try {
            if (environment.group == EnvironmentBean.GROUP_API) {
                if (ObjectUtils.isNotEmpty(environment.url)) {
                    UrlConst.BASE_URL = environment.url
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}