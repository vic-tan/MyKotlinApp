package com.tanlifei.app.persenal.network

import com.tanlifei.app.common.config.api.ApiConst
import rxhttp.RxHttp
import rxhttp.toResponse


/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/1 11:14
 */
class SettingNetwork {

    suspend fun requestLoginOut() = RxHttp.postForm(ApiConst.URL_LOGIN_OUT)
        .toResponse<String>().await()

    companion object {
        private var network: SettingNetwork? = null
        fun getInstance(): SettingNetwork {
            if (network == null) {
                synchronized(SettingNetwork::class.java) {
                    if (network == null) {
                        network = SettingNetwork()
                    }
                }
            }
            return network!!
        }

    }
}