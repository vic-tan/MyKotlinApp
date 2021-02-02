package com.tanlifei.app.main.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.rxLifeScope
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.RegexUtils
import com.common.utils.LogTools
import com.example.httpsender.kt.errorCode
import com.example.httpsender.kt.errorMsg
import com.example.httpsender.kt.show
import com.hjq.toast.ToastUtils
import com.tanlifei.app.BaseApplication
import com.tanlifei.app.common.bean.BaseViewModel
import com.tanlifei.app.common.bean.UserBean
import com.tanlifei.app.common.config.UrlConst
import com.tanlifei.app.main.network.LoginNetwork
import com.xiaomai.environmentswitcher.EnvironmentSwitcher
import com.xiaomai.environmentswitcher.bean.EnvironmentBean
import com.xiaomai.environmentswitcher.bean.ModuleBean
import com.xiaomai.environmentswitcher.listener.OnEnvironmentChangeListener
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
class LoginViewModel(private val repository: LoginNetwork) : BaseViewModel(),
    OnEnvironmentChangeListener {

    var isLoading = MutableLiveData<Boolean>()
    var isToken = MutableLiveData<Boolean>()

    var token: String? = null

    lateinit var user: MutableList<UserBean>

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
        repository.getCode(phone)
        startInterval()
    }


    fun login(phone: String, code: String) = launch {
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
        EnvironmentSwitcher.addOnEnvironmentChangeListener(this)
    }

    override fun onEnvironmentChanged(
        module: ModuleBean?,
        oldEnvironment: EnvironmentBean?,
        newEnvironment: EnvironmentBean?
    ) {
        try {
            LogTools.show(
                module?.name + "oleEnvironment=" + oldEnvironment?.name + "，oldUrl=" + oldEnvironment?.url
                        + ",newNevironment=" + newEnvironment?.name + "，newUrl=" + newEnvironment?.url
            )
            if (module?.equals(EnvironmentSwitcher.MODULE_APP)!!) {
                if (ObjectUtils.isNotEmpty(newEnvironment?.url)) {
                    UrlConst.URL_BASE = newEnvironment?.url.toString()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}