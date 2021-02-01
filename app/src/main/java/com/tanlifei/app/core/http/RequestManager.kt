package com.tanlifei.app.core.http

import androidx.lifecycle.LifecycleOwner
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
        rxLifeScope: RxLifeScope,
        url: String,
        params: Map<String, String>
    ) {
        RxHttpUtils.start(rxLifeScope) {
            RxHttp.get(url)
                .addAll(params)
                .toClass<T>().await()
        }
    }


    fun requestSendSms( rxLifeScope: RxLifeScope, phone: String) {
        val params = mutableMapOf<String, String>()
        params["phone"] = phone
        requestBean<SmsCodeBean>( rxLifeScope, UrlConst.URL_SEND_SMS, params)
    }

    fun requestLogin(rxLifeScope: RxLifeScope, phone: String, code: String) {
        val params = mutableMapOf<String, String>()
        params["phone"] = phone
        params["code"] = code
        requestBean<String>(rxLifeScope, UrlConst.URL_LOGIN, params)
    }

}