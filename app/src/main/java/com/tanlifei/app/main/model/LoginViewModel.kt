package com.tanlifei.app.main.model

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.rxLifeScope
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.RegexUtils
import com.common.utils.LogTools
import com.example.httpsender.kt.errorCode
import com.example.httpsender.kt.errorMsg
import com.example.httpsender.kt.show
import com.hjq.toast.ToastUtils
import com.lxj.xpopup.core.BasePopupView
import com.tanlifei.app.common.bean.BaseViewModel
import com.tanlifei.app.common.bean.UserBean
import com.tanlifei.app.common.config.UrlConst
import com.tanlifei.app.main.bean.SmsCodeBean
import com.xiaomai.environmentswitcher.EnvironmentSwitcher
import com.xiaomai.environmentswitcher.bean.EnvironmentBean
import com.xiaomai.environmentswitcher.bean.ModuleBean
import com.xiaomai.environmentswitcher.listener.OnEnvironmentChangeListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import rxhttp.RxHttp
import rxhttp.toClass
import java.util.concurrent.TimeUnit

/**
 * @desc:登录ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class LoginViewModel : BaseViewModel(), OnEnvironmentChangeListener {
    lateinit var user: MutableList<UserBean>
    var isLoading = MutableLiveData<Boolean>()
    private var intervalListener: OnIntervalListener? = null

    fun startInterval() {
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


    /**
     * 校验表单信息
     */
    fun checkFormInfo(phone: String, code: String): Boolean {
        if (phone.isEmpty()) {
            ToastUtils.show("请输入手机号")
            return false
        } else if (!RegexUtils.isMobileSimple(phone)) {
            ToastUtils.show("请输入正确的手机号码")
            return false
        }
        if (code.isEmpty()) {
            ToastUtils.show("请输入验证码")
            return false
        } else if (code.length < 4) {
            ToastUtils.show("请输入4位验证码")
            return false
        }
        return true
    }

    /**
     * 校验手机号
     */
    fun checkPhone(phone: String): Boolean {
        if (phone.isEmpty()) {
            ToastUtils.show("请输入手机号")
            return false
        } else if (!RegexUtils.isMobileSimple(phone)) {
            ToastUtils.show("请输入正确的手机号码")
            return false
        }
        return true
    }

    fun getCode(phone: String) = launch {
        val params = mutableMapOf<String, String>()
        params["phone"] = phone
        RxHttp.get(UrlConst.URL_SEND_SMS)
            .addAll(params)
            .toClass<SmsCodeBean>().await()
    }


    fun login(phone: String, code: String) = launch {
        val params = mutableMapOf<String, String>()
        params["phone"] = phone
        params["code"] = code
        RxHttp.get(UrlConst.URL_LOGIN)
            .addAll(params)
            .toClass<SmsCodeBean>().await()
    }

    private fun launch(block: suspend () -> Unit) = rxLifeScope.launch({
        isLoading.value = true
        block()
    }, {
        it.show(it.errorCode, it.errorMsg)
        isLoading.value = false
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

    companion object {
        const val REGISTER_AGREEMENT = "https://www.9bao.tv/mod/static/registerAgreementNew.html"
        const val AGREEMENT = "https://www.9bao.tv/webview/app/shop/agreement?active=2"
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