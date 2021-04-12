package com.onlineaginguniversity.login.bean

/**
 * @desc:密码登录返回结果
 * @author: tanlifei
 * @date: 2021/4/12 14:12
 */
data class PwdLoginResultBean(
    var status: Int = 0, var token: String? = null, var count: Int = 0,
    var expireTime: String? = null
) {
}