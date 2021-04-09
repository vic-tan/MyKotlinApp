package com.onlineaginguniversity.common.repository

import com.blankj.utilcode.util.AppUtils
import com.common.constant.GlobalConst
import com.common.base.bean.UserBean
import com.common.base.bean.UpdateAppBean
import com.onlineaginguniversity.circle.bean.CategoryBean
import com.onlineaginguniversity.circle.bean.CircleBean
import com.onlineaginguniversity.circle.bean.CommentBean
import com.onlineaginguniversity.circle.bean.ResponseCommentBean
import com.onlineaginguniversity.circle.utils.CommentUrlType
import com.onlineaginguniversity.common.bean.FollowResponse
import com.onlineaginguniversity.common.bean.PraiseResponse
import com.onlineaginguniversity.common.constant.ApiUrlConst
import com.onlineaginguniversity.home.bean.HomeHeaderDataBean
import com.onlineaginguniversity.main.bean.AdsBean
import com.onlineaginguniversity.profile.bean.AddressBean
import com.onlineaginguniversity.profile.bean.AreaJsonBean
import com.onlineaginguniversity.profile.bean.ManualBean
import com.onlineaginguniversity.profile.bean.UniversityBean
import rxhttp.RxHttp
import rxhttp.toResponse

/**
 * @desc:接口请求
 * @author: tanlifei
 * @date: 2021/2/7 16:17
 */
object Repository {

    /**—————————————————————————————————————————————————— 我的相关  ——————————————————————————————————————————————*/

    /**
     * 获取验证码
     */
    suspend fun requestSmsCode(phone: String) = RxHttp.get(ApiUrlConst.URL_SEND_SMS)
        .add("phone", phone)
        .toResponse<String>().await()

    /**
     * 登录
     */
    suspend fun requestLogin(phone: String, code: String) = RxHttp.get(ApiUrlConst.URL_LOGIN)
        .add("phone", phone)
        .add("code", code)
        .toResponse<String>().await()


    /**
     * 退出登录
     */
    suspend fun requestLoginOut() = RxHttp.postJson(ApiUrlConst.URL_LOGIN_OUT)
        .toResponse<String>().await()

    /**
     * 获取用户资料
     */
    suspend fun requestUserInfo() = RxHttp.postJson(ApiUrlConst.URL_USER_INFO)
        .toResponse<UserBean>().await()

    /**
     * 操作手册列表
     */
    suspend fun requestManualList(pageNum: Int) = RxHttp.postJson(ApiUrlConst.URL_MANUAL_LIST)
        .add(GlobalConst.Http.PAGE_NUM_KEY, pageNum)
        .add(GlobalConst.Http.PAGE_SIZE_kEY, GlobalConst.Http.PAGE_SIZE_VALUE)
        .toResponse<MutableList<ManualBean>>().await()

    /**
     * 操作手册列表
     */
    suspend fun requestManualDetail(manualId: Long) = RxHttp.postJson(ApiUrlConst.URL_MANUAL_DETAIL)
        .add("manualId", manualId)
        .toResponse<ManualBean>().await()

    /**
     * 获取收货地址
     */
    suspend fun requestGoodsAddress(id: Long) = RxHttp.postJson(ApiUrlConst.URL_GOODS_ADDRESS)
        .add("id", id)
        .toResponse<AddressBean>().await()

    /**
     * 获取合作大学存在的省区
     */
    suspend fun requestUniversityAreaList() = RxHttp.postJson(ApiUrlConst.URL_UNIVERSITY_AREA_LIST)
        .toResponse<MutableList<AreaJsonBean>>().await()


    /**
     * 根据地区获取大学列表
     */
    suspend fun requestUniversity(id: Long) = RxHttp.postJson(ApiUrlConst.URL_UNIVERSITY_List)
        .add("id", id)
        .toResponse<MutableList<UniversityBean>>().await()


