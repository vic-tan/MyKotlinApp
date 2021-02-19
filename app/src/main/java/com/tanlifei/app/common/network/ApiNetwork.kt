package com.tanlifei.app.common.network

import com.common.cofing.constant.GlobalConst
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.common.bean.ManualBean
import com.tanlifei.app.common.bean.UserBean
import com.tanlifei.app.common.config.api.ApiConst
import com.tanlifei.app.main.bean.AdsBean
import rxhttp.RxHttp
import rxhttp.toResponse

/**
 * @desc:接口请求
 * @author: tanlifei
 * @date: 2021/2/7 16:17
 */
class ApiNetwork {

    /**—————————————————————————————————————————————————— 我的相关  ——————————————————————————————————————————————*/

    /**
     * 获取验证码
     */
    suspend fun requestSmsCode(phone: String) = RxHttp.get(ApiConst.URL_SEND_SMS)
        .add("phone", phone)
        .toResponse<String>().await()

    /**
     * 登录
     */
    suspend fun requestLogin(phone: String, code: String) = RxHttp.get(ApiConst.URL_LOGIN)
        .add("phone", phone)
        .add("code", code)
        .toResponse<String>().await()


    /**
     * 退出登录
     */
    suspend fun requestLoginOut() = RxHttp.postJson(ApiConst.URL_LOGIN_OUT)
        .toResponse<String>().await()

    /**
     * 获取用户资料
     */
    suspend fun requestUserInfo() = RxHttp.postJson(ApiConst.URL_USER_INFO)
        .toResponse<UserBean>().await()

    /**
     * 操作手册列表
     */
    suspend fun requestManualList(pageNum: Int) = RxHttp.postJson(ApiConst.URL_MANUAL_LIST)
        .add(GlobalConst.Http.PAGE_NUM_KEY, pageNum)
        .add(GlobalConst.Http.PAGE_SIZE_kEY, GlobalConst.Http.PAGE_SIZE_VALUE)
        .toResponse<List<ManualBean>>().await()

    /**—————————————————————————————————————————————————— 同学圈相关  ——————————————————————————————————————————————*/

    /**
     * 请求关注列表
     */
    suspend fun requestFriendsEntertainmentList(pageNum: Int) =
        RxHttp.postJson(ApiConst.URL_FRIENDS_ENTERTAINMENT_LIST)
            .add(GlobalConst.Http.PAGE_NUM_KEY, pageNum)
            .add(GlobalConst.Http.PAGE_SIZE_kEY, GlobalConst.Http.PAGE_SIZE_VALUE)
            .toResponse<List<ClassmateCircleBean>>().await()

    /**—————————————————————————————————————————————————— 其它相关  ——————————————————————————————————————————————*/

    /**
     * 广告接口请求
     */
    suspend fun requestAds() = RxHttp.postJson(ApiConst.URL_ADS)
        .toResponse<AdsBean>().await()


    companion object {
        private var network: ApiNetwork? = null
        fun getInstance(): ApiNetwork {
            if (network == null) {
                synchronized(ApiNetwork::class.java) {
                    if (network == null) {
                        network = ApiNetwork()
                    }
                }
            }
            return network!!
        }

    }
}