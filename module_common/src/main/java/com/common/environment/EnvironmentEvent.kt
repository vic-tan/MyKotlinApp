package com.common.environment

import com.common.base.event.BaseEvent

/**
 * @desc:切换了环境通知调用者
 * @author: tanlifei
 * @date: 2021/2/3 11:20
 */
class EnvironmentEvent(val environment: EnvironmentBean) : BaseEvent()