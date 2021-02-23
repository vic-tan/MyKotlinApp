package com.tanlifei.app.common.utils

import com.blankj.utilcode.util.ObjectUtils
import com.common.ComApplication
import com.common.core.base.bean.UserBean
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
        if (ObjectUtils.isEmpty(ComApplication.user)) {
            ComApplication.user = LitePal.findLast(UserBean::class.java)
        }
        return ComApplication.user
    }

    /**
     * 获取用户Uid
     */
    fun getUid(): Long? {
        if (ObjectUtils.isEmpty(ComApplication.user)) {
            ComApplication.user = LitePal.findLast(UserBean::class.java)
        }
        return ComApplication.user?.id
    }

    /**
     * 获用户token
     */
    fun getToken(): String? {
        return if (ObjectUtils.isNotEmpty(ComApplication.token)) {
            ComApplication.token
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
        var user: UserBean =
            UserBean()
        user.token = token
        user.save()
    }
}