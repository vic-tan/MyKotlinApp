package com.tanlifei.app.classmatecircle.network

import com.tanlifei.app.common.config.api.ApiConst
import rxhttp.RxHttp
import rxhttp.toResponse

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 16:17
 */
class FollowNetwork {


    suspend fun requestFriendsEntertainmentList() =
        RxHttp.postJson(ApiConst.URL_FRIENDS_ENTERTAINMENT_LIST)
            .add("pageNum", 1)
            .add("pageSize", 10)
            .toResponse<String>().await()

    companion object {
        private var network: FollowNetwork? = null
        fun getInstance(): FollowNetwork {
            if (network == null) {
                synchronized(FollowNetwork::class.java) {
                    if (network == null) {
                        network = FollowNetwork()
                    }
                }
            }
            return network!!
        }

    }
}