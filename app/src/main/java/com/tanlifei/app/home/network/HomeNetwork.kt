package com.tanlifei.app.home.network


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