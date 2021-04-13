package com.onlineaginguniversity.login.bean

/**
 * @desc:微信授权登录返回结果
 * @author: tanlifei
 * @date: 2021/4/12 14:12
 */
data class WxLoginResultBean(
    var id: Long = 0, var uid: Long = 0,
    var openid: String? = null,var token: String? = null, var status: Int = 0
) {
}