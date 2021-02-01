package com.tanlifei.app.main.network

import com.tanlifei.app.common.config.UrlConst
import com.tanlifei.app.main.bean.SmsCodeBean
import rxhttp.RxHttp
import rxhttp.toClass
import rxhttp.toResponse


/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/1 11:14
 */
class LoginNetwork {

    suspend fun getCode(phone: String) = RxHttp.get(UrlConst.URL_LOGIN)
        .add("phone", phone)
        .toResponse<String>().await()

    companion object {
        private var network: LoginNetwork? = null
        fun getInstance(): LoginNetwork {
            if (network == null) {
                synchronized(LoginNetwork::class.java) {
                    if (network == null) {
                        network = LoginNetwork()
                    }
                }
            }
            return network!!
        }

    }
}