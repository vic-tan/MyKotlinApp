package com.tanlifei.app.classmatecircle.network

import com.common.cofing.constant.GlobalConst
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.common.config.api.ApiConst
import rxhttp.RxHttp
import rxhttp.toResponse

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 16:17
 */
class FollowNetwork {

    suspend fun requestFriendsEntertainmentList(pageNum: Int) =
        RxHttp.postJson(ApiConst.URL_FRIENDS_ENTERTAINMENT_LIST)
            .add(GlobalConst.Http.PAGE_NUM_KEY, pageNum)
            .add(GlobalConst.Http.PAGE_SIZE_kEY, GlobalConst.Http.PAGE_SIZE_VALUE)
            .toResponse<List<ClassmateCircleBean>>().await()

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