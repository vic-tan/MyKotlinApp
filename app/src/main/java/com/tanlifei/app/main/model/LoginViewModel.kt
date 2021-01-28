package com.tanlifei.app.main.model

import com.blankj.utilcode.util.RegexUtils
import com.hjq.toast.ToastUtils
import com.tanlifei.app.common.bean.BaseViewModel
import com.tanlifei.app.common.bean.UserBean
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
class LoginViewModel : BaseViewModel() {
    private val TAG = this.javaClass.name
    val user: UserBean? = null
    private var intervalListener: OnIntervalListener? = null

    open fun startInterval() {
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
    open fun regexForm(phone: String, code: String): Boolean {
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

}