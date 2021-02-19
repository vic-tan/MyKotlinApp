package com.tanlifei.app.common.config.api

import com.common.cofing.constant.ApiEnvironmentConst

/**
 * @desc:接口定义
 * @author: tanlifei
 * @date: 2021/1/29 16:13
 */
object ApiConst {

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

    /* 发送短信验证码 */
    const val URL_SEND_SMS = "auth/sendsms"

    /* 验证验证码登录 */
    const val URL_LOGIN = "auth/login"

    /* 退出登录 */
    const val URL_LOGIN_OUT = "auth/loginOut"

    /* 获取用户资料 */
    const val URL_USER_INFO = "major/api/user/getUser"

    /* 操作手册列表 */
    const val URL_MANUAL_LIST = "major/api/manual/list"

    /* 操作手册详情 */
    const val URL_MANUAL_DETAIL = "major/api/manual/detail"

    /**—————————————————————————————————————————————————— 同学圈相关  ——————————————————————————————————————————————*/

    /* 关注人文娱列表 */
    const val URL_FRIENDS_ENTERTAINMENT_LIST = "major/api/entertainment/getFriendsEntertainmentList"


    /**—————————————————————————————————————————————————— 其它相关  ——————————————————————————————————————————————*/

    /* 开屏广告 */
    const val URL_ADS = "message/api/ads/open/screen"

    /* 版本管理控制器 */
    const val URL_VERSION = "major/api/version/getVersion"

}