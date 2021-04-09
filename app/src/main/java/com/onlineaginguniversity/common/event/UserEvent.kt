package com.onlineaginguniversity.common.event

import com.common.base.bean.UserBean
import com.common.core.event.BaseEvent

/**
 * @desc:用户数据刷新
 * @author: tanlifei
 * @date: 2021/2/3 11:20
 */
data class UserEvent(val userBean: UserBean) : BaseEvent()