package com.tanlifei.app.main.network

import com.tanlifei.app.common.config.api.ApiUrlConst
import com.tanlifei.app.main.bean.AdsBean
import rxhttp.RxHttp
import rxhttp.toResponse


/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/1 11:14
 */
class SplashNetwork {

    /**
     * 广告接口请求
     */
    suspend fun requestAds() = RxHttp.postJson(ApiUrlConst.URL_ADS)
        .toResponse<AdsBean>().await()


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