package com.tanlifei.app.main.network

import androidx.lifecycle.LifecycleOwner
import com.example.httpsender.kt.errorCode
import com.example.httpsender.kt.errorMsg
import com.example.httpsender.kt.show
import com.lxj.xpopup.core.BasePopupView
import com.rxjava.rxlife.BaseScope
import com.rxlife.coroutine.RxLifeScope
import com.tanlifei.app.common.config.UrlConst
import com.tanlifei.app.core.http.ResponseCallBack
import com.tanlifei.app.main.bean.SmsCodeBean
import rxhttp.RxHttp
import rxhttp.toClass

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/1 11:14
 */
class LoginPresenter(owner: LifecycleOwner) : BaseScope(owner) {

    fun getCode(
        hud: BasePopupView,
        rxLifeScope: RxLifeScope,
        phone: String,
        callBack: ResponseCallBack
    ) = rxLifeScope.launch({
        val params = mutableMapOf<String, String>()
        params["phone"] = phone
        var bean = RxHttp.get(UrlConst.URL_SEND_SMS)
            .addAll(params)
            .toClass<SmsCodeBean>().await()
        callBack.onSuccess(bean)
    }, {
        callBack.onFailed(it.errorCode, it.errorMsg)
    }, {
        hud.show()
    }, {
        hud.dismiss()
    })
}