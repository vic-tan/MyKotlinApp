package com.tanlifei.app.main.network

import com.tanlifei.app.common.config.api.ApiConst
import rxhttp.RxHttp
import rxhttp.toResponse


/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/1 11:14
 */
class LoginNetwork {

    suspend fun requestSmsCode(phone: String) = RxHttp.get(ApiConst.URL_SEND_SMS)
        .add("phone", phone)
        .toResponse<String>().await()

    suspend fun requestLogin(phone: String, code: String) = RxHttp.get(ApiConst.URL_LOGIN)
        .add("phone", phone)
        .add("code", code)
        .toResponse<String>().await()

    suspend fun requestLoginOut() = RxHttp.get(ApiConst.URL_LOGIN_OUT)
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