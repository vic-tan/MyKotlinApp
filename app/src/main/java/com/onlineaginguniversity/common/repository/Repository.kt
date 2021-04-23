package com.onlineaginguniversity.common.repository

import com.blankj.utilcode.util.AppUtils
import com.common.base.bean.UpdateAppBean
import com.common.base.bean.UserBean
import com.common.constant.GlobalConst
import com.google.gson.Gson
import com.onlineaginguniversity.circle.bean.*
import com.onlineaginguniversity.common.widget.component.share.bean.ShareBean
import com.onlineaginguniversity.circle.utils.CommentUrlType
import com.onlineaginguniversity.common.bean.FollowResponse
import com.onlineaginguniversity.common.bean.PraiseResponse
import com.onlineaginguniversity.common.constant.ApiUrlConst
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.home.bean.HomeHeaderDataBean
import com.onlineaginguniversity.login.bean.PwdLoginResultBean
import com.onlineaginguniversity.login.bean.WxLoginResultBean
import com.onlineaginguniversity.login.utils.Base64Sink
import com.onlineaginguniversity.main.bean.AdsBean
import com.onlineaginguniversity.profile.bean.AddressBean
import com.onlineaginguniversity.profile.bean.AreaJsonBean
import com.onlineaginguniversity.profile.bean.ManualBean
import com.onlineaginguniversity.profile.bean.UniversityBean
import okio.Buffer
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
     * 获取短信验证码
     */
    suspend fun requestSMSCode(phone: String, type: EnumConst.SMSType): String {
        var map = HashMap<String, Any>()
        map["phone"] = phone
        //后台未做统一，统一后应该都为URL_SEND_SMS接，后台问题
        return when (type) {
            EnumConst.SMSType.MOBILE_LOGIN -> {
                RxHttp.get(ApiUrlConst.URL_LOGIN_SEND_SMS)
                    .addAll(map)
                    .toResponse<String>().await()
            }
            else -> {
                map["type"] = type.value
                RxHttp.postJson(ApiUrlConst.URL_SEND_SMS)
                    .addAll(map)
                    .toResponse<String>().await()
            }
        }

    }


    /**
     * 发送语音验证码
     */
    suspend fun requestVoiceCode(phone: String, type: EnumConst.SMSType): String {
        var map = HashMap<String, Any>()
        map["phone"] = phone
        //后台未做统一，统一后应该都为URL_VOICE_SMS，后台问题
        return when (type) {
            EnumConst.SMSType.MOBILE_LOGIN -> {
                RxHttp.get(ApiUrlConst.URL_LOGIN_VOICE_SMS)
                    .addAll(map)
                    .toResponse<String>().await()
            }
            else -> {
                map["type"] = type.value
                RxHttp.postJson(ApiUrlConst.URL_VOICE_SMS)
                    .addAll(map)
                    .toResponse<String>().await()
            }
        }

    }

    /**
     * 找回密码
     */
    suspend fun requestSetPwd(
        phone: String,
        code: String,
        pwd: String,
        type: EnumConst.SMSType
    ): String {
        //OKio base64
        val utf8Sink = Buffer().writeUtf8(pwd)
        val base64Buffer = Buffer()
        val base64Sink = Base64Sink(base64Buffer)
        base64Sink.write(utf8Sink, Long.MAX_VALUE)
        return RxHttp.postJson(ApiUrlConst.URL_RETRIEVE_PASSWORD)
            .add("phone", phone)
            .add("code", code)
            .add("password", base64Buffer.readUtf8())
            .add("type", type.value)
            .toResponse<String>().await()
    }


    /**
     * 一键登陆
     */
    suspend fun requestOneKeyLogin(accessToken: String) =
        RxHttp.get(ApiUrlConst.URL_ONE_KEY_LOGIN)
            .add("accessToken", accessToken)
            .toResponse<String>().await()


    /**
     * 微信授权登录
     */
    suspend fun requestWechatLogin(openid: String) =
        RxHttp.postJson(ApiUrlConst.URL_WECHAT_LOGIN)
            .add("openid", openid)
            .toResponse<WxLoginResultBean>().await()


    /**
     * 微信绑定用户关系
     */
    suspend fun requestBindPhoneLogin(openid: String) =
        RxHttp.postJson(ApiUrlConst.URL_BIND_PHONE_LOGIN)
            .add("openid", openid)
            .toResponse<String>().await()


    /**
     * 验证码登录
     */
    suspend fun requestSMSLogin(phone: String, code: String) = RxHttp.get(ApiUrlConst.URL_SMS_LOGIN)
        .add("phone", phone)
        .add("code", code)
        .toResponse<String>().await()


    /**
     * 密码登录
     */
    suspend fun requestPwdLogin(phone: String, pwd: String): PwdLoginResultBean {
        //OKio base64
        val utf8Sink = Buffer().writeUtf8(pwd)
        val base64Buffer = Buffer()
        val base64Sink = Base64Sink(base64Buffer)
        base64Sink.write(utf8Sink, Long.MAX_VALUE)

        return RxHttp.get(ApiUrlConst.URL_PWD_LOGIN)
            .add("phone", phone)
            .add("password", base64Buffer.readUtf8())
            .toResponse<PwdLoginResultBean>().await()
    }


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

    /**
     * 获取文娱类型列表
     */
    suspend fun requestEntertainmentCategory() =
        RxHttp.postJson(ApiUrlConst.URL_ENTERTAINMENT_CATEGORY)
            .toResponse<MutableList<CategoryBean>>().await()

    /**
     * 获取文娱话题标签列表
     */
    suspend fun requestEntertainmentTopic() =
        RxHttp.postJson(ApiUrlConst.URL_ENTERTAINMENT_TOPIC)
            .toResponse<MutableList<TopicTagBean>>().await()

    /**
     * 添加文娱
     */
    suspend fun requestEntertainmentAdd(
        content: String,
        mediaType: Int,
        categoryId: Long?,
        videoUrl: String?,
        entertainmentTopicId: Long?,
        uploadList: MutableList<ImageBean>
    ) = RxHttp.postJson(ApiUrlConst.URL_ENTERTAINMENT_ADD)
        .add("content", content)
        .add("mediaType", mediaType)
        .add("categoryId", categoryId)
        .add("videoUrl", videoUrl)
        .add("assetId", "")
        .add("entertainmentTopicId", entertainmentTopicId)
        .add("urls", uploadList)
        .toResponse<String>().await()


    /**
     * 删除文娱
     */
    suspend fun requestEntertainmentDelete(
        id: Long,
    ) = RxHttp.postJson(ApiUrlConst.URL_ENTERTAINMENT_DEL)
        .add("moduleId", 1)//文娱传1
        .add("businessId", id)
        .toResponse<String>().await()


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

    /**
     * 分享接口
     * 模块code
     * 1话题分享2直播专栏分享3文娱图片分享4课程分享5文娱视频分享6自己作业分享7名师分享8直播分享9回播分享10同学的作业
     * 11邀请好友12结课证书13专题分享14电台音频分享15学习周报
     */
    suspend fun requestShare(id: Long, moduleCode: EnumConst.ShareModuleCode) =
        RxHttp.postJson(ApiUrlConst.URL_SHARE)
            .add("code", moduleCode.value)
            .add("id", id)
            .toResponse<ShareBean>().await()


}