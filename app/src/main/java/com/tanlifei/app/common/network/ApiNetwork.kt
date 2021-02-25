package com.tanlifei.app.common.network

import com.blankj.utilcode.util.AppUtils
import com.common.cofing.constant.GlobalConst
import com.common.core.base.bean.UserBean
import com.common.core.bean.UpdateAppBean
import com.tanlifei.app.classmatecircle.bean.CategoryBean
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.common.config.api.ApiConst
import com.tanlifei.app.main.bean.AdsBean
import com.tanlifei.app.profile.bean.ManualBean
import rxhttp.RxHttp
import rxhttp.toResponse

/**
 * @desc:接口请求
 * @author: tanlifei
 * @date: 2021/2/7 16:17
 */
object ApiNetwork {

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
        .toResponse<MutableList<ManualBean>>().await()

    /**
     * 操作手册列表
     */
    suspend fun requestManualDetail(manualId: Long) = RxHttp.postJson(ApiConst.URL_MANUAL_DETAIL)
        .add("manualId", manualId)
        .toResponse<ManualBean>().await()

    /**—————————————————————————————————————————————————— 同学圈相关  ——————————————————————————————————————————————*/

    /**
     * 请求关注列表
     */
    suspend fun requestFriendsEntertainmentList(pageNum: Int) =
        RxHttp.postJson(ApiConst.URL_ENTERTAINMENT_LIST)
            .add(GlobalConst.Http.PAGE_NUM_KEY, pageNum)
            .add(GlobalConst.Http.PAGE_SIZE_kEY, GlobalConst.Http.PAGE_SIZE_VALUE)
            .toResponse<MutableList<ClassmateCircleBean>>().await()

    /**
     * 文娱推荐列表
     */
    suspend fun requestFriendsEntertainmentListByType(pageNum: Int, categoryId: Long) =
        RxHttp.postJson(ApiConst.URL_ENTERTAINMENT_LIST_BY_TYPE)
            .add(GlobalConst.Http.PAGE_NUM_KEY, pageNum)
            .add(GlobalConst.Http.PAGE_SIZE_kEY, GlobalConst.Http.PAGE_SIZE_VALUE)
            .add("categoryId", categoryId)
            .toResponse<MutableList<ClassmateCircleBean>>().await()

    /**
     * 请求推荐分类列表
     */
    suspend fun requestEntertainmentCategoryList() =
        RxHttp.postJson(ApiConst.URL_ENTERTAINMENT_CATEGORY_LIST)
            .toResponse<MutableList<CategoryBean>>().await()

    /**—————————————————————————————————————————————————— 其它相关  ——————————————————————————————————————————————*/

    /**
     * 广告接口请求
     */
    suspend fun requestAds() = RxHttp.postJson(ApiConst.URL_ADS)
        .toResponse<AdsBean>().await()

    /**
     * 获取用户资料
     */
    suspend fun requestVersion() = RxHttp.postJson(ApiConst.URL_VERSION)
        .add("systemType", 1)
        .add("appId", "com.onlineaginguniversity")
        .add("clientVersion", AppUtils.getAppVersionName())
        .toResponse<UpdateAppBean>().await()

}