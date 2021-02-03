package com.tanlifei.app.common.bean

import com.common.base.bean.BaseLitePalBean

/**
 * @desc:用户信息
 * @author: tanlifei
 * @date: 2021/1/28 16:02
 */
class UserBean : BaseLitePalBean() {
    override val modelId: Long
        get() = userId
    var userId: Long = 0
}