    /**
     * 获取省市区JSON
     */
    suspend fun requestAreaJsonList() = RxHttp.postJson(ApiUrlConst.URL_AREA_JSON)
        .toResponse<MutableList<AreaJsonBean>>().await()


    /**
     * 更新收货地址
     *
     */
    suspend fun requestEidtGoodsAddress(add: Boolean, addressBean: AddressBean) =
        RxHttp.postJson(if (add) ApiUrlConst.URL_ADD_GOODS_ADDRESS else ApiUrlConst.URL_UPDATE_GOODS_ADDRESS)
            .add("id", addressBean.id)
            .add("username", addressBean.username)
            .add("mobile", addressBean.mobile)
            .add("provinceId", addressBean.provinceId)
            .add("cityId", addressBean.cityId)
            .add("areaId", addressBean.areaId)
            .add("addressPrefix", addressBean.addressPrefix)
            .add("address", addressBean.address)
            .add("email", addressBean.email)
            .toResponse<String>().await()

    /**
     * 更新个人信息
     */
    suspend fun requestUpdateUser(userBean: UserBean) = RxHttp.postJson(ApiUrlConst.URL_UPDATE_USER)
        .add("id", userBean.uid)
        .add("avatar", userBean.avatar)
        .add("nickname", userBean.nickname)
        .add("address", userBean.address)
        .add("university", userBean.university)
        .add("universityName", userBean.universityName)
        .add("gender", userBean.gender)
        .add("age", userBean.age)
        .add("goodsAddress", userBean.goodsAddress)
        .add("score", userBean.score)
        .add("areaId", userBean.areaId)
        .add("points", userBean.points)
        .add("mobile", userBean.mobile)
        .add("bio", userBean.bio)
        .add("email", userBean.email)
        .toResponse<String>().await()


    /**
     * 关注/取消关注
     */
    suspend fun requestFollow(id: Long, isFollow: Boolean) =
        RxHttp.postJson(if (isFollow) ApiUrlConst.URL_FOLLOW else ApiUrlConst.URL_CANCEL_FOLLOW)
            .add("followerUid", id)
            .toResponse<FollowResponse>().await()

    /**—————————————————————————————————————————————————— 首页相关  ——————————————————————————————————————————————*/

    /**
     * 首页顶部信息，banner,icon,新人礼包
     */
    suspend fun requestHomeBanner() =
        RxHttp.postJson(ApiUrlConst.URL_HOME_BANNER)
            .toResponse<HomeHeaderDataBean>().await()

    /**—————————————————————————————————————————————————— 同学圈相关  ——————————————————————————————————————————————*/

    /**
     * 请求关注列表
     */
    suspend fun requestFriendsEntertainmentList(pageNum: Int) =
        RxHttp.postJson(ApiUrlConst.URL_ENTERTAINMENT_LIST)
            .add(GlobalConst.Http.PAGE_NUM_KEY, pageNum)
            .add(GlobalConst.Http.PAGE_SIZE_kEY, GlobalConst.Http.PAGE_SIZE_VALUE)
            .toResponse<MutableList<CircleBean>>().await()

    /**
     * 文娱推荐列表
     */
    suspend fun requestFriendsEntertainmentListByType(pageNum: Int, categoryId: Long) =
        RxHttp.postJson(ApiUrlConst.URL_ENTERTAINMENT_LIST_BY_TYPE)
            .add(GlobalConst.Http.PAGE_NUM_KEY, pageNum)
            .add(GlobalConst.Http.PAGE_SIZE_kEY, GlobalConst.Http.PAGE_SIZE_VALUE)
            .add("categoryId", categoryId)
            .toResponse<MutableList<CircleBean>>().await()

    /**
     * 请求推荐分类列表
     */
    suspend fun requestEntertainmentCategoryList() =
        RxHttp.postJson(ApiUrlConst.URL_ENTERTAINMENT_CATEGORY_LIST)
            .toResponse<MutableList<CategoryBean>>().await()

