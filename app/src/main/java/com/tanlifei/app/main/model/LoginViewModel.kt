package com.tanlifei.app.main.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.rxLifeScope
import com.blankj.utilcode.util.ObjectUtils
import com.common.environment.EnvironmentBean
import com.common.environment.ModuleBean
import com.common.utils.LogTools
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

    var isLoading = MutableLiveData<Boolean>()
    var isToken = MutableLiveData<Boolean>()

    var token: String? = null

    lateinit var user: MutableList<UserBean>
    lateinit var environmentList: MutableList<ModuleBean>

    private var intervalListener: OnIntervalListener? = null

    private fun startInterval() {
        var count = 60
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .take((count + 1).toLong())
            .map { aLong -> count - aLong }
            .observeOn(AndroidSchedulers.mainThread()) //ui线程中进行控件更新
            .doOnSubscribe {
                intervalListener?.onIntervalStart()//倒计时过程禁止按钮点击
            }.subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(t: Long) {
                    intervalListener?.onIntervalChanged(t)//倒计时
                }

                override fun onError(e: Throwable?) {}
                override fun onComplete() {
                    intervalListener?.onIntervalComplete() //回复原来初始状态
                }
            })
    }


    fun getCode(phone: String) = launch {
        ToastUtils.show(UrlConst.BASE_URL)
        repository.getCode(phone)
        startInterval()
    }


    fun login(phone: String, code: String) = launch {
        ToastUtils.show(UrlConst.BASE_URL)
        token = repository.getLogin(phone, code)
        isToken.value = ObjectUtils.isNotEmpty(token)
    }

    private fun launch(block: suspend () -> Unit) = rxLifeScope.launch({
        isLoading.value = true
        block()
        isLoading.value = false
    }, {
        isLoading.value = false
        it.show(it.errorCode, it.errorMsg)
    }, {
        isLoading.value = true
    }, {
        isLoading.value = false
    })

    interface OnIntervalListener {
        fun onIntervalStart()
        fun onIntervalChanged(second: Long)
        fun onIntervalComplete()
    }

    fun setOnIntervalListener(intervalListener: OnIntervalListener) {
        this.intervalListener = intervalListener
    }


    /**
     * 初始化环境切换器
     */
    fun initEnvironmentSwitcher() {
        environmentList = ArrayList()
        val apiList: MutableList<EnvironmentBean> = ArrayList()
        apiList.add(EnvironmentBean("开发环境", BuildConfig.BASE_URL_DEV, true))
        apiList.add(EnvironmentBean("测试环境", BuildConfig.BASE_URL_TEST))
        apiList.add(EnvironmentBean("正式环境", BuildConfig.BASE_URL_PRO))
        environmentList.add(ModuleBean("接口", EnvironmentBean.GROUP_API, apiList))

        val shareList: MutableList<EnvironmentBean> = ArrayList()
        shareList.add(EnvironmentBean("开发分享", BuildConfig.BASE_URL_DEV + "devShare/", true))
        shareList.add(EnvironmentBean("测试分享", BuildConfig.BASE_URL_TEST + "testShare/"))
        shareList.add(EnvironmentBean("正式分享", BuildConfig.BASE_URL_PRO + "proShare/"))
        environmentList.add(ModuleBean("分享", EnvironmentBean.GROUP_SHARE, shareList))
    }

    fun onEnvironmentChanged(
        environment: EnvironmentBean
    ) {
        try {
            LogTools.show(environment.toString() + "group=" + environment.group)
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