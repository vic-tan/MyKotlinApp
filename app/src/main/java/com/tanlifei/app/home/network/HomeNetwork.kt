package com.tanlifei.app.home.network

import com.tanlifei.app.common.config.UrlConst
import com.tanlifei.app.main.bean.AdsBean
import rxhttp.RxHttp
import rxhttp.toResponse


/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/1 11:14
 */
class HomeNetwork {

    companion object {
        private var network: HomeNetwork? = null
        fun getInstance(): HomeNetwork {
            if (network == null) {
                synchronized(HomeNetwork::class.java) {
                    if (network == null) {
                        network = HomeNetwork()
                    }
                }
            }
            return network!!
        }

    }
}