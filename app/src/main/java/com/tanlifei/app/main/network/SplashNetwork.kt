package com.tanlifei.app.main.network

import com.tanlifei.app.common.config.UrlConst
import rxhttp.RxHttp
import rxhttp.toResponse


/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/1 11:14
 */
class SplashNetwork {

    suspend fun getCode(phone: String) = RxHttp.get(UrlConst.URL_SEND_SMS)
        .add("phone", phone)
        .toResponse<String>().await()

    suspend fun getLogin(phone: String, code: String) = RxHttp.get(UrlConst.URL_LOGIN)
        .add("phone", phone)
        .add("code", code)
        .toResponse<String>().await()

    companion object {
        private var network: SplashNetwork? = null
        fun getInstance(): SplashNetwork {
            if (network == null) {
                synchronized(SplashNetwork::class.java) {
                    if (network == null) {
                        network = SplashNetwork()
                    }
                }
            }
            return network!!
        }

    }
}