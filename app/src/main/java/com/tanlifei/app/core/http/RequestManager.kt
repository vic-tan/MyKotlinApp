package com.tanlifei.app.core.http

import androidx.lifecycle.LifecycleOwner
import com.kaopiz.kprogresshud.KProgressHUD
import com.rxjava.rxlife.BaseScope
import com.rxlife.coroutine.RxLifeScope
import com.tanlifei.app.common.config.UrlConst
import com.tanlifei.app.main.bean.SmsCodeBean
import rxhttp.RxHttp
import rxhttp.toClass

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/29 17:52
 */
class RequestManager(owner: LifecycleOwner) : BaseScope(owner) {

    private inline fun <reified T : Any> requestBean(
        hud: KProgressHUD,
        rxLifeScope: RxLifeScope,
        url: String,
        params: Map<String, String>
    ) {
        RxHttpUtils.start(hud, rxLifeScope) {
            RxHttp.get(url)
                .addAll(params)
                .toClass<T>().await()
        }
    }


    fun requestSendSms(hud: KProgressHUD, rxLifeScope: RxLifeScope, phone: String) {
        val params = mutableMapOf<String, String>()
        params["phone"] = phone
        requestBean<SmsCodeBean>(hud, rxLifeScope, UrlConst.URL_SEND_SMS, params)
    }

    fun requestLogin(hud: KProgressHUD, rxLifeScope: RxLifeScope, phone: String, code: String) {
        val params = mutableMapOf<String, String>()
        params["phone"] = phone
        params["code"] = code
        requestBean<String>(hud, rxLifeScope, UrlConst.URL_LOGIN, params)
    }

}