    /**
     * 文娱详情
     */
    suspend fun requestEntertainmentDetail(id: Long) =
        RxHttp.postJson(ApiUrlConst.URL_ENTERTAINMENT_DETAIL)
            .add("publishId", id)
            .toResponse<CircleBean>().await()


    /**
     * 点赞或者取消
     */
    suspend fun requestPraise(id: Long, isPraise: Boolean) =
        RxHttp.postJson(ApiUrlConst.URL_ENTERTAINMENT_PRAISE)
            .add("publishId", id)
            .add("type", if (isPraise) 2 else 1)
            .toResponse<PraiseResponse>().await()


    /**
     * 评论列表
     */
    suspend fun requestCommentList(
        id: Long,
        pageNum: Int,
        urlType: CommentUrlType = CommentUrlType.CLASSMATE_CIRCLE
    ) =
        RxHttp.postJson(
            when (urlType) {
                CommentUrlType.CLASSMATE_CIRCLE -> ApiUrlConst.URL_ENTERTAINMENT_COMMENT_LIST
            }
        )
            .add(GlobalConst.Http.PAGE_NUM_KEY, pageNum)
            .add("publishId", id)
            .add(GlobalConst.Http.PAGE_SIZE_kEY, GlobalConst.Http.PAGE_SIZE_VALUE)
            .toResponse<MutableList<CommentBean>>().await()

    /**
     * 发送评论
     */
    suspend fun requestSendComment(
        id: Long,
        content: String,
        urlType: CommentUrlType = CommentUrlType.CLASSMATE_CIRCLE
    ) =
        RxHttp.postJson(
            when (urlType) {
                CommentUrlType.CLASSMATE_CIRCLE -> ApiUrlConst.URL_ENTERTAINMENT_SEND_COMMENT
            }
        )
            .add("publishId", id)
            .add("content", content)
            .toResponse<ResponseCommentBean>().await()

    /**
     * 删除评论
     */
    suspend fun requestDeleteComment(
        id: Long,
        urlType: CommentUrlType = CommentUrlType.CLASSMATE_CIRCLE
    ) =
        RxHttp.postJson(
            when (urlType) {
                CommentUrlType.CLASSMATE_CIRCLE -> ApiUrlConst.URL_ENTERTAINMENT_DELETE_COMMENT
            }
        )
            .add("commentId", id)
            .toResponse<String>().await()


    /**
     * 获取文娱视频列表
     */
    suspend fun requestVideoList(
        publishId: Long,
        mUserId: Long,
        pageNum: Int
    ): MutableList<CircleBean> {
        var map = HashMap<String, Any>()
        map[GlobalConst.Http.PAGE_NUM_KEY] = pageNum
        map[GlobalConst.Http.PAGE_SIZE_kEY] = GlobalConst.Http.PAGE_SIZE_VALUE
        if (publishId != -1L) {
            map["publishId"] = publishId
        }
        if (mUserId != -1L) {
            map["mUserId"] = mUserId
        }

        return RxHttp.postJson(ApiUrlConst.URL_ENTERTAINMENT_VIDEO_LIST)
            .addAll(map)
            .toResponse<MutableList<CircleBean>>().await()
    }


    /**—————————————————————————————————————————————————— 其它相关  ——————————————————————————————————————————————*/

    /**
     * 广告接口请求
     */
    suspend fun requestAds() = RxHttp.postJson(ApiUrlConst.URL_ADS)
        .toResponse<AdsBean>().await()

    /**
     * 获取用户资料
     */
    suspend fun requestVersion() = RxHttp.postJson(ApiUrlConst.URL_VERSION)
        .add("systemType", 1)
        .add("appId", AppUtils.getAppPackageName())
        .add("clientVersion", AppUtils.getAppVersionName())
        .toResponse<UpdateAppBean>().await()


}