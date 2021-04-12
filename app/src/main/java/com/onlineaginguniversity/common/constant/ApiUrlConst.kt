package com.onlineaginguniversity.common.constant

import com.common.core.environment.utils.ApiEnvironmentConst

/**
 * @desc:接口定义
 * @author: tanlifei
 * @date: 2021/1/29 16:13
 */
object ApiUrlConst {

    /**—————————————————————————————————————————————————— H5地址  ——————————————————————————————————————————————*/

    /* 固定地址（各环境都一样） */
    private const val URL_AGREEMENT = "https://appoffice.jinlingkeji.cn/#/"

    /* 用户协议 */
    const val URL_USER_AGREEMENT = "${URL_AGREEMENT}xieyi"

    /* 隐私政策 */
    const val URL_PRIVATE_AGREEMENT = "${URL_AGREEMENT}yinsi"

    /* 学校领导协议 */
    const val URL_SCHOOL_LEADERS_AGREEMENT = "${URL_AGREEMENT}leader"

    /* 充值购买协议 */
    const val URL_RECHARGE_AGREEMENT = "${URL_AGREEMENT}payMode"

    /* 讲师入驻协议 */
    val URL_LECTURER_AGREEMENT = "${ApiEnvironmentConst.URL_BASE_HELPER}/lecturer/agreement"

    /* 讲师入驻入口 */
    val URL_LECTURER_ASKFOR = "${ApiEnvironmentConst.URL_BASE_HELPER}/rank/rankrule"


    /**—————————————————————————————————————————————————— 我的相关  ——————————————————————————————————————————————*/

    private const val URL_USER = "major/api/user/"

    /* 发送短信验证码 */
    const val URL_SEND_SMS = "${URL_USER}sendTextSms"

    /* 发送语音验证码 */
    const val URL_VOICE_SMS = "${URL_USER}sendVoiceSms"

    /* 发送登录短信验证码 */
    const val URL_LOGIN_SEND_SMS = "auth/sendsms"//后台未做统一，应该统一用上面URL_SEND_SMS接口，后台问题

    /* 发送登录语音验证码 */
    const val URL_LOGIN_VOICE_SMS = "auth/sendVoiceSms"//后台未做统一，应该统一用上面URL_VOICE_SMS口，后台问题

    /* 找回密码 */
    const val URL_RETRIEVE_PASSWORD = "${URL_USER}retrievePassword"


    /* 验证验证码登录 */
    const val URL_SMS_LOGIN = "auth/login"

    /* 密码登录 */
    const val URL_PWD_LOGIN = "auth/passwordLogin"

    /* 退出登录 */
    const val URL_LOGIN_OUT = "auth/loginOut"

    /* 操作手册列表 */
    const val URL_MANUAL_LIST = "major/api/manual/list"

    /* 操作手册详情 */
    const val URL_MANUAL_DETAIL = "major/api/manual/detail"

    /* 获取用户资料 */
    const val URL_USER_INFO = "${URL_USER}getUser"

    /* 获取合作大学存在的省区 */
    const val URL_UNIVERSITY_AREA_LIST = "${URL_USER}getAreaListByUniversity"

    /* 根据地区获取大学列表 */
    const val URL_UNIVERSITY_List = "${URL_USER}getUniversityList"


    /* 获取省市区JSON */
    const val URL_AREA_JSON = "${URL_USER}getAreaJson"

    /* 获取收货地址 */
    const val URL_GOODS_ADDRESS = "${URL_USER}getGoodsAddress"

    /* 新增收货地址 */
    const val URL_ADD_GOODS_ADDRESS = "${URL_USER}setGoodsAddress"

    /* 更新收货地址 */
    const val URL_UPDATE_GOODS_ADDRESS = "${URL_USER}updateGoodsAddress"

    /* 更新个人信息 */
    const val URL_UPDATE_USER = "${URL_USER}updateUser"

    /* 关注 */
    const val URL_FOLLOW = "${URL_USER}follow"

    /* 取消关注 */
    const val URL_CANCEL_FOLLOW = "${URL_USER}cancelFollow"


    /**—————————————————————————————————————————————————— 首页相关  ——————————————————————————————————————————————*/

    /* 首页顶部信息，banner,icon,新人礼包 */
    const val URL_HOME_BANNER = "major/api/index/getHomeTopInfo"


    /**—————————————————————————————————————————————————— 同学圈相关  ——————————————————————————————————————————————*/

    private const val URL_ENTERTAINMENT = "major/api/entertainment/"

    /* 关注人文娱列表 */
    const val URL_ENTERTAINMENT_LIST = "${URL_ENTERTAINMENT}getFriendsEntertainmentList"

    /* 文娱推荐列表 */
    const val URL_ENTERTAINMENT_LIST_BY_TYPE = "${URL_ENTERTAINMENT}getEntertainmentListByType"

    /* 获取文娱类型列表 */
    const val URL_ENTERTAINMENT_CATEGORY_LIST = "${URL_ENTERTAINMENT}getEntertainmentCategoryList"

    /* 文娱详情 */
    const val URL_ENTERTAINMENT_DETAIL = "${URL_ENTERTAINMENT}getEntertainmentDetail"

    /* 评论列表 */
    const val URL_ENTERTAINMENT_COMMENT_LIST = "${URL_ENTERTAINMENT}getCommentList"

    /* 发送评论回复 */
    const val URL_ENTERTAINMENT_SEND_COMMENT = "${URL_ENTERTAINMENT}comment"

    /* 删除评论 */
    const val URL_ENTERTAINMENT_DELETE_COMMENT = "${URL_ENTERTAINMENT}deleteComment"

    /* 获取文娱视频列表 */
    const val URL_ENTERTAINMENT_VIDEO_LIST = "${URL_ENTERTAINMENT}getEntertainmentVideo"

    /* 点赞或者取消 */
    const val URL_ENTERTAINMENT_PRAISE = "${URL_ENTERTAINMENT}starOrCancel"


    /**—————————————————————————————————————————————————— 其它相关  ——————————————————————————————————————————————*/

    /* 开屏广告 */
    const val URL_ADS = "message/api/ads/open/screen"

    /* 版本管理控制器 */
    const val URL_VERSION = "major/api/version/getVersion"

}