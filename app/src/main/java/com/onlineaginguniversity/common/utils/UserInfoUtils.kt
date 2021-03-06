package com.onlineaginguniversity.common.utils

import com.blankj.utilcode.util.ObjectUtils
import com.common.ComFun
import com.common.base.bean.UserBean
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
    fun getUser(): UserBean? {
        if (ObjectUtils.isEmpty(ComFun.mUser)) {
            ComFun.mUser = LitePal.findLast(UserBean::class.java)
        }
        return ComFun.mUser
    }

    /**
     * 获取用户Uid
     */
    fun getUid(): Long? {
        if (ObjectUtils.isEmpty(ComFun.mUser)) {
            ComFun.mUser = LitePal.findLast(UserBean::class.java)
        }
        return ComFun.mUser?.uid
    }

    /**
     * 获用户token
     */
    fun getToken(): String? {
        return if (ObjectUtils.isNotEmpty(ComFun.mToken)) {
            ComFun.mToken
        } else {
            getUser()?.token
        }
    }

    /**
     * 删除
     */
    fun clear() {
        LitePal.deleteAll(UserBean::class.java)
    }

    /**
     * 保存Token
     */
    fun saveToken(token: String) {
        LitePal.deleteAll(UserBean::class.java)
        var user = UserBean()
        user.token = token
        user.save()
    }
}