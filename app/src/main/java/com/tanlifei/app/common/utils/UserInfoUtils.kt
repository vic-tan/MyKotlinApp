package com.tanlifei.app.common.utils

import com.tanlifei.app.common.bean.UserBean
import org.litepal.LitePal

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/3 17:47
 */
object UserInfoUtils {
    /**
     * 获取用户信息
     */
    fun getUser(): UserBean? = LitePal.findLast(UserBean::class.java)

    /**
     * 获用户token
     */
    fun getToken(): String? = getUser()?.token